package tienda;

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
	
	
	public void mostrarInfo() {
		
	}
}
