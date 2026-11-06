package service;

import model.Batch;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;


public class BatchService 
{
    
    
    //Create
    public void addBatch(int productId, int supplierId, double quantity, String receivedDateText, String expirationText) throws SQLException
    {
        Date expirationDate = null;
        Date receivedDate;
        
        
        // Convert expiration text to SQL Date
        if (expirationText != null && !expirationText.isBlank())
        {
            LocalDate expirationLocalDate = LocalDate.parse(expirationText, java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
            
            expirationDate = Date.valueOf(expirationLocalDate);
        }
        
        //Convert received Date
        LocalDate receivedLocalDate = LocalDate.parse(receivedDateText, java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        
        receivedDate = Date.valueOf(receivedLocalDate);
        
        String sql = """ 
            INSERT INTO batches
            (product_id, supplier_id, quantity, expiration_date, received_date)
            VALUES (?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))  
        {
            stmt.setInt   (1, productId);
            stmt.setInt   (2, supplierId);
            stmt.setDouble(3, quantity);
            
            //Expiration date
            if (expirationDate != null)
            {
                stmt.setDate(4, expirationDate);
            }
            else
            {
                stmt.setNull(4, java.sql.Types.DATE);
            }
            
            //Received date = today
            stmt.setDate(5, receivedDate);
            
            stmt.executeUpdate();
            
        } 
    }
    
    //Read
    public ArrayList<Batch> getBatches(int productId) throws SQLException
    {
        ArrayList<Batch> batches = new ArrayList<>();
        
        
        String sql = """
                     SELECT b.batch_id, b.product_id, b.supplier_id, s.supplier_name, b.quantity, b.expiration_date, b.received_date
                     FROM batches b
                     LEFT JOIN suppliers s
                     ON b.supplier_id = s.supplier_id
                     WHERE b.product_id = ?
                     """;
        
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, productId);
            
            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    int supplierId = rs.getInt("supplier_id");

                    //if theres no Supplier
                    if (rs.wasNull())
                    {
                        supplierId = 0;
                    }

                    Batch batch = new Batch(rs.getInt("batch_id"), rs.getInt("product_id"), supplierId, rs.getString("supplier_name"),rs.getDouble("quantity"), rs.getDate("expiration_date") != null ? rs.getDate("expiration_date").toLocalDate() : null,  rs.getDate("received_date").toLocalDate());

                    batches.add(batch);
                }
            }
        }
        
        return batches;
    }
    
    //Update
    public void updateBatch(Batch batch) throws SQLException
    {
        String sql = """
                     UPDATE batches
                     SET product_id = ?, supplier_id = ?, quantity = ?, expiration_date = ?, received_date = ?
                     WHERE batch_id = ?
                     """;
        
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, batch.getProductId());
            
            if (batch.getSupplierId() > 0)
            {
                stmt.setInt(2, batch.getSupplierId());
            }
            else
            {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            stmt.setDouble(3, batch.getQuantity());
            
            if (batch.getExpirationDate() != null)
            {
                stmt.setDate(4, Date.valueOf(batch.getExpirationDate()));
            }
            else
            {
                stmt.setNull(4, java.sql.Types.DATE);
            }
            
            stmt.setDate(5, Date.valueOf(batch.getReceivedDate()));
            
            //WHERE batch_id = ?
            stmt.setInt(6, batch.getId());
            
            stmt.executeUpdate();
        } 
    }
    
    //Add Quantity
    public void stockIn(int batchId, double quantity) throws SQLException 
    {
        if (quantity <= 0 || !Double.isFinite(quantity)) 
        {
            throw new IllegalArgumentException("Stock-in quantity must be greater than zero.");
        }

    String sql = """
        UPDATE batches
        SET quantity = quantity + ?
        WHERE batch_id = ?
        """;

    try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) 
    {

        stmt.setDouble(1, quantity);
        stmt.setInt(2, batchId);

            if (stmt.executeUpdate() == 0) 
            {
                throw new SQLException("Batch does not exist.");
            }
        }
    }

public void stockOut(int batchId, double quantity) throws SQLException {
    if (quantity <= 0 || !Double.isFinite(quantity)) 
    {
       throw new IllegalArgumentException("Stock-out quantity must be greater than zero.");
    }

    String sql = """
        UPDATE batches
        SET quantity = quantity - ?
        WHERE batch_id = ?
          AND quantity >= ?
        """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setDouble(1, quantity);
        stmt.setInt(2, batchId);
        stmt.setDouble(3, quantity);

        if (stmt.executeUpdate() == 0) 
        {
            throw new SQLException("Insufficient stock or batch does not exist.");
        }
    }
}
    
    //Delete
    public void deleteBatch(int batchId) throws SQLException
    {
        String sql = """
                     DELETE FROM batches
                     WHERE batch_id = ?
                     """;
        
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, batchId);
            
            stmt.executeUpdate();
        } 
    }
    
    
    //Duplicate Handling
    public boolean isDuplicateBatch(Batch batch) throws SQLException
    {
        String sql = """
                     SELECT COUNT(*)
                     FROM batches
                     WHERE product_id = ?
                     AND supplier_id = ?
                     AND received_date = ?
                     AND expiration_date = ?
                     AND batch_id != ?
                     """;
        
          try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, batch.getProductId());
            stmt.setInt(2, batch.getSupplierId());
            stmt.setDate(3, Date.valueOf(batch.getReceivedDate()));

            if (batch.getExpirationDate() != null) {
                stmt.setDate(4, Date.valueOf(batch.getExpirationDate()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            stmt.setInt(5, batch.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
        
    }
    
    
    //Adjust Batches Quantity (Adjustment Form)
    public void adjustBatchStock(int batchId, double adjustmentQuantity, String adjustmentType, int userId, String reason) throws SQLException 
    {

        if (adjustmentQuantity <= 0) 
        {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (adjustmentType == null) 
        {
            throw new IllegalArgumentException("Please select an adjustment type");
        }

        String type = adjustmentType.trim().toUpperCase();

        if (type.equals("INCREASE STOCK")) 
        {
            type = "INCREASE";
        } 
        else if (type.equals("DECREASE STOCK")) 
        {
            type = "DECREASE";
        }

        if (!type.equals("INCREASE") && !type.equals("DECREASE")) 
        {
            throw new IllegalArgumentException("Invalid adjustment type.");
        }

        if (reason == null || reason.trim().isEmpty()) 
        {
            throw new IllegalArgumentException("Please select an adjustment reason.");
        }

        String findBatchSQL = """
            SELECT product_id, quantity
            FROM batches
            WHERE batch_id = ?
            FOR UPDATE
            """;

        String updateBatchSQL = """
            UPDATE batches
            SET quantity = ?
            WHERE batch_id = ?
            """;

        String insertAdjustmentSQL = """
            INSERT INTO stock_adjustments
                (batch_id, product_id, user_id,
                 adjustment_type, quantity, reason)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection()) 
        {

            conn.setAutoCommit(false);

            try 
            {
                int productId;
                double currentQuantity;

                // Retrieve and lock the selected batch
                try (PreparedStatement stmt = conn.prepareStatement(findBatchSQL)) 
                {

                    stmt.setInt(1, batchId);

                    try (ResultSet rs = stmt.executeQuery()) 
                    {
                        if (!rs.next()) 
                        {
                            throw new SQLException("Batch ID " + batchId + " does not exist.");
                        }

                        productId = rs.getInt("product_id");
                        currentQuantity = rs.getDouble("quantity");
                    }
                }

                // Calculate the new quantity
                double newQuantity;

                if (type.equals("INCREASE")) 
                {
                    newQuantity = currentQuantity + adjustmentQuantity;
                } 
                else 
                {
                    if (adjustmentQuantity > currentQuantity) 
                    {
                        throw new IllegalArgumentException("Cannot decrease by more than the current " + "quantity of " + currentQuantity + ".");
                    }

                    newQuantity = currentQuantity - adjustmentQuantity;
                }

                // Update the batch quantity
                try (PreparedStatement stmt =conn.prepareStatement(updateBatchSQL)) 
                {

                    stmt.setDouble(1, newQuantity);
                    stmt.setInt(2, batchId);

                    if (stmt.executeUpdate() != 1) 
                    {
                        throw new SQLException("Failed to update batch quantity.");
                    }
                }

                // Record the adjustment history
                try (PreparedStatement stmt =
                        conn.prepareStatement(insertAdjustmentSQL)) {

                    stmt.setInt(1, batchId);
                    stmt.setInt(2, productId);
                    stmt.setInt(3, userId);
                    stmt.setString(4, type);
                    stmt.setDouble(5, adjustmentQuantity);
                    stmt.setString(6, reason.trim());

                    stmt.executeUpdate();
                }

                conn.commit();

            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;

            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    
    
    //Show Stock Details in Adjustment Form
    public Object[] getBatchAdjustmentDetails(int batchId) throws SQLException 
    {
        String sql = """
                    SELECT
                        p.product_id,
                        p.product_name,
                        b.batch_id,
                        b.quantity
                    FROM batches b
                    JOIN products p
                        ON p.product_id = b.product_id
                    WHERE b.batch_id = ?
                    """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            stmt.setInt(1, batchId);

            try (ResultSet rs = stmt.executeQuery()) 
            {

                if (rs.next()) 
                {
                    return new Object[] 
                    {
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("batch_id"),
                        rs.getDouble("quantity")
                    };
                }
            }
        }

        return null;
    }
    
    
    //For cbreason_adjust
    public ArrayList<String> getAdjustmentReasons() throws SQLException 
    {
        ArrayList<String> reasons = new ArrayList<>();

        String sql = """
                    SELECT DISTINCT TRIM(reason) AS reason
                    FROM stock_adjustments
                    WHERE reason IS NOT NULL
                      AND TRIM(reason) <> ''
                    ORDER BY reason
                    """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);ResultSet rs = stmt.executeQuery()) 
        {
            while (rs.next()) 
            {
                reasons.add(rs.getString("reason"));
            }
        }

        return reasons;
    }
    
}
