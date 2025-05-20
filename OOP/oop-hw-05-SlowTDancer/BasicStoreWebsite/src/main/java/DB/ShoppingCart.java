package DB;

import DB.Product;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private Map<String, Integer> sc;
    private double totalCost;
    public ShoppingCart(){
        sc = new HashMap<>();
        totalCost = 0;
    }

    public Map<String, Integer> getShoppingCart(){
        return sc;
    }

    public void addItem(Product product){
        if(sc.containsKey(product.getProductId())){
            sc.put(product.getProductId(), sc.get(product.getProductId()) + 1);
        }else{
            sc.put(product.getProductId(), 1);
        }
        totalCost += product.getProductPrice();
    }

    public int getAmount(Product product){
        if(!sc.containsKey(product.getProductId())) return 0;
        return sc.get(product.getProductId());
    }

    public void updateShoppingCart(Product product, int amount){
        if(!sc.containsKey(product.getProductId())) return;
        if(amount < 0) amount = 0;
        int diff = amount - sc.get(product.getProductId());
        totalCost += diff * product.getProductPrice();
        if(amount == 0) {
            sc.remove(product.getProductId());
        }else{
            sc.put(product.getProductId(), amount);
        }
    }

    public double getItemCost(Product product){
        if(!sc.containsKey(product.getProductId())) return 0;
        double number = sc.get(product.getProductId()) * product.getProductPrice();;
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(number));
    }

    public double getTotalCost(){
        double number = totalCost;
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(number));
    }
}
