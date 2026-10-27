import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Producto {
    public static void main(String[] args) {

        String url = "jdbc:h2:./GestiondeStock";

        try (Connection con = DriverManager.getConnection(url, "sa", "");
             Statement st = con.createStatement()) {

            String sql = """
                CREATE TABLE IF NOT EXISTS productos (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre_producto VARCHAR (100) NOT NULL ,
                    nombre_proveedor VARCHAR (100) NOT NULL ,
                    cantidad_producto INT NOT NULL,
                    precio_pvp DOUBLE NOT NULL,
                    precio_compra DOUBLE NOT NULL,
                    CONSTRAINT fk_productos_proveedor
                    FOREIGN KEY (nombre_proveedor)
                    REFERENCES productos(id)
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