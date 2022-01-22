import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Order {

	// Attributes of a Order object
	public int orderID;
	public String customerName;
	public String driverName;
	public String specialInstructions;
	public String orderRestaurant;

	// Constructor to make a Order object
	public Order(int orderID, String customerName, String driverName,String specialInstructions, String orderRestaurant) {
		this.orderID = orderID;
		this.customerName = customerName;
		this.driverName = driverName;
		this.specialInstructions = specialInstructions;
		this.orderRestaurant = orderRestaurant;
	}

	public String toString() {
		String output = "Order#: " + orderID + " || Customer: " + customerName + " || Driver: " + driverName;
		return output;
	}
	
	public Customer GetCustomer() {
		ArrayList<Customer> customers = Customer.LoadCustomers();
		Customer selectedC = null;
		for (int i = 0; i < customers.size(); i++) {
			if(customers.get(i).customerName.equals(this.customerName)) {
				selectedC = customers.get(i);
				break;
			}
		}
		return selectedC;		
	}
	
	public Restaurant getRestaurant() {
		ArrayList<Restaurant> restaurants = Restaurant.LoadRestaurants();
		Restaurant selectedR = null;
		for (int i = 0; i < restaurants.size(); i++) {
			if(restaurants.get(i).getName().equals(this.orderRestaurant)) {
				selectedR = restaurants.get(i);
				break;
			}
		}
		return selectedR;		
	}
	
	
	public Driver getDriver() {
		ArrayList<Driver> drivers = Driver.LoadDrivers();
		Driver selectedD = null;
		for (int i = 0; i < drivers.size(); i++) {
			if(drivers.get(i).getName().equals(this.driverName)) {
				selectedD = drivers.get(i);
				break;
			}
		}
		return selectedD;		
	}

	// Getters and setters
	public Long getOrderID() {
		return (long) orderID;
	}

	public void setSpecial(String specialInstructions) {
		this.specialInstructions = specialInstructions;
		try {	
			//declare the database connection string
			Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost; database=QuickFoodMS; user=gerhard; password=piesang");
			//connection statement
			int results = connection.createStatement().executeUpdate("UPDATE orders SET specialInstructions = '" + specialInstructions + "' WHERE orderID = " + this.orderID);	
			if(results<1) {
				System.out.println("Error");
			}
		}
		catch (SQLException e) {
			// Catch a SQLException
			e.printStackTrace();
		}
	}

	// Method to read file where past data is stored
	public static ArrayList<Order> LoadOrders() {
		ArrayList<Order> lst = new ArrayList<>();
		try {	
			//declare the database connection string
			Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost; database=QuickFoodMS; user=gerhard; password=piesang");
				
			//connection statement
			Statement statement = connection.createStatement();
			ResultSet results = null;
			results = statement.executeQuery("SELECT * FROM orders");
			while (results.next()) {
				Order newOrder = new Order(results.getInt("orderID"), results.getString("customerName"), results.getString("driverName"), results.getString("specialInstructions"), results.getString("orderRestaurant"));
				lst.add(newOrder);
			}	
		}
		catch (SQLException e) {
			// Catch a SQLException
			e.printStackTrace();
		}
		return lst;
	}

}
