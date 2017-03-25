To correct the first problem described in README-data-races.txt, I added a new variable
to Ball.scala called collisionsAllowed. This is defaulted to true, but whenever this
ball registers a collision with another or another ball registers a collision with
it, it is set to false. When it is false, it will not register collisions with other balls.
This makes sure that a collision between two balls will only be registered once. 

After the updateLocation() call in Ball.scala, collisionsAllowed is set to true for that
ball so that it can interact with other balls once again.

In order to make this work, I put a synchronized block around the if condition in the
checkCollisions() function. This once again makes sure that two balls do not register
collisions with each other at the same time, guaranteeing that a collision will only
be counted once, and solving the data race described in README-data-races.txt.

----------------------------------------------------------------------------------------

The solution that makes the most sense for the second data race is to put the checkCollisions()
call and the updateLocation() call in a synchronized block; however, this would take a lot of
concurrency out of the program since only one ball would be able to update its location at
a time. This would work because the ball would move immediately after checking for collisions,
which means that there are no collisions that would go undetected. 

Instead, what I decided to do was have a semaphore for every x position on the screen. Before
a ball checks for collisions or moves, it must acquire the semaphore of the x position it is
currently on, and the semaphores for the x positions on both sides of it. This means that
multiple balls can be moving concurrently as long as they are not moving while they have
similar x positions. This solves the collisions problem because collisions can only occur when
x values are equal. Grabbing the semaphore to either side of the ball ensures that the ball 
will still be safe from data races after moving. 

In order to avoid deadlocks, I set up a system where it each ball would try to acquire all
3 semaphores. If it succeeds, great. If one of the semaphores it needs is already taken, the
ball then releases all of the semaphores that it has previously acquired, waits a random amount
of time, and then tries to acquire them all again.

This solution actually solves both the second data race as well as the first; however, I decided
to keep the first solution in place as well for redundancy and to demonstrate how the solution
would have worked.