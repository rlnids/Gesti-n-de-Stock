import java.sql.*;
import java.until.Scanner;
 public class Menu {
     private static final String URL = "jdbc:h2:./GestiondeStock";
     private static final Scanner SC = new Scanner(system.in);

     public static  void main(String[] args) {
         int opcion = 0;
         try (Connection con = DriverManager.getConnection(URL, "sa", "")) {
             while (opcion != 5) {
                 mostrarmenu();
                 opcion = leerEntero("opcion: ");

                 switch (opcion) {
                     case 1 -> insertarProducto(con);
                     case 2 -> listarProducto(con);
                     case 3 -> insertarProveedor(con);
                     case 4 -> listarProveedor(con);
                     case 5 -> System.out.println("hasta luego lucas");
                     case 6 -> System.out.println("opción no valida");
                 }
             }
         } catch (SQLException e) {
             System.out.println("Error de conexión" + e.getMessage());
         } finally {
             SC.close();
         }
     }
     private static void mostarmenu() {
             System.out.println("\n---------------MENÚ------------");
             System.out.println("1. Añadir producto");
             System.out.println("2. listar producto");
             System.out.println("3. añadir proveedor");
             System.out.println("4. listar proveedores");
             System.out.println("5. Salir");
         }
         //Añadir productos
     private static void insertarProducto(Connection con){
         System.out.println("Nombre del producto : ");
         String nombreProducto = SC.nextline();
         System.out.println("Nombre del proveedor: ");
         String nombreProveedor = SC.nextline();
         System.out.println("Cantidad de producto: ");
         int cantidadProducto = SC.nextline();
         System.out.println("Precio_pvp : ");
         double precioPvp = SC.nextline();
         System.out.println("Precio compra ");
         double precioCompra = SC.nextline();

         String sql = "INSERT INTO productos (nombre_producto, nombre_proveedor, cantidad_producto, precio_pvp, precio_compra) VALUES(?,?,?,?,?)";
         try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
             ps.setString(1, nombreProducto);
             ps.setString(2, nombreProveedor);
             ps.setInt(3, cantidadProducto);
             ps.setDouble(4, precioPvp);
             ps.setDouble(5, precioCompra);
             ps.executeUpdate();

             try (ResultSet rs = ps.getGeneratedKeys()){
                 if (rs.next()) {
                     System.out.println("Contacto insertado con id = " + rs.getInt(1));
                 }
                 }

             }catch (SQLException e){
             System.out.println("Error al insertar contacto" + e.getMessage());


         }
     }


}