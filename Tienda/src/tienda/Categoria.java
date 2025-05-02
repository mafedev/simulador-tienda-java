package tienda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Categoria {
	private int id;
	private String nombre;
	
	// Constructores
	public Categoria() {}
	
	public Categoria(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
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
	
	// Métodos
	public static void mostrarCategorias(Connection c) {
		try {
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM categorias");

			System.out.println("  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
			System.out.println("  │ CATEGORÍAS");
			while (rs.next()) {
				System.out.println("  │   ID: " + rs.getString("id"));
				System.out.println("  │     Nombre: " + rs.getString("nombre"));
				System.out.println("  │");
			}
			System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int encontrarIdMaximo(Connection c) {
		int id = 0;
		try {
			String obtenerId = "SELECT MAX(id) FROM categorias"; // Se obtiene el último id en la tabla categorias para luego insertarlo en la tabla productos
			PreparedStatement psId = c.prepareStatement(obtenerId);
			ResultSet rsUltimoId = psId.executeQuery();
			
			if (rsUltimoId.next()) {
				id = rsUltimoId.getInt(1); // Obtiene el id más alto
				System.out.println("La categoría se creó con éxito");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	
	// Mirar si se puede modificar el método para usarlo en tienda
	public void mostrarInfo() {
		
	}
	
	
}
