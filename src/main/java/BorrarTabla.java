import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BorrarTabla {
    public static void main(String[] args) {

        try (Connection con = Conexion.abrir();
             Statement st = con.createStatement()) {

            // Orden por la FK: productos antes que proveedor.
            st.executeUpdate("DROP TABLE IF EXISTS productos");
            st.executeUpdate("DROP TABLE IF EXISTS proveedor");
            System.out.println("Tablas borradas correctamente");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}