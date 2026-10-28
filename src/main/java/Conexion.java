import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL      = "jdbc:h2:./GestiondeStock";
    private static final String USER     = "sa";
    private static final String PASSWORD = "";

    /** Abre una conexión nueva a la base de datos. */
    public static Connection abrir() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** Prueba de conexión (tu main original). */
    public static void main(String[] args) {
        try (Connection con = abrir()) {
            System.out.println("Conexion abierta con H2");
        } catch (SQLException e) {
            System.out.println("No se pudo conectar: " + e.getMessage());
        }
        System.out.println("Programa terminado, base de datos cerrada");
    }
}