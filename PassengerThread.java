import java.util.ArrayList;
import java.util.List;

public class PassengerThread extends Thread {
    private MBTA mbta;
    private String name;
    private Log log;
    private String onBoard = null;

    PassengerThread(MBTA mbta, String name, Log log) {
        this.mbta = mbta;
        this.name = name;
        this.log = log;
    }

    public void run() {
        while (true) {
            // if arrived at terminal, exit -> written in decoard part
            int pos = mbta.getPassPosition(name);
            List<String> trips = mbta.getTrip(name);
            String station = mbta.getPassStation(name);
            String nextStation = trips.get(pos + 1);

            // switch current position
            if (onBoard != null) {
                // ATOMIC: if on a train, wait to deboard
                mbta.pass_lock.lock();
                // System.out.println("** passenger " + name + " gets the pass_lock!");

                while (!mbta.getTrainStation(onBoard).equals(nextStation)) {
                    try {
                        // System.out.println( "** passenger " + name + " releases the pass_lock,
                        // awaiting on train " + onBoard + "!");
                        mbta.pass_c.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                // ATOMIC: log
                log.passenger_deboards(Passenger.make(name), Train.make(onBoard), Station.make(nextStation));

                // if terminal, kill itself
                if (pos == trips.size() - 2) {
                    mbta.removePassenger(name);
                    System.out.println("*** Passenger " + name + " leaves MBTA ***");

                    // interrupt();
                    mbta.pass_c.signalAll();
                    mbta.pass_lock.unlock();

                    return;
                }

                // otherwise deboard and wait
                onBoard = null;
                mbta.setPassengerPosition(name, pos + 1);
                mbta.pass_c.signalAll();
                // System.out.println("** passenger " + name + " releases the pass_lock when
                // deboarding at " + nextStation + "!");
                mbta.pass_lock.unlock();
            } else {
                // if in station, wait to board
                mbta.pass_lock.lock();
                // System.out.println("** passenger " + name + " gets the pass_lock!");

                // find "next train plans" for the passenger
                List<String> trainPlan = new ArrayList<>();
                // if need to switch
                for (String t : mbta.getTrains()) {
                    // if arrive, check if
                    List<String> line = mbta.getLine(t);
                    if (line.contains(station) && line.contains(nextStation)) {
                        trainPlan.add(t);
                    }

                }

                // if no "planned train" arrive, await
                while (!trainPlan.contains(mbta.getStationTrain(station))) {
                    try {
                        // System.out.println("** passenger " + name + " releases the pass_lock,
                        // awaiting at station "+ station + " for " + trainPlan.toString() + "!");
                        mbta.pass_c.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                // otherwise board
                onBoard = mbta.getStationTrain(station);
                log.passenger_boards(Passenger.make(name), Train.make(onBoard), Station.make(station));

                mbta.pass_c.signalAll();
                // System.out.println("** passenger " + name + " releases the pass_lock, after
                // boarding " + onBoard + "!");
                mbta.pass_lock.unlock();
            }
        }
    }
}
