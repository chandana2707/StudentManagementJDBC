package ep_exam;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ImageJdbcCRUD {

    private static final String URL = "jdbc:mysql://localhost:3306/company";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Chandana27072005!";

    // Load the JDBC driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers loaded successfully!!!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading JDBC driver: " + e.getMessage());
        }
    }

    // Method to insert an image into the database (CREATE)
    public static void insertImage(Connection con, Scanner scanner) {
        System.out.print("Enter the image path to insert: ");
        String imagePath = scanner.nextLine();
        String query = "INSERT INTO image_table(image_data) VALUES(?)";

        try (FileInputStream fileInputStream = new FileInputStream(imagePath);
             PreparedStatement stmt = con.prepareStatement(query)) {

            byte[] imageData = new byte[fileInputStream.available()];
            fileInputStream.read(imageData);
            stmt.setBytes(1, imageData);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Image inserted successfully");
            } else {
                System.out.println("Image insertion failed");
            }

        } catch (SQLException | IOException e) {
            System.out.println("Error during image insertion: " + e.getMessage());
        }
    }

    // Method to retrieve an image from the database by ID (READ)
    public static void retrieveImage(Connection con, Scanner scanner) {
        System.out.print("Enter the image ID to retrieve: ");
        int imageId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter the output path to save the image: ");
        String outputPath = scanner.nextLine();

        String query = "SELECT image_data FROM image_table WHERE image_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, imageId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                byte[] imageData = rs.getBytes("image_data");

                try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath)) {
                    fileOutputStream.write(imageData);
                    System.out.println("Image retrieved and saved to " + outputPath);
                } catch (IOException e) {
                    System.out.println("Error writing image to file: " + e.getMessage());
                }
            } else {
                System.out.println("Image not found");
            }

        } catch (SQLException e) {
            System.out.println("Error during image retrieval: " + e.getMessage());
        }
    }

    // Method to update an image in the database (UPDATE)
    public static void updateImage(Connection con, Scanner scanner) {
        System.out.print("Enter the image ID to update: ");
        int imageId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter the new image path: ");
        String newImagePath = scanner.nextLine();

        String query = "UPDATE image_table SET image_data = ? WHERE image_id = ?";

        try (FileInputStream fileInputStream = new FileInputStream(newImagePath);
             PreparedStatement stmt = con.prepareStatement(query)) {

            byte[] imageData = new byte[fileInputStream.available()];
            fileInputStream.read(imageData);
            stmt.setBytes(1, imageData);
            stmt.setInt(2, imageId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Image updated successfully");
            } else {
                System.out.println("Image update failed");
            }

        } catch (SQLException | IOException e) {
            System.out.println("Error during image update: " + e.getMessage());
        }
    }

    // Method to delete an image from the database by ID (DELETE)
    public static void deleteImage(Connection con, Scanner scanner) {
        System.out.print("Enter the image ID to delete: ");
        int imageId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        String query = "DELETE FROM image_table WHERE image_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, imageId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Image deleted successfully");
            } else {
                System.out.println("Image deletion failed or image not found");
            }

        } catch (SQLException e) {
            System.out.println("Error during image deletion: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            boolean running = true;
            while (running) {
                System.out.println("\n--- Image CRUD Operations ---");
                System.out.println("1. Insert Image");
                System.out.println("2. Retrieve Image");
                System.out.println("3. Update Image");
                System.out.println("4. Delete Image");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        insertImage(con, scanner);
                        break;
                    case 2:
                        retrieveImage(con, scanner);
                        break;
                    case 3:
                        updateImage(con, scanner);
                        break;
                    case 4:
                        deleteImage(con, scanner);
                        break;
                    case 5:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }

        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}
