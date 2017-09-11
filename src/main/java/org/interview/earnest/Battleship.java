package org.interview.earnest;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Scanner;

public class Battleship {
    public static Scanner reader = new Scanner(System.in);
    final static Logger logger = Logger.getLogger(Battleship.class);

    int shipCount = -1;
    Board board;

    public Battleship(int board_size) {
        board = new Board(board_size);

    }

    public static void playGame(Battleship bs) {
        System.out.println("----Game Setup----");
        System.out.println("----Enter ship count----");
        String gridShipCountStringified = reader.nextLine();

        try {
            bs.shipCount = Integer.valueOf(gridShipCountStringified);
        } catch (NumberFormatException ne) {
            logger.debug("input:" + gridShipCountStringified);
        }

        System.out.println("----Setup Board----");
        bs.addShipToGame(bs.shipCount);
        bs.attack(reader);
    }

    public static void main(String[] args) {

        int board_size=5;
        Battleship bs = new Battleship(board_size);
        playGame(bs);
    }



    private boolean extractShipSizeFromCMD(Scanner reader, Ship ship) {

        System.out.println("Enter ship size , max_size=" + board.NUM_ROWS);
        String ship_size_string = reader.nextLine();
        boolean succuess = false;
        try {
            int ship_size = Integer.valueOf(ship_size_string);
            ship.setLength(ship_size);
            succuess = true;
            logger.debug("ship size: " + ship_size);
        } catch (NumberFormatException ne) {
            logger.error("wrong ship size: " + ship_size_string + ". Please retry.");
        }
        return succuess;
    }

    private boolean extractDirectionFromCMD(Scanner reader, Ship ship) {

        System.out.println("Enter ship orientation 1:Vertical 0: Horizental");
        boolean success = false;
        String ship_direction_string = reader.nextLine();

        try {
            int ship_direction = Integer.valueOf(ship_direction_string);
            ship.setDirection(ship_direction);
            logger.debug("ship direction: " + ship_direction);
            success = true;
        } catch (NumberFormatException ne) {
            logger.error("wrong ship direction: " + ship_direction_string);
        }
        return success;
    }

    private boolean extractRowColumnFromCMD(Scanner reader, HashMap<String, Integer> pos_map) {
        System.out.println("Enter row, column (r1,c1) etc, no validation");
        String position = reader.nextLine();
        boolean success = false;
        try {
            String removed = position.trim().replaceAll("[a-zA-Z()]", "");
            logger.debug("removed: " + removed);
            String[] pos = removed.split(",");
            int row = Integer.valueOf(pos[0]);
            int col = Integer.valueOf(pos[1]);
            pos_map.put("row", row);
            pos_map.put("col", col);
            logger.debug("row: " + row + " col:" + col);
            success = true;
        } catch (NumberFormatException ne) {
            logger.error("Couldn't extract row & column from input: " + position.trim());

        }
        return success;
    }

    public boolean constructShipFromCommandLine(Ship s) {
        Scanner reader = new Scanner(System.in);
        boolean success = false;

        success = extractShipSizeFromCMD(reader, s);
        if (!success) {
            return success;
        }
        success = extractDirectionFromCMD(reader, s);
        if (!success) {
            return success;
        }
        HashMap<String, Integer> pos_map = new HashMap<>();
        success = extractRowColumnFromCMD(reader, pos_map);

        if (!success) {
            return success;
        } else {
            int row = pos_map.get("row");
            int col = pos_map.get("col");
            s.setRow(row);
            s.setCol(col);

        }
        return success;

    }

    public void addShipToGame(int count) {

        System.out.println("Adding ship interactively");
        Board.printShip(board, 1);
        for (int s_count = 0; s_count < count; s_count++) {
            int id = s_count + 1;
            String ship_name = "S" + id;
            Ship s = new Ship();
            s.setName(ship_name);
            logger.info("Adding ship: " + ship_name);
            boolean success = false;
            while (!success) {
                success = constructShipFromCommandLine(s);
                if (success) {
                    success = board.addShip(s);
                } else {
                    continue;
                }
            }
            Board.printShip(board, 1);
        }
    }

    //Just a wrapper around attck in Board class
    public void attack(Scanner scanner) {
        boolean win = false;
        HashMap<String, Integer> pos_map = new HashMap<>();

        while (!win) {
            Board.printShip(board, 2);
            System.out.println("Play");
            extractRowColumnFromCMD(scanner, pos_map);
            int row = pos_map.get("row");
            int col = pos_map.get("col");
            win = board.attack(row, col);
        }
    }
}