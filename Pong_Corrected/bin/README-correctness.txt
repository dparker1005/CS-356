From a concurrency perspective, the correct behavior of the program needs to be 
defined for any time two or more threads interact with each other. In this particular
Pong example, the threads that can collide are balls with paddles and balls with other
balls. 

When a ball shares a location with a paddle, the paddle should not be affected in any
way; however, the x velocity of the ball should be multiplied by -1. The y velocity
of the ball should not change. After the collision, the correct behavior of the ball
is for it to be going the opposite horizontal direction than the direction it was going
before the collision.

When two balls share the same location, each of their x velocities should be multiplied
by -1 and their y velocities should remain constant. Similar to the collision with a 
paddle, the correct resulting behavior for the ball is for it to be going the opposite
horizontal direction than the direction it was going before the collision.