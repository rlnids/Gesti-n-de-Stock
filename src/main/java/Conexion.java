import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    public static void main(String[] args) {
        String url = "jdbc:h2:./GestiondeStock";
        String user = "sa";
        String password = "";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            System.out.println("Conexion abierta con H2");
        } catch (SQLException e) {
            System.out.println("No se pudo conectar: " + e.getMessage());
        }
        System.out.println("Programa terminado, base de datos cerrada");
    }
}
