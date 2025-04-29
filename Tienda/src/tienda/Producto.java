package tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Producto {
	private int id;
	private String nombre;
	private String descripcion;
	private int categoriaId;
	private int subcategoriaId;
	private double precio;
	private int cantidad;

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

	// Getters y Setters	
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

	// Métodos
	public void actualizarStockCompra(Connection c, int cantidad, int id) {
		try {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void actualizarStockDevolucion(Connection c, int cantidad, int productoId) {
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
	
	public void mostrarInfoDetallada() {
		System.out.println("\n  │   ID: " + this.id);
        System.out.println("  │     Nombre: " + this.nombre);
        System.out.println("  |     " + descripcion);
        System.out.println("  │     Precio: " + this.precio + " €");
        System.out.println("  │     En stock: " + this.cantidad);
        System.out.println("  │\n");
	}
	
	public void mostrarInfo() {
		System.out.println("  │   ID: " + this.id);
        System.out.println("  │     Nombre: " + this.nombre);
	}
}
