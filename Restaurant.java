import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Restaurant{

	// Attributes of a Restaurant object
	public String restaurantName;
	public String restaurantCity;
	public String restaurantContactNo;

	// Constructor to make a Restaurant object
	public Restaurant(String restaurantName, String restaurantCity, String restaurantContactNo) {
		this.restaurantName = restaurantName;
		this.restaurantCity = restaurantCity;
		this.restaurantContactNo = restaurantContactNo;
	}

	@Override
	public String toString() {
		String output = restaurantName + ", " + restaurantCity + ", " + restaurantContactNo;
		return output;
	}

	// Getters and setters
	public String getCity() {
		return restaurantCity;
	}

	public String getName() {
		return restaurantName;
	}

	// Method to read file where past data is stored
	public static ArrayList<Restaurant> LoadRestaurants() {
		ArrayList<Restaurant> lst = new ArrayList<>();
		try {	
			//declare the database connection string
			Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost; database=QuickFoodMS; user=gerhard; password=piesang");
				
			//connection statement
			Statement statement = connection.createStatement();
			ResultSet results = null;
			results = statement.executeQuery("SELECT * FROM restaurants");
			while (results.next()) {
				Restaurant newRestaurant = new Restaurant(results.getString("restaurantName"), results.getString("restaurantCity"), results.getString("restaurantContactNo"));
				lst.add(newRestaurant);
			}	
		}
		catch (SQLException e) {
			// Catch a SQLException
			e.printStackTrace();
		}
		return lst;
	}

}
