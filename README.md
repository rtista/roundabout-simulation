# Roundabout Simulation
This project began as a final project for a Concurrency and Reliable Software course (SOCOF) at ISEP's MSc. Software Engineering and since I enjoyed developing it I decided to share it as it might help someone else.

## Implementation Details
The implementation is based on multithreading and shared-memory data structures. Basically, each thread is a vehicle which wants to enter a roundabout at a determined entry and exit at a determined exit.

The roundabout is a shared-memory data structure which implements lock-free mechanisms to make the application concurrency safe. The roundabout itself is a graph in which each node is a position which can be occupied by a vehicle (thread), an entry or an exit. In order to decide which path a vehicle will take, a shortest-path algorithm is applied for light vehichles while an outer roundabout lane only shortest-path is applied for heavy vehicles (as these can only travel in the outer lane of the roudabout).

The application features a Graphical User Interface when launched which provides a visual layout of the roudabout graph and allows the instantiation of vehicles (light or heavy) as well as chosing which entry and exit it should take. When the button is pressed a vehicle is created and its path is highlighted as he moves through the nodes which make it.
