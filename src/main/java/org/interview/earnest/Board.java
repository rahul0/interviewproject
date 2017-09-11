package org.interview.earnest;

import org.apache.log4j.Logger;

import java.util.HashSet;

public class Board
{

    final static Logger logger = Logger.getLogger(Board.class);
    private HashSet<Ship> ships= new HashSet<>();
    private Cell[][] cells;
    String[] row_header;
    String[] col_header;
    // Constants for number of rows and columns.
    public int NUM_ROWS = 10;

    public Board(int size)
    {
        NUM_ROWS=size;
        setCells(new Cell[NUM_ROWS][NUM_ROWS]);
        row_header= new String[NUM_ROWS];
        col_header= new String[NUM_ROWS];

        for (int row = 0; row < getCells().length; row++)
        {
            row_header[row]="C"+row;
            col_header[row]="R"+row;
            for (int col = 0; col < getCells()[row].length; col++)
            {
                getCells()[row][col] = new Cell();
            }
        }
    }
    
    // Mark a hit in this location by calling the markHit method
    // on the Cell object.

    /**
     * mode 1 show ship
     * mode 2 show attacks
     * @param mode
     */

    public static void printShip(Board board, int mode){
        String space_long="  ";
        for(int r_h = 0; r_h< board.row_header.length; r_h++){
            String value= board.row_header[r_h];
            if(r_h==0){
                value=space_long+value;
                System.out.print(String.format("%11s", value));
            }else{
                System.out.print(String.format("%5s", value));
            }

        }
        System.out.println();
        for(int c_h = 0; c_h< board.col_header.length; c_h++){
            String value= board.col_header[c_h];
            System.out.print(String.format("%5s", value));
            for(int i = 0; i< board.getCells()[0].length; i++){
                int row=c_h;
                int col=i;
                String shipValue="";
                boolean isOccupied= board.getCells()[row][col].isOccupied();
                shipValue=isOccupied? board.getCells()[row][col].getShip().getName():"_";
                if(mode==2){
                    boolean wasAttacked=isOccupied ? board.getCells()[row][col].isAttacked():false;
                    if(wasAttacked){
                        shipValue=shipValue+"X";
                    }

                }
                System.out.print(String.format("%5s",shipValue ));
            }
            System.out.println();
        }

    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }


    public GAME_STATUS attack(int x, int y){


        if(x < 0 || y<0 || x>=NUM_ROWS || y>NUM_ROWS){
            logger.error("Position is not valid. Please retry");
            return GAME_STATUS.NONE;
        }
        Cell l = cells[x][y];
        GAME_STATUS result;

        // if  a miss
        if(!l.isOccupied()){
            if(l.isAttacked()){
//                System.out.println("Already Taken");
                result= GAME_STATUS.ALREADY_TAKEN;
            }else{
                result= GAME_STATUS.MISS;
//                System.out.println("Miss");
            }
            l.setAttacked(true);

//            return false;
        }else{ //every thing is occupied in this flow
            if(l.isAttacked()){
                result= GAME_STATUS.ALREADY_TAKEN;
            }else{
//                GAME_STATUS result;
                l.setAttacked(true);
                result= GAME_STATUS.HIT;
//                System.out.println("Hit");
                Ship s=l.getShip();
                s.hit(x,y);
                if(s.isSunk()){
                    result=GAME_STATUS.SUNK;
//                    System.out.println("Sunk");
                    boolean allSunk=false;
                    for(Ship ship: ships){
                        allSunk=ship.isSunk();
                        if(!allSunk){
                            break;
                        }
                    }
                    if(allSunk){
                        result=GAME_STATUS.WIN;
//                        System.out.println("Win");
//                        return true;
                    }
                }
            }
        }

        return result;
    }

    public HashSet<Ship> getShips() {
        return ships;
    }

    public void setShips(HashSet<Ship> ships) {
        this.ships = ships;
    }

    public boolean addShip(Ship s) {

        final boolean isHorizental = s.getDirection() == 0;
        int row = s.getRow();
        int col = s.getCol();
        //check
        for (int i = 0; i < s.getLength(); i++) {
            Cell l ;
            try{
                l=this.getCells()[row][col];
            }catch (ArrayIndexOutOfBoundsException  ae){
                logger.error("Ship doesn't fit on board, wrong position provided");
              return false;
            }

            if (l.isOccupied()) {
                logger.error("row=" + row + " col=" + col + " is occupied with ship " + l.getShip().getName());
                return false;
            }
            if (isHorizental) {
                col++;
            } else {
                row++;
            }
        }

        logger.debug("positions are not occupied!!!!");

        //add all ships to inventory
        this.getShips().add(s);

        //reset
        row = s.getRow();
        col = s.getCol();
        for (int i = 0; i < s.getLength(); i++) {
            Cell l = this.getCells()[row][col];
            l.setShip(s);
            s.getPositions().add(row + "," + col);

            if (isHorizental) {
                col++;
            } else {
                row++;
            }
        }
        return true;
    }

}