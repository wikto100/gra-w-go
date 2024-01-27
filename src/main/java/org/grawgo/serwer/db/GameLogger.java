package org.grawgo.serwer.db;

import org.grawgo.serwer.GoServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GameLogger {
    private static final Connection connection;
    private static final Statement statement;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/GOgames", "goplayer", "superhaslo");
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String insertQuery;
    private static int currGameID;


    public static void logBoard(int turn, String state) {
        // jezeli jest w bazie to zaladuj turn z bazy, ustaw turn w board na ten z bazy i ok
        try {
            insertQuery = "INSERT INTO `GOgames`.`boards` (`game_id`, `turn`, `state`) VALUES ('" + currGameID + "', '"
                    + turn + "', '" + state + "');";
            statement.executeUpdate(insertQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logGame(int size, int black_points, int white_points) {
        if (!GoServer.getBoard().isLogged() && !isFoundInDB(currGameID)) {
            try {

                // jesli gra juz jest w bazie, nie rob tu nic.
                // jesli nie ma go jeszcze w bazie to :
                insertQuery = "INSERT INTO `GOgames`.`games` (`game_id`,`size`, `black_points`, `white_points`) VALUES ('" + currGameID + "', '" + size
                        + "', '" + black_points + "', '" + white_points + "');";
                statement.executeUpdate(insertQuery);
                GoServer.getBoard().setLogged(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isFoundInDB(int gameno) {
        boolean found = false;
        String query = "SELECT COUNT(*) FROM `GOgames`.`games` WHERE `game_id` = '" + gameno + "'";

        try (ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                found = count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return found;
    }

    public static void logScores(int blackPoints, int whitePoints) {
        try {
            String query = "UPDATE `GOgames`.`games` SET `black_points` = '" + blackPoints + "', `white_points` = '" + whitePoints + "' WHERE `game_id` = '" + currGameID + "'";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setCurrGameID(int currGameID) {
        GameLogger.currGameID = currGameID;
    }

    public static void close() {
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
