package tienda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

	static String url = "jdbc:mysql://localhost:3306/tienda";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char opc;
		System.out.println("\nBIENVENID@");
		try {
			Connection c = DriverManager.getConnection(url, "root", "root");
			Tienda t = new Tienda(c); // Se crea una tienda con una conexión para no abrir varias conexiones
			InfoTienda it = new InfoTienda(c);

			// Bucle principal
			do {
				opc = menuPrincipal(); 
				switch (opc) {
					case '1':
						t.venderProducto();
						break;
					case '2':
						logicaOpcion2(t, it);
						break;
					case '3':
						t.devolverProducto();
						break;
					case '4':
						t.eliminarProducto();
						break;
					case '5':
						logicaOpcion5(t, it);
						break;
					case '6':
						logicaOpcion6(c);
						break;
					case '7':
						System.out.println("Saliendo...");
						break;
					default:
						System.out.println("Ingrese una opción correcta");
				}
			} while (opc != '7');

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//--------------------- MENÚS ---------------------
	// Menú principal
	static char menuPrincipal() {
		Scanner sc = new Scanner(System.in);
		System.out.println("\n   ╭────────────────────────────────────────╮");
		System.out.println("      Ingrese una opción\n        1. Comprar producto\n        2. Actualizar inventario\n        3. Devolver producto\n        4. Eliminar Producto\n        5. Ver información de los productos\n        6. Informe de ventas\n        7. Salir");
		System.out.println("   ╰────────────────────────────────────────╯");
		System.out.print("⏵"); 
		return sc.nextLine().charAt(0);
	}

	// Menú secundario para la opción 2
	static char menuOpcion2() {
		Scanner sc = new Scanner(System.in);
		System.out.println("\n   ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈");
		System.out.println("      1. Agregar nuevo producto\n      2. Actualizar stock de producto un existente\n      3. Modificar precio de un producto\n      4. Volver ");
		System.out.println("   ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈");
		System.out.print("\n⏵");
		return sc.nextLine().charAt(0);
	}
	
	// Menú secundario para ver la información en la opción 4
	static char menuOpcion5() {
		Scanner sc = new Scanner(System.in);
		System.out.println("\n   ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈");
		System.out.println("      Seleccione que información quiere ver\n       1. Ver información detallada de todos los productos\n       2. Buscar producto por nombre\n       3. Buscar por ID\n       4. Buscar por categoria\n       5. Busca por subcategoría\n       6. Volver");
		System.out.println("   ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈");
		System.out.print("⏵");
		return sc.nextLine().charAt(0);
	}
	
	// Menú secundario para ver el informe de ventas (opción 6)
	static char menuOpcion6() {
		Scanner sc = new Scanner(System.in);
		System.out.println("\n   ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈");
		System.out.println("      Seleccione una opción\n       1. Ver ventas\n       2. Ver devoluciones\n       3. Total de ventas\n       4. Volver");
		System.out.println("   ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈");
		System.out.print("⏵");
		return sc.nextLine().charAt(0);
	}

	//--------------------- SWITCHES MENÚS SECUNDARIOS ---------------------
	// Maneja la lógica de la opción 2 del menú
	static void logicaOpcion2(Tienda t, InfoTienda it) {
		char opc;
		do {
			opc = menuOpcion2();
			switch(opc) {
				case '1':
					t.agregarProducto();
					break;
				case '2':
					t.cargarProductos(); // Carga los productos
					it.mostrarInfoProductos(t.getProductos()); // Muestra los productos disponibles
					Producto.actualizarStock(t.getConexion()); // Llama al método
					break;
				case '3':
					t.actualizarPrecio();
					break;
				case '4':
					System.out.println("Volviendo...");
					break;
				default:
			}
		} while (opc != '4');
	}
	
	// Maneja la logica de la opción 5 del menú
	static void logicaOpcion5(Tienda t, InfoTienda it) {
		Scanner sc = new Scanner(System.in);
		char menu, opc;
		do {
			it.obtenerProductos(t.getProductos());
			menu = menuOpcion5();
			switch (menu) {
				case '1':
					it.mostrarInfoDetalladaProductos(t.getProductos());
					break;
				case '2':
					it.buscarProductoPorNombre(t.getProductos());
					break;
				case '3':
					it.buscarProductoPorId(t.getProductos());
					break;
				case '4':
					it.buscarProductoCategoria(t.getProductos());
					break;
				case '5':
					it.buscarProductoSubcategoria(t.getProductos());
					break;
				case '6':
					System.out.println("Volviendo...");
					break;
				default:
					System.out.println("Ingrese una opción válida");
			}
		} while (menu != '6');
	}
	
	// Maneja la lógica de la opción 6 del menú
	static void logicaOpcion6(Connection c) {
		char opc;
		do {
			opc = menuOpcion6();
			switch(opc) {
				case '1':
					Venta.mostrarVentas(c); // Llama al método correspondiente
					break;
				case '2':
					Devolucion.mostrarDevoluciones(c); // Llama al método correspondiente
					break;
				case '3':
					try {
						// Suma el total de la tabla ventas solo si no tiene devoluciones registradas, es decir, si devuelto es false
						String sumaVentas = "SELECT SUM(total) AS totalVentas FROM ventas WHERE devuelto = false";
						Statement suma = c.createStatement();
						ResultSet rs = suma.executeQuery(sumaVentas);
						
						if (rs.next()) {
							double totalVentas = rs.getDouble("totalVentas");
							System.out.println("El total acumulado en caja es: " + totalVentas + " €");
						} else {
							System.out.println("No hay ventas registradas");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					break;
				case '4':
					System.out.println("Volviendo...");
					break;
				default:
					System.out.println("Ingrese una opción válida");
			}
		}while(opc != '4');
			
	}
}