package gui;

import java.sql.*;
import database.DatabaseConnection;

import java.util.ArrayList;
import java.util.prefs.Preferences;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;


import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


import model.ProductOption;
import model.Batch;
import model.Sale;
import model.Stock;
import model.Supplier;
import model.Transaction;
import service.BatchService;
import service.SaleService;
import service.StockService;
import service.SupplierService;
import service.TransactionService;


public class AdminFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminFrame.class.getName());
    
    private TableRowSorter<DefaultTableModel> inventorySorter;
    private TableRowSorter<DefaultTableModel> batchSorter;
    private ArrayList<Batch> displayedBatches = new ArrayList<>();
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    
    private final Preferences stockPrefs = Preferences.userNodeForPackage(AdminFrame.class);
    private final BatchService managebatch;
    private final StockService managestock;
    private final SupplierService managesupplier;
    private final SaleService managesale;
    
    //Set cardLayout variable
    private CardLayout parentCardLayout;
    private CardLayout productContentCardLayout;
    private CardLayout saleCardLayout;
    private CardLayout inventorySearchCardLayout;
    
    private int UserId = -1;
    private int selectedProductId = -1;
    private int selectedBatchId = -1;
    private int selectedAdjustmentBatchId = -1;
    private int selectedAdjustmentProductId = -1;
    private int selectedBatchProductId = -1;

    
    public AdminFrame(StockService stockService, BatchService batchService, SupplierService supplierService, SaleService saleService, int userId) 
    {
        initComponents();

        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);
       
        //Disable Buttons and Textfields
        Main_Panel.setOpaque(false);
        
        
        this.managestock = stockService;
        this.managebatch = batchService;
        this.managesupplier = supplierService;
        this.managesale = saleService;
        this.UserId = userId;
        
        System.out.println("AdminFrame received User ID: " + this.UserId);
        
        
        //Set Header Size
        tbinventory.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 35));
        tbbatches.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 35));
        tbsales_admin.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 35));
        tbsaledetails.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 35));
        
        
        refreshStockTable();
        loadSalesDashboardSummary();
        loadSalesPerformanceDashboard();
        loadAdjustmentReasons();
        
        
        //Initialize cardLayout for Inventory and Batch table
        parentCardLayout = (CardLayout) Content_Panel.getLayout();
        productContentCardLayout = (CardLayout) ProductContent.getLayout();
        saleCardLayout = (CardLayout) SaleContent.getLayout();
        inventorySearchCardLayout = (CardLayout) InventorySearch_Content.getLayout();
        
        customizeButton(btnviewbatches);
  

        

        
        //Configure Design Opacity
        MainScroll.setOpaque(false);
        MainScroll.getViewport().setOpaque(false);
        btnhome.setBorderPainted(false);
        btninventory.setBorderPainted(false);
        btnsales.setBorderPainted(false);
        btnlogout.setBorderPainted(false);
        
        

        btnhome.setFocusPainted(false);
        btninventory.setFocusPainted(false);
        btnsales.setFocusPainted(false);
        btnlogout.setFocusPainted(false);
        
 
        
        
        //Vertical Scroll Bar
        MainScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI()
        {
            @Override
            protected void configureScrollBarColors()
            {
                this.thumbColor = new Color(64, 64, 64);
                this.trackColor = new Color(255, 255, 255);
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation)
            {
                return createArrowButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation)
            {
                return createArrowButton();
            }
            
            private JButton createArrowButton()
            {
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                button.setForeground(Color.WHITE);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setOpaque(true);
                return button;
            }
        });
        
        //Horizontal Scroll Bar
        MainScroll.getHorizontalScrollBar().setUI(new BasicScrollBarUI()
        {
            @Override
            protected void configureScrollBarColors()
            {
                this.thumbColor = new Color(64, 64, 64);
                this.trackColor = new Color(255, 255, 255);
            }
            
             @Override
            protected JButton createDecreaseButton(int orientation)
            {
                return createArrowButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation)
            {
                return createArrowButton();
            }
            
            private JButton createArrowButton()
            {
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                button.setForeground(Color.WHITE);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setOpaque(true);
                return button;
            }
        });
        
        

        tbinventory.setRowHeight(32);
        tbinventory.setShowGrid(false);
        tbinventory.setIntercellSpacing(new Dimension(0, 0));
        tbinventory.setSelectionBackground(new Color(230, 220, 170));
        tbinventory.setSelectionForeground(Color.BLACK);


        tbbatches.setRowHeight(32);
        tbbatches.setShowGrid(false);
        tbbatches.setIntercellSpacing(new Dimension(0, 0));
        tbbatches.setSelectionBackground(new Color(230, 220, 170));
        tbbatches.setSelectionForeground(Color.BLACK);
        
        
        tbsales_admin.setRowHeight(32);
        tbsales_admin.setShowGrid(false);
        tbsales_admin.setIntercellSpacing(new Dimension(0, 0));
        tbsales_admin.setSelectionBackground(new Color(230, 220, 170));
        tbsales_admin.setSelectionForeground(Color.BLACK);
        
        
        tbsaledetails.setRowHeight(32);
        tbsaledetails.setShowGrid(false);
        tbsaledetails.setIntercellSpacing(new Dimension(0, 0));
        tbsaledetails.setSelectionBackground(new Color(230, 220, 170));
        tbsaledetails.setSelectionForeground(Color.BLACK);

    
        
        
    
        //Table Batches Selection Listener
        tbbatches.getSelectionModel().addListSelectionListener(e -> 
        {

            if (e.getValueIsAdjusting()) 
            {
                return;
            }

            int row = tbbatches.getSelectedRow();

            if (row == -1) 
            {
                lblbatchidBatch_show.setText("");
                lblstatusBatch_show.setText("");

                selectedAdjustmentBatchId = -1;
                selectedAdjustmentProductId = -1;
                return;
            }

            int modelRow = tbbatches.convertRowIndexToModel(row);

            if (modelRow < 0 || modelRow >= displayedBatches.size()) 
            {
                return;
            }

            Batch batch = displayedBatches.get(modelRow);

            // Display the selected batch details
            lblbatchidBatch_show.setText(String.valueOf(batch.getId()));
            lblstatusBatch_show.setText(batch.getStatus());

            // Store IDs for Stock Adjustment
            selectedAdjustmentBatchId = batch.getId();
            selectedAdjustmentProductId = batch.getProductId();
        });
        
        
        
        
        

    
    
    
    
    
        
    }
    
    
    
    

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Main_Panel = new BackgroundPanel();
        Body = new javax.swing.JPanel();
        Navigation_Panel = new javax.swing.JPanel();
        Nav_north = new javax.swing.JPanel();
        Adminicon_north = new javax.swing.JPanel();
        Adminicon_south = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Nav_center = new javax.swing.JPanel();
        btnhome = new javax.swing.JButton();
        btninventory = new javax.swing.JButton();
        btnsales = new javax.swing.JButton();
        Nav_south = new javax.swing.JPanel();
        Logout_north = new javax.swing.JPanel();
        Logout_south = new javax.swing.JPanel();
        btnlogout = new javax.swing.JButton();
        MainScroll = new javax.swing.JScrollPane();
        Content_Panel = new javax.swing.JPanel();
        Home = new javax.swing.JPanel();
        Home_tab = new javax.swing.JLayeredPane();
        Home_east = new javax.swing.JLayeredPane();
        Home_west = new javax.swing.JLayeredPane();
        Home_south = new javax.swing.JLayeredPane();
        Home_center = new javax.swing.JLayeredPane();
        Product_Summary = new javax.swing.JLayeredPane();
        Summary_north = new javax.swing.JLayeredPane();
        Total_Products = new javax.swing.JPanel();
        Total_Products_title = new javax.swing.JLabel();
        lblweeklysales_show = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        Total_Stock = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        lblsalesthismonth_show = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        Summary_south = new javax.swing.JLayeredPane();
        Stock_level = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        lblcompletedtransactions_show = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        Earliest_Expiration = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        lblvoidedthismonth_show = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        Home_north = new javax.swing.JLayeredPane();
        bghome = new javax.swing.JLabel();
        Inventory = new javax.swing.JPanel();
        Inventrory_header = new javax.swing.JPanel();
        Inventory_Actions_title = new javax.swing.JLabel();
        Inventory_center = new javax.swing.JPanel();
        InventoryActions = new javax.swing.JPanel();
        AddNewProduct = new javax.swing.JPanel();
        btnaddnewproduct = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        ProductInventory = new javax.swing.JPanel();
        Product_Inventory_title = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        ProductInventory_form = new javax.swing.JPanel();
        InventorySearch_Content = new javax.swing.JPanel();
        InventorySearchCard = new javax.swing.JPanel();
        txtsearch_inventory = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        BatchSearchCard = new javax.swing.JPanel();
        txtsearch_inventory1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        ProductScrollPane = new javax.swing.JScrollPane();
        Productlist = new javax.swing.JPanel();
        ProductContent = new javax.swing.JPanel();
        ProductCard = new javax.swing.JPanel();
        ProductInfo = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbinventory = new javax.swing.JTable();
        ProductButtons = new javax.swing.JPanel();
        btnviewbatches = new javax.swing.JButton();
        BatchCard = new javax.swing.JPanel();
        BatchInfo = new javax.swing.JPanel();
        Batchinfo_north = new javax.swing.JPanel();
        Batch_ID = new javax.swing.JLabel();
        lblbatchidBatch_show = new javax.swing.JLabel();
        Batchinfo_center = new javax.swing.JPanel();
        BatchinfoInner_north = new javax.swing.JPanel();
        Status = new javax.swing.JLabel();
        lblstatusBatch_show = new javax.swing.JLabel();
        BatchScroll = new javax.swing.JScrollPane();
        tbbatches = new javax.swing.JTable();
        BatchButtons = new javax.swing.JPanel();
        btnstockinexistingsupplier_batches = new javax.swing.JButton();
        btnstockinnewsupplier_batches = new javax.swing.JButton();
        btnadjust_batches = new javax.swing.JButton();
        btnback_batches = new javax.swing.JButton();
        Inventory_south = new javax.swing.JPanel();
        Inventory_east = new javax.swing.JPanel();
        Inventory_west = new javax.swing.JPanel();
        Add_InitialStock = new javax.swing.JPanel();
        Initial_north = new javax.swing.JPanel();
        Initial_center = new javax.swing.JPanel();
        Initial_left = new javax.swing.JPanel();
        ProductDetails_title = new javax.swing.JPanel();
        Add_Inventory_title = new javax.swing.JLabel();
        ProductDetails_form = new javax.swing.JPanel();
        ProductDetails_west = new javax.swing.JPanel();
        ProductDetails_east = new javax.swing.JPanel();
        ProductDetails_center = new javax.swing.JPanel();
        Product_Name = new javax.swing.JLabel();
        txtproductname_initial = new javax.swing.JTextField();
        Brand = new javax.swing.JLabel();
        txtbrand_initial = new javax.swing.JTextField();
        Category = new javax.swing.JLabel();
        cbcategory_initial = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cbunit_initial = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtunitprice_initial = new javax.swing.JTextField();
        ProductDetails_north = new javax.swing.JPanel();
        ProductDetails_south = new javax.swing.JPanel();
        Initial_right = new javax.swing.JPanel();
        StockDetails_title = new javax.swing.JPanel();
        Add_Inventory_title1 = new javax.swing.JLabel();
        StockDetails_form = new javax.swing.JPanel();
        StockDetails_east = new javax.swing.JPanel();
        StockDetails_west = new javax.swing.JPanel();
        StockDetails_north = new javax.swing.JPanel();
        StockDetails_south = new javax.swing.JPanel();
        StockDetails_center = new javax.swing.JPanel();
        Supplier_Name = new javax.swing.JLabel();
        cbsuppliername_initial = new javax.swing.JComboBox<>();
        Quantity = new javax.swing.JLabel();
        txtquantity_initial = new javax.swing.JTextField();
        Date_Received = new javax.swing.JLabel();
        txtdatereceived_initial = new javax.swing.JTextField();
        Expiry_Date = new javax.swing.JLabel();
        txtexpirydate_initial = new javax.swing.JTextField();
        HasnoExpiry = new javax.swing.JPanel();
        chbnoexpiry_initial = new javax.swing.JCheckBox();
        Initial_south = new javax.swing.JPanel();
        btnadd_initial = new javax.swing.JButton();
        btncancel_initial = new javax.swing.JButton();
        Initial_west = new javax.swing.JPanel();
        Initial_east = new javax.swing.JPanel();
        Add_Restock = new javax.swing.JPanel();
        Restock_north = new javax.swing.JPanel();
        Restock_center = new javax.swing.JPanel();
        Restock_left = new javax.swing.JPanel();
        RestockDetails_title = new javax.swing.JPanel();
        Add_Inventory_title2 = new javax.swing.JLabel();
        RestockDetails_form = new javax.swing.JPanel();
        RestockDetails_west = new javax.swing.JPanel();
        RestockDetails_east = new javax.swing.JPanel();
        RestockDetails_center = new javax.swing.JPanel();
        lblproductname_restock1 = new javax.swing.JLabel();
        lblrestocktype_show = new javax.swing.JLabel();
        lblproductname_restock = new javax.swing.JLabel();
        cbproductname_restock = new javax.swing.JComboBox<>();
        Brand1 = new javax.swing.JLabel();
        cbsuppliername_restock = new javax.swing.JComboBox<>();
        Category1 = new javax.swing.JLabel();
        txtquantity_restock = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtdatereceived_restock = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtexpirationdate_restock = new javax.swing.JTextField();
        NoExpiry_restock = new javax.swing.JPanel();
        chbnoexpiry_restock = new javax.swing.JCheckBox();
        RestockDetails_north = new javax.swing.JPanel();
        RestockDetails_south = new javax.swing.JPanel();
        Restock_south = new javax.swing.JPanel();
        btnsave_restock = new javax.swing.JButton();
        btncancel_restock = new javax.swing.JButton();
        Restock_west = new javax.swing.JPanel();
        Restock_east = new javax.swing.JPanel();
        Less_AdjustStock = new javax.swing.JPanel();
        Adjust_north = new javax.swing.JPanel();
        Adjust_center = new javax.swing.JPanel();
        Adjust_left = new javax.swing.JPanel();
        AdjustDetails_title = new javax.swing.JPanel();
        Add_Inventory_title4 = new javax.swing.JLabel();
        AdjustDetails_form = new javax.swing.JPanel();
        AdjustDetails_west = new javax.swing.JPanel();
        AdjustDetails_east = new javax.swing.JPanel();
        AdjustDetails_center = new javax.swing.JPanel();
        AdjustmentType_adjust = new javax.swing.JLabel();
        cbadjustmenttype_adjust = new javax.swing.JComboBox<>();
        Quantity_toAdjust = new javax.swing.JLabel();
        txtadjustquantity = new javax.swing.JTextField();
        Reason_adjust = new javax.swing.JLabel();
        cbreason_adjust = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        ProductDetails_north2 = new javax.swing.JPanel();
        ProductDetails_south2 = new javax.swing.JPanel();
        Adjust_right = new javax.swing.JPanel();
        StockDetails_title2 = new javax.swing.JPanel();
        Add_Inventory_title5 = new javax.swing.JLabel();
        StockDetails_form2 = new javax.swing.JPanel();
        StockDetails_east2 = new javax.swing.JPanel();
        StockDetails_west2 = new javax.swing.JPanel();
        StockDetails_north2 = new javax.swing.JPanel();
        StockDetails_south2 = new javax.swing.JPanel();
        StockDetails_center2 = new javax.swing.JPanel();
        ProductId_Adjust = new javax.swing.JLabel();
        lblproductIdAdjust_show = new javax.swing.JLabel();
        ProductName_Adjust = new javax.swing.JLabel();
        lblproductnameAdjust_show = new javax.swing.JLabel();
        BatchId_Adjust = new javax.swing.JLabel();
        lblbatchIdAdjust_show = new javax.swing.JLabel();
        CurrentQuantity_Adjust = new javax.swing.JLabel();
        lblcurrentquantityAdjust_show = new javax.swing.JLabel();
        Adjust_south = new javax.swing.JPanel();
        btnconfirm_adjustment = new javax.swing.JButton();
        btncancel_adjustment = new javax.swing.JButton();
        Adjust_west = new javax.swing.JPanel();
        Adjust_east = new javax.swing.JPanel();
        Sales = new javax.swing.JPanel();
        Sales_header = new javax.swing.JPanel();
        Sales_center = new javax.swing.JPanel();
        SalesActions = new javax.swing.JPanel();
        Sale_and_Transactions_title = new javax.swing.JPanel();
        lblsalesandtransactions = new javax.swing.JLabel();
        Sales_content = new javax.swing.JPanel();
        Total_sales = new javax.swing.JPanel();
        lbltotal_sales = new javax.swing.JLabel();
        lbltotalsales_show = new javax.swing.JLabel();
        Transactions = new javax.swing.JPanel();
        lbltransactions = new javax.swing.JLabel();
        lbltransactions_show = new javax.swing.JLabel();
        Voided = new javax.swing.JPanel();
        lblvoided = new javax.swing.JLabel();
        lblvoided_show = new javax.swing.JLabel();
        SalesInventory = new javax.swing.JPanel();
        Sales_Separator = new javax.swing.JPanel();
        SaleInventory_form = new javax.swing.JPanel();
        SaleScrollPane = new javax.swing.JScrollPane();
        SalelistPanel = new javax.swing.JPanel();
        SaleContent = new javax.swing.JPanel();
        ViewSaleCard = new javax.swing.JPanel();
        SaleSearch = new javax.swing.JPanel();
        txtsearch_transactionid = new javax.swing.JTextField();
        btnsearch_transactionid = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        SaleInfo = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbsales_admin = new javax.swing.JTable();
        SaleButtons = new javax.swing.JPanel();
        btnviewsalesdetails_admin = new javax.swing.JButton();
        ViewSalesDetailsCard = new javax.swing.JPanel();
        SelectedDetails3 = new javax.swing.JPanel();
        StockDetails_title5 = new javax.swing.JPanel();
        Add_Inventory_title8 = new javax.swing.JLabel();
        StockDetails_form5 = new javax.swing.JPanel();
        StockDetails_north5 = new javax.swing.JPanel();
        StockDetails_center5 = new javax.swing.JPanel();
        Saledetails_north = new javax.swing.JPanel();
        StockDetails_center3 = new javax.swing.JPanel();
        SalesDetails_left = new javax.swing.JPanel();
        SelectedTransactioID = new javax.swing.JLabel();
        lblselectedsalesID_show_admin = new javax.swing.JLabel();
        TransactionDate_transactiondetails = new javax.swing.JLabel();
        lbltransactiondatesalesDetails_show_admin = new javax.swing.JLabel();
        Cashier_transactiondetails = new javax.swing.JLabel();
        lblcashiersalesDetails_show_admin = new javax.swing.JLabel();
        SalesDetails_right = new javax.swing.JPanel();
        lblnewtotalstock_show1 = new javax.swing.JLabel();
        StockDetails_center6 = new javax.swing.JPanel();
        Supplier_Name2 = new javax.swing.JLabel();
        lblcurrentstock_show1 = new javax.swing.JLabel();
        PaymentMethod_transactiondetails = new javax.swing.JLabel();
        lblpaymentmethodTransactionDetails_show_admin = new javax.swing.JLabel();
        Status_transactiondetails = new javax.swing.JLabel();
        lblstatusTransactionDetails_show_admin = new javax.swing.JLabel();
        Restock_space2 = new javax.swing.JPanel();
        lblnewtotalstock_show_admin = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        ItemsSold_panel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        ItemsSold_center = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbsaledetails = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        Total_transactiondetails = new javax.swing.JPanel();
        Total_SaleDetails = new javax.swing.JLabel();
        lbltotal_salesdetails = new javax.swing.JLabel();
        SaleSummary_buttons2 = new javax.swing.JPanel();
        btnback_saledetails = new javax.swing.JButton();
        Sales_south = new javax.swing.JPanel();
        Sales_east = new javax.swing.JPanel();
        Sales_west = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1024, 768));

        Main_Panel.setLayout(new java.awt.BorderLayout());

        Body.setPreferredSize(new java.awt.Dimension(1024, 768));
        Body.setLayout(new java.awt.BorderLayout());

        Navigation_Panel.setPreferredSize(new java.awt.Dimension(80, 768));
        Navigation_Panel.setLayout(new java.awt.BorderLayout());

        Nav_north.setBackground(new java.awt.Color(255, 244, 230));
        Nav_north.setPreferredSize(new java.awt.Dimension(80, 150));
        Nav_north.setLayout(new java.awt.BorderLayout());

        Adminicon_north.setBackground(new java.awt.Color(255, 244, 230));
        Adminicon_north.setPreferredSize(new java.awt.Dimension(80, 10));

        javax.swing.GroupLayout Adminicon_northLayout = new javax.swing.GroupLayout(Adminicon_north);
        Adminicon_north.setLayout(Adminicon_northLayout);
        Adminicon_northLayout.setHorizontalGroup(
            Adminicon_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Adminicon_northLayout.setVerticalGroup(
            Adminicon_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Nav_north.add(Adminicon_north, java.awt.BorderLayout.NORTH);

        Adminicon_south.setBackground(new java.awt.Color(255, 244, 230));
        Adminicon_south.setPreferredSize(new java.awt.Dimension(80, 40));

        javax.swing.GroupLayout Adminicon_southLayout = new javax.swing.GroupLayout(Adminicon_south);
        Adminicon_south.setLayout(Adminicon_southLayout);
        Adminicon_southLayout.setHorizontalGroup(
            Adminicon_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );
        Adminicon_southLayout.setVerticalGroup(
            Adminicon_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        Nav_north.add(Adminicon_south, java.awt.BorderLayout.SOUTH);
        Nav_north.add(jLabel1, java.awt.BorderLayout.CENTER);

        Navigation_Panel.add(Nav_north, java.awt.BorderLayout.NORTH);

        Nav_center.setPreferredSize(new java.awt.Dimension(80, 100));
        Nav_center.setLayout(new java.awt.GridLayout(3, 1));

        btnhome.setBackground(new java.awt.Color(255, 244, 230));
        btnhome.setIcon(new javax.swing.ImageIcon("C:\\Users\\user\\OneDrive\\Desktop\\Netbeans Programs\\Java Projects\\002-PetSupplies_Ledger_and_Tracker\\src\\main\\resources\\images\\home(resized).png")); // NOI18N
        btnhome.addActionListener(this::btnhomeActionPerformed);
        Nav_center.add(btnhome);

        btninventory.setBackground(new java.awt.Color(255, 244, 230));
        btninventory.setIcon(new javax.swing.ImageIcon("C:\\Users\\user\\OneDrive\\Desktop\\Netbeans Programs\\Java Projects\\002-PetSupplies_Ledger_and_Tracker\\src\\main\\resources\\images\\inventory3(resized)v1.png")); // NOI18N
        btninventory.addActionListener(this::btninventoryActionPerformed);
        Nav_center.add(btninventory);

        btnsales.setBackground(new java.awt.Color(255, 244, 230));
        btnsales.setIcon(new javax.swing.ImageIcon("C:\\Users\\user\\OneDrive\\Desktop\\Netbeans Programs\\Java Projects\\002-PetSupplies_Ledger_and_Tracker\\src\\main\\resources\\images\\transactions2.png")); // NOI18N
        btnsales.addActionListener(this::btnsalesActionPerformed);
        Nav_center.add(btnsales);

        Navigation_Panel.add(Nav_center, java.awt.BorderLayout.CENTER);

        Nav_south.setPreferredSize(new java.awt.Dimension(80, 150));
        Nav_south.setLayout(new java.awt.BorderLayout());

        Logout_north.setBackground(new java.awt.Color(255, 244, 230));
        Logout_north.setPreferredSize(new java.awt.Dimension(80, 46));

        javax.swing.GroupLayout Logout_northLayout = new javax.swing.GroupLayout(Logout_north);
        Logout_north.setLayout(Logout_northLayout);
        Logout_northLayout.setHorizontalGroup(
            Logout_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Logout_northLayout.setVerticalGroup(
            Logout_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Nav_south.add(Logout_north, java.awt.BorderLayout.NORTH);

        Logout_south.setBackground(new java.awt.Color(255, 244, 230));
        Logout_south.setPreferredSize(new java.awt.Dimension(80, 20));

        javax.swing.GroupLayout Logout_southLayout = new javax.swing.GroupLayout(Logout_south);
        Logout_south.setLayout(Logout_southLayout);
        Logout_southLayout.setHorizontalGroup(
            Logout_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );
        Logout_southLayout.setVerticalGroup(
            Logout_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        Nav_south.add(Logout_south, java.awt.BorderLayout.SOUTH);

        btnlogout.setBackground(new java.awt.Color(255, 244, 230));
        btnlogout.setIcon(new javax.swing.ImageIcon("C:\\Users\\user\\OneDrive\\Desktop\\Netbeans Programs\\Java Projects\\002-PetSupplies_Ledger_and_Tracker\\src\\main\\resources\\images\\logout(resized).png")); // NOI18N
        btnlogout.setPreferredSize(new java.awt.Dimension(80, 74));
        btnlogout.addActionListener(this::btnlogoutActionPerformed);
        Nav_south.add(btnlogout, java.awt.BorderLayout.CENTER);

        Navigation_Panel.add(Nav_south, java.awt.BorderLayout.SOUTH);

        Body.add(Navigation_Panel, java.awt.BorderLayout.WEST);

        MainScroll.setAlignmentX(0.0F);
        MainScroll.setAlignmentY(0.0F);
        MainScroll.setPreferredSize(new java.awt.Dimension(1024, 768));

        Content_Panel.setPreferredSize(new java.awt.Dimension(944, 768));
        Content_Panel.setLayout(new java.awt.CardLayout());

        Home.setAlignmentX(0.0F);
        Home.setAlignmentY(0.0F);
        Home.setLayout(new javax.swing.OverlayLayout(Home));

        Home_tab.setAlignmentX(0.0F);
        Home_tab.setAlignmentY(0.0F);
        Home_tab.setPreferredSize(new java.awt.Dimension(944, 768));
        Home_tab.setLayout(new java.awt.BorderLayout());

        Home_east.setPreferredSize(new java.awt.Dimension(15, 638));

        javax.swing.GroupLayout Home_eastLayout = new javax.swing.GroupLayout(Home_east);
        Home_east.setLayout(Home_eastLayout);
        Home_eastLayout.setHorizontalGroup(
            Home_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        Home_eastLayout.setVerticalGroup(
            Home_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 673, Short.MAX_VALUE)
        );

        Home_tab.add(Home_east, java.awt.BorderLayout.LINE_END);

        Home_west.setPreferredSize(new java.awt.Dimension(15, 638));

        javax.swing.GroupLayout Home_westLayout = new javax.swing.GroupLayout(Home_west);
        Home_west.setLayout(Home_westLayout);
        Home_westLayout.setHorizontalGroup(
            Home_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        Home_westLayout.setVerticalGroup(
            Home_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 673, Short.MAX_VALUE)
        );

        Home_tab.add(Home_west, java.awt.BorderLayout.WEST);

        Home_south.setPreferredSize(new java.awt.Dimension(944, 15));

        javax.swing.GroupLayout Home_southLayout = new javax.swing.GroupLayout(Home_south);
        Home_south.setLayout(Home_southLayout);
        Home_southLayout.setHorizontalGroup(
            Home_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 944, Short.MAX_VALUE)
        );
        Home_southLayout.setVerticalGroup(
            Home_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        Home_tab.add(Home_south, java.awt.BorderLayout.PAGE_END);

        Home_center.setLayout(new java.awt.BorderLayout());

        Product_Summary.setPreferredSize(new java.awt.Dimension(914, 300));
        Product_Summary.setLayout(new java.awt.GridLayout(2, 1, 0, 15));

        Summary_north.setLayout(new java.awt.GridLayout(1, 2, 15, 0));

        Total_Products.setBackground(new java.awt.Color(204, 255, 204));
        Total_Products.setLayout(new java.awt.GridLayout(3, 1));

        Total_Products_title.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        Total_Products_title.setText("Sales This Week");
        Total_Products_title.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Total_Products.add(Total_Products_title);

        lblweeklysales_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lblweeklysales_show.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Total_Products.add(lblweeklysales_show);

        jLabel12.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        jLabel12.setText("Completed sales since Monday");
        jLabel12.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Total_Products.add(jLabel12);

        Summary_north.add(Total_Products);

        Total_Stock.setBackground(new java.awt.Color(255, 255, 153));
        Total_Stock.setLayout(new java.awt.GridLayout(3, 1));

        jLabel11.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel11.setText("Sales This Month");
        jLabel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Total_Stock.add(jLabel11);

        lblsalesthismonth_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lblsalesthismonth_show.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Total_Stock.add(lblsalesthismonth_show);

        jLabel15.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        jLabel15.setText("Completed sales this month");
        jLabel15.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Total_Stock.add(jLabel15);

        Summary_north.add(Total_Stock);

        Product_Summary.add(Summary_north);

        Summary_south.setLayout(new java.awt.GridLayout(1, 2, 15, 0));

        Stock_level.setBackground(new java.awt.Color(255, 204, 204));
        Stock_level.setLayout(new java.awt.GridLayout(3, 1));

        jLabel16.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel16.setText("Completed Transactions");
        jLabel16.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Stock_level.add(jLabel16);

        lblcompletedtransactions_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lblcompletedtransactions_show.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Stock_level.add(lblcompletedtransactions_show);

        jLabel18.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        jLabel18.setText("Transactions this month");
        jLabel18.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Stock_level.add(jLabel18);

        Summary_south.add(Stock_level);

        Earliest_Expiration.setBackground(new java.awt.Color(153, 255, 255));
        Earliest_Expiration.setLayout(new java.awt.GridLayout(3, 1));

        jLabel19.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel19.setText("Voided This Month");
        jLabel19.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Earliest_Expiration.add(jLabel19);

        lblvoidedthismonth_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lblvoidedthismonth_show.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Earliest_Expiration.add(lblvoidedthismonth_show);

        jLabel21.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        jLabel21.setText("Voided transactions this month");
        jLabel21.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Earliest_Expiration.add(jLabel21);

        Summary_south.add(Earliest_Expiration);

        Product_Summary.add(Summary_south);

        Home_center.add(Product_Summary, java.awt.BorderLayout.NORTH);

        Home_tab.add(Home_center, java.awt.BorderLayout.CENTER);

        Home_north.setPreferredSize(new java.awt.Dimension(944, 80));

        javax.swing.GroupLayout Home_northLayout = new javax.swing.GroupLayout(Home_north);
        Home_north.setLayout(Home_northLayout);
        Home_northLayout.setHorizontalGroup(
            Home_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 944, Short.MAX_VALUE)
        );
        Home_northLayout.setVerticalGroup(
            Home_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );

        Home_tab.add(Home_north, java.awt.BorderLayout.NORTH);

        Home.add(Home_tab);

        bghome.setIcon(new javax.swing.ImageIcon("C:\\Users\\user\\OneDrive\\Desktop\\Netbeans Programs\\Java Projects\\002-PetSupplies_Ledger_and_Tracker\\src\\main\\resources\\images\\bg-oversize.png")); // NOI18N
        bghome.setAlignmentY(0.0F);
        Home.add(bghome);

        Content_Panel.add(Home, "Display_home");

        Inventory.setBackground(new java.awt.Color(255, 255, 255));
        Inventory.setPreferredSize(new java.awt.Dimension(944, 768));
        Inventory.setLayout(new java.awt.BorderLayout());

        Inventrory_header.setBackground(new java.awt.Color(255, 255, 255));
        Inventrory_header.setPreferredSize(new java.awt.Dimension(944, 80));
        Inventrory_header.setLayout(new java.awt.GridLayout(1, 0));

        Inventory_Actions_title.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        Inventory_Actions_title.setText("Inventory Actions");
        Inventory_Actions_title.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 1, 1));
        Inventrory_header.add(Inventory_Actions_title);

        Inventory.add(Inventrory_header, java.awt.BorderLayout.NORTH);

        Inventory_center.setBackground(new java.awt.Color(255, 255, 255));
        Inventory_center.setLayout(new java.awt.BorderLayout());

        InventoryActions.setBackground(new java.awt.Color(255, 255, 255));
        InventoryActions.setPreferredSize(new java.awt.Dimension(914, 100));
        InventoryActions.setLayout(new java.awt.GridLayout(1, 1));

        AddNewProduct.setBackground(new java.awt.Color(204, 204, 255));
        AddNewProduct.setLayout(new java.awt.BorderLayout());

        btnaddnewproduct.setBackground(new java.awt.Color(204, 255, 204));
        btnaddnewproduct.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 24)); // NOI18N
        btnaddnewproduct.setText("Add New Product");
        btnaddnewproduct.setBorderPainted(false);
        btnaddnewproduct.setFocusPainted(false);
        btnaddnewproduct.setFocusable(false);
        btnaddnewproduct.addActionListener(this::btnaddnewproductActionPerformed);
        AddNewProduct.add(btnaddnewproduct, java.awt.BorderLayout.CENTER);

        jPanel15.setBackground(new java.awt.Color(204, 255, 204));
        jPanel15.setPreferredSize(new java.awt.Dimension(100, 100));
        jPanel15.setLayout(new java.awt.GridLayout(1, 0));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/box-add(resized).png"))); // NOI18N
        jPanel15.add(jLabel2);

        AddNewProduct.add(jPanel15, java.awt.BorderLayout.WEST);

        InventoryActions.add(AddNewProduct);

        Inventory_center.add(InventoryActions, java.awt.BorderLayout.NORTH);

        ProductInventory.setBackground(new java.awt.Color(204, 204, 204));
        ProductInventory.setLayout(new java.awt.BorderLayout());

        Product_Inventory_title.setBackground(new java.awt.Color(255, 255, 255));
        Product_Inventory_title.setPreferredSize(new java.awt.Dimension(914, 80));
        Product_Inventory_title.setLayout(new java.awt.GridLayout(1, 0));

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel8.setText("Product Inventory");
        jLabel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        Product_Inventory_title.add(jLabel8);

        ProductInventory.add(Product_Inventory_title, java.awt.BorderLayout.NORTH);

        ProductInventory_form.setLayout(new java.awt.BorderLayout());

        InventorySearch_Content.setLayout(new java.awt.CardLayout());

        InventorySearchCard.setBackground(new java.awt.Color(245, 218, 83));
        InventorySearchCard.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        InventorySearchCard.setPreferredSize(new java.awt.Dimension(914, 50));
        InventorySearchCard.setLayout(new java.awt.BorderLayout());

        txtsearch_inventory.setEditable(false);
        txtsearch_inventory.setBackground(new java.awt.Color(245, 218, 83));
        txtsearch_inventory.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        txtsearch_inventory.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        InventorySearchCard.add(txtsearch_inventory, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(245, 218, 83));
        jPanel2.setPreferredSize(new java.awt.Dimension(200, 23));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );

        InventorySearchCard.add(jPanel2, java.awt.BorderLayout.EAST);

        InventorySearch_Content.add(InventorySearchCard, "Display_searchinventory");

        BatchSearchCard.setBackground(new java.awt.Color(245, 218, 83));
        BatchSearchCard.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        BatchSearchCard.setPreferredSize(new java.awt.Dimension(914, 50));
        BatchSearchCard.setLayout(new java.awt.BorderLayout());

        txtsearch_inventory1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        txtsearch_inventory1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        BatchSearchCard.add(txtsearch_inventory1, java.awt.BorderLayout.CENTER);

        jLabel14.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Search Batches");
        jLabel14.setPreferredSize(new java.awt.Dimension(280, 16));
        BatchSearchCard.add(jLabel14, java.awt.BorderLayout.WEST);

        jPanel3.setBackground(new java.awt.Color(245, 218, 83));
        jPanel3.setPreferredSize(new java.awt.Dimension(200, 23));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );

        BatchSearchCard.add(jPanel3, java.awt.BorderLayout.EAST);

        InventorySearch_Content.add(BatchSearchCard, "Display_searchbatches");

        ProductInventory_form.add(InventorySearch_Content, java.awt.BorderLayout.NORTH);

        ProductScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ProductScrollPane.setViewportView(Productlist);

        Productlist.setLayout(new javax.swing.BoxLayout(Productlist, javax.swing.BoxLayout.Y_AXIS));

        ProductContent.setLayout(new java.awt.CardLayout());

        ProductCard.setBackground(new java.awt.Color(255, 255, 255));
        ProductCard.setLayout(new java.awt.BorderLayout());

        ProductInfo.setBackground(new java.awt.Color(244, 243, 243));
        ProductInfo.setLayout(new javax.swing.BoxLayout(ProductInfo, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(452, 200));

        tbinventory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Name", "Brand", "Category", "Total Stock", "Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbinventory.setRowHeight(50);
        tbinventory.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbinventory);
        if (tbinventory.getColumnModel().getColumnCount() > 0) {
            tbinventory.getColumnModel().getColumn(0).setResizable(false);
            tbinventory.getColumnModel().getColumn(1).setResizable(false);
            tbinventory.getColumnModel().getColumn(2).setResizable(false);
            tbinventory.getColumnModel().getColumn(3).setResizable(false);
            tbinventory.getColumnModel().getColumn(4).setResizable(false);
            tbinventory.getColumnModel().getColumn(5).setResizable(false);
        }

        ProductInfo.add(jScrollPane1);

        ProductCard.add(ProductInfo, java.awt.BorderLayout.CENTER);

        ProductButtons.setBackground(new java.awt.Color(244, 243, 243));
        ProductButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        btnviewbatches.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        btnviewbatches.setText("View Batches");
        btnviewbatches.setFocusPainted(false);
        btnviewbatches.setFocusable(false);
        btnviewbatches.setPreferredSize(new java.awt.Dimension(150, 30));
        btnviewbatches.addActionListener(this::btnviewbatchesActionPerformed);
        ProductButtons.add(btnviewbatches);

        ProductCard.add(ProductButtons, java.awt.BorderLayout.SOUTH);

        ProductContent.add(ProductCard, "Display_productcard");

        BatchCard.setLayout(new java.awt.BorderLayout());

        BatchInfo.setBackground(new java.awt.Color(244, 243, 243));
        BatchInfo.setLayout(new java.awt.BorderLayout());

        Batchinfo_north.setBackground(new java.awt.Color(255, 255, 255));
        Batchinfo_north.setPreferredSize(new java.awt.Dimension(100, 50));
        Batchinfo_north.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 13, 13));

        Batch_ID.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        Batch_ID.setText("Batch ID:");
        Batchinfo_north.add(Batch_ID);

        lblbatchidBatch_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        Batchinfo_north.add(lblbatchidBatch_show);

        BatchInfo.add(Batchinfo_north, java.awt.BorderLayout.NORTH);

        Batchinfo_center.setBackground(new java.awt.Color(204, 255, 204));
        Batchinfo_center.setLayout(new java.awt.BorderLayout());

        BatchinfoInner_north.setBackground(new java.awt.Color(255, 255, 255));
        BatchinfoInner_north.setPreferredSize(new java.awt.Dimension(912, 50));
        BatchinfoInner_north.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 13, 13));

        Status.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        Status.setText("Status:");
        BatchinfoInner_north.add(Status);

        lblstatusBatch_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        BatchinfoInner_north.add(lblstatusBatch_show);

        Batchinfo_center.add(BatchinfoInner_north, java.awt.BorderLayout.NORTH);

        BatchScroll.setPreferredSize(new java.awt.Dimension(452, 100));

        tbbatches.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Supplier", "Quantity", "Expiration Date", "Received Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        BatchScroll.setViewportView(tbbatches);
        if (tbbatches.getColumnModel().getColumnCount() > 0) {
            tbbatches.getColumnModel().getColumn(0).setResizable(false);
            tbbatches.getColumnModel().getColumn(1).setResizable(false);
            tbbatches.getColumnModel().getColumn(2).setResizable(false);
            tbbatches.getColumnModel().getColumn(3).setResizable(false);
        }

        Batchinfo_center.add(BatchScroll, java.awt.BorderLayout.CENTER);

        BatchInfo.add(Batchinfo_center, java.awt.BorderLayout.CENTER);

        BatchCard.add(BatchInfo, java.awt.BorderLayout.CENTER);

        BatchButtons.setBackground(new java.awt.Color(244, 243, 243));
        BatchButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        btnstockinexistingsupplier_batches.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        btnstockinexistingsupplier_batches.setText("Restock Existing Suppliers");
        btnstockinexistingsupplier_batches.setFocusPainted(false);
        btnstockinexistingsupplier_batches.setFocusable(false);
        btnstockinexistingsupplier_batches.setPreferredSize(new java.awt.Dimension(200, 30));
        btnstockinexistingsupplier_batches.addActionListener(this::btnstockinexistingsupplier_batchesActionPerformed);
        BatchButtons.add(btnstockinexistingsupplier_batches);

        btnstockinnewsupplier_batches.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        btnstockinnewsupplier_batches.setText("Restock New Supplier");
        btnstockinnewsupplier_batches.setFocusPainted(false);
        btnstockinnewsupplier_batches.setFocusable(false);
        btnstockinnewsupplier_batches.setPreferredSize(new java.awt.Dimension(200, 30));
        btnstockinnewsupplier_batches.addActionListener(this::btnstockinnewsupplier_batchesActionPerformed);
        BatchButtons.add(btnstockinnewsupplier_batches);

        btnadjust_batches.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        btnadjust_batches.setText("Adjust Stock");
        btnadjust_batches.setFocusPainted(false);
        btnadjust_batches.setFocusable(false);
        btnadjust_batches.setPreferredSize(new java.awt.Dimension(150, 30));
        btnadjust_batches.addActionListener(this::btnadjust_batchesActionPerformed);
        BatchButtons.add(btnadjust_batches);

        btnback_batches.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        btnback_batches.setText("Back");
        btnback_batches.setFocusPainted(false);
        btnback_batches.setFocusable(false);
        btnback_batches.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnback_batches.setPreferredSize(new java.awt.Dimension(72, 30));
        btnback_batches.addActionListener(this::btnback_batchesActionPerformed);
        BatchButtons.add(btnback_batches);

        BatchCard.add(BatchButtons, java.awt.BorderLayout.SOUTH);

        ProductContent.add(BatchCard, "Display_batchcard");

        Productlist.add(ProductContent);

        ProductScrollPane.setViewportView(Productlist);

        ProductInventory_form.add(ProductScrollPane, java.awt.BorderLayout.CENTER);

        ProductInventory.add(ProductInventory_form, java.awt.BorderLayout.CENTER);

        Inventory_center.add(ProductInventory, java.awt.BorderLayout.CENTER);

        Inventory.add(Inventory_center, java.awt.BorderLayout.CENTER);

        Inventory_south.setBackground(new java.awt.Color(255, 255, 255));
        Inventory_south.setPreferredSize(new java.awt.Dimension(944, 100));

        javax.swing.GroupLayout Inventory_southLayout = new javax.swing.GroupLayout(Inventory_south);
        Inventory_south.setLayout(Inventory_southLayout);
        Inventory_southLayout.setHorizontalGroup(
            Inventory_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 944, Short.MAX_VALUE)
        );
        Inventory_southLayout.setVerticalGroup(
            Inventory_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        Inventory.add(Inventory_south, java.awt.BorderLayout.SOUTH);

        Inventory_east.setBackground(new java.awt.Color(255, 255, 255));
        Inventory_east.setPreferredSize(new java.awt.Dimension(15, 638));

        javax.swing.GroupLayout Inventory_eastLayout = new javax.swing.GroupLayout(Inventory_east);
        Inventory_east.setLayout(Inventory_eastLayout);
        Inventory_eastLayout.setHorizontalGroup(
            Inventory_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        Inventory_eastLayout.setVerticalGroup(
            Inventory_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );

        Inventory.add(Inventory_east, java.awt.BorderLayout.LINE_END);

        Inventory_west.setBackground(new java.awt.Color(255, 255, 255));
        Inventory_west.setPreferredSize(new java.awt.Dimension(15, 638));

        javax.swing.GroupLayout Inventory_westLayout = new javax.swing.GroupLayout(Inventory_west);
        Inventory_west.setLayout(Inventory_westLayout);
        Inventory_westLayout.setHorizontalGroup(
            Inventory_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        Inventory_westLayout.setVerticalGroup(
            Inventory_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );

        Inventory.add(Inventory_west, java.awt.BorderLayout.LINE_START);

        Content_Panel.add(Inventory, "Display_inventory");

        Add_InitialStock.setLayout(new java.awt.BorderLayout());

        Initial_north.setBackground(new java.awt.Color(244, 243, 243));
        Initial_north.setPreferredSize(new java.awt.Dimension(944, 30));

        javax.swing.GroupLayout Initial_northLayout = new javax.swing.GroupLayout(Initial_north);
        Initial_north.setLayout(Initial_northLayout);
        Initial_northLayout.setHorizontalGroup(
            Initial_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 944, Short.MAX_VALUE)
        );
        Initial_northLayout.setVerticalGroup(
            Initial_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        Add_InitialStock.add(Initial_north, java.awt.BorderLayout.NORTH);

        Initial_center.setBackground(new java.awt.Color(244, 243, 243));
        Initial_center.setLayout(new java.awt.GridLayout(2, 1, 0, 30));

        Initial_left.setBackground(new java.awt.Color(255, 255, 255));
        Initial_left.setPreferredSize(new java.awt.Dimension(452, 688));
        Initial_left.setLayout(new java.awt.BorderLayout());

        ProductDetails_title.setBackground(new java.awt.Color(245, 218, 83));
        ProductDetails_title.setPreferredSize(new java.awt.Dimension(452, 50));
        ProductDetails_title.setLayout(new java.awt.GridLayout(1, 0));

        Add_Inventory_title.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        Add_Inventory_title.setText("Product Details");
        Add_Inventory_title.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        ProductDetails_title.add(Add_Inventory_title);

        Initial_left.add(ProductDetails_title, java.awt.BorderLayout.NORTH);

        ProductDetails_form.setLayout(new java.awt.BorderLayout());

        ProductDetails_west.setBackground(new java.awt.Color(255, 255, 255));
        ProductDetails_west.setPreferredSize(new java.awt.Dimension(200, 279));

        javax.swing.GroupLayout ProductDetails_westLayout = new javax.swing.GroupLayout(ProductDetails_west);
        ProductDetails_west.setLayout(ProductDetails_westLayout);
        ProductDetails_westLayout.setHorizontalGroup(
            ProductDetails_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        ProductDetails_westLayout.setVerticalGroup(
            ProductDetails_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 224, Short.MAX_VALUE)
        );

        ProductDetails_form.add(ProductDetails_west, java.awt.BorderLayout.WEST);

        ProductDetails_east.setBackground(new java.awt.Color(255, 255, 255));
        ProductDetails_east.setPreferredSize(new java.awt.Dimension(200, 638));

        javax.swing.GroupLayout ProductDetails_eastLayout = new javax.swing.GroupLayout(ProductDetails_east);
        ProductDetails_east.setLayout(ProductDetails_eastLayout);
        ProductDetails_eastLayout.setHorizontalGroup(
            ProductDetails_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        ProductDetails_eastLayout.setVerticalGroup(
            ProductDetails_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 224, Short.MAX_VALUE)
        );

        ProductDetails_form.add(ProductDetails_east, java.awt.BorderLayout.EAST);

        ProductDetails_center.setBackground(new java.awt.Color(255, 255, 255));
        ProductDetails_center.setLayout(new java.awt.GridLayout(10, 1));

        Product_Name.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Product_Name.setText("Product Name");
        Product_Name.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        ProductDetails_center.add(Product_Name);

        txtproductname_initial.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        ProductDetails_center.add(txtproductname_initial);

        Brand.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Brand.setText("Brand");
        Brand.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        ProductDetails_center.add(Brand);

        txtbrand_initial.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        ProductDetails_center.add(txtbrand_initial);

        Category.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Category.setText("Category");
        Category.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        ProductDetails_center.add(Category);

        cbcategory_initial.setEditable(true);
        cbcategory_initial.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        cbcategory_initial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Food", "Medicine", "Accessories", "Grooming", "Toys", "Other" }));
        ProductDetails_center.add(cbcategory_initial);

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabel3.setText("Unit");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        ProductDetails_center.add(jLabel3);

        cbunit_initial.setEditable(true);
        cbunit_initial.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        cbunit_initial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "kg", "mg", "litre", "pack" }));
        ProductDetails_center.add(cbunit_initial);

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabel4.setText("Unit Price(₱)");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        ProductDetails_center.add(jLabel4);

        txtunitprice_initial.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        ProductDetails_center.add(txtunitprice_initial);

        ProductDetails_form.add(ProductDetails_center, java.awt.BorderLayout.CENTER);

        ProductDetails_north.setBackground(new java.awt.Color(255, 255, 255));
        ProductDetails_north.setPreferredSize(new java.awt.Dimension(904, 20));

        javax.swing.GroupLayout ProductDetails_northLayout = new javax.swing.GroupLayout(ProductDetails_north);
        ProductDetails_north.setLayout(ProductDetails_northLayout);
        ProductDetails_northLayout.setHorizontalGroup(
            ProductDetails_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 904, Short.MAX_VALUE)
        );
        ProductDetails_northLayout.setVerticalGroup(
            ProductDetails_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        ProductDetails_form.add(ProductDetails_north, java.awt.BorderLayout.NORTH);

        ProductDetails_south.setBackground(new java.awt.Color(255, 255, 255));
        ProductDetails_south.setPreferredSize(new java.awt.Dimension(904, 20));

        javax.swing.GroupLayout ProductDetails_southLayout = new javax.swing.GroupLayout(ProductDetails_south);
        ProductDetails_south.setLayout(ProductDetails_southLayout);
        ProductDetails_southLayout.setHorizontalGroup(
            ProductDetails_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 904, Short.MAX_VALUE)
        );
        ProductDetails_southLayout.setVerticalGroup(
            ProductDetails_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        ProductDetails_form.add(ProductDetails_south, java.awt.BorderLayout.SOUTH);

        Initial_left.add(ProductDetails_form, java.awt.BorderLayout.CENTER);

        Initial_center.add(Initial_left);

        Initial_right.setBackground(new java.awt.Color(255, 255, 255));
        Initial_right.setPreferredSize(new java.awt.Dimension(452, 688));
        Initial_right.setLayout(new java.awt.BorderLayout());

        StockDetails_title.setBackground(new java.awt.Color(245, 218, 83));
        StockDetails_title.setPreferredSize(new java.awt.Dimension(452, 50));
        StockDetails_title.setLayout(new java.awt.BorderLayout());

        Add_Inventory_title1.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        Add_Inventory_title1.setText("Stock Details");
        Add_Inventory_title1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        StockDetails_title.add(Add_Inventory_title1, java.awt.BorderLayout.CENTER);

        Initial_right.add(StockDetails_title, java.awt.BorderLayout.NORTH);

        StockDetails_form.setLayout(new java.awt.BorderLayout());

        StockDetails_east.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_east.setPreferredSize(new java.awt.Dimension(200, 638));

        javax.swing.GroupLayout StockDetails_eastLayout = new javax.swing.GroupLayout(StockDetails_east);
        StockDetails_east.setLayout(StockDetails_eastLayout);
        StockDetails_eastLayout.setHorizontalGroup(
            StockDetails_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        StockDetails_eastLayout.setVerticalGroup(
            StockDetails_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 224, Short.MAX_VALUE)
        );

        StockDetails_form.add(StockDetails_east, java.awt.BorderLayout.EAST);

        StockDetails_west.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_west.setPreferredSize(new java.awt.Dimension(200, 279));

        javax.swing.GroupLayout StockDetails_westLayout = new javax.swing.GroupLayout(StockDetails_west);
        StockDetails_west.setLayout(StockDetails_westLayout);
        StockDetails_westLayout.setHorizontalGroup(
            StockDetails_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        StockDetails_westLayout.setVerticalGroup(
            StockDetails_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 224, Short.MAX_VALUE)
        );

        StockDetails_form.add(StockDetails_west, java.awt.BorderLayout.WEST);

        StockDetails_north.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_north.setPreferredSize(new java.awt.Dimension(904, 20));

        javax.swing.GroupLayout StockDetails_northLayout = new javax.swing.GroupLayout(StockDetails_north);
        StockDetails_north.setLayout(StockDetails_northLayout);
        StockDetails_northLayout.setHorizontalGroup(
            StockDetails_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 904, Short.MAX_VALUE)
        );
        StockDetails_northLayout.setVerticalGroup(
            StockDetails_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        StockDetails_form.add(StockDetails_north, java.awt.BorderLayout.NORTH);

        StockDetails_south.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_south.setPreferredSize(new java.awt.Dimension(904, 20));

        javax.swing.GroupLayout StockDetails_southLayout = new javax.swing.GroupLayout(StockDetails_south);
        StockDetails_south.setLayout(StockDetails_southLayout);
        StockDetails_southLayout.setHorizontalGroup(
            StockDetails_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 904, Short.MAX_VALUE)
        );
        StockDetails_southLayout.setVerticalGroup(
            StockDetails_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        StockDetails_form.add(StockDetails_south, java.awt.BorderLayout.SOUTH);

        StockDetails_center.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_center.setLayout(new java.awt.GridLayout(10, 1));

        Supplier_Name.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Supplier_Name.setText("Supplier Name");
        Supplier_Name.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center.add(Supplier_Name);

        cbsuppliername_initial.setEditable(true);
        cbsuppliername_initial.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        cbsuppliername_initial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        StockDetails_center.add(cbsuppliername_initial);

        Quantity.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Quantity.setText("Initial Quantity");
        Quantity.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center.add(Quantity);

        txtquantity_initial.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center.add(txtquantity_initial);

        Date_Received.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Date_Received.setText("Date Received");
        Date_Received.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center.add(Date_Received);

        txtdatereceived_initial.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center.add(txtdatereceived_initial);

        Expiry_Date.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Expiry_Date.setText("Expiry Date");
        Expiry_Date.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center.add(Expiry_Date);

        txtexpirydate_initial.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center.add(txtexpirydate_initial);

        HasnoExpiry.setBackground(new java.awt.Color(255, 255, 255));
        HasnoExpiry.setPreferredSize(new java.awt.Dimension(163, 20));
        HasnoExpiry.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        chbnoexpiry_initial.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        chbnoexpiry_initial.setText("No Expiry");
        chbnoexpiry_initial.addActionListener(this::chbnoexpiry_initialActionPerformed);
        HasnoExpiry.add(chbnoexpiry_initial);

        StockDetails_center.add(HasnoExpiry);

        StockDetails_form.add(StockDetails_center, java.awt.BorderLayout.CENTER);

        Initial_right.add(StockDetails_form, java.awt.BorderLayout.CENTER);

        Initial_center.add(Initial_right);

        Add_InitialStock.add(Initial_center, java.awt.BorderLayout.CENTER);

        Initial_south.setBackground(new java.awt.Color(244, 243, 243));
        Initial_south.setPreferredSize(new java.awt.Dimension(944, 80));
        Initial_south.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 20, 10));

        btnadd_initial.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        btnadd_initial.setText("Save Initial Stock");
        btnadd_initial.setPreferredSize(new java.awt.Dimension(200, 50));
        btnadd_initial.addActionListener(this::btnadd_initialActionPerformed);
        Initial_south.add(btnadd_initial);

        btncancel_initial.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        btncancel_initial.setText("Cancel");
        btncancel_initial.setPreferredSize(new java.awt.Dimension(90, 50));
        btncancel_initial.addActionListener(this::btncancel_initialActionPerformed);
        Initial_south.add(btncancel_initial);

        Add_InitialStock.add(Initial_south, java.awt.BorderLayout.SOUTH);

        Initial_west.setBackground(new java.awt.Color(244, 243, 243));
        Initial_west.setPreferredSize(new java.awt.Dimension(20, 668));

        javax.swing.GroupLayout Initial_westLayout = new javax.swing.GroupLayout(Initial_west);
        Initial_west.setLayout(Initial_westLayout);
        Initial_westLayout.setHorizontalGroup(
            Initial_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        Initial_westLayout.setVerticalGroup(
            Initial_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 658, Short.MAX_VALUE)
        );

        Add_InitialStock.add(Initial_west, java.awt.BorderLayout.WEST);

        Initial_east.setBackground(new java.awt.Color(244, 243, 243));
        Initial_east.setPreferredSize(new java.awt.Dimension(20, 668));

        javax.swing.GroupLayout Initial_eastLayout = new javax.swing.GroupLayout(Initial_east);
        Initial_east.setLayout(Initial_eastLayout);
        Initial_eastLayout.setHorizontalGroup(
            Initial_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        Initial_eastLayout.setVerticalGroup(
            Initial_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 658, Short.MAX_VALUE)
        );

        Add_InitialStock.add(Initial_east, java.awt.BorderLayout.EAST);

        Content_Panel.add(Add_InitialStock, "Display_initialstock");

        Add_Restock.setLayout(new java.awt.BorderLayout());

        Restock_north.setBackground(new java.awt.Color(244, 243, 243));
        Restock_north.setPreferredSize(new java.awt.Dimension(944, 100));

        javax.swing.GroupLayout Restock_northLayout = new javax.swing.GroupLayout(Restock_north);
        Restock_north.setLayout(Restock_northLayout);
        Restock_northLayout.setHorizontalGroup(
            Restock_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 944, Short.MAX_VALUE)
        );
        Restock_northLayout.setVerticalGroup(
            Restock_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        Add_Restock.add(Restock_north, java.awt.BorderLayout.NORTH);

        Restock_center.setBackground(new java.awt.Color(244, 243, 243));
        Restock_center.setLayout(new java.awt.GridLayout(1, 1, 0, 30));

        Restock_left.setBackground(new java.awt.Color(255, 255, 255));
        Restock_left.setPreferredSize(new java.awt.Dimension(452, 688));
        Restock_left.setLayout(new java.awt.BorderLayout());

        RestockDetails_title.setBackground(new java.awt.Color(245, 218, 83));
        RestockDetails_title.setPreferredSize(new java.awt.Dimension(452, 50));
        RestockDetails_title.setLayout(new java.awt.GridLayout(1, 0));

        Add_Inventory_title2.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        Add_Inventory_title2.setText("Receive Stock");
        Add_Inventory_title2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        RestockDetails_title.add(Add_Inventory_title2);

        Restock_left.add(RestockDetails_title, java.awt.BorderLayout.NORTH);

        RestockDetails_form.setLayout(new java.awt.BorderLayout());

        RestockDetails_west.setBackground(new java.awt.Color(255, 255, 255));
        RestockDetails_west.setPreferredSize(new java.awt.Dimension(200, 279));

        javax.swing.GroupLayout RestockDetails_westLayout = new javax.swing.GroupLayout(RestockDetails_west);
        RestockDetails_west.setLayout(RestockDetails_westLayout);
        RestockDetails_westLayout.setHorizontalGroup(
            RestockDetails_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        RestockDetails_westLayout.setVerticalGroup(
            RestockDetails_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 428, Short.MAX_VALUE)
        );

        RestockDetails_form.add(RestockDetails_west, java.awt.BorderLayout.WEST);

        RestockDetails_east.setBackground(new java.awt.Color(255, 255, 255));
        RestockDetails_east.setPreferredSize(new java.awt.Dimension(200, 638));

        javax.swing.GroupLayout RestockDetails_eastLayout = new javax.swing.GroupLayout(RestockDetails_east);
        RestockDetails_east.setLayout(RestockDetails_eastLayout);
        RestockDetails_eastLayout.setHorizontalGroup(
            RestockDetails_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        RestockDetails_eastLayout.setVerticalGroup(
            RestockDetails_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 428, Short.MAX_VALUE)
        );

        RestockDetails_form.add(RestockDetails_east, java.awt.BorderLayout.EAST);

        RestockDetails_center.setBackground(new java.awt.Color(255, 255, 255));
        RestockDetails_center.setLayout(new java.awt.GridLayout(13, 1));

        lblproductname_restock1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lblproductname_restock1.setText("Restock Type");
        lblproductname_restock1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        RestockDetails_center.add(lblproductname_restock1);

        lblrestocktype_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        RestockDetails_center.add(lblrestocktype_show);

        lblproductname_restock.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lblproductname_restock.setText("Product Name");
        lblproductname_restock.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        RestockDetails_center.add(lblproductname_restock);

        cbproductname_restock.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        RestockDetails_center.add(cbproductname_restock);

        Brand1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Brand1.setText("Supplier");
        Brand1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        RestockDetails_center.add(Brand1);

        cbsuppliername_restock.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        RestockDetails_center.add(cbsuppliername_restock);

        Category1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Category1.setText("Quantity Received");
        Category1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        RestockDetails_center.add(Category1);

        txtquantity_restock.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        RestockDetails_center.add(txtquantity_restock);

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabel5.setText("Date Received");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        RestockDetails_center.add(jLabel5);

        txtdatereceived_restock.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        RestockDetails_center.add(txtdatereceived_restock);

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabel6.setText("Expiration Date");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        RestockDetails_center.add(jLabel6);

        txtexpirationdate_restock.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        RestockDetails_center.add(txtexpirationdate_restock);

        NoExpiry_restock.setBackground(new java.awt.Color(255, 255, 255));
        NoExpiry_restock.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        chbnoexpiry_restock.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        chbnoexpiry_restock.setText("No Expiry");
        NoExpiry_restock.add(chbnoexpiry_restock);

        RestockDetails_center.add(NoExpiry_restock);

        RestockDetails_form.add(RestockDetails_center, java.awt.BorderLayout.CENTER);

        RestockDetails_north.setBackground(new java.awt.Color(255, 255, 255));
        RestockDetails_north.setPreferredSize(new java.awt.Dimension(904, 20));

        javax.swing.GroupLayout RestockDetails_northLayout = new javax.swing.GroupLayout(RestockDetails_north);
        RestockDetails_north.setLayout(RestockDetails_northLayout);
        RestockDetails_northLayout.setHorizontalGroup(
            RestockDetails_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 904, Short.MAX_VALUE)
        );
        RestockDetails_northLayout.setVerticalGroup(
            RestockDetails_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        RestockDetails_form.add(RestockDetails_north, java.awt.BorderLayout.NORTH);

        RestockDetails_south.setBackground(new java.awt.Color(255, 255, 255));
        RestockDetails_south.setPreferredSize(new java.awt.Dimension(904, 20));

        javax.swing.GroupLayout RestockDetails_southLayout = new javax.swing.GroupLayout(RestockDetails_south);
        RestockDetails_south.setLayout(RestockDetails_southLayout);
        RestockDetails_southLayout.setHorizontalGroup(
            RestockDetails_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 904, Short.MAX_VALUE)
        );
        RestockDetails_southLayout.setVerticalGroup(
            RestockDetails_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        RestockDetails_form.add(RestockDetails_south, java.awt.BorderLayout.SOUTH);

        Restock_left.add(RestockDetails_form, java.awt.BorderLayout.CENTER);

        Restock_center.add(Restock_left);

        Add_Restock.add(Restock_center, java.awt.BorderLayout.CENTER);

        Restock_south.setBackground(new java.awt.Color(244, 243, 243));
        Restock_south.setPreferredSize(new java.awt.Dimension(944, 150));
        Restock_south.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 20, 10));

        btnsave_restock.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        btnsave_restock.setText("Save Restock");
        btnsave_restock.setPreferredSize(new java.awt.Dimension(200, 50));
        btnsave_restock.addActionListener(this::btnsave_restockActionPerformed);
        Restock_south.add(btnsave_restock);

        btncancel_restock.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        btncancel_restock.setText("Cancel");
        btncancel_restock.setPreferredSize(new java.awt.Dimension(90, 50));
        btncancel_restock.addActionListener(this::btncancel_restockActionPerformed);
        Restock_south.add(btncancel_restock);

        Add_Restock.add(Restock_south, java.awt.BorderLayout.SOUTH);

        Restock_west.setBackground(new java.awt.Color(244, 243, 243));
        Restock_west.setPreferredSize(new java.awt.Dimension(20, 668));

        javax.swing.GroupLayout Restock_westLayout = new javax.swing.GroupLayout(Restock_west);
        Restock_west.setLayout(Restock_westLayout);
        Restock_westLayout.setHorizontalGroup(
            Restock_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        Restock_westLayout.setVerticalGroup(
            Restock_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 518, Short.MAX_VALUE)
        );

        Add_Restock.add(Restock_west, java.awt.BorderLayout.WEST);

        Restock_east.setBackground(new java.awt.Color(244, 243, 243));
        Restock_east.setPreferredSize(new java.awt.Dimension(20, 668));

        javax.swing.GroupLayout Restock_eastLayout = new javax.swing.GroupLayout(Restock_east);
        Restock_east.setLayout(Restock_eastLayout);
        Restock_eastLayout.setHorizontalGroup(
            Restock_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        Restock_eastLayout.setVerticalGroup(
            Restock_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 518, Short.MAX_VALUE)
        );

        Add_Restock.add(Restock_east, java.awt.BorderLayout.EAST);

        Content_Panel.add(Add_Restock, "Display_restock");

        Less_AdjustStock.setLayout(new java.awt.BorderLayout());

        Adjust_north.setBackground(new java.awt.Color(244, 243, 243));
        Adjust_north.setPreferredSize(new java.awt.Dimension(944, 30));

        javax.swing.GroupLayout Adjust_northLayout = new javax.swing.GroupLayout(Adjust_north);
        Adjust_north.setLayout(Adjust_northLayout);
        Adjust_northLayout.setHorizontalGroup(
            Adjust_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 944, Short.MAX_VALUE)
        );
        Adjust_northLayout.setVerticalGroup(
            Adjust_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        Less_AdjustStock.add(Adjust_north, java.awt.BorderLayout.NORTH);

        Adjust_center.setBackground(new java.awt.Color(244, 243, 243));
        Adjust_center.setLayout(new java.awt.GridLayout(2, 1, 0, 30));

        Adjust_left.setBackground(new java.awt.Color(255, 255, 255));
        Adjust_left.setPreferredSize(new java.awt.Dimension(452, 688));
        Adjust_left.setLayout(new java.awt.BorderLayout());

        AdjustDetails_title.setBackground(new java.awt.Color(245, 218, 83));
        AdjustDetails_title.setPreferredSize(new java.awt.Dimension(452, 50));
        AdjustDetails_title.setLayout(new java.awt.GridLayout(1, 0));

        Add_Inventory_title4.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        Add_Inventory_title4.setText("Stock Adjustment");
        Add_Inventory_title4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        AdjustDetails_title.add(Add_Inventory_title4);

        Adjust_left.add(AdjustDetails_title, java.awt.BorderLayout.NORTH);

        AdjustDetails_form.setLayout(new java.awt.BorderLayout());

        AdjustDetails_west.setBackground(new java.awt.Color(255, 255, 255));
        AdjustDetails_west.setPreferredSize(new java.awt.Dimension(200, 279));

        javax.swing.GroupLayout AdjustDetails_westLayout = new javax.swing.GroupLayout(AdjustDetails_west);
        AdjustDetails_west.setLayout(AdjustDetails_westLayout);
        AdjustDetails_westLayout.setHorizontalGroup(
            AdjustDetails_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        AdjustDetails_westLayout.setVerticalGroup(
            AdjustDetails_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 224, Short.MAX_VALUE)
        );

        AdjustDetails_form.add(AdjustDetails_west, java.awt.BorderLayout.WEST);

        AdjustDetails_east.setBackground(new java.awt.Color(255, 255, 255));
        AdjustDetails_east.setPreferredSize(new java.awt.Dimension(200, 638));

        javax.swing.GroupLayout AdjustDetails_eastLayout = new javax.swing.GroupLayout(AdjustDetails_east);
        AdjustDetails_east.setLayout(AdjustDetails_eastLayout);
        AdjustDetails_eastLayout.setHorizontalGroup(
            AdjustDetails_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        AdjustDetails_eastLayout.setVerticalGroup(
            AdjustDetails_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 224, Short.MAX_VALUE)
        );

        AdjustDetails_form.add(AdjustDetails_east, java.awt.BorderLayout.EAST);

        AdjustDetails_center.setBackground(new java.awt.Color(255, 255, 255));
        AdjustDetails_center.setLayout(new java.awt.GridLayout(7, 1));

        AdjustmentType_adjust.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        AdjustmentType_adjust.setText("Adjustment Type");
        AdjustmentType_adjust.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        AdjustDetails_center.add(AdjustmentType_adjust);

        cbadjustmenttype_adjust.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        cbadjustmenttype_adjust.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Increase Stock", "Decrease Stock" }));
        AdjustDetails_center.add(cbadjustmenttype_adjust);

        Quantity_toAdjust.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Quantity_toAdjust.setText("Quantity to Adjust");
        Quantity_toAdjust.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        AdjustDetails_center.add(Quantity_toAdjust);

        txtadjustquantity.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        AdjustDetails_center.add(txtadjustquantity);

        Reason_adjust.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Reason_adjust.setText("Reason");
        Reason_adjust.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        AdjustDetails_center.add(Reason_adjust);

        cbreason_adjust.setEditable(true);
        cbreason_adjust.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        AdjustDetails_center.add(cbreason_adjust);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 504, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        AdjustDetails_center.add(jPanel1);

        AdjustDetails_form.add(AdjustDetails_center, java.awt.BorderLayout.CENTER);

        ProductDetails_north2.setBackground(new java.awt.Color(255, 255, 255));
        ProductDetails_north2.setPreferredSize(new java.awt.Dimension(904, 20));

        javax.swing.GroupLayout ProductDetails_north2Layout = new javax.swing.GroupLayout(ProductDetails_north2);
        ProductDetails_north2.setLayout(ProductDetails_north2Layout);
        ProductDetails_north2Layout.setHorizontalGroup(
            ProductDetails_north2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 904, Short.MAX_VALUE)
        );
        ProductDetails_north2Layout.setVerticalGroup(
            ProductDetails_north2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        AdjustDetails_form.add(ProductDetails_north2, java.awt.BorderLayout.NORTH);

        ProductDetails_south2.setBackground(new java.awt.Color(255, 255, 255));
        ProductDetails_south2.setPreferredSize(new java.awt.Dimension(904, 20));

        javax.swing.GroupLayout ProductDetails_south2Layout = new javax.swing.GroupLayout(ProductDetails_south2);
        ProductDetails_south2.setLayout(ProductDetails_south2Layout);
        ProductDetails_south2Layout.setHorizontalGroup(
            ProductDetails_south2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 904, Short.MAX_VALUE)
        );
        ProductDetails_south2Layout.setVerticalGroup(
            ProductDetails_south2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        AdjustDetails_form.add(ProductDetails_south2, java.awt.BorderLayout.SOUTH);

        Adjust_left.add(AdjustDetails_form, java.awt.BorderLayout.CENTER);

        Adjust_center.add(Adjust_left);

        Adjust_right.setBackground(new java.awt.Color(255, 255, 255));
        Adjust_right.setPreferredSize(new java.awt.Dimension(452, 688));
        Adjust_right.setLayout(new java.awt.BorderLayout());

        StockDetails_title2.setBackground(new java.awt.Color(245, 218, 83));
        StockDetails_title2.setPreferredSize(new java.awt.Dimension(452, 50));
        StockDetails_title2.setLayout(new java.awt.BorderLayout());

        Add_Inventory_title5.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        Add_Inventory_title5.setText("Stock Details");
        Add_Inventory_title5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        StockDetails_title2.add(Add_Inventory_title5, java.awt.BorderLayout.CENTER);

        Adjust_right.add(StockDetails_title2, java.awt.BorderLayout.NORTH);

        StockDetails_form2.setLayout(new java.awt.BorderLayout());

        StockDetails_east2.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_east2.setPreferredSize(new java.awt.Dimension(200, 638));

        javax.swing.GroupLayout StockDetails_east2Layout = new javax.swing.GroupLayout(StockDetails_east2);
        StockDetails_east2.setLayout(StockDetails_east2Layout);
        StockDetails_east2Layout.setHorizontalGroup(
            StockDetails_east2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        StockDetails_east2Layout.setVerticalGroup(
            StockDetails_east2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 224, Short.MAX_VALUE)
        );

        StockDetails_form2.add(StockDetails_east2, java.awt.BorderLayout.EAST);

        StockDetails_west2.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_west2.setPreferredSize(new java.awt.Dimension(200, 279));

        javax.swing.GroupLayout StockDetails_west2Layout = new javax.swing.GroupLayout(StockDetails_west2);
        StockDetails_west2.setLayout(StockDetails_west2Layout);
        StockDetails_west2Layout.setHorizontalGroup(
            StockDetails_west2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        StockDetails_west2Layout.setVerticalGroup(
            StockDetails_west2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 224, Short.MAX_VALUE)
        );

        StockDetails_form2.add(StockDetails_west2, java.awt.BorderLayout.WEST);

        StockDetails_north2.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_north2.setPreferredSize(new java.awt.Dimension(904, 20));

        javax.swing.GroupLayout StockDetails_north2Layout = new javax.swing.GroupLayout(StockDetails_north2);
        StockDetails_north2.setLayout(StockDetails_north2Layout);
        StockDetails_north2Layout.setHorizontalGroup(
            StockDetails_north2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 904, Short.MAX_VALUE)
        );
        StockDetails_north2Layout.setVerticalGroup(
            StockDetails_north2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        StockDetails_form2.add(StockDetails_north2, java.awt.BorderLayout.NORTH);

        StockDetails_south2.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_south2.setPreferredSize(new java.awt.Dimension(904, 20));

        javax.swing.GroupLayout StockDetails_south2Layout = new javax.swing.GroupLayout(StockDetails_south2);
        StockDetails_south2.setLayout(StockDetails_south2Layout);
        StockDetails_south2Layout.setHorizontalGroup(
            StockDetails_south2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 904, Short.MAX_VALUE)
        );
        StockDetails_south2Layout.setVerticalGroup(
            StockDetails_south2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        StockDetails_form2.add(StockDetails_south2, java.awt.BorderLayout.SOUTH);

        StockDetails_center2.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_center2.setLayout(new java.awt.GridLayout(9, 1));

        ProductId_Adjust.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        ProductId_Adjust.setText("Product Id");
        ProductId_Adjust.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center2.add(ProductId_Adjust);

        lblproductIdAdjust_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        StockDetails_center2.add(lblproductIdAdjust_show);

        ProductName_Adjust.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        ProductName_Adjust.setText("Product Name");
        ProductName_Adjust.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center2.add(ProductName_Adjust);

        lblproductnameAdjust_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        StockDetails_center2.add(lblproductnameAdjust_show);

        BatchId_Adjust.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        BatchId_Adjust.setText("Batch Id");
        BatchId_Adjust.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center2.add(BatchId_Adjust);

        lblbatchIdAdjust_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        StockDetails_center2.add(lblbatchIdAdjust_show);

        CurrentQuantity_Adjust.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        CurrentQuantity_Adjust.setText("Current Quantity");
        CurrentQuantity_Adjust.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center2.add(CurrentQuantity_Adjust);

        lblcurrentquantityAdjust_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        StockDetails_center2.add(lblcurrentquantityAdjust_show);

        StockDetails_form2.add(StockDetails_center2, java.awt.BorderLayout.CENTER);

        Adjust_right.add(StockDetails_form2, java.awt.BorderLayout.CENTER);

        Adjust_center.add(Adjust_right);

        Less_AdjustStock.add(Adjust_center, java.awt.BorderLayout.CENTER);

        Adjust_south.setBackground(new java.awt.Color(244, 243, 243));
        Adjust_south.setPreferredSize(new java.awt.Dimension(944, 80));
        Adjust_south.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 20, 10));

        btnconfirm_adjustment.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        btnconfirm_adjustment.setText("Confrim Stock Adjustment");
        btnconfirm_adjustment.setPreferredSize(new java.awt.Dimension(200, 50));
        btnconfirm_adjustment.addActionListener(this::btnconfirm_adjustmentActionPerformed);
        Adjust_south.add(btnconfirm_adjustment);

        btncancel_adjustment.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        btncancel_adjustment.setText("Cancel");
        btncancel_adjustment.setPreferredSize(new java.awt.Dimension(90, 50));
        btncancel_adjustment.addActionListener(this::btncancel_adjustmentActionPerformed);
        Adjust_south.add(btncancel_adjustment);

        Less_AdjustStock.add(Adjust_south, java.awt.BorderLayout.SOUTH);

        Adjust_west.setBackground(new java.awt.Color(244, 243, 243));
        Adjust_west.setPreferredSize(new java.awt.Dimension(20, 668));

        javax.swing.GroupLayout Adjust_westLayout = new javax.swing.GroupLayout(Adjust_west);
        Adjust_west.setLayout(Adjust_westLayout);
        Adjust_westLayout.setHorizontalGroup(
            Adjust_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        Adjust_westLayout.setVerticalGroup(
            Adjust_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 658, Short.MAX_VALUE)
        );

        Less_AdjustStock.add(Adjust_west, java.awt.BorderLayout.WEST);

        Adjust_east.setBackground(new java.awt.Color(244, 243, 243));
        Adjust_east.setPreferredSize(new java.awt.Dimension(20, 668));

        javax.swing.GroupLayout Adjust_eastLayout = new javax.swing.GroupLayout(Adjust_east);
        Adjust_east.setLayout(Adjust_eastLayout);
        Adjust_eastLayout.setHorizontalGroup(
            Adjust_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        Adjust_eastLayout.setVerticalGroup(
            Adjust_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 658, Short.MAX_VALUE)
        );

        Less_AdjustStock.add(Adjust_east, java.awt.BorderLayout.EAST);

        Content_Panel.add(Less_AdjustStock, "Display_adjuststock");

        Sales.setBackground(new java.awt.Color(255, 255, 255));
        Sales.setPreferredSize(new java.awt.Dimension(944, 768));
        Sales.setLayout(new java.awt.BorderLayout());

        Sales_header.setBackground(new java.awt.Color(255, 255, 255));
        Sales_header.setPreferredSize(new java.awt.Dimension(944, 50));
        Sales_header.setLayout(new java.awt.GridLayout(1, 0));
        Sales.add(Sales_header, java.awt.BorderLayout.NORTH);

        Sales_center.setBackground(new java.awt.Color(255, 255, 255));
        Sales_center.setLayout(new java.awt.BorderLayout());

        SalesActions.setBackground(new java.awt.Color(255, 255, 255));
        SalesActions.setPreferredSize(new java.awt.Dimension(914, 300));
        SalesActions.setLayout(new java.awt.GridLayout(3, 1));

        Sale_and_Transactions_title.setBackground(new java.awt.Color(245, 218, 83));
        Sale_and_Transactions_title.setLayout(new java.awt.BorderLayout());

        lblsalesandtransactions.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        lblsalesandtransactions.setText("Sales & Transactions");
        lblsalesandtransactions.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 1, 1));
        Sale_and_Transactions_title.add(lblsalesandtransactions, java.awt.BorderLayout.CENTER);

        SalesActions.add(Sale_and_Transactions_title);

        Sales_content.setBackground(new java.awt.Color(255, 204, 204));
        Sales_content.setLayout(new java.awt.GridLayout(1, 3));

        Total_sales.setBackground(new java.awt.Color(255, 204, 204));
        Total_sales.setLayout(new java.awt.GridLayout(2, 1));

        lbltotal_sales.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lbltotal_sales.setText("Total Sales");
        lbltotal_sales.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Total_sales.add(lbltotal_sales);

        lbltotalsales_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lbltotalsales_show.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Total_sales.add(lbltotalsales_show);

        Sales_content.add(Total_sales);

        Transactions.setBackground(new java.awt.Color(204, 255, 204));
        Transactions.setLayout(new java.awt.GridLayout(2, 1));

        lbltransactions.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lbltransactions.setText("Transactions");
        lbltransactions.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Transactions.add(lbltransactions);

        lbltransactions_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lbltransactions_show.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Transactions.add(lbltransactions_show);

        Sales_content.add(Transactions);

        Voided.setBackground(new java.awt.Color(204, 255, 255));
        Voided.setLayout(new java.awt.GridLayout(2, 1));

        lblvoided.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lblvoided.setText("Voided");
        lblvoided.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Voided.add(lblvoided);

        lblvoided_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lblvoided_show.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        Voided.add(lblvoided_show);

        Sales_content.add(Voided);

        SalesActions.add(Sales_content);

        Sales_center.add(SalesActions, java.awt.BorderLayout.NORTH);

        SalesInventory.setBackground(new java.awt.Color(204, 204, 204));
        SalesInventory.setLayout(new java.awt.BorderLayout());

        Sales_Separator.setBackground(new java.awt.Color(255, 255, 255));
        Sales_Separator.setPreferredSize(new java.awt.Dimension(914, 50));
        Sales_Separator.setLayout(new java.awt.GridLayout(1, 0));
        SalesInventory.add(Sales_Separator, java.awt.BorderLayout.SOUTH);

        SaleInventory_form.setLayout(new java.awt.BorderLayout());

        SaleScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        SalelistPanel.setBackground(new java.awt.Color(255, 253, 248));
        SalelistPanel.setLayout(new javax.swing.BoxLayout(SalelistPanel, javax.swing.BoxLayout.Y_AXIS));

        SaleContent.setLayout(new java.awt.CardLayout());

        ViewSaleCard.setLayout(new java.awt.BorderLayout());

        SaleSearch.setBackground(new java.awt.Color(245, 218, 83));
        SaleSearch.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        SaleSearch.setPreferredSize(new java.awt.Dimension(914, 50));
        SaleSearch.setLayout(new java.awt.BorderLayout());

        txtsearch_transactionid.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        SaleSearch.add(txtsearch_transactionid, java.awt.BorderLayout.CENTER);

        btnsearch_transactionid.setBackground(new java.awt.Color(245, 218, 83));
        btnsearch_transactionid.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        btnsearch_transactionid.setText("Search");
        btnsearch_transactionid.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnsearch_transactionid.setFocusPainted(false);
        btnsearch_transactionid.setFocusable(false);
        btnsearch_transactionid.setPreferredSize(new java.awt.Dimension(200, 23));
        SaleSearch.add(btnsearch_transactionid, java.awt.BorderLayout.EAST);

        jLabel13.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Search Transaction ID");
        jLabel13.setPreferredSize(new java.awt.Dimension(280, 16));
        SaleSearch.add(jLabel13, java.awt.BorderLayout.WEST);

        ViewSaleCard.add(SaleSearch, java.awt.BorderLayout.NORTH);

        SaleInfo.setLayout(new javax.swing.BoxLayout(SaleInfo, javax.swing.BoxLayout.Y_AXIS));

        tbsales_admin.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        tbsales_admin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Sale ID", "User ID", "Sale Date", "Payment Method", "Total Amount", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbsales_admin.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbsales_admin);
        if (tbsales_admin.getColumnModel().getColumnCount() > 0) {
            tbsales_admin.getColumnModel().getColumn(0).setResizable(false);
            tbsales_admin.getColumnModel().getColumn(1).setResizable(false);
            tbsales_admin.getColumnModel().getColumn(2).setResizable(false);
            tbsales_admin.getColumnModel().getColumn(3).setResizable(false);
            tbsales_admin.getColumnModel().getColumn(4).setResizable(false);
            tbsales_admin.getColumnModel().getColumn(5).setResizable(false);
        }

        SaleInfo.add(jScrollPane2);

        ViewSaleCard.add(SaleInfo, java.awt.BorderLayout.CENTER);

        SaleButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        btnviewsalesdetails_admin.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        btnviewsalesdetails_admin.setText("View Sales");
        btnviewsalesdetails_admin.setFocusPainted(false);
        btnviewsalesdetails_admin.setFocusable(false);
        btnviewsalesdetails_admin.setPreferredSize(new java.awt.Dimension(150, 30));
        btnviewsalesdetails_admin.addActionListener(this::btnviewsalesdetails_adminActionPerformed);
        SaleButtons.add(btnviewsalesdetails_admin);

        ViewSaleCard.add(SaleButtons, java.awt.BorderLayout.SOUTH);

        SaleContent.add(ViewSaleCard, "Display_salecard");

        ViewSalesDetailsCard.setLayout(new java.awt.BorderLayout());

        SelectedDetails3.setBackground(new java.awt.Color(255, 255, 255));
        SelectedDetails3.setPreferredSize(new java.awt.Dimension(452, 688));
        SelectedDetails3.setLayout(new java.awt.BorderLayout());

        StockDetails_title5.setBackground(new java.awt.Color(245, 218, 83));
        StockDetails_title5.setPreferredSize(new java.awt.Dimension(452, 50));
        StockDetails_title5.setLayout(new java.awt.BorderLayout());

        Add_Inventory_title8.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        Add_Inventory_title8.setText("Sales Details");
        Add_Inventory_title8.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        StockDetails_title5.add(Add_Inventory_title8, java.awt.BorderLayout.CENTER);

        SelectedDetails3.add(StockDetails_title5, java.awt.BorderLayout.NORTH);

        StockDetails_form5.setLayout(new java.awt.BorderLayout());

        StockDetails_north5.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_north5.setPreferredSize(new java.awt.Dimension(904, 10));

        javax.swing.GroupLayout StockDetails_north5Layout = new javax.swing.GroupLayout(StockDetails_north5);
        StockDetails_north5.setLayout(StockDetails_north5Layout);
        StockDetails_north5Layout.setHorizontalGroup(
            StockDetails_north5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 914, Short.MAX_VALUE)
        );
        StockDetails_north5Layout.setVerticalGroup(
            StockDetails_north5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        StockDetails_form5.add(StockDetails_north5, java.awt.BorderLayout.NORTH);

        StockDetails_center5.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_center5.setLayout(new javax.swing.BoxLayout(StockDetails_center5, javax.swing.BoxLayout.Y_AXIS));

        Saledetails_north.setBackground(new java.awt.Color(204, 255, 255));
        Saledetails_north.setPreferredSize(new java.awt.Dimension(914, 250));
        Saledetails_north.setLayout(new java.awt.GridLayout(1, 2));

        StockDetails_center3.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_center3.setLayout(new java.awt.BorderLayout());

        SalesDetails_left.setBackground(new java.awt.Color(255, 255, 255));
        SalesDetails_left.setLayout(new java.awt.GridLayout(7, 1, 0, 10));

        SelectedTransactioID.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        SelectedTransactioID.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SelectedTransactioID.setText("Selected Transaction ID");
        SelectedTransactioID.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        SalesDetails_left.add(SelectedTransactioID);

        lblselectedsalesID_show_admin.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lblselectedsalesID_show_admin.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblselectedsalesID_show_admin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        SalesDetails_left.add(lblselectedsalesID_show_admin);

        TransactionDate_transactiondetails.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        TransactionDate_transactiondetails.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TransactionDate_transactiondetails.setText("Transaction Date");
        TransactionDate_transactiondetails.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        SalesDetails_left.add(TransactionDate_transactiondetails);

        lbltransactiondatesalesDetails_show_admin.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lbltransactiondatesalesDetails_show_admin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        SalesDetails_left.add(lbltransactiondatesalesDetails_show_admin);

        Cashier_transactiondetails.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        Cashier_transactiondetails.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Cashier_transactiondetails.setText("Cashier/User ID");
        Cashier_transactiondetails.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        SalesDetails_left.add(Cashier_transactiondetails);

        lblcashiersalesDetails_show_admin.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lblcashiersalesDetails_show_admin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        SalesDetails_left.add(lblcashiersalesDetails_show_admin);

        StockDetails_center3.add(SalesDetails_left, java.awt.BorderLayout.CENTER);

        SalesDetails_right.setBackground(new java.awt.Color(255, 255, 255));
        SalesDetails_right.setLayout(new java.awt.GridLayout(1, 0));

        lblnewtotalstock_show1.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        lblnewtotalstock_show1.setText("Items Sold");
        lblnewtotalstock_show1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        SalesDetails_right.add(lblnewtotalstock_show1);

        StockDetails_center3.add(SalesDetails_right, java.awt.BorderLayout.SOUTH);

        Saledetails_north.add(StockDetails_center3);

        StockDetails_center6.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_center6.setLayout(new java.awt.GridLayout(10, 1, 0, 5));

        Supplier_Name2.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Supplier_Name2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center6.add(Supplier_Name2);

        lblcurrentstock_show1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center6.add(lblcurrentstock_show1);

        PaymentMethod_transactiondetails.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        PaymentMethod_transactiondetails.setText("Payment Method");
        PaymentMethod_transactiondetails.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        StockDetails_center6.add(PaymentMethod_transactiondetails);

        lblpaymentmethodTransactionDetails_show_admin.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center6.add(lblpaymentmethodTransactionDetails_show_admin);

        Status_transactiondetails.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        Status_transactiondetails.setText("Status");
        Status_transactiondetails.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center6.add(Status_transactiondetails);

        lblstatusTransactionDetails_show_admin.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lblstatusTransactionDetails_show_admin.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center6.add(lblstatusTransactionDetails_show_admin);

        Restock_space2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout Restock_space2Layout = new javax.swing.GroupLayout(Restock_space2);
        Restock_space2.setLayout(Restock_space2Layout);
        Restock_space2Layout.setHorizontalGroup(
            Restock_space2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 457, Short.MAX_VALUE)
        );
        Restock_space2Layout.setVerticalGroup(
            Restock_space2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        StockDetails_center6.add(Restock_space2);

        lblnewtotalstock_show_admin.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center6.add(lblnewtotalstock_show_admin);

        Saledetails_north.add(StockDetails_center6);

        StockDetails_center5.add(Saledetails_north);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setPreferredSize(new java.awt.Dimension(537, 10));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 914, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        StockDetails_center5.add(jPanel9);

        ItemsSold_panel.setBackground(new java.awt.Color(255, 255, 255));
        ItemsSold_panel.setPreferredSize(new java.awt.Dimension(536, 100));
        ItemsSold_panel.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(20, 263));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 237, Short.MAX_VALUE)
        );

        ItemsSold_panel.add(jPanel8, java.awt.BorderLayout.WEST);

        ItemsSold_center.setBackground(new java.awt.Color(255, 255, 255));
        ItemsSold_center.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ItemsSold_center.setLayout(new java.awt.GridLayout(1, 3));

        tbsaledetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Name", "Quantity", "Unit Price", "Subtotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbsaledetails.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tbsaledetails);

        ItemsSold_center.add(jScrollPane3);

        ItemsSold_panel.add(ItemsSold_center, java.awt.BorderLayout.CENTER);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setPreferredSize(new java.awt.Dimension(20, 263));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 237, Short.MAX_VALUE)
        );

        ItemsSold_panel.add(jPanel10, java.awt.BorderLayout.EAST);

        StockDetails_center5.add(ItemsSold_panel);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(537, 10));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 914, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        StockDetails_center5.add(jPanel5);

        Total_transactiondetails.setBackground(new java.awt.Color(255, 255, 255));
        Total_transactiondetails.setPreferredSize(new java.awt.Dimension(914, 60));
        Total_transactiondetails.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        Total_SaleDetails.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        Total_SaleDetails.setText("Total:");
        Total_SaleDetails.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 1, 1));
        Total_transactiondetails.add(Total_SaleDetails);

        lbltotal_salesdetails.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        lbltotal_salesdetails.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 1, 1));
        Total_transactiondetails.add(lbltotal_salesdetails);

        StockDetails_center5.add(Total_transactiondetails);

        SaleSummary_buttons2.setBackground(new java.awt.Color(255, 255, 255));
        SaleSummary_buttons2.setPreferredSize(new java.awt.Dimension(533, 60));
        SaleSummary_buttons2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));

        btnback_saledetails.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        btnback_saledetails.setText("Back");
        btnback_saledetails.setPreferredSize(new java.awt.Dimension(250, 50));
        btnback_saledetails.addActionListener(this::btnback_saledetailsActionPerformed);
        SaleSummary_buttons2.add(btnback_saledetails);

        StockDetails_center5.add(SaleSummary_buttons2);

        StockDetails_form5.add(StockDetails_center5, java.awt.BorderLayout.CENTER);

        SelectedDetails3.add(StockDetails_form5, java.awt.BorderLayout.CENTER);

        ViewSalesDetailsCard.add(SelectedDetails3, java.awt.BorderLayout.CENTER);

        SaleContent.add(ViewSalesDetailsCard, "Display_salesdetails");

        SalelistPanel.add(SaleContent);

        SaleScrollPane.setViewportView(SalelistPanel);

        SaleInventory_form.add(SaleScrollPane, java.awt.BorderLayout.CENTER);

        SalesInventory.add(SaleInventory_form, java.awt.BorderLayout.CENTER);

        Sales_center.add(SalesInventory, java.awt.BorderLayout.CENTER);

        Sales.add(Sales_center, java.awt.BorderLayout.CENTER);

        Sales_south.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout Sales_southLayout = new javax.swing.GroupLayout(Sales_south);
        Sales_south.setLayout(Sales_southLayout);
        Sales_southLayout.setHorizontalGroup(
            Sales_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 944, Short.MAX_VALUE)
        );
        Sales_southLayout.setVerticalGroup(
            Sales_southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        Sales.add(Sales_south, java.awt.BorderLayout.PAGE_END);

        Sales_east.setBackground(new java.awt.Color(255, 255, 255));
        Sales_east.setPreferredSize(new java.awt.Dimension(15, 638));

        javax.swing.GroupLayout Sales_eastLayout = new javax.swing.GroupLayout(Sales_east);
        Sales_east.setLayout(Sales_eastLayout);
        Sales_eastLayout.setHorizontalGroup(
            Sales_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        Sales_eastLayout.setVerticalGroup(
            Sales_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 703, Short.MAX_VALUE)
        );

        Sales.add(Sales_east, java.awt.BorderLayout.LINE_END);

        Sales_west.setBackground(new java.awt.Color(255, 255, 255));
        Sales_west.setPreferredSize(new java.awt.Dimension(15, 638));

        javax.swing.GroupLayout Sales_westLayout = new javax.swing.GroupLayout(Sales_west);
        Sales_west.setLayout(Sales_westLayout);
        Sales_westLayout.setHorizontalGroup(
            Sales_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        Sales_westLayout.setVerticalGroup(
            Sales_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 703, Short.MAX_VALUE)
        );

        Sales.add(Sales_west, java.awt.BorderLayout.LINE_START);

        Content_Panel.add(Sales, "Display_sales");

        MainScroll.setViewportView(Content_Panel);

        Body.add(MainScroll, java.awt.BorderLayout.CENTER);

        Main_Panel.add(Body, java.awt.BorderLayout.CENTER);

        getContentPane().add(Main_Panel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnhomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhomeActionPerformed
        loadSalesPerformanceDashboard();
        
        parentCardLayout.show(Content_Panel, "Display_home");
    }//GEN-LAST:event_btnhomeActionPerformed

    private void btninventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btninventoryActionPerformed
        parentCardLayout.show(Content_Panel, "Display_inventory");
        
        setSelectedButton(btninventory);
    }//GEN-LAST:event_btninventoryActionPerformed

    private void btnsalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalesActionPerformed
        loadSalesHistory("");
        loadSalesDashboardSummary();

        // Keep your existing navigation code here.
        parentCardLayout.show(Content_Panel, "Display_saleshistory");
        
        parentCardLayout.show(Content_Panel, "Display_sales");
    }//GEN-LAST:event_btnsalesActionPerformed

    private void btnlogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlogoutActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Do you want to Logout?", "Logging out", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION)
        {
            MainFrame login = new MainFrame();
            login.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnlogoutActionPerformed

    private void btnaddnewproductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddnewproductActionPerformed
        parentCardLayout.show(Content_Panel, "Display_initialstock");
    }//GEN-LAST:event_btnaddnewproductActionPerformed

    private void btnadd_initialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnadd_initialActionPerformed
        
       if (txtproductname_initial.getText().isBlank())
        {
            JOptionPane.showMessageDialog(this, "Product Name is Missing!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (txtbrand_initial.getText().isBlank())
        {
            JOptionPane.showMessageDialog(this, "Brand is Missing!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (txtunitprice_initial.getText().isBlank())
        {
            JOptionPane.showMessageDialog(this, "Price is Missing!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (!txtunitprice_initial.getText().matches("^\\d+(\\.\\d+)?$"))
        {
            JOptionPane.showMessageDialog(this, "Price must contain Numbers Only!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (cbsuppliername_initial.getSelectedItem().toString().isBlank())
        {
            JOptionPane.showMessageDialog(this, "Supplier is Missing!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (txtquantity_initial.getText().isBlank())
        {
            JOptionPane.showMessageDialog(this, "Quantity is Missing!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (!txtquantity_initial.getText().trim().matches("^\\d+(\\.\\d+)?$"))
        {
            JOptionPane.showMessageDialog(this, "Quantity must contain Numbers Only!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (txtdatereceived_initial.getText().isBlank())
        {
            JOptionPane.showMessageDialog(this, "Date received is Missing!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (!txtdatereceived_initial.getText().trim().matches("\\d{2}-\\d{2}-\\d{4}"))
        {
            JOptionPane.showMessageDialog(this, "Invalid Received Date Format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            double price = Double.parseDouble(txtunitprice_initial.getText());
            double quantity = Double.parseDouble(txtquantity_initial.getText());
            
            if (price <= 0)
            {
                JOptionPane.showMessageDialog(this, "Price cannot be Negative or Zero!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if (quantity <= 0) 
            {
                JOptionPane.showMessageDialog(this, "Quantity cannot be Negative or Zero!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            
            String receivedDateText = txtdatereceived_initial.getText().trim();
            String expirationText = null;
            
            //Check if Expiraton was checked
            if (!chbnoexpiry_initial.isSelected())
            {
                if (txtexpirydate_initial.getText().trim().isEmpty())
                {
                    JOptionPane.showMessageDialog(this, "Expiration Date is Missing!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if (!txtexpirydate_initial.getText().trim().matches("\\d{2}-\\d{2}-\\d{4}"))
                {
                    JOptionPane.showMessageDialog(this, "Invalid Expiration Date Format!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                expirationText = txtexpirydate_initial.getText().trim();
            }
            
            
            try
            {
                String productName = txtproductname_initial.getText().trim();
                String brand = txtbrand_initial.getText().trim();
                String category = cbcategory_initial.getSelectedItem().toString();
                String unit = cbunit_initial.getSelectedItem().toString();
                String supplierName = cbsuppliername_initial.getSelectedItem().toString().trim();
                
                
                DateTimeFormatter strictFormatter = DateTimeFormatter.ofPattern("MM-dd-uuuu").withResolverStyle(java.time.format.ResolverStyle.STRICT);

                LocalDate receivedDate = LocalDate.parse(receivedDateText, strictFormatter);

                if (receivedDate.isAfter(LocalDate.now())) 
                {
                    JOptionPane.showMessageDialog(this, "Received date cannot be in the future!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (expirationText != null) 
                {
                    LocalDate expirationDate = LocalDate.parse(expirationText, strictFormatter);

                    if (expirationDate.isBefore(LocalDate.now())) 
                    {
                        JOptionPane.showMessageDialog(this, "Expiration date has already passed!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (expirationDate.isBefore(receivedDate)) 
                    {
                        JOptionPane.showMessageDialog(this, "Expiration date cannot be before the received date!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                
                //Check if product already Exists
                int existingProductId = managestock.findExistingProduct(productName, brand, category, unit);

                int productId;
                
                if (existingProductId != -1)
                {
                    productId = existingProductId;
                    
                    JOptionPane.showMessageDialog(this, "Product already Exists. A new Batch will be Added", "Duplicate Product", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    productId = managestock.addStock(productName, brand, category, unit, price);
                    
                    if (productId == -1)
                    {
                        JOptionPane.showMessageDialog(this, "No ID given to this entry!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                //Get Supplier
                int supplierId = managesupplier.getORaddSupplier(supplierName);
                    
                    
                //Create Batch using the correct Product ID
                managebatch.addBatch(productId, supplierId, quantity, receivedDateText, expirationText);
                
                //Remember the Product and Supplier
                stockPrefs.putInt("lastProductId", productId);
                stockPrefs.putInt("lastSupplierId", supplierId);
                
                // Remember category and unit
                rememberComboBoxInput(cbcategory_initial);
                rememberComboBoxInput(cbunit_initial);

                
                txtproductname_initial.setText("");
                txtbrand_initial.setText("");
                txtunitprice_initial.setText("");
                txtquantity_initial.setText("");
                txtdatereceived_initial.setText("");
                txtexpirydate_initial.setText("");
                
                chbnoexpiry_initial.setSelected(false);
                
                refreshStockTable();


                //Return to Product Information
                parentCardLayout.show(Content_Panel, "Display_inventory");
                
                JOptionPane.showMessageDialog(this, "Product Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);         
                
            }
            catch (SQLException e)
            {
                JOptionPane.showMessageDialog(this, "Database Error!" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(this, "Please enter valid Numbers!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch (java.time.format.DateTimeParseException e)
            {
                JOptionPane.showMessageDialog(this, "Please use MM-dd-yyyy Format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnadd_initialActionPerformed

    private void btncancel_initialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancel_initialActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to Cancel this Entry?", "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION)
        {
            txtproductname_initial.setText("");
            txtbrand_initial.setText("");
            txtunitprice_initial.setText("");
            cbcategory_initial.setSelectedIndex(0);
            cbunit_initial.setSelectedIndex(0);
            txtunitprice_initial.setText("");
            
            cbsuppliername_initial.setSelectedIndex(0);
            txtquantity_initial.setText("");
            txtdatereceived_initial.setText("");
            txtexpirydate_initial.setText("");

            txtdatereceived_initial.setText(LocalDate.now().format(formatter));
            
            
            parentCardLayout.show(Content_Panel, "Display_inventory");
            
            JOptionPane.showMessageDialog(this,"Canceled Successfully", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btncancel_initialActionPerformed

    private void btncancel_restockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancel_restockActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to Cancel this Entry?", "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION)
        {
            cbproductname_restock.setSelectedIndex(0);
            cbsuppliername_restock.setSelectedIndex(0);
            txtquantity_restock.setText("");
            txtdatereceived_restock.setText("");
            txtexpirationdate_restock.setText("");

            txtdatereceived_restock.setText(LocalDate.now().format(formatter));
            
            
            parentCardLayout.show(Content_Panel, "Display_inventory");

            setSelectedButton(btncancel_restock);
            
            JOptionPane.showMessageDialog(this,"Canceled Successfully", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btncancel_restockActionPerformed

    private void btnconfirm_adjustmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnconfirm_adjustmentActionPerformed
        
        // 1. Get the selected adjustment type
        Object typeValue = cbadjustmenttype_adjust.getSelectedItem();

        if (typeValue == null) 
        {
            JOptionPane.showMessageDialog(this, "Please select an adjustment type.");
            return;
        }

        String AdjustmentType = typeValue.toString().trim();

        try 
        {
            // 3. Get the adjustment quantity
            double quantity = Double.parseDouble(txtadjustquantity.getText().trim());

            if (quantity <= 0) 
            {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.");
                return;
            }

            // 4. Get the reason
            Object reasonValue = cbreason_adjust.getEditor().getItem();

            String reason = reasonValue == null ? "" : reasonValue.toString().trim();

            if (reason.isEmpty() || reason.equalsIgnoreCase("Select Reason")) 
            {

                JOptionPane.showMessageDialog(this, "Please select or enter an adjustment reason.", "Missing Reason", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 5. Confirm the selected operation
            int choice = JOptionPane.showConfirmDialog(this, AdjustmentType + " by " + quantity + " units for Batch ID " + selectedAdjustmentBatchId + "?", "Confirm Stock Adjustment", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (choice != JOptionPane.YES_OPTION) 
            {
                return;
            }

            // 6. Decide which operation to perform
            if (AdjustmentType.equals("Increase Stock")) 
            {

                System.out.println("User ID passed to adjustment: " + UserId);
                
                managebatch.adjustBatchStock(selectedAdjustmentBatchId, quantity, "Increase Stock", UserId, reason);

            } else if (AdjustmentType.equals("Decrease Stock")) 
            {

                System.out.println("User ID passed to adjustment: " + UserId);
                
                managebatch.adjustBatchStock(selectedAdjustmentBatchId, quantity, "Decrease Stock", UserId, reason);
            } 
            else 
            {
                JOptionPane.showMessageDialog(this, "Adjustment Type not Found: " + AdjustmentType, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 7. Refresh the stock details and batch display
            loadStockAdjustmentDetails(selectedAdjustmentBatchId);

            refreshBatchTable(selectedAdjustmentProductId);

            // 8. Clear the quantity field
            txtadjustquantity.setText("");
            
            
            //Return to Batches Table
            productContentCardLayout.show(ProductContent, "Display_batchcard");
            parentCardLayout.show(Content_Panel, "Display_inventory");
            inventorySearchCardLayout.show(InventorySearch_Content, "Display_searchinventory");
            

            JOptionPane.showMessageDialog(this, "Stock adjustment completed successfully!", "Success",JOptionPane.INFORMATION_MESSAGE);

        } 
        catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity.", "Invalid Quantity", JOptionPane.WARNING_MESSAGE);

        } catch (SQLException | IllegalArgumentException e) 
        {
            JOptionPane.showMessageDialog(this, "Unable to adjust stock: " + e.getMessage(), "Stock Adjustment Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btnconfirm_adjustmentActionPerformed

    private void btncancel_adjustmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancel_adjustmentActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this entry?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) 
        {

            // Reset Adjustment Type
            if (cbadjustmenttype_adjust.getItemCount() > 0) 
            {
                cbadjustmenttype_adjust.setSelectedIndex(0);
            }

            // Clear the editable Reason combo box
            cbreason_adjust.setSelectedItem(null);

            if (cbreason_adjust.isEditable())
            {
                cbreason_adjust.getEditor().setItem("");
            }

            // Clear quantity input
            txtadjustquantity.setText("");

            // Clear Stock Details labels
            lblproductIdAdjust_show.setText("");
            lblproductnameAdjust_show.setText("");
            lblbatchIdAdjust_show.setText("");
            lblcurrentquantityAdjust_show.setText("");

            // Reset selected batch IDs
            selectedAdjustmentBatchId = -1;
            selectedAdjustmentProductId = -1;

            // Return to Inventory, then display Batches
            parentCardLayout.show(Content_Panel,"Display_inventory");

            productContentCardLayout.show(ProductContent,"Display_batchcard");

            JOptionPane.showMessageDialog(this, "Stock adjustment cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btncancel_adjustmentActionPerformed

    private void btnviewsalesdetails_adminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnviewsalesdetails_adminActionPerformed
        int selectedRow = tbsales_admin.getSelectedRow();

        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Select a Row First!", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        showAdminSaleDetails();
        
        
        saleCardLayout.show(SaleContent, "Display_salesdetails");
    }//GEN-LAST:event_btnviewsalesdetails_adminActionPerformed

    private void btnback_batchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnback_batchesActionPerformed
        productContentCardLayout.show(ProductContent, "Display_productcard");

        CardLayout layout = (CardLayout) Content_Panel.getLayout();

        parentCardLayout.show(Content_Panel, "Inventory_Panel");
        inventorySearchCardLayout.show(InventorySearch_Content, "Display_searchinventory");

    }//GEN-LAST:event_btnback_batchesActionPerformed

    private void btnadjust_batchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnadjust_batchesActionPerformed
        
        if (selectedAdjustmentBatchId == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a batch from the table first.", "No Batch Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try 
        {
            // Load the selected batch's details
            loadStockAdjustmentDetails(selectedAdjustmentBatchId);

            // Open Stock Adjustment
            parentCardLayout.show(Content_Panel, "Display_adjuststock");

        } 
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(this, "Unable to load batch details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnadjust_batchesActionPerformed

    private void btnstockinnewsupplier_batchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnstockinnewsupplier_batchesActionPerformed
        int productId = stockPrefs.getInt("lastProductId", -1);

        int supplierId = stockPrefs.getInt("lastSupplierId", -1);

        try 
        {
            lblrestocktype_show.setText("New Supplier");
            
            cbsuppliername_restock.removeAllItems();
            cbsuppliername_restock.setEditable(true);
            cbsuppliername_restock.setSelectedItem("");

            prepareRestockForm();
            
            loadExistingSuppliersForProduct(productId);
            cbproductname_restock.setEnabled(false);
            
            parentCardLayout.show(Content_Panel, "Display_restock");
            
        }
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(this, "Failed to load Existing Suppliers", "Error",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btnstockinnewsupplier_batchesActionPerformed

    private void btnviewbatchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnviewbatchesActionPerformed

       // Check whether a row is selected
        int selectedRow = tbinventory.getSelectedRow();

        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Select a Row First!", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the selected product ID
        int productId = getSelectedProductId();

        if (productId <= 0) 
        {
            JOptionPane.showMessageDialog(this, "Invalid product selected.", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectedBatchProductId = productId;
        stockPrefs.putInt("lastProductId", productId);

        refreshBatchTable(productId);

        productContentCardLayout.show(ProductContent, "Display_batchcard");
        inventorySearchCardLayout.show(InventorySearch_Content, "Display_searchbatches");

        CardLayout layout = (CardLayout) Content_Panel.getLayout();
        layout.show(Content_Panel, "Display_batches");

    }//GEN-LAST:event_btnviewbatchesActionPerformed

    private void btnstockinexistingsupplier_batchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnstockinexistingsupplier_batchesActionPerformed
        int productId = stockPrefs.getInt("lastProductId", -1);

        int supplierId = stockPrefs.getInt("lastSupplierId", -1);

        try 
        {
            lblrestocktype_show.setText("Existing Suppliers");

            prepareRestockForm();
            
            loadExistingSuppliersForProduct(productId);
            cbproductname_restock.setEnabled(false);
            
            parentCardLayout.show(Content_Panel, "Display_restock");
            
        }
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(this, "Failed to load Existing Suppliers", "Error",JOptionPane.ERROR_MESSAGE);
        } 
        
    }//GEN-LAST:event_btnstockinexistingsupplier_batchesActionPerformed

    private void btnsave_restockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsave_restockActionPerformed
    String RestockType = lblrestocktype_show.getText().trim();
        
    
        if (RestockType.equals("New Supplier"))
        {
            int productId = stockPrefs.getInt("lastProductId", -1);

            if (productId == -1) 
            {
                JOptionPane.showMessageDialog(this, "Please select a product first.");
                return;
            }

            try 
            {
                //Get supplier name
                String supplierName = cbsuppliername_restock.getEditor().getItem().toString().trim();

                if (supplierName.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(this,"Please enter a supplier name.");
                    return;
                }

                //Get quantity
                double quantity = Double.parseDouble(txtquantity_restock.getText().trim());

                if (quantity <= 0) 
                {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.");
                    return;
                }

                //Get received date
                String receivedDate = txtdatereceived_restock.getText().trim();

                //Get expiration date
                String expirationDate = null;

                if (!chbnoexpiry_restock.isSelected()) 
                {
                    expirationDate = txtexpirationdate_restock.getText().trim();

                    if (expirationDate.isEmpty()) 
                    {
                        JOptionPane.showMessageDialog(this, "Enter an expiration date or select No Expiry.");
                        return;
                    }
                }

                if (receivedDate.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(this, "Please enter the received date.");
                    return;
                }

                //Find or create the supplier
                int supplierId = managesupplier.getORaddSupplier(supplierName);

                //Create a new batch for the selected product
                managebatch.addBatch(productId, supplierId, quantity, receivedDate, expirationDate);

                refreshBatchTable(productId);
                
                
                //Return to View Batches
                parentCardLayout.show(Content_Panel, "Display_inventory");
                productContentCardLayout.show(ProductContent, "Display_batchcard");
                inventorySearchCardLayout.show(InventorySearch_Content, "Display_searchinventory");
                
                
                //Show success message
                JOptionPane.showMessageDialog(this, "Restock saved successfully!");

            } 
            catch (NumberFormatException e) 
            {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");

            } 
            catch (SQLException | IllegalArgumentException e) 
            {
                JOptionPane.showMessageDialog(this,"Unable to save restock: " + e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (RestockType.equals("Existing Suppliers"))
        {
            try 
            {
                int productId = stockPrefs.getInt("lastProductId", -1);


                Supplier selectedSupplier = (Supplier) cbsuppliername_restock.getSelectedItem();

                if (selectedSupplier == null) 
                {
                    JOptionPane.showMessageDialog(this,"Please select a supplier.","Validation Error",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double quantity = Double.parseDouble(txtquantity_restock.getText().trim());

                if (quantity <= 0) 
                {
                    JOptionPane.showMessageDialog(this,"Quantity must be greater than zero.","Validation Error",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String receivedDate = txtdatereceived_restock.getText().trim();

                String expirationDate = chbnoexpiry_restock.isSelected() ? "" : txtexpirationdate_restock.getText().trim();

                //Save the new batch for the existing supplier
                managebatch.addBatch(productId, selectedSupplier.getSupplierId(), quantity, receivedDate, expirationDate);

                JOptionPane.showMessageDialog(this, "Restock saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                //Return to the inventory section and show batches
                parentCardLayout.show(Content_Panel, "Display_inventory");
                productContentCardLayout.show(ProductContent, "Display_batchcard");

                //Refresh the batch display
                refreshBatchTable(productId);

            } 
            catch (NumberFormatException e) 
            {
                JOptionPane.showMessageDialog(this,"Please enter a valid quantity.","Validation Error",JOptionPane.WARNING_MESSAGE);
            } 
            catch (SQLException | IllegalArgumentException e) 
            {
                JOptionPane.showMessageDialog(this,"Unable to save restock: " + e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this,"Restock Type not Found! " + RestockType, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnsave_restockActionPerformed

    private void btnback_saledetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnback_saledetailsActionPerformed
        loadSalesHistory("");

        parentCardLayout.show(Content_Panel, "Display_sales");
        saleCardLayout.show(SaleContent, "Display_salecard");
        
    }//GEN-LAST:event_btnback_saledetailsActionPerformed

    private void chbnoexpiry_initialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbnoexpiry_initialActionPerformed
        boolean hasnoExpiry = chbnoexpiry_initial.isSelected();
       
       txtexpirydate_initial.setEnabled(hasnoExpiry);
       
       if (hasnoExpiry)
       {
           txtexpirydate_initial.setEnabled(false);
           txtexpirydate_initial.setBackground(new Color(102, 102, 102));
           txtexpirydate_initial.setText("");        
       }
       else
       {
            txtexpirydate_initial.setBackground(Color.WHITE);
            txtexpirydate_initial.setEnabled(true);
       }
    }//GEN-LAST:event_chbnoexpiry_initialActionPerformed
    
    private void setSelectedButton(JButton selectedButton)
    {
        btnhome.setBackground(new Color(255, 244, 230));
        btninventory.setBackground(new Color(255, 244, 230));
        
        btnsales.setBackground(new Color(255, 244, 230));
        
        btncancel_initial.setBackground(Color.WHITE);
        btncancel_restock.setBackground(Color.WHITE);
        btncancel_adjustment.setBackground(Color.WHITE);
     
        
        btnadd_initial.setBackground(Color.WHITE);
        btnviewbatches.setBackground(Color.WHITE);
        btnstockinexistingsupplier_batches.setBackground(Color.WHITE);
        btnstockinnewsupplier_batches.setBackground(Color.WHITE);
        btnadjust_batches.setBackground(Color.WHITE);
        btnconfirm_adjustment.setBackground(Color.WHITE);
        btnback_saledetails.setBackground(Color.WHITE);
        btnviewsalesdetails_admin.setBackground(Color.WHITE);
        btnback_batches.setBackground(Color.WHITE);
        btnconfirm_adjustment.setBackground(Color.WHITE); 
        btnsave_restock.setBackground(Color.WHITE);
        cbcategory_initial.setBackground(Color.WHITE);
        cbadjustmenttype_adjust.setBackground(Color.WHITE);
        cbproductname_restock.setBackground(Color.WHITE);
        cbreason_adjust.setBackground(Color.WHITE);
        cbsuppliername_initial.setBackground(Color.WHITE);
        cbunit_initial.setBackground(Color.WHITE);
        cbsuppliername_restock.setBackground(Color.WHITE);
        chbnoexpiry_initial.setBackground(Color.WHITE);
    }
    
    
    
    
    
    
    private void loadProductCombo(javax.swing.JComboBox combo) 
    {
        combo.removeAllItems();

        for (Stock stock : managestock.getStocks()) 
        {
            combo.addItem(stock);
        }
    }
    
    
    
    private void loadSupplierCombo(javax.swing.JComboBox combo) throws SQLException 
    {
        combo.removeAllItems();

        for (Supplier supplier : managesupplier.getSuppliers()) 
        {
            combo.addItem(supplier);
        }
    }
    
    
    
    private void loadExistingSuppliersForProduct(int productId) throws SQLException 
    {

        cbsuppliername_restock.removeAllItems();

        ArrayList<Supplier> suppliers = managesupplier.getSuppliersByProduct(productId);

        for (Supplier supplier : suppliers) 
        {
            cbsuppliername_restock.addItem(supplier);
        }

        cbsuppliername_restock.setEnabled(!suppliers.isEmpty());

        if (suppliers.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "No existing suppliers were found for this product.", "No Suppliers", JOptionPane.INFORMATION_MESSAGE);
        }
    }
   
   
   
    //Load Sales History
    private void loadSalesHistory(String searchText) 
    {
        DefaultTableModel model = (DefaultTableModel) tbsales_admin.getModel();

        // Clear previous rows
        model.setRowCount(0);

        try 
        {
            // Retrieve saved transactions
            ArrayList<Sale> sales = managesale.getSales();

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a");

            String keyword = searchText == null ? "" : searchText.trim().toLowerCase();

            for (Sale sale : sales) 
            {
                String saleId = String.valueOf(sale.getId());
                String userId = String.valueOf(sale.getUserId());

                String saleDate = sale.getSaleDate() == null ? "" : dateFormat.format(sale.getSaleDate());

                String paymentMethod = String.valueOf(sale.getPaymentMethod());

                String totalAmount = String.format("₱%,.2f", sale.getTotalAmount());

                String status = String.valueOf(sale.getStatus());

                // Include all transaction fields in the search
                String searchableText = (saleId + " " + userId + " " + saleDate + " " + paymentMethod + " " + totalAmount + " " + status).toLowerCase();

                if (keyword.isEmpty() || searchableText.contains(keyword)) 
                {
                    model.addRow(new Object[] {sale.getId(), sale.getUserId(), saleDate, sale.getPaymentMethod(), totalAmount, sale.getStatus()});
                }
            }
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(this, "Unable to load sales history: " + e.getMessage(), "Database Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void loadSalesDashboardSummary() 
    {
        try 
        {
            Object[] stats = managesale.getSalesSummaryStats();

            double totalSales = ((Number) stats[0]).doubleValue();

            int transactionCount = ((Number) stats[1]).intValue();

            int voidedCount = ((Number) stats[2]).intValue();

            // Update Total Sales card
            lbltotalsales_show.setText(String.format("₱%,.2f", totalSales)
            );

            // Update Transactions card
            lbltransactions_show.setText(String.valueOf(transactionCount)
            );

            // Update Voided card
            lblvoided_show.setText(String.valueOf(voidedCount)
            );

        } 
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(this, "Unable to load sales summary: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    private void loadSalesPerformanceDashboard() 
    {
        try 
        {
            Object[] stats = managesale.getSalesPeriodDashboardStats();

            double weeklySales = ((Number) stats[0]).doubleValue();

            double monthlySales = ((Number) stats[1]).doubleValue();

            int completedTransactions = ((Number) stats[2]).intValue();

            int voidedTransactions = ((Number) stats[3]).intValue();

            // Card 1: Sales This Week
            lblweeklysales_show.setText(String.format("₱%,.2f", weeklySales));

            // Card 2: Sales This Month
            lblsalesthismonth_show.setText(String.format("₱%,.2f", monthlySales));

            // Card 3: Completed Transactions
            lblcompletedtransactions_show.setText(String.valueOf(completedTransactions));

            // Card 4: Voided This Month
            lblvoidedthismonth_show.setText(String.valueOf(voidedTransactions));
        } 
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(this, "Unable to load sales dashboard: " + e.getMessage(), "Database Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    private void loadStockAdjustmentDetails(int batchId) throws SQLException 
    {

        Object[] details = managebatch.getBatchAdjustmentDetails(batchId);

        if (details == null) 
        {
            JOptionPane.showMessageDialog(this, "The selected batch could not be found.", "Batch Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Remember the selected batch and product
        selectedAdjustmentProductId = ((Number) details[0]).intValue();

        selectedAdjustmentBatchId = ((Number) details[2]).intValue();

        // Display the selected product and batch details
        lblproductIdAdjust_show.setText(String.valueOf(details[0]));

        lblproductnameAdjust_show.setText(String.valueOf(details[1]));

        lblbatchIdAdjust_show.setText(String.valueOf(details[2]));

        lblcurrentquantityAdjust_show.setText(String.valueOf(details[3]));
    }
    
    
    //For cbreason_adjust
    private void loadAdjustmentReasons() 
    {

        javax.swing.DefaultComboBoxModel<String> model = new javax.swing.DefaultComboBoxModel<>();

        // Default reasons
        model.addElement("Stock Count Correction");
        model.addElement("Damaged Stock");
        model.addElement("Lost Stock");
        model.addElement("Expired Stock");
        model.addElement("Other");

        try 
        {
            // Load previously used reasons from the database
            ArrayList<String> savedReasons = managebatch.getAdjustmentReasons();

            for (String reason : savedReasons) 
            {

                boolean exists = false;

                for (int i = 0; i < model.getSize(); i++) 
                {
                    if (model.getElementAt(i).equalsIgnoreCase(reason)) 
                    {
                        exists = true;
                        break;
                    }
                }

                if (!exists) 
                {
                    model.addElement(reason);
                }
            }

        } 
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(this, "Unable to load adjustment reasons: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        cbreason_adjust.setModel(model);
        cbreason_adjust.setEditable(true);
        cbreason_adjust.setSelectedItem(null);
    }
    
    
    
    
    
    
    
    
    
    //Load Sales History Details
    private void showAdminSaleDetails() 
    {

        // 1. Check the selected transaction
        int selectedRow = tbsales_admin.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Please select a transaction first.",
                "No Transaction Selected",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            // 2. Convert selected row to model row (for sorting)
            int row = tbsales_admin.convertRowIndexToModel(selectedRow);

            DefaultTableModel historyModel =
                (DefaultTableModel) tbsales_admin.getModel();

            // 3. Retrieve transaction information
            int saleId = Integer.parseInt(
                historyModel.getValueAt(row, 0).toString()
            );

            String userId = historyModel.getValueAt(row, 1).toString();
            String saleDate = historyModel.getValueAt(row, 2).toString();
            String paymentMethod = historyModel.getValueAt(row, 3).toString();
            String totalAmount = historyModel.getValueAt(row, 4).toString();
            String status = historyModel.getValueAt(row, 5).toString();

            // 4. Display transaction information
            lblselectedsalesID_show_admin.setText(String.valueOf(saleId));
            lbltransactiondatesalesDetails_show_admin.setText(saleDate);
            lblcashiersalesDetails_show_admin.setText(userId);
            lblpaymentmethodTransactionDetails_show_admin.setText(paymentMethod);
            lblstatusTransactionDetails_show_admin.setText(status);
            lbltotal_salesdetails.setText(totalAmount);

            // 5. Clear ONLY the sales details table
            DefaultTableModel detailsModel =
                (DefaultTableModel) tbsaledetails.getModel();

            detailsModel.setRowCount(0);

            // 6. Retrieve items sold in this transaction
            ArrayList<Object[]> details =
                managesale.getSaleDetails(saleId);

            for (Object[] item : details) {
                detailsModel.addRow(new Object[] {
                    item[0], // Product ID
                    item[1], // Product Name
                    item[2], // Quantity
                    String.format(
                        "₱%,.2f",
                        ((Number) item[3]).doubleValue()
                    ), // Unit Price
                    String.format(
                        "₱%,.2f",
                        ((Number) item[4]).doubleValue()
                    )  // Subtotal
                });
            }

            // 7. Refresh the sales details table
            detailsModel.fireTableDataChanged();
            tbsaledetails.revalidate();
            tbsaledetails.repaint();

            // 8. Show the sales details card
            parentCardLayout.show(Content_Panel, "Display_salesdetails");

            Content_Panel.revalidate();
            Content_Panel.repaint();

        } catch (Exception e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                this,
                "Unable to display sales details: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    
    
    private void selectProductById(javax.swing.JComboBox combo, int productId) 
    {
        
        for (int i = 0; i < combo.getItemCount(); i++) 
        {
            Object selectedItem = combo.getItemAt(i);

            if (selectedItem instanceof ProductOption item) 
            {
                if (item.getId() == productId) 
                {
                   combo.setSelectedIndex(i);
                   return;
                }
            }
        }
    }

    private void selectSupplierById(javax.swing.JComboBox combo, int supplierId) 
    {
        
        for (int i = 0; i < combo.getItemCount(); i++) 
        {
            Object selectedItem = combo.getItemAt(i);

            if (selectedItem instanceof Supplier item) 
            {
                if (item.getSupplierId() == supplierId) 
                {
                    combo.setSelectedIndex(i);
                    return;
                }
            }
        }
    }
    
    
    
    
    
    
    
        
    
    private void prepareRestockForm() 
    {
        try 
        {
            loadProductCombo(cbproductname_restock);
            loadSupplierCombo(cbsuppliername_restock);

            selectProductById(cbproductname_restock, stockPrefs.getInt("lastProductId", -1));

            selectSupplierById(cbsuppliername_restock, stockPrefs.getInt("lastSupplierId", -1));
            
            
            

        } 
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(this, "Failed to load products and suppliers: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void rememberStockSelections(int productId, int supplierId) 
    {
        stockPrefs.putInt("lastProductId", productId);
        stockPrefs.putInt("lastSupplierId", supplierId);
    }

    private int getRememberedProductId() 
    {
        return stockPrefs.getInt("lastProductId", -1);
    }

    private int getRememberedSupplierId() 
    {
        return stockPrefs.getInt("lastSupplierId", -1);
    }
    
    
    //For Stockin Initial
    private void rememberComboBoxInput(javax.swing.JComboBox<String> comboBox) 
    {
        Object selectedItem = comboBox.isEditable()
                ? comboBox.getEditor().getItem()
                : comboBox.getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        String input = selectedItem.toString().trim();

        if (input.isEmpty()) {
            return;
        }

        // Avoid duplicate entries, ignoring capitalization
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).trim()
                    .equalsIgnoreCase(input)) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }

        // Add and select the new entry
        comboBox.addItem(input);
        comboBox.setSelectedItem(input);
    }
    
    
    
    
    
    
    
    private void openBatches(int productId) 
    {
        selectedProductId = productId;
        selectedBatchId = -1;

        refreshBatchTable(productId);
        productContentCardLayout.show(ProductContent, "Display_batchcard");
    }
    
    private void openRestockForm() 
    {
        Batch batch = getSelectedBatch();

        if (batch != null) {
            selectedProductId = batch.getProductId();
            selectedBatchId = batch.getId();

        }

        productContentCardLayout.show(ProductContent, "Restock");
    }
    
    
    private int getSelectedProductId() 
    {

        int row = tbinventory.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Please select a product first.",
                "No Product Selected",
                JOptionPane.WARNING_MESSAGE
            );
            return -1;
        }

        int modelRow = tbinventory.convertRowIndexToModel(row);

        return Integer.parseInt(
            tbinventory.getModel()
                .getValueAt(modelRow, 0).toString()
        );
    }
    
    
    
    
    private Batch getSelectedBatch() 
    {
        int row = tbbatches.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Please select a batch first.",
                "No Batch Selected",
                JOptionPane.WARNING_MESSAGE
            );
            return null;
        }

        int modelRow = tbbatches.convertRowIndexToModel(row);

        if (modelRow < 0 || modelRow >= displayedBatches.size()) {
            return null;
        }

        return displayedBatches.get(modelRow);
    }
    
    
    
    
    
    
    
    
    


        private void refreshStockTable()
    {
        try 
        {
            DefaultTableModel table = (DefaultTableModel) tbinventory.getModel();
        
            table.setRowCount(0);

            for (Object[] row : managestock.getInventoryData())
            {
                Number quantity = (Number) row[4];
                String unit;
                
                if ( row[5] != null)
                {
                    unit = row[5].toString();
                }
                else
                {
                    unit = "N/A";
                }

                DecimalFormat df = new DecimalFormat("#,##0.##");

                String stock = df.format(quantity.doubleValue()) + (unit.isEmpty() ? "" : " " + " | " + unit);
                
                
                table.addRow(new Object[]{
                    row[0], 
                    row[1], 
                    row[2], 
                    row[3], 
                    stock, 
                    "₱" + String.format("%,.2f", ((Number) row[6]).doubleValue()),
                    row[6]});
            }
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(this,"Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
   
    }
    

    
    
    private void refreshBatchTable(int productId)
    {
        DefaultTableModel table = (DefaultTableModel) tbbatches.getModel();

        table.setRowCount(0);
        displayedBatches.clear();

        try
        {
            displayedBatches.addAll(managebatch.getBatches(productId));

            for (Batch batch : displayedBatches)
            {
                table.addRow(new Object[]{batch.getSupplierName(), batch.getQuantity(), batch.getExpirationDate() == null ? "N/A" : batch.getExpirationDate(), batch.getReceivedDate()});
            }

            // Reset labels until a batch is selected
            lblbatchidBatch_show.setText("");
            lblstatusBatch_show.setText("");
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this, "Error loading Batches: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    
    private void customizeButton(javax.swing.JButton button) {
    button.setUI(new javax.swing.plaf.basic.BasicButtonUI());

    button.setBackground(new Color(245, 218, 83));
    button.setForeground(Color.BLACK);

    button.setOpaque(true);
    button.setContentAreaFilled(true);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
}

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
        //java.awt.EventQueue.invokeLater(() -> new AdminFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AddNewProduct;
    private javax.swing.JPanel Add_InitialStock;
    private javax.swing.JLabel Add_Inventory_title;
    private javax.swing.JLabel Add_Inventory_title1;
    private javax.swing.JLabel Add_Inventory_title2;
    private javax.swing.JLabel Add_Inventory_title4;
    private javax.swing.JLabel Add_Inventory_title5;
    private javax.swing.JLabel Add_Inventory_title8;
    private javax.swing.JPanel Add_Restock;
    private javax.swing.JPanel AdjustDetails_center;
    private javax.swing.JPanel AdjustDetails_east;
    private javax.swing.JPanel AdjustDetails_form;
    private javax.swing.JPanel AdjustDetails_title;
    private javax.swing.JPanel AdjustDetails_west;
    private javax.swing.JPanel Adjust_center;
    private javax.swing.JPanel Adjust_east;
    private javax.swing.JPanel Adjust_left;
    private javax.swing.JPanel Adjust_north;
    private javax.swing.JPanel Adjust_right;
    private javax.swing.JPanel Adjust_south;
    private javax.swing.JPanel Adjust_west;
    private javax.swing.JLabel AdjustmentType_adjust;
    private javax.swing.JPanel Adminicon_north;
    private javax.swing.JPanel Adminicon_south;
    private javax.swing.JPanel BatchButtons;
    private javax.swing.JPanel BatchCard;
    private javax.swing.JLabel BatchId_Adjust;
    private javax.swing.JPanel BatchInfo;
    private javax.swing.JScrollPane BatchScroll;
    private javax.swing.JPanel BatchSearchCard;
    private javax.swing.JLabel Batch_ID;
    private javax.swing.JPanel BatchinfoInner_north;
    private javax.swing.JPanel Batchinfo_center;
    private javax.swing.JPanel Batchinfo_north;
    private javax.swing.JPanel Body;
    private javax.swing.JLabel Brand;
    private javax.swing.JLabel Brand1;
    private javax.swing.JLabel Cashier_transactiondetails;
    private javax.swing.JLabel Category;
    private javax.swing.JLabel Category1;
    private javax.swing.JPanel Content_Panel;
    private javax.swing.JLabel CurrentQuantity_Adjust;
    private javax.swing.JLabel Date_Received;
    private javax.swing.JPanel Earliest_Expiration;
    private javax.swing.JLabel Expiry_Date;
    private javax.swing.JPanel HasnoExpiry;
    private javax.swing.JPanel Home;
    private javax.swing.JLayeredPane Home_center;
    private javax.swing.JLayeredPane Home_east;
    private javax.swing.JLayeredPane Home_north;
    private javax.swing.JLayeredPane Home_south;
    private javax.swing.JLayeredPane Home_tab;
    private javax.swing.JLayeredPane Home_west;
    private javax.swing.JPanel Initial_center;
    private javax.swing.JPanel Initial_east;
    private javax.swing.JPanel Initial_left;
    private javax.swing.JPanel Initial_north;
    private javax.swing.JPanel Initial_right;
    private javax.swing.JPanel Initial_south;
    private javax.swing.JPanel Initial_west;
    private javax.swing.JPanel Inventory;
    private javax.swing.JPanel InventoryActions;
    private javax.swing.JPanel InventorySearchCard;
    private javax.swing.JPanel InventorySearch_Content;
    private javax.swing.JLabel Inventory_Actions_title;
    private javax.swing.JPanel Inventory_center;
    private javax.swing.JPanel Inventory_east;
    private javax.swing.JPanel Inventory_south;
    private javax.swing.JPanel Inventory_west;
    private javax.swing.JPanel Inventrory_header;
    private javax.swing.JPanel ItemsSold_center;
    private javax.swing.JPanel ItemsSold_panel;
    private javax.swing.JPanel Less_AdjustStock;
    private javax.swing.JPanel Logout_north;
    private javax.swing.JPanel Logout_south;
    private javax.swing.JScrollPane MainScroll;
    private javax.swing.JPanel Main_Panel;
    private javax.swing.JPanel Nav_center;
    private javax.swing.JPanel Nav_north;
    private javax.swing.JPanel Nav_south;
    private javax.swing.JPanel Navigation_Panel;
    private javax.swing.JPanel NoExpiry_restock;
    private javax.swing.JLabel PaymentMethod_transactiondetails;
    private javax.swing.JPanel ProductButtons;
    private javax.swing.JPanel ProductCard;
    private javax.swing.JPanel ProductContent;
    private javax.swing.JPanel ProductDetails_center;
    private javax.swing.JPanel ProductDetails_east;
    private javax.swing.JPanel ProductDetails_form;
    private javax.swing.JPanel ProductDetails_north;
    private javax.swing.JPanel ProductDetails_north2;
    private javax.swing.JPanel ProductDetails_south;
    private javax.swing.JPanel ProductDetails_south2;
    private javax.swing.JPanel ProductDetails_title;
    private javax.swing.JPanel ProductDetails_west;
    private javax.swing.JLabel ProductId_Adjust;
    private javax.swing.JPanel ProductInfo;
    private javax.swing.JPanel ProductInventory;
    private javax.swing.JPanel ProductInventory_form;
    private javax.swing.JLabel ProductName_Adjust;
    private javax.swing.JScrollPane ProductScrollPane;
    private javax.swing.JPanel Product_Inventory_title;
    private javax.swing.JLabel Product_Name;
    private javax.swing.JLayeredPane Product_Summary;
    private javax.swing.JPanel Productlist;
    private javax.swing.JLabel Quantity;
    private javax.swing.JLabel Quantity_toAdjust;
    private javax.swing.JLabel Reason_adjust;
    private javax.swing.JPanel RestockDetails_center;
    private javax.swing.JPanel RestockDetails_east;
    private javax.swing.JPanel RestockDetails_form;
    private javax.swing.JPanel RestockDetails_north;
    private javax.swing.JPanel RestockDetails_south;
    private javax.swing.JPanel RestockDetails_title;
    private javax.swing.JPanel RestockDetails_west;
    private javax.swing.JPanel Restock_center;
    private javax.swing.JPanel Restock_east;
    private javax.swing.JPanel Restock_left;
    private javax.swing.JPanel Restock_north;
    private javax.swing.JPanel Restock_south;
    private javax.swing.JPanel Restock_space2;
    private javax.swing.JPanel Restock_west;
    private javax.swing.JPanel SaleButtons;
    private javax.swing.JPanel SaleContent;
    private javax.swing.JPanel SaleInfo;
    private javax.swing.JPanel SaleInventory_form;
    private javax.swing.JScrollPane SaleScrollPane;
    private javax.swing.JPanel SaleSearch;
    private javax.swing.JPanel SaleSummary_buttons2;
    private javax.swing.JPanel Sale_and_Transactions_title;
    private javax.swing.JPanel Saledetails_north;
    private javax.swing.JPanel SalelistPanel;
    private javax.swing.JPanel Sales;
    private javax.swing.JPanel SalesActions;
    private javax.swing.JPanel SalesDetails_left;
    private javax.swing.JPanel SalesDetails_right;
    private javax.swing.JPanel SalesInventory;
    private javax.swing.JPanel Sales_Separator;
    private javax.swing.JPanel Sales_center;
    private javax.swing.JPanel Sales_content;
    private javax.swing.JPanel Sales_east;
    private javax.swing.JPanel Sales_header;
    private javax.swing.JPanel Sales_south;
    private javax.swing.JPanel Sales_west;
    private javax.swing.JPanel SelectedDetails3;
    private javax.swing.JLabel SelectedTransactioID;
    private javax.swing.JLabel Status;
    private javax.swing.JLabel Status_transactiondetails;
    private javax.swing.JPanel StockDetails_center;
    private javax.swing.JPanel StockDetails_center2;
    private javax.swing.JPanel StockDetails_center3;
    private javax.swing.JPanel StockDetails_center5;
    private javax.swing.JPanel StockDetails_center6;
    private javax.swing.JPanel StockDetails_east;
    private javax.swing.JPanel StockDetails_east2;
    private javax.swing.JPanel StockDetails_form;
    private javax.swing.JPanel StockDetails_form2;
    private javax.swing.JPanel StockDetails_form5;
    private javax.swing.JPanel StockDetails_north;
    private javax.swing.JPanel StockDetails_north2;
    private javax.swing.JPanel StockDetails_north5;
    private javax.swing.JPanel StockDetails_south;
    private javax.swing.JPanel StockDetails_south2;
    private javax.swing.JPanel StockDetails_title;
    private javax.swing.JPanel StockDetails_title2;
    private javax.swing.JPanel StockDetails_title5;
    private javax.swing.JPanel StockDetails_west;
    private javax.swing.JPanel StockDetails_west2;
    private javax.swing.JPanel Stock_level;
    private javax.swing.JLayeredPane Summary_north;
    private javax.swing.JLayeredPane Summary_south;
    private javax.swing.JLabel Supplier_Name;
    private javax.swing.JLabel Supplier_Name2;
    private javax.swing.JPanel Total_Products;
    private javax.swing.JLabel Total_Products_title;
    private javax.swing.JLabel Total_SaleDetails;
    private javax.swing.JPanel Total_Stock;
    private javax.swing.JPanel Total_sales;
    private javax.swing.JPanel Total_transactiondetails;
    private javax.swing.JLabel TransactionDate_transactiondetails;
    private javax.swing.JPanel Transactions;
    private javax.swing.JPanel ViewSaleCard;
    private javax.swing.JPanel ViewSalesDetailsCard;
    private javax.swing.JPanel Voided;
    private javax.swing.JLabel bghome;
    private javax.swing.JButton btnadd_initial;
    private javax.swing.JButton btnaddnewproduct;
    private javax.swing.JButton btnadjust_batches;
    private javax.swing.JButton btnback_batches;
    private javax.swing.JButton btnback_saledetails;
    private javax.swing.JButton btncancel_adjustment;
    private javax.swing.JButton btncancel_initial;
    private javax.swing.JButton btncancel_restock;
    private javax.swing.JButton btnconfirm_adjustment;
    private javax.swing.JButton btnhome;
    private javax.swing.JButton btninventory;
    private javax.swing.JButton btnlogout;
    private javax.swing.JButton btnsales;
    private javax.swing.JButton btnsave_restock;
    private javax.swing.JButton btnsearch_transactionid;
    private javax.swing.JButton btnstockinexistingsupplier_batches;
    private javax.swing.JButton btnstockinnewsupplier_batches;
    private javax.swing.JButton btnviewbatches;
    private javax.swing.JButton btnviewsalesdetails_admin;
    private javax.swing.JComboBox<String> cbadjustmenttype_adjust;
    private javax.swing.JComboBox<String> cbcategory_initial;
    private javax.swing.JComboBox<String> cbproductname_restock;
    private javax.swing.JComboBox<String> cbreason_adjust;
    private javax.swing.JComboBox<String> cbsuppliername_initial;
    private javax.swing.JComboBox<Supplier> cbsuppliername_restock;
    private javax.swing.JComboBox<String> cbunit_initial;
    private javax.swing.JCheckBox chbnoexpiry_initial;
    private javax.swing.JCheckBox chbnoexpiry_restock;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblbatchIdAdjust_show;
    private javax.swing.JLabel lblbatchidBatch_show;
    private javax.swing.JLabel lblcashiersalesDetails_show_admin;
    private javax.swing.JLabel lblcompletedtransactions_show;
    private javax.swing.JLabel lblcurrentquantityAdjust_show;
    private javax.swing.JLabel lblcurrentstock_show1;
    private javax.swing.JLabel lblnewtotalstock_show1;
    private javax.swing.JLabel lblnewtotalstock_show_admin;
    private javax.swing.JLabel lblpaymentmethodTransactionDetails_show_admin;
    private javax.swing.JLabel lblproductIdAdjust_show;
    private javax.swing.JLabel lblproductnameAdjust_show;
    private javax.swing.JLabel lblproductname_restock;
    private javax.swing.JLabel lblproductname_restock1;
    private javax.swing.JLabel lblrestocktype_show;
    private javax.swing.JLabel lblsalesandtransactions;
    private javax.swing.JLabel lblsalesthismonth_show;
    private javax.swing.JLabel lblselectedsalesID_show_admin;
    private javax.swing.JLabel lblstatusBatch_show;
    private javax.swing.JLabel lblstatusTransactionDetails_show_admin;
    private javax.swing.JLabel lbltotal_sales;
    private javax.swing.JLabel lbltotal_salesdetails;
    private javax.swing.JLabel lbltotalsales_show;
    private javax.swing.JLabel lbltransactiondatesalesDetails_show_admin;
    private javax.swing.JLabel lbltransactions;
    private javax.swing.JLabel lbltransactions_show;
    private javax.swing.JLabel lblvoided;
    private javax.swing.JLabel lblvoided_show;
    private javax.swing.JLabel lblvoidedthismonth_show;
    private javax.swing.JLabel lblweeklysales_show;
    private javax.swing.JTable tbbatches;
    private javax.swing.JTable tbinventory;
    private javax.swing.JTable tbsaledetails;
    private javax.swing.JTable tbsales_admin;
    private javax.swing.JTextField txtadjustquantity;
    private javax.swing.JTextField txtbrand_initial;
    private javax.swing.JTextField txtdatereceived_initial;
    private javax.swing.JTextField txtdatereceived_restock;
    private javax.swing.JTextField txtexpirationdate_restock;
    private javax.swing.JTextField txtexpirydate_initial;
    private javax.swing.JTextField txtproductname_initial;
    private javax.swing.JTextField txtquantity_initial;
    private javax.swing.JTextField txtquantity_restock;
    private javax.swing.JTextField txtsearch_inventory;
    private javax.swing.JTextField txtsearch_inventory1;
    private javax.swing.JTextField txtsearch_transactionid;
    private javax.swing.JTextField txtunitprice_initial;
    // End of variables declaration//GEN-END:variables
}
