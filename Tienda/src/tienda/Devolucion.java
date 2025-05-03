package tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Devolucion {
	private int id;
	private int ventaId;
	private int productoId;
	private String motivo;
	private double dineroDevuelto;
	
	// --------------------- Constructores ---------------------
	public Devolucion() {
	}

	public Devolucion(int id, int ventaId, int productoId, String motivo, double dineroDevuelto) {
		this.id = id;
		this.ventaId = ventaId;
		this.productoId = productoId;
		this.motivo = motivo;
		this.dineroDevuelto = dineroDevuelto;
	}

	// --------------------- Getters y Setters ---------------------
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVentaId() {
		return ventaId;
	}

	public void setVentaId(int ventaId) {
		this.ventaId = ventaId;
	}

	public int getProductoId() {
		return productoId;
	}

	public void setProductoId(int productoId) {
		this.productoId = productoId;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public double getDineroDevuelto() {
		return dineroDevuelto;
	}

	public void setDineroDevuelto(double dineroDevuelto) {
		this.dineroDevuelto = dineroDevuelto;
	}
	
	// --------------------- Métodos ---------------------
	// Registra en la tabla devoluciones la devolución con los datos que recibe como parámetro
	public static void registrarDevolucion(Connection c, int ventaId, int productoId, String motivo, double dineroDevuelto) {
		try {
			String insertarDevolucion = "INSERT INTO devoluciones (venta_id, producto_id, motivo, dinero_devuelto) VALUES (?, ?, ?, ?)";
			PreparedStatement psDevolucion2 = c.prepareStatement(insertarDevolucion);
			psDevolucion2.setInt(1, ventaId);
			psDevolucion2.setInt(2, productoId);
			psDevolucion2.setString(3, motivo);
			psDevolucion2.setDouble(4, dineroDevuelto);
			psDevolucion2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Muestra todas las devoluciones registradas
	public static void mostrarDevoluciones(Connection c) {
		try {
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM devoluciones"); // Muestra todo lo de la tabla devoluciones
			
			System.out.println("  ┍━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┑");
			System.out.println("  │           DEVOLUCIONES");
			while(rs.next()) {
				System.out.println("  │   ID: " + rs.getInt("id"));
				System.out.println("  │     Id de la venta: " + rs.getInt("producto_id"));
				System.out.println("  │     Motivo: " + rs.getString("motivo"));
				System.out.println("  │     Dinero devuelto: " + rs.getDouble("dinero_devuelto") + " €");
				System.out.println("  │ ");
			}
			System.out.println("  ┕━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┙");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
