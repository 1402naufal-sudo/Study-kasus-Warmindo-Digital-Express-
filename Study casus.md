### Warmindo Digital Express" — Kasir Custom Racikan Mie & Topping

- **Latar Belakang:** Warung makan Indomie memiliki puluhan kombinasi pesanan (varian mie kuah/goreng, level cabai, topping telur/kornet/sosis/keju) yang sering salah hitung jika ditulis manual.
- **Rancangan OOP:**
    - Class `VarianMie`: atribut `namaMie`, `jenis` (Goreng/Kuah), `hargaDasar`.
    - Class `Topping`: atribut `namaTopping`, `hargaTopping`.
    - Class `PesananWarmindo`: atribut `mieUtama`, `levelPedas` (0–5), `listTopping`, met hod `hitungBiayaTotal()`.
- **Antarmuka GUI:** Checkbox pilihan multi-topping, slider atau spinner untuk level kepedasan cabai rawit, dan kalkulasi dinamis penambahan biaya topping.
- **Data Persistence:** Menyimpan log pesanan ke `log_warmindo.csv` yang mendata topping paling favorit (*Best Seller*). 