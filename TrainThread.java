public class TrainThread extends Thread {
    private MBTA mbta;
    private String name;
    private Log log;

    TrainThread(MBTA mbta, String name, Log log) {
        this.mbta = mbta;
        this.name = name;
        this.log = log;
    }

    public void run() {
        // System.out.println("** train " + name + " starts");
        mbta.pass_lock.lock();

        // if no more passengers, interrupt
        while (!mbta.isEmpty()) {
            try {
                mbta.pass_c.signalAll();
                mbta.pass_lock.unlock();
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // otherwise run
            // stop for 0.5s

            // ATOMIC: then prepare to move
            mbta.train_lock.lock();

            int pos = mbta.getTrainPosition(name);
            if (pos == 0) {
                mbta.setTrainDirection(name, 1);
            } else if (pos == mbta.getLine(name).size() - 1) {
                mbta.setTrainDirection(name, -1);
            }

            int direct = mbta.getTrainDirection(name);
            String station = mbta.getTrainStation(name);
            String next_station = mbta.getLine(name).get(pos + direct);

            // if the next station is occupied, await
            while (mbta.getStationTrain(next_station) != null) {
                // if all set, wake up every train in await set
                if (mbta.isEmpty()) {
                    mbta.train_c.signalAll();
                    mbta.train_lock.unlock();
                    return;
                }

                try {
                    mbta.train_c.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // ATOMIC: move forward
            mbta.pass_lock.lock();

            mbta.releaseStation(station);
            log.train_moves(Train.make(name), Station.make(station), Station.make(next_station));
            mbta.setTrainPosition(name, pos + direct);
            mbta.setStationTrain(next_station, name);

            mbta.train_c.signalAll();
            mbta.train_lock.unlock();
        }
        mbta.pass_c.signalAll();
        mbta.pass_lock.unlock();

        System.out.println("--- Train " + name + " exits ---");

        return;
    }
}
