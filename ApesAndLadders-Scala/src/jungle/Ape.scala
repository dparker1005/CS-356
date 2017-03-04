package jungle

/**
 * @author davew
 */
class Ape(val name: String, private var _ladderToCross: Ladder, val goingEast: Boolean) extends Thread {
  override def run(): Unit = {
    val debug=true;  // "static" is shared by all Apes
    val rungDelayMin=0.8;
    val rungDelayVar=1.0;
    
    val westMost = 0  // west rung is rung 0
    var eastMost = _ladderToCross.nRungs-1
    var startRung = if (goingEast) westMost else eastMost
    var endRung   = if (goingEast) eastMost else westMost
    var move      = if (goingEast)        1 else       -1
    
    //println("Ape " + name + " starting to go " + (if (goingEast)"East." else "West."))
    
    if (debug)
      //System.out.println("Ape " + name + " wants rung " + startRung);      
    if (!_ladderToCross.grabRung(startRung, goingEast)) {
      System.out.println("  Ape " + name + " has been eaten by the crocodiles!");
      return;  // died
    }
    if (debug)
      System.out.println("Ape " + name + "  got  rung " + startRung);      
    for (i <- startRung+move to endRung by move) {
      Jungle.tryToSleep(rungDelayMin, rungDelayVar);
      if (debug)
        System.out.println("Ape " + name + " wants rung " + i);      
      if (!_ladderToCross.grabRung(i, goingEast)) {
        System.out.println("Ape " + name + ": AAaaaaaah!  falling off the ladder :-(");
        System.out.println("  Ape " + name + " has been eaten by the crocodiles!");
        _ladderToCross.releaseRung(i-move, goingEast); /// so far, we have no way to wait, so release the old lock as we die :-(
        return;  //  died
      }
      if (debug)
        System.out.println("Ape " + name + "  got  " + i + " releasing " + (i-move));      
      _ladderToCross.releaseRung(i-move, goingEast);
    }
    if (debug)
      System.out.println("Ape " + name + " releasing " + endRung);     
    _ladderToCross.releaseRung(endRung,goingEast);
    
    println("Ape " + name + " finished going " + (if (goingEast)"East." else "West."));
  }
}