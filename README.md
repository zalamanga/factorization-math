# Factorization Math

Program konsol Java untuk menguraikan bilangan bulat menjadi faktor prima. Contohnya, `46 = 2 × 23` dan `12 = 2 × 2 × 3`. Faktor prima yang berulang tetap ditampilkan; hasil ini berbeda dari daftar semua pembagi bilangan.

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

Contoh sesi interaktif; angka setelah prompt adalah masukan pengguna:

```text
Enter an integer (0 to exit): 46
Prime factors of 46: 2 23
Enter an integer (0 to exit): 12
Prime factors of 12: 2 2 3
Enter an integer (0 to exit): 0
Exiting the program.
```

- Bilangan `2` sampai `2147483647` diuraikan menjadi faktor prima.
- `1` menampilkan `1 has no prime factors.` lalu program meminta masukan lagi.
- `0` mengakhiri program.
- Bilangan negatif ditolak dengan pesan, lalu program meminta masukan lagi.
- Token bukan bilangan bulat atau di luar jangkauan `int` ditolak tanpa menghentikan program.
- Akhir masukan (EOF) mengakhiri program tanpa pesan kesalahan.

Masukan dipisahkan oleh spasi atau baris baru. Bilangan desimal seperti `2.5` tidak diterima.

## API dan algoritma

`matchalgo.Matchalgo.findFactors(int number)` mengembalikan `List<Integer>` berisi faktor prima dalam urutan naik, termasuk faktor yang berulang. Metode ini mengembalikan daftar kosong untuk nilai kurang dari `2`, termasuk bilangan negatif; antarmuka konsol menolak masukan negatif.

```java
Matchalgo.findFactors(46); // [2, 23]
Matchalgo.findFactors(12); // [2, 2, 3]
Matchalgo.findFactors(1);  // []
```

Algoritma membagi faktor `2`, lalu memeriksa calon pembagi ganjil hingga akar bilangan yang tersisa. Pemeriksaan memakai pembagian untuk menghindari overflow pada perhitungan batas. Waktu terburuknya `O(√n)`.

## Struktur repository

```text
src/matchalgo/Matchalgo.java      Program konsol dan faktorisasi
test/matchalgo/MatchalgoTest.java Tes mandiri tanpa library tambahan
build.ps1                        Task Build, Test, dan Run
build.xml                        Build Ant
nbproject/                       Metadata NetBeans
manifest.mf                      Metadata JAR
```

Source dan file build berada langsung di root repository. Folder hasil build `build/`, `dist/`, serta pengaturan pribadi `nbproject/private/` tidak dimasukkan ke Git. Main class untuk IDE adalah `matchalgo.Matchalgo`.

## Pengujian

Jalankan task Test untuk memeriksa faktorisasi dan perilaku konsol. Tes memakai Java Standard Library dan dapat dijalankan tanpa akses jaringan.

Verifikasi pada JDK 17: build dengan target Java 8 berhasil, dan 36 pemeriksaan pada `MatchalgoTest` lulus melalui PowerShell serta Ant 1.10.15. Program konsol juga berhasil dijalankan dengan masukan `46`, `12`, dan `0`.
