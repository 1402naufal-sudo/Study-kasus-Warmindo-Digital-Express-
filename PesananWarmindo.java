import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class PesananWarmindo merepresentasikan pesanan racikan mie di Warmindo.
 * Sesuai spesifikasi:
 * - Atribut: mieUtama (VarianMie), levelPedas (0–5), listTopping (List<Topping>)
 * - Method: hitungBiayaTotal()
 */
public class PesananWarmindo {
    private String idPesanan;
    private String namaPelanggan;
    private String waktuPesanan;
    private VarianMie mieUtama;
    private int levelPedas; // 0 - 5
    private List<Topping> listTopping;

    private static final Locale LOCALE_ID = new Locale("id", "ID");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(LOCALE_ID);

    // Constructor lengkap
    public PesananWarmindo(String idPesanan, String namaPelanggan, String waktuPesanan, 
                           VarianMie mieUtama, int levelPedas, List<Topping> listTopping) {
        this.idPesanan = idPesanan;
        this.namaPelanggan = (namaPelanggan == null || namaPelanggan.trim().isEmpty()) ? "Pelanggan Umum" : namaPelanggan.trim();
        this.waktuPesanan = waktuPesanan;
        this.mieUtama = mieUtama;
        this.levelPedas = Math.max(0, Math.min(5, levelPedas));
        this.listTopping = (listTopping != null) ? new ArrayList<>(listTopping) : new ArrayList<>();
    }

    // Constructor sederhana sesuai minimal spec
    public PesananWarmindo(VarianMie mieUtama, int levelPedas, List<Topping> listTopping) {
        this("WMD-" + System.currentTimeMillis() % 10000, "Pelanggan Umum", "", mieUtama, levelPedas, listTopping);
    }

    /**
     * Menghitung total biaya pesanan:
     * Harga Dasar Mie + Seluruh Biaya Topping yang dipilih.
     */
    public double hitungBiayaTotal() {
        double total = 0.0;
        if (mieUtama != null) {
            total += mieUtama.getHargaDasar();
        }
        if (listTopping != null) {
            for (Topping topping : listTopping) {
                total += topping.getHargaTopping();
            }
        }
        return total;
    }

    /**
     * Menghitung total biaya khusus topping saja
     */
    public double hitungBiayaTopping() {
        double totalTopping = 0.0;
        if (listTopping != null) {
            for (Topping topping : listTopping) {
                totalTopping += topping.getHargaTopping();
            }
        }
        return totalTopping;
    }

    // Getter dan Setter
    public String getIdPesanan() {
        return idPesanan;
    }

    public void setIdPesanan(String idPesanan) {
        this.idPesanan = idPesanan;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public String getWaktuPesanan() {
        return waktuPesanan;
    }

    public void setWaktuPesanan(String waktuPesanan) {
        this.waktuPesanan = waktuPesanan;
    }

    public VarianMie getMieUtama() {
        return mieUtama;
    }

    public void setMieUtama(VarianMie mieUtama) {
        this.mieUtama = mieUtama;
    }

    public int getLevelPedas() {
        return levelPedas;
    }

    public void setLevelPedas(int levelPedas) {
        this.levelPedas = Math.max(0, Math.min(5, levelPedas));
    }

    public List<Topping> getListTopping() {
        return listTopping;
    }

    public void setListTopping(List<Topping> listTopping) {
        this.listTopping = (listTopping != null) ? new ArrayList<>(listTopping) : new ArrayList<>();
    }

    public void tambahTopping(Topping topping) {
        if (this.listTopping == null) {
            this.listTopping = new ArrayList<>();
        }
        this.listTopping.add(topping);
    }

    /**
     * Mengembalikan daftar nama topping dalam format teks dipisahkan koma
     */
    public String getToppingString() {
        if (listTopping == null || listTopping.isEmpty()) {
            return "Tanpa Topping (Polos)";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listTopping.size(); i++) {
            sb.append(listTopping.get(i).getNamaTopping());
            if (i < listTopping.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public String getDeskripsiLevelPedas() {
        switch (levelPedas) {
            case 0: return "Level 0 (Original - Tidak Pedas)";
            case 1: return "Level 1 (Sedang - 1 Cabai Rawit)";
            case 2: return "Level 2 (Pedas - 3 Cabai Rawit)";
            case 3: return "Level 3 (Ekstra Pedas - 5 Cabai Rawit)";
            case 4: return "Level 4 (Gila - 8 Cabai Rawit)";
            case 5: return "Level 5 (Mampus - 12 Cabai Rawit 🔥)";
            default: return "Level " + levelPedas;
        }
    }

    /**
     * Format cetak struk kasir profesional
     */
    public String generateStruk() {
        StringBuilder struk = new StringBuilder();
        struk.append("========================================\n");
        struk.append("       WARMINDO DIGITAL EXPRESS         \n");
        struk.append("   Kasir Custom Racikan Mie & Topping   \n");
        struk.append("========================================\n");
        struk.append("ID Pesanan : ").append(idPesanan).append("\n");
        struk.append("Waktu      : ").append(waktuPesanan).append("\n");
        struk.append("Pelanggan  : ").append(namaPelanggan).append("\n");
        struk.append("----------------------------------------\n");
        struk.append("Menu Utama:\n");
        if (mieUtama != null) {
            struk.append(String.format(" %-24s Rp %,9.0f\n", mieUtama.getNamaMie() + " (" + mieUtama.getJenis() + ")", mieUtama.getHargaDasar()));
        }
        struk.append("Kepedasan  : ").append(getDeskripsiLevelPedas()).append("\n");
        struk.append("----------------------------------------\n");
        struk.append("Topping Pilihan:\n");
        if (listTopping == null || listTopping.isEmpty()) {
            struk.append(" (Tanpa topping tambahan)\n");
        } else {
            for (Topping t : listTopping) {
                struk.append(String.format(" + %-22s Rp %,9.0f\n", t.getNamaTopping(), t.getHargaTopping()));
            }
        }
        struk.append("----------------------------------------\n");
        struk.append(String.format("Subtotal Topping          : Rp %,9.0f\n", hitungBiayaTopping()));
        struk.append(String.format("TOTAL BIAYA               : Rp %,9.0f\n", hitungBiayaTotal()));
        struk.append("========================================\n");
        struk.append("        Terima Kasih Atas Kunjungan Anda! \n");
        struk.append("        Selamat Menikmati Racikan Anda!  \n");
        struk.append("========================================\n");
        return struk.toString();
    }
}
