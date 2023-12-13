import java.util.*;

public class BoardEvent implements Event {
  public final Passenger p;
  public final Train t;
  public final Station s;

  public BoardEvent(Passenger p, Train t, Station s) {
    this.p = p;
    this.t = t;
    this.s = s;
  }

  public boolean equals(Object o) {
    if (o instanceof BoardEvent e) {
      return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
    }
    return false;
  }

  public int hashCode() {
    return Objects.hash(p, t, s);
  }

  public String toString() {
    return "Passenger " + p + " boards " + t + " at " + s;
  }

  public List<String> toStringList() {
    return List.of(p.toString(), t.toString(), s.toString());
  }

  public void replayAndCheck(MBTA mbta) {
    String tStation = mbta.getTrainStation(t.toString());
    String pStation = mbta.getPassStation(p.toString());

    if (!tStation.equals(pStation) || !tStation.equals(s.toString())) {
      System.out.println("** Invalid Board of "+p.toString()+" when: passenger at "+mbta.getPassPosition(p.toString())+":  "+pStation+", train at "+mbta.getTrainPosition(t.toString())+": "+tStation+", station is "+s.toString());
      throw new UnsupportedOperationException();
    }
  }
}
