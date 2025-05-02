package tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Subcategoria {
	private int id;
	private String nombre;
	private Categoria categoria;
		
	// --------------------- Constructores ---------------------
	public Subcategoria() {}

	public Subcategoria(int id, String nombre, Categoria categoria) {
		this.id = id;
		this.nombre = nombre;
		this.categoria = categoria;
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

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	// --------------------- Métodos ---------------------	
	public static int encontrarIdMaximo(Connection c) {
		int id = 0;
		try {
			String obtenerId = "SELECT MAX(id) FROM subcategorias"; // Obtiene el ùltimo id o el id mayor, de la tabla, para luego poder usarlo en Tienda en el método de agregarProducto
			PreparedStatement psId = c.prepareStatement(obtenerId);
			ResultSet rsUltimoId = psId.executeQuery();
			
			if (rsUltimoId.next()) {
				id = rsUltimoId.getInt(1); // Obtiene solo el mayor id
				System.out.println("La subcategoría se creó con éxito");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}

	public static void mostrarSubcategorias(Connection c, int categoriaId) { // Al igual que en Categoria, el método también es static para no tener que crear un objeto
		try {
			String consulta = "SELECT * FROM subcategorias WHERE categoria_id = ?"; // Busca las subcategorías asociadas a la categoria
	        PreparedStatement ps = c.prepareStatement(consulta);
	        ps.setInt(1, categoriaId);
	        ResultSet rs = ps.executeQuery();

			System.out.println("  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
			System.out.println("  │ SUBCATEGORÍAS");
			while (rs.next()) {
				System.out.println("  │   ID: " + rs.getString("id"));
				System.out.println("  │     Nombre: " + rs.getString("nombre"));
				System.out.println("  │     Categoria ID: " + rs.getInt("categoria_id"));
				System.out.println("  │");
			}
			System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
