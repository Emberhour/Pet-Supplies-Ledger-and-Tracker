package model;


import java.time.LocalDate;


public class Batch 
{
    private final int id;
    private final int productId;
    private int supplierId;
    private String supplierName;
    private double quantity;
    private LocalDate expirationDate;
    private LocalDate receivedDate;
            
    public Batch(int id, int productId, int supplierId, String supplierName, double quantity, LocalDate expirationDate, LocalDate receivedDate)
    {
        this.id = id;
        this.productId = productId;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.receivedDate = receivedDate;
    }
    
    //Getter
    public int getId()
    {
        return id;
    }
    
    public int getProductId()
    {
        return productId;
    }
    
    public int getSupplierId()
    {
        return supplierId;
    }
    
    public double getQuantity()
    {
        return quantity;
    }
    
    public String getSupplierName()
    {
        return supplierName;
    }
    
    public LocalDate getExpirationDate()
    {
        return expirationDate;
    }
    
    public LocalDate getReceivedDate()
    {
        return receivedDate;
    }
    
    //Setter
    public void setSupplierId(int supplierId)
    {
        this.supplierId = supplierId;
    }
    
    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
    }
    
    public void setExpirationDate(LocalDate expirationDate)
    {
        this.expirationDate = expirationDate;
    }
    
    public void setReceivedDate(LocalDate receivedDate)
    {
        this.receivedDate = receivedDate;
    }
    
    
    //Automated Status Checker
    public String getStatus()
    {
        if (quantity <= 0)
        {
            return "Depleted";
        }
        else if (expirationDate != null && expirationDate.isBefore(LocalDate.now()))
        {
            return "Expired";
        }
        else
        {
            return "Available";
        }
    }
}   


