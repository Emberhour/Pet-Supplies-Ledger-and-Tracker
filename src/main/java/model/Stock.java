package model;


public class Stock 
{
    private int id;
    private String name;
    private String brand;
    private String category;
    private String unit;
    private double price;
    
    public Stock(int id, String name, String brand, String category, String unit, double price)
    {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.unit = unit;
        this.price = price;
    }
    
    //Getter  
    public int getId()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }
    
    @Override
    public String toString()
    {
        return getName();
    }
    
    public String getBrand()
    {
        return brand;
    }
    
    public String getCategory()
    {
        return category;
    }
    
    public String getUnit()
    {
        return unit;
    }
    
    public double getPrice()
    {
        return price;
    }
    
    //Setter
    public void setName(String name)
    {
        this.name = name;
    }
    
    public void setBrand(String brand)
    {
        this.brand = brand;
    }
    
    public void setCategory(String category)
    {
        this.category = category;
    }
    
    public void setUnit(String unit)
    {
        this.unit = unit;
    }
    
    public void setPrice(double price)
    {
        this.price = price;
    }
    
}
