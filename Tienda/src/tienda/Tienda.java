package tienda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Tienda {
	private Connection conexion;
	
	public Tienda() {}
	
	public Tienda(Connection c) {
		this.conexion = c;
	}
	
	public void venderProducto() {
		Scanner sc = new Scanner(System.in);
		mostrarProductos(); // Se llama a la función para muestrar los productos con su información

		System.out.println("\nIngrese el id del producto que quiere comprar");
		int id = sc.nextInt();

		try {
			String consulta = "SELECT * FROM productos WHERE id = ?"; // Genera la consulta para buscar el producto por el id
			PreparedStatement ps = conexion.prepareStatement(consulta);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery(); // Ejecuta la consulta sql

			if (rs.next()) { // Si el id existe
				// Obtiene el nombre, el precio y la cantidad de productos
				String producto = rs.getString("nombre");
				double precio = rs.getDouble("precio");
				int cantidadProducto = rs.getInt("cantidad");

				// Muestra la información del producto
				System.out.println("   Producto: " + producto);
				System.out.println("   Precio: " + precio + " €");
				System.out.println("   En stock: " + cantidadProducto);

				if (cantidadProducto > 0) { // Comprueba que hallan productos disponibles
					boolean valido = true;

					System.out.println("¿Cuántos quiere comprar?");
					int cantidad = sc.nextInt();

					// Comprueba que sea la cantidad sea válida
					if (cantidad <= 0 || cantidad > cantidadProducto) {
						valido = false;
					}

					while (!valido) { // Si la cantidad introducida es inválida, entonces entra en el bucle hasta que ingrese una cantidad válida
						System.out.println("Ingrese una cantidad válida");
						cantidad = sc.nextInt();

						if (cantidad <= 0 || cantidad > cantidadProducto) { // Comprueba la cantidad hasta que sea válida
							valido = false;
						} else {
							valido = true;
						}
					}

					// Se calcula el total de la compra
					double total = precio * cantidad;

					// Se muestra la descripción de la compra
					System.out.println("\n"
							+ "      /\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\ \r\n"
							+ "     │                                                  |\r\n"
							+ "     │       @                                          |\r\n"
							+ "     │     @@@@@      Producto: " + producto +"\r\n"
							+ "     │    @  @        Precio c/u: " + precio + " €\r\n"
							+ "     │     @@@@       Cantidad: " + cantidad + "\r\n"
							+ "     │       @ @@                                       |\r\n"
							+ "     │       @  @                                       |\r\n"
							+ "     │    @@@@@@      Total: " + total + " €\r\n"
							+ "     │       @                                          |\r\n"
							+ "      \\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/ \r\n");
					
					// Insertar venta
					String venta = "INSERT INTO ventas (producto_id, cantidad, total) VALUES (?,?,?)";
					PreparedStatement psVenta = conexion.prepareStatement(venta);
					psVenta.setInt(1, id);
					psVenta.setInt(2, cantidad);
					psVenta.setDouble(3, total);

					int filasAfectadas = psVenta.executeUpdate();
					if (filasAfectadas > 0) {
						System.out.println("Venta registrada correctamente");
					} else {
						System.out.println("Error al registrar la venta");
					}

					// Descontar del stock
					String descontarCantidad = "UPDATE productos SET cantidad = cantidad - ? WHERE id = ?";
					PreparedStatement psCantidad = conexion.prepareStatement(descontarCantidad);
					psCantidad.setInt(1, cantidad);
					psCantidad.setInt(2, id);

					int actualizados = psCantidad.executeUpdate();
					if (actualizados > 0) {
						System.out.println("Stock actualizado correctamente");
					} else {
						System.out.println("Error al actualizar el stock");
					}

				} else {
					System.out.println("Lo sentimos, no se puede realizar la compra, no hay productos disponibles");
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void mostrarProductos() {
		try {
			Statement s = conexion.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM productos");
			
			System.out.println("  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
			System.out.println("  │ INSTRUMENTOS");
			while (rs.next()) {
				System.out.println("  │   ID: " + rs.getString("id"));
				System.out.println("  │     Nombre: " + rs.getString("nombre"));
				System.out.println("  │     Precio: " + rs.getDouble("precio") + " €");
				System.out.println("  │     En stock: " + rs.getInt("cantidad"));
				System.out.println("  │");
			}
			System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
