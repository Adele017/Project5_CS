import static org.junit.Assert.*;

import java.util.List;

import org.junit.*;

public class Tests {
  @Test
  public void testPass() {
    assertTrue("true should be true", true);
  }

  @Test
  public void manualTest() {
    MBTA mbta = new MBTA();
    Train train = Train.make("sampleTrain");
    Passenger passenger = Passenger.make("samplePass");
    Log log = new Log();

    // make stations
    Station s_a = Station.make("A Station");
    Station s_b = Station.make("B Station");
    Station s_c = Station.make("C Station");
    Station s_d = Station.make("D Station");
    Station s_e = Station.make("E Station");

    // make line route
    List<String> route = List.of("A Station", "B Station", "C Station", "D Station", "E Station");
    mbta.addLine("sampleTrain", route);

    List<String> tour = List.of("B Station", "D Station");
    mbta.addJourney("samplePass", tour);
    mbta.printConfig("After manual construction:");

    log.train_moves(train, s_a, s_b);
    log.passenger_boards(passenger, train, s_b);
    log.train_moves(train, s_b, s_c);
    log.train_moves(train, s_c, s_d);
    log.passenger_deboards(passenger, train, s_d);
    log.train_moves(train, s_d, s_e);
    Verify.verify(mbta, log);
  }

  @Test
  public void sampleTest(){
    MBTA mbta = new MBTA();
    mbta.loadConfig("test_input.json");
    // mbta.printConfig("After load <test_input.json> file");

    // make stations
    Station s_a = Station.make("A Station");
    Station s_b = Station.make("B Station");
    Station s_c = Station.make("C Station");
    Station s_d = Station.make("D Station");
    Station s_e = Station.make("E Station");

    Log log = new Log();
    Train train = Train.make("Red");
    Passenger passenger = Passenger.make("Lily");

    log.train_moves(train, s_a, s_b);
    log.passenger_boards(passenger, train, s_b);
    log.train_moves(train, s_b, s_c);
    log.train_moves(train, s_c, s_d);
    log.passenger_deboards(passenger, train, s_d);
    log.train_moves(train, s_d, s_e);
    // Verify.verify(mbta, log);
  }
}
