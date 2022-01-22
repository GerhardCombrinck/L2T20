import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Customer{

	// Attributes of a customer object
	public String customerName;
	public String customerContactNo;
	public String customerCity;
	public String customerAddress;
	public String customerEmailAddress;

	// Constructor to make a customer object
	public Customer(String customerName, String customerContactNo, String customerCity, String customerAddress,
			String customerEmailAddress) {
		this.customerName = customerName;
		this.customerContactNo = customerContactNo;
		this.customerCity = customerCity;
		this.customerAddress = customerAddress;
		this.customerEmailAddress = customerEmailAddress;
	}

	@Override
	public String toString() {
		String output = customerName + ", " + customerContactNo + ", " + customerCity + ", " + customerAddress + ", " + customerEmailAddress;
		return output;
	}

	// Getters and setters
	public String getEmail() {
		return customerEmailAddress;
	}

	public String getContactNo() {
		return customerContactNo;
	}

	public String getName() {
		return customerName;
	}

	public String getCity() {
		return customerCity;
	}

	// Method to read table where past data is stored
	public static ArrayList<Customer> LoadCustomers() {
		ArrayList<Customer> lst = new ArrayList<>();
		try {	
			//declare the database connection string
			Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost; database=QuickFoodMS; user=gerhard; password=piesang");
				
			//connection statement
			Statement statement = connection.createStatement();
			ResultSet results = null;
			results = statement.executeQuery("SELECT * FROM customers");
			while (results.next()) {
				Customer newCustomer = new Customer(results.getString("customerName"), results.getString("customerContactNo"), results.getString("customerCity"), results.getString("customerAddress"), results.getString("customerEmailAddress"));
				lst.add(newCustomer);
			}	
		}
		catch (SQLException e) {
			// Catch a SQLException
			e.printStackTrace();
		}
		return lst;
	}

}
