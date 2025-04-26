package tienda;

public class Subcategoria {
	private int id;
	private String nombre;
	private Categoria categoria;
		
	public Subcategoria() {}

	public Subcategoria(int id, String nombre, Categoria categoria) {
		this.id = id;
		this.nombre = nombre;
		this.categoria = categoria;
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

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	public void mostrarInfo() {
		
	}
	
}
