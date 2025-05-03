package tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Tienda {
	private InfoTienda infoTienda;
	private Connection conexion;
	private double totalVenta = 0;
	private ArrayList<Producto> productos = new ArrayList<Producto>();

	// --------------------- Constructores ---------------------
	public Tienda() {
	}
	
	public Tienda(Connection conexion) {
		this.conexion = conexion;
		this.infoTienda = new InfoTienda(conexion); // Inicializa infoTienda con la conexión
	}

	public Tienda(InfoTienda infoTienda, Connection conexion, double totalVenta, ArrayList<Producto> productos) {
		this.infoTienda = new InfoTienda(conexion);
		this.conexion = conexion;
		this.totalVenta = totalVenta;
		this.productos = productos;
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

	public InfoTienda getInfoTienda() {
		return infoTienda;
	}

	public void setInfoTienda(InfoTienda infoTienda) {
		this.infoTienda = infoTienda;
	}

	public ArrayList<Producto> getProductos() {
		return productos;
	}

	public void setProductos(ArrayList<Producto> productos) {
		this.productos = productos;
	}

	// --------------------- Métodos ---------------------
	public void cargarProductos() {
        infoTienda.obtenerProductos(productos);
    }
	
	public void venderProducto() {
		Scanner sc = new Scanner(System.in);
		cargarProductos(); // Se llama el método para que cargue los productos de la tabla
		infoTienda.mostrarInfoProductos(productos); // Se llama a la función para mostrar los productos con su información

		System.out.println("\nIngrese el id del producto que quiere comprar");
		int id = sc.nextInt();

		for (Producto p : productos) {
			if (p.getId() == id) {
				p.mostrarInfoDetallada(0); // Muestra la información del producto si coincide con el id

				if (p.getCantidad() > 0) { // Verifica si hay productos disponibles
					boolean valido = true;

					System.out.println("¿Cuántos quiere comprar?");
					int cantidad = sc.nextInt();

					if (cantidad <= 0 || cantidad > p.getCantidad()) { // Comprueba que la cantidad sea válida
						valido = false;
					}

					while (!valido) { // Si la cantidad introducida es inválida, entra en el bucle hasta que ingrese una cantidad válida
						System.out.println("Ingrese una cantidad válida");
						cantidad = sc.nextInt();
						
						valido = (cantidad <= 0 || cantidad > p.getCantidad()) ? false : true; // Comprueba la cantidad hasta que sea válida
					}

					this.totalVenta = p.getPrecio() * cantidad; // Se calcula el total de la compra

					// Luego se inserta dentro de la tabla ventas
					Venta v = new Venta();
					v.registrarVenta(conexion, id, cantidad, totalVenta); // Se registra la compra llamando al método

					v.mostrarTicket(conexion, p, totalVenta, cantidad); // Muestra el recibo de compra
					
					p.actualizarStockCompra(conexion, cantidad, id); // Descuenta del stock los productos comprados
				} else {
					System.out.println("Lo sentimos, no se puede realizar la compra, no hay productos disponibles"); // Si la cantidad es 0, no se puede realizar la compra
				}
				return; // Sale del método luego de encontrar el producto y hacer la venta
			}
		}
		System.out.println("No se encontro el producto"); // Si después de recorren el ArrayList de productos no lo encuentra, entonces muestra el mensaje
	}

	public void agregarProducto() {
		Scanner sc = new Scanner(System.in);
		int id = 0, subcategoria = 0, categoria = 0, cantidad = 0;
		boolean categoriaValida = false, subcategoriaValida = false, precioValido = false, cantidadValida = false; // Por defecto son false para que entren en los bucles
		double precio = 0;
		String nombreSubcategoria = "";
		cargarProductos(); // Carga el arraylist productos

		try {
			System.out.println("Estos son los productos disponibles en este momento:");
			infoTienda.mostrarInfoProductos(productos); // Muestra los productos disponibles

			// Pide la información del nuevo producto
			System.out.println("Ingrese el nombre del nuevo producto");
			String nombre = sc.nextLine();

			System.out.println("Ingrese la descripción del nuevo producto");
			String descripcion = sc.nextLine();

			// Verifica si el producto ya existe
			String consulta = "SELECT * FROM productos WHERE nombre = ? OR descripcion = ?";
			PreparedStatement psRepetido = conexion.prepareStatement(consulta);
			psRepetido.setString(1, nombre);
			psRepetido.setString(2, descripcion);
			ResultSet rs = psRepetido.executeQuery();

		    if (rs.next()) { // Si hay algún resultado, significa que el producto ya existe
		        System.out.println("El producto ya existe");
		        return; // Se sale del metodo para evitar tener productos duplicados
		    }
			
			Categoria.mostrarCategorias(conexion); // Muestra las categorías existentes
			while (!categoriaValida) {
				System.out.println("Ingrese el número de la categoría correspondiente, o ingrese 0 si desea crear una nueva categoría"); // Selecciona la categoría o la crea	
				categoria = sc.nextInt();

				String verificarCategoria = "SELECT id FROM categorias WHERE id = ?"; // Se verifica si la categoría ingresada es válida
				PreparedStatement psVerificar = conexion.prepareStatement(verificarCategoria);
				psVerificar.setInt(1, categoria);
				ResultSet rsVerificar = psVerificar.executeQuery();

				if (rsVerificar.next() || categoria == 0) { // Si hay un resultado o se ingresa cero, se toma como válido
					categoriaValida = true;
				} else {
					System.out.println("    ⚠ La categoría ingresada no es válida, inténtelo nuevamente");
				}
			}

			if (categoria == 0) { // Si ingresa un 0, se pide la información para crear la nueva categoría
				System.out.println("Ingrese el nombre de la nueva categoría");
				sc.nextLine();
				String nombreCategoria = sc.nextLine();

				String comprobarCategoria = "SELECT id FROM categorias WHERE nombre = ?"; // Se comprueba si la categoría ya existe buscando el nombre en la tabla
				PreparedStatement ps = conexion.prepareStatement(comprobarCategoria);
				ps.setString(1, nombreCategoria);
				ResultSet rsCategoria = ps.executeQuery();

				if (rsCategoria.next()) { // Si la categoría existe, asigna el id de esa categoría
					categoria = rsCategoria.getInt("id");
					System.out.println("\nLa categoría ya existe, se va a asignar el id correspondiente");
				} else { // Si no existe, se crea la nueva categoría insertandola en la tabla
					String crearCategoria = "INSERT INTO categorias (nombre) VALUES (?)";
					PreparedStatement psCategoria = conexion.prepareStatement(crearCategoria);
					psCategoria.setString(1, nombreCategoria);
					psCategoria.executeUpdate();

					categoria = Categoria.encontrarIdMaximo(conexion); // Usa el método para obtener el id de la nueva categoría
				}
				
				System.out.println("\nAhora ingrese el nombre de la nueva subcategoria a la que pertenece, si no pertenece a ninguna ingrese null"); // Igual que antes, solo que si no pertenece a ninguna subcategoria se deja como null
				nombreSubcategoria = sc.nextLine();

				if (nombreSubcategoria.equalsIgnoreCase("null")) { // Si no tiene subcategoría, entonces el número de la subcategoría va a ser 0, para que luego al insertar el producto en la tabla se inserte un null
				    subcategoria = 0;
				} else {
				    String crearSubcategoria = "INSERT INTO subcategorias (nombre, categoria_id) VALUES (?, ?)"; // Si no es null, se crea la subcategoría
				    PreparedStatement psSubcategoria = conexion.prepareStatement(crearSubcategoria);
				    psSubcategoria.setString(1, nombreSubcategoria);
				    psSubcategoria.setInt(2, categoria);
				    psSubcategoria.executeUpdate();

				    subcategoria = Subcategoria.encontrarIdMaximo(conexion); // Obtiene el ID de la nueva subcategoría
				}
			} else { // Si seleccionó una categoría, muestra solo las subcategorías correspondientes a esa categoría
				Subcategoria.mostrarSubcategorias(conexion, categoria);
				while (!subcategoriaValida) {
					System.out.println("Ahora ingrese el número de la subcategoría a la que pertenece, si no pertenece a ninguna ingrese 0");
					subcategoria = sc.nextInt();

					String verificarSubcategoria = "SELECT id FROM subcategorias WHERE id = ? AND categoria_id = ?"; // Verifica que la subcategoría corresponda con la categoría
					try {
						PreparedStatement psVerificar = conexion.prepareStatement(verificarSubcategoria);
						psVerificar.setInt(1, subcategoria);
						psVerificar.setInt(2, categoria);
						ResultSet rsVerificar = psVerificar.executeQuery();

						if (rsVerificar.next() || subcategoria == 0) { // Si hay resultado o ingreso un 0, la subcategoría es válida
							subcategoriaValida = true;
						} else {
							System.out.println("    ⚠ La subcategoría ingresada no es válida, inténtelo nuevamente");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			// Solicita el precio y la cantidad
			while(!precioValido || !cantidadValida) { // Comprueba que tanto el precio como la cantidad sean válidos
				if(!precioValido && !cantidadValida) { // Si ambos son inválidos
					System.out.println("\nIngrese el precio del articulo");
					precio = sc.nextDouble();
					
					System.out.println("\nIngrese la cantidad de articulos");
					cantidad = sc.nextInt();
					
				} else if (!precioValido && cantidadValida) { // Si solo el precio es inválido
					System.out.println("\nIngrese el precio del articulo");
					precio = sc.nextDouble();
				} else if(precioValido && !cantidadValida) { // Si solo la cantidad es inválida
					System.out.println("\nIngrese la cantidad de articulos");
					cantidad = sc.nextInt();
				}
				
				// Comprueba si lo introducido es válido, que tanto el precio como la cantidad no sean negativos o iguales a 0
				if (precio > 0 && cantidad <= 0) {
					System.out.println("    ⚠ La cantidad ingresada no es válida, inténtelo nuevamente");
					precioValido = true;
				} else if (precio <= 0 && cantidad > 0) {
					System.out.println("    ⚠  El precio ingresado no es válido, inténtelo nuevamente");
					cantidadValida = true;
				} else if (precio <= 0 || cantidad <= 0) {
					System.out.println("    ⚠ El precio y cantidad ingresados no son válidos, inténtelo nuevamente");
				} else if (precio > 0 && cantidad > 0) {
					precioValido = true;
					cantidadValida = true;
				}
			}
			
			// Se inserta en la tabla productos el nuevo producto
			Statement s = conexion.createStatement();
			String fila = String.format("INSERT INTO productos (nombre, descripcion, categoria_id, subcategoria_id, precio, cantidad) VALUES ('%s', '%s', '%d', %s, '%f', '%d')",
					nombre, descripcion, categoria, (subcategoria == 0 ? "NULL" : subcategoria), precio, cantidad); // Si la subcategoria es 0, es decir, no tiene subcategoría, inserta un null

			int confirmar = s.executeUpdate(fila); // Es para confirmar que el producto se insertó correctamente en la tabla y mostrar el mensaje de confirmación

			if (confirmar > 0) {
			    System.out.println("¡Producto agregado correctamente!");
			} else {
			    System.out.println("Ocurrió un problema al agregar el producto, inténtelo nuevamente");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void devolverProducto() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Para realizar la devolución ingrese la siguiente información:\n 1. Ingrese el id de la compra, se encuentra en la parte superior del ticket");
		int ventaId = sc.nextInt();
		sc.nextLine();

		try {
			// Verifica si la venta existe
			String verificarVenta = "SELECT * FROM ventas WHERE id = ?";
			PreparedStatement psVerificar = conexion.prepareStatement(verificarVenta);
			psVerificar.setInt(1, ventaId);
			ResultSet rs = psVerificar.executeQuery();

			// Verifica que no se haya hecho una devolución con el mismo venta_id
			String verificarDevolucion = "SELECT * FROM devoluciones WHERE venta_id = ?";
			PreparedStatement psDevolucion = conexion.prepareStatement(verificarDevolucion);
			psDevolucion.setInt(1, ventaId);
			ResultSet rsDevolucion = psDevolucion.executeQuery();

			if (rs.next()) { // Si la venta existe
			    if (rsDevolucion.next()) { // Si ya hay una devolución registrada
			        System.out.println(" ⚠ No se puede realizar la acción, ya hay una devolución registrada con este id");
			    } else { // Si no hay una devolución
					Venta v = new Venta(); // Crea una instancia de la clase para acceder a sus métodos 
			        v.mostrarInfoVentaId(conexion, ventaId); // Muestra la información de la venta
					
					System.out.println("\n 2. Indique el motivo de la devolución:");
					String motivo = sc.nextLine();

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
			            v.actualizarColumnaDevuelto(conexion, ventaId); // Llama el método para modificar la columna devuelto en ventas

			            // Se inserta la devolución en la tabla devoluciones
			            Devolucion.registrarDevolucion(conexion, ventaId, productoId, motivo, dineroDevuelto);
			            System.out.println("\n¡Devolución registrada correctamente!");
			        }
			    }
			} else {
			    System.out.println("No se encontró una venta con esos datos");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void eliminarProducto() {
		Scanner sc = new Scanner(System.in);
		cargarProductos();
		infoTienda.mostrarInfoProductos(productos);
		System.out.println("Ingrese el id del producto que quiere eliminar");
		int id = sc.nextInt();

		try {
			/*
			 * Se verifica si el producto tiene alguna venta relacionada. Si la tiene, no se
			 * puede borrar el producto, porque la tabla de ventas tiene una clave foránea
			 * que apunta a la tabla de productos. Entonces lo más sencillo es no dejar que
			 * lo elimine, porque por de lo contrario se tendría que eliminar también la venta (lo
			 * cual no tiene mucho sentido) o editar la tabla ventas, pero sería más complejo
			 */
			
			String contarVentas = "SELECT COUNT(*) FROM ventas WHERE producto_id = ?";
			PreparedStatement psContar = conexion.prepareStatement(contarVentas);
			psContar.setInt(1, id);
			ResultSet rsContar = psContar.executeQuery();

			if (rsContar.next()) {
				int cantidadVentas = rsContar.getInt(1);
				if (cantidadVentas > 0) {
					System.out.println("No se puede eliminar el producto porque tiene ventas asociadas");
					return;
				}
			}

			// Si no tiene ventas relacionadas, se elimina el producto
			String eliminar = "DELETE FROM productos WHERE id = ?";
			PreparedStatement ps = conexion.prepareStatement(eliminar);
			ps.setInt(1, id);

			int confirmar = ps.executeUpdate();
			if (confirmar > 0) {
				System.out.println("¡Producto eliminado correctamente!");
			} else {
				System.out.println("No se encontró el producto");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void actualizarPrecio() {
        Scanner sc = new Scanner(System.in);
        cargarProductos();
        
        System.out.println("Ingrese el ID del producto");
        int id = sc.nextInt();

		for (Producto p : productos) {
			if (p.getId() == id) {
				p.actualizarPrecio(conexion);
				return; // Si encuentra el producto, sale del método
			}
		}
        
		System.out.println("Producto no encontrado"); // Si no encuentra el produco muestra el mensaje
    }
}
