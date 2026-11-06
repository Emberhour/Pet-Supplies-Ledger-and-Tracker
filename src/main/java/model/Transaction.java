package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Transaction 
{
    private int productId;
    private String productname;
    private String category;
    private int quantity;
    private double totalPrice;
    private String date;
    
    public Transaction(int productId,String productname, String category, int quantity, double totalPrice)
    {
        this.productId = productId;
        this.productname = productname;
        this.category = category;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss a");
        this.date = LocalDateTime.now().format(formatter);
    }
    
    //Getter
    public int getProductID()
    {
        return productId;
    }
    
    public String getProductName()
    {
        return productname;
    }
    
    public String getCategory()
    {
        return category;
    }
    
    public int getQuantity()
    {
        return quantity;
    }
    
    public double getTotalPrice()
    {
        return totalPrice;
    }
    
    public String getDate()
    {
        return date;
    }
}
