public class OrderLine{

	// Attributes of a OrderLine object
	public long orderID;
	public Item itemName;
	public int orderQty;

	// Constructor to make a OrderLine object
	public OrderLine(long orderID, Item itemName, int orderQty) {
		this.orderID = orderID;
		this.itemName = itemName;
		this.orderQty = orderQty;
	}

	@Override
	public String toString() {
		String output = orderID + ", " + itemName + ", " + orderQty;
		return output;
	}

	// Getters and setters
	public long getOrderID() {
		return orderID;
	}

	public int getorderQty() {
		return orderQty;
	}

	public Item getItem() {
		return itemName;
	}

}
