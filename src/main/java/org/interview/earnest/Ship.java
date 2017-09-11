package org.interview.earnest;

import java.util.HashSet;

/**
 * Created by rahulkumar on 9/9/17.
 */
public class Ship
{

    private int row;
    private int col;
    private int length;
    private int direction;
    private String name="X";
    private HashSet<String> positions= new HashSet<>();

    // Direction Constants
    public static final int UNSET = -1;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    public Ship()
    {

    }

    // Set the direction of the ship
    public void setDirection(int direction)
    {
        if (direction != UNSET && direction != HORIZONTAL && direction != VERTICAL)
            throw new IllegalArgumentException("Invalid direction, direction was" + direction +"must be -1, 0, or 1");
        this.direction = direction;
    }

    // Getter for the row value
    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public int getLength()
    {
        return length;
    }

    public int getDirection()
    {
        return direction;
    }

    // Helper method to get a string value from the direction
    private String directionToString()
    {
        if (getDirection() == UNSET)
            return "UNSET";
        else if (getDirection() == HORIZONTAL)
            return "HORIZONTAL";
        else
            return "VERTICAL";
    }

    // toString value for this Ship
    public String toString()
    {
        return "Ship:" + getName() + ", " +getRow() + ", " + getCol() + " with length " + getLength() + " and direction " + directionToString();
    }


    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashSet<String> getPositions() {
        return positions;
    }

    public void setPositions(HashSet<String> positions) {
        this.positions = positions;
    }
    public void addPositions(int x, int y){
        positions.add(x+"," + y);
    }

    public void hit(int x,int y){
        String cordinate=x+","+y;

        if(positions.contains(cordinate)){
            positions.remove( x+","+y);
        }
    }

    public boolean isSunk(){
       return  positions.size()==0;
    }
}
