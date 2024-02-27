package control.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDAO {
    private static final String INSERT_PLAYER_SQL = "INSERT INTO players (name, score) VALUES (?, ?)";
    private static final String SELECT_ALL_PLAYERS_SQL = "SELECT * FROM players ORDER BY score DESC LIMIT 10";

    public void addPlayer(String name, int score) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(INSERT_PLAYER_SQL)) {
            statement.setString(1, name);
            statement.setInt(2, score);
            statement.executeUpdate();
        }
    }

    public void getTopPlayers() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_ALL_PLAYERS_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                System.out.println(resultSet.getString("name") + ": " + resultSet.getInt("score"));
            }
        }
    }
}
