package tienda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Venta {
	private int id;
	private int productoId;
	private int cantidad;
	private double total;
	private boolean devuelto;
	
	// --------------------- Constructores ---------------------
	public Venta() {
	}

	public Venta(int id, int productoId, int cantidad, double total, boolean devuelto) {
		this.id = id;
		this.productoId = productoId;
		this.cantidad = cantidad;
		this.total = total;
		this.devuelto = devuelto;
	}

	// --------------------- Getters y Setters ---------------------
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

	public boolean getDevuelto() {
		return devuelto;
	}

	public void setDevuelto(boolean devuelto) {
		this.devuelto = devuelto;
	}
	
	// --------------------- Métodos ---------------------	
	// Método que se encarga de registrar la venta en la tabla ventas, y recibe como parámetros los valores a insertar
	public void registrarVenta(Connection c, int id, int cantidad, double total) {
		try {
			String venta = "INSERT INTO ventas (producto_id, cantidad, total) VALUES (?,?,?)";
			PreparedStatement psVenta = c.prepareStatement(venta);
			psVenta.setInt(1, id);
			psVenta.setInt(2, cantidad);
			psVenta.setDouble(3, total);

			int filasAfectadas = psVenta.executeUpdate();
			if (filasAfectadas > 0) { // Verifica si la venta se insertó correctamente en la tabla
				System.out.println("Compra registrada correctamente");
			} else {
				System.out.println("Error al registrar la compra");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Descuenta el dinero en la tabla de ventas en caso de que haya una devolución
	public void descontarDevolucion(Connection c, int ventaId) {
		try {
			String descontarDinero = "UPDATE ventas SET total = 0, devuelto = TRUE WHERE id = ?"; // Pone el dinero en 0
			PreparedStatement psDescontar = c.prepareStatement(descontarDinero);
			psDescontar.setInt(1, ventaId);
			psDescontar.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Este método muestra todas las ventas registradas
	public static void mostrarVentas(Connection c) {
		try {
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM ventas");

			while (rs.next()) {
				System.out.println("ID: " + rs.getInt("id"));
				System.out.println("Nombre: " + rs.getInt("producto_id"));
				System.out.println("Categoria: " + rs.getInt("cantidad"));
				System.out.println("Total: " + rs.getDouble("total"));

				boolean devuelto = rs.getBoolean("devuelto");
				if (devuelto) {
					System.out.println("Ha sido devuelto"); // Si se ha hecho una devolución, muestra el mensaje
				}

				System.out.println();
			}
			System.out.println("____________________\n");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void mostrarTicket(Connection c, Producto p, double total, int cantidad) {
		int id = 0;
		try {
			String obtenerId = "SELECT MAX(id) FROM ventas";
			PreparedStatement ps = c.prepareStatement(obtenerId);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				id = rs.getInt(1); // Obtiene el id de la última venta para luego mostrarlo en el ticket
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		// Se muestra la descripción de la compra
		System.out.println("\n"
				+ "      /\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\ \r\n"
				+ "     │                               Id compra: " + id + "       |\r\n"
				+ "     │                                                  |\r\n"
				+ "     │       @                                          |\r\n"
				+ "     │     @@@@@      Producto: " + p.getNombre() +"\r\n"
				+ "     │    @  @        Precio c/u: " + p.getPrecio()+ " €\r\n"
				+ "     │     @@@@       Cantidad: " + cantidad + "\r\n"
				+ "     │       @ @@                                       |\r\n"
				+ "     │       @  @                                       |\r\n"
				+ "     │    @@@@@@      Total: " + total + " €\r\n"
				+ "     │       @                                          |\r\n"
				+ "     │                                                  |\r\n"
				+ "      \\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/ \r\n");
		
	}
	
}
