package jungle
import java.util.concurrent.Semaphore

/**
 * @author davew
 */
class Ladder (val nRungs: Integer) {  
  // Array of integer capacities, initialized to 1 available on each rung
  // thanks to Martin K's answer at http://stackoverflow.com/questions/3881013/array-initializing-in-scala
  private var rungCapacity = Array.fill[Integer](nRungs)(1)
  private var rungSemaphore = Array.fill[Semaphore](nRungs)(new Semaphore(1, true))
  private var apesOnLadders = 0
  private var eastSemaphore = new Semaphore(0, true)
  private var westSemaphore = new Semaphore(0, true)
  private var noDirection = true
  private var apesThatAreGoingToCross = 0

  // return True if you succeed in grabbing the rung
  def grabRung(which: Integer, headedEast: Boolean): Boolean = {
    if(headedEast){
      if(which==0){
        eastSemaphore.acquire(1)
        apesOnLadders += 1
        apesThatAreGoingToCross -= 1
      }
    }
    else{
      if(which==nRungs-1){
        westSemaphore.acquire(1)
        apesOnLadders += 1
        apesThatAreGoingToCross -= 1
      }
    }
  
    rungSemaphore(which).acquire(1)
    if (rungCapacity(which) < 1) {
      false
    } else {
      rungCapacity(which)-=1
      true
    }
  }
  
  def releaseRung(which: Integer, headedEast: Boolean): Unit = {
    rungCapacity(which)+=1
    rungSemaphore(which).release(1)
    if(headedEast){
      if(which==nRungs-1){
        this.synchronized{
          apesOnLadders -= 1
          if(apesOnLadders == 0 && eastSemaphore.availablePermits()==0 && apesThatAreGoingToCross == 0){
            if(westSemaphore.hasQueuedThreads()){
              westSemaphore.release(westSemaphore.getQueueLength())
              apesThatAreGoingToCross = westSemaphore.getQueueLength()
              System.out.println("----"+westSemaphore.getQueueLength()+" Going West----")
            }
            else if(eastSemaphore.hasQueuedThreads()){
               eastSemaphore.release(eastSemaphore.getQueueLength)
               apesThatAreGoingToCross = eastSemaphore.getQueueLength()
               System.out.println("----"+eastSemaphore.getQueueLength()+" Going East----")
             }
             else{
               noDirection = true
               waitForApe()
             }
           }
        }
      }
    }
    else{
      if(which==0){
        this.synchronized{
          apesOnLadders -= 1
          if(apesOnLadders == 0 && westSemaphore.availablePermits()==0 && apesThatAreGoingToCross == 0){
            if(eastSemaphore.hasQueuedThreads()){
              eastSemaphore.release(eastSemaphore.getQueueLength())
              apesThatAreGoingToCross = eastSemaphore.getQueueLength()
              System.out.println("----"+eastSemaphore.getQueueLength()+" Going East----")
            }
            else if(westSemaphore.hasQueuedThreads()){
              westSemaphore.release(westSemaphore.getQueueLength)
              apesThatAreGoingToCross = westSemaphore.getQueueLength()
              System.out.println("----"+westSemaphore.getQueueLength()+" Going West----")
            }
            else{
              noDirection = true
              new Thread(new Runnable{ //This thread notation was borrowed from twitter.github.io/scala_school/concurrency.html
                def run(){waitForApe()}}).start()
            }
          }
        }
      }
    }
  }
  
  def waitForApe(): Unit = {
    System.out.println("----Waiting For Apes----")
    //Dave verbally said that I can have one thing that busy-waits
    while(noDirection){
      if(eastSemaphore.hasQueuedThreads()){
        this.synchronized{
          System.out.println("----"+eastSemaphore.getQueueLength()+" Going East----")
          eastSemaphore.release(eastSemaphore.getQueueLength())
          apesThatAreGoingToCross = eastSemaphore.getQueueLength()
          noDirection = false
        }
      }
      else if(westSemaphore.hasQueuedThreads()){
        this.synchronized{
          System.out.println("----"+westSemaphore.getQueueLength()+" Going West----")
          westSemaphore.release(westSemaphore.getQueueLength())
          apesThatAreGoingToCross = westSemaphore.getQueueLength()
          noDirection = false
        }
        
      }
    }
  }
}