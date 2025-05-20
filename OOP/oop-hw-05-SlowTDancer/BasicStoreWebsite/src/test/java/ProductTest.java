import DB.Product;
import junit.framework.TestCase;
public class ProductTest extends TestCase{
    public void testProduct(){
        Product p1 = new Product("abc", "kamateli", "nardi", 2.21);
        Product p2 = new Product("hex", "hexagon", "6", 6.66);
        assertEquals("abc", p1.getProductId());
        assertEquals("kamateli", p1.getProductName());
        assertEquals("nardi", p1.getImageFileName());
        assertEquals(2.21, p1.getProductPrice());
        assertEquals("hex", p2.getProductId());
        assertEquals("hexagon", p2.getProductName());
        assertEquals("6", p2.getImageFileName());
        assertEquals(6.66, p2.getProductPrice());
    }
}
