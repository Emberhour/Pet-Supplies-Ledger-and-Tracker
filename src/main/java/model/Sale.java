package model;

import java.sql.Timestamp;


public class Sale 
{
    private int       id;
    private Integer   customerId;
    private int       userId;
    private Timestamp saleDate;
    private String    paymentMethod;
    private double    totalAmount;
    private String    status;
    
    public Sale(int id, Integer customerId, int userId, Timestamp saleDate, String paymentMethod, double totalAmount, String status)
    {
        this.id = id;
        this.customerId = customerId;
        this. userId = userId;
        this.saleDate = saleDate;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    //Getter
    public int getId()
    {
        return id;
    }
    
    public Integer getCustomerId()
    {
        return customerId;
    }
    
    public int getUserId()
    {
        return userId;
    }
    
    public Timestamp getSaleDate()
    {
        return saleDate;
    }
    
    public String getPaymentMethod()
    {
        return paymentMethod;
    }
    
    public double getTotalAmount()
    {
        return totalAmount;
    }
    
    public String getStatus() //Abstraction
    {
        return status;
    }
    
    //Setter
    public void setSaleDate(Timestamp saleDate)
    {
        this.saleDate = saleDate;
    }
    
    public void setPaymentMethod(String paymentMethod)
    {
        this.paymentMethod = paymentMethod;
    }
    
    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
}
