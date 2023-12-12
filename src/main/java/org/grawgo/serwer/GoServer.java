package org.grawgo.serwer;

import org.grawgo.core.Board;

import java.io.*;
import java.net.*;
/**
 * Klasa serwera
 */
public class GoServer {
    // instancja board przekazywana każdemu wątkowi do modyfikacji
    // TODO: board powinien być thread-safe
    private static volatile Board board;
    public static void main(String[] args) {
        //TODO: zaimplementuj zmianę rozmiaru planszy
        board = new Board(19);

        try (ServerSocket serverSocket = new ServerSocket(4444)) {
            System.out.println("Server active");
            //Laczenie klienta
            while (true) {
                Socket socket = serverSocket.accept();
                new GoThread(socket).start();
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

}