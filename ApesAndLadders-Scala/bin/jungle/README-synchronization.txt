Part II:

In order to make sure that an ape does not grab a rung that another ape is already
holding onto, I will use semaphores. In Ladder.scala, I will create a new array
of equal size to the array that represents the rungs of the ladder. Each spot
in the array will contain a semaphore. Whenever an ape calls the grabRung method, 
he will acquire the semaphore for the rung that he is reaching for. Whenever an ape
calls the release rung method, he will release the semaphore as well, allowing
another ape to grab it. This means that no apes will be able to grab a single rung
at the same time since there is only 1 semaphore for each rung, and furthermore
that no ape will ever fall even if two apes are going in different directions.

------------------------------------------------------------------------------------
Part III:

I will also use semaphores to make sure that there is no deadlock caused by apes
going in different directions. There will be two additional semaphores to the previous
part, which include eastSemaphore and westSemaphore. Before going east, an ape must
acquire an eastSemaphore. Before going west, an ape must acquire a westSemaphore. These
semaphores are not released once the ape gets off the last ring of the ladder. Both of these 
semaphores start with a value of 0 and have a fairness value of True, which guarantees
that apes will acquire semaphores in the order that they requested them.

There are also three other new variables in Ladder.scala, which are apesOnLadder, noDirection,
and apesThatAreWaitingToCross. apesOnLadder starts at 0, is incremented whenever an ape acquires 
an east or west semaphore, and is decremented whenever they get off the last rung of a ladder. 
As a result, this variable contains the number of apes currently on the ladder. noDirection is 
a boolean variable that starts as true, and turns to false once apes are going east or west. 
It can be turned back to true if there are not apes waiting on either side of the ladder, which 
will be explained in the future. apesThatAreWaitingToCross is used to solve a data race, and
essentially counts down the number of apes that still need to go onto the ladder.

When the program starts, a thread is started that calls a new method in Ladder.scala called
waitForApe(). All this method does is ask if there are apes waiting to go east, and then if
there are apes waiting to go west. It continues asking this question until there are indeed
apes waiting to go east or west. At this point in time, noDirection is set to false and the
loop breaks. If the ape that broke the loop wants to go east, then this method releases
eastSemaphores equal to the number of apes wanting to go east. If the ape that broke the
loop wants to go west, this method releases westSemaphores equal to the number of apes wanting
to go west. This method takes advantage of Semaphore.hasQueuedThreads() and 
Semaphore.getQueueLength(), both of which were discovered in the Java API Documentation and
both are used later in the program as well.

Once either westSemaphore or eastSemaphore have a value greater than 0, a number of apes
equal to that value will go in the specified direction. It is guaranteed that there is
a number of apes greater than or equal to that value at any time since that value was
determined by Semaphore.getQueueLength(). 

After each ape releases the last ring in the direction they are going, they are asked if 
apesOnLadder is equal to 0 and if westSemaphore/eastSemaphore.availablePermits() is equal 
to 0 (westbound apes will ask about westSemaphore, eastbound apes will ask about eastSemaphore).
If both of these are true, then that means that all of the apes that were supposed to start
across the ladder have gotten onto the ladder, and that there are no apes currently on the
ladder; therefore, all apes that were supposed to cross the ladder have crossed the ladder.

Once this has been confirmed, there are three possible outcomes, all of which are in a 
synchronized block for security. apesOnLadder is also decremented in this synchronized block.
First, if there are apes waiting to go in the other direction, which is determined by 
Semaphore.hasQueuedThreads(), then release a number of semaphores to their direction equal 
to the number of apes currently waiting. If there are no apes waiting to go in the other
direction, a similar check is done to see if there are additional apes waiting to go in the
direction that apes just went in. If there are no apes waiting for that, then there are 
no apes waiting to go in either direction. In this case, noDirection is set to true and
waitForApe() is started in a new thread. This is exactly what happens in the beginning of
the program where the ladder waits for new apes to come.

During this program, none of the monkeys will fall due to the description provided in
part II of the lab. 

There will never be deadlock while using this algorithm because apes are only ever going west
or going east at one time; there will never be apes going in both directions at once. This is 
based on the fact that there will always be an equal or lesser amount of semaphores for each
side than apes waiting on that side, so there will never be semaphores remaining once
the last ape for a single rotation for a side has crossed. There will also never be semaphores 
released until all apes going to once side have crossed.

Assuming that there is a finite amount of apes trying to cross the latter, the best possible 
complexity for this program is to have all apes from one side cross, and then have all apes 
from the other side cross. The fewer times that the direction in which apes cross change, 
the better the complexity of the algorithm will be. Since we do not know the total number of
apes that will cross, our approach, which to send all apes that are waiting when the other
side finishing crossing, is a good alternative. It is better than a linear complexity
because multiple apes will be crossing the ladder concurrently. The reason that we don't 
send all apes from one side until there are no more on that side is because, in a case with
infinite apes, this can result in starvation if apes never stop going to one side. The reason 
that we don't send x apes every time, where x is an integer, is that the algorithm that we 
currently have is able to scale in order to send more apes at once if that is appropriate, 
resulting in a better complexity.