import DB.Product;
import DB.ShoppingCart;
import junit.framework.TestCase;
public class ShoppingCartTest extends TestCase {
    public void testSC(){
        ShoppingCart sc = new ShoppingCart();
        assertTrue(sc.getShoppingCart().isEmpty());
        Product p1 = new Product("abc", "kamateli", "nardi", 2.21);
        Product p2 = new Product("hex", "hexagon", "6", 6.66);
        sc.addItem(p1);
        sc.addItem(p1);
        sc.addItem(p2);
        sc.addItem(p2);
        sc.addItem(p2);
        assertEquals(2, sc.getAmount(p1));
        assertEquals(3, sc.getAmount(p2));
        assertEquals(4.42,sc.getItemCost(p1));
        sc.updateShoppingCart(p1, -1);
        sc.updateShoppingCart(p2, 6);
        assertEquals(1, sc.getShoppingCart().size());
        assertEquals(39.96, sc.getItemCost(p2));
        assertEquals(39.96, sc.getTotalCost());
    }
}
