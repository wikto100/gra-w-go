package org.grawgo.serwer.db;

import org.grawgo.serwer.GoServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class GameLoader {
    static private final Statement statement;

    static private ResultSet currGameBoards;
    static private final Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/gogames", "goplayer", "superhaslo");
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String listGames() {

        StringBuilder resToSend = new StringBuilder();
        String query = "SELECT * FROM games;";

        try (ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int gameId = resultSet.getInt("game_id");
                int blackPoints = resultSet.getInt("black_points");
                int whitePoints = resultSet.getInt("white_points");
                resToSend.append("Game ID: ").append(gameId).append("\tScores: b").append(blackPoints).append(" w").append(whitePoints).append("|");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (resToSend.toString().isEmpty()) {
            return "No games found.";
        }
        return resToSend.toString();
    }

    public static void loadBoardDBSV(int gameno) {
        // deserialize board from db
        try {
            String query = "SELECT * FROM boards WHERE game_id='" + gameno + "' ORDER BY turn ASC;";
            currGameBoards = statement.executeQuery(query);
            loadNext();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // skrolluj resultset na komende uzytkownika
    public static void loadNext() {
        try {
            if (currGameBoards != null) {
                if (currGameBoards.next()) {
                    int turn = currGameBoards.getInt("turn");
                    String state = currGameBoards.getString("state");
                    GoServer.getBoard().setTurn(turn);
                    GoServer.getBoard().setBoard(state);
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void loadPrev() {
        try {
            if (currGameBoards != null) {
                if (currGameBoards.previous()) {
                    int turn = currGameBoards.getInt("turn");
                    String state = currGameBoards.getString("state");
                    GoServer.getBoard().setTurn(turn);
                    GoServer.getBoard().setBoard(state);
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void close() {

        if (currGameBoards != null) {
            try {
                currGameBoards.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
