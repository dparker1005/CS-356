package pong

/**
 * @author dlparker
 */
object Pong extends App{
  var height = 25
  var length = 100
  
  var balls : Array[Ball] = new Array[Ball](0); 
  var gameEnded = false
  var leftPaddle = new Paddle(length/2, height/2, 2, 7) //game, posX, posY, speed, length
  var rightPaddle = new Paddle(length/2, height/2, 2, 7)
  
  var rightScore = 0
  var leftScore = 0
  
  var muteBallCoords = true
  val r = scala.util.Random
  var numBalls = 11
  
  
   println("starting game...")
    leftPaddle.start()
    rightPaddle.start()
    println("game has started!")
    var index = 0
    while(index < numBalls){
      Thread.sleep(1000)
      if(gameEnded){
        index=numBalls+1
      }
      else{
        var newVelX = 1
        if(index%2==0){newVelX = -1}
        var b = new Ball(length/2, r.nextInt(height), newVelX, 0, 15) //game, posX, posY, velX, velY, speed
        balls = balls :+ b
        b.start()
        println("Sent ball "+(index+1) +" into play!!!")
        //println(Pong.balls.length)
        index +=1
      }
    }
    
  def ballAtSideOfScreen(b: Ball){
    val onLeft = (b.posX<=0)
    if((onLeft&&((b.posY<leftPaddle.posY+leftPaddle.halfLength) 
        && ((b.posY>leftPaddle.posY-leftPaddle.halfLength)))
        ||(!onLeft&&(b.posY<rightPaddle.posY+rightPaddle.halfLength) 
            && (b.posY>rightPaddle.posY-rightPaddle.halfLength)))){
      b.velX *= -1
      b.velY += (r.nextInt(5)-3)
      b.speedUp()
      if(onLeft){
        println("Left hit the ball at y="+leftPaddle.posY)
      }
      else{
        println("Right hit the ball at y="+rightPaddle.posY)
      }
      return
    }
    if(onLeft){
      println("Right Scores at y="+b.posY+" while left was at y="+leftPaddle.posY)
      rightScore += 1
      printScore()
    }
    else{
      println("Left Scores at y="+b.posY+" while right was at y="+rightPaddle.posY)
      leftScore += 1
      printScore()
    }
    b.alive = false
    
    if(rightScore>numBalls/2 || leftScore>numBalls/2){
      println("GAME OVER!!!")
      gameEnded = true
    }
  }
  
  def checkBallCollisions(b: Ball){
    for(index <- 0 until balls.length){
      val checkBall = balls(index)
      if(!(b eq checkBall) && checkBall.posX==b.posX && checkBall.posY==b.posY){
        b.setVelocity(-(b.velX), b.velY)
        checkBall.setVelocity(-(checkBall.velX), checkBall.velY)
        println("There was a ball collision at "+b.posX+", "+b.posY)
      }
    }
  }
  
  def printScore(){
    println("Right "+rightScore+"   -   "+leftScore+" Left")
  }
}