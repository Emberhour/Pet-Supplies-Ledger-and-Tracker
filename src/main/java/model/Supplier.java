package model;


public class Supplier 
{
    private int supplierId;
    private String supplierName;
    
    public Supplier(int supplierId, String supplierName)
    {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
    }
    
    public int getSupplierId()
    {
        return supplierId;
    }
    
    public String getSupplierName()
    {
        return supplierName;
    }
    
    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }
    
    @Override
    public String toString()
    {
        return supplierName;
    }
}
