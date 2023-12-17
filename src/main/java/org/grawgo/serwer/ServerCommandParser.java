package org.grawgo.serwer;

import org.grawgo.core.Board;

//TODO wylapywanie nieprawidlowych danych po place
//Double locked singleton
public class ServerCommandParser {
    private static volatile ServerCommandParser instance;

    private ServerCommandParser() {
    }

    public static ServerCommandParser getInstance() {
        ServerCommandParser result = instance;
        if (result != null) {
            return result;
        }
        synchronized (ServerCommandParser.class) {
            if (instance == null) {
                instance = new ServerCommandParser();
            }
            return instance;
        }
    }


    public String parseCommand(String input) {
        return input.split("\\$")[0];
    }


    public int[] parseCoords(String input) {
        String data = input.split("\\$")[1];
        int[] coords = new int[2];
        coords[0] = Integer.parseInt(data.split("\\|")[0]) - 1;
        coords[1] = Integer.parseInt(data.split("\\|")[1]) - 1;
        return coords;
    }

    public String parseOutput(Board board) { // nie wiem czy podawanie calej planszy jest tu dobrym pomyslem
        String response = "PLACE_RESPONSE$";
        response += board.printBoard();
        return response;
    }
}
