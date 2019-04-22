package domain.roundabout;


import domain.vehicles.Vehicle;

/**
 * Roundabout class.
 */
public class Roundabout {

    /**
     * The roundabout entries.
     */
    private RoundaboutEntry[] entries;

    /**
     * The roundabout exits.
     */
    private RoundaboutExit[] exits;

    /**
     *
     */
    private RoundaboutLane[] lanes;

    /**
     * Roundabout empty constructor.
     * @param nEntries Number of entries and exits in the roundabout.
     * @param nLanes Number of lanes
     */
    public Roundabout(int nEntries, int nLanes, int nExits) {

        this.entries = new RoundaboutEntry[nEntries];
        this.lanes = new RoundaboutLane[nLanes];
        this.exits = new RoundaboutExit[nExits];
    }

    /**
     * Returns the number of lanes in the roundabout.
     *
     * @return int
     */
    public int entries() {

        return this.entries.length;
    }

    /**
     * Returns the number of lanes in the roundabout.
     *
     * @return int
     */
    public int lanes() {

        return this.lanes.length;
    }

    /**
     * Returns the number of exits in the roundabout.
     *
     * @return int
     */
    public int exits() {

        return this.exits.length;
    }

    /**
     * Allows queueing a vehicle to the roundabout.
     * @param vehicle The vehicle to be enqueued.
     * @param entry The entry where the vehicle should be enqueued.
     */
    public boolean queueVehicle(Vehicle vehicle, int entry) {

        return this.entries[entry].add(vehicle);
    }
}
