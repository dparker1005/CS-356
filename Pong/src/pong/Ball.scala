package pong

/**
 * @author dlparker
 */
class Ball( var posX: Integer, var posY: Integer, var velX: Integer, var velY: Integer, var speed : Long) extends Thread {
  var alive = false
  var game = Pong
  val height = game.height
  val length = game.length
  
  override def run() {    
    alive = true
    while(alive && !game.gameEnded){
        Thread.sleep(speed)
        game.checkBallCollisions(this)
        updateLocation()
        if(!game.muteBallCoords){
          println(posX+", "+posY)
        }
      }
  }
  
  def setVelocity(velX: Integer, velY: Integer){
    this.velX = velX
    this.velY = velY
  }
  
  def speedUp(){
    speed -= 1
  }
  
  def updateLocation(){
    posX += velX
    posY += velY
    if(posY<=0 || posY>=height){
      velY *= -1
      if(posY<=0){
        posY=0
      }
      else{
        posY=height
      }
    }
    if(posX<=0 || posX>=length){
      game.ballAtSideOfScreen(this)
    }
  }
  

}