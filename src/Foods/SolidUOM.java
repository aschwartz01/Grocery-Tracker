package Foods;

public class SolidUOM extends FoodItem{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String uom;
	public SolidUOM(String item, int weight) {
		super(item, weight);
		this.uom = "g";
	}
	
	public SolidUOM(SolidUOM other) {
		super(other.getItem(), other.getWeight());
		this.uom = "g";
	}
	
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	@Override
	public String toString() {
		float w = this.getWeight();
		String UoM = this.uom;
		if(w >= 1000) {
			w = w/1000;
			UoM = "kg";
			return String.format("%-20s", this.getItem()) + " | " + String.format("%-10s", w + " " + UoM);
		}
		else {
			return String.format("%-20s", this.getItem()) + " | " + String.format("%-10s", this.getWeight() + " " + this.getUom());
		}
	}
}