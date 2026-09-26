import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Program pengujian otomatis dan inisialisasi data contoh (Seed Data).
 */
public class WarmindoTest {
    public static void main(String[] args) {
        System.out.println("=== PENGUJIAN LOGIKA & DATA PERSISTENCE WARMINDO ===");

        // Siapkan master data
        VarianMie mie1 = new VarianMie("Indomie Goreng Original", "Goreng", 8000);
        VarianMie mie2 = new VarianMie("Indomie Kuah Kari Ayam Spesial", "Kuah", 8500);
        VarianMie mie3 = new VarianMie("Indomie Goreng Aceh Pedas Mantap", "Goreng", 9000);

        Topping topTelur = new Topping("Telur Ceplok (Setengah Matang)", 3500);
        Topping topKornet = new Topping("Kornet Sapi Tumis", 4500);
        Topping topKeju = new Topping("Keju Cheddar Parut Melimpah", 3500);
        Topping topSosis = new Topping("Sosis Bakar / Goreng", 3500);

        // Pesanan 1
        List<Topping> toppings1 = Arrays.asList(topTelur, topKornet, topKeju);
        PesananWarmindo p1 = new PesananWarmindo("WMD-001", "Budi Santoso", "2026-09-26 10:15:00", mie1, 3, toppings1);
        System.out.println("1. Pesanan 1 Total: Rp " + p1.hitungBiayaTotal() + " (Ekspektasi: 8000 + 3500 + 4500 + 3500 = 19500)");
        assert p1.hitungBiayaTotal() == 19500 : "Perhitungan Pesanan 1 salah!";

        // Pesanan 2 (Topping: Telur Ceplok + Sosis)
        List<Topping> toppings2 = Arrays.asList(topTelur, topSosis);
        PesananWarmindo p2 = new PesananWarmindo("WMD-002", "Siti Rahma", "2026-09-26 10:30:00", mie2, 1, toppings2);
        System.out.println("2. Pesanan 2 Total: Rp " + p2.hitungBiayaTotal() + " (Ekspektasi: 8500 + 3500 + 3500 = 15500)");

        // Pesanan 3 (Topping: Telur Ceplok + Kornet)
        List<Topping> toppings3 = Arrays.asList(topTelur, topKornet);
        PesananWarmindo p3 = new PesananWarmindo("WMD-003", "Andi Wijaya", "2026-09-26 10:45:00", mie3, 5, toppings3);

        // Simpan ke CSV
        WarmindoDataManager.simpanPesanan(p1);
        WarmindoDataManager.simpanPesanan(p2);
        WarmindoDataManager.simpanPesanan(p3);
        System.out.println("3. Berhasil menyimpan 3 transaksi contoh ke " + WarmindoDataManager.FILE_CSV);

        // Baca kembali dari CSV
        List<String[]> riwayat = WarmindoDataManager.bacaRiwayatCsv();
        System.out.println("4. Jumlah transaksi terbaca dari CSV: " + riwayat.size());

        // Hitung Topping Paling Favorit (Best Seller)
        Map<String, Integer> stats = WarmindoDataManager.hitungStatistikTopping();
        System.out.println("\n--- PERINGKAT TOPPING BEST SELLER ---");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            System.out.println(rank + ". " + entry.getKey() + ": " + entry.getValue() + "x pesanan");
            rank++;
        }

        System.out.println("\nTopping Juara 1: " + WarmindoDataManager.getBestSellerTopping());
        System.out.println("Total Omset: Rp " + String.format("%,.0f", WarmindoDataManager.hitungTotalOmset()));
        System.out.println("\n=== PENGUJIAN SELESAI DENGAN SUKSES ===");
    }
}
