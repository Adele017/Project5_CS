import java.util.HashMap;
import java.util.Map;


public class Station extends Entity {
  private Station(String name) {
    super(name);
  }

  private boolean occupied = false;
  public boolean isOccupied() {
    return occupied;
  }

  public void setOccupied(boolean occupied) {
    String name = toString();
    Station.make(name).occupied = occupied;
  }

  private static Map<String, Station> statnMap = new HashMap<>();
  public static void clear(){
    statnMap.clear();
  }

  public static Station make(String name) {
    // Change this method!
    // Change this method!
    if (statnMap.containsKey(name)) {
      return statnMap.get(name);
    }
    Station statn = new Station(name);
    statnMap.put(name, statn);
    return statn;
  }
}
