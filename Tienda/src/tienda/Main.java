package tienda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

			// Bucle principal
			do {
				opc = menuPrincipal(); 
				switch (opc) {
					case '1':
						t.venderProducto();
						break;
					case '2':
						t.agregarProducto();
						break;
					case '3':
						t.devolverProducto();
						break;
					case '4':
						t.eliminarProducto();
						break;
					case '5':
						logicaMenuSecundario(t);
						break;
					case '6':
						Venta.mostrarVentas(c);
						break;
					case '7':
						Devolucion.mostrarDevoluciones(c);
						break;
					case '8':
						try {
							String sumaVentas = "SELECT SUM(total) AS totalVentas FROM ventas";
							Statement suma = c.createStatement();
							ResultSet rs = suma.executeQuery(sumaVentas);
							
							if (rs.next()) {
								double totalVentas = rs.getDouble("totalVentas");
								System.out.println("El total acumulado en caja es: " + totalVentas + " €");
							} else {
								System.out.println("No hay ventas registradas.");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case '9':
						System.out.println("Saliendo...");
						break;
					default:
						System.out.println("Ingrese una opción correcta");
				}
			} while (opc != '9');

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//--------------------- MENÚS ---------------------
	// Menú principal
	static char menuPrincipal() {
		Scanner sc = new Scanner(System.in);
		System.out.println("\n Ingrese una opción\n  1. Comprar producto\n  2. Añadir producto\n  3. Devolver producto\n  4. Eliminar Producto\n  5. Ver información de los productos\n  6. Ver ventas\n  7. Ver devoluciones\n  8. Total de ventas\n  9. Salir");
		return sc.nextLine().charAt(0);
	}

	// Menú secundario para ver la información en la opción 4
	static char menuSecundario() {
		Scanner sc = new Scanner(System.in);
		System.out.println(" Seleccione que información quiere ver\n  1. Ver información detallada de todos los productos\n  2. Buscar producto por nombre o ID\n  3. Volver");
		return sc.nextLine().charAt(0);
	}

	// Switch que maneja la logica del segundo menú
	static void logicaMenuSecundario(Tienda t) {
		char menu;
		do {
			menu = menuSecundario();
			switch (menu) {
				case '1':
					// Hacer que muestre lo de informacion detallada
					t.mostrarInfoDetalladaProductos();
					break;
				case '2':
					t.buscarProducto();
					break;
				case '3':
					System.out.println("Volviendo...");
					break;
				default:
					System.out.println("Ingrese una opción válida");
			}
		} while (menu != '3');
	}
	
}