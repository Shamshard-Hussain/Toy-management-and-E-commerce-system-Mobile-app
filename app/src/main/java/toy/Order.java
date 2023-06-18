package toy;

import java.io.Serializable;

public class Order implements Serializable {
    private int Id;
    private String ToyName;
    private String Quantity;
    private String Email;
    private String UserName;
    private String Address;
    private String PostalCode;
    private String Status;
    private String Date;

    public Order(int id, String toyName, String quantity, String email, String userName, String address, String postalCode,  String date,String status) {
        Id = id;
        ToyName = toyName;
        Quantity = quantity;
        Email = email;
        UserName = userName;
        Address = address;
        PostalCode = postalCode;
        Date = date;
        Status = status;

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getToyName() {
        return ToyName;
    }

    public void setToyName(String toyName) {
        ToyName = toyName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
