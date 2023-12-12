package Foods;

public class NoUOM extends FoodItem{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoUOM(String item, int weight) {
		super(item, weight);
		
	}
	
	@Override
	public String toString() {
		return String.format("%-20s", this.getItem()) + " | " + String.format("%-10s", "N/A");
	}
	
	public String toStringQuantity() {
		return String.format("%-20s", this.getItem()) + " | " + String.format("%-10s", this.getWeight());
	}
}