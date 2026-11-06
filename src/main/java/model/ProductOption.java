package model;


public class ProductOption 
{
    private final int id;
    private final String name;

    public ProductOption(int id, String name) 
    {
        this.id = id;
        this.name = name;
    }

    public int getId() 
    {
        return id;
    }

    @Override
    public String toString() 
    {
        return name;
    }
}
