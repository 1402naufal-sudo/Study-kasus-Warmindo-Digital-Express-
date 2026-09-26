import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class WarmindoDataManager bertanggung jawab atas Data Persistence:
 * - Menyimpan setiap transaksi pesanan ke log_warmindo.csv
 * - Membaca riwayat transaksi dari log_warmindo.csv
 * - Menganalisis frekuensi topping untuk menentukan Topping Paling Favorit (Best Seller)
 */
public class WarmindoDataManager {
    public static final String FILE_CSV = "log_warmindo.csv";
    private static final String CSV_HEADER = "ID_Pesanan,Waktu_Pesanan,Nama_Pelanggan,Nama_Mie,Jenis_Mie,Level_Pedas,Daftar_Topping,Biaya_Mie,Biaya_Topping,Total_Biaya";

    /**
     * Menyimpan satu objek PesananWarmindo ke dalam file CSV (mode append).
     * Jika file belum ada, header akan otomatis dibuat terlebih dahulu.
     */
    public static synchronized boolean simpanPesanan(PesananWarmindo pesanan) {
        File file = new File(FILE_CSV);
        boolean fileBaru = !file.exists() || file.length() == 0;

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            if (fileBaru) {
                writer.println(CSV_HEADER);
            }

            // Bersihkan koma pada daftar topping dengan menggantinya jadi titik koma (agar CSV tetap valid)
            StringBuilder toppingsCsv = new StringBuilder();
            if (pesanan.getListTopping() != null) {
                for (int i = 0; i < pesanan.getListTopping().size(); i++) {
                    toppingsCsv.append(pesanan.getListTopping().get(i).getNamaTopping());
                    if (i < pesanan.getListTopping().size() - 1) {
                        toppingsCsv.append(";");
                    }
                }
            }

            String row = String.format("%s,%s,%s,%s,%s,%d,\"%s\",%.0f,%.0f,%.0f",
                    escapeCsv(pesanan.getIdPesanan()),
                    escapeCsv(pesanan.getWaktuPesanan()),
                    escapeCsv(pesanan.getNamaPelanggan()),
                    escapeCsv(pesanan.getMieUtama() != null ? pesanan.getMieUtama().getNamaMie() : "-"),
                    escapeCsv(pesanan.getMieUtama() != null ? pesanan.getMieUtama().getJenis() : "-"),
                    pesanan.getLevelPedas(),
                    toppingsCsv.toString(),
                    pesanan.getMieUtama() != null ? pesanan.getMieUtama().getHargaDasar() : 0.0,
                    pesanan.hitungBiayaTopping(),
                    pesanan.hitungBiayaTotal()
            );

            writer.println(row);
            return true;
        } catch (IOException e) {
            System.err.println("Gagal menyimpan ke " + FILE_CSV + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Membaca seluruh baris data dari log_warmindo.csv untuk ditampilkan di tabel GUI
     */
    public static List<String[]> bacaRiwayatCsv() {
        List<String[]> rows = new ArrayList<>();
        File file = new File(FILE_CSV);
        if (!file.exists()) {
            return rows;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean barisPertama = true;
            while ((line = br.readLine()) != null) {
                if (barisPertama) {
                    barisPertama = false;
                    continue; // Skip header
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] tokens = parseCsvLine(line);
                rows.add(tokens);
            }
        } catch (IOException e) {
            System.err.println("Gagal membaca CSV: " + e.getMessage());
        }
        return rows;
    }

    /**
     * Menghitung statistik pemesanan tiap topping dari riwayat di log_warmindo.csv.
     * Hasil diurutkan dari yang terbanyak (Best Seller pertama).
     */
    public static Map<String, Integer> hitungStatistikTopping() {
        Map<String, Integer> frekuensi = new HashMap<>();
        List<String[]> riwayat = bacaRiwayatCsv();

        for (String[] row : riwayat) {
            // Kolom ke-6 adalah Daftar_Topping (indeks 6)
            if (row.length > 6) {
                String toppingStr = row[6].replace("\"", "").trim();
                if (!toppingStr.isEmpty() && !toppingStr.equalsIgnoreCase("Tanpa Topping")) {
                    String[] toppings = toppingStr.split(";");
                    for (String t : toppings) {
                        String nama = t.trim();
                        if (!nama.isEmpty()) {
                            frekuensi.put(nama, frekuensi.getOrDefault(nama, 0) + 1);
                        }
                    }
                }
            }
        }

        // Urutkan Map berdasarkan jumlah frekuensi tertinggi (Descending)
        List<Map.Entry<String, Integer>> list = new ArrayList<>(frekuensi.entrySet());
        list.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /**
     * Mendapatkan nama Topping nomor 1 paling favorit (Best Seller)
     */
    public static String getBestSellerTopping() {
        Map<String, Integer> stats = hitungStatistikTopping();
        if (stats.isEmpty()) {
            return "Belum ada data";
        }
        Map.Entry<String, Integer> top = stats.entrySet().iterator().next();
        return top.getKey() + " (" + top.getValue() + "x dipesan)";
    }

    /**
     * Menghitung total omset pendapatan dari seluruh pesanan di CSV
     */
    public static double hitungTotalOmset() {
        double total = 0.0;
        List<String[]> riwayat = bacaRiwayatCsv();
        for (String[] row : riwayat) {
            if (row.length > 9) {
                try {
                    total += Double.parseDouble(row[9].replace("\"", "").trim());
                } catch (NumberFormatException ignored) {}
            }
        }
        return total;
    }

    // Helper CSV parser sederhana yang mendukung nilai ber-tanda kutip
    private static String[] parseCsvLine(String line) {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean dalamKutip = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                dalamKutip = !dalamKutip;
            } else if (c == ',' && !dalamKutip) {
                list.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        list.add(sb.toString().trim());
        return list.toArray(new String[0]);
    }

    private static String escapeCsv(String str) {
        if (str == null) return "";
        if (str.contains(",") || str.contains("\"") || str.contains("\n")) {
            return "\"" + str.replace("\"", "\"\"") + "\"";
        }
        return str;
    }
}
