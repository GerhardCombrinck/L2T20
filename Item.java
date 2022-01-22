import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Item {

	// Attributes of a Item object
	public String ItemName;
	public double itemPrice;
	public String restaurantName;

	// Constructor to make a Item object
	public Item(String ItemName, double itemPrice, String restaurantName) {
		this.ItemName = ItemName;
		this.itemPrice = itemPrice;
		this.restaurantName = restaurantName;
	}

	@Override
	public String toString() {
		String output = ItemName + ", " + itemPrice + ", " + restaurantName;
		return output;
	}

	// Getters and setters
	public String getName() {
		return ItemName;
	}

	public double getPrice() {
		return itemPrice;
	}

	// Method to read table where past data is stored
	public static ArrayList<Item> LoadItems() {
		ArrayList<Item> lst = new ArrayList<>();
		try {	
			//declare the database connection string
			Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost; database=QuickFoodMS; user=gerhard; password=piesang");
				
			//connection statement
			Statement statement = connection.createStatement();
			ResultSet results = null;
			results = statement.executeQuery("SELECT * FROM items");
			while (results.next()) {
				Item newItem = new Item(results.getString("ItemName"), results.getDouble("itemPrice"), results.getString("restaurantName"));
				lst.add(newItem);
			}	
		}
		catch (SQLException e) {
			// Catch a SQLException
			e.printStackTrace();
		}
		return lst;
	}

}
