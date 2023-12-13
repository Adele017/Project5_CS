import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import com.google.gson.*;

// serialize & sesirialize json strings
public class C {
  private Map<String, List<String>> lines;
  private Map<String, List<String>> trips;
  public List<String> l = new ArrayList<>();
  public Map<String, String> m=new HashMap<>();

  public static void main(String[] args) throws IOException {
    Gson gson = new Gson();
    C c = new C();
    c.lines = Map.of("k1", List.of("k1v1","k1v2"), "k2",List.of("k2v2"));
    c.l = List.of("a", "b", "c");
    c.m = Map.of("k1", "v1", "k2", "v2");
    String s = gson.toJson(c);
    System.out.println("s is:\n"+s);

    Reader rder = new FileReader("./sample.json");
    // int line = rder.readLine();
    // while(line!=null){
    //   System.out.println(line);
    // }
    C c2 = gson.fromJson(rder, C.class);
    // C c2 = gson.fromJson(s, C.class);
    // System.out.println("lines type: " + c2.lines.getClass() + ",\nm type: " + c2.trips.getClass());
    System.out.println(c2.lines);
    System.out.println(c2.trips);
    System.out.println(c2.l);
    System.out.println(c2.m);
    rder.close();

  }
}