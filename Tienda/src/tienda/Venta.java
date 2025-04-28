package tienda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Venta {
	private int id;
	private int productoId;
	private int cantidad;
	private double total;
	private boolean devuelto;
	
	// Constructores
	public Venta() {}

	public Venta(int id, int productoId, int cantidad, double total, boolean devuelto) {
		this.id = id;
		this.productoId = productoId;
		this.cantidad = cantidad;
		this.total = total;
		this.devuelto = devuelto;
	}

	// Getters y Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductoId() {
		return productoId;
	}

	public void setProductoId(int productoId) {
		this.productoId = productoId;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public boolean isDevuelto() {
		return devuelto;
	}

	public void setDevuelto(boolean devuelto) {
		this.devuelto = devuelto;
	}
	
	// Métodos	
	public void mostrarVenta() {
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda", "root", "root");
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
}
