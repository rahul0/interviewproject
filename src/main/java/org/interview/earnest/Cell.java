package org.interview.earnest;

public class Cell
{

    //if this position was attacked and was occupied
    private boolean attacked=false;

    private Ship ship =null;
    // Cell constructor.
    public Cell() {

    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    /**
     * Checks if this location is occupied, means if a ship is pesent here
     * @return
     */
    public boolean isOccupied(){
        return  ship!=null;
    }

    public boolean isAttacked() {
        return attacked;
    }

    public void setAttacked(boolean attacked) {
        this.attacked = attacked;
    }
}