/**
 * Class VarianMie merepresentasikan data varian mie yang tersedia di Warmindo.
 * Sesuai spesifikasi:
 * - Atribut: namaMie, jenis (Goreng/Kuah), hargaDasar
 */
public class VarianMie {
    private String namaMie;
    private String jenis; // "Goreng" atau "Kuah"
    private double hargaDasar;

    // Constructor
    public VarianMie(String namaMie, String jenis, double hargaDasar) {
        this.namaMie = namaMie;
        this.jenis = jenis;
        this.hargaDasar = hargaDasar;
    }

    // Getter dan Setter
    public String getNamaMie() {
        return namaMie;
    }

    public void setNamaMie(String namaMie) {
        this.namaMie = namaMie;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public double getHargaDasar() {
        return hargaDasar;
    }

    public void setHargaDasar(double hargaDasar) {
        this.hargaDasar = hargaDasar;
    }

    @Override
    public String toString() {
        return namaMie + " (" + jenis + ") - Rp " + String.format("%,.0f", hargaDasar);
    }
}
