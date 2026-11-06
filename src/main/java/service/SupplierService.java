package service;

import model.Supplier;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SupplierService 
{
    
    
    //Create
    public int getORaddSupplier(String supplierName) throws SQLException {

    if (supplierName == null || supplierName.trim().isEmpty()) {
        throw new IllegalArgumentException(
            "Supplier name cannot be empty."
        );
    }

    String name = supplierName.trim();

    String selectSql = """
        SELECT supplier_id
        FROM suppliers
        WHERE supplier_name_normalized = LOWER(TRIM(?))
        LIMIT 1
        """;

    try (Connection conn = DatabaseConnection.getConnection()) {

        // Check whether the supplier already exists.
        try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("supplier_id");
                }
            }
        }

        // Insert only when the supplier does not exist.
        String insertSql = """
            INSERT INTO suppliers (supplier_name)
            VALUES (?)
            """;

        try (PreparedStatement stmt = conn.prepareStatement(
                insertSql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);

            try {
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }

            } catch (SQLException e) {
                // Duplicate-key error: another request may have
                // inserted the same supplier first.
                if (e.getErrorCode() != 1062) {
                    throw e;
                }
            }
        }

        // If another request inserted it first, retrieve its ID.
        try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("supplier_id");
                }
            }
        }
    }

    throw new SQLException("Unable to find or create supplier.");
}

    // Read all suppliers.
    public ArrayList<Supplier> getSuppliers() throws SQLException 
    {

        ArrayList<Supplier> suppliers = new ArrayList<>();

        String sql = """
            SELECT supplier_id, supplier_name
            FROM suppliers
            ORDER BY supplier_name ASC
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                suppliers.add(new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name")
                ));
            }
        }

        return suppliers;
    }
    
    //Get Suppliers by Product
    public ArrayList<Supplier> getSuppliersByProduct(int productId) throws SQLException 
    {

        ArrayList<Supplier> suppliers = new ArrayList<>();

        String sql = """
            SELECT DISTINCT s.supplier_id, s.supplier_name
            FROM suppliers s
            JOIN batches b ON b.supplier_id = s.supplier_id
            WHERE b.product_id = ?
            ORDER BY s.supplier_name
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) 
        {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) 
            {
                while (rs.next()) 
                {
                    Supplier supplier = new Supplier(rs.getInt("supplier_id"), rs.getString("supplier_name"));

                    suppliers.add(supplier);
                }
            }
        }

        return suppliers;
    }

    // Update an existing supplier.
    public boolean updateSupplier(Supplier supplier) throws SQLException {

        if (supplier == null
                || supplier.getSupplierName() == null
                || supplier.getSupplierName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Supplier name cannot be empty."
            );
        }

         String name = supplier.getSupplierName().trim();

    String sql = """
        UPDATE suppliers
        SET supplier_name = ?
        WHERE supplier_id = ?
        """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, name);
        stmt.setInt(2, supplier.getSupplierId());

        return stmt.executeUpdate() > 0;
    }
}

    // Delete a supplier.
    public boolean deleteSupplier(int supplierId) throws SQLException {

        String sql = """
            DELETE FROM suppliers
            WHERE supplier_id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, supplierId);

            return stmt.executeUpdate() > 0;
        }
    }
}