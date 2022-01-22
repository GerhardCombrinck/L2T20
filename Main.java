import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

public class Main {


	public static void main(String[] args) {

		// Initialize arrays using text files where past data is stored
		ArrayList<Customer> customers = Customer.LoadCustomers();
		ArrayList<Restaurant> restaurants = Restaurant.LoadRestaurants();
		ArrayList<Item> items = Item.LoadItems();
		ArrayList<Order> orders = Order.LoadOrders();
		ArrayList<OrderLine> orderLines = new ArrayList<>();
		
			
		// set initial values for user input and initialize scanner
		String input = "";
		Scanner userInput = new Scanner(System.in);

		// start the loop the will represent the main menu.
		while (!input.equals("x")) {

			prompt();
			input = userInput.nextLine();

			// if the user sects option 1 create a new customer
			if (input.equals("1")) {
				System.out.println("Input Customer Name:");
				String customerName = userInput.nextLine();
				System.out.println("Input customer Contact No:");
				String customerContactNo = userInput.nextLine();
				System.out.println("Input customer City:");
				String customerCity = userInput.nextLine();
				System.out.println("Input customer Address:");
				String customerAddress = userInput.nextLine();
				System.out.println("Input customer Email Address:");
				String customerEmailAddress = userInput.nextLine();
				Customer newCustomer = new Customer(customerName, customerContactNo, customerCity, customerAddress, customerEmailAddress);
				customers.add(newCustomer);
				// save the changes to database
				try {	
					//add the row to the database
					Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost; database=QuickFoodMS; user=gerhard; password=piesang");
					Statement statement = connection.createStatement();
					int results = statement.executeUpdate("INSERT INTO customers values('" + customerName + "', '" + customerContactNo + "', '" + customerCity + "', '" + customerAddress + "', '" + customerEmailAddress + "')");
					if(results<1) {
						System.out.println("Error");
					}
				}
				catch (SQLException e) {
					// Catch a SQLException
					e.printStackTrace();
				}
				System.out.println("Customer Added: ");
				prompt();
				input = userInput.nextLine();
			}

			// if the user sects option 2 create a new restaurant
			else if (input.equals("2")) {

				System.out.println("Input Restaurant Name:");
				String restaurantName = userInput.nextLine();
				System.out.println("Input restaurant City:");
				String restaurantCity = userInput.nextLine();
				System.out.println("Input restaurant Contact No:");
				String restaurantContactNo = userInput.nextLine();
				Restaurant newRestaurant = new Restaurant(restaurantName, restaurantCity, restaurantContactNo);
				restaurants.add(newRestaurant);
				// save the changes to database
				try {	
					//add the row to the database
					Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost; database=QuickFoodMS; user=gerhard; password=piesang");
					Statement statement = connection.createStatement();
					int results = statement.executeUpdate("INSERT INTO restaurants values('" + restaurantName + "', '" + restaurantCity + "', '" + restaurantContactNo + "')");
					if(results<1) {
						System.out.println("Error");
					}
				}
				catch (SQLException e) {
					// Catch a SQLException
					e.printStackTrace();
				}
				System.out.println("Restaurant Added: ");
				prompt();
				input = userInput.nextLine();
			}

			// if the user sects option 3 create a new item, but there must be at least 1 restaurant to assign the item to
			else if (input.equals("3")) {
				int numberOfRestaurants = restaurants.size();
				if (numberOfRestaurants < 1) {
					System.out.println("Sorry, you need to add at least 1 restaurant be fore crating an item.");
				} else {
					System.out.println("Input Item Name:");
					String ItemName = userInput.nextLine();
					System.out.println("Input item Price:");
					String priceInput = userInput.nextLine();
					priceInput = priceInput.replace(',', '.');
					System.out.println(priceInput);
					double itemPrice = Double.valueOf(priceInput);

					System.out.println(
							"Choose restaurant that supplies this item (type the number of the resuaurant chosen:");
					for (int i = 0; i < restaurants.size(); i++) {
						System.out.println((i + 1) + ". " + restaurants.get(i).toString());
					}
					int userChoice = userInput.nextInt() - 1;
					Restaurant restaurantName = restaurants.get(userChoice);
					Item newItem = new Item(ItemName, itemPrice, restaurantName.getName());
					items.add(newItem);
					// save the changes to data\items.txt
					try {	
						//add the row to the database
						Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost; database=QuickFoodMS; user=gerhard; password=piesang");
						Statement statement = connection.createStatement();
						int results = statement.executeUpdate("INSERT INTO items values('" + ItemName + "', '" + itemPrice + "', '" + restaurantName.getName() + "')");
						if(results<1) {
							System.out.println("Error");
						}
					}
					catch (SQLException e) {
						// Catch a SQLException
						e.printStackTrace();
					}
					System.out.println("Item Added: ");
					System.out.println("");
				}
				prompt();
				input = userInput.nextLine();
			}

			// if the user sects option 4 create a new order and then add line items to the orders
			else if (input.equals("4")) {

				// Get the customer whom the order is for:
				System.out.println("Choose a customer: (type the number of the customer chosen:");
				for (int i = 0; i < customers.size(); i++) {
					System.out.println((i + 1) + ". " + customers.get(i).toString());
				}
				int userChoice = userInput.nextInt() - 1;
				Customer customerName = customers.get(userChoice);
				System.out.println(customerName.customerName + " selected, this customer is located in "
						+ customerName.customerCity);

				// Check and find a driver with the fewest loads in the area
				ArrayList<Driver> drivers = Driver.LoadDrivers();
				drivers.sort((o1, o2) -> o1.getLoads().compareTo(o2.getLoads()));
				Driver assignedDriver = null;
				int j = 0;
				while (assignedDriver == null & j < drivers.size()) {

					if (customerName.getCity().equals(drivers.get(j).getCity())) {
						assignedDriver = drivers.get(j);
						System.out.println("Driver " + assignedDriver.getName()
								+ "has been assigend to this order, she is located in " + drivers.get(j).getCity()
								+ " and only has " + drivers.get(j).getLoads() + " loads.");
					}
					j = j + 1;
				}
				// If there are no drivers take the user back to the menu
				if (assignedDriver == null) {
					System.out.println(
							"Sorry! Our drivers are too far away from you to be able to deliver to your location, you will now be redirected back to the main menu.");
					input = "";
					input = userInput.nextLine();
				}

				// If there are drivers the user must choose a restaurant in the city where the customer lives
				else {
					//
					System.out.println("Choose a restaurant: (type the number of the resturant chosen)");
					for (int i = 0; i < restaurants.size(); i++) {
						if (restaurants.get(i).getCity().equals(customerName.getCity())) {
							System.out.println((i + 1) + ". " + restaurants.get(i).toString());
						}
					}
					userChoice = userInput.nextInt() - 1;
					Restaurant restaurantName = restaurants.get(userChoice);

					// Create a new order with the above collected info
					int orderIndex = orders.size();
					Order newOrder = new Order(orderIndex, customerName.getName(), assignedDriver.getName(), "",restaurantName.getName());
					orders.add(newOrder);
					
					try {	
						//declare the database connection string
						Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost; database=QuickFoodMS; user=gerhard; password=piesang");
						//connection statement
						int results = connection.createStatement().executeUpdate("INSERT INTO orders VALUES (" + orderIndex  + ",'" +  customerName.getName()+ "','" + assignedDriver.getName()+ "','','" + restaurantName.getName() + "')");
						if(results<1) {
							System.out.println("Error");
						}
					}
					catch (SQLException e) {
						// Catch a SQLException
						e.printStackTrace();
					}

					// Add line items to the order, user must choose a item
					while (userChoice != -1) {
						System.out.println("");
						System.out.println("Please choose items to add to the order (type the number of the item chosen) or type '0' to complete and create the invoice for this order:");

						for (int i = 0; i < items.size(); i++) {
							if (items.get(i).restaurantName.equals(restaurantName.restaurantName)) {
								System.out.println((i + 1) + ". " + items.get(i).toString());
							}
						}
						userChoice = userInput.nextInt() - 1;
						if (userChoice != -1) {
							Item itemName = items.get(userChoice);

							// User must input a quantity for the above chosen item
							System.out.println("Enter the quantity for this item:");
							int userQty = userInput.nextInt();
							OrderLine newOrderLine = new OrderLine(newOrder.getOrderID(), itemName, userQty);
							orderLines.add(newOrderLine);
							try {	
								//declare the database connection string
								Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost; database=QuickFoodMS; user=gerhard; password=piesang");
								//connection statement
								int results = connection.createStatement().executeUpdate("INSERT INTO orderLines VALUES (" + newOrder.getOrderID()  + ",'" +  itemName.ItemName + "'," + userQty +")");
								if(results<1) {
									System.out.println("Error");
								}
							}
							catch (SQLException e) {
								// Catch a SQLException
								e.printStackTrace();
							}
							
						}
					}

					// If user types 0 then close off the order and generate the invoice
					if (userChoice == -1) {
						System.out.println("Please enter any special instructions now:");
						String special = userInput.nextLine();
						special = userInput.nextLine();
						orders.get((int) orderIndex).setSpecial(special);
						Order thisOrder = orders.get((int) orderIndex);
						double orderTotal = 0;

						// build the string this will be written to the invoice as per the task instructions				
						String invoiceText = "Order number " + thisOrder.getOrderID() + "\n" + "Customer:"
								+ thisOrder.GetCustomer().getName() + "\n" + "Email: "
								+ thisOrder.GetCustomer().getEmail() + "\n" + "Phone number: "
								+ thisOrder.GetCustomer().getContactNo() + "\n" + "Location: "
								+ thisOrder.GetCustomer().getCity() + "\n" + "\n"
								+ "You have ordered the following from " + thisOrder.getRestaurant().getName() + " in "
								+ thisOrder.getRestaurant().getCity() + ":\n" + "\n";
						for (OrderLine orderLine : orderLines) {
							if (orderLine.getOrderID() == thisOrder.getOrderID()) {
								invoiceText = invoiceText + orderLine.orderQty + " x "
										+ orderLine.getItem().ItemName + " (R"
										+ orderLine.getItem().itemPrice + ")\n";
								orderTotal = orderTotal
										+ (orderLine.orderQty * orderLine.getItem().itemPrice);
							}
						}
						invoiceText = invoiceText + "\n" + "\n" + "Special instructions: " + thisOrder.specialInstructions
								+ "\n" + "\n" + "Total: R" + round(orderTotal, 2) + "\n" + "\n"
								+ thisOrder.getDriver().getName()
								+ " is nearest to the restaurant and so he will be delivering your order to you at: "
								+ "\n" + "\n" + thisOrder.GetCustomer().customerAddress + "\n"
								+ thisOrder.GetCustomer().customerCity + "\n" + "\n"
								+ "If you need to contact the restaurant, their number is "
								+ thisOrder.getRestaurant().restaurantContactNo + "\n";

						// Write the invoice file
						try {
							Formatter f = new Formatter("Order#_" + thisOrder.getOrderID() + ".txt");
							f.format(invoiceText);
							f.close();
						} catch (Exception e) {
							System.out.println("An error occurred at .txt output.");
						}

						// update the drivers load
						thisOrder.getDriver().setLoads(thisOrder.getDriver().getLoads() + 1);

						// Notify user that the order was successfully created
						System.out.println("Order number " + thisOrder.getOrderID() + " created.");
					}
				}
			}
		}
		userInput.close();
		System.out.println("App closed... Have a nice day =)");
	}

	// Repeat the main menu prompt when required
	public static void prompt() {
		System.out.println("");
		System.out.println("Choose an option (type '1', '2', '3', '4' or 'x' :\n" + "1. New Customer\n"
				+ "2. New Restaurant\n" + "3. New Item\n" + "4. New Order\n" + "x. Exit app");
	}

	// round double to 2 decimal placed (for output of total cost)
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
