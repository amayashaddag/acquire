package debug;

import java.sql.SQLException;

import control.database.DatabaseConnection;

public class Debug {
    public static void main(String[] args) throws SQLException {
        DatabaseConnection.getConnection();
    }
}
