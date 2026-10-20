# Factorization Math

Program konsol Java dengan dua fitur: faktorisasi prima serta perhitungan FPB/GCD dan KPK/LCM. Keduanya tersedia melalui satu menu utama.

Contoh faktorisasi prima: `46 = 2 × 23` dan `12 = 2 × 2 × 3`. Faktor yang berulang tetap ditampilkan; hasil ini berbeda dari daftar semua pembagi bilangan. Untuk pasangan `12` dan `18`, FPB-nya `6` dan KPK-nya `36`.

Program memakai Java Standard Library tanpa dependency tambahan atau database.

## Kebutuhan

- JDK 11 atau lebih baru untuk script build, dengan `JAVA_HOME` atau `java`, `javac`, dan `jar` tersedia di PATH.
- PowerShell untuk menjalankan `build.ps1` di Windows.
- NetBeans atau Ant bersifat opsional; metadata project disertakan.

Build menargetkan Java 8 melalui `--release 8`.

## Build dan jalankan

Clone repository, lalu masuk ke folder project:

```powershell
git clone https://github.com/zalamanga/factorization-math.git
Set-Location -LiteralPath '.\factorization-math'
```

Jika project sudah ada di komputer, gunakan folder yang berisi `src/` dan `build.ps1`. Jalankan perintah berikut satu per satu:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\build.ps1 -Task Build
powershell -NoProfile -ExecutionPolicy Bypass -File .\build.ps1 -Task Test
powershell -NoProfile -ExecutionPolicy Bypass -File .\build.ps1 -Task Run
```

Task Build membuat `dist/factorization-math.jar`. Task Test menjalankan tes tanpa JUnit atau unduhan dependency. Task Run membangun dan menjalankan program. Pengaturan execution policy pada contoh hanya berlaku untuk proses PowerShell tersebut.

JAR yang sudah dibangun juga dapat dijalankan langsung:

```powershell
java -jar .\dist\factorization-math.jar
```

### NetBeans atau Ant

Di NetBeans, pilih Open Project, buka folder project ini, lalu jalankan Clean and Build atau Run. Main class adalah `matchalgo.Matchalgo`.

Jika Ant tersedia di PATH, build dan tes juga dapat dijalankan dengan:

```powershell
ant test jar
```

## Masukan dan keluaran

Potongan sesi interaktif untuk pilihan FPB/KPK; angka setelah prompt adalah masukan pengguna:

```text
Math tools
1. Prime factorization
2. GCD / LCM (FPB / KPK)
0. Exit
Choose an option: 2
Enter the 1st number: 12
Enter the 2nd number: 18
GCD (FPB): 6
LCM (KPK): 36
```

- Pilih `1` untuk faktorisasi prima. Masukan `12` menghasilkan `Prime factors of 12: 2 2 3`; `1` menampilkan `1 has no prime factors.`.
- Pada prompt faktorisasi, `0` kembali ke menu dan bilangan negatif ditolak.
- Pilih `2` untuk menghitung FPB dan KPK dua bilangan. Nilai negatif dan nol diterima; pasangan `0, 0` menghasilkan FPB `0` dan KPK `0`.
- Pilih `0` pada menu utama untuk keluar. Setelah perhitungan, program kembali ke menu.
- Token bukan bilangan bulat atau di luar jangkauan `int` ditolak tanpa menghentikan program. Rentang `int` adalah `-2147483648` sampai `2147483647`.
- Akhir masukan (EOF) pada prompt mana pun mengakhiri program tanpa pesan kesalahan atau hasil pasangan yang belum lengkap.

Masukan dipisahkan oleh spasi atau baris baru. Bilangan desimal seperti `2.5` tidak diterima.

## API dan algoritma

Semua kelas berikut berada dalam package `matchalgo`.

| Pemanggilan | Hasil |
| --- | --- |
| `NumberTheory.primeFactors(12)` | `List<Integer>` berisi `[2, 2, 3]` |
| `Matchalgo.findFactors(46)` | `[2, 23]`; API lama tetap tersedia dan memanggil `NumberTheory.primeFactors` |
| `NumberTheory.gcd(-12, 18)` | `6L` |
| `NumberTheory.lcm(-12, 18)` | `36L` |

Faktorisasi mengembalikan daftar baru yang dapat diubah, berurutan naik dan menyertakan pengulangan. Semua nilai kurang dari `2` menghasilkan daftar kosong, termasuk bilangan negatif.

FPB dan KPK menerima seluruh rentang `int` dan mengembalikan hasil `long` yang tidak negatif. `gcd(0, 0) = 0`; KPK bernilai `0` jika salah satu masukan `0`. Konversi ke `long` menjaga nilai batas: `gcd(Integer.MIN_VALUE, 0) = 2147483648L`.

Faktorisasi membagi faktor `2`, lalu memeriksa pembagi ganjil hingga akar bilangan tersisa, dengan waktu terburuk `O(√n)`. FPB memakai algoritma Euclid; KPK dihitung menggunakan FPB dan aritmetika `long`.

## Struktur repository

```text
src/matchalgo/Matchalgo.java      Menu konsol dan API kompatibilitas
src/matchalgo/NumberTheory.java  Faktorisasi prima, FPB, dan KPK
test/matchalgo/MatchalgoTest.java Tes menu dan penanganan masukan
test/matchalgo/NumberTheoryTest.java Tes fungsi matematika
build.ps1                        Task Build, Test, dan Run
build.xml                        Build Ant
nbproject/                       Metadata NetBeans
manifest.mf                      Metadata JAR
```

Source dan file build berada langsung di root repository. Folder hasil build `build/`, `dist/`, serta pengaturan pribadi `nbproject/private/` tidak dimasukkan ke Git. Main class untuk IDE adalah `matchalgo.Matchalgo`.

## Pengujian

Jalankan task Test untuk memeriksa faktorisasi, FPB/KPK, nilai batas, serta menu dan penanganan masukan konsol. Tes memakai Java Standard Library dan dapat dijalankan tanpa akses jaringan.

Verifikasi pada JDK 17: build dengan target Java 8 serta kedua kelas tes, `MatchalgoTest` dan `NumberTheoryTest`, lulus melalui PowerShell dan Ant 1.10.15. JAR juga lulus uji konsol untuk faktorisasi prima, FPB/KPK, dan pasangan batas `Integer.MIN_VALUE` dengan `Integer.MAX_VALUE`.
