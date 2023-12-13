import java.io.UncheckedIOException;
import java.nio.channels.UnsupportedAddressTypeException;
import java.util.*;

public class DeboardEvent implements Event {
  public final Passenger p;
  public final Train t;
  public final Station s;

  public DeboardEvent(Passenger p, Train t, Station s) {
    this.p = p;
    this.t = t;
    this.s = s;
  }

  public boolean equals(Object o) {
    if (o instanceof DeboardEvent e) {
      return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
    }
    return false;
  }

  public int hashCode() {
    return Objects.hash(p, t, s);
  }

  public String toString() {
    return "Passenger " + p + " deboards " + t + " at " + s;
  }

  public List<String> toStringList() {
    return List.of(p.toString(), t.toString(), s.toString());
  }

  public void replayAndCheck(MBTA mbta) {
    String tStation = mbta.getTrainStation(t.toString());
    int pPos = mbta.getPassPosition(p.toString());
    String pStation = mbta.getTrip(p.toString()).get(pPos+1);

    if (!tStation.equals(pStation) || !tStation.equals(s.toString())) {
      System.out.println("** Invalid Deboard of "+p.toString()+" when: passenger at "+(pPos+1)+" :  "+pStation+", train at "+mbta.getTrainPosition(t.toString())+" : "+tStation+", but station is "+s.toString());
      throw new UnsupportedAddressTypeException();
    }
    p.moveForward();
    mbta.setPassengerPosition(p.toString(), pPos+1);

  }
}
