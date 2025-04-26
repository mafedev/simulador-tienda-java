package tienda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

	static String url = "jdbc:mysql://localhost:3306/tienda";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char opc;
		
		// Bucle principal para el menú principal
		do {
			opc = menu();

			switch (opc) {
			case '1':
				comprarProducto();
				break;
			case '2':
				agregarProducto();
				break;
			case '3':
				devolverProducto();
				break;
			case '4':
				eliminarProducto();
				break;
			case '5':
				logicaMenuSecundario();
				break;
			case '6':
				mostrarVentas();
				break;
			case '7':
				mostrarDevoluciones();
				break;
			case '8':
				System.out.println("Saliendo...");
				totalVentas();
				break;
			default:
				System.out.println("Ingrese una opción correcta");
			}

		} while (opc != '8');

	}

	// ------------------------------------------- MENÚS -------------------------------------------
	// Menú principal
	static char menu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("\nBIENVENIDO");
		System.out.println("Ingrese una opción\n 1. Comprar producto\n 2. Añadir producto\n 3. Devolver producto\n 4. Eliminar Producto\n 5. Ver información de los productos\n 6. Ver ventas\n 7. Ver devoluciones\n 8. Salir");
		return sc.nextLine().charAt(0);
	}

	// Menú secundario para ver la información en la opción 4
	static char menuSecundario() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Seleccione que información quiere ver\n 1. Ver todos los productos\n 2. Buscar producto por nombre o ID\n 3. Salir");
		return sc.nextLine().charAt(0);
	}

	// Switch que maneja la logica del segundo menú
	static void logicaMenuSecundario() {
		char menu;
		do {
			menu = menuSecundario();
			switch (menu) {
				case '1':
					mostrarProductos();
					break;
				case '2':
					buscarProducto();
					break;
				case '3':
					System.out.println("Volviendo...");
					break;
				default:
					System.out.println("Ingrese una opción válida");
				}
		} while (menu != '3');
	}

	// Función para comprar un producto
	static void comprarProducto() {
		Scanner sc = new Scanner(System.in);
		mostrarProductos(); // Se llama a la función para muestrar los productos con su información

		System.out.println("\nIngrese el id del producto que quiere comprar");
		int id = sc.nextInt();

		try {
			Connection c = DriverManager.getConnection(url, "root", "root"); // Se conecta a la base de datos
			String consulta = "SELECT * FROM productos WHERE id = ?"; // Genera la consulta para buscar el producto por el id
			PreparedStatement ps = c.prepareStatement(consulta);
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
					PreparedStatement psVenta = c.prepareStatement(venta);
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
					PreparedStatement psCantidad = c.prepareStatement(descontarCantidad);
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

	static void agregarProducto() {
		// tiene que agregar uno que no exista, y si existe que diga que no se puede
		Scanner sc = new Scanner(System.in);
		int id = 0;
		// muestra los productos que

		try {
			Connection c = DriverManager.getConnection(url, "root", "root"); // Creamos la conexión
			String consulta = "SELECT MAX(id) FROM productos";
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(consulta);

			// obtiene el ultimo id de la tabla y le suma 1
			if (rs.next()) {
				id = rs.getInt(1) + 1;
			}

			System.out.println("Los productos disponibles son:");
			mostrarProductos();
			// Info del producto nuevo

			// Que pregunte si quiere agregar un producto nuevo, o uno al stock

			System.out.println("Ingrese el nombre del nuevo producto");
			String nombre = sc.nextLine();
			System.out.println("Ingrese la descripción del producto");
			String descripcion = sc.nextLine();

			mostrarCategoria();
			System.out.println(
					"Ingrese el número de la categoria al que pertenece, si no está ingrese 0, para crearla a continuación");
			int categoria = sc.nextInt();

			if (categoria == 0) {
				System.out.println("Ingrese el nombre de la categoría");
				sc.nextLine(); // Consumimos el salto de línea
				String nombreCategoria = sc.nextLine();

				String comprobarCategoria = "SELECT id FROM categorias WHERE nombre = ?";
				PreparedStatement ps = c.prepareStatement(comprobarCategoria);
				ps.setString(1, nombreCategoria);
				ResultSet rsCategoria = ps.executeQuery();

				if (rsCategoria.next()) { // Si la categoria existe
					categoria = rsCategoria.getInt("id");
					System.out.println("La categoría ya existe, se va a asignar el id correspondiente");
				} else {
					String crearCategoria = "INSERT INTO categorias (nombre) VALUES (?)";
					PreparedStatement psCategoria = c.prepareStatement(crearCategoria);
					psCategoria.setString(1, nombreCategoria);
					psCategoria.executeUpdate();

					System.out.println("La categoría se creó con éxito");
				}
			}

			mostrarSubcategorias();
			System.out.println("Ahora ingrese si pertenece a alguna subcategoría, si no, ingrese 0");
			int subcategoria = sc.nextInt();

			if (subcategoria == 0) {
				subcategoria = -1; // Usamos -1 para representar "sin subcategoría"
			}

			System.out.println("Ingrese el precio del articulo");
			double precio = sc.nextDouble();

			System.out.println("Ingrese la cantidad de articulos");
			int cantidad = sc.nextInt();
			String fila = String.format(
					"INSERT INTO productos (id, nombre, descripcion, categoria_id, subcategoria_id, precio, cantidad) "
							+ "VALUES ('%s','%s', '%s', '%s', %s, '%s', '%s')",
					id, nombre, descripcion, categoria, (subcategoria == 0 ? "NULL" : subcategoria), precio, cantidad);

			s.executeUpdate(fila);
			c.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// da error porque no esta el statement
		// s.executeUpdate(fila);
	}

	static void devolverProducto() {
		Scanner sc = new Scanner(System.in);

		System.out.println("Ingrese el id de la venta que aparece en la factura");
		int ventaId = sc.nextInt();
		
		System.out.println("Ingrese el id del producto a devolver");
		int productoId = sc.nextInt();

		sc.nextLine();
		System.out.println("Indique el motivo de la devolución:");
		String motivo = sc.nextLine();

		try {
			Connection c = DriverManager.getConnection(url, "root", "root");

			// Verifica si la venta existe y si corresponde al producto
			String verificarVenta = "SELECT * FROM ventas WHERE id = ? AND producto_id = ?";
			PreparedStatement psVerificar = c.prepareStatement(verificarVenta);
			psVerificar.setInt(1, ventaId);
			psVerificar.setInt(2, productoId);
			ResultSet rs = psVerificar.executeQuery();

			if (rs.next()) {
				int cantidadProductos = rs.getInt("cantidad");
				int dineroDevuelto = rs.getInt("total");

				// Inserta la devolución
				String insertarDevolucion = "INSERT INTO devoluciones (venta_id, producto_id, motivo, dinero_devuelto) VALUES (?, ?, ?, ?)";
				PreparedStatement psDevolucion = c.prepareStatement(insertarDevolucion);
				psDevolucion.setInt(1, ventaId);
				psDevolucion.setInt(2, productoId);
				psDevolucion.setString(3, motivo);
				psDevolucion.setInt(4, dineroDevuelto);
				psDevolucion.executeUpdate();

				// Devolver la cantidad en el inventario
				String actualizarStock = "UPDATE productos SET cantidad = cantidad + ? WHERE id = ?";
				PreparedStatement psActualizar = c.prepareStatement(actualizarStock);
				psActualizar.setInt(1, cantidadProductos);
				psActualizar.setInt(2, productoId);
				psActualizar.executeUpdate();

				String descontarDinero = "UPDATE ventas SET total = 0, devueLto = TRUE WHERE id = ?";
				PreparedStatement psDescontar = c.prepareStatement(descontarDinero);
				psDescontar.setInt(1, ventaId);
				psDescontar.executeUpdate();

				System.out.println("Devolución registrada correctamente");

			} else {
				System.out.println("No se encontró una venta con esos datos");
			}

			c.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Agregarlo al menú
	static void eliminarProducto() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Ingrese el id del producto que quiere eliminar");
		int id = sc.nextInt();

		try {
			Connection c = DriverManager.getConnection(url, "root", "root");

			String eliminar = "DELETE FROM productos WHERE id = ?";
			PreparedStatement ps = c.prepareStatement(eliminar);
			ps.setInt(1, id);

			int filasAfectadas = ps.executeUpdate();
			if (filasAfectadas > 0) {
				System.out.println("Producto eliminado correctamente");
			} else {
				System.out.println("No se encontró el producto con el ID especificado");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	static void buscarProducto() {
		Scanner sc = new Scanner(System.in);
		char opc;
		try {
			Connection c = DriverManager.getConnection(url, "root", "root");

			do {
				System.out.println(" 1. Buscar por nombre\n 2. Buscar por ID\n 3. Salir");
				opc = sc.nextLine().charAt(0);

				switch (opc) {
					case '1':
						System.out.println("Ingrese el nombre del producto");
						String nombre = sc.nextLine();
	
						String buscar = "SELECT * FROM productos WHERE nombre = ?";
						PreparedStatement ps = c.prepareStatement(buscar);
						ps.setString(1, nombre);
						ResultSet rs = ps.executeQuery();
	
						if (rs.next()) {
							System.out.println("ID: " + rs.getInt("id"));
							System.out.println("Nombre: " + rs.getString("nombre"));
							System.out.println("Descripción: " + rs.getString("descripcion"));
							System.out.println("Precio: " + rs.getDouble("precio"));
							System.out.println("Cantidad: " + rs.getInt("cantidad"));
							System.out.println("____________________");
						}
						break;
					case '2':
						System.out.println("Ingrese el ID del producto");
						int id = sc.nextInt();
						
						
						
						break;
					case '3':
						System.out.println("Volviendo...");
						break;
					default:
						System.out.println("Opción inválida");
				}
			} while (opc != '3');

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	static void mostrarProductos() {
		try {
			Connection c = DriverManager.getConnection(url, "root", "root");
			Statement s = c.createStatement();
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

	static void mostrarCategoria() {
		try {
			Connection c = DriverManager.getConnection(url, "root", "root");
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM categorias");

			while (rs.next()) {
				System.out.println("ID: " + rs.getInt("id"));
				System.out.println("Nombre: " + rs.getString("nombre"));
				System.out.println();
			}
			System.out.println("____________________\n");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static void mostrarSubcategorias() {
		try {
			Connection c = DriverManager.getConnection(url, "root", "root");
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM subcategorias");

			while (rs.next()) {
				System.out.println("ID: " + rs.getInt("id"));
				System.out.println("Nombre: " + rs.getString("nombre"));
				System.out.println("Categoria: " + rs.getInt("categoria_id"));
				System.out.println();
			}
			System.out.println("____________________\n");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void mostrarVentas() {
		try {
			Connection c = DriverManager.getConnection(url, "root", "root");
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM ventas");

			while (rs.next()) {
				System.out.println("ID: " + rs.getInt("id"));
				System.out.println("Nombre: " + rs.getInt("producto_id"));
				System.out.println("Categoria: " + rs.getInt("cantidad"));
				System.out.println("Total: " + rs.getDouble("total"));
				
				boolean devuelto = rs.getBoolean("devuelto");
				if(devuelto) {
					System.out.println("Ha sido devuelto");
				} 
				
				System.out.println();
			}
			System.out.println("____________________\n");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void totalVentas() {
		try {
			Connection c = DriverManager.getConnection(url, "root", "root");
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM ventas");

			while (rs.next()) {
				System.out.println("ID: " + rs.getInt("id"));
				System.out.println("Nombre: " + rs.getInt("producto_id"));
				System.out.println("Categoria: " + rs.getInt("cantidad"));
				System.out.println("Total: " + rs.getDouble("total"));
				
				boolean devuelto = rs.getBoolean("devuelto");
				if(devuelto) {
					System.out.println("Ha sido devuelto");
				} 
				
				System.out.println();
			}
			System.out.println("____________________\n");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void mostrarDevoluciones() {
		try {
			Connection c = DriverManager.getConnection(url, "root", "root");
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM devoluciones");
			
			while(rs.next()) {
				System.out.println("   ID: " + rs.getInt("id"));
				System.out.println("    Id de la venta: " + rs.getInt("producto_id"));
				System.out.println("    Motivo: " + rs.getString("motivo"));
				System.out.println("    Dinero: " + rs.getDouble("dinero_devuelto"));
				System.out.println();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}