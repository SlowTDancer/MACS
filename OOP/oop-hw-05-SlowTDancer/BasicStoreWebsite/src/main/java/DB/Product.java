package DB;

public class Product {
    private String productId;
    private String productName;
    private String imageFileName;
    private double productPrice;
    public Product(String productId, String productName, String imageFileName, double productPrice){
        this.productId = productId;
        this.productName = productName;
        this.imageFileName = imageFileName;
        this.productPrice = productPrice;
    }

    public String getProductId() {
        return productId;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public String getImageFileName() {
        return imageFileName;
    }
}