package model;


public class Account 
{
    private int userId;
    private String username;
    private String password;
    private String role;
    
    
    public Account(int userId, String username, String password, String role)
    {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    
    //getters
    public int getUserId()
    {
        return userId;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public String getRole()
    {
        return role;
    }
}
