package trucksAndBridges

/**
 * @author davew
 */

// SCALA NOTE: "val" means we're creating a name for a value that won't change;
//             "var" means we're creating a name for a variable (which can change)

class Bridge (val length: Double,
              var weightLimit: Double,
              val beforeRace: Int,
              val duringRace: Int) {
  private var currentLoad = 0.0
  private var _raceFlag = new java.util.concurrent.Semaphore(beforeRace)
  def availableLoad(): Double = {
    weightLimit - currentLoad
  }
  def waitForPermission(): Unit = {
    _raceFlag.acquire(1)
  }
  def startRace(): Unit = {
    _raceFlag.release(duringRace)
  }

  // supportWhileDriving returns true if the truck made it, false if the bridge failed...
  def supportWhileDriving(t: Truck): Boolean = {
    if (currentLoad + t.weight > weightLimit) {
      println("Bridge load of " + weightLimit + " tonnes exceeded by total load of " + (currentLoad + t.weight))
      println(t.name+ ":  Aaaaaaaaaaaaaaaaa! (boom)");
      weightLimit = 0
      false  // return false, from the "if", and thus from the method
    } else {
      currentLoad = currentLoad + t.weight
      val timeToCross = length / 1000 / t.speed * 60 * 60
      Simulation.tryToSleep(timeToCross, timeToCross)
      currentLoad = currentLoad - t.weight
      true // return true
    }
  }
}