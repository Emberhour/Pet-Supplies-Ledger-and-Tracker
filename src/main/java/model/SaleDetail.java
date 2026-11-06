package model;


public class SaleDetail 
{
    private int id;
    private int saleId;
    private int productId;
    private double quantity;
    private double unitPrice;
    private double subtotal;
    
    //For Creating a new Temporary Receipt Item
    public SaleDetail(int id, int productId, double quantity, double unitPrice)
    {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = quantity * unitPrice;
    }
    
    //For Loading an Existing Sale Detail from SQL
    public SaleDetail(int id, int saleId, int productId, double quantity, double unitPrice, double subtotal)
    {
        this.id = id;
        this.saleId = saleId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }
    
    
    //Getter
    public int getId()
    {
        return id;
    }
    
    public int getSaleId()
    {
        return saleId;
    }
    
    public int getProductId()
    {
        return productId;
    }
    
    public double getQuantity()
    {
        return quantity;
    }
    
    public double getUnitPrice()
    {
        return unitPrice;
    }
    
    public double getSubtotal()
    {
        return subtotal;
    }
    
    //Setter
    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
        this.subtotal = quantity * this.unitPrice;
    }
    
    public void setUnitPrice(double unitPrice)
    {
        this.unitPrice = unitPrice;
        this.subtotal = this.quantity * unitPrice;
    }
    
    public void setSubtotal(double subtotal)
    {
        this.subtotal = subtotal;
    }
}
