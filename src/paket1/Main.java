package paket1;

import java.sql.*;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {

		loadJDBCDriver();

		// Connect to a database
		Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost/world", "scott", "tiger");
		System.out.println("************Database connected***************\n");

		// Create a statement
		Statement statement = connection.createStatement();

		// Create a resultSet
		ResultSet resultSet;

		Scanner input = new Scanner(System.in);

		printUserMenu();
		System.out.print("Unesite svoj izbor: ");

		switch (input.next()) {
		case "1":

			// korisnik trazi drzavu po imenu
			System.out.print("Unesite ime drzave(engleski naziv): ");
			resultSet = statement
					.executeQuery("select * from country where name = '"
							+ input.next() + "'");
			break;

		case "2":

			// korisnik trazi drzacu po jednim od gradova
			System.out.print("Unesi ime grada(engleski naziv): ");
			String cityName = input.next();

			resultSet = statement
					.executeQuery("select countrycode from city where name = '"
							+ cityName + "'");

			resultSet.next();
			String countryCodeString = resultSet.getString(1);

			resultSet = statement
					.executeQuery("select * from country where code = '"
							+ countryCodeString + "'");
			break;

		case "3":

			// korisnik trazi drzave po broju stanovnika
			System.out
					.print("Unesite gornju i donju granicu broja stanovnika drzava koje zelite printati: ");
			int lowerBorder = input.nextInt();
			int higherBorder = input.nextInt();

			resultSet = statement
					.executeQuery("select * from country where population between "
							+ lowerBorder
							+ " and "
							+ higherBorder
							+ " order by name");
			break;

		default:
			resultSet = null;
			System.out
					.println("Napravili ste gresku u unosu ili ste unljeli netacne podatke.");
			System.exit(1);
		}

		System.out.println();

		printResulHeader();
		// Iterate through the result and print
		while (resultSet.next()) {
			printTuple(resultSet);
		}

		connection.close();
		input.close();

	}

	/** Load the JDBD Driver */
	public static void loadJDBCDriver() throws ClassNotFoundException {
		// Load the JDBC Driver
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("**************Driver loaded******************\n");
	}

	/** Print User menu */
	public static void printUserMenu() {

		System.out.println("Ako zelite bazu pretrazivati po ");
		System.out.println(" - imenu drzave prisnite 1.");
		System.out.println(" - broju stanovnika drzave prisnite 2.");
		System.out
				.println(" - imenu jednog od gradova te drzave  prisnite 3.\n");
	}

	public static void printResulHeader() {
		System.out
				.println("| Code | Name\t\t\t\t | Continent\t\t\t | Region\t\t\t | Population\t |");
	}

	/** Print a defined version of the result tuple */
	public static void printTuple(ResultSet resultSet) throws SQLException {
		System.out.printf("  %-6s", resultSet.getString(1));
		System.out.printf(" %-33s",
				(resultSet.getString(2).length() > 30) ? resultSet.getString(2)
						.substring(0, 27) + "..." : resultSet.getString(2));
		System.out.printf(" %-31s", resultSet.getString(3));
		System.out.printf(" %-31s", resultSet.getString(4));
		System.out.printf(" %-33s\n", resultSet.getString(5));

	}
}