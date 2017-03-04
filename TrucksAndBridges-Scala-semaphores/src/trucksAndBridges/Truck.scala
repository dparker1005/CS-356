package trucksAndBridges

/**
 * @author davew
 */


// SCALA NOTE: "val" means we're creating a name for a value that won't change;
//             "var" means we're creating a name for a variable (which can change)

class Truck (val name: String, val speed: Double, val weight: Double, val bridge: Bridge) extends Thread {
  val verboseWaiting = false

  override def run(): Unit = {
    println(name + ": Starting up, asking permission for bridge");
    bridge.waitForPermission();    
    println(name + ": Ready to go onto bridge");
   
    var keepWaiting = 64;  // wait for a while before giving up
    while (bridge.availableLoad() < weight && keepWaiting > 0) {
      Simulation.tryToSleep(0.1, 0.2);  // pause a small time to let other threads run
      if (verboseWaiting) println(name + ": waiting ...")
      keepWaiting = keepWaiting - 1;
    }

    if (keepWaiting == 0) { // gave up!
      println(name + ": giving up and going around!");
    } else {
      println(name + ": going onto bridge ");
      val stillAlive = bridge.supportWhileDriving(this);
      if (stillAlive) {
        println(name + ": now off of the bridge");
      } else {
        println(name + ": now off the bridge the hard way!");
      }
    }
  }
}