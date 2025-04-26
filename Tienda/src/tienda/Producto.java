package tienda;

public class Producto {
	private int id;
	private String nombre;
	private String descripcion;
	private Categoria categoria;
	private Subcategoria subcategoria;
	private double precio;
	private int cantidad;
	
	public Producto() {}

	public Producto(int id, String nombre, String descripcion, Categoria categoria, Subcategoria subcategoria, double precio, int cantidad) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.subcategoria = subcategoria;
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

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Subcategoria getSubcategoria() {
		return subcategoria;
	}

	public void setSubcategoria(Subcategoria subcategoria) {
		this.subcategoria = subcategoria;
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
	
	
	public void venderProducto() {
		
	}
	
	public void mostrarInfo() {
        System.out.println("Producto: " + nombre);
        System.out.println("Precio: " + precio + " €");
        System.out.println("Cantidad en stock: " + cantidad);
        System.out.println("Categoría: " + categoria.getNombre());
        System.out.println("Subcategoría: " + subcategoria.getNombre());
	}
}
