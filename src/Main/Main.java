package Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import Foods.FluidUOM;
import Foods.FoodItem;
import Foods.NoUOM;
import Foods.SolidUOM;
import User.User;

public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		User user = Login(in);
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.ser"))){
			String name = (String)ois.readObject();
			if(user.getName().equals(name)) {
				user.inventory = (List<FoodItem>) ois.readObject();
				user.shoppingList = (TreeMap<FoodItem, Integer>) ois.readObject();
				user.recipeBook = (TreeMap<String, List<FoodItem>>) ois.readObject();
				System.out.println("Welcome back " + name + "! Your inventory, shopping list, and recipe book are saved!\n");
			}
			else {
				System.out.println("You are a different user than the one who previously logged in. Welcome!\n");
			}
			
		}
		catch(FileNotFoundException e) {
			System.out.println("There is no serialized data file found\n");
		}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		MainMenu(in, user);
	}
	
	public static void serialize(User user, Scanner in) {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.ser"))) {
			oos.writeObject(user.getName());
			oos.writeObject(user.inventory);
			oos.writeObject(user.shoppingList);
			oos.writeObject(user.recipeBook);
			System.out.println("Serialized the data!");
		} 
		catch (IOException e) {
			System.out.println("Failed to serialize the data!");
		}
		
	}
	
	public static User Login(Scanner in) {
		System.out.println("What is your name? ");
		String name = in.nextLine();
		System.out.println();
		User user = new User(name);
		return user;
	}
	
	public static void MainMenu(Scanner in, User user) {
		System.out.println("What would you like to do?");
		System.out.println("(1) View Your Inventory");
		System.out.println("(2) Add an item to your shopping list");
		System.out.println("(3) Remove an item from your shopping list");
		System.out.println("(4) View Shopping List");
		System.out.println("(5) Add an item to your inventory");
		System.out.println("(6) Add multiple items to your inventory");
		System.out.println("(7) Remove an item from your inventory");
		System.out.println("(8) Remove multiple items from your inventory");
		System.out.println("(9) Add recipe to recipe book");
		System.out.println("(10) Check if you have the groceries to make a specific recipe");
		System.out.println("(11) Search your inventory for a specific item");
		System.out.println("(12) View Recipe Book");
		System.out.println("(13) Quit\n");
		
		String input = in.nextLine();
		int choice = 0;
		try {
			choice = Integer.parseInt(input);
		}
		catch(NumberFormatException e) {
			System.out.println("You did not enter a number. Try again!");
			MainMenu(in, user);
		}
		
		if(choice < 1 || choice >= 14) {
			System.out.println("Invalid Selection, try again!");
			MainMenu(in, user);
		}
		else {
			if(choice == 1) {
				user.showInventory();
				MainMenu(in, user);
			}
			else if(choice == 2) {
				addToShoppingList(in, user);
			}
			
			else if(choice == 3) {
				removeFromShoppingList(in, user);
			}
			else if(choice == 4) {
				//(4) View Shopping List
				user.showShoppingList();
				MainMenu(in, user);
			}
			else if(choice == 5) {
				addItemToInventory(in, user);
			}
			else if(choice == 6) {
				addItemsToInventory(in, user);
			}
			else if(choice == 7) {
				removeItemFromInventory(in, user);
			}
			else if(choice == 8) {
				removeItemsFromInventory(in, user);
			}
			else if(choice == 9) {
				addRecipe(in, user);
			}
			else if(choice == 10) {
				checkRecipe(in, user);
			}
			else if(choice == 11) {
				//(11) Search your inventory for a specific item
				System.out.println("What item would you like to search your inventory for?");
				String item = in.nextLine().toUpperCase();
				user.searchInventory(item);
				MainMenu(in, user);
				
			}
			else if(choice == 12) {
				//(12) View Recipe Book
				user.showRecipeBook();
				MainMenu(in, user);
			}
			else if(choice == 13) {
				
				serialize(user, in);
				in.close();
				System.exit(0);
				
			}
		}
	}
	

	public static void addToShoppingList(Scanner in, User user) {
		//(2) Add items to your shopping list
		System.out.println("Please enter the item you would like to add to your shopping list: ");
		String item = in.nextLine().strip().toUpperCase();
		
		int uomSelection = 0;
		while(true) {
			System.out.println("Please enter the unit of measurement:");
			// ('milliliters', 'grams', or 'single')
			System.out.println("    1. milliliters");
			System.out.println("    2. grams");
			System.out.println("    3. single");
			String uom = in.nextLine().strip();
			
			try {
				uomSelection = Integer.parseInt(uom);
			}
			catch(NumberFormatException e) {
				System.out.println("You did not enter a number. Try again!");
				System.out.println("Enter \"1\" for milliliters, \"2\" for grams, and \"3\" for single or no UOM.");
				continue;
			}
			if(uomSelection > 0 && uomSelection <= 3) {
				break;
			}
			else {
				System.out.println("Enter \"1\" for milliliters, \"2\" for grams, and \"3\" for single or no UOM.");
			}
		}
		
		String uom = "";
		if(uomSelection == 1) {
			uom = "ml";
		}
		else if(uomSelection == 2) {
			uom = "g";
		}
		else if(uomSelection == 3) {
			uom = "";
		}
		
		int weight = 1;
		if(uomSelection != 3) {
			while(true) {
				if(uomSelection == 1) {
					System.out.println("Please enter the number of milliliters of " + item + " you would like to add to your shopping list: ");
				}
				else if(uomSelection == 2) {
					System.out.println("Please enter the number of grams of " + item + " you would like to add to your shopping list: ");
				}
				String currSelection = in.nextLine().strip();
				try {
					weight = Integer.parseInt(currSelection);
					break;
				}
				catch(NumberFormatException e) {
					System.out.println("You did not enter a number. Try again!");
					continue;
				}
			}
		}
		
		int quantity = 0;
		while(true) {
			System.out.println("Please enter the quantity of " + item + " you would like to add to your shopping list: ");
			String currSelection = in.nextLine().strip();
			try {
				quantity = Integer.parseInt(currSelection);
				break;
			}
			catch(NumberFormatException e) {
				System.out.println("You did not enter a number. Try again!");
				continue;
			}
		}
		
		user.addToShoppingList(item, weight, uom, quantity);
		MainMenu(in, user);
	}
	
	public static void removeFromShoppingList(Scanner in, User user) {
		//(3) Remove items from your shopping list
		System.out.println("Please enter the item you would like to remove from your shopping list: ");
		String item = in.nextLine().strip().toUpperCase();
		int weight = 1;
		while(true) {
			System.out.println("Please enter the weight of the item you would like to remove from your shopping list (If UOM is 'single', enter 1): ");

			String currSelection = in.nextLine().strip();
			try {
				weight = Integer.parseInt(currSelection);
				break;
			}
			catch(NumberFormatException e) {
				System.out.println("You did not enter a number. Try again!");
				continue;
			}
		}
		
		int quantity = 1;
		while(true) {
			System.out.println("Please enter the quantity of the item you would like to remove from your shopping list: ");
			String currSelection = in.nextLine().strip();
			try {
				quantity = Integer.parseInt(currSelection);
				break;
			}
			catch(NumberFormatException e) {
				System.out.println("You did not enter a number. Try again!");
				continue;
			}
		}
							
		user.removeFromShoppingList(item, weight, quantity);
		MainMenu(in, user);
	}
	
	public static void addItemToInventory(Scanner in, User user) {
		//(5) Add an item to your inventory
		System.out.println("Please enter the item you would like to add to your inventory: ");
		String item = in.nextLine().strip().toUpperCase();
		
		int uomSelection = 0;
		while(true) {
			System.out.println("Please enter the unit of measurement:");
			System.out.println("    1. milliliters");
			System.out.println("    2. grams");
			System.out.println("    3. single");
			String uom = in.nextLine().strip();
			
			try {
				uomSelection = Integer.parseInt(uom);
			}
			catch(NumberFormatException e) {
				System.out.println("You did not enter a number. Try again!");
				System.out.println("Enter \"1\" for milliliters, \"2\" for grams, and \"3\" for single or no UOM.");
				continue;
			}
			if(uomSelection > 0 && uomSelection <= 3) {
				break;
			}
			else {
				System.out.println("Enter \"1\" for milliliters, \"2\" for grams, and \"3\" for single or no UOM.");
			}
		}
		
		System.out.println("Please enter the unit of measurement ('milliliters', 'grams', or 'single'):");
		String uom = "";
		
		if(uomSelection == 1) {
			uom = "ml";
		}
		else if(uomSelection == 2) {
			uom = "g";
		}
		else if(uomSelection == 3) {
			uom = "";
		}
		
		int weight = 1;
		if(uomSelection != 3) {
			while(true) {
				if(uomSelection == 1) {
					System.out.println("Please enter the number of milliliters of " + item + " you would like to add to your inventory: ");
				}
				else if(uomSelection == 2) {
					System.out.println("Please enter the number of grams of " + item + " you would like to add to your inventory: ");
				}
				String currSelection = in.nextLine().strip();
				try {
					weight = Integer.parseInt(currSelection);
					break;
				}
				catch(NumberFormatException e) {
					System.out.println("You did not enter a number. Try again!");
					continue;
				}
			}
		}
		
		int quantity = 0;
		while(true) {
			System.out.println("Please enter the quantity of " + item + " you would like to add to your inventory: ");
			String currSelection = in.nextLine().strip();
			try {
				quantity = Integer.parseInt(currSelection);
				break;
			}
			catch(NumberFormatException e) {
				System.out.println("You did not enter a number. Try again!");
				continue;
			}
		}
		
		user.addItemToInventory(item, weight, uom, quantity);
		MainMenu(in, user);
	}
	
	public static void addItemsToInventory(Scanner in, User user) {
		//(6) Add multiple items to your inventory
		TreeMap<FoodItem, Integer> itemsToAdd = new TreeMap<>();
		while(true) {
			System.out.println("Please enter the item you would like to add to your inventory ('No' to stop adding): ");
			String item = in.nextLine().strip().toUpperCase();
			if(item.toUpperCase().equals("NO")) {
				break;
			}
			int uomSelection = 0;
			while(true) {
				System.out.println("Please enter the unit of measurement:");
				System.out.println("    1. milliliters");
				System.out.println("    2. grams");
				System.out.println("    3. single");
				String uom = in.nextLine().strip();
				
				try {
					uomSelection = Integer.parseInt(uom);
				}
				catch(NumberFormatException e) {
					System.out.println("You did not enter a number. Try again!");
					System.out.println("Enter \"1\" for milliliters, \"2\" for grams, and \"3\" for single or no UOM.");
					continue;
				}
				if(uomSelection > 0 && uomSelection <= 3) {
					break;
				}
				else {
					System.out.println("Enter \"1\" for milliliters, \"2\" for grams, and \"3\" for single or no UOM.");
				}
			}
			
			int weight = 1;
			if(uomSelection != 3) {
				while(true) {
					if(uomSelection == 1) {
						System.out.println("Please enter the number of milliliters of " + item + " you would like to add to your inventory: ");
					}
					else if(uomSelection == 2) {
						System.out.println("Please enter the number of grams of " + item + " you would like to add to your inventory: ");
					}
					String currSelection = in.nextLine().strip();
					try {
						weight = Integer.parseInt(currSelection);
						break;
					}
					catch(NumberFormatException e) {
						System.out.println("You did not enter a number. Try again!");
						continue;
					}
				}
			}
			
			int quantity = 0;
			while(true) {
				System.out.println("Please enter the quantity of " + item + " you would like to add to your inventory: ");
				String currSelection = in.nextLine().strip();
				try {
					quantity = Integer.parseInt(currSelection);
					break;
				}
				catch(NumberFormatException e) {
					System.out.println("You did not enter a number. Try again!");
					continue;
				}
			}
			
			
			if(uomSelection == 1) {
				itemsToAdd.put(new FluidUOM(item, weight), quantity);
			}
			else if(uomSelection == 2) {
				itemsToAdd.put(new SolidUOM(item, weight), quantity);
			}
			else {
				itemsToAdd.put(new NoUOM(item, weight), quantity);
			}
			System.out.println();
		}
		
		user.addItemsToInventory(itemsToAdd);
		MainMenu(in, user);
	}
	
	public static void removeItemFromInventory(Scanner in, User user) {
		//(7) Remove an item from your inventory
		System.out.println("Please enter the item you would like to remove from your inventory: ");
		String item = in.nextLine().strip().toUpperCase();
		
		int weight = 1;
		while(true) {
			System.out.println("Please enter the weight/quantity of " + item + " you would like to remove from your inventory: ");

			String currSelection = in.nextLine().strip();
			try {
				weight = Integer.parseInt(currSelection);
				break;
			}
			catch(NumberFormatException e) {
				System.out.println("You did not enter a number. Try again!");
				continue;
			}
		}
		
		user.removeItemFromInventory(item, weight);
		MainMenu(in, user);
	}
	
	public static void removeItemsFromInventory(Scanner in, User user) {
		//(8) Remove multiple items from your inventory
		List<FoodItem> itemsToRemove = new ArrayList<>();
		while(true) {
			System.out.println("Please enter the items you would like to remove from your inventory ('No' to stop removing): ");
			String item = in.nextLine().strip().toUpperCase();
			if(item.toUpperCase().equals("NO")) {
				break;
			}
			
			int weight = 1;
			while(true) {
				System.out.println("Please enter the weight/quantity of " + item + " you would like to remove from your inventory: ");

				String currSelection = in.nextLine().strip();
				try {
					weight = Integer.parseInt(currSelection);
					break;
				}
				catch(NumberFormatException e) {
					System.out.println("You did not enter a number. Try again!");
					continue;
				}
			}
		
			itemsToRemove.add(new FoodItem(item, weight));
			System.out.println();
		}
		
		user.removeItemsFromInventory(itemsToRemove);
		MainMenu(in, user);
	}
	
	public static void addRecipe(Scanner in, User user) {
		//(9) Add recipe to recipe book
		List<FoodItem> recipe = new ArrayList<>();
		int n = 0;
		while(true) {
			System.out.println("How many ingredients are in this recipe?");
			String currSelection = in.nextLine().strip();
			try {
				n = Integer.parseInt(currSelection);
				break;
			}
			catch(NumberFormatException e) {
				System.out.println("You did not enter a number. Try again!");
				continue;
			}
		}
		
		for(int i = 0; i < n; i++) {
			System.out.println("Enter an ingredient in the recipe:");
			String item = in.nextLine().strip().toUpperCase();
			
			int uomSelection = 0;
			while(true) {
				System.out.println("Please enter the unit of measurement of " + item + " in this recipe:");
				// ('milliliters', 'grams', or 'single')
				System.out.println("    1. milliliters");
				System.out.println("    2. grams");
				System.out.println("    3. single");
				String uom = in.nextLine().strip();
				
				try {
					uomSelection = Integer.parseInt(uom);
				}
				catch(NumberFormatException e) {
					System.out.println("You did not enter a number. Try again!");
					System.out.println("Enter \"1\" for milliliters, \"2\" for grams, and \"3\" for single or no UOM.");
					continue;
				}
				if(uomSelection > 0 && uomSelection <= 3) {
					break;
				}
				else {
					System.out.println("Enter \"1\" for milliliters, \"2\" for grams, and \"3\" for single or no UOM.");
				}
			}
			
			int amount = 0;
			while(true) {
				if(uomSelection == 1) {
					System.out.println("Enter the number of milliliters of " + item + " in this recipe: ");
				}
				else if(uomSelection == 2) {
					System.out.println("Enter the number of grams of " + item + " in this recipe: ");
				}
				else if(uomSelection == 3) {
					System.out.println("Enter the quantity of " + item + " in this recipe: ");
				}
				String currSelection = in.nextLine().strip();
				try {
					amount = Integer.parseInt(currSelection);
					break;
				}
				catch(NumberFormatException e) {
					System.out.println("You did not enter a number. Try again!");
					continue;
				}
			}
		
			if(uomSelection == 1) {
				recipe.add(new FluidUOM(item, amount));
			}
			else if(uomSelection == 2) {
				recipe.add(new SolidUOM(item, amount));
			}
			else {
				recipe.add(new NoUOM(item, amount));
			}
			System.out.println();
		}
		
		System.out.println("What would you like to name this recipe?");
		String recipeName = in.nextLine().strip().toUpperCase();
		user.addRecipe(recipeName, recipe);
		System.out.println("Adding the recipe to the recipe book!\n");
		
		int select = 0;
		while(true) {
			System.out.println("Would you like to check if you have the right ingredients to make this recipe?");
			System.out.println("    1. Yes");
			System.out.println("    2. No");
			
			String selection = in.nextLine().strip();
			
			try {
				select = Integer.parseInt(selection);
			}
			catch(NumberFormatException e) {
				System.out.println("You did not enter a number. Try again!");
				System.out.println("Enter \"1\" to make this recipe, or \"2\" to not make this recipe.\n");
				continue;
			}
			if(select > 0 && select <= 2) {
				break;
			}
			else {
				System.out.println("Enter \"1\" to make this recipe, or \"2\" to not make this recipe.\n");
			}
		}
		
		if(select == 1) {
			boolean checked = user.checkRecipe(recipe);
			if(checked) {
				makeRecipe(in, user, recipe);
			}

		}
		else {
			System.out.println("Okay!");
		}
		MainMenu(in, user);
	}
	
	public static void checkRecipe(Scanner in, User user) {
		//(10) Check if you have the groceries to make a specific recipe
		List<FoodItem> recipe = new ArrayList<>();
		
		if(user.recipeBook.size() <= 0) {
			System.out.println("You don't have any recipes in your recipe book!");
		}
		else {
			while(true) {
				user.showRecipeBook();
				System.out.println("Enter the recipe name you would like to check:");
				String recipeChoice = in.nextLine().strip().toUpperCase();
				
				recipe = user.recipeBook.get(recipeChoice);
				if(recipe == null) {
					System.out.println("That recipe is not in your recipe book!\n");
				}
				else {
					break;
				}
				
			}
			boolean foundAll = user.checkRecipe(recipe);
			if(foundAll) {
				makeRecipe(in, user, recipe);
			}
			
		}
		MainMenu(in, user);
			
		
	}
	
	public static void makeRecipe(Scanner in, User user, List<FoodItem> recipe) {
		int select = 0;
		while(true) {
			System.out.println("You have all the ingredients for this recipe in your inventory! Would you like to make this recipe?");
			System.out.println("    1. Yes");
			System.out.println("    2. No");
			
			String selection = in.nextLine().strip();
			
			try {
				select = Integer.parseInt(selection);
			}
			catch(NumberFormatException e) {
				System.out.println("You did not enter a number. Try again!");
				System.out.println("Enter \"1\" to make this recipe, or \"2\" to not make this recipe.");
				continue;
			}
			if(select > 0 && select <= 2) {
				break;
			}
			else {
				System.out.println("Enter \"1\" to make this recipe, or \"2\" to not make this recipe.");
			}
		}
		
		if(select == 1) {
			System.out.println("Okay! All the ingedients in this recipe will be deducted from your inventory!\n");
			user.makeRecipe(recipe);
			
		}
		else {
			System.out.println("Okay!");
		}
		MainMenu(in, user);
	}

}
