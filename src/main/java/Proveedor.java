import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Proveedor {
    public static void main(String[] args) {

        try (Connection con = Conexion.abrir();
             Statement st = con.createStatement()) {

            String sql = """
                CREATE TABLE IF NOT EXISTS proveedor (
                    proveedor_id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre_proveedor VARCHAR(50) NOT NULL UNIQUE,
                    correo VARCHAR(100),
                    telefono VARCHAR (12)
                )
                """;

            st.executeUpdate(sql);
            System.out.println("Tabla de proveedor creada");

        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
    }
}