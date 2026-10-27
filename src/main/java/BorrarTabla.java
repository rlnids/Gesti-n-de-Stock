import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BorrarTabla {
    public static void main(String[] args) {

        String url = "jdbc:h2:./GestiondeStock";

        try (Connection con = DriverManager.getConnection(url, "sa", "");
             Statement st = con.createStatement()) {


            String sql = "DROP TABLE IF EXISTS proveedor";

            st.executeUpdate(sql);
            System.out.println("Tabla 'proveedor' borrada correctamente");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}