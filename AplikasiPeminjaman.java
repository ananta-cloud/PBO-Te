import java.util.ArrayList;
import java.util.Scanner;

// Kelas Utama untuk menjalankan aplikasi
public class AplikasiPeminjaman {

    // Data utama aplikasi: daftar peminjam dan daftar barang
    static ArrayList<Peminjam> daftarPeminjam = new ArrayList<>();
    static ArrayList<Barang> daftarBarang = new ArrayList<>();

    public static void main(String[] args) {
        // Menambahkan beberapa data awal untuk mempermudah testing
        inisialisasiData();
        Scanner scanner = new Scanner(System.in);
        int pilihan = -1;

        while (pilihan != 0) {
            tampilkanMenu();
            System.out.print("Masukkan pilihan Anda: ");
            try {
                pilihan = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid, silakan masukkan angka.");
                pilihan = -1;
                continue;
            }

            switch (pilihan) {
                case 1:
                    prosesPinjam(scanner);
                    break;
                case 2:
                    prosesKembalikan(scanner);
                    break;
                case 3:
                    lihatStatusSemuaBarang();
                    break;
                case 4:
                    lihatDaftarPeminjam();
                    break;
                case 5:
                    tambahBarang(scanner);
                    break;
                case 0:
                    System.out.println("Terima kasih, program selesai.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
            if (pilihan != 0) {
                System.out.println("\nTekan Enter untuk kembali ke menu...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    public static void tampilkanMenu() {
        System.out.println("\n===== MENU PEMINJAMAN BARANG =====");
        System.out.println("1. Pinjam Barang");
        System.out.println("2. Kembalikan Barang");
        System.out.println("3. Lihat Status Semua Barang");
        System.out.println("4. Lihat Daftar Peminjam");
        System.out.println("5. Tambah Barang Baru"); 
        System.out.println("0. Keluar");
        System.out.println("====================================");
    }

    public static void tambahBarang(Scanner scanner) {
        System.out.println("\n--- Tambah Barang Baru ---");
        System.out.print("Masukkan nama barang baru: ");
        String namaBarang = scanner.nextLine();

        // Cek agar nama barang tidak kosong
        if (namaBarang == null || namaBarang.trim().isEmpty()) {
            System.out.println("\nGagal! Nama barang tidak boleh kosong.");
            return;
        }

        // Membuat objek barang baru dan menambahkannya ke daftar
        Barang barangBaru = new Barang(namaBarang);
        daftarBarang.add(barangBaru);

        System.out.println("\nBerhasil! Barang '" + namaBarang + "' telah ditambahkan ke sistem.");
    }

    public static void inisialisasiData() {
        // Data Peminjam Awal
        daftarPeminjam.add(new Peminjam("Budi Santoso", "2301001", "1B D3"));
        daftarPeminjam.add(new Peminjam("Citra Lestari", "2302005", "2B D4"));

        // Data Barang Awal
        daftarBarang.add(new Barang("Proyektor Epson EB-S41"));
        daftarBarang.add(new Barang("Kabel HDMI 5m"));
    }

    /**
     * DIUBAH: Proses pinjam sekarang langsung meminta data peminjam.
     */
    public static void prosesPinjam(Scanner scanner) {
        System.out.println("\n--- Proses Peminjaman Barang ---");

        // Langkah 1: Input dan validasi data peminjam
        System.out.print("Masukkan NIM Anda: ");
        String nim = scanner.nextLine();

        Peminjam peminjam = null;
        // Cek apakah peminjam dengan NIM ini sudah ada
        for (Peminjam p : daftarPeminjam) {
            if (p.getNim().equals(nim)) {
                peminjam = p; // Jika ada, gunakan data yang sudah ada
                System.out.println("Selamat datang kembali, " + p.getNama() + "!");
                break;
            }
        }

        // Jika peminjam tidak ditemukan, minta data baru
        if (peminjam == null) {
            System.out.println("NIM tidak terdaftar. Silakan masukkan data baru.");
            System.out.print("Masukkan Nama Anda: ");
            String nama = scanner.nextLine();
            System.out.print("Masukkan Kelas Anda: ");
            String kelas = scanner.nextLine();

            peminjam = new Peminjam(nama, nim, kelas);
            daftarPeminjam.add(peminjam); // Tambahkan peminjam baru ke daftar
            System.out.println("\nData peminjam baru '" + nama + "' berhasil disimpan.");
        }

        // Langkah 2: Pilih Barang yang Tersedia (tidak berubah)
        System.out.println("\nDaftar barang yang tersedia:");
        ArrayList<Barang> barangTersedia = new ArrayList<>();
        for (Barang barang : daftarBarang) {
            if (barang.isTersedia()) {
                barangTersedia.add(barang);
                System.out.println("- " + barang.getNamaBarang());
            }
        }
        if (barangTersedia.isEmpty()) {
            System.out.println("Maaf, tidak ada barang yang tersedia untuk dipinjam.");
            return;
        }

        // Langkah 3: Input Nama Barang dan Proses
        System.out.print("\nKetik nama barang yang ingin dipinjam: ");
        String namaBarangDipinjam = scanner.nextLine();
        boolean barangDitemukan = false;
        for (Barang barang : barangTersedia) {
            if (barang.getNamaBarang().equalsIgnoreCase(namaBarangDipinjam)) {
                barang.pinjam(peminjam);
                System.out.println(
                        "\nBerhasil! '" + peminjam.getNama() + "' telah meminjam '" + barang.getNamaBarang() + "'.");
                barangDitemukan = true;
                break;
            }
        }
        if (!barangDitemukan) {
            System.out.println("\nGagal! Barang tidak ditemukan atau sudah dipinjam.");
        }
    }

    /**
     * DIUBAH: Proses kembalikan sekarang mencari peminjam berdasarkan NIM.
     */
    public static void prosesKembalikan(Scanner scanner) {
        System.out.println("\n--- Proses Pengembalian Barang ---");

        // Langkah 1: Input NIM untuk identifikasi
        System.out.print("Masukkan NIM Anda untuk verifikasi: ");
        String nim = scanner.nextLine();

        Peminjam peminjam = null;
        for (Peminjam p : daftarPeminjam) {
            if (p.getNim().equals(nim)) {
                peminjam = p;
                break;
            }
        }
        // Jika peminjam dengan NIM tersebut tidak ada di data
        if (peminjam == null) {
            System.out.println("\nGagal! Peminjam dengan NIM " + nim + " tidak ditemukan.");
            return;
        }

        // Langkah 2: Cari barang yang dipinjam oleh peminjam tersebut
        ArrayList<Barang> barangDipinjamUser = new ArrayList<>();
        for (Barang barang : daftarBarang) {
            if (!barang.isTersedia() && barang.getDipinjamOleh() == peminjam) {
                barangDipinjamUser.add(barang);
            }
        }
        if (barangDipinjamUser.isEmpty()) {
            System.out.println("\n" + peminjam.getNama() + " tidak memiliki barang yang sedang dipinjam.");
            return;
        }

        // Langkah 3: Pilih barang untuk dikembalikan
        System.out.println("\nBarang yang Anda pinjam (" + peminjam.getNama() + "):");
        for (int i = 0; i < barangDipinjamUser.size(); i++) {
            System.out.println((i + 1) + ". " + barangDipinjamUser.get(i).getNamaBarang());
        }
        System.out.print("Pilih nomor barang yang ingin dikembalikan: ");
        int pilihan = Integer.parseInt(scanner.nextLine());
        if (pilihan > 0 && pilihan <= barangDipinjamUser.size()) {
            Barang barangDikembalikan = barangDipinjamUser.get(pilihan - 1);
            barangDikembalikan.kembalikan();
            System.out.println("\n Berhasil! '" + barangDikembalikan.getNamaBarang() + "' telah dikembalikan.");
        } else {
            System.out.println("\n Pilihan tidak valid.");
        }
    }

    public static void lihatStatusSemuaBarang() {
        System.out.println("\n--- Status Semua Barang ---");
        for (Barang barang : daftarBarang) {
            System.out.println("- " + barang.getNamaBarang() + " | Status: " + barang.getStatus());
        }
    }

    public static void lihatDaftarPeminjam() {
        System.out.println("\n--- Daftar Peminjam Terdaftar ---");
        if (daftarPeminjam.isEmpty()) {
            System.out.println("Belum ada peminjam yang terdaftar.");
        }
        for (Peminjam p : daftarPeminjam) {
            System.out.println("- Nama: " + p.getNama() + ", NIM: " + p.getNim() + ", Kelas: " + p.getKelas());
        }
    }
}

// Kelas untuk menympan data Peminjam
class Peminjam {
    private String nama;
    private String nim;
    private String kelas;

    public Peminjam(String nama, String nim, String kelas) {
        this.nama = nama;
        this.nim = nim;
        this.kelas = kelas;
    }

    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }

    public String getKelas() {
        return kelas;
    }
}

// Kelas Untuk Menyimpan data Barang
class Barang {
    private String namaBarang;
    private boolean tersedia;
    private Peminjam dipinjamOleh;

    public Barang(String namaBarang) {
        this.namaBarang = namaBarang;
        this.tersedia = true;
        this.dipinjamOleh = null;
    }

    public void pinjam(Peminjam peminjam) {
        if (this.tersedia) {
            this.tersedia = false;
            this.dipinjamOleh = peminjam;
        }
    }

    public void kembalikan() {
        this.tersedia = true;
        this.dipinjamOleh = null;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public boolean isTersedia() {
        return tersedia;
    }

    public Peminjam getDipinjamOleh() {
        return dipinjamOleh;
    }

    public String getStatus() {
        if (tersedia) {
            return " Tersedia";
        } else {
            return "Dipinjam oleh: " + this.dipinjamOleh.getNama() + " (NIM: " + this.dipinjamOleh.getNim() + ")";
        }
    }
}