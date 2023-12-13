# Write down your multi-threading designs here!
I created 2 `class extends Thread`, `PassengerThread` and `TrainThread` respectively. Each passenger and train is coordinated with a subclass of `Thread`.

And I put almost everything in `MBTA.java` and assign private fields `mbta`, `log` to each of the class so that they can access the shared memory and modify them through `synchronized method` in `Log` and `MBTA`.

There are 3 locks in total here:
- lock named by object mbta, used automatically in `synchronized` methods in mbta objects
- `pass_lock` & `train_lock`, for passengers and trains respectively.
  - `train_lock` is used to make sure the atomicity of each train movement:
    - Each train stops for 500ms, then attempts to move forward in an atomic section as following:
    - acquires the lock
    - check if the next station is occupied:
      - if so:
        - `await()` until the station is released.
      - otherwise:
        - move forward and release this `train_lock`.
  - `pass_lock` is used to schedule between trains and passengers, and to make sure each passenger operation(board/deboard) is atomic:
    - Each train acquires this lock at first, then when it stops, `notifyAll()` and `unlock()` to wake all passenger threads up so that they can board & deboard during the trains' stop. During which passengers also use this lock to achieve atomic operation of boading/deboarding. Then the train check if it's OK to move --
    - if so: train thread lock `pass_lock` again and move forward.
      - in this case, no passengers may board or deboard during the trains running.
    - if not: `await()` until it's ready to move.