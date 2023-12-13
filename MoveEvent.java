import java.util.*;

public class MoveEvent implements Event {
  public final Train t;
  public final Station s1, s2;

  public MoveEvent(Train t, Station s1, Station s2) {
    this.t = t;
    this.s1 = s1;
    this.s2 = s2;
  }

  public boolean equals(Object o) {
    if (o instanceof MoveEvent e) {
      return t.equals(e.t) && s1.equals(e.s1) && s2.equals(e.s2);
    }
    return false;
  }

  public int hashCode() {
    return Objects.hash(t, s1, s2);
  }

  public String toString() {
    return "Train " + t + " moves from " + s1 + " to " + s2;
  }

  public List<String> toStringList() {
    return List.of(t.toString(), s1.toString(), s2.toString());
  }

  public void replayAndCheck(MBTA mbta) {
    // check s1
    String train = t.toString();
    String station2 = s2.toString();
    List<String> tLine = mbta.getLine(train);
    int tPos = mbta.getTrainPosition(train);
    if (!tLine.get(tPos).equals(s1.toString())) {
      System.out.println("** Invalid Move of "+train+" to "+station2+" since this stop "+tPos+" is "+tLine.get(tPos)+", not "+s1.toString()+"!");
      throw new UnsupportedOperationException();
    }

    // check s2
    if (tPos == 0) {
      mbta.setTrainDirection(train, 1);
    } else if (tPos == tLine.size() - 1) {
      mbta.setTrainDirection(train, -1);
    }
    int tDirect = mbta.getTrainDirection(train);

    // s2 not occupied & s2 is next station in config
    if (mbta.getStationTrain(station2)!=null || (!tLine.get(tPos+tDirect).equals(station2))) {
      System.out.println(station2+" occupied: "+mbta.getStationTrain(station2));
      System.out.println("** Invalid Move of "+train+" from "+s1.toString()+" since next stop "+(tPos+tDirect)+" is "+tLine.get(tPos+tDirect)+", not "+station2+"!");
      throw new UnsupportedOperationException();
    }

    // move & set occupied
    mbta.releaseStation(s1.toString());
    mbta.setTrainPosition(train, tPos+tDirect);
    mbta.setStationTrain(station2, train);
    System.out.println("Verify: Train "+train+" moves from "+tPos+" to "+(tPos+tDirect));

  }
}
