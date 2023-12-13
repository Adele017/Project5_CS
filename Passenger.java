import java.util.HashMap;
import java.util.Map;

public class Passenger extends Entity {
  private Passenger(String name) {
    super(name);
  }

  private int position = 0;
  private static Map<String, Passenger> passMap = new HashMap<>();

  public static void clear() {
    passMap.clear();
  }

  public static Passenger make(String name) {
    // Change this method!
    if (passMap.containsKey(name)) {
      return passMap.get(name);
    }
    Passenger pass = new Passenger(name);
    passMap.put(name, pass);
    return pass;
  }

  public int getPosition() {
    String name = toString();

    return Passenger.make(name).position;
  }
  public static void move(String name){
    Passenger.make(name).position += 1;
  }
  public void moveForward() {
    String name = toString();
    System.out.println("Verify: Passenger " + name + " deboards from " + position + " to " + (position + 1));
    move(name);
  }
}
