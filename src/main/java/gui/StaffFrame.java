package gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.SaleDetail;
import model.Sale;
import model.Account;

import service.BatchService;
import service.StockService;
import service.SupplierService;
import service.SaleService;


public class StaffFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StaffFrame.class.getName());
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    
    private final SaleService managesale;
    private final ArrayList<SaleDetail> temporaryReceipt = new ArrayList<>();
    private final StockService managestock;
    private final BatchService managebatch;
    private final SupplierService managesupplier;
    
    //Set cardLayout variable
    private CardLayout cardLayout;
    private CardLayout saleCardLayout;
    private CardLayout salesHistoryCardLayout;
    
    private int UserId = -1;
    private int selectedSaleId = -1;
    private int selectedProductId = -1;
    private String selectedProductName;
    private double selectedUnitPrice = 0;
    private double selectedAvailableStock = 0;

    
    public StaffFrame(StockService stockService, BatchService batchService, SupplierService supplierService, SaleService saleService, int userId) 
    {
        initComponents();
        
        
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);
        
        //Disable Buttons/Panel Visibility
        Main_Panel.setOpaque(false);
        
        this.managesale = saleService;
        this.managestock = stockService;
        this.managebatch = batchService;
        this.managesupplier = supplierService;
        this.UserId = userId;
        
        
        lbluseridAddSale_show.setText(String.valueOf(this.UserId));
        
        refreshSaleTable();
        loadPaymentMethods();
        DefaultTableModel summaryModel = (DefaultTableModel) tbsales_summary.getModel();
        summaryModel.setRowCount(0);
        
        
        //Initialize cardLayout for Inventory and Batch table
        cardLayout = (CardLayout) Content_Panel.getLayout();
        saleCardLayout = (CardLayout) Sales.getLayout();
        salesHistoryCardLayout = (CardLayout) Stocks.getLayout();
        
        
        
        //Configure Design Opacity
        MainScroll.setOpaque(false);
        MainScroll.getViewport().setOpaque(false);
        btnsale.setBorderPainted(false);
        btnviewsales.setBorderPainted(false);
        btnlogout.setBorderPainted(false);

        btnsale.setFocusPainted(false);
        btnviewsales.setFocusPainted(false);
        btnlogout.setFocusPainted(false);
        
        
        btnsale.setBackground(new Color(255, 244, 230));
        btnviewsales.setBackground(new Color(255, 244, 230));
        btnaddorder_addsale.setBackground(Color.WHITE);
        btnback_saledetails.setBackground(Color.WHITE);
        btnclearorder_sale.setBackground(Color.WHITE);
        btnsaveorder_addsale.setBackground(Color.WHITE);
        btnviewsaledetails_saleshistory.setBackground(Color.WHITE);
        btnvoidsale.setBackground(Color.WHITE);
        
        
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
        
        
        
        txtdiscount_addsale.addFocusListener(new java.awt.event.FocusAdapter() 
        {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) 
            {
                updateSaleSummaryTotals();
            }
        }
        );
        
        
        
        tbinventory_view.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        tbinventory_view.getSelectionModel().addListSelectionListener(e -> {if (!e.getValueIsAdjusting()) {showSelectedProductDetails();}});
        
        
        tbsales_summary.setRowHeight(32);
        tbsales_summary.setShowGrid(false);
        tbsales_summary.setIntercellSpacing(new Dimension(0, 0));
        tbsales_summary.setSelectionBackground(new Color(230, 220, 170));
        tbsales_summary.setSelectionForeground(Color.BLACK);
        
        tbinventory_view.setRowHeight(32);
        tbinventory_view.setShowGrid(false);
        tbinventory_view.setIntercellSpacing(new Dimension(0, 0));
        tbinventory_view.setSelectionBackground(new Color(230, 220, 170));
        tbinventory_view.setSelectionForeground(Color.BLACK);
        
        
        tbsaleshistory.setRowHeight(32);
        tbsaleshistory.setShowGrid(false);
        tbsaleshistory.setIntercellSpacing(new Dimension(0, 0));
        tbsaleshistory.setSelectionBackground(new Color(230, 220, 170));
        tbsaleshistory.setSelectionForeground(Color.BLACK);
        
        

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
        btnsale = new javax.swing.JButton();
        btnviewsales = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        Nav_south = new javax.swing.JPanel();
        Logout_north = new javax.swing.JPanel();
        Logout_south = new javax.swing.JPanel();
        btnlogout = new javax.swing.JButton();
        MainScroll = new javax.swing.JScrollPane();
        Content_Panel = new javax.swing.JPanel();
        Sales = new javax.swing.JPanel();
        AddSale_tab = new javax.swing.JPanel();
        AddSale_north = new javax.swing.JPanel();
        AddSale_west = new javax.swing.JPanel();
        AddSale_center = new javax.swing.JPanel();
        AddSale_left = new javax.swing.JPanel();
        AddSaleInventory = new javax.swing.JPanel();
        SaleSearch = new javax.swing.JPanel();
        txtsearch_sales = new javax.swing.JTextField();
        tbsearch_sales = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        SalelistPanel = new javax.swing.JPanel();
        SaleContent = new javax.swing.JPanel();
        SaleCard = new javax.swing.JPanel();
        SaleInfo = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbinventory_view = new javax.swing.JTable();
        AddSale_separator = new javax.swing.JPanel();
        SelectedDetails = new javax.swing.JPanel();
        StockDetails_title2 = new javax.swing.JPanel();
        Add_Inventory_title5 = new javax.swing.JLabel();
        StockDetails_form2 = new javax.swing.JPanel();
        StockDetails_east2 = new javax.swing.JPanel();
        StockDetails_west2 = new javax.swing.JPanel();
        StockDetails_north2 = new javax.swing.JPanel();
        StockDetails_south2 = new javax.swing.JPanel();
        StockDetails_center2 = new javax.swing.JPanel();
        ProductIdAddSale = new javax.swing.JLabel();
        lblproductIdAddSale_show = new javax.swing.JLabel();
        ProductNameAddSale = new javax.swing.JLabel();
        lblproductnameAddSale_show = new javax.swing.JLabel();
        BrandAddSale = new javax.swing.JLabel();
        lblbrandAddSale_show = new javax.swing.JLabel();
        CategoryAddSale = new javax.swing.JLabel();
        lblcategoryAddSale_show = new javax.swing.JLabel();
        AvailableStockAddSale = new javax.swing.JLabel();
        lblavailablestockAddSale_show = new javax.swing.JLabel();
        PriceAddSale = new javax.swing.JLabel();
        lblpriceAddSale_show = new javax.swing.JLabel();
        ExpiryAddSale = new javax.swing.JLabel();
        lblexpiryAddSale_show = new javax.swing.JLabel();
        Quantity_AddSale = new javax.swing.JLabel();
        Quantity_AddSale_space = new javax.swing.JLabel();
        txtquantityinput_addsale = new javax.swing.JTextField();
        btnaddorder_addsale = new javax.swing.JButton();
        AddSale_centerseparator = new javax.swing.JPanel();
        AddSale_right = new javax.swing.JPanel();
        SelectedDetails1 = new javax.swing.JPanel();
        StockDetails_title3 = new javax.swing.JPanel();
        Add_Inventory_title6 = new javax.swing.JLabel();
        StockDetails_form3 = new javax.swing.JPanel();
        StockDetails_north3 = new javax.swing.JPanel();
        StockDetails_center3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbsales_summary = new javax.swing.JTable();
        Total_panel = new javax.swing.JPanel();
        User_addsale = new javax.swing.JLabel();
        lbluseridAddSale_show = new javax.swing.JLabel();
        Subtotal_addsale = new javax.swing.JLabel();
        lblsubtotal_addsale = new javax.swing.JLabel();
        PaymentMethod_addsale = new javax.swing.JLabel();
        cbpaymentmethod_addsale = new javax.swing.JComboBox<>();
        Discount_addsale = new javax.swing.JLabel();
        txtdiscount_addsale = new javax.swing.JTextField();
        Total_addsale = new javax.swing.JLabel();
        lbltotalAddSale_show = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        SaleSummary_buttons = new javax.swing.JPanel();
        btnsaveorder_addsale = new javax.swing.JButton();
        btnclearorder_sale = new javax.swing.JButton();
        AddSale_east = new javax.swing.JPanel();
        AddSale_south = new javax.swing.JPanel();
        Stocks = new javax.swing.JPanel();
        ViewSales = new javax.swing.JPanel();
        AddSale_west1 = new javax.swing.JPanel();
        AddSale_east1 = new javax.swing.JPanel();
        AddSale_south1 = new javax.swing.JPanel();
        ViewHistory_center = new javax.swing.JPanel();
        SaleHistory_right = new javax.swing.JPanel();
        SaleHistorySelectedDetails = new javax.swing.JPanel();
        StockDetails_title4 = new javax.swing.JPanel();
        Add_Inventory_title7 = new javax.swing.JLabel();
        StockDetails_form4 = new javax.swing.JPanel();
        StockDetails_north4 = new javax.swing.JPanel();
        StockDetails_center4 = new javax.swing.JPanel();
        SaleHistoryInventory = new javax.swing.JPanel();
        TransactionHistory_header = new javax.swing.JPanel();
        txtsearch_saleshistory = new javax.swing.JTextField();
        btnsearch_transactionhistory = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        SaleHistorylistPanel = new javax.swing.JPanel();
        SaleHistoryContent = new javax.swing.JPanel();
        SaleHistoryCard = new javax.swing.JPanel();
        SaleHistoryInfo = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbsaleshistory = new javax.swing.JTable();
        SaleHistoryButtons = new javax.swing.JPanel();
        btnviewsaledetails_saleshistory = new javax.swing.JButton();
        Total_panel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        AddSale_north2 = new javax.swing.JPanel();
        ViewSalesDetails = new javax.swing.JPanel();
        ViewHistory_west = new javax.swing.JPanel();
        ViewHistory_east = new javax.swing.JPanel();
        ViewHistory_south = new javax.swing.JPanel();
        ViewHistory_center_inner = new javax.swing.JPanel();
        ViewHistory_right = new javax.swing.JPanel();
        SelectedDetails3 = new javax.swing.JPanel();
        StockDetails_title5 = new javax.swing.JPanel();
        Add_Inventory_title8 = new javax.swing.JLabel();
        StockDetails_form5 = new javax.swing.JPanel();
        StockDetails_north5 = new javax.swing.JPanel();
        StockDetails_center5 = new javax.swing.JPanel();
        Saledetails_north = new javax.swing.JPanel();
        StockDetails_center1 = new javax.swing.JPanel();
        SalesDetails_left = new javax.swing.JPanel();
        SelectedTransactioID = new javax.swing.JLabel();
        lblselectedsalesID_show = new javax.swing.JLabel();
        TransactionDate_transactiondetails = new javax.swing.JLabel();
        lbltransactiondatesalesDetails_show = new javax.swing.JLabel();
        Cashier_transactiondetails = new javax.swing.JLabel();
        lblcashiersalesDetails_show = new javax.swing.JLabel();
        SalesDetails_right = new javax.swing.JPanel();
        lblnewtotalstock_show = new javax.swing.JLabel();
        StockDetails_center6 = new javax.swing.JPanel();
        Supplier_Name2 = new javax.swing.JLabel();
        lblcurrentstock_show1 = new javax.swing.JLabel();
        PaymentMethod_transactiondetails = new javax.swing.JLabel();
        lblpaymentmethodTransactionDetails_show = new javax.swing.JLabel();
        Status_transactiondetails = new javax.swing.JLabel();
        lblstatusTransactionDetails_show = new javax.swing.JLabel();
        Restock_space2 = new javax.swing.JPanel();
        lblnewtotalstock_show1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        ItemsSold_panel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        ItemsSold_center = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbsaledetails = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        Total_transactiondetails = new javax.swing.JPanel();
        Total_SaleDetails = new javax.swing.JLabel();
        lbltotal_salesdetails = new javax.swing.JLabel();
        SaleSummary_buttons2 = new javax.swing.JPanel();
        btnvoidsale = new javax.swing.JButton();
        btnback_saledetails = new javax.swing.JButton();
        ViewHistory_north = new javax.swing.JPanel();

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
            .addGap(0, 80, Short.MAX_VALUE)
        );
        Adminicon_northLayout.setVerticalGroup(
            Adminicon_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
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

        btnsale.setBackground(new java.awt.Color(255, 244, 230));
        btnsale.setIcon(new javax.swing.ImageIcon("C:\\Users\\user\\OneDrive\\Desktop\\Netbeans Programs\\Java Projects\\002-PetSupplies_Ledger_and_Tracker\\src\\main\\resources\\images\\Record(resized).png")); // NOI18N
        btnsale.addActionListener(this::btnsaleActionPerformed);
        Nav_center.add(btnsale);

        btnviewsales.setBackground(new java.awt.Color(255, 244, 230));
        btnviewsales.setIcon(new javax.swing.ImageIcon("C:\\Users\\user\\OneDrive\\Desktop\\Netbeans Programs\\Java Projects\\002-PetSupplies_Ledger_and_Tracker\\src\\main\\resources\\images\\transactions2.png")); // NOI18N
        btnviewsales.addActionListener(this::btnviewsalesActionPerformed);
        Nav_center.add(btnviewsales);

        jPanel3.setBackground(new java.awt.Color(255, 244, 230));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );

        Nav_center.add(jPanel3);

        Navigation_Panel.add(Nav_center, java.awt.BorderLayout.CENTER);

        Nav_south.setPreferredSize(new java.awt.Dimension(80, 150));
        Nav_south.setLayout(new java.awt.BorderLayout());

        Logout_north.setBackground(new java.awt.Color(255, 244, 230));
        Logout_north.setPreferredSize(new java.awt.Dimension(80, 46));

        javax.swing.GroupLayout Logout_northLayout = new javax.swing.GroupLayout(Logout_north);
        Logout_north.setLayout(Logout_northLayout);
        Logout_northLayout.setHorizontalGroup(
            Logout_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );
        Logout_northLayout.setVerticalGroup(
            Logout_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
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

        Sales.setPreferredSize(new java.awt.Dimension(944, 768));
        Sales.setLayout(new java.awt.CardLayout());

        AddSale_tab.setLayout(new java.awt.BorderLayout());

        AddSale_north.setBackground(new java.awt.Color(244, 243, 243));

        javax.swing.GroupLayout AddSale_northLayout = new javax.swing.GroupLayout(AddSale_north);
        AddSale_north.setLayout(AddSale_northLayout);
        AddSale_northLayout.setHorizontalGroup(
            AddSale_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1343, Short.MAX_VALUE)
        );
        AddSale_northLayout.setVerticalGroup(
            AddSale_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        AddSale_tab.add(AddSale_north, java.awt.BorderLayout.NORTH);

        AddSale_west.setBackground(new java.awt.Color(244, 243, 243));
        AddSale_west.setPreferredSize(new java.awt.Dimension(15, 638));

        javax.swing.GroupLayout AddSale_westLayout = new javax.swing.GroupLayout(AddSale_west);
        AddSale_west.setLayout(AddSale_westLayout);
        AddSale_westLayout.setHorizontalGroup(
            AddSale_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        AddSale_westLayout.setVerticalGroup(
            AddSale_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 638, Short.MAX_VALUE)
        );

        AddSale_tab.add(AddSale_west, java.awt.BorderLayout.LINE_START);

        AddSale_center.setLayout(new javax.swing.BoxLayout(AddSale_center, javax.swing.BoxLayout.X_AXIS));

        AddSale_left.setPreferredSize(new java.awt.Dimension(400, 638));
        AddSale_left.setLayout(new javax.swing.BoxLayout(AddSale_left, javax.swing.BoxLayout.Y_AXIS));

        AddSaleInventory.setPreferredSize(new java.awt.Dimension(914, 914));
        AddSaleInventory.setLayout(new java.awt.BorderLayout());

        SaleSearch.setBackground(new java.awt.Color(245, 218, 83));
        SaleSearch.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        SaleSearch.setPreferredSize(new java.awt.Dimension(914, 50));
        SaleSearch.setLayout(new java.awt.BorderLayout());

        txtsearch_sales.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        SaleSearch.add(txtsearch_sales, java.awt.BorderLayout.CENTER);

        tbsearch_sales.setBackground(new java.awt.Color(245, 218, 83));
        tbsearch_sales.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        tbsearch_sales.setText("Search");
        tbsearch_sales.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tbsearch_sales.setPreferredSize(new java.awt.Dimension(200, 23));
        SaleSearch.add(tbsearch_sales, java.awt.BorderLayout.EAST);

        jLabel13.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Search Products");
        jLabel13.setPreferredSize(new java.awt.Dimension(280, 16));
        SaleSearch.add(jLabel13, java.awt.BorderLayout.WEST);

        AddSaleInventory.add(SaleSearch, java.awt.BorderLayout.NORTH);
        AddSaleInventory.add(jSeparator3, java.awt.BorderLayout.CENTER);

        SalelistPanel.setBackground(new java.awt.Color(255, 253, 248));
        SalelistPanel.setLayout(new javax.swing.BoxLayout(SalelistPanel, javax.swing.BoxLayout.Y_AXIS));

        SaleContent.setLayout(new java.awt.CardLayout());

        SaleCard.setBackground(new java.awt.Color(255, 255, 255));
        SaleCard.setLayout(new java.awt.BorderLayout());

        SaleInfo.setBackground(new java.awt.Color(244, 243, 243));
        SaleInfo.setLayout(new javax.swing.BoxLayout(SaleInfo, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane3.setPreferredSize(new java.awt.Dimension(452, 360));

        tbinventory_view.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Name", "Brand", "Category", "Total Stock", "Price", "Expiration Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbinventory_view.setRowHeight(50);
        tbinventory_view.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tbinventory_view);
        if (tbinventory_view.getColumnModel().getColumnCount() > 0) {
            tbinventory_view.getColumnModel().getColumn(0).setResizable(false);
            tbinventory_view.getColumnModel().getColumn(1).setResizable(false);
            tbinventory_view.getColumnModel().getColumn(2).setResizable(false);
            tbinventory_view.getColumnModel().getColumn(3).setResizable(false);
            tbinventory_view.getColumnModel().getColumn(4).setResizable(false);
            tbinventory_view.getColumnModel().getColumn(5).setResizable(false);
            tbinventory_view.getColumnModel().getColumn(6).setResizable(false);
        }

        SaleInfo.add(jScrollPane3);

        SaleCard.add(SaleInfo, java.awt.BorderLayout.CENTER);

        SaleContent.add(SaleCard, "Display_productcard");

        SalelistPanel.add(SaleContent);

        AddSaleInventory.add(SalelistPanel, java.awt.BorderLayout.CENTER);

        AddSale_left.add(AddSaleInventory);

        AddSale_separator.setBackground(new java.awt.Color(255, 255, 255));
        AddSale_separator.setPreferredSize(new java.awt.Dimension(588, 20));

        javax.swing.GroupLayout AddSale_separatorLayout = new javax.swing.GroupLayout(AddSale_separator);
        AddSale_separator.setLayout(AddSale_separatorLayout);
        AddSale_separatorLayout.setHorizontalGroup(
            AddSale_separatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 683, Short.MAX_VALUE)
        );
        AddSale_separatorLayout.setVerticalGroup(
            AddSale_separatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        AddSale_left.add(AddSale_separator);

        SelectedDetails.setBackground(new java.awt.Color(255, 255, 255));
        SelectedDetails.setPreferredSize(new java.awt.Dimension(452, 688));
        SelectedDetails.setLayout(new java.awt.BorderLayout());

        StockDetails_title2.setBackground(new java.awt.Color(245, 218, 83));
        StockDetails_title2.setPreferredSize(new java.awt.Dimension(452, 50));
        StockDetails_title2.setLayout(new java.awt.BorderLayout());

        Add_Inventory_title5.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        Add_Inventory_title5.setText("Product Details");
        Add_Inventory_title5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        StockDetails_title2.add(Add_Inventory_title5, java.awt.BorderLayout.CENTER);

        SelectedDetails.add(StockDetails_title2, java.awt.BorderLayout.NORTH);

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
            .addGap(0, 331, Short.MAX_VALUE)
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
            .addGap(0, 331, Short.MAX_VALUE)
        );

        StockDetails_form2.add(StockDetails_west2, java.awt.BorderLayout.WEST);

        StockDetails_north2.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_north2.setPreferredSize(new java.awt.Dimension(904, 10));

        javax.swing.GroupLayout StockDetails_north2Layout = new javax.swing.GroupLayout(StockDetails_north2);
        StockDetails_north2.setLayout(StockDetails_north2Layout);
        StockDetails_north2Layout.setHorizontalGroup(
            StockDetails_north2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 683, Short.MAX_VALUE)
        );
        StockDetails_north2Layout.setVerticalGroup(
            StockDetails_north2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        StockDetails_form2.add(StockDetails_north2, java.awt.BorderLayout.NORTH);

        StockDetails_south2.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_south2.setPreferredSize(new java.awt.Dimension(904, 20));

        javax.swing.GroupLayout StockDetails_south2Layout = new javax.swing.GroupLayout(StockDetails_south2);
        StockDetails_south2.setLayout(StockDetails_south2Layout);
        StockDetails_south2Layout.setHorizontalGroup(
            StockDetails_south2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 683, Short.MAX_VALUE)
        );
        StockDetails_south2Layout.setVerticalGroup(
            StockDetails_south2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        StockDetails_form2.add(StockDetails_south2, java.awt.BorderLayout.SOUTH);

        StockDetails_center2.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_center2.setLayout(new java.awt.GridLayout(9, 2, 0, 10));

        ProductIdAddSale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        ProductIdAddSale.setText("Product Id");
        StockDetails_center2.add(ProductIdAddSale);

        lblproductIdAddSale_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center2.add(lblproductIdAddSale_show);

        ProductNameAddSale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        ProductNameAddSale.setText("Product Name");
        StockDetails_center2.add(ProductNameAddSale);

        lblproductnameAddSale_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center2.add(lblproductnameAddSale_show);

        BrandAddSale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        BrandAddSale.setText("Brand");
        StockDetails_center2.add(BrandAddSale);

        lblbrandAddSale_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center2.add(lblbrandAddSale_show);

        CategoryAddSale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        CategoryAddSale.setText("Category");
        StockDetails_center2.add(CategoryAddSale);

        lblcategoryAddSale_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center2.add(lblcategoryAddSale_show);

        AvailableStockAddSale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        AvailableStockAddSale.setText("Available Stock");
        StockDetails_center2.add(AvailableStockAddSale);

        lblavailablestockAddSale_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center2.add(lblavailablestockAddSale_show);

        PriceAddSale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        PriceAddSale.setText("Price");
        StockDetails_center2.add(PriceAddSale);

        lblpriceAddSale_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center2.add(lblpriceAddSale_show);

        ExpiryAddSale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        ExpiryAddSale.setText("Expiration Date");
        StockDetails_center2.add(ExpiryAddSale);

        lblexpiryAddSale_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center2.add(lblexpiryAddSale_show);

        Quantity_AddSale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        Quantity_AddSale.setText("Quantity");
        Quantity_AddSale.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center2.add(Quantity_AddSale);

        Quantity_AddSale_space.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Quantity_AddSale_space.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center2.add(Quantity_AddSale_space);

        txtquantityinput_addsale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        StockDetails_center2.add(txtquantityinput_addsale);

        btnaddorder_addsale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        btnaddorder_addsale.setText("+Add to Order");
        btnaddorder_addsale.setFocusPainted(false);
        btnaddorder_addsale.setFocusable(false);
        btnaddorder_addsale.addActionListener(this::btnaddorder_addsaleActionPerformed);
        StockDetails_center2.add(btnaddorder_addsale);

        StockDetails_form2.add(StockDetails_center2, java.awt.BorderLayout.CENTER);

        SelectedDetails.add(StockDetails_form2, java.awt.BorderLayout.CENTER);

        AddSale_left.add(SelectedDetails);

        AddSale_center.add(AddSale_left);

        AddSale_centerseparator.setBackground(new java.awt.Color(244, 243, 243));
        AddSale_centerseparator.setPreferredSize(new java.awt.Dimension(15, 638));

        javax.swing.GroupLayout AddSale_centerseparatorLayout = new javax.swing.GroupLayout(AddSale_centerseparator);
        AddSale_centerseparator.setLayout(AddSale_centerseparatorLayout);
        AddSale_centerseparatorLayout.setHorizontalGroup(
            AddSale_centerseparatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        AddSale_centerseparatorLayout.setVerticalGroup(
            AddSale_centerseparatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 638, Short.MAX_VALUE)
        );

        AddSale_center.add(AddSale_centerseparator);

        AddSale_right.setPreferredSize(new java.awt.Dimension(614, 38));
        AddSale_right.setLayout(new java.awt.GridLayout(1, 0));

        SelectedDetails1.setBackground(new java.awt.Color(255, 255, 255));
        SelectedDetails1.setPreferredSize(new java.awt.Dimension(452, 688));
        SelectedDetails1.setLayout(new java.awt.BorderLayout());

        StockDetails_title3.setBackground(new java.awt.Color(245, 218, 83));
        StockDetails_title3.setPreferredSize(new java.awt.Dimension(452, 50));
        StockDetails_title3.setLayout(new java.awt.BorderLayout());

        Add_Inventory_title6.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        Add_Inventory_title6.setText("Sale Summary");
        Add_Inventory_title6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        StockDetails_title3.add(Add_Inventory_title6, java.awt.BorderLayout.CENTER);

        SelectedDetails1.add(StockDetails_title3, java.awt.BorderLayout.NORTH);

        StockDetails_form3.setLayout(new java.awt.BorderLayout());

        StockDetails_north3.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_north3.setPreferredSize(new java.awt.Dimension(904, 10));

        javax.swing.GroupLayout StockDetails_north3Layout = new javax.swing.GroupLayout(StockDetails_north3);
        StockDetails_north3.setLayout(StockDetails_north3Layout);
        StockDetails_north3Layout.setHorizontalGroup(
            StockDetails_north3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 614, Short.MAX_VALUE)
        );
        StockDetails_north3Layout.setVerticalGroup(
            StockDetails_north3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        StockDetails_form3.add(StockDetails_north3, java.awt.BorderLayout.NORTH);

        StockDetails_center3.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_center3.setLayout(new javax.swing.BoxLayout(StockDetails_center3, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane4.setPreferredSize(new java.awt.Dimension(452, 350));

        tbsales_summary.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Product ID", "Product Name", "Price", "Quantity", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbsales_summary.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(tbsales_summary);
        if (tbsales_summary.getColumnModel().getColumnCount() > 0) {
            tbsales_summary.getColumnModel().getColumn(0).setResizable(false);
            tbsales_summary.getColumnModel().getColumn(1).setResizable(false);
            tbsales_summary.getColumnModel().getColumn(2).setResizable(false);
            tbsales_summary.getColumnModel().getColumn(3).setResizable(false);
            tbsales_summary.getColumnModel().getColumn(4).setResizable(false);
        }

        StockDetails_center3.add(jScrollPane4);

        Total_panel.setBackground(new java.awt.Color(255, 255, 255));
        Total_panel.setPreferredSize(new java.awt.Dimension(536, 100));
        Total_panel.setLayout(new java.awt.GridLayout(5, 2, 0, 5));

        User_addsale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        User_addsale.setText("User ID");
        User_addsale.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        Total_panel.add(User_addsale);

        lbluseridAddSale_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        Total_panel.add(lbluseridAddSale_show);

        Subtotal_addsale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Subtotal_addsale.setText("Subtotal");
        Subtotal_addsale.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        Total_panel.add(Subtotal_addsale);

        lblsubtotal_addsale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Total_panel.add(lblsubtotal_addsale);

        PaymentMethod_addsale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        PaymentMethod_addsale.setText("Payment Method");
        PaymentMethod_addsale.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        Total_panel.add(PaymentMethod_addsale);

        cbpaymentmethod_addsale.setEditable(true);
        cbpaymentmethod_addsale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        Total_panel.add(cbpaymentmethod_addsale);

        Discount_addsale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        Discount_addsale.setText("Discount");
        Discount_addsale.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        Total_panel.add(Discount_addsale);

        txtdiscount_addsale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        Total_panel.add(txtdiscount_addsale);

        Total_addsale.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        Total_addsale.setText("Total");
        Total_addsale.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        Total_panel.add(Total_addsale);

        lbltotalAddSale_show.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 12)); // NOI18N
        Total_panel.add(lbltotalAddSale_show);

        StockDetails_center3.add(Total_panel);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(537, 10));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 614, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );

        StockDetails_center3.add(jPanel2);

        SaleSummary_buttons.setBackground(new java.awt.Color(255, 255, 255));
        SaleSummary_buttons.setPreferredSize(new java.awt.Dimension(533, 10));
        SaleSummary_buttons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));

        btnsaveorder_addsale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        btnsaveorder_addsale.setText("Save Order");
        btnsaveorder_addsale.setFocusPainted(false);
        btnsaveorder_addsale.setFocusable(false);
        btnsaveorder_addsale.setPreferredSize(new java.awt.Dimension(250, 30));
        btnsaveorder_addsale.addActionListener(this::btnsaveorder_addsaleActionPerformed);
        SaleSummary_buttons.add(btnsaveorder_addsale);

        btnclearorder_sale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        btnclearorder_sale.setText("Clear Order");
        btnclearorder_sale.setFocusPainted(false);
        btnclearorder_sale.setFocusable(false);
        btnclearorder_sale.setPreferredSize(new java.awt.Dimension(250, 30));
        btnclearorder_sale.addActionListener(this::btnclearorder_saleActionPerformed);
        SaleSummary_buttons.add(btnclearorder_sale);

        StockDetails_center3.add(SaleSummary_buttons);

        StockDetails_form3.add(StockDetails_center3, java.awt.BorderLayout.CENTER);

        SelectedDetails1.add(StockDetails_form3, java.awt.BorderLayout.CENTER);

        AddSale_right.add(SelectedDetails1);

        AddSale_center.add(AddSale_right);

        AddSale_tab.add(AddSale_center, java.awt.BorderLayout.CENTER);

        AddSale_east.setBackground(new java.awt.Color(244, 243, 243));
        AddSale_east.setPreferredSize(new java.awt.Dimension(15, 638));

        javax.swing.GroupLayout AddSale_eastLayout = new javax.swing.GroupLayout(AddSale_east);
        AddSale_east.setLayout(AddSale_eastLayout);
        AddSale_eastLayout.setHorizontalGroup(
            AddSale_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        AddSale_eastLayout.setVerticalGroup(
            AddSale_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 638, Short.MAX_VALUE)
        );

        AddSale_tab.add(AddSale_east, java.awt.BorderLayout.LINE_END);

        AddSale_south.setBackground(new java.awt.Color(244, 243, 243));
        AddSale_south.setPreferredSize(new java.awt.Dimension(944, 80));
        AddSale_south.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 20, 10));
        AddSale_tab.add(AddSale_south, java.awt.BorderLayout.SOUTH);

        Sales.add(AddSale_tab, "Display_addsale");

        Content_Panel.add(Sales, "Display_inventory");

        Stocks.setLayout(new java.awt.CardLayout());

        ViewSales.setLayout(new java.awt.BorderLayout());

        AddSale_west1.setBackground(new java.awt.Color(244, 243, 243));
        AddSale_west1.setPreferredSize(new java.awt.Dimension(15, 638));

        javax.swing.GroupLayout AddSale_west1Layout = new javax.swing.GroupLayout(AddSale_west1);
        AddSale_west1.setLayout(AddSale_west1Layout);
        AddSale_west1Layout.setHorizontalGroup(
            AddSale_west1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        AddSale_west1Layout.setVerticalGroup(
            AddSale_west1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 638, Short.MAX_VALUE)
        );

        ViewSales.add(AddSale_west1, java.awt.BorderLayout.LINE_START);

        AddSale_east1.setBackground(new java.awt.Color(244, 243, 243));
        AddSale_east1.setPreferredSize(new java.awt.Dimension(15, 638));

        javax.swing.GroupLayout AddSale_east1Layout = new javax.swing.GroupLayout(AddSale_east1);
        AddSale_east1.setLayout(AddSale_east1Layout);
        AddSale_east1Layout.setHorizontalGroup(
            AddSale_east1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        AddSale_east1Layout.setVerticalGroup(
            AddSale_east1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 638, Short.MAX_VALUE)
        );

        ViewSales.add(AddSale_east1, java.awt.BorderLayout.LINE_END);

        AddSale_south1.setBackground(new java.awt.Color(244, 243, 243));
        AddSale_south1.setPreferredSize(new java.awt.Dimension(944, 80));
        AddSale_south1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 20, 10));
        ViewSales.add(AddSale_south1, java.awt.BorderLayout.SOUTH);

        ViewHistory_center.setLayout(new java.awt.GridLayout(1, 0));

        SaleHistory_right.setPreferredSize(new java.awt.Dimension(614, 38));
        SaleHistory_right.setLayout(new java.awt.GridLayout(1, 0));

        SaleHistorySelectedDetails.setBackground(new java.awt.Color(255, 255, 255));
        SaleHistorySelectedDetails.setPreferredSize(new java.awt.Dimension(452, 688));
        SaleHistorySelectedDetails.setLayout(new java.awt.BorderLayout());

        StockDetails_title4.setBackground(new java.awt.Color(245, 218, 83));
        StockDetails_title4.setPreferredSize(new java.awt.Dimension(452, 50));
        StockDetails_title4.setLayout(new java.awt.BorderLayout());

        Add_Inventory_title7.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        Add_Inventory_title7.setText("Sales History");
        Add_Inventory_title7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        StockDetails_title4.add(Add_Inventory_title7, java.awt.BorderLayout.CENTER);

        SaleHistorySelectedDetails.add(StockDetails_title4, java.awt.BorderLayout.NORTH);

        StockDetails_form4.setLayout(new java.awt.BorderLayout());

        StockDetails_north4.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_north4.setPreferredSize(new java.awt.Dimension(904, 10));

        javax.swing.GroupLayout StockDetails_north4Layout = new javax.swing.GroupLayout(StockDetails_north4);
        StockDetails_north4.setLayout(StockDetails_north4Layout);
        StockDetails_north4Layout.setHorizontalGroup(
            StockDetails_north4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1313, Short.MAX_VALUE)
        );
        StockDetails_north4Layout.setVerticalGroup(
            StockDetails_north4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        StockDetails_form4.add(StockDetails_north4, java.awt.BorderLayout.NORTH);

        StockDetails_center4.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_center4.setLayout(new javax.swing.BoxLayout(StockDetails_center4, javax.swing.BoxLayout.Y_AXIS));

        SaleHistoryInventory.setPreferredSize(new java.awt.Dimension(914, 914));
        SaleHistoryInventory.setLayout(new java.awt.BorderLayout());

        TransactionHistory_header.setBackground(new java.awt.Color(245, 218, 83));
        TransactionHistory_header.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        TransactionHistory_header.setPreferredSize(new java.awt.Dimension(914, 50));
        TransactionHistory_header.setLayout(new java.awt.BorderLayout());

        txtsearch_saleshistory.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TransactionHistory_header.add(txtsearch_saleshistory, java.awt.BorderLayout.CENTER);

        btnsearch_transactionhistory.setBackground(new java.awt.Color(245, 218, 83));
        btnsearch_transactionhistory.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        btnsearch_transactionhistory.setText("Search");
        btnsearch_transactionhistory.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnsearch_transactionhistory.setPreferredSize(new java.awt.Dimension(200, 23));
        TransactionHistory_header.add(btnsearch_transactionhistory, java.awt.BorderLayout.EAST);

        jLabel14.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Search Sales");
        jLabel14.setPreferredSize(new java.awt.Dimension(280, 16));
        TransactionHistory_header.add(jLabel14, java.awt.BorderLayout.WEST);

        SaleHistoryInventory.add(TransactionHistory_header, java.awt.BorderLayout.NORTH);
        SaleHistoryInventory.add(jSeparator4, java.awt.BorderLayout.CENTER);

        SaleHistorylistPanel.setBackground(new java.awt.Color(255, 253, 248));
        SaleHistorylistPanel.setPreferredSize(new java.awt.Dimension(1313, 864));
        SaleHistorylistPanel.setLayout(new javax.swing.BoxLayout(SaleHistorylistPanel, javax.swing.BoxLayout.Y_AXIS));

        SaleHistoryContent.setPreferredSize(new java.awt.Dimension(1313, 864));
        SaleHistoryContent.setLayout(new java.awt.CardLayout());

        SaleHistoryCard.setLayout(new java.awt.BorderLayout());

        SaleHistoryInfo.setLayout(new javax.swing.BoxLayout(SaleHistoryInfo, javax.swing.BoxLayout.Y_AXIS));

        tbsaleshistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

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
        tbsaleshistory.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbsaleshistory);
        if (tbsaleshistory.getColumnModel().getColumnCount() > 0) {
            tbsaleshistory.getColumnModel().getColumn(0).setResizable(false);
            tbsaleshistory.getColumnModel().getColumn(1).setResizable(false);
            tbsaleshistory.getColumnModel().getColumn(2).setResizable(false);
            tbsaleshistory.getColumnModel().getColumn(3).setResizable(false);
            tbsaleshistory.getColumnModel().getColumn(4).setResizable(false);
            tbsaleshistory.getColumnModel().getColumn(5).setResizable(false);
        }

        SaleHistoryInfo.add(jScrollPane1);

        SaleHistoryCard.add(SaleHistoryInfo, java.awt.BorderLayout.CENTER);

        SaleHistoryButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        btnviewsaledetails_saleshistory.setText("View Sales");
        btnviewsaledetails_saleshistory.setPreferredSize(new java.awt.Dimension(130, 30));
        btnviewsaledetails_saleshistory.addActionListener(this::btnviewsaledetails_saleshistoryActionPerformed);
        SaleHistoryButtons.add(btnviewsaledetails_saleshistory);

        SaleHistoryCard.add(SaleHistoryButtons, java.awt.BorderLayout.SOUTH);

        SaleHistoryContent.add(SaleHistoryCard, "card2");

        SaleHistorylistPanel.add(SaleHistoryContent);

        SaleHistoryInventory.add(SaleHistorylistPanel, java.awt.BorderLayout.CENTER);

        StockDetails_center4.add(SaleHistoryInventory);

        Total_panel1.setBackground(new java.awt.Color(255, 255, 255));
        Total_panel1.setPreferredSize(new java.awt.Dimension(536, 100));
        Total_panel1.setLayout(new java.awt.GridLayout(1, 2));

        jPanel7.setLayout(new java.awt.GridLayout(1, 0));
        Total_panel1.add(jPanel7);

        StockDetails_center4.add(Total_panel1);

        StockDetails_form4.add(StockDetails_center4, java.awt.BorderLayout.CENTER);

        SaleHistorySelectedDetails.add(StockDetails_form4, java.awt.BorderLayout.CENTER);

        SaleHistory_right.add(SaleHistorySelectedDetails);

        ViewHistory_center.add(SaleHistory_right);

        ViewSales.add(ViewHistory_center, java.awt.BorderLayout.CENTER);

        AddSale_north2.setBackground(new java.awt.Color(244, 243, 243));

        javax.swing.GroupLayout AddSale_north2Layout = new javax.swing.GroupLayout(AddSale_north2);
        AddSale_north2.setLayout(AddSale_north2Layout);
        AddSale_north2Layout.setHorizontalGroup(
            AddSale_north2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1343, Short.MAX_VALUE)
        );
        AddSale_north2Layout.setVerticalGroup(
            AddSale_north2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        ViewSales.add(AddSale_north2, java.awt.BorderLayout.NORTH);

        Stocks.add(ViewSales, "Display_saleshistory");

        ViewSalesDetails.setLayout(new java.awt.BorderLayout());

        ViewHistory_west.setBackground(new java.awt.Color(244, 243, 243));
        ViewHistory_west.setPreferredSize(new java.awt.Dimension(15, 638));

        javax.swing.GroupLayout ViewHistory_westLayout = new javax.swing.GroupLayout(ViewHistory_west);
        ViewHistory_west.setLayout(ViewHistory_westLayout);
        ViewHistory_westLayout.setHorizontalGroup(
            ViewHistory_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        ViewHistory_westLayout.setVerticalGroup(
            ViewHistory_westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 638, Short.MAX_VALUE)
        );

        ViewSalesDetails.add(ViewHistory_west, java.awt.BorderLayout.LINE_START);

        ViewHistory_east.setBackground(new java.awt.Color(244, 243, 243));
        ViewHistory_east.setPreferredSize(new java.awt.Dimension(15, 638));

        javax.swing.GroupLayout ViewHistory_eastLayout = new javax.swing.GroupLayout(ViewHistory_east);
        ViewHistory_east.setLayout(ViewHistory_eastLayout);
        ViewHistory_eastLayout.setHorizontalGroup(
            ViewHistory_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        ViewHistory_eastLayout.setVerticalGroup(
            ViewHistory_eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 638, Short.MAX_VALUE)
        );

        ViewSalesDetails.add(ViewHistory_east, java.awt.BorderLayout.LINE_END);

        ViewHistory_south.setBackground(new java.awt.Color(244, 243, 243));
        ViewHistory_south.setPreferredSize(new java.awt.Dimension(944, 80));
        ViewHistory_south.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 20, 10));
        ViewSalesDetails.add(ViewHistory_south, java.awt.BorderLayout.SOUTH);

        ViewHistory_center_inner.setLayout(new java.awt.GridLayout(1, 0));

        ViewHistory_right.setPreferredSize(new java.awt.Dimension(614, 38));
        ViewHistory_right.setLayout(new java.awt.GridLayout(1, 0));

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
            .addGap(0, 1313, Short.MAX_VALUE)
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

        StockDetails_center1.setBackground(new java.awt.Color(255, 255, 255));
        StockDetails_center1.setLayout(new java.awt.BorderLayout());

        SalesDetails_left.setBackground(new java.awt.Color(255, 255, 255));
        SalesDetails_left.setLayout(new java.awt.GridLayout(7, 1, 0, 10));

        SelectedTransactioID.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        SelectedTransactioID.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SelectedTransactioID.setText("Selected Transaction ID");
        SelectedTransactioID.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        SalesDetails_left.add(SelectedTransactioID);

        lblselectedsalesID_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lblselectedsalesID_show.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblselectedsalesID_show.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        SalesDetails_left.add(lblselectedsalesID_show);

        TransactionDate_transactiondetails.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        TransactionDate_transactiondetails.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TransactionDate_transactiondetails.setText("Transaction Date");
        TransactionDate_transactiondetails.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        SalesDetails_left.add(TransactionDate_transactiondetails);

        lbltransactiondatesalesDetails_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lbltransactiondatesalesDetails_show.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        SalesDetails_left.add(lbltransactiondatesalesDetails_show);

        Cashier_transactiondetails.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        Cashier_transactiondetails.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Cashier_transactiondetails.setText("Cashier/User ID");
        Cashier_transactiondetails.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        SalesDetails_left.add(Cashier_transactiondetails);

        lblcashiersalesDetails_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lblcashiersalesDetails_show.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        SalesDetails_left.add(lblcashiersalesDetails_show);

        StockDetails_center1.add(SalesDetails_left, java.awt.BorderLayout.CENTER);

        SalesDetails_right.setBackground(new java.awt.Color(255, 255, 255));
        SalesDetails_right.setLayout(new java.awt.GridLayout(1, 0));

        lblnewtotalstock_show.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        lblnewtotalstock_show.setText("Items Sold");
        lblnewtotalstock_show.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        SalesDetails_right.add(lblnewtotalstock_show);

        StockDetails_center1.add(SalesDetails_right, java.awt.BorderLayout.SOUTH);

        Saledetails_north.add(StockDetails_center1);

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

        lblpaymentmethodTransactionDetails_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center6.add(lblpaymentmethodTransactionDetails_show);

        Status_transactiondetails.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N
        Status_transactiondetails.setText("Status");
        Status_transactiondetails.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center6.add(Status_transactiondetails);

        lblstatusTransactionDetails_show.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        lblstatusTransactionDetails_show.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        StockDetails_center6.add(lblstatusTransactionDetails_show);

        Restock_space2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout Restock_space2Layout = new javax.swing.GroupLayout(Restock_space2);
        Restock_space2.setLayout(Restock_space2Layout);
        Restock_space2Layout.setHorizontalGroup(
            Restock_space2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 656, Short.MAX_VALUE)
        );
        Restock_space2Layout.setVerticalGroup(
            Restock_space2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        StockDetails_center6.add(Restock_space2);

        lblnewtotalstock_show1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        StockDetails_center6.add(lblnewtotalstock_show1);

        Saledetails_north.add(StockDetails_center6);

        StockDetails_center5.add(Saledetails_north);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setPreferredSize(new java.awt.Dimension(537, 10));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1313, Short.MAX_VALUE)
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
            .addGap(0, 187, Short.MAX_VALUE)
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
        jScrollPane2.setViewportView(tbsaledetails);
        if (tbsaledetails.getColumnModel().getColumnCount() > 0) {
            tbsaledetails.getColumnModel().getColumn(0).setResizable(false);
            tbsaledetails.getColumnModel().getColumn(1).setResizable(false);
            tbsaledetails.getColumnModel().getColumn(2).setResizable(false);
            tbsaledetails.getColumnModel().getColumn(3).setResizable(false);
            tbsaledetails.getColumnModel().getColumn(4).setResizable(false);
        }

        ItemsSold_center.add(jScrollPane2);

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
            .addGap(0, 187, Short.MAX_VALUE)
        );

        ItemsSold_panel.add(jPanel10, java.awt.BorderLayout.EAST);

        StockDetails_center5.add(ItemsSold_panel);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(537, 10));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1313, Short.MAX_VALUE)
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

        btnvoidsale.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        btnvoidsale.setText("Void Transaction");
        btnvoidsale.setPreferredSize(new java.awt.Dimension(250, 50));
        btnvoidsale.addActionListener(this::btnvoidsaleActionPerformed);
        SaleSummary_buttons2.add(btnvoidsale);

        btnback_saledetails.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        btnback_saledetails.setText("Back");
        btnback_saledetails.setPreferredSize(new java.awt.Dimension(250, 50));
        btnback_saledetails.addActionListener(this::btnback_saledetailsActionPerformed);
        SaleSummary_buttons2.add(btnback_saledetails);

        StockDetails_center5.add(SaleSummary_buttons2);

        StockDetails_form5.add(StockDetails_center5, java.awt.BorderLayout.CENTER);

        SelectedDetails3.add(StockDetails_form5, java.awt.BorderLayout.CENTER);

        ViewHistory_right.add(SelectedDetails3);

        ViewHistory_center_inner.add(ViewHistory_right);

        ViewSalesDetails.add(ViewHistory_center_inner, java.awt.BorderLayout.CENTER);

        ViewHistory_north.setBackground(new java.awt.Color(244, 243, 243));

        javax.swing.GroupLayout ViewHistory_northLayout = new javax.swing.GroupLayout(ViewHistory_north);
        ViewHistory_north.setLayout(ViewHistory_northLayout);
        ViewHistory_northLayout.setHorizontalGroup(
            ViewHistory_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1343, Short.MAX_VALUE)
        );
        ViewHistory_northLayout.setVerticalGroup(
            ViewHistory_northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        ViewSalesDetails.add(ViewHistory_north, java.awt.BorderLayout.NORTH);

        Stocks.add(ViewSalesDetails, "Display_transactiondetails");

        Content_Panel.add(Stocks, "Display_sales");

        MainScroll.setViewportView(Content_Panel);

        Body.add(MainScroll, java.awt.BorderLayout.CENTER);

        Main_Panel.add(Body, java.awt.BorderLayout.CENTER);

        getContentPane().add(Main_Panel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnsaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsaleActionPerformed
        refreshSaleTable();
        
        cardLayout.show(Content_Panel, "Display_inventory");
    }//GEN-LAST:event_btnsaleActionPerformed

    private void btnviewsalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnviewsalesActionPerformed
        loadSalesHistory("");
        
        String keyword = txtsearch_saleshistory.getText().trim();
        loadSalesHistory(keyword);
        
        cardLayout.show(Content_Panel, "Display_sales");
    }//GEN-LAST:event_btnviewsalesActionPerformed

    private void btnlogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlogoutActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Do you want to Logout?", "Logging out", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION)
        {
            MainFrame login = new MainFrame(managestock, managebatch, managesupplier, managesale);
            login.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnlogoutActionPerformed

    private void btnsaveorder_addsaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsaveorder_addsaleActionPerformed
         DefaultTableModel model = (DefaultTableModel) tbsales_summary.getModel();

        // 1. Check if the order has any products
        if (model.getRowCount() == 0) 
        {
            JOptionPane.showMessageDialog(this, "Please add products to the order first.", "Empty Order",JOptionPane.WARNING_MESSAGE);
            return;
        }

        try 
        {
            // 2. Prepare the sale details
            ArrayList<SaleDetail> temporaryReceipt = new ArrayList<>();

            for (int i = 0; i < model.getRowCount(); i++) 
            {
                int productId = Integer.parseInt(model.getValueAt(i, 0).toString());

                double unitPrice = Double.parseDouble(model.getValueAt(i, 2).toString());

                double quantity = Double.parseDouble(model.getValueAt(i, 3).toString());

                temporaryReceipt.add(new SaleDetail(0, productId, quantity, unitPrice));
            }

            // 3. Confirm the order
            int choice = JOptionPane.showConfirmDialog(this, "Save this order and complete the transaction?", "Confirm Order", JOptionPane.YES_NO_OPTION);

            if (choice != JOptionPane.YES_OPTION) 
            {
                return;
            }
            
            //Add Payment Method and Validation
            Object paymentValue = cbpaymentmethod_addsale.getEditor().getItem();

            String paymentMethod = paymentValue == null ? "" : paymentValue.toString().trim();

            if (paymentMethod.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, "Please select or enter a payment method.", "Missing Payment Method", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            //Record Sale
            managesale.recordSale(temporaryReceipt, null, UserId, paymentMethod);

            // 5. Clear the summary after a successful save
            model.setRowCount(0);

            // 6. Refresh inventory and Payment Method
            refreshSaleTable();
            loadPaymentMethods();
            
            cbpaymentmethod_addsale.setSelectedItem(paymentMethod);
            updateSaleSummaryTotals();

            JOptionPane.showMessageDialog(this, "Order saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } 
        catch (Exception e) 
        {

            JOptionPane.showMessageDialog(this, "Unable to save order: " + e.getMessage(), "Transaction Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnsaveorder_addsaleActionPerformed

    private void btnclearorder_saleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnclearorder_saleActionPerformed
        DefaultTableModel summaryModel = (DefaultTableModel) tbsales_summary.getModel();


        if (summaryModel.getRowCount() == 0) 
        {
            JOptionPane.showMessageDialog(this, "The Order is Empty.", "Empty Order", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirm before clearing
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear the entire order?", "Confirm Clear Order", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) 
        {
            return;
        }

        summaryModel.setRowCount(0);

        txtquantityinput_addsale.setText("");
        txtdiscount_addsale.setText("");

        
        selectedProductId = -1;
        selectedAvailableStock = 0;
        selectedUnitPrice = 0;
        selectedProductName = "";

        updateSaleSummaryTotals();

        
        // Refresh the order table
        tbsales_summary.revalidate();
        tbsales_summary.repaint();

        JOptionPane.showMessageDialog(this, "Order Cleared Successfully!", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnclearorder_saleActionPerformed

    private void btnaddorder_addsaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddorder_addsaleActionPerformed

        
        if (selectedProductId == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a product first.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
                double quantity = Double.parseDouble(txtquantityinput_addsale.getText().trim());

            if (quantity <= 0) 
            {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.");
                return;
            }

            if (quantity > selectedAvailableStock) 
            {
                JOptionPane.showMessageDialog(this, "Insufficient stock. Available stock: " + selectedAvailableStock);
                return;
            }

            DefaultTableModel summaryModel = (DefaultTableModel) tbsales_summary.getModel();

            // Find the product if it already exists in the summary
            int existingRow = -1;

            for (int i = 0; i < summaryModel.getRowCount(); i++) 
            {

                Object idValue = summaryModel.getValueAt(i, 0);

                // Skip empty rows
                if (idValue == null || idValue.toString().trim().isEmpty()) {
                    continue;
                }

                int productId = Integer.parseInt(idValue.toString());

                if (productId == selectedProductId) {
                    existingRow = i;
                    break;
                }
            }

            if (existingRow != -1) 
            {

                // Read the existing quantity
                double oldQuantity = Double.parseDouble(summaryModel.getValueAt(existingRow, 3).toString());

                double newQuantity = oldQuantity + quantity;

                if (newQuantity > selectedAvailableStock) 
                {
                    JOptionPane.showMessageDialog(this, "Insufficient stock for the combined quantity. " + "Available stock: " + selectedAvailableStock);
                    return;
                }

                // Update quantity and subtotal
                double lineSubtotal = selectedUnitPrice * newQuantity;

                summaryModel.setValueAt(newQuantity, existingRow, 3);
                summaryModel.setValueAt(lineSubtotal, existingRow, 4);

            } 
            else 
            {

                // Calculate subtotal for the new product
                double lineSubtotal = selectedUnitPrice * quantity;

                // Add all FIVE columns
                summaryModel.addRow(new Object[] {selectedProductId, selectedProductName, selectedUnitPrice, quantity, lineSubtotal});
            }

            // DO NOT clear the table here!
            // Recalculate the subtotal and total labels
            updateSaleSummaryTotals();

            // Clear only the quantity input
            txtquantityinput_addsale.setText("");

            JOptionPane.showMessageDialog(this, "Product added to order successfully!");

        } 
        catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity.", "Invalid Quantity",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnaddorder_addsaleActionPerformed

    private void btnvoidsaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvoidsaleActionPerformed
        int saleId;

        try 
        {
            saleId = Integer.parseInt(lblselectedsalesID_show.getText().trim());
        } 
        catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, "Please select a valid transaction first.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Void transaction #" + saleId + " and restore its stock?","Confirm Void", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) 
        {
            return;
        }

        try 
        {
            managesale.voidSale(saleId);

            // Refresh the transaction list and inventory
            loadSalesHistory("");
            refreshSaleTable();

            JOptionPane.showMessageDialog(this, "Transaction voided successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } 
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(this,"Unable to void transaction: " + e.getMessage(),"Void Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnvoidsaleActionPerformed

    private void btnback_saledetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnback_saledetailsActionPerformed
        loadSalesHistory("");
        
        salesHistoryCardLayout.show(Stocks, "Display_saleshistory");
    }//GEN-LAST:event_btnback_saledetailsActionPerformed

    private void btnviewsaledetails_saleshistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnviewsaledetails_saleshistoryActionPerformed
         int selectedRow = tbsaleshistory .getSelectedRow();

        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Select a Row First!", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        showSelectedSaleDetails();
        
        salesHistoryCardLayout.show(Stocks, "Display_transactiondetails");
    }//GEN-LAST:event_btnviewsaledetails_saleshistoryActionPerformed
    
    
    
    private void loadPaymentMethods() 
    {

        javax.swing.DefaultComboBoxModel<String> model =
                new javax.swing.DefaultComboBoxModel<>();

        // Common payment methods
        model.addElement("Cash");
        model.addElement("GCash");
        model.addElement("Maya");
        model.addElement("Card");
        model.addElement("Bank Transfer");

        try {
            // Add payment methods already saved in the database
            for (String method : managesale.getPaymentMethods()) {

                boolean exists = false;

                for (int i = 0; i < model.getSize(); i++) {
                    if (model.getElementAt(i).equalsIgnoreCase(method)) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    model.addElement(method);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load payment methods: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        cbpaymentmethod_addsale.setModel(model);
        cbpaymentmethod_addsale.setEditable(true);
        cbpaymentmethod_addsale.setSelectedItem("Cash");
    }
    
    
    
    private void loadSalesHistory(String searchText) 
    {

        DefaultTableModel model = (DefaultTableModel) tbsaleshistory.getModel();

        // Clear previous rows
        model.setRowCount(0);

        try 
        {
            // Retrieve saved sales from SaleService
            ArrayList<Sale> sales = managesale.getSales();

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a");

            String keyword = searchText == null ? "" : searchText.trim().toLowerCase();

            for (Sale sale : sales) 
            {
                String saleId = String.valueOf(sale.getId());

                String userId = String.valueOf(sale.getUserId());

                String saleDate = sale.getSaleDate() == null ? "" : dateFormat.format(sale.getSaleDate());

                String paymentMethod = String.valueOf(sale.getPaymentMethod());

                String totalAmount = String.format("₱%,.2f",sale.getTotalAmount());

                String status = String.valueOf(sale.getStatus());

                // Search across the transaction fields
                String searchableText = (saleId + " " + userId + " " + saleDate + " " + paymentMethod + " " + totalAmount + " " + status).toLowerCase();

                if (keyword.isEmpty() || searchableText.contains(keyword)) 
                {
                    model.addRow(new Object[] {sale.getId(), sale.getUserId(), saleDate,sale.getPaymentMethod(), totalAmount,sale.getStatus()});
                }
            }
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(this, "Unable to load sales history: " + e.getMessage(), "Database Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void showSelectedProductDetails() 
    {

        int selectedRow = tbinventory_view.getSelectedRow();

        if (selectedRow == -1) 
        {
            return;
        }

        // Convert selected row in case the table is sorted
        int row = tbinventory_view.convertRowIndexToModel(selectedRow);

        DefaultTableModel model = (DefaultTableModel) tbinventory_view.getModel();

        selectedProductId = Integer.parseInt(model.getValueAt(row, 0).toString());

        selectedProductName = model.getValueAt(row, 1).toString();

        String brand = model.getValueAt(row, 2).toString();

        String category = model.getValueAt(row, 3).toString();

        selectedAvailableStock = Double.parseDouble(model.getValueAt(row, 4).toString());

        selectedUnitPrice = Double.parseDouble(model.getValueAt(row, 5).toString().replace("₱", "").replace(",", "").trim());

        String expiration = model.getValueAt(row, 6).toString();

        // Display information in your Product Details panel
        lblproductIdAddSale_show.setText(String.valueOf(selectedProductId));

        lblproductnameAddSale_show.setText(selectedProductName);
        lblbrandAddSale_show.setText(brand);
        lblcategoryAddSale_show.setText(category);

        lblavailablestockAddSale_show.setText(String.valueOf(selectedAvailableStock));

        lblpriceAddSale_show.setText(String.format("₱%.2f", selectedUnitPrice));

        lblexpiryAddSale_show.setText(expiration);
    }
    
    
    
    private void showSelectedSaleDetails() 
    {
        // 1. Check whether a transaction is selected
        int selectedRow = tbsaleshistory.getSelectedRow();

        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a transaction first.", "No Transaction Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try 
        {
            // Support sorted tables
            int row = tbsaleshistory.convertRowIndexToModel(selectedRow);

            DefaultTableModel historyModel = (DefaultTableModel) tbsaleshistory.getModel();

            // 2. Get the selected transaction's information
            int saleId = Integer.parseInt(historyModel.getValueAt(row, 0).toString());

            String userId = historyModel.getValueAt(row, 1).toString();

            String saleDate =
                    historyModel.getValueAt(row, 2).toString();

            String paymentMethod = historyModel.getValueAt(row, 3).toString();

            String totalAmount = historyModel.getValueAt(row, 4).toString();

            String status = historyModel.getValueAt(row, 5).toString();

            // 3. Display the transaction information
            lblselectedsalesID_show.setText(String.valueOf(saleId));

            lbltransactiondatesalesDetails_show.setText(saleDate);
            lblcashiersalesDetails_show.setText(userId);
            lblpaymentmethodTransactionDetails_show.setText(paymentMethod);
            lblstatusTransactionDetails_show.setText(status);
            lbltotal_salesdetails.setText(totalAmount);

            // 4. Clear previous transaction items
            DefaultTableModel detailsModel = (DefaultTableModel) tbsaledetails.getModel();

            detailsModel.setRowCount(0);

            // 5. Retrieve items belonging to this transaction
            ArrayList<Object[]> details = managesale.getSaleDetails(saleId);

            for (Object[] item : details) 
            {

                detailsModel.addRow(new Object[] 
                {
                    item[0],                              // Product ID
                    item[1],                              // Product Name
                    item[2],                              // Quantity
                    String.format("₱%,.2f",
                            ((Number) item[3]).doubleValue()), // Unit Price
                    String.format("₱%,.2f",
                            ((Number) item[4]).doubleValue())  // Subtotal
                });
            }

            // 6. Refresh the table
            tbsaledetails.revalidate();
            tbsaledetails.repaint();

        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(this, "Unable to display sales details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    private void updateSaleSummaryTotals() 
    {

        DefaultTableModel model = (DefaultTableModel) tbsales_summary.getModel();

        double subtotal = 0.0;

        // Calculate each product's subtotal and the order subtotal
        for (int i = 0; i < model.getRowCount(); i++) 
        {

            double unitPrice = Double.parseDouble(model.getValueAt(i, 2).toString());

            double quantity = Double.parseDouble(model.getValueAt(i, 3).toString());

            double lineSubtotal = unitPrice * quantity;

            // Update the Subtotal column in the table
            model.setValueAt(lineSubtotal, i, 4);

            subtotal += lineSubtotal;
        }

        // Display subtotal BEFORE discount
        lblsubtotal_addsale.setText(String.format("₱%,.2f", subtotal));

        // Read discount percentage
        String discountText = txtdiscount_addsale.getText().trim();

        double discount = 0.0;

        if (!discountText.isEmpty()) 
        {
            try 
            {
                discount = Double.parseDouble(discountText);
            } 
            catch (NumberFormatException e) 
            {
                JOptionPane.showMessageDialog(this, "Please enter a valid discount percentage.");
                return;
            }
        }

        // Validate discount percentage
        if (discount < 0 || discount > 100) 
        {
            JOptionPane.showMessageDialog(this, "Discount must be between 0% and 100%.", "Invalid Discount", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Calculate discount in pesos
        double discountAmount = subtotal * (discount / 100.0);

        // Calculate final total
        double total = subtotal - discountAmount;

        // Display final total
        lbltotalAddSale_show.setText(String.format("₱%,.2f", total));

        // Display the logged-in user ID
        lbluseridAddSale_show.setText(String.valueOf(UserId));

        model.fireTableDataChanged();
    }
    
    
    //Refresh Staff Product Table
    private void refreshSaleTable() 
    {
        try 
        {
            DefaultTableModel table = (DefaultTableModel) tbinventory_view.getModel();

            table.setRowCount(0);

            ArrayList<Object[]> inventory = managesale.getSaleableInventoryData();

            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM-dd-yyyy");

            for (Object[] row : inventory) 
            {
                String price = String.format("₱%.2f", ((Number) row[6]).doubleValue());

                String expirationText = "N/A";

                if (row[7] != null) 
                {
                    expirationText = dateFormat.format((java.sql.Date) row[7]);
                }

                table.addRow(new Object[] 
                {
                    row[0],           // Product ID
                    row[1],           // Product Name
                    row[2],           // Brand
                    row[3],           // Category
                    row[4],           // Saleable Stock
                    price,            // Unit Price
                    expirationText    // Earliest Expiration
                });
            }
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(this, "Unable to load saleable products: " + e.getMessage(), "Database Error",JOptionPane.ERROR_MESSAGE);
        }
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
    private javax.swing.JPanel AddSaleInventory;
    private javax.swing.JPanel AddSale_center;
    private javax.swing.JPanel AddSale_centerseparator;
    private javax.swing.JPanel AddSale_east;
    private javax.swing.JPanel AddSale_east1;
    private javax.swing.JPanel AddSale_left;
    private javax.swing.JPanel AddSale_north;
    private javax.swing.JPanel AddSale_north2;
    private javax.swing.JPanel AddSale_right;
    private javax.swing.JPanel AddSale_separator;
    private javax.swing.JPanel AddSale_south;
    private javax.swing.JPanel AddSale_south1;
    private javax.swing.JPanel AddSale_tab;
    private javax.swing.JPanel AddSale_west;
    private javax.swing.JPanel AddSale_west1;
    private javax.swing.JLabel Add_Inventory_title5;
    private javax.swing.JLabel Add_Inventory_title6;
    private javax.swing.JLabel Add_Inventory_title7;
    private javax.swing.JLabel Add_Inventory_title8;
    private javax.swing.JPanel Adminicon_north;
    private javax.swing.JPanel Adminicon_south;
    private javax.swing.JLabel AvailableStockAddSale;
    private javax.swing.JPanel Body;
    private javax.swing.JLabel BrandAddSale;
    private javax.swing.JLabel Cashier_transactiondetails;
    private javax.swing.JLabel CategoryAddSale;
    private javax.swing.JPanel Content_Panel;
    private javax.swing.JLabel Discount_addsale;
    private javax.swing.JLabel ExpiryAddSale;
    private javax.swing.JPanel ItemsSold_center;
    private javax.swing.JPanel ItemsSold_panel;
    private javax.swing.JPanel Logout_north;
    private javax.swing.JPanel Logout_south;
    private javax.swing.JScrollPane MainScroll;
    private javax.swing.JPanel Main_Panel;
    private javax.swing.JPanel Nav_center;
    private javax.swing.JPanel Nav_north;
    private javax.swing.JPanel Nav_south;
    private javax.swing.JPanel Navigation_Panel;
    private javax.swing.JLabel PaymentMethod_addsale;
    private javax.swing.JLabel PaymentMethod_transactiondetails;
    private javax.swing.JLabel PriceAddSale;
    private javax.swing.JLabel ProductIdAddSale;
    private javax.swing.JLabel ProductNameAddSale;
    private javax.swing.JLabel Quantity_AddSale;
    private javax.swing.JLabel Quantity_AddSale_space;
    private javax.swing.JPanel Restock_space2;
    private javax.swing.JPanel SaleCard;
    private javax.swing.JPanel SaleContent;
    private javax.swing.JPanel SaleHistoryButtons;
    private javax.swing.JPanel SaleHistoryCard;
    private javax.swing.JPanel SaleHistoryContent;
    private javax.swing.JPanel SaleHistoryInfo;
    private javax.swing.JPanel SaleHistoryInventory;
    private javax.swing.JPanel SaleHistorySelectedDetails;
    private javax.swing.JPanel SaleHistory_right;
    private javax.swing.JPanel SaleHistorylistPanel;
    private javax.swing.JPanel SaleInfo;
    private javax.swing.JPanel SaleSearch;
    private javax.swing.JPanel SaleSummary_buttons;
    private javax.swing.JPanel SaleSummary_buttons2;
    private javax.swing.JPanel Saledetails_north;
    private javax.swing.JPanel SalelistPanel;
    private javax.swing.JPanel Sales;
    private javax.swing.JPanel SalesDetails_left;
    private javax.swing.JPanel SalesDetails_right;
    private javax.swing.JPanel SelectedDetails;
    private javax.swing.JPanel SelectedDetails1;
    private javax.swing.JPanel SelectedDetails3;
    private javax.swing.JLabel SelectedTransactioID;
    private javax.swing.JLabel Status_transactiondetails;
    private javax.swing.JPanel StockDetails_center1;
    private javax.swing.JPanel StockDetails_center2;
    private javax.swing.JPanel StockDetails_center3;
    private javax.swing.JPanel StockDetails_center4;
    private javax.swing.JPanel StockDetails_center5;
    private javax.swing.JPanel StockDetails_center6;
    private javax.swing.JPanel StockDetails_east2;
    private javax.swing.JPanel StockDetails_form2;
    private javax.swing.JPanel StockDetails_form3;
    private javax.swing.JPanel StockDetails_form4;
    private javax.swing.JPanel StockDetails_form5;
    private javax.swing.JPanel StockDetails_north2;
    private javax.swing.JPanel StockDetails_north3;
    private javax.swing.JPanel StockDetails_north4;
    private javax.swing.JPanel StockDetails_north5;
    private javax.swing.JPanel StockDetails_south2;
    private javax.swing.JPanel StockDetails_title2;
    private javax.swing.JPanel StockDetails_title3;
    private javax.swing.JPanel StockDetails_title4;
    private javax.swing.JPanel StockDetails_title5;
    private javax.swing.JPanel StockDetails_west2;
    private javax.swing.JPanel Stocks;
    private javax.swing.JLabel Subtotal_addsale;
    private javax.swing.JLabel Supplier_Name2;
    private javax.swing.JLabel Total_SaleDetails;
    private javax.swing.JLabel Total_addsale;
    private javax.swing.JPanel Total_panel;
    private javax.swing.JPanel Total_panel1;
    private javax.swing.JPanel Total_transactiondetails;
    private javax.swing.JLabel TransactionDate_transactiondetails;
    private javax.swing.JPanel TransactionHistory_header;
    private javax.swing.JLabel User_addsale;
    private javax.swing.JPanel ViewHistory_center;
    private javax.swing.JPanel ViewHistory_center_inner;
    private javax.swing.JPanel ViewHistory_east;
    private javax.swing.JPanel ViewHistory_north;
    private javax.swing.JPanel ViewHistory_right;
    private javax.swing.JPanel ViewHistory_south;
    private javax.swing.JPanel ViewHistory_west;
    private javax.swing.JPanel ViewSales;
    private javax.swing.JPanel ViewSalesDetails;
    private javax.swing.JButton btnaddorder_addsale;
    private javax.swing.JButton btnback_saledetails;
    private javax.swing.JButton btnclearorder_sale;
    private javax.swing.JButton btnlogout;
    private javax.swing.JButton btnsale;
    private javax.swing.JButton btnsaveorder_addsale;
    private javax.swing.JButton btnsearch_transactionhistory;
    private javax.swing.JButton btnviewsaledetails_saleshistory;
    private javax.swing.JButton btnviewsales;
    private javax.swing.JButton btnvoidsale;
    private javax.swing.JComboBox<String> cbpaymentmethod_addsale;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel lblavailablestockAddSale_show;
    private javax.swing.JLabel lblbrandAddSale_show;
    private javax.swing.JLabel lblcashiersalesDetails_show;
    private javax.swing.JLabel lblcategoryAddSale_show;
    private javax.swing.JLabel lblcurrentstock_show1;
    private javax.swing.JLabel lblexpiryAddSale_show;
    private javax.swing.JLabel lblnewtotalstock_show;
    private javax.swing.JLabel lblnewtotalstock_show1;
    private javax.swing.JLabel lblpaymentmethodTransactionDetails_show;
    private javax.swing.JLabel lblpriceAddSale_show;
    private javax.swing.JLabel lblproductIdAddSale_show;
    private javax.swing.JLabel lblproductnameAddSale_show;
    private javax.swing.JLabel lblselectedsalesID_show;
    private javax.swing.JLabel lblstatusTransactionDetails_show;
    private javax.swing.JLabel lblsubtotal_addsale;
    private javax.swing.JLabel lbltotalAddSale_show;
    private javax.swing.JLabel lbltotal_salesdetails;
    private javax.swing.JLabel lbltransactiondatesalesDetails_show;
    private javax.swing.JLabel lbluseridAddSale_show;
    private javax.swing.JTable tbinventory_view;
    private javax.swing.JTable tbsaledetails;
    private javax.swing.JTable tbsales_summary;
    private javax.swing.JTable tbsaleshistory;
    private javax.swing.JButton tbsearch_sales;
    private javax.swing.JTextField txtdiscount_addsale;
    private javax.swing.JTextField txtquantityinput_addsale;
    private javax.swing.JTextField txtsearch_sales;
    private javax.swing.JTextField txtsearch_saleshistory;
    // End of variables declaration//GEN-END:variables
}
