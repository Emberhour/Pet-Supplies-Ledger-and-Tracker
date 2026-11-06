package gui;

import database.DatabaseConnection;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import model.Account;
import service.AccountService;
import service.StockService;
import service.BatchService;
import service.SaleService;
import service.SupplierService;





public class MainFrame extends javax.swing.JFrame 
{
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainFrame.class.getName());
    
    private final StockService stockService;
    private final BatchService batchService;
    private final SupplierService supplierService;
    private final SaleService saleService;
    private final AccountService accountService;
    
    
    
    public MainFrame()
    {
        this(new StockService(), new BatchService(), new SupplierService(), new SaleService());
    }
    
    public MainFrame(StockService stockService, BatchService batchService, SupplierService supplierService, SaleService saleService) 
    {
        //setUndecorated(true);
        
        
        initComponents();
        
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        
        
        
        this.batchService = batchService;
        this.stockService = stockService;
        this.accountService = new AccountService();
        this.supplierService = supplierService;
        this.saleService = saleService;
        
        //setResizable(false);
        
        
        
        btnlogin.setBackground(Color.WHITE);
    }

   
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Main_Panel = new javax.swing.JPanel();
        MainScroll = new javax.swing.JScrollPane();
        Body = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jLayeredPane3 = new javax.swing.JLayeredPane();
        login_north = new javax.swing.JLayeredPane();
        login_center = new javax.swing.JPanel();
        loginmain_north = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        loginmain_center = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        loginform_center = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        txtusername = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        txtpassword = new javax.swing.JPasswordField();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        btnlogin = new javax.swing.JButton();
        login_south = new javax.swing.JLayeredPane();
        jLayeredPane4 = new javax.swing.JLayeredPane();
        jLayeredPane5 = new javax.swing.JLayeredPane();
        bghome = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Main_Panel.setBackground(new java.awt.Color(204, 255, 255));
        Main_Panel.setPreferredSize(new java.awt.Dimension(1024, 768));
        Main_Panel.setLayout(new java.awt.BorderLayout());

        MainScroll.setPreferredSize(new java.awt.Dimension(1024, 768));

        Body.setPreferredSize(new java.awt.Dimension(512, 729));
        Body.setLayout(new javax.swing.OverlayLayout(Body));

        jLayeredPane1.setAlignmentX(0.0F);
        jLayeredPane1.setAlignmentY(0.0F);
        jLayeredPane1.setLayout(new java.awt.GridLayout(1, 2));

        javax.swing.GroupLayout jLayeredPane2Layout = new javax.swing.GroupLayout(jLayeredPane2);
        jLayeredPane2.setLayout(jLayeredPane2Layout);
        jLayeredPane2Layout.setHorizontalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );
        jLayeredPane2Layout.setVerticalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 729, Short.MAX_VALUE)
        );

        jLayeredPane1.add(jLayeredPane2);

        jLayeredPane3.setLayout(new java.awt.BorderLayout());

        login_north.setPreferredSize(new java.awt.Dimension(506, 30));

        javax.swing.GroupLayout login_northLayout = new javax.swing.GroupLayout(login_north);
        login_north.setLayout(login_northLayout);
        login_northLayout.setHorizontalGroup(
            login_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );
        login_northLayout.setVerticalGroup(
            login_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jLayeredPane3.add(login_north, java.awt.BorderLayout.NORTH);

        login_center.setBackground(new java.awt.Color(255, 255, 255));
        login_center.setLayout(new java.awt.BorderLayout());

        loginmain_north.setPreferredSize(new java.awt.Dimension(446, 350));
        loginmain_north.setLayout(new java.awt.GridLayout(1, 0));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Header(resized).png"))); // NOI18N
        loginmain_north.add(jLabel3);

        login_center.add(loginmain_north, java.awt.BorderLayout.NORTH);

        loginmain_center.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(50, 369));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 219, Short.MAX_VALUE)
        );

        loginmain_center.add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(50, 369));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 219, Short.MAX_VALUE)
        );

        loginmain_center.add(jPanel2, java.awt.BorderLayout.EAST);

        loginform_center.setBackground(new java.awt.Color(255, 255, 255));
        loginform_center.setPreferredSize(new java.awt.Dimension(346, 300));
        loginform_center.setLayout(new java.awt.GridLayout(1, 0));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setAlignmentX(0.0F);
        jPanel3.setAlignmentY(0.0F);
        jPanel3.setLayout(new java.awt.GridLayout(5, 1));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setAlignmentX(0.0F);
        jPanel6.setAlignmentY(0.0F);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel6);

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Username");
        jPanel3.add(jLabel1);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel7);

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Password");
        jPanel3.add(jLabel2);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel8);

        loginform_center.add(jPanel3);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new java.awt.GridLayout(5, 1));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 173, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );

        jPanel14.add(jPanel9);

        txtusername.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jPanel14.add(txtusername);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 173, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );

        jPanel14.add(jPanel10);

        txtpassword.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jPanel14.add(txtpassword);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 173, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );

        jPanel14.add(jPanel5);

        loginform_center.add(jPanel14);

        loginmain_center.add(loginform_center, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setPreferredSize(new java.awt.Dimension(446, 80));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel12, java.awt.BorderLayout.CENTER);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setPreferredSize(new java.awt.Dimension(446, 80));
        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        btnlogin.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 18)); // NOI18N
        btnlogin.setText("Log In");
        btnlogin.setPreferredSize(new java.awt.Dimension(250, 70));
        btnlogin.addActionListener(this::btnloginActionPerformed);
        jPanel13.add(btnlogin);

        jPanel4.add(jPanel13, java.awt.BorderLayout.NORTH);

        loginmain_center.add(jPanel4, java.awt.BorderLayout.SOUTH);

        login_center.add(loginmain_center, java.awt.BorderLayout.CENTER);

        jLayeredPane3.add(login_center, java.awt.BorderLayout.CENTER);

        login_south.setPreferredSize(new java.awt.Dimension(506, 30));

        javax.swing.GroupLayout login_southLayout = new javax.swing.GroupLayout(login_south);
        login_south.setLayout(login_southLayout);
        login_southLayout.setHorizontalGroup(
            login_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );
        login_southLayout.setVerticalGroup(
            login_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jLayeredPane3.add(login_south, java.awt.BorderLayout.SOUTH);

        jLayeredPane4.setPreferredSize(new java.awt.Dimension(30, 669));

        javax.swing.GroupLayout jLayeredPane4Layout = new javax.swing.GroupLayout(jLayeredPane4);
        jLayeredPane4.setLayout(jLayeredPane4Layout);
        jLayeredPane4Layout.setHorizontalGroup(
            jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jLayeredPane4Layout.setVerticalGroup(
            jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 669, Short.MAX_VALUE)
        );

        jLayeredPane3.add(jLayeredPane4, java.awt.BorderLayout.EAST);

        jLayeredPane5.setPreferredSize(new java.awt.Dimension(30, 669));

        javax.swing.GroupLayout jLayeredPane5Layout = new javax.swing.GroupLayout(jLayeredPane5);
        jLayeredPane5.setLayout(jLayeredPane5Layout);
        jLayeredPane5Layout.setHorizontalGroup(
            jLayeredPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jLayeredPane5Layout.setVerticalGroup(
            jLayeredPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 669, Short.MAX_VALUE)
        );

        jLayeredPane3.add(jLayeredPane5, java.awt.BorderLayout.WEST);

        jLayeredPane1.add(jLayeredPane3);

        Body.add(jLayeredPane1);

        bghome.setIcon(new javax.swing.ImageIcon("C:\\Users\\user\\OneDrive\\Desktop\\Netbeans Programs\\Java Projects\\002-PetSupplies_Ledger_and_Tracker\\src\\main\\resources\\images\\bg-oversize.png")); // NOI18N
        bghome.setAlignmentY(0.0F);
        Body.add(bghome);

        MainScroll.setViewportView(Body);

        Main_Panel.add(MainScroll, java.awt.BorderLayout.CENTER);

        getContentPane().add(Main_Panel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnloginActionPerformed
        String username = txtusername.getText().trim();
        String password = new String(txtpassword.getPassword());

        try 
        {
            // 1. Authenticate using the database
            Account account = accountService.login(username, password);

            if (account == null) 
            {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Get the actual database user ID
            int userId = account.getUserId();

            System.out.println("Logged-in username: " + account.getUsername());
            System.out.println("Database User ID: " + userId);
            System.out.println("Role: " + account.getRole());

            // 3. Open the correct frame
            if (account.getRole().equalsIgnoreCase("ADMIN")) 
            {
                AdminFrame admin = new AdminFrame(stockService, batchService, supplierService, saleService, userId);

                admin.setVisible(true);
                this.dispose();

            } 
            else if (account.getRole().equalsIgnoreCase("STAFF")) 
            {

                StaffFrame staff = new StaffFrame(stockService, batchService, supplierService, saleService,userId);

                staff.setVisible(true);
                this.dispose();
            }
            else 
            {
                JOptionPane.showMessageDialog(this, "This account has an unrecognized role.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }

        } 
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(this, "Unable to connect to the user database: " + e.getMessage(), "Database Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnloginActionPerformed

    
    private int getUserIdByUsername(String username) throws SQLException 
    {

        String sql = """
            SELECT user_id
            FROM users
            WHERE username = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) 
            {
                if (rs.next()) 
                {
                    return rs.getInt("user_id");
                }
            }
        }

        return -1;
    }

    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Body;
    private javax.swing.JScrollPane MainScroll;
    private javax.swing.JPanel Main_Panel;
    private javax.swing.JLabel bghome;
    private javax.swing.JButton btnlogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JLayeredPane jLayeredPane3;
    private javax.swing.JLayeredPane jLayeredPane4;
    private javax.swing.JLayeredPane jLayeredPane5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel login_center;
    private javax.swing.JLayeredPane login_north;
    private javax.swing.JLayeredPane login_south;
    private javax.swing.JPanel loginform_center;
    private javax.swing.JPanel loginmain_center;
    private javax.swing.JPanel loginmain_north;
    private javax.swing.JPasswordField txtpassword;
    private javax.swing.JTextField txtusername;
    // End of variables declaration//GEN-END:variables
}
