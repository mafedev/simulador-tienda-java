package tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Producto {
	private int id;
	private String nombre;
	private String descripcion;
	private int categoriaId;
	private int subcategoriaId;
	private double precio;
	private int cantidad;

	// --------------------- Constructores ---------------------
	public Producto() {
	}

	public Producto(int id, String nombre, String descripcion, int categoriaId, int subcategoriaId, double precio,
			int cantidad) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.categoriaId = categoriaId;
		this.subcategoriaId = subcategoriaId;
		this.precio = precio;
		this.cantidad = cantidad;
	}

	// --------------------- Getters y Setters ---------------------
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(int categoriaId) {
		this.categoriaId = categoriaId;
	}

	public int getSubcategoriaId() {
		return subcategoriaId;
	}

	public void setSubcategoriaId(int subcategoriaId) {
		this.subcategoriaId = subcategoriaId;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	// --------------------- Métodos ---------------------
	// Cuando hay una compra, actualiza el stock restando la cantidad de productos comprados
	public void actualizarStockCompra(Connection c, int cantidad, int id) {
		try {
			String descontarCantidad = "UPDATE productos SET cantidad = cantidad - ? WHERE id = ?"; // Resta la cantidad comprada a la cantidad de productos que hay
			PreparedStatement psCantidad = c.prepareStatement(descontarCantidad);
			psCantidad.setInt(1, cantidad);
			psCantidad.setInt(2, id);

			int actualizados = psCantidad.executeUpdate();
			if (actualizados > 0) {
				System.out.println("Stock actualizado correctamente");
			} else {
				System.out.println("Error al actualizar el stock");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void actualizarStock(Connection c) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Ingrese el ID del producto");
		int productoId = sc.nextInt();
		
		System.out.println("¿Cuántos va a agregar?");
		int cantidad = sc.nextInt();
		
		try {
			String actualizarStock = "UPDATE productos SET cantidad = cantidad + ? WHERE id = ?";
			PreparedStatement psActualizar = c.prepareStatement(actualizarStock);
			psActualizar.setInt(1, cantidad);
			psActualizar.setInt(2, productoId);
			psActualizar.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void actualizarPrecio(Connection c) {
        Scanner sc = new Scanner(System.in);
        System.out.println("El precio actual del producto es: " + this.precio);
        System.out.println("Ingrese el nuevo precio: ");
        double nuevoPrecio = sc.nextDouble();
        
        if (nuevoPrecio > 0) { // Verificar que el nuevo precio sea válido
        	try {
    			String actualizar = "UPDATE productos SET precio = ? WHERE id = ?";
    			PreparedStatement ps = c.prepareStatement(actualizar);
    			ps.setDouble(1, nuevoPrecio);
    			ps.setInt(2, id);
    			
    			 int confirmar = ps.executeUpdate();
    		        if (confirmar > 0) {
    		            System.out.println("Precio actualizado correctamente");
    		        } else {
    		            System.out.println("No se pudo actualizar el precio");
    		        }
    		} catch(SQLException e) {
    			e.printStackTrace();
    		}
        } else {
            System.out.println("Ingrese un precio válido");
        }
    }
	
	public void mostrarInfoDetallada(int opc) { 
		/*
		 * El parámetro opc es solo para mejorar como se ve por la terminal, si se pone
		 * un 1 significa que es para ver la información de todos los productos en una
		 * lista, entonces muestra un ║, de lo contrario es cuando se quiere ver la
		 * información de un producto en específico
		 */
		String simbolo = opc == 1 ? "║" : "";
		System.out.println("  " + simbolo + "    ID: " + this.id);
        System.out.println("  " + simbolo + "     Nombre: " + this.nombre);
        System.out.println("  " + simbolo + "     " + descripcion);
        System.out.println("  " + simbolo + "     Precio: " + this.precio + " €");
        System.out.println("  " + simbolo + "     En stock: " + this.cantidad);
        System.out.println("  " + simbolo + "");
	}
	
	public void mostrarInfo(int opc) {
		String simbolo = opc == 1 ? "║" : " "; // El parámetro opc es solo para mejorar como se ve por la terminal
		
		System.out.println("  " + simbolo + "   ID: " + this.id);
        System.out.println("  " + simbolo + "     Nombre: " + this.nombre);
	}
}
