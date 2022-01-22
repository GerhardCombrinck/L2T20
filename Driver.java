import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Driver {

	// Attributes of a Driver object
	public String driverName;
	public String driverCity;
	public Integer driverLoad;

	// Constructor to make a Driver object
	public Driver(String driverName, String driverCity, int driverLoad) {
		this.driverName = driverName;
		this.driverCity = driverCity;
		this.driverLoad = driverLoad;
	}

	@Override
	public String toString() {
		String output = driverName + ", " + driverCity + ", " + driverLoad;
		return output;
	}

	// Getters and setters
	public String getCity() {
		return driverCity;
	}

	public String getName() {
		return driverName;
	}

	public Integer getLoads() {
		return driverLoad;
	}

	public void setLoads(int newLoad) {
		this.driverLoad = newLoad;
		try {	
			//declare the database connection string
			Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost; database=QuickFoodMS; user=gerhard; password=piesang");
			//connection statement
			int results = connection.createStatement().executeUpdate("UPDATE drivers SET driverLoad = " + newLoad + " WHERE driverName = '" + this.driverName + "'");
			if(results<1) {
				System.out.println("Error");
			}
		}
		catch (SQLException e) {
			// Catch a SQLException
			e.printStackTrace();
		}
	}

	// Method to read table where past data is stored
	public static ArrayList<Driver> LoadDrivers() {
		ArrayList<Driver> lst = new ArrayList<>();
		try {	
			//declare the database connection string
			Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost; database=QuickFoodMS; user=gerhard; password=piesang");
				
			//connection statement
			Statement statement = connection.createStatement();
			ResultSet results = null;
			results = statement.executeQuery("SELECT * FROM drivers");
			while (results.next()) {
				Driver newDriver = new Driver(results.getString("driverName"), results.getString("driverCity"), results.getInt("driverLoad"));
				lst.add(newDriver);
			}	
		}
		catch (SQLException e) {
			// Catch a SQLException
			e.printStackTrace();
		}
		return lst;
	}

	// Method to check if the value can be converted to an integer
	public static boolean isInt(String str) {
		try {
			@SuppressWarnings("unused")
			int x = Integer.parseInt(str);
			return true; // String is an Integer
		} catch (NumberFormatException e) {
			return false; // String is not an Integer
		}

	}

}
