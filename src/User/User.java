package User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Foods.FluidUOM;
import Foods.FoodItem;
import Foods.NoUOM;
import Foods.SolidUOM;

public class User {
	
	private String name;
	public List<FoodItem> inventory;
	public TreeMap<FoodItem, Integer> shoppingList;
	public TreeMap<String, List<FoodItem>> recipeBook;
	
	public User(String name) {
		super();
		this.name = name;
		this.inventory = new ArrayList<>();;
		this.shoppingList = new TreeMap<>();;
		this.recipeBook = new TreeMap<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FoodItem> getInventory() {
		return inventory;
	}

	public void setInventory(List<FoodItem> inventory) {
		this.inventory = inventory;
	}

	public TreeMap<FoodItem, Integer> getShoppingList() {
		return shoppingList;
	}

	public void setShoppingList(TreeMap<FoodItem, Integer> shoppingList) {
		this.shoppingList = shoppingList;
	}
	
	public void showInventory() {
		if(this.inventory.size() <= 0) {
			System.out.println("Your inventory is currently empty\n");
		}
		else {
			StringBuilder sb = new StringBuilder("Viewing Your Inventory:\n" + String.format("%-20s", "Item") + " | " + "Quantity/Weight\n");
			for(int i = 0; i < this.inventory.size(); i++) {
				if(this.inventory.get(i).getClass().getSimpleName().equals("NoUOM")) {
					sb.append(((NoUOM)this.inventory.get(i)).toStringQuantity() + "\n");
				}
				else {
					sb.append(this.inventory.get(i).toString() + "\n");
				}
			}
			System.out.println(sb.toString());
		}
	}
	
	public void showShoppingList() {
		if(this.shoppingList.size() <= 0) {
			System.out.println("Your shopping list is currently empty\n");
		}
		else {
			StringBuilder sb = new StringBuilder("Viewing Your Shopping List:\n" + String.format("%-20s", "Item") + " | " + String.format("%-10s", "Weight") + " | " + "Quantity\n");
			for(Map.Entry<FoodItem, Integer> entry : this.shoppingList.entrySet()) {
				if(entry.getKey().getClass().getSimpleName().equals("NoUOM")) {
					sb.append(((NoUOM)entry.getKey()).toString() + " | " + entry.getValue() + "\n");
				}
				else {
					sb.append(entry.getKey().toString() + " | " + entry.getValue() + "\n");
				}
			}
			System.out.println(sb.deleteCharAt(sb.length()-1).toString() + "\n");
		}
	}
	
	public void showRecipeBook() {
		StringBuilder sb = new StringBuilder("Viewing Your Recipe Book:\n");
		for(Map.Entry<String, List<FoodItem>> entry : this.recipeBook.entrySet()) {
			sb.append(entry.getKey() + ":\n");
			sb.append(String.format("%-20s", "Item") + " | " + "Weight/Quantity\n");
			for(int i = 0; i < entry.getValue().size(); i++) {
				if(entry.getValue().get(i).getClass().getSimpleName().equals("NoUOM")) {
					sb.append(((NoUOM)entry.getValue().get(i)).toStringQuantity() + "\n");
				}
				else {
					sb.append(entry.getValue().get(i).toString() + "\n");
				}
			}
			sb.append("\n");
			
		}
		System.out.println(sb.deleteCharAt(sb.length()-1).toString());
	}
	
	public void addToShoppingList(String item, int weight, String uom, int quantity) {
		boolean found = false;
		for(Map.Entry<FoodItem, Integer> entry : this.shoppingList.entrySet()) {
			if(entry.getKey().getItem().equals(item) && entry.getKey().getWeight() == weight) {
				found = true;
				this.shoppingList.replace(entry.getKey(), entry.getValue() + quantity);
				break;
			}
		}
		if(!found) {
			boolean found2 = false;
			for(Map.Entry<FoodItem, Integer> entry : this.shoppingList.entrySet()) {
				if(entry.getKey().getItem().equals(item.toUpperCase())) {
					found2 = true;
					int newWeight = (entry.getKey().getWeight() * entry.getValue()) + (weight * quantity);
					entry.getKey().setWeight(newWeight);

					
					if(entry.getKey().getClass().getSimpleName().equals("NoUOM")) {
						this.shoppingList.replace(entry.getKey(), newWeight);
					}
					else {
						this.shoppingList.replace(entry.getKey(), 1);
					}
					
					break;
				}
			}
			if(!found2) {
				if(uom.equals("ml")) {
					this.shoppingList.put(new FluidUOM(item.toUpperCase(), weight), quantity);
				}
				else if(uom.equals("g")) {
					this.shoppingList.put(new SolidUOM(item.toUpperCase(), weight), quantity);
				}
				else {
					this.shoppingList.put(new NoUOM(item.toUpperCase(), weight), quantity);
				}
			}
			
		}
		System.out.println();
		
	}
	
	public void removeFromShoppingList(String item, int weight, int quantity) {
		boolean found = false;
		for(Map.Entry<FoodItem, Integer> entry : this.shoppingList.entrySet()) {
			if(entry.getKey().getItem().equals(item.toUpperCase()) && entry.getKey().getWeight() == weight) {
				found = true;

				if(entry.getValue() - quantity > 0) {
					this.shoppingList.replace(entry.getKey(), entry.getValue() - quantity);
				}
				else {
					this.shoppingList.remove(entry.getKey());
				}
				break;
			}
		}
		if(!found) {
			boolean found2 = false;
			for(Map.Entry<FoodItem, Integer> entry : this.shoppingList.entrySet()) {
				if(entry.getKey().getItem().equals(item.toUpperCase())) {
					found2 = true;
					int newWeight = entry.getKey().getWeight() * entry.getValue();
					entry.getKey().setWeight(newWeight);
					this.shoppingList.replace(entry.getKey(), 1);
					
					if(entry.getKey().getWeight() - (weight * quantity) > 0) {
						entry.getKey().setWeight(entry.getKey().getWeight() - (weight * quantity));
					}
					else {
						this.shoppingList.remove(entry.getKey());
					}
					
					this.shoppingList.replace(entry.getKey(), entry.getValue());
					break;
				}
			}
			if(!found2) {
				System.out.println("Item not found in the shopping list");
			}
		}
	}
	
	public void addItemToInventory(String item, int weight, String uom, int quantity) {
		boolean found = false;
		for(int i = 0; i < this.inventory.size(); i++) {
			if(this.inventory.get(i).getItem().equals(item.toUpperCase())) {
				this.inventory.get(i).setWeight(this.inventory.get(i).getWeight() + (weight * quantity));
				removeFromShoppingList(item.toUpperCase(), weight, quantity);
				found = true;
				break;
			}
		}
		if(!found) {
			if(uom.equals("ml")) {
				this.inventory.add(new FluidUOM(item.toUpperCase(), weight * quantity));
			}
			else if(uom.equals("g")) {
				this.inventory.add(new SolidUOM(item.toUpperCase(), weight * quantity));
			}
			else {
				this.inventory.add(new NoUOM(item.toUpperCase(), weight * quantity));
			}

			removeFromShoppingList(item, weight, quantity);
		}
	}
	
	public void addItemsToInventory(TreeMap<FoodItem, Integer> itemsToAdd) {
		for(Map.Entry<FoodItem, Integer> entry : itemsToAdd.entrySet()) {
			FoodItem fi = entry.getKey();
			String item = fi.getItem();
			int weight = fi.getWeight();
			String uom = "";
			if(fi.getClass().getSimpleName().equals("FluidUOM")) {
				uom = "ml";
			}
			else if(fi.getClass().getSimpleName().equals("SolidUOM")) {
				uom = "g";
			}
			
			int quantity = entry.getValue();
			addItemToInventory(item, weight, uom, quantity);
		}
	}
	
	public void removeItemFromInventory(String item, int weight) {
		boolean found = false;
		for(int i = 0; i < this.inventory.size(); i++) {
			FoodItem fi = this.inventory.get(i);
			if(fi.getItem().equals(item.toUpperCase()) && fi.getWeight() == weight) {
				found = true;
				this.inventory.remove(i);
				break;
			}
		}
		if(!found) {
			boolean found2 = false;
			for(int i = 0; i < this.inventory.size(); i++) {
				FoodItem fi = this.inventory.get(i);
				if(fi.getItem().equals(item.toUpperCase())) {
					found2 = true;
					
					
					if(fi.getWeight() - weight > 0) {
						fi.setWeight(fi.getWeight() - weight);
					}
					else {
						this.inventory.remove(i);
					}
					
					break;
				}
			}
			if(!found2) {
				System.out.println("Item not found in the inventory");
			}
		}
	}
	
	public void removeItemsFromInventory(List<FoodItem> fiList) {
		for(int i = 0; i < fiList.size(); i++) {
			String item = fiList.get(i).getItem();
			int weight = fiList.get(i).getWeight();
			removeItemFromInventory(item, weight);
		}
	}
	
	public void addRecipe(String name, List<FoodItem> recipe) {
		this.recipeBook.put(name, recipe);
	}
	
	public boolean checkRecipe(List<FoodItem> recipe) {
		boolean foundAll = true;
		for(int i = 0; i < recipe.size(); i++) {
			FoodItem currFood = recipe.get(i);
			String currItem = currFood.getItem();
			boolean found = false;
			for(int j = 0; j < this.inventory.size(); j++) {
				if(this.inventory.get(j).getItem().equals(currItem)) {
					if(currFood.getWeight() > this.inventory.get(j).getWeight()) {
						if(recipe.get(i).getClass().getSimpleName().equals("FluidUOM")) {
							this.shoppingList.put(new FluidUOM((FluidUOM)recipe.get(i)), 1);
						}
						else if(recipe.get(i).getClass().getSimpleName().equals("SolidUOM")) {
							this.shoppingList.put(new SolidUOM((SolidUOM)recipe.get(i)), 1);
						}
						else {
							this.shoppingList.put(new NoUOM(currFood.getItem(), 1), currFood.getWeight());
						}
						foundAll = false;
						System.out.println("You do not have enough " + currItem + " for this recipe. " + currItem + " is now in your shopping list.");
					}
					else {
						System.out.println("You have enough " + currItem + " for this recipe!");
					}
					found = true;
				}
			}
			if(!found) {
				if(recipe.get(i).getClass().getSimpleName().equals("FluidUOM")) {
					this.shoppingList.put(new FluidUOM((FluidUOM)recipe.get(i)), 1);
				}
				else if(recipe.get(i).getClass().getSimpleName().equals("SolidUOM")) {
					this.shoppingList.put(new SolidUOM((SolidUOM)recipe.get(i)), 1);
				}
				else {
					this.shoppingList.put(new NoUOM(currFood.getItem(), 1), currFood.getWeight());
				}
				foundAll = false;
				System.out.println("You do not have any " + currItem + ". " + currItem + " is now in your shopping list.");
			}
		}
		System.out.println();
		if(foundAll) {
			return true;
			
		}
		else {
			return false;
		}
	}
	
	public void makeRecipe(List<FoodItem> ingredients) {
		removeItemsFromInventory(ingredients);
	}
	
	public void searchInventory(String item) {
		boolean found = false;
		for(int i = 0; i < this.inventory.size(); i++) {
			if(this.inventory.get(i).getItem().equals(item)) {
				if(this.inventory.get(i).getClass().getSimpleName().equals("NoUOM")) {
					System.out.println("You have " + this.inventory.get(i).getWeight() + " " + this.inventory.get(i).getItem() +".\n");
				}
				else if(this.inventory.get(i).getClass().getSimpleName().equals("FluidUOM")) {
					FluidUOM fluid = (FluidUOM)this.inventory.get(i);
					float weight = fluid.getWeight();
					if(weight >= 1000) {
						weight = weight/1000;
						System.out.println("You have " + weight + " liters of " + fluid.getItem() +".\n");
					}
					else {
						System.out.println("You have " + fluid.getWeight() + " milliliters of " + fluid.getItem() +".\n");
					}
					
				}
				else if(this.inventory.get(i).getClass().getSimpleName().equals("SolidUOM")) {
					SolidUOM solid = (SolidUOM)this.inventory.get(i);
					float weight = solid.getWeight();
					if(weight >= 1000) {
						weight = weight/1000;
						System.out.println("You have " + weight + " kilograms of " + solid.getItem() +".\n");
					}
					else {
						System.out.println("You have " + solid.getWeight() + " grams of " + solid.getItem() +".\n");
					}
				}
				found = true;
				break;
			}
		}
		if(!found) {
			System.out.println(item + " was not found in your inventory.\n");
		}
	}

}
