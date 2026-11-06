package service;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Sale;
import model.SaleDetail;


public class SaleService 
{
    
    
    // Retrieve only Products that have Saleable Stock
    public ArrayList<Object[]> getSaleableInventoryData()throws SQLException 
    {

        ArrayList<Object[]> inventory = new ArrayList<>();

        String sql = """
            SELECT
                p.product_id,
                p.product_name,
                p.brand,
                p.category,
                stock.total_quantity,
                p.unit,
                p.unit_price,
                stock.earliest_expiration

            FROM products p

            INNER JOIN (
                SELECT
                    product_id,

                    SUM(quantity) AS total_quantity,

                    MIN(
                        CASE
                            WHEN expiration_date IS NOT NULL
                            THEN expiration_date
                            ELSE NULL
                        END
                    ) AS earliest_expiration

                FROM batches

                WHERE quantity > 0
                  AND (
                      expiration_date IS NULL
                      OR expiration_date >= CURDATE()
                  )

                GROUP BY product_id

            ) stock
                ON p.product_id = stock.product_id

            ORDER BY p.product_name ASC
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) 
        {
            while (rs.next()) 
            {

                Object[] row = {
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("brand"),
                    rs.getString("category"),
                    rs.getDouble("total_quantity"),
                    rs.getString("unit"),
                    rs.getDouble("unit_price"),
                    rs.getDate("earliest_expiration")
                };

                inventory.add(row);
            }
        }

        return inventory;
    }
    
    
    //Record the entire Transaction
    public int recordSale(ArrayList<SaleDetail> record, Integer customerId, int userId, String paymentMethod) throws SQLException
    {
        if (record == null || record.isEmpty())
        {
            throw new SQLException("Sale has no Items.");
        }
        
        
        String saleSQL =   """
                           INSERT INTO sales
                           (customer_id, user_id, payment_method, total_amount, status)
                           VALUES (?, ?, ?, ?, 'Completed')
                           """;
        
        String detailSQL = """
                           INSERT INTO sale_details
                           (sale_id, product_id, batch_id, quantity, unit_price, subtotal)
                           VALUES (?, ?, ?, ?, ?, ?)
                           """;
        
        String batchSQL =  """
                           SELECT batch_id, quantity
                           FROM batches
                           WHERE product_id = ?
                            AND quantity > 0
                            AND (expiration_date IS NULL OR expiration_date >= CURDATE())
                           ORDER BY received_date ASC, batch_id ASC
                           FOR UPDATE
                           """;
        
        String updateBatchSQL = """
                                UPDATE batches
                                SET quantity = ?
                                WHERE batch_id = ?
                                """;
        
        double totalAmount = 0;
        
        for (SaleDetail detail : record)
        {
            totalAmount += detail.getSubtotal();
        }
        
        try (Connection conn = DatabaseConnection.getConnection())
        {
            try 
            {
                //Start Database Transaction
                conn.setAutoCommit(false);
                
                int saleId;
                
                //Insert Sales
                try (PreparedStatement stmt = conn.prepareStatement(saleSQL, PreparedStatement.RETURN_GENERATED_KEYS))
                {
                    if (customerId == null)
                    {
                        stmt.setNull(1, java.sql.Types.INTEGER);
                    }
                    else
                    {
                        stmt.setInt(1, customerId);
                    }

                    stmt.setInt(2, userId);
                    stmt.setString(3, paymentMethod);
                    stmt.setDouble(4, totalAmount);

                    stmt.executeUpdate();


                    try (ResultSet rs = stmt.getGeneratedKeys())
                    {
                        if (!rs.next())
                        {
                            throw new SQLException("Failed to create sale.");
                        }

                        saleId = rs.getInt(1);
                    } 

                }
                
                //Process Each Sale Detail
                for (SaleDetail detail : record)
                {
                    double remainingQuantity = detail.getQuantity();
                    
                   
                    //Get Batches Using FIFO
                    try (PreparedStatement stmt = conn.prepareStatement(batchSQL))
                    {
                        stmt.setInt(1, detail.getProductId());

                        try (ResultSet rs = stmt.executeQuery())
                        {
                            while (rs.next() && remainingQuantity > 0)
                            {
                                int batchId = rs.getInt("batch_id");

                                double batchQuantity = rs.getDouble("quantity");

                                double subtractQuantities = Math.min(remainingQuantity, batchQuantity);

                                double newQuantity = batchQuantity - subtractQuantities;

                                // Calculate the subtotal for this portion of the batch
                                double subtotal = subtractQuantities * detail.getUnitPrice();

                                // Record the sale detail, including the batch used (FIFO)
                                try (PreparedStatement detailStmt = conn.prepareStatement(detailSQL)) 
                                {

                                    detailStmt.setInt(1, saleId);
                                    detailStmt.setInt(2, detail.getProductId());
                                    detailStmt.setInt(3, batchId);
                                    detailStmt.setDouble(4, subtractQuantities);
                                    detailStmt.setDouble(5, detail.getUnitPrice());
                                    detailStmt.setDouble(6, subtotal);

                                    detailStmt.executeUpdate();
                                }

                                // Update Batch Quantity
                                try (PreparedStatement updateStmt = conn.prepareStatement(updateBatchSQL))
                                {
                                    updateStmt.setDouble(1, newQuantity);
                                    updateStmt.setInt(2, batchId);

                                    updateStmt.executeUpdate();
                                }

                                remainingQuantity -= subtractQuantities;
                            }
                        }
                    }
                    
                    //If Not Enough Stock
                    if (remainingQuantity > 0)
                    {
                        throw new SQLException("Not enough Stock for Product Id: " + detail.getProductId());
                    }   
                }
                
                
                //Everything Successful
                conn.commit();
                
                return saleId; 
            }
            catch (SQLException e)
            {
                //Undo Everything if Something Failed
                conn.rollback();
                throw e;
            }
            finally
            {
                conn.setAutoCommit(true);
            }
        } 
    }
    
    //Get Transaction History
    public ArrayList<Sale> getSales() throws SQLException
    {
        ArrayList<Sale> sales = new ArrayList<>();
        
        
        String sql = """
                     SELECT sale_id, customer_id, user_id, sale_date, payment_method, total_amount, status
                     FROM sales
                     ORDER By sale_id DESC
                     """;
        
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                Sale sale = new Sale(rs.getInt("sale_id"), rs.getObject("customer_id") != null ? rs.getInt("customer_id") : null, rs.getInt("user_id"), rs.getTimestamp("sale_date"), rs.getString("payment_method"), rs.getDouble("total_amount"), rs.getString("status"));
                
                sales.add(sale);
            }
        }
        
        return sales;
    }
    
    //Get Details of Selected Transaction
    public ArrayList<Object[]> getSaleDetails(int saleId) throws SQLException 
    {
        ArrayList<Object[]> details = new ArrayList<>();

        String sql = """
            SELECT
                sd.product_id,
                p.product_name,
                SUM(sd.quantity) AS total_quantity,
                sd.unit_price,
                SUM(sd.subtotal) AS total_subtotal
            FROM sale_details sd
            JOIN products p
                ON p.product_id = sd.product_id
            WHERE sd.sale_id = ?
            GROUP BY
                sd.product_id,
                p.product_name,
                sd.unit_price
            ORDER BY p.product_name
            """;

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, saleId);

            try (ResultSet rs = stmt.executeQuery()) 
            {
                while (rs.next()) {

                    Object[] row = {
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("total_quantity"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("total_subtotal")
                    };

                    details.add(row);
                }
            }
        }

        return details;
    }

    public void voidSale(int saleId) throws SQLException {

    String checkSaleSQL = """
        SELECT status
        FROM sales
        WHERE sale_id = ?
        FOR UPDATE
        """;

    String getDetailsSQL = """
        SELECT product_id, batch_id, quantity
        FROM sale_details
        WHERE sale_id = ?
        FOR UPDATE
        """;

    String restoreBatchSQL = """
        UPDATE batches
        SET quantity = quantity + ?
        WHERE batch_id = ?
          AND product_id = ?
        """;

    String voidSaleSQL = """
        UPDATE sales
        SET status = 'Voided'
        WHERE sale_id = ?
          AND status = 'Completed'
        """;

    try (Connection conn = DatabaseConnection.getConnection()) 
    {

        conn.setAutoCommit(false);

        try {
            // 1. Verify the transaction exists and is Completed
            try (PreparedStatement stmt = conn.prepareStatement(checkSaleSQL)) 
            {
                stmt.setInt(1, saleId);

                try (ResultSet rs = stmt.executeQuery()) 
                {

                    if (!rs.next()) 
                    {
                        throw new SQLException("Transaction not found: " + saleId);
                    }

                    String status = rs.getString("status");

                    if (!"Completed".equalsIgnoreCase(status)) 
                    {
                        throw new SQLException("Only Completed transactions can be voided.");
                    }
                }
            }

            // 2. Retrieve original batch allocations
            ArrayList<Object[]> allocations = new ArrayList<>();

            try (PreparedStatement stmt = conn.prepareStatement(getDetailsSQL)) 
            {
                stmt.setInt(1, saleId);

                try (ResultSet rs = stmt.executeQuery()) 
                {

                    while (rs.next()) 
                    {

                        Object batchValue = rs.getObject("batch_id");

                        if (batchValue == null) 
                        {
                            throw new SQLException("This transaction has no recorded batch " + "allocation. Its stock cannot be " + "restored automatically.");
                        }

                        allocations.add(new Object[] {rs.getInt("product_id"), rs.getInt("batch_id"), rs.getDouble("quantity")});
                    }
                }
            }

            if (allocations.isEmpty()) 
            {
                throw new SQLException("No sale details found for transaction " + saleId);
            }

            // 3. Restore quantities to their original batches
            try (PreparedStatement stmt = conn.prepareStatement(restoreBatchSQL)) 
            {

                for (Object[] allocation : allocations) 
                {

                    int productId = (Integer) allocation[0];
                    int batchId = (Integer) allocation[1];
                    double quantity = (Double) allocation[2];

                    if (quantity <= 0) 
                    {
                        throw new SQLException("Invalid quantity in sale details.");
                    }

                    stmt.setDouble(1, quantity);
                    stmt.setInt(2, batchId);
                    stmt.setInt(3, productId);

                    if (stmt.executeUpdate() != 1) 
                    {
                        throw new SQLException("Could not restore stock to batch " + batchId + ".");
                    }
                }
            }

            // 4. Change the transaction status
            try (PreparedStatement stmt = conn.prepareStatement(voidSaleSQL)) 
            {

                stmt.setInt(1, saleId);

                if (stmt.executeUpdate() != 1) 
                {
                    throw new SQLException(
                            "Unable to void transaction " + saleId
                    );
                }
            }

            // 5. Commit both changes together
            conn.commit();

        } 
        catch (SQLException | RuntimeException e) 
        {
            conn.rollback();
            throw e;
        }
        finally 
        {
            conn.setAutoCommit(true);
        }
    }
}
    
    
    public ArrayList<String> getPaymentMethods() throws SQLException 
    {
        ArrayList<String> methods = new ArrayList<>();

        String sql = """
            SELECT DISTINCT payment_method
            FROM sales
            WHERE payment_method IS NOT NULL
              AND TRIM(payment_method) <> ''
            ORDER BY payment_method
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) 
        {
            while (rs.next()) 
            {
                methods.add(rs.getString("payment_method"));
            }
        }

        return methods;
    }
    
    
    
    public Object[] getSalesSummaryStats() throws SQLException 
    {
        String sql = """
            SELECT
                COALESCE(
                    SUM(
                        CASE
                            WHEN LOWER(status) = 'completed'
                            THEN total_amount
                            ELSE 0
                        END
                    ), 0
                ) AS total_sales,

                COUNT(*) AS transaction_count,

                COALESCE(
                    SUM(
                        CASE
                            WHEN LOWER(status) = 'voided'
                            THEN 1
                            ELSE 0
                        END
                    ), 0
                ) AS voided_count

            FROM sales
            """;

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            if (rs.next()) {
                return new Object[] {
                    rs.getDouble("total_sales"),
                    rs.getInt("transaction_count"),
                    rs.getInt("voided_count")
                };
            }
        }

        return new Object[] {0.0, 0, 0};
    }
    
    
    
    public Object[] getSalesPeriodDashboardStats() throws SQLException 
    {
        String sql = """
            SELECT
                COALESCE(
                    SUM(
                        CASE
                            WHEN LOWER(status) = 'completed'
                             AND YEARWEEK(sale_date, 3)
                                 = YEARWEEK(CURDATE(), 3)
                            THEN total_amount
                            ELSE 0
                        END
                    ), 0
                ) AS weekly_sales,

                COALESCE(
                    SUM(
                        CASE
                            WHEN LOWER(status) = 'completed'
                             AND sale_date >= DATE_FORMAT(
                                 CURDATE(), '%Y-%m-01'
                             )
                             AND sale_date < DATE_ADD(
                                 DATE_FORMAT(CURDATE(), '%Y-%m-01'),
                                 INTERVAL 1 MONTH
                             )
                            THEN total_amount
                            ELSE 0
                        END
                    ), 0
                ) AS monthly_sales,

                SUM(
                    CASE
                        WHEN LOWER(status) = 'completed'
                         AND sale_date >= DATE_FORMAT(
                             CURDATE(), '%Y-%m-01'
                         )
                         AND sale_date < DATE_ADD(
                             DATE_FORMAT(CURDATE(), '%Y-%m-01'),
                             INTERVAL 1 MONTH
                         )
                        THEN 1
                        ELSE 0
                    END
                ) AS completed_transactions,

                SUM(
                    CASE
                        WHEN LOWER(status) = 'voided'
                         AND sale_date >= DATE_FORMAT(
                             CURDATE(), '%Y-%m-01'
                         )
                         AND sale_date < DATE_ADD(
                             DATE_FORMAT(CURDATE(), '%Y-%m-01'),
                             INTERVAL 1 MONTH
                         )
                        THEN 1
                        ELSE 0
                    END
                ) AS voided_transactions

            FROM sales
            """;

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            if (rs.next()) {
                return new Object[] {
                    rs.getDouble("weekly_sales"),
                    rs.getDouble("monthly_sales"),
                    rs.getInt("completed_transactions"),
                    rs.getInt("voided_transactions")
                };
            }
        }

        return new Object[] {0.0, 0.0, 0, 0};
    }
}
