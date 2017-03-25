One data race in this program can occur whenever two balls collide with each other. If
both threads run the checkBallCollisions() function and two balls identify themselves as colliding 
with each other at the same time, it is possible that one of the balls will multiply
both of their x velocities by -1, and then that the other will do so immediately after. This can be
demonstrated by inserting a sleep in the checkBallCollisions() function after the if statement
and before the setVelocity() calls to the balls. The effect of this data race is that the
collision would be negated and that both balls would continue on the same trajectory that they
had before the collision. 

There is another data race that occurs when two balls have both already checked for collisions,
and then one moves onto the other, then the other moves away. In this scenario, no collision
will ever be detected, even though the balls did occupy the same location.This goes against
what is stated in README-correctness.txt. This data race can be demonstrated by inserting
a sleep after the call to checkCollisions(), but before the call to updateLocation().