package jungle

/**
 * @author davew
 */
object Jungle extends App {
    //  A solution for Lab 3 should work (have no deadlock, livelock, or starvation)
    //    regardless of the settings of the configuration variables below,
    //    i.e., even if there are infinite apes going both ways.
    //  It should also work regardless of timing, so any values for the
    //    timing configuration should work, and there should be no way to
    //    add spurious "tryToSleep"'s *anywhere* to mess it up.
    //
    val eastBound = 10 // how many apes going East? use -1 for infinity
    val westBound = 10 // how many apes going West? use -1 for infinity
    val apeMin = 0.5  // 4 how long to wait between consecutive apes going one way
    val apeVar = 4.0  //  4 seconds is usually enough, but vary a bit to see what happens
    val sideMin = 5.0 // how long to wait before coming back across
    val sideVar = 0.0 //  5.0 seconds is usually enough 

    // allow some randomness in tryToSleep below, but initialize before any calls to it
    var dice = new java.util.Random()

    // create a Ladder
    var l = new Ladder(4)
    new Thread(new Runnable{ //This thread notation was borrowed from twitter.github.io/scala_school/concurrency.html
      def run(){l.waitForApe()}}).start()
    
    new Thread(new Runnable{ //This thread notation was borrowed from twitter.github.io/scala_school/concurrency.html
      def run(){
        // create some Eastbound apes who want that ladder
        var nRemaining = eastBound
        var apeCounter = 1
        while (nRemaining != 0) {
          var a = new Ape("E-"+apeCounter, l,true)
          a.start()
          apeCounter+=1
          tryToSleep(apeMin, apeVar)
          if (nRemaining > 0)
            nRemaining-=1
    }}}).start()

    new Thread(new Runnable{
       def run(){
         // and create some Westbound apes who want the SAME ladder
         var nRemaining = westBound
         var apeCounter=1
         while (nRemaining != 0) {
           var a = new Ape("W-"+apeCounter, l,false)
           a.start()
           apeCounter+=1
           tryToSleep(apeMin, apeVar)
           if (nRemaining > 0)
             nRemaining-=1
    }}}).start()

  // tryToSleep will pause be at least minimumSec seconds,
  //     and try not to pause more than minimumSec + variationSec,
  //     but of course extra delay could happen anywhere at any time,
  //     and if something goes wrong (e.g., an interrupt) it could wake early

  def tryToSleep(minimumSec: Double, variationSec: Double): Unit = {
    val howLongInMS = Math.round(minimumSec*1000) + Math.round(dice.nextDouble()*(variationSec)*1000)
    Thread.sleep(howLongInMS) // sleep uses milliSeconds, not Secondy
  }
}