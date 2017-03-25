package pong

/**
 * @author dlparker
 */
class Paddle(posX: Integer, var posY: Integer, speed: Long, var halfLength: Integer) extends Thread{
  var game = Pong
  override def run() {
      val height = game.height
      while(!game.gameEnded){
           Thread.sleep(speed)
           updateLocation()
           //println("paddle moving")
      }
  }
  
  def updateLocation(){
    var totalBallPosY = 0
    var totalBallNum = 0
    //println(Pong.balls.length)
    for(index <- 0 until game.balls.length){
      var ball = game.balls(index)
      //println("ball index:"+index+"   alive:"+ball.alive)
      if (ball.alive){
        totalBallNum += 1
        totalBallPosY += ball.posY
      }
    }
    if(totalBallNum==0){return}
    //println("ADJUSTING PADDLE POSITION")
    var averageBallPosY = totalBallPosY/totalBallNum
    if(averageBallPosY>posY){
      posY += 1
    }
    else if(averageBallPosY<posY){
      posY -= 1
    }
    if(posY-halfLength<0){
      posY = halfLength
    }
    if(posY+halfLength>game.height){
      posY = game.height-halfLength
    }
  }
}