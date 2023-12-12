package Foods;

import java.io.Serializable;

public class FoodItem implements Comparable<FoodItem>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String item;
	protected int weight;
	
	public FoodItem(String item, int weight) {
		super();
		this.item = item;
		this.weight = weight;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getWeight() {
		return weight;
	}
 
	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public int compareTo(FoodItem o) {
		return this.item.compareTo(o.item);
		
	}

}
