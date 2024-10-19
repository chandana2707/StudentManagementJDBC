package ep_exam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDb {

    private static final String url = "jdbc:mysql://localhost:3306/ep";
    private static final String user = "root";
    private static final String pass = "Chandana27072005!";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    public static void createUser(String name, String email) {
        String query = "INSERT INTO users (name, email) VALUES (?, ?)";

        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("A new user was inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void readUsers() {
        String query = "SELECT * FROM users";

        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.println("id: " + id + ", name: " + name + ", email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("User not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateUser(int id, String newEmail) {
        String query = "UPDATE users SET email = ? WHERE id = ?";

        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, newEmail);
            stmt.setInt(2, id);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("User not found or no update occurred!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createUser("Chandana", "chandana@gmail.com");
        createUser("Aman", "aman@gmail.com");

        System.out.println("Reading Users: ");
        readUsers();

        updateUser(1, "chandananewemail@gmail.com");

        System.out.println("\nReading users after update: ");
        readUsers();

        deleteUser(2);

        System.out.println("\nReading users after deletion:");
        readUsers();
    }
}
