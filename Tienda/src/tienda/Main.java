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
			Tienda t = new Tienda(c); // Se crea una tienda con la conexión para no abrir varias conexiones

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
						// que se puedan devolver x cantidad de productos
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
						System.out.println("Saliendo...");
	
						System.out.println("El total en caja es de: " + t.getTotalVenta() + " €");
						break;
					default:
						System.out.println("Ingrese una opción correcta");
				}

			} while (opc != '8');

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	//--------------------- MENÚS ---------------------
	// Menú principal
	static char menuPrincipal() {
		Scanner sc = new Scanner(System.in);
		System.out.println("\nIngrese una opción\n 1. Comprar producto\n 2. Añadir producto\n 3. Devolver producto\n 4. Eliminar Producto\n 5. Ver información de los productos\n 6. Ver ventas\n 7. Ver devoluciones\n 8. Salir");
		return sc.nextLine().charAt(0);
	}

	// Menú secundario para ver la información en la opción 4
	static char menuSecundario() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Seleccione que información quiere ver\n 1. Ver todos los productos\n 2. Buscar producto por nombre o ID\n 3. Salir");
		return sc.nextLine().charAt(0);
	}

	// Switch que maneja la logica del segundo menú
	static void logicaMenuSecundario(Tienda t) {
		char menu;
		do {
			menu = menuSecundario();
			switch (menu) {
				case '1':
					t.mostrarProductos();
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