import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.gson.Gson;

public class MBTA {

  // for Part2: verification
  private class Config {
    private Map<String, List<String>> lines = new HashMap<>();
    private Map<String, List<String>> trips = new HashMap<>();
  }

  private Config config = new Config();

  // Creates an initially empty simulation
  public MBTA() {
  }

  // for Part3: simulation
  private Map<String, String> stationTrain = new HashMap<>();
  private Map<String, Integer> trainPosition = new HashMap<>();
  private Map<String, Integer> trainDirection = new HashMap<>();
  private Map<String, Integer> passengerPosition = new HashMap<>();

  public Lock train_lock = new ReentrantLock();
  public Condition train_c = train_lock.newCondition();

  public Lock pass_lock = new ReentrantLock();
  public Condition pass_c = pass_lock.newCondition();

  // Adds a new transit line with given name and stations
  public void addLine(String name, List<String> stations) {
    config.lines.put(name, stations);
    Train.make(name);
    setTrainDirection(name, 1);
    setTrainPosition(name, 0);

    // set stations
    int i = 0;
    List<String> line = getLine(name);

    setStationTrain(line.get(i), name);
    Station.make(line.get(i));
    while (++i < line.size()) {
      setStationTrain(line.get(i), null);
      Station.make(line.get(i));
    }
    printConfig("** After addLine for " + name);
  }

  // Adds a new planned journey to the simulation
  public void addJourney(String name, List<String> stations) {
    config.trips.put(name, stations);
    Passenger.make(name);
    setPassengerPosition(name, 0);

    for (String station : getTrip(name)) {
      Station.make(station);
    }
    printConfig("** After addJourney for " + name);
  }

  // Return normally if initial simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkStart() {
    // check for trains' initial pos
    for (String trainName : config.lines.keySet()) {
      if (getTrainPosition(trainName) != 0) {
        throw new UnsupportedOperationException();
      }
    }

    // check for passengers' initial pos
    for (String passName : config.trips.keySet()) {
      if (getPassPosition(passName) != 0) {
        throw new UnsupportedOperationException();
      }
    }
  }

  // Return normally if final simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkEnd() {
    // check for passengers' final pos
    for (String passName : config.trips.keySet()) {
      if (getPassPosition(passName) != getTrip(passName).size() - 1) {
        System.out.println(
            "invalid End! " + passName + " at " + getPassPosition(passName) + " != " + (getTrip(passName).size() - 1));
        throw new UnsupportedOperationException();
      }
    }
  }

  // reset to an empty simulation
  public void reset() {
    config.lines.clear();
    config.trips.clear();
    Passenger.clear();
    Train.clear();
    Station.clear();

    stationTrain.clear();
    trainDirection.clear();
    trainPosition.clear();
    passengerPosition.clear();
  }

  // adds simulation configuration from a file
  public void loadConfig(String filename) {
    Gson gson = new Gson();
    Reader rder;
    try {
      rder = new FileReader(filename);
      config = gson.fromJson(rder, Config.class);
      for (String trainName : config.lines.keySet()) {
        Train.make(trainName);
        setTrainDirection(trainName, 1);
        setTrainPosition(trainName, 0);

        // set stations
        int i = 0;
        List<String> line = getLine(trainName);

        setStationTrain(line.get(i), trainName);
        Station.make(line.get(i));
        while (++i < line.size()) {
          setStationTrain(line.get(i), null);
          Station.make(line.get(i));
        }
      }
      for (String passName : config.trips.keySet()) {
        Passenger.make(passName);
        setPassengerPosition(passName, 0);
      }

      printConfig("*** After Load JSON file: " + filename);
      rder.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // helper function bellow
  public List<String> getTrains() {
    return config.lines.keySet().stream().toList();
  }

  public List<String> getPassengers() {
    return config.trips.keySet().stream().toList();
  }

  public List<String> getStations() {
    return stationTrain.keySet().stream().toList();
  }

  /**
   * get List<String> Line stations by String trainName
   * @param trainName
   * @return List<String>
   */
  public List<String> getLine(String trainName) {
    if (!config.lines.containsKey(trainName)) {
      System.out.println("*** No Train Name " + trainName + " in MBTA lines: " + config.lines.keySet().toString());
      throw new UnsupportedOperationException();
    }
    return config.lines.get(trainName);
  }

  /**
   * get List<String> Trip stations by String passenger Name
   * @param passName
   * @return List<String>
   */
  public List<String> getTrip(String passName) {
    if (!config.trips.containsKey(passName)) {
      System.out.println("*** No Train Name " + passName + " in MBTA lines: " + config.trips.keySet().toString());
      throw new UnsupportedOperationException();
    }
    return config.trips.get(passName);
  }

  public void printConfig(String prefix) {
    System.out.println(prefix);
    System.out.println(new Gson().toJson(config));
    // System.out.println("{\"lines\":\n "+config.lines);
    // System.out.println("\"trips\":\n "+config.trips+"}");
  }

  /////////////// bellow for part 3 ////////////////////////////
  public int getTrainPosition(String train) {
    return trainPosition.get(train).intValue();
  }

  public int getTrainDirection(String train) {
    return trainDirection.get(train).intValue();
  }

  public String getTrainStation(String trainName) {
    return config.lines.get(trainName).get(getTrainPosition(trainName));
  }

  public synchronized void setTrainPosition(String train, Integer position) {
    trainPosition.put(train, position);
  }

  public synchronized void setTrainDirection(String train, Integer direction) {
    trainDirection.put(train, direction);
  }

  public int getPassPosition(String passName) {
    return passengerPosition.get(passName).intValue();
  }

  public String getPassStation(String passName) {
    return config.trips.get(passName).get(getPassPosition(passName));
  }

  public synchronized void setPassengerPosition(String passenger, Integer position) {
    passengerPosition.put(passenger, position);
  }

  public synchronized void removePassenger(String passName) {
    passengerPosition.remove(passName);
  }

  public boolean isEmpty() {
    return passengerPosition.isEmpty();
  }

  ///////// Station
  public String getStationTrain(String stationName) {
    return stationTrain.get(stationName);
  }

  public synchronized void setStationTrain(String station, String train) {
    if (stationTrain.keySet().contains(station) && stationTrain.get(station) != null) {
      System.out.println("** unable to set: station " + station + " occupied by train " + stationTrain.get(station));
      return;
    }
    stationTrain.put(station, train);
    // System.out.println("** put train at station: "+train+", "+station);
  }

  public synchronized void releaseStation(String station) {
    stationTrain.put(station, null);
  }

}
