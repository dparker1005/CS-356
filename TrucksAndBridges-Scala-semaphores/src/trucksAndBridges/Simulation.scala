package trucksAndBridges

/**
 * @author davew
 */

// SCALA NOTE: I will use this form of comment to
//   describe important features of Scala; feel free
//   to ignore these comments if you already know Scala


object Simulation extends App {
 
  // SCALA NOTE: this Scala App object will be started when we
  //   right-click on this FILE and pick 'Run As-> Scala Application' in Eclipse
  // Note that the variables and statements of the App can just be listed
  //   within the body of the App object; indeed, this is the only way I can make it work
  // Also, stackoverflow.com is awesome.
  //   http://stackoverflow.com/questions/9079713/scala-app-val-initialization-in-main-method

  println("=== Welcome to the Trucks and Bridges Simulation (Scala Edition) ===")
  // set simulation parameters (see the end of the object defintion)
  val minStartingInterval = 0.5
  val startingIntervalVar = 2.0
  val waitUntilRace       = 1.0
  val verboseWaiting = false
  var dice = new java.util.Random()
  
  // runSequentialExample()  // see method definitions below
  runConcurrentExample()
  println("=== Done. I hope you have enjoyed the simulation ===")
  
  
  // The following two methods are the core ideas of this project:
  // Running a simulation of trucks crossing one bridge either
  //  sequentially or with multiple threads.
  def runSequentialExample(): Unit = {
    println("== Running sequential trucks example. ==")
    var B = new Bridge(100, 3, 2, 5)
    var T = new Truck("T", 100, 1, B)
    var U = new Truck("U", 100, 2, B)
    var V = new Truck("V",  80, 3, B)
    var W = new Truck("W", 200, 3, B)
    var X = new Truck("X", 100, 1, B)
    var Y = new Truck("Y",  80, 1, B)
    var Z = new Truck("Z", 100, 1, B)
    
    T.run(); tryToSleep(minStartingInterval, startingIntervalVar);
    U.run(); tryToSleep(minStartingInterval, startingIntervalVar);
    V.run(); tryToSleep(minStartingInterval, startingIntervalVar);
    W.run(); tryToSleep(minStartingInterval, startingIntervalVar);
    X.run(); tryToSleep(minStartingInterval, startingIntervalVar);
    Y.run(); tryToSleep(minStartingInterval, startingIntervalVar);
    Z.run(); tryToSleep(minStartingInterval, startingIntervalVar);

    tryToSleep(waitUntilRace, 0)
    B.startRace()
    println("And, they're off!")   

    if (B.availableLoad() != 3) {
      println("That's strange --- the bridge should once again be able to take 3 tonnes, but it says " + B.availableLoad())
    }
    println("== Sequential Trucks example is done now. ==")
  }
  
  def runConcurrentExample(): Unit = {
    println("== Starting concurrent trucks example. ==")
    var B = new Bridge(100, 3, 2, 5)
    var T = new Truck("T", 100, 1, B)
    var U = new Truck("U", 100, 2, B)
    var V = new Truck("V",  80, 3, B)
    var W = new Truck("W",  20, 3, B)
    var X = new Truck("X", 100, 1, B)
    var Y = new Truck("Y",  80, 1, B)
    var Z = new Truck("Z", 100, 1, B)

    T.start(); tryToSleep(minStartingInterval, startingIntervalVar);
    U.start(); tryToSleep(minStartingInterval, startingIntervalVar);
    V.start(); tryToSleep(minStartingInterval, startingIntervalVar);
    W.start(); tryToSleep(minStartingInterval, startingIntervalVar);
    X.start(); tryToSleep(minStartingInterval, startingIntervalVar);
    Y.start(); tryToSleep(minStartingInterval, startingIntervalVar);
    Z.start(); tryToSleep(minStartingInterval, startingIntervalVar);

    tryToSleep(waitUntilRace, 0)
    B.startRace()
    println("And, they're off!")   

    T.join()  // wait for T to be done
    U.join()
    V.join()
    W.join()
    X.join()
    Y.join()
    Z.join()

    if (B.availableLoad() != 3) {
      println("That's strange --- the bridge should once again be able to take 3 tonnes, but it says " + B.availableLoad())
    }
    println("== Concurrent trucks example is done now. ==")
  }
  
  // SCALA NOTE: the tryToSleep method can be called from any part of
  //    this program with the notation Simulation.tryToSleep(secMin, secVar)

  // tryToSleep will pause be at least minimumSec seconds,
  //     and try not to pause more than minimumSec + variationSec,
  //     but of course extra delay could happen anywhere at any time,
  //     and if something goes wrong (e.g., an interrupt) it could wake early

  def tryToSleep(minimumSec: Double, variationSec: Double): Unit = {
    val howLongInMS = Math.round(minimumSec*1000) + Math.round(dice.nextDouble()*(variationSec)*1000)
    if (verboseWaiting) print(s"*** waiting $howLongInMS ms ***")
    Thread.sleep(howLongInMS) // sleep uses milliSeconds, not Seconds
  }
}
