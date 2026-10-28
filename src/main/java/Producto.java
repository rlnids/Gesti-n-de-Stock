// Ejecutar antes Proveedor.java (la FK depende de la tabla proveedor).
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Producto {
    public static void main(String[] args) {

        try (Connection con = Conexion.abrir();
             Statement st = con.createStatement()) {

            String sql = """
                CREATE TABLE IF NOT EXISTS productos (
                    producto_id       INT AUTO_INCREMENT PRIMARY KEY,
                    nombre_producto   VARCHAR(100) NOT NULL,
                    proveedor_id      INT NOT NULL,
                    cantidad_producto INT NOT NULL,
                    precio_pvp        DOUBLE NOT NULL,
                    precio_compra     DOUBLE NOT NULL,
                    CONSTRAINT fk_productos_proveedor
                        FOREIGN KEY (proveedor_id)
                        REFERENCES proveedor(proveedor_id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
                )
                """;

            st.executeUpdate(sql);
            System.out.println("Tabla de productos creada");

        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
    }
}