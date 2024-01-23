package org.grawgo.serwer;

import org.grawgo.core.Board;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class GoServer {
    // instancja board przekazywana każdemu wątkowi do modyfikacji
    // TODO: board powinien być thread-safe
    // TODO: nowa plansza po zakonczeniu gry
    private static volatile Board board;
    private static volatile boolean whiteConnected = false;
    private static volatile boolean blackConnected = false;
    public static volatile ArrayList<GoThread> threads = new ArrayList<>();

    public static void main(String[] args) {
        //TODO: zaimplementuj zmianę rozmiaru planszy
        board = new Board(19);

        try (ServerSocket serverSocket = new ServerSocket(4444)) {
            System.out.println("Server active");
            //Laczenie klienta
            while (true) {
                Socket socket = serverSocket.accept();
                // wątki są w stanie ColorPicking
                threads.add(new GoThread(socket));
                threads.get(threads.size() - 1).start();
                // zabij wszystkie wątki na zamknieciu serwera
                System.out.println("New client connected");
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // TODO: nie jestem pewny tego rozwiązania
    public static Board getBoard() {
        return board;
    }

    public static void connect(String player) { // możemy to przerzucić znowu do board jak chcesz
        if (player.equals("white")) {
            whiteConnected = true;
        } else if (player.equals("black")) {
            blackConnected = true;
        }
    }

    public static void reset(GoThread player) {
        if (player.getPlayerString().equals("white")) {
            whiteConnected = false;
        } else {
            blackConnected = false;
        }
        threads.remove(player);
    }

    // zwroc pierwszego czarnego ktory spi
    public static GoThread getFirstBlackSleeper() {
        for (GoThread player : threads) {
            if (player.getState().equals(Thread.State.WAITING) && player.getPlayerString().equals("black")) {
                return player;
            }
        }
        return null;
    }

    public static GoThread getFirstWhiteSleeper() {
        for (GoThread player : threads) {
            if (player.getState().equals(Thread.State.WAITING) && player.getPlayerString().equals("white")) {
                return player;
            }
        }
        return null;
    }

    // będziemy mieli threads jako pary graczy na więcej graczy w 2 iteracji
    public static GoThread findOther(GoThread me) {
        for (GoThread player : threads) {
            if (!player.equals(me))
                return player;
        }
        return null;
    }

    public static Boolean isWhiteConnected() {
        return whiteConnected;
    }

    public static Boolean isBlackConnected() {
        return blackConnected;
    }

    public static void resetBoard(int size) {
        board = new Board(size);
    }

}