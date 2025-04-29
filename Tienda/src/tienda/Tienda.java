package tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Tienda {
	private Connection conexion;
	private double totalVenta = 0;
	private ArrayList<Producto> productos = new ArrayList<Producto>();

	// --------------------- Constructores ---------------------
	public Tienda() {
	}

	public Tienda(Connection c, double totalVenta) {
		this.conexion = c;
		this.totalVenta = totalVenta;
	}

	public Tienda(Connection c) {
		this.conexion = c;
	}

	// --------------------- Getters y Setters ---------------------
	public Connection getConexion() {
		return conexion;
	}

	public void setConexion(Connection conexion) {
		this.conexion = conexion;
	}

	public double getTotalVenta() {
		return totalVenta;
	}

	public void setTotalVenta(double totalVenta) {
		this.totalVenta = totalVenta;
	}

	// --------------------- Métodos ---------------------
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

			if (rs.next()) { // Confirma si el id existe
				// Obtiene el nombre, el precio y la cantidad del producto
				Producto producto = new Producto();
				producto.setId(rs.getInt("id"));
				producto.setNombre(rs.getString("nombre"));
				producto.setDescripcion(rs.getString("descripcion"));
				producto.setCategoriaId(rs.getInt("categoria_id"));
				producto.setSubcategoriaId(rs.getInt("subcategoria_id"));
				producto.setPrecio(rs.getDouble("precio"));
				producto.setCantidad(rs.getInt("cantidad"));

				// Muestra la información del producto
				producto.mostrarInfoDetallada();

				if (producto.getCantidad() > 0) { // Comprueba que hallan productos disponibles
					boolean valido = true;

					System.out.println("¿Cuántos quiere comprar?");
					int cantidad = sc.nextInt();

					// Comprueba que sea la cantidad sea válida
					if (cantidad <= 0 || cantidad > producto.getCantidad()) {
						valido = false;
					}

					while (!valido) { // Si la cantidad introducida es inválida, entonces entra en el bucle hasta que ingrese una cantidad válida
						System.out.println("Ingrese una cantidad válida");
						cantidad = sc.nextInt();

						if (cantidad <= 0 || cantidad > producto.getCantidad()) { // Comprueba la cantidad hasta que sea válida
							valido = false;
						} else {
							valido = true;
						}
					}

					// Se calcula el total de la compra
					this.totalVenta = producto.getPrecio() * cantidad;

					// Insertar venta
					Venta v = new Venta();
					v.registrarVenta(conexion, id, cantidad, totalVenta);
					
					// Muestra el recibo de compra
					v.mostrarTicket(conexion, producto, totalVenta, cantidad);


					// Descontar del stock
					producto.actualizarStockCompra(conexion, cantidad, id);

				} else {
					System.out.println("Lo sentimos, no se puede realizar la compra, no hay productos disponibles");
				}

			} else { // Si el id no existe
				System.out.println("Producto no encontrado.");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void agregarProducto() {
		Scanner sc = new Scanner(System.in);
		int id = 0;

		try {
			// Muestra los productos disponibles
			System.out.println("Los productos disponibles son:");
			mostrarProductos();

			// Información del producto nuevo
			System.out.println("Ingrese el nombre del nuevo producto");
			String nombre = sc.nextLine();

			System.out.println("Ingrese la descripción del producto");
			String descripcion = sc.nextLine();

			// Selección de categoría
			System.out.println("Ingrese el número de la categoría correspondiente o ingrese 0 si desea crear una nueva categoría");
			int categoria = sc.nextInt();

			if (categoria == 0) { // Si ingresa un 0, se pide la información para crear la nueva categoría
				System.out.println("Ingrese el nombre de la categoría");
				sc.nextLine();
				String nombreCategoria = sc.nextLine();

				// Comprueba si la categoría ya existe
				String comprobarCategoria = "SELECT id FROM categorias WHERE nombre = ?";
				PreparedStatement ps = conexion.prepareStatement(comprobarCategoria);
				ps.setString(1, nombreCategoria);
				ResultSet rsCategoria = ps.executeQuery();

				if (rsCategoria.next()) { // Si la categoría existe, asigna el id de esa categoría
					categoria = rsCategoria.getInt("id");
					System.out.println("La categoría ya existe, se va a asignar el id correspondiente");
				} else {
					// Si no existe, se crea la nueva categoría insertandola en la tabla
					String crearCategoria = "INSERT INTO categorias (nombre) VALUES (?)";
					PreparedStatement psCategoria = conexion.prepareStatement(crearCategoria);
					psCategoria.setString(1, nombreCategoria);
					psCategoria.executeUpdate();

					// Se obtiene el último id en la tabla categorias para luego insertarlo en la
					// tabla productos
					String obtenerId = "SELECT MAX(id) FROM categorias";
					PreparedStatement psId = conexion.prepareStatement(obtenerId);
					ResultSet rsUltimoId = psId.executeQuery();

					if (rsUltimoId.next()) {
						categoria = rsUltimoId.getInt(1); // Obtiene solo el mayor id
						System.out.println("La categoría se creó con éxito");
					}
				}
			}

			// Muestra las subcategorías que hay
			Subcategoria.mostrarSubcategorias(conexion);
			System.out.println("Ahora ingrese si pertenece a alguna subcategoría, si no, ingrese 0");
			int subcategoria = sc.nextInt();

			// Solicita el precio y la cantidad
			System.out.println("Ingrese el precio del articulo");
			double precio = sc.nextDouble();

			System.out.println("Ingrese la cantidad de articulos");
			int cantidad = sc.nextInt();

			// Se inserta en la tabla productos el nuevo producto
			Statement s = conexion.createStatement();
			String fila = String.format(
					"INSERT INTO productos (nombre, descripcion, categoria_id, subcategoria_id, precio, cantidad) VALUES ('%s', '%s', '%d', %s, '%f', '%d')",
					nombre, descripcion, categoria, (subcategoria == 0 ? "NULL" : subcategoria), precio, cantidad); // Si la subcategoria es 0, es decir, no tiene subcategoría, inserta un null
			s.executeUpdate(fila);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void devolverProducto() {
		// que compruebe que no esta roto o en mal estado
		Scanner sc = new Scanner(System.in);

		System.out.println("Ingrese el id de la compra, se encuentra en la parte superior del ticket");
		int ventaId = sc.nextInt();

		sc.nextLine();
		System.out.println("Indique el motivo de la devolución:");
		String motivo = sc.nextLine();

		try {
			// Verifica si la venta existe y si corresponde al producto
			String verificarVenta = "SELECT * FROM ventas WHERE id = ?";
			PreparedStatement psVerificar = conexion.prepareStatement(verificarVenta);
			psVerificar.setInt(1, ventaId);
			ResultSet rs = psVerificar.executeQuery();

			String verificarDevolucion = "SELECT * FROM devoluciones WHERE venta_id = ?";
			PreparedStatement psDevolucion = conexion.prepareStatement(verificarDevolucion);
			psDevolucion.setInt(1, ventaId);
			ResultSet rsDevolucion = psDevolucion.executeQuery();

			if (rs.next() && !rsDevolucion.next()) {
				int cantidadProductos = rs.getInt("cantidad");
				int dineroDevuelto = rs.getInt("total");

				// Obtiene el id del producto a devolver
				String idProducto = "SELECT producto_id FROM ventas WHERE id = ?";
				PreparedStatement ps = conexion.prepareStatement(idProducto);
				ps.setInt(1, ventaId);
				ResultSet rsId = ps.executeQuery();

				int productoId = 0;
				if (rsId.next()) {
					productoId = rsId.getInt("producto_id");
					// Se inserta la devolución en la tabla devoluciones
					Devolucion.registrarDevolucion(conexion, ventaId, productoId, motivo, dineroDevuelto);

					// Y se "actualiza" el stock con los productos devueltos
					Producto.actualizarStockDevolucion(conexion, cantidadProductos, productoId);
					
					// Llama el método de Venta, para que descuente el dinero devuelto de la tabla
					Venta v = new Venta();
					v.descontarDevolucion(conexion, ventaId);

					System.out.println("Devolución registrada correctamente");
				}

			} else if (rsDevolucion.next()) {
				System.out.println("No se puede realizar la devolcuion, ya hay una devolucion registrada con este id");
			} else {
				System.out.println("No se encontró una venta con esos datos");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void buscarProducto() {
		Scanner sc = new Scanner(System.in);
		char opc;
		try {
			do {
				System.out.println(" 1. Buscar por nombre\n 2. Buscar por ID\n 3. Salir");
				opc = sc.nextLine().charAt(0);

				switch (opc) {
				case '1':
					System.out.println("Ingrese el nombre del producto");
					String nombre = sc.nextLine();

					String buscar = "SELECT * FROM productos WHERE nombre = ?";
					PreparedStatement ps = conexion.prepareStatement(buscar);
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
					obtenerProductos();
					System.out.println("Ingrese el ID del producto");
					int id = sc.nextInt();
					sc.nextLine();

					for (Producto p : productos) {
						if (p.getId() == id) {
							p.mostrarInfoDetallada();
						}
					}

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

	public void eliminarProducto() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Ingrese el id del producto que quiere eliminar");
		int id = sc.nextInt();

		try {
			String eliminar = "DELETE FROM productos WHERE id = ?";
			PreparedStatement ps = conexion.prepareStatement(eliminar);
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

	public void mostrarProductos() {
		obtenerProductos();

		System.out.println("  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("  │ INSTRUMENTOS");

		for (Producto p : productos) {
			p.mostrarInfo();
		}

		System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
	}

	public void obtenerProductos() {
		productos.clear(); // Es para evitar que se dupliquen los productos al llamar el método

		try {
			String consulta = "SELECT * FROM productos";
			PreparedStatement ps = conexion.prepareStatement(consulta);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Producto p = new Producto();

				p.setId(rs.getInt("id"));
				p.setNombre(rs.getString("nombre"));
				p.setDescripcion(rs.getString("descripcion"));
				p.setCategoriaId(rs.getInt("categoria_id"));
				p.setSubcategoriaId(rs.getInt("subcategoria_id")); // o puedes usar rs.wasNull() si puede ser NULL
				p.setPrecio(rs.getDouble("precio"));
				p.setCantidad(rs.getInt("cantidad"));

				productos.add(p);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
