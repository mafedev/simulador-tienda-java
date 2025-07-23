# Simulación de Tienda en Java con MySQL

Este proyecto es una simulación de una tienda desarrollada en Java, que utiliza una base de datos MySQL para almacenar y gestionar la información de productos, categorías, subcategorías, ventas y devoluciones. La interacción se realiza a través de la terminal (consola) mediante un menú de opciones.

## Descripción

El programa permite realizar operaciones básicas de una tienda como:

- Listar productos disponibles
- Buscar productos por nombre, ID, categoría o subcategoría
- Agregar nuevos productos
- Actualizar stock y precios
- Eliminar productos
- Registrar ventas y devoluciones
- Consultar historial de ventas y devoluciones
- Calcular el total acumulado en caja

Todos los datos se gestionan en tiempo real desde una base de datos MySQL.

## Tecnologías utilizadas

- Java (JDK 8 o superior)
- MySQL
- JDBC (Java Database Connectivity)
- [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)

## Requisitos

Antes de ejecutar el programa, asegúrate de tener instalado:

- Java JDK
- MySQL Server
- MySQL Connector/J (incluido en el classpath, ver [`Tienda/.classpath`](Tienda/.classpath))

## Estructura de la base de datos

La base de datos contiene las siguientes tablas (ver [`tienda.sql`](tienda.sql)):

- **categorias**
- **subcategorias**
- **productos**
- **ventas**
- **devoluciones**

Puedes adaptar o extender las tablas según las necesidades del proyecto.

## Ejecución del programa

1. Clona o descarga el repositorio.
2. Configura los parámetros de conexión en [`Main.java`](Tienda/src/tienda/Main.java):

   ````java
   // ...existing code...
   Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda", "root", "root");
   // ...existing code...
   ````
   
3. Compila y ejecuta el programa:

Usa el menú en la terminal para interactuar con la tienda.

## Funcionalidades principales
- Menú interactivo por consola
- Gestión de stock y precios
- Registro de compras y devoluciones
- Conexión persistente a base de datos
- Búsqueda avanzada de productos
