import java.sql.*;
import java.util.Scanner;

public class Menu {

    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion = 0;
        try (Connection con = Conexion.abrir()) {
            while (opcion != 4) {
                mostrarmenu();
                opcion = leerEntero("Opción: ");

                switch (opcion) {
                    case 1 -> insertarProducto(con);
                    case 2 -> listarProducto(con);
                    case 3 -> listarProveedor(con);
                    case 4 -> System.out.println("¡Hasta luego, Lucas!");
                    default -> System.out.println("Opción no válida.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        } finally {
            SC.close();
        }
    }

    private static void mostrarmenu() {
        System.out.println("\n--------------- MENÚ ------------");
        System.out.println("1. Añadir producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Listar proveedores");
        System.out.println("4. Salir");
    }

    // ---------- PRODUCTOS ----------
    private static void insertarProducto(Connection con) {
        System.out.print("Nombre del producto : ");
        String nombreProducto = SC.nextLine().trim();

        System.out.print("Nombre del proveedor: ");
        String nombreProveedor = SC.nextLine().trim();

        Integer proveedorId = obtenerProveedorId(con, nombreProveedor);

        // Si el proveedor no existe, ofrece crearlo en el momento, creada con deepsek
        if (proveedorId == null) {
            System.out.print("⚠ El proveedor '" + nombreProveedor +
                    "' no existe. ¿Crearlo ahora? (s/n): ");
            if (!SC.nextLine().trim().equalsIgnoreCase("s")) {
                System.out.println("Operación cancelada. Producto no insertado.");
                return;
            }

            proveedorId = pedirYCrearProveedor(con, nombreProveedor);
            if (proveedorId == null) {
                System.out.println("No se pudo crear el proveedor. Producto no insertado.");
                return;
            }
        }

        int cantidadProducto = leerEntero("Cantidad de producto: ");
        double precioPvp     = leerDouble("Precio_pvp : ");
        double precioCompra  = leerDouble("Precio compra : ");

        String sql = "INSERT INTO productos " +
                "(nombre_producto, proveedor_id, cantidad_producto, precio_pvp, precio_compra) " +
                "VALUES (?,?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombreProducto);
            ps.setInt(2, proveedorId);
            ps.setInt(3, cantidadProducto);
            ps.setDouble(4, precioPvp);
            ps.setDouble(5, precioCompra);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    System.out.println("Producto insertado con id = " + rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
        }
    }
    //join para ver los productos junto con su proveedor
    private static void listarProducto(Connection con) {
        String sql = """
                SELECT p.producto_id, p.nombre_producto, pr.nombre_proveedor,
                       p.cantidad_producto, p.precio_pvp, p.precio_compra
                FROM productos p
                JOIN proveedor pr ON pr.proveedor_id = p.proveedor_id
                ORDER BY p.producto_id
                """;

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n id | nombre_producto | nombre_proveedor | cantidad_producto | precio_pvp | precio_compra");
            System.out.println("-------------------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%d | %s | %s | %d | %.2f | %.2f%n",
                        rs.getInt("producto_id"),
                        rs.getString("nombre_producto"),
                        rs.getString("nombre_proveedor"),
                        rs.getInt("cantidad_producto"),
                        rs.getDouble("precio_pvp"),
                        rs.getDouble("precio_compra"));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
    }

    // ---------- PROVEEDORES ----------
    private static void listarProveedor(Connection con) {
        String sql = "SELECT proveedor_id, nombre_proveedor, correo, telefono " +
                "FROM proveedor ORDER BY proveedor_id";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n id | nombre_proveedor | correo | telefono");
            System.out.println("---------------------------------------------");

            while (rs.next()) {
                System.out.printf("%d | %s | %s | %s%n",
                        rs.getInt("proveedor_id"),
                        rs.getString("nombre_proveedor"),
                        rs.getString("correo"),
                        rs.getString("telefono"));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar proveedores: " + e.getMessage());
        }
    }


    private static Integer pedirYCrearProveedor(Connection con, String nombreProveedor) {
        System.out.print("Inserte el correo: ");
        String correo = SC.nextLine().trim();

        System.out.print("Inserte el telefono: ");
        String telefono = SC.nextLine().trim();

        String sql = "INSERT INTO proveedor (nombre_proveedor, correo, telefono) VALUES (?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombreProveedor);
            ps.setString(2, correo);
            ps.setString(3, telefono);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Proveedor insertado con id = " + id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar proveedor: " + e.getMessage());
        }
        return null;
    }

    // ---------- UTILIDADES ----------
    // Devuelve el proveedor_id a partir del nombre, o null si no existe.
    // Un null indica que el llamador debe ofrecer crear el proveedor
    private static Integer obtenerProveedorId(Connection con, String nombreProveedor) {
        String sql = "SELECT proveedor_id FROM proveedor WHERE nombre_proveedor = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreProveedor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("proveedor_id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar proveedor: " + e.getMessage());
        }
        return null;
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            if (!mensaje.isEmpty()) System.out.print(mensaje);
            String linea = SC.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero válido.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            if (!mensaje.isEmpty()) System.out.print(mensaje);
            String linea = SC.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(linea);
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número decimal válido (ej: 12.50).");
            }
        }
    }
}