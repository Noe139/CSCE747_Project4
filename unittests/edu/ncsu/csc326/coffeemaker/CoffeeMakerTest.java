import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoffeeMakerTest {
    private CoffeeMaker coffeeMaker;
    private Recipe recipe1;
    private Recipe recipe2;

    @Before
    public void setUp() {
        coffeeMaker = new CoffeeMaker();

        recipe1 = new Recipe();
        recipe1.setName("Coffee");
        recipe1.setPrice(50);
        recipe1.setAmtCoffee(3);
        recipe1.setAmtMilk(1);
        recipe1.setAmtSugar(1);
        recipe1.setAmtChocolate(0);

        recipe2 = new Recipe();
        recipe2.setName("Latte");
        recipe2.setPrice(100);
        recipe2.setAmtCoffee(3);
        recipe2.setAmtMilk(3);
        recipe2.setAmtSugar(1);
        recipe2.setAmtChocolate(0);
    }

    @Test
    public void testAddRecipe() {
        assertTrue(coffeeMaker.addRecipe(recipe1));
        assertTrue(coffeeMaker.addRecipe(recipe2));
        assertFalse(coffeeMaker.addRecipe(recipe1)); // Adding duplicate recipe
        Recipe[] recipes = coffeeMaker.getRecipes();
        assertEquals(recipe1, recipes[0]);
        assertEquals(recipe2, recipes[1]);
    }

    @Test
    public void testDeleteRecipe() {
        coffeeMaker.addRecipe(recipe1);
        assertTrue(coffeeMaker.deleteRecipe(recipe1));
        assertFalse(coffeeMaker.deleteRecipe(recipe1)); // Deleting non-existent recipe
        Recipe[] recipes = coffeeMaker.getRecipes();
        assertNull(recipes[0]);
    }

    @Test
    public void testEditRecipe() {
        coffeeMaker.addRecipe(recipe1);
        Recipe newRecipe = new Recipe();
        newRecipe.setName("Coffee");
        newRecipe.setPrice(60);
        newRecipe.setAmtCoffee(2);
        newRecipe.setAmtMilk(2);
        newRecipe.setAmtSugar(2);
        newRecipe.setAmtChocolate(1);
        assertTrue(coffeeMaker.editRecipe(recipe1, newRecipe));
        assertFalse(coffeeMaker.editRecipe(recipe2, newRecipe)); // Editing non-existent recipe
        Recipe[] recipes = coffeeMaker.getRecipes();
        assertEquals(newRecipe, recipes[0]);
    }

    @Test
    public void testAddInventory() throws InventoryException {
        coffeeMaker.addInventory(10, 10, 10, 10);
        Inventory inventory = coffeeMaker.checkInventory();
        assertEquals(25, inventory.getCoffee());
        assertEquals(25, inventory.getMilk());
        assertEquals(25, inventory.getSugar());
        assertEquals(25, inventory.getChocolate());
    }

    @Test(expected = InventoryException.class)
    public void testAddInventoryInvalid() throws InventoryException {
        coffeeMaker.addInventory("four", "3", "milk", "2");
    }

    @Test
    public void testCheckInventory() {
        Inventory inventory = coffeeMaker.checkInventory();
        assertNotNull(inventory);
        assertEquals(15, inventory.getCoffee()); // Default inventory
        assertEquals(15, inventory.getMilk());
        assertEquals(15, inventory.getSugar());
        assertEquals(15, inventory.getChocolate());
    }

    @Test
    public void testPurchaseBeverage() {
        coffeeMaker.addRecipe(recipe1);
        assertEquals(50, coffeeMaker.makeCoffee(recipe1, 100));
        Inventory inventory = coffeeMaker.checkInventory();
        assertEquals(12, inventory.getCoffee()); // Assuming initial inventory was 15
        assertEquals(14, inventory.getMilk());
        assertEquals(14, inventory.getSugar());
        assertEquals(15, inventory.getChocolate());
    }

    @Test
    public void testPurchaseBeverageInsufficientFunds() {
        coffeeMaker.addRecipe(recipe1);
        assertEquals(30, coffeeMaker.makeCoffee(recipe1, 30)); // Not enough money, should return the same amount
        Inventory inventory = coffeeMaker.checkInventory();
        assertEquals(15, inventory.getCoffee()); // Inventory should remain unchanged
        assertEquals(15, inventory.getMilk());
        assertEquals(15, inventory.getSugar());
        assertEquals(15, inventory.getChocolate());
    }

    @Test
    public void testPurchaseBeverageNoRecipe() {
        assertEquals(100, coffeeMaker.makeCoffee(recipe1, 100)); // No recipe added, should return all money
        Inventory inventory = coffeeMaker.checkInventory();
        assertEquals(15, inventory.getCoffee()); // Inventory should remain unchanged
        assertEquals(15, inventory.getMilk());
        assertEquals(15, inventory.getSugar());
        assertEquals(15, inventory.getChocolate());
    }

    @Test
    public void testPurchaseBeverageInsufficientIngredients() {
        coffeeMaker.addRecipe(recipe2); // Adding a recipe that requires more milk than available
        assertEquals(100, coffeeMaker.makeCoffee(recipe2, 100)); // Not enough ingredients, should return all money
        Inventory inventory = coffeeMaker.checkInventory();
        assertEquals(15, inventory.getCoffee()); // Inventory should remain unchanged
        assertEquals(15, inventory.getMilk());
        assertEquals(15, inventory.getSugar());
        assertEquals(15, inventory.getChocolate());
    }

    @Test
    public void testGetRecipes() {
        coffeeMaker.addRecipe(recipe1);
        coffeeMaker.addRecipe(recipe2);
        Recipe[] recipes = coffeeMaker.getRecipes();
        assertNotNull(recipes);
        assertEquals(recipe1, recipes[0]);
        assertEquals(recipe2, recipes[1]);
        assertNull(recipes[2]); // No third recipe
    }

    @Test
    public void testDeleteNonexistentRecipe() {
        assertFalse(coffeeMaker.deleteRecipe(recipe1)); // Trying to delete a recipe that hasn't been added
    }

    @Test
    public void testEditNonexistentRecipe() {
        Recipe newRecipe = new Recipe();
        newRecipe.setName("Nonexistent");
        newRecipe.setPrice(60);
        newRecipe.setAmtCoffee(2);
        newRecipe.setAmtMilk(2);
        newRecipe.setAmtSugar(2);
        newRecipe.setAmtChocolate(1);
        assertFalse(coffeeMaker.editRecipe(recipe1, newRecipe)); // Trying to edit a recipe that hasn't been added
    }

    @Test(expected = RecipeException.class)
    public void testSetRecipePriceInvalid() throws RecipeException {
        Recipe invalidRecipe = new Recipe();
        invalidRecipe.setPrice(-1); // Setting an invalid price
    }

    @Test(expected = RecipeException.class)
    public void testSetRecipeAmtCoffeeInvalid() throws RecipeException {
        Recipe invalidRecipe = new Recipe();
        invalidRecipe.setAmtCoffee(-1); // Setting an invalid coffee amount
    }

    @Test(expected = RecipeException.class)
    public void testSetRecipeAmtMilkInvalid() throws RecipeException {
        Recipe invalidRecipe = new Recipe();
        invalidRecipe.setAmtMilk(-1); // Setting an invalid milk amount
    }

    @Test(expected = RecipeException.class)
    public void testSetRecipeAmtSugarInvalid() throws RecipeException {
        Recipe invalidRecipe = new Recipe();
        invalidRecipe.setAmtSugar(-1); // Setting an invalid sugar amount
    }

    @Test(expected = RecipeException.class)
    public void testSetRecipeAmtChocolateInvalid() throws RecipeException {
        Recipe invalidRecipe = new Recipe();
        invalidRecipe.setAmtChocolate(-1); // Setting an invalid chocolate amount
    }
}

