package service;

import model.Stock;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class StockService 
{
    
    
    //Create
    public int addStock(String name, String brand, String category, String unit, double price)
    {
        String sql = """
            INSERT INTO products
            (product_name, brand, category, unit, unit_price)
            VALUES (?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, name);
            stmt.setString(2, brand);
            stmt.setString(3, category);
            stmt.setString(4, unit);
            stmt.setDouble(5, price);
            
            stmt.executeUpdate();
            
            //Get Auto Incremented Product Id from SQL
            try (ResultSet rs = stmt.getGeneratedKeys())
            {
                if (rs.next())
                {
                    return rs.getInt(1);
                }
            }
            
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        return -1;
    }
    
    //Read
    public ArrayList<Stock> getStocks()
    {
        ArrayList<Stock> stocks = new ArrayList<>();
        
        
        String sql = """
            SELECT product_id, product_name, brand, category, unit, unit_price
            FROM products
            ORDER BY product_id
            """;
        
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                Stock stock = new Stock(
                rs.getInt("product_id"),
                rs.getString("product_name"),
                rs.getString("brand"),
                rs.getString("category"),
                rs.getString("unit"),
                rs.getDouble("unit_price")
                );
                
                stocks.add(stock);
            }
            
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        return stocks;
    }
    
    //Update
    public void updateStock(Stock stock)
    {
        String sql = """
            UPDATE products
            SET product_name = ?,
                brand = ?,
                category = ?,
                unit = ?,
                unit_price = ?
            WHERE product_id = ?
            """;
        
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, stock.getName());
            stmt.setString(2, stock.getBrand());
            stmt.setString(3, stock.getCategory());
            stmt.setString(4, stock.getUnit());
            stmt.setDouble(5, stock.getPrice());
            stmt.setInt   (6, stock.getId());
            
            stmt.executeUpdate();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    //Combine Products Table and Batch Table in SQL (This is part of refreshStockTable)
    public ArrayList<Object[]> getInventoryData() throws Exception
    {
        ArrayList<Object[]> Inventory = new ArrayList<>();
        
        String sql = """
            SELECT
                p.product_id,
                p.product_name,
                p.brand,
                p.category,
                COALESCE(SUM(b.quantity), 0) AS total_quantity,
                p.unit,
                p.unit_price,
                MIN(b.expiration_date) AS earliest_expiration
            FROM products p
            LEFT JOIN batches b
                ON p.product_id = b.product_id
            GROUP BY
                p.product_id,
                p.product_name,
                p.brand,
                p.category,
                p.unit,
                p.unit_price
            ORDER BY p.product_id
            """;
        
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                Object[] row = {rs.getInt("product_id"), rs.getString("product_name"), rs.getString("brand"), rs.getString("category"), rs.getDouble("total_quantity"), rs.getString("unit"), rs.getDouble("unit_price"), rs.getDate("earliest_expiration")};
                
                Inventory.add(row);
            }
        }
        
        return Inventory;
    }
    
    
    // Read inventory specifically for Staff View Inventory
    public ArrayList<Object[]> getStaffInventoryData() throws SQLException 
    {

        ArrayList<Object[]> inventory = new ArrayList<>();

            String sql = """
                SELECT
                    p.product_id,
                    p.product_name,
                    p.brand,
                    p.category,

                    COALESCE(
                        SUM(
                            CASE
                                WHEN b.quantity > 0
                                AND (
                                    b.expiration_date IS NULL
                                    OR b.expiration_date >= CURDATE()
                                )
                                THEN b.quantity
                                ELSE 0
                            END
                        ), 0
                    ) AS total_stock,

                    p.unit_price,

                    MIN(
                        CASE
                            WHEN b.quantity > 0
                            AND b.expiration_date >= CURDATE()
                            THEN b.expiration_date
                            ELSE NULL
                        END
                    ) AS earliest_expiration

                FROM products p

                LEFT JOIN batches b
                    ON p.product_id = b.product_id

                GROUP BY
                    p.product_id,
                    p.product_name,
                    p.brand,
                    p.category,
                    p.unit_price

                ORDER BY p.product_name ASC
                """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) 
        {
            while (rs.next()) {

                Object[] row = {
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("brand"),
                    rs.getString("category"),
                    rs.getDouble("total_stock"),
                    rs.getDouble("unit_price"),
                    rs.getDate("earliest_expiration")
                };

                inventory.add(row);
            }
        }

        return inventory;
    }
    
    
    
    
    
    
    
    
    //Delete
    public void deleteStock(int productId) throws SQLException
    {
        String sql = """
                     DELETE FROM products
                     WHERE product_id = ?
                     """;
        
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, productId);
            
            stmt.executeUpdate();
        } 
    }
    
    
    //Duplicate Data Handling
    public boolean stockExisted(String productname, String brand, String category, String unit, Double price, int selectedProductId) throws SQLException
    {
        String sql = """
            SELECT product_id
            FROM products
            WHERE product_id != ?
              AND LOWER(TRIM(product_name)) = LOWER(TRIM(?))
              AND LOWER(TRIM(brand)) = LOWER(TRIM(?))
              AND LOWER(TRIM(category)) = LOWER(TRIM(?))
              AND LOWER(TRIM(unit)) = LOWER(TRIM(?))
              AND unit_price = ?
            LIMIT 1
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, selectedProductId);
            stmt.setString(2, productname);
            stmt.setString(3, brand);
            stmt.setString(4, category);
            stmt.setString(5, unit);
            stmt.setDouble(6, price);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    //Duplicate Handling for Inventory
    public int findExistingProduct(String name, String brand, String category, String unit) throws SQLException
    {
        String sql = """
                     SELECT product_id
                     FROM products
                     WHERE LOWER(product_name) = LOWER(?)
                     AND LOWER(brand) = LOWER(?)
                     AND LOWER(category) = LOWER(?)
                     AND LOWER(unit) = LOWER(?)
                     LIMIT 1
                     """;
        
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, name.trim());
            stmt.setString(2, brand.trim());
            stmt.setString(3, category);
            stmt.setString(4, unit);
            
            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                     return rs.getInt("product_id");
                }
            }
        }
        
        return -1;
    }
    
    
    //Clear all data in SQL
    public void clearData() throws SQLException
    {
        String deleteBatches = "DELETE FROM batches";
        String deleteProducts = "DELETE FROM products";
        String deleteSuppliers = "DELETE FROM suppliers";
        
        try (Connection conn = DatabaseConnection.getConnection())
        {
            try (PreparedStatement stmt = conn.prepareStatement(deleteBatches))
            {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(deleteProducts))
            {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(deleteSuppliers))
            {
                stmt.executeUpdate();
            } 
        }  
    }
}
