package tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Devolucion {

	public Devolucion() {
	}

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
	
	public static void mostrarDevoluciones(Connection c) {
		try {
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
