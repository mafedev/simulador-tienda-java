package tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class InfoTienda {
    private Connection conexion;

	// --------------------- Constructores ---------------------
    public InfoTienda() {}

    public InfoTienda(Connection conexion) {
        this.conexion = conexion;
    }

	// --------------------- Getters y Setters ---------------------
    public Connection getConexion() {
    	return conexion;
    }
    
    public void setConexion(Connection conexion) {
    	this.conexion = conexion;
    }
    
    // --------------------- Métodos para buscar ---------------------
    // Busca el prodcuto por el nombre en el arrayList productos
	public void buscarProductoPorNombre(ArrayList<Producto> productos) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Ingrese el nombre del producto (con tíldes):");
		String nombre = sc.nextLine();

		for (Producto p : productos) { // Itera sobre la lista de productos
			if (p.getNombre().equalsIgnoreCase(nombre)) {
				System.out.println();
				p.mostrarInfoDetallada(0); // Muestra la información detallada del producto
				return;
			}
		}
		System.out.println("No se encontró el producto"); // Si no lo encuentra muestra el mensaje
	}
	
    // Busca el producto por el ID en el arrayList productos
	public void buscarProductoPorId(ArrayList<Producto> productos) {
		Scanner sc = new Scanner(System.in);
	    System.out.println("Ingrese el ID del producto:");
	    int id = sc.nextInt();

	    for (Producto p : productos) { // Itera sobre la lista de productos
	        if (p.getId() == id) {
	            p.mostrarInfoDetallada(0); // Muestra la información detallada del producto
	            return; // Sale del método
	        }
	    }
		System.out.println("No se encontró el producto"); // Si no lo encuentra muestra el mensaje
	}

	// Busca los productos por el número de la categoría en el arrayList productos
	public void buscarProductoCategoria(ArrayList<Producto> productos) {
	    Scanner sc = new Scanner(System.in);
	    int id = 0;
	    System.out.println("Ingrese el nombre de la categoría: ");
	    String categoria = sc.nextLine();

	    try {
	        String consulta = "SELECT id FROM categorias WHERE nombre = ?"; // Si el nombre coincide selecciona el id
	        PreparedStatement ps = conexion.prepareStatement(consulta);
	        ps.setString(1, categoria);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            id = rs.getInt("id"); // Obtiene el id de la categoría, para luego buscar con ese id los productos que coincidan
	        } else {
	            System.out.println("Categoría no encontrada");
	            return; // Sale del método si no se encuentra la categoría
	        }

	        System.out.println("\n  ╔══════════════════════════════════════════════════════");
	        System.out.println("  ║           PRODUCTOS QUE COINCIDEN:");
	        for (Producto p : productos) {
	            if (p.getCategoriaId() == id) { // Itera sobre los productos y solo muestra la inforamción de los que coincidan con el número de la categoría
	                p.mostrarInfoDetallada(1);
	            }
	        }
	        System.out.println("  ╚══════════════════════════════════════════════════════");

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// Busca los productos por el número de la subcategoría en el arrayList productos
	public void buscarProductoSubcategoria(ArrayList<Producto> productos) {
		Scanner sc = new Scanner(System.in);
	    int id = 0;
	    System.out.println("Ingrese el nombre de la subcategoría: ");
	    String subcategoria = sc.nextLine();

	    try {
	        String consulta = "SELECT id FROM subcategorias WHERE nombre = ?";
	        PreparedStatement ps = conexion.prepareStatement(consulta);
	        ps.setString(1, subcategoria);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            id = rs.getInt("id"); // Obtiene el id de la subcategoría, para luego buscar los que coincidan
	        } else {
	            System.out.println("Subcategoría no encontrada");
	            return; // Sale del método si no la encuentra
	        }

	        System.out.println("\n  ╔══════════════════════════════════════════════════════");
	        System.out.println("  ║           PRODUCTOS QUE COINCIDEN:");
	        for (Producto p : productos) {
	            if (p.getSubcategoriaId() == id) {
	                p.mostrarInfoDetallada(1); // Itera sobre los productos y solo muestra la información de los que coincidan
	            }
	        }
	        System.out.println("  ╚══════════════════════════════════════════════════════");

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// --------------------- Métodos para mostrar información ---------------------
	// Muestra la inforamción como el nombre y precio de los productos
	public void mostrarInfoProductos(ArrayList<Producto> productos) {
		System.out.println("\n  ╔═══════════════════════════════════╗");
		System.out.println("  ║           INSTRUMENTOS            ║");
		System.out.println("  ║           Y ACCESORIOS            ║");

		for (Producto p : productos) {
			p.mostrarInfo(1);
		}
		System.out.println("  ╚═══════════════════════════════════╝");
	}

	// Muestra la información de los productos pero más detallada, como la descripción
	public void mostrarInfoDetalladaProductos(ArrayList<Producto> productos) {
		System.out.println("  ╔═════════════════════════════════════════════");
		System.out.println("  ║           INFORMACIÓN DE LOS PRODUCTOS");
		for (Producto p : productos) {
			p.mostrarInfoDetallada(1);
		}
		System.out.println("  ╚═════════════════════════════════════════════");
	}

	// Carga los productos de la tabla productos al arrayList productos
	public void obtenerProductos(ArrayList<Producto> productos) {
		productos.clear(); // Limpia la lista 'productos' para evitar que se dupliquen los productos al llamar el método
		
		try {
			String consulta = "SELECT * FROM productos";
			PreparedStatement ps = conexion.prepareStatement(consulta);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Producto p = new Producto(); // Crea un nuevo producto en cada iteración y se asigna los valores con los setters

				p.setId(rs.getInt("id"));
				p.setNombre(rs.getString("nombre"));
				p.setDescripcion(rs.getString("descripcion"));
				p.setCategoriaId(rs.getInt("categoria_id"));
				p.setSubcategoriaId(rs.getInt("subcategoria_id"));
				p.setPrecio(rs.getDouble("precio"));
				p.setCantidad(rs.getInt("cantidad"));

				productos.add(p); // Agrega el producto al arraylist
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
