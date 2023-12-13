# CS121@Tufts Project 5 Concurrency

In this project, I implement a multi-threaded simulation of the T (or MBTA, the subway system in Boston). In the simulation, passengers will ride trains between stations, boarding and deboarding (getting on and off) to complete their journey. The simulation will generate a log showing the movements of passengers and trains, for example:

```bash
$ ./sim sample.json
Passenger Alice boards red at Davis
Passenger Bob boards green at Park
Train green moves from Park to Government Center
Train red moves from Davis to Harvard
Train red moves from Harvard to Kendall
Train green moves from Government Center to North Station
Train green moves from North Station to Lechmere
Train green moves from Lechmere to East Sommerville
Passenger Alice deboards red at Kendall
Train green moves from East Sommerville to Tufts
Passenger Bob deboards green at Tufts
```

## Building & Running the Code
The project uses JSON (with package [Gson](https://github.com/google/gson) included, see [doc](https://www.javadoc.io/doc/com.google.code.gson/gson/latest/com.google.gson/com/google/gson/Gson.html) here.)

To run the simulation, there are several scripts provided with the following usages:

`./build` - run `javac *.java` with Gson and JUnit in classpath
Always remember to `build` to compile after making any modifications!
`./sim config_filename` - run `java Sim config_filename` with Gson in classpath
`./verify config_filename log_filename` - run `java Verify config_filename log_filename` with Gson in classpath
`./test test_classname` - run `java org.junit.runner.JUnitCore test_classname` with Gson and Junit in classpath

For any other self-written java files, (e.g. `C.java` here foe example) you can use command like the ones in `run_c` to compile and run them.
(Attention: my environment is MacOS Big Sur. The code works in both VS Code, IntelliJ Idea CE)


## Project Design
The key part is concurrency mostly in `Sim.java` with helper functions in `MBTA.java`. More details of the design are well described in `design.md`.