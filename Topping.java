/**
 * Class Topping merepresentasikan pilihan tambahan topping mie.
 * Sesuai spesifikasi:
 * - Atribut: namaTopping, hargaTopping
 */
public class Topping {
    private String namaTopping;
    private double hargaTopping;

    // Constructor
    public Topping(String namaTopping, double hargaTopping) {
        this.namaTopping = namaTopping;
        this.hargaTopping = hargaTopping;
    }

    // Getter dan Setter
    public String getNamaTopping() {
        return namaTopping;
    }

    public void setNamaTopping(String namaTopping) {
        this.namaTopping = namaTopping;
    }

    public double getHargaTopping() {
        return hargaTopping;
    }

    public void setHargaTopping(double hargaTopping) {
        this.hargaTopping = hargaTopping;
    }

    @Override
    public String toString() {
        return namaTopping + " (+Rp " + String.format("%,.0f", hargaTopping) + ")";
    }
}
