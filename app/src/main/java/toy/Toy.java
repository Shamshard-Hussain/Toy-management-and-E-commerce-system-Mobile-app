package toy;

import java.io.Serializable;

public class Toy implements Serializable {
    private String id;
    private String ToyName;
    private String CategoryName;
    private String AgeGroup;
    private String Price;
    private String Quantity;
    byte[] Image;


    public Toy(){}

    public Toy(String id, String toyName, String categoryName, byte[] image, String ageGroup, String price, String quantity) {
        this.id = id;
        ToyName = toyName;
        CategoryName = categoryName;
        AgeGroup = ageGroup;
        Price = price;
        Quantity = quantity;
        Image = image;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAgeGroup() {
        return AgeGroup;
    }

    public void setAgeGroup(String ageGroup) {
        AgeGroup = ageGroup;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }
}
