package toy;

import java.io.Serializable;

public class Cart implements Serializable {
    private int Id;
    private String UserId;
    private String ToyName;
    private String CategoryName;
    private String Quantity;
    private String Price;
    byte[] Image;


    public Cart(int id,String userId, String toyName, String categoryName, byte[] image, String price, String quantity ) {
        Id = id;
        UserId = userId;
        ToyName = toyName;
        CategoryName = categoryName;
        Quantity = quantity;
        Price = price;
        Image = image;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getToyName() {
        return ToyName;
    }

    public void setToyName(String toyName) {
        ToyName = toyName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public Cart() {
    }
}
