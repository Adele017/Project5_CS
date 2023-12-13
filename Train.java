import java.util.HashMap;
import java.util.Map;

public class Train extends Entity {
  private int direction = 1; // forward 1, backward -1
  private int position = 0;
  private Train(String name) {
    super(name);
  }

  private static Map<String, Train> trainMap = new HashMap<>();
  public static void clear(){
    trainMap.clear();
  }

  public static Train make(String name) {
    // Change this method!
    if (trainMap.containsKey(name)) {
      return trainMap.get(name);
    }
    Train train = new Train(name);
    trainMap.put(name, train);
    return train;
  }

  public int getPosition(){
    String name = toString();
    return Train.make(name).position;
  }
  public void set_direction(int direction){
    String name = toString();
    Train.make(name).direction = direction;
  }
  public void moveForward(){
    String name = toString();
    System.out.println("Verify: Train "+name+" moves from "+position+" to "+(position+direction));
    Train.make(name).position += Train.make(name).direction;
  }
}
