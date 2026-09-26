import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * WarmindoApp - Aplikasi GUI Kasir Custom Racikan Mie & Topping
 * "Warmindo Digital Express"
 */
public class WarmindoApp extends JFrame {

    // Palet Warna UI Modern Khas Warmindo
    private static final Color WARNA_HEADER = new Color(180, 83, 9);       // Deep Warm Amber
    private static final Color WARNA_HEADER_TEXT = Color.WHITE;
    private static final Color WARNA_AKSEN = new Color(234, 88, 12);        // Vibrant Orange
    private static final Color WARNA_BG = new Color(248, 250, 252);         // Off-White Slate
    private static final Color WARNA_CARD = Color.WHITE;
    private static final Color WARNA_BORDER = new Color(226, 232, 240);
    private static final Color WARNA_TEKS_UTAMA = new Color(30, 41, 59);
    private static final Color WARNA_TOTAL_BG = new Color(254, 243, 199);   // Light Amber Highlight
    private static final Color WARNA_TOTAL_TEKS = new Color(146, 64, 14);

    private static final Locale LOCALE_ID = new Locale("id", "ID");
    private static final NumberFormat FORMAT_RUPIAH = NumberFormat.getCurrencyInstance(LOCALE_ID);

    // Komponen Form Input Racikan
    private JTextField txtIdPesanan;
    private JTextField txtNamaPelanggan;
    private JComboBox<VarianMie> cbVarianMie;
    private JLabel lblJenisMie;
    private JSlider sliderPedas;
    private JLabel lblPedasInfo;
    private List<JCheckBox> listCbTopping;
    private List<Topping> masterTopping;

    // Komponen Kalkulasi Dinamis
    private JLabel lblRincianMie;
    private JLabel lblRincianTopping;
    private JLabel lblTotalBiaya;

    // Komponen Panel Kanan (Tabs)
    private JTabbedPane tabbedPane;
    private JTextArea txtStruk;
    private JTable tabelRiwayat;
    private DefaultTableModel modelTabel;
    private JLabel lblRingkasanTransaksi;
    private JLabel lblRingkasanOmset;
    private JPanel panelBestSeller;

    public WarmindoApp() {
        super("Warmindo Digital Express — Kasir Custom Racikan Mie & Topping");
        initDataMaster();
        initComponents();
        hitungKalkulasiDinamis();
        refreshTabelDanStatistik();
    }

    /**
     * Inisialisasi daftar menu mie dan pilihan topping
     */
    private void initDataMaster() {
        // Master Topping sesuai study case
        masterTopping = new ArrayList<>();
        masterTopping.add(new Topping("Telur Ceplok (Setengah Matang)", 3500));
        masterTopping.add(new Topping("Telur Dadar Gurih", 4000));
        masterTopping.add(new Topping("Kornet Sapi Tumis", 4500));
        masterTopping.add(new Topping("Sosis Bakar / Goreng", 3500));
        masterTopping.add(new Topping("Keju Cheddar Parut Melimpah", 3500));
        masterTopping.add(new Topping("Bakso Sapi Kuah (3 pcs)", 4000));
        masterTopping.add(new Topping("Pangsit Goreng Renyah", 2500));
        masterTopping.add(new Topping("Sambal Matah Spesial", 2500));
        masterTopping.add(new Topping("Sayur Sawi & Kol Segar", 1500));
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setMinimumSize(new Dimension(980, 680));
        setLocationRelativeTo(null);
        getContentPane().setBackground(WARNA_BG);
        setLayout(new BorderLayout());

        // 1. Header Banner
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Konten Utama (Split: Kiri Formulir Racik, Kanan Tab Output & Analitik)
        JPanel panelKiri = createPanelFormRacik();
        JPanel panelKanan = createPanelOutputDanStatistik();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelKiri, panelKanan);
        splitPane.setDividerLocation(520);
        splitPane.setResizeWeight(0.5);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);

        // 3. Status Bar Bawah
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    /**
     * Banner Header Aplikasi
     */
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WARNA_HEADER);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setOpaque(false);

        JLabel lblTitle = new JLabel("🍜 WARMINDO DIGITAL EXPRESS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(WARNA_HEADER_TEXT);

        JLabel lblSubtitle = new JLabel("Sistem Kasir Custom Racikan Mie, Level Cabai Rawit & Multi-Topping");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(254, 240, 138));

        titleBox.add(lblTitle);
        titleBox.add(Box.createVerticalStrut(3));
        titleBox.add(lblSubtitle);

        header.add(titleBox, BorderLayout.WEST);

        // Badge Status File CSV
        JLabel lblStatus = new JLabel("● Log: log_warmindo.csv Terkoneksi ");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(new Color(220, 252, 231));
        header.add(lblStatus, BorderLayout.EAST);

        return header;
    }

    /**
     * Panel Kiri: Formulir Racik Pesanan Custom
     */
    private JPanel createPanelFormRacik() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(WARNA_BG);
        container.setBorder(new EmptyBorder(12, 12, 12, 6));

        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setBackground(WARNA_BG);

        // Card 1: Data Pelanggan & No. Pesanan
        JPanel cardPelanggan = createCard("1. Data Pelanggan");
        cardPelanggan.setLayout(new GridLayout(2, 2, 8, 8));

        JLabel lblId = new JLabel("ID Pesanan:");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtIdPesanan = new JTextField(generateNomorPesanan());
        txtIdPesanan.setEditable(false);
        txtIdPesanan.setBackground(new Color(241, 245, 249));

        JLabel lblNama = new JLabel("Nama Pelanggan / No. Meja:");
        lblNama.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtNamaPelanggan = new JTextField("Pelanggan 1");

        cardPelanggan.add(lblId);
        cardPelanggan.add(txtIdPesanan);
        cardPelanggan.add(lblNama);
        cardPelanggan.add(txtNamaPelanggan);
        formContent.add(cardPelanggan);
        formContent.add(Box.createVerticalStrut(10));

        // Card 2: Pilihan Varian Mie Utama (OOP Class VarianMie)
        JPanel cardMie = createCard("2. Pilihan Varian Mie Utama");
        cardMie.setLayout(new BorderLayout(8, 8));

        VarianMie[] daftarMie = new VarianMie[] {
            new VarianMie("Indomie Goreng Original", "Goreng", 8000),
            new VarianMie("Indomie Goreng Aceh Pedas Mantap", "Goreng", 9000),
            new VarianMie("Indomie Goreng Rendang", "Goreng", 9000),
            new VarianMie("Indomie Goreng Ayam Geprek", "Goreng", 9000),
            new VarianMie("Indomie Kuah Ayam Bawang", "Kuah", 8000),
            new VarianMie("Indomie Kuah Kari Ayam Spesial", "Kuah", 8500),
            new VarianMie("Indomie Kuah Soto Mie Lamongan", "Kuah", 8500),
            new VarianMie("Mie Nyemek Warmindo Spesial", "Kuah Nyemek", 10000)
        };

        cbVarianMie = new JComboBox<>(daftarMie);
        cbVarianMie.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbVarianMie.addActionListener(e -> {
            updateInfoJenisMie();
            hitungKalkulasiDinamis();
        });

        lblJenisMie = new JLabel();
        lblJenisMie.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblJenisMie.setForeground(WARNA_AKSEN);
        updateInfoJenisMie();

        cardMie.add(cbVarianMie, BorderLayout.CENTER);
        cardMie.add(lblJenisMie, BorderLayout.SOUTH);
        formContent.add(cardMie);
        formContent.add(Box.createVerticalStrut(10));

        // Card 3: Level Kepedasan Cabai Rawit (Slider 0-5)
        JPanel cardPedas = createCard("3. Level Kepedasan Cabai Rawit (0 - 5)");
        cardPedas.setLayout(new BorderLayout(6, 6));

        sliderPedas = new JSlider(0, 5, 1);
        sliderPedas.setMajorTickSpacing(1);
        sliderPedas.setPaintTicks(true);
        sliderPedas.setPaintLabels(true);
        sliderPedas.setBackground(WARNA_CARD);
        sliderPedas.setFont(new Font("Segoe UI", Font.BOLD, 11));

        lblPedasInfo = new JLabel();
        lblPedasInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPedasInfo.setHorizontalAlignment(SwingConstants.CENTER);
        lblPedasInfo.setForeground(new Color(194, 65, 12));
        updateLabelPedas(sliderPedas.getValue());

        sliderPedas.addChangeListener(e -> {
            updateLabelPedas(sliderPedas.getValue());
            hitungKalkulasiDinamis();
        });

        cardPedas.add(sliderPedas, BorderLayout.CENTER);
        cardPedas.add(lblPedasInfo, BorderLayout.SOUTH);
        formContent.add(cardPedas);
        formContent.add(Box.createVerticalStrut(10));

        // Card 4: Multi-Topping Checkbox (OOP Class Topping)
        JPanel cardTopping = createCard("4. Racikan Multi-Topping (Bisa Pilih Banyak)");
        cardTopping.setLayout(new GridLayout(0, 1, 4, 4));

        listCbTopping = new ArrayList<>();
        for (Topping topping : masterTopping) {
            String label = String.format("<html><b>%s</b> &nbsp;<font color='#b45309'>(+Rp %,.0f)</font></html>",
                    topping.getNamaTopping(), topping.getHargaTopping());
            JCheckBox cb = new JCheckBox(label);
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            cb.setBackground(WARNA_CARD);
            cb.addItemListener(e -> hitungKalkulasiDinamis());
            listCbTopping.add(cb);
            cardTopping.add(cb);
        }
        formContent.add(cardTopping);
        formContent.add(Box.createVerticalStrut(10));

        // Card 5: Kalkulasi Dinamis Penambahan Biaya Topping (Live Preview Biaya)
        JPanel cardKalkulasi = createCard("5. Kalkulasi Dinamis Biaya Pesanan");
        cardKalkulasi.setLayout(new BorderLayout(8, 8));
        cardKalkulasi.setBackground(WARNA_TOTAL_BG);

        JPanel panelDetail = new JPanel(new GridLayout(2, 1, 4, 4));
        panelDetail.setOpaque(false);

        lblRincianMie = new JLabel("Harga Mie Dasar: Rp 0");
        lblRincianMie.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        lblRincianTopping = new JLabel("Biaya Tambahan Topping: Rp 0 (0 topping)");
        lblRincianTopping.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        panelDetail.add(lblRincianMie);
        panelDetail.add(lblRincianTopping);

        lblTotalBiaya = new JLabel("TOTAL: Rp 0");
        lblTotalBiaya.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotalBiaya.setForeground(WARNA_TOTAL_TEKS);
        lblTotalBiaya.setHorizontalAlignment(SwingConstants.RIGHT);

        cardKalkulasi.add(panelDetail, BorderLayout.CENTER);
        cardKalkulasi.add(lblTotalBiaya, BorderLayout.EAST);
        formContent.add(cardKalkulasi);
        formContent.add(Box.createVerticalStrut(12));

        // Tombol Aksi (Simpan Pesanan & Reset)
        JPanel panelTombol = new JPanel(new GridLayout(1, 2, 10, 0));
        panelTombol.setOpaque(false);

        JButton btnSimpan = new JButton("💳 Simpan & Cetak Pesanan");
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSimpan.setBackground(new Color(22, 163, 74)); // Forest Green
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimpan.setPreferredSize(new Dimension(0, 42));
        btnSimpan.addActionListener(e -> prosesSimpanPesanan());

        JButton btnReset = new JButton("🔄 Reset Racikan");
        btnReset.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnReset.setBackground(new Color(241, 245, 249));
        btnReset.setForeground(WARNA_TEKS_UTAMA);
        btnReset.setFocusPainted(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(e -> resetForm());

        panelTombol.add(btnReset);
        panelTombol.add(btnSimpan);
        formContent.add(panelTombol);

        JScrollPane scrollPane = new JScrollPane(formContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        container.add(scrollPane, BorderLayout.CENTER);

        return container;
    }

    /**
     * Panel Kanan: Struk Transaksi, Riwayat CSV, dan Statistik Best Seller
     */
    private JPanel createPanelOutputDanStatistik() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(WARNA_BG);
        container.setBorder(new EmptyBorder(12, 6, 12, 12));

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Tab 1: Struk Pesanan
        JPanel tabStruk = new JPanel(new BorderLayout(8, 8));
        tabStruk.setBackground(WARNA_BG);
        tabStruk.setBorder(new EmptyBorder(10, 10, 10, 10));

        txtStruk = new JTextArea();
        txtStruk.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtStruk.setEditable(false);
        txtStruk.setBackground(new Color(254, 252, 232)); // Light cream paper
        txtStruk.setText("Belum ada transaksi yang diproses.\nSilakan racik pesanan di panel kiri dan klik 'Simpan & Cetak Pesanan'.");

        JScrollPane scrollStruk = new JScrollPane(txtStruk);
        scrollStruk.setBorder(BorderFactory.createLineBorder(WARNA_BORDER));

        JPanel panelBtnStruk = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtnStruk.setOpaque(false);

        JButton btnCopyStruk = new JButton("📋 Salin Teks Struk");
        btnCopyStruk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCopyStruk.addActionListener(e -> {
            String text = txtStruk.getText();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
            JOptionPane.showMessageDialog(this, "Teks struk berhasil disalin ke clipboard!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        });

        panelBtnStruk.add(btnCopyStruk);

        tabStruk.add(scrollStruk, BorderLayout.CENTER);
        tabStruk.add(panelBtnStruk, BorderLayout.SOUTH);
        tabbedPane.addTab("🧾 Struk Kasir", tabStruk);

        // Tab 2: Riwayat Log CSV
        JPanel tabRiwayat = new JPanel(new BorderLayout(8, 8));
        tabRiwayat.setBackground(WARNA_BG);
        tabRiwayat.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Summary Card di atas tabel
        JPanel panelSummary = new JPanel(new GridLayout(1, 2, 10, 10));
        panelSummary.setOpaque(false);

        JPanel cardTrx = createCardSimple("Total Pesanan Tercatat");
        lblRingkasanTransaksi = new JLabel("0 Pesanan", SwingConstants.CENTER);
        lblRingkasanTransaksi.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRingkasanTransaksi.setForeground(WARNA_AKSEN);
        cardTrx.add(lblRingkasanTransaksi, BorderLayout.CENTER);

        JPanel cardOmset = createCardSimple("Total Pendapatan (Omset)");
        lblRingkasanOmset = new JLabel("Rp 0", SwingConstants.CENTER);
        lblRingkasanOmset.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRingkasanOmset.setForeground(new Color(22, 163, 74));
        cardOmset.add(lblRingkasanOmset, BorderLayout.CENTER);

        panelSummary.add(cardTrx);
        panelSummary.add(cardOmset);

        // Tabel Data CSV
        String[] kolom = {"ID", "Waktu", "Pelanggan", "Mie", "Jenis", "Pedas", "Toppings", "Biaya Mie", "Biaya Topping", "Total"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelRiwayat = new JTable(modelTabel);
        tabelRiwayat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelRiwayat.setRowHeight(24);
        tabelRiwayat.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelRiwayat.getTableHeader().setBackground(new Color(241, 245, 249));

        // Center alignment untuk beberapa kolom
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabelRiwayat.getColumnModel().getColumn(0).setPreferredWidth(75);
        tabelRiwayat.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tabelRiwayat.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        JScrollPane scrollTabel = new JScrollPane(tabelRiwayat);
        scrollTabel.setBorder(BorderFactory.createLineBorder(WARNA_BORDER));

        JPanel panelBtnRiwayat = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtnRiwayat.setOpaque(false);

        JButton btnMuatUlang = new JButton("🔄 Muat Ulang CSV");
        btnMuatUlang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnMuatUlang.addActionListener(e -> refreshTabelDanStatistik());

        JButton btnBukaCsv = new JButton("📂 Cek Lokasi CSV");
        btnBukaCsv.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnBukaCsv.addActionListener(e -> {
            File f = new File(WarmindoDataManager.FILE_CSV);
            JOptionPane.showMessageDialog(this, 
                "File tersimpan di:\n" + f.getAbsolutePath() + "\nUkuran: " + f.length() + " bytes",
                "Lokasi Data Persistence", JOptionPane.INFORMATION_MESSAGE);
        });

        panelBtnRiwayat.add(btnMuatUlang);
        panelBtnRiwayat.add(btnBukaCsv);

        tabRiwayat.add(panelSummary, BorderLayout.NORTH);
        tabRiwayat.add(scrollTabel, BorderLayout.CENTER);
        tabRiwayat.add(panelBtnRiwayat, BorderLayout.SOUTH);
        tabbedPane.addTab("📊 Riwayat Log (CSV)", tabRiwayat);

        // Tab 3: Analisis Topping Paling Favorit (Best Seller)
        JPanel tabBestSeller = new JPanel(new BorderLayout(10, 10));
        tabBestSeller.setBackground(WARNA_BG);
        tabBestSeller.setBorder(new EmptyBorder(10, 10, 10, 10));

        panelBestSeller = new JPanel();
        panelBestSeller.setLayout(new BoxLayout(panelBestSeller, BoxLayout.Y_AXIS));
        panelBestSeller.setBackground(WARNA_BG);

        JScrollPane scrollBestSeller = new JScrollPane(panelBestSeller);
        scrollBestSeller.setBorder(null);
        scrollBestSeller.getVerticalScrollBar().setUnitIncrement(16);

        tabBestSeller.add(scrollBestSeller, BorderLayout.CENTER);
        tabbedPane.addTab("⭐ Best Seller Topping", tabBestSeller);

        container.add(tabbedPane, BorderLayout.CENTER);
        return container;
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(241, 245, 249));
        footer.setBorder(new EmptyBorder(6, 16, 6, 16));

        JLabel lblCopyright = new JLabel("Warmindo Digital Express v1.0 • Java Swing OOP Architecture");
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCopyright.setForeground(new Color(100, 116, 139));

        JLabel lblPetunjuk = new JLabel("Pilih varian, cabai, dan centang topping untuk kalkulasi otomatis");
        lblPetunjuk.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblPetunjuk.setForeground(new Color(100, 116, 139));

        footer.add(lblCopyright, BorderLayout.WEST);
        footer.add(lblPetunjuk, BorderLayout.EAST);
        return footer;
    }

    /**
     * Menghitung secara dinamis penambahan biaya topping dan total harga
     */
    private void hitungKalkulasiDinamis() {
        VarianMie mie = (VarianMie) cbVarianMie.getSelectedItem();
        double hargaMie = (mie != null) ? mie.getHargaDasar() : 0.0;

        List<Topping> toppingDipilih = getSelectedToppings();
        double hargaTopping = 0.0;
        for (Topping t : toppingDipilih) {
            hargaTopping += t.getHargaTopping();
        }

        double totalBiaya = hargaMie + hargaTopping;

        // Update tampilan label
        lblRincianMie.setText(String.format("Harga Mie Dasar: Rp %,.0f", hargaMie));
        lblRincianTopping.setText(String.format("Biaya Topping: +Rp %,.0f (%d topping dipilih)", hargaTopping, toppingDipilih.size()));
        lblTotalBiaya.setText(String.format("TOTAL: Rp %,.0f", totalBiaya));
    }

    /**
     * Proses penyimpanan pesanan ke log CSV dan pencetakan struk
     */
    private void prosesSimpanPesanan() {
        String nama = txtNamaPelanggan.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon masukkan nama pelanggan atau nomor meja!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            txtNamaPelanggan.requestFocus();
            return;
        }

        VarianMie mie = (VarianMie) cbVarianMie.getSelectedItem();
        if (mie == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih varian mie terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int levelPedas = sliderPedas.getValue();
        List<Topping> listTopping = getSelectedToppings();

        String waktuSekarang = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String idPesanan = txtIdPesanan.getText();

        // Buat objek PesananWarmindo (OOP)
        PesananWarmindo pesanan = new PesananWarmindo(idPesanan, nama, waktuSekarang, mie, levelPedas, listTopping);

        // Simpan ke log CSV (Data Persistence)
        boolean sukses = WarmindoDataManager.simpanPesanan(pesanan);

        if (sukses) {
            // Tampilkan Struk ke Tab Struk
            String struk = pesanan.generateStruk();
            txtStruk.setText(struk);
            tabbedPane.setSelectedIndex(0); // Pindah ke tab struk

            // Perbarui data riwayat dan statistik best seller
            refreshTabelDanStatistik();

            JOptionPane.showMessageDialog(this,
                    "Pesanan berhasil disimpan ke " + WarmindoDataManager.FILE_CSV + "!\n" +
                    "Total Biaya: " + String.format("Rp %,.0f", pesanan.hitungBiayaTotal()),
                    "Pesanan Berhasil", JOptionPane.INFORMATION_MESSAGE);

            // Persiapkan nomor pesanan baru
            txtIdPesanan.setText(generateNomorPesanan());
        } else {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan pesanan ke CSV!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Memperbarui data tabel riwayat dan panel analisis topping Best Seller
     */
    private void refreshTabelDanStatistik() {
        // 1. Muat data CSV ke tabel
        modelTabel.setRowCount(0);
        List<String[]> dataCsv = WarmindoDataManager.bacaRiwayatCsv();
        for (String[] row : dataCsv) {
            modelTabel.addRow(row);
        }

        // 2. Update ringkasan atas
        lblRingkasanTransaksi.setText(dataCsv.size() + " Pesanan");
        double omset = WarmindoDataManager.hitungTotalOmset();
        lblRingkasanOmset.setText(String.format("Rp %,.0f", omset));

        // 3. Update panel Best Seller Topping
        renderPanelBestSeller();
    }

    /**
     * Membuat tampilan visual peringkat Topping Paling Favorit (Best Seller)
     */
    private void renderPanelBestSeller() {
        panelBestSeller.removeAll();

        Map<String, Integer> frekuensi = WarmindoDataManager.hitungStatistikTopping();

        // Header Card Best Seller
        JPanel cardHeader = createCardSimple("🏆 Topping Paling Favorit (Best Seller)");
        cardHeader.setLayout(new BorderLayout(8, 8));
        cardHeader.setBackground(new Color(254, 249, 195)); // Soft gold

        JLabel lblTop1 = new JLabel();
        lblTop1.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTop1.setForeground(new Color(161, 98, 7));

        if (frekuensi.isEmpty()) {
            lblTop1.setText("Belum ada data topping yang tercatat di " + WarmindoDataManager.FILE_CSV);
        } else {
            Map.Entry<String, Integer> topItem = frekuensi.entrySet().iterator().next();
            lblTop1.setText(String.format("JUARA 1: %s (Dipesan sebanyak %d kali)", topItem.getKey(), topItem.getValue()));
        }
        cardHeader.add(lblTop1, BorderLayout.CENTER);
        panelBestSeller.add(cardHeader);
        panelBestSeller.add(Box.createVerticalStrut(12));

        if (!frekuensi.isEmpty()) {
            int maxPesanan = frekuensi.values().iterator().next();

            JPanel cardList = createCard("Peringkat Popularitas Topping:");
            cardList.setLayout(new BoxLayout(cardList, BoxLayout.Y_AXIS));

            int rank = 1;
            for (Map.Entry<String, Integer> entry : frekuensi.entrySet()) {
                JPanel rowPanel = new JPanel(new BorderLayout(8, 4));
                rowPanel.setOpaque(false);
                rowPanel.setBorder(new EmptyBorder(6, 4, 6, 4));

                String medal = (rank == 1) ? "🥇" : (rank == 2) ? "🥈" : (rank == 3) ? "🥉" : "   " + rank + ".";
                JLabel lblNama = new JLabel(String.format("%s %-30s (%d pesanan)", medal, entry.getKey(), entry.getValue()));
                lblNama.setFont(new Font("Segoe UI", rank <= 3 ? Font.BOLD : Font.PLAIN, 13));

                JProgressBar bar = new JProgressBar(0, maxPesanan);
                bar.setValue(entry.getValue());
                bar.setStringPainted(true);
                bar.setString(entry.getValue() + " order");
                bar.setPreferredSize(new Dimension(160, 20));
                bar.setForeground(rank == 1 ? WARNA_AKSEN : new Color(59, 130, 246));

                rowPanel.add(lblNama, BorderLayout.WEST);
                rowPanel.add(bar, BorderLayout.EAST);

                cardList.add(rowPanel);
                rank++;
            }
            panelBestSeller.add(cardList);
        }

        panelBestSeller.revalidate();
        panelBestSeller.repaint();
    }

    private List<Topping> getSelectedToppings() {
        List<Topping> selected = new ArrayList<>();
        for (int i = 0; i < listCbTopping.size(); i++) {
            if (listCbTopping.get(i).isSelected()) {
                selected.add(masterTopping.get(i));
            }
        }
        return selected;
    }

    private void updateInfoJenisMie() {
        VarianMie mie = (VarianMie) cbVarianMie.getSelectedItem();
        if (mie != null) {
            lblJenisMie.setText("Kategori: " + mie.getJenis() + " | Harga Dasar: Rp " + String.format("%,.0f", mie.getHargaDasar()));
        }
    }

    private void updateLabelPedas(int level) {
        switch (level) {
            case 0:
                lblPedasInfo.setText("Level 0: Original (Tanpa Cabai Rawit)");
                break;
            case 1:
                lblPedasInfo.setText("Level 1: Gurih Sedang (1 Cabai Rawit)");
                break;
            case 2:
                lblPedasInfo.setText("Level 2: Pedas Mantap (3 Cabai Rawit) 🌶️");
                break;
            case 3:
                lblPedasInfo.setText("Level 3: Ekstra Pedas (5 Cabai Rawit) 🌶️🌶️");
                break;
            case 4:
                lblPedasInfo.setText("Level 4: Level Gila (8 Cabai Rawit) 🔥");
                break;
            case 5:
                lblPedasInfo.setText("Level 5: Level Mampus Neraka (12 Cabai Rawit) 🔥🔥");
                break;
        }
    }

    private void resetForm() {
        txtNamaPelanggan.setText("Pelanggan " + ((int)(Math.random() * 50) + 1));
        cbVarianMie.setSelectedIndex(0);
        sliderPedas.setValue(1);
        for (JCheckBox cb : listCbTopping) {
            cb.setSelected(false);
        }
        hitungKalkulasiDinamis();
    }

    private String generateNomorPesanan() {
        int randomCode = (int) (Math.random() * 900) + 100;
        return "WMD-" + new SimpleDateFormat("HHmm").format(new Date()) + "-" + randomCode;
    }

    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setBackground(WARNA_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(WARNA_BORDER, 1, true),
                BorderFactory.createTitledBorder(
                        BorderFactory.createEmptyBorder(6, 8, 8, 8),
                        title,
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 13),
                        WARNA_TEKS_UTAMA
                )
        ));
        return card;
    }

    private JPanel createCardSimple(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WARNA_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(WARNA_BORDER, 1, true),
                BorderFactory.createTitledBorder(
                        BorderFactory.createEmptyBorder(4, 6, 6, 6),
                        title,
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 12),
                        WARNA_TEKS_UTAMA
                )
        ));
        return card;
    }

    public static void main(String[] args) {
        // Terapkan Look and Feel Nimbus jika tersedia, atau System Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }

        SwingUtilities.invokeLater(() -> {
            WarmindoApp app = new WarmindoApp();
            app.setVisible(true);
        });
    }
}
