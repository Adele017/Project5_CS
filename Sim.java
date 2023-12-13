import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sim {

  public static void run_sim(MBTA mbta, Log log) {
    // if(true){
    //   System.out.println("skip run_sim!");
    //   throw new OutOfMemoryError();
    // }
    // create all train & passenger threads
    Map<String, Thread> threads = new HashMap<>();

    // trains
    for(String train: mbta.getTrains()){
      threads.put(train, new TrainThread(mbta, train, log));
    }

    // passengers
    for(String pass: mbta.getPassengers()){
      threads.put(pass, new PassengerThread(mbta, pass, log));
    }

    // start running trains & passengers
    for(Thread t: threads.values()){
      t.start();
    }

    try {
      // join main with all threads
      for(Thread t: threads.values()){
        t.join();
      }
    }catch (Exception e){
      e.printStackTrace();
    }

    System.out.println("**** run_sim function exits! ****");
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("usage: ./sim <config file>");
      System.exit(1);
    }

    MBTA mbta = new MBTA();
    mbta.loadConfig(args[0]);

    Log log = new Log();

    run_sim(mbta, log);

    String s = new LogJson(log).toJson();
    PrintWriter out = new PrintWriter("log.json");
    out.print(s);
    out.close();

    mbta.reset();
    mbta.loadConfig(args[0]);
    Verify.verify(mbta, log);
  }
}
