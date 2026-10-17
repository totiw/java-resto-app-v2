
import java.util.Scanner;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

enum Category {
  Makanan,
  Minuman
}

class Menu {
  String name;
  int price;
  Category category;

  Menu(String name, int price, Category category) {
    this.name = name;
    this.price = price;
    this.category = category;
  }
}

class Order {
  Menu menu;
  int quantity;
  boolean isPromo;

  Order(Menu menu, int quantity, boolean isPromo) {
    this.menu = menu;
    this.quantity = quantity;
    this.isPromo = isPromo;
  }

  int getSubtotal() {
    if (isPromo) {
      return 0;
    }

    return menu.price * quantity;
  }
}

public class Main {
  static Scanner input = new Scanner(System.in);
  static List<Menu> menu = new ArrayList<>();
  static List<Order> pesanan = new ArrayList<>();

  static Menu[] menu_list = {
      new Menu("Nasi Goreng", 25000, Category.Makanan),
      new Menu("Mie Goreng", 20000, Category.Makanan),
      new Menu("Ayam Geprek", 22000, Category.Makanan),
      new Menu("Sate Ayam", 25000, Category.Makanan),
      new Menu("Bakso", 20000, Category.Makanan),

      new Menu("Es Teh", 5000, Category.Minuman),
      new Menu("Es Jeruk", 8000, Category.Minuman),
      new Menu("Kopi Hitam", 10000, Category.Minuman),
      new Menu("Jus Alpukat", 15000, Category.Minuman),
      new Menu("Air Mineral", 5000, Category.Minuman)
  };

  static void instanceMenu() {
    for (Menu m : menu_list) {
      menu.add(m);
    }
  }

  // -------- UTILS --------

  // format rupiah
  static String toIDR(int nominal) {
    NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
    formatter.setMaximumFractionDigits(0);
    formatter.setMinimumFractionDigits(0);
    return formatter.format(nominal);
  }

  // hitung sub total
  static int calculateSubtotal() {
    int total = 0;

    for (Order order : pesanan) {
      total += order.getSubtotal();
    }

    return total;
  }

  // input positif integer
  static int positiveInt(String message) {
    while (true) {
      System.out.print(message);
      String value = input.nextLine().trim();

      try {
        int number = Integer.parseInt(value);

        if (number > 0) {
          return number;
        }

        System.out.println("Angka harus lebih dari 0.");
      } catch (NumberFormatException e) {
        System.out.println("Input harus berupa angka bulat.");
      }
    }
  }

  // -------- CREATE MENU --------

  static void createMenu() {
    while (true) {
      System.out.println("\n======== Tambah Menu ========");
      System.out.print("Nama menu: ");
      String nama_menu = input.nextLine().trim();
      Category kategori_menu = null;

      if (nama_menu.isEmpty()) {
        continue;
      }

      int harga_menu = positiveInt("Harga: ");

      while (true) {
        System.out.println("1. Makanan");
        System.out.println("2. Minuman");
        int kategori_id = positiveInt("Kategori (Makanan/Minuman): ");
        switch (kategori_id) {
          case 1:
            kategori_menu = Category.Makanan;
            break;
          case 2:
            kategori_menu = Category.Minuman;
            break;

          default:
            System.out.println("Pilih kategori berikut dengan benar (Makanan/Minuman).\n");
            continue;
        }
        menu.add(new Menu(nama_menu, harga_menu, kategori_menu));
        System.out.print("Berhasil menambah menu " + nama_menu + "!\n");
        return;
      }

    }
  }

  // -------- READ MENU --------

  // list menu
  static void listMenu(List<Menu> menu, Category category) {
    for (Menu m : menu) {
      if (m.category == category) {
        System.out.println(m.name + " - " + toIDR(m.price) + " (" + m.category + ")");
      }
    }
  }

  // cari menu berdasarkan nama atau kategori
  static Menu findMenu(String menuName, Category category) {
    for (Menu m : menu) {
      boolean sameName = m.name.equalsIgnoreCase(menuName);

      boolean sameCategory = category == null || m.category == category;

      if (sameName && sameCategory) {
        return m;
      }
    }

    return null;
  }

  // menampilkan semua daftar menu
  static void showMenu() {
    showMenu(null);
  }

  // menampilkan daftar menu berdasarkan kategori
  static void showMenu(Category category) {
    System.out.println("\n=========   List    =========");
    System.out.println("=========   Menu    =========");
    if (category == null || category == Category.Makanan) {
      System.out.println("\n--------- Makanan ---------\n");
      listMenu(menu, Category.Makanan);
    }
    if (category == null || category == Category.Minuman) {
      System.out.println("\n--------- Minuman ---------\n");
      listMenu(menu, Category.Minuman);
    }
  }

  // menampilkan daftar menu dengan nomor urut
  static void showMenuWithNumber() {
    for (int i = 0; i < menu.size(); i++) {
      Menu item = menu.get(i);
      System.out.println(i + 1 + ". " + item.name + " - " + toIDR(item.price) + " (" + item.category + ")");
    }
  }

  // -------- UPDATE MENU --------
  static void updateMenu() {
    while (true) {
      System.out.println("\n======== Pilih Menu ========");
      showMenuWithNumber();
      int input_menu = positiveInt("\nPilih nomer menu: ");
      Menu selectedMenu = menu.get(input_menu - 1);

      if (selectedMenu == null) {
        System.out.println("Pilih menu berikut dengan benar.\n");
        continue;
      }

      int price_value = positiveInt("Ubah Harga: ");
      System.out.print("Yakin mengubah harga? (y/n).\n");
      String confirm = input.nextLine();
      if (!confirm.equalsIgnoreCase("y")) {
        return;
      }

      selectedMenu.price = price_value;
      System.out.print("Berhasil mengubah harga!\n");
      return;
    }
  }

  // -------- DELETE MENU --------
  static void deleteMenu() {
    while (true) {
      System.out.println("\n======== Pilih Menu ========");
      showMenuWithNumber();
      int input_menu = positiveInt("\nPilih nomer menu yang akan dihapus: ");

      if (input_menu > menu.size()) {
        System.out.println("Nomer menu tidak valid.\n");
        continue;
      }

      System.out.print("Yakin menghapus menu? (y/n): ");
      String confirm = input.nextLine();
      if (!confirm.equalsIgnoreCase("y")) {
        return;
      }

      menu.remove(input_menu - 1);
      System.out.print("Berhasil menghapus menu!\n");
      return;
    }
  }

  // -------- MAIN MENU --------

  static void showMainMenu() {
    while (true) {
      System.out.println("\n======== Menu Utama ========");
      System.out.println("1. Pemesanan pelanggan");
      System.out.println("2. Manajemen menu");
      System.out.println("0. Keluar");
      System.out.print("Pilih menu: ");

      String menu_option = input.nextLine().trim();
      switch (menu_option) {

        // 1. Pesanan Pelanggan
        case "1":
          startOrder();
          break;

        // 2. Pengelolaan Menu Resto
        case "2":
          manageApp();
          break;

        // 0. Keluar Aplikasi
        case "0":
          System.out.println("Terima kasih!");
          return;

        default:
          System.out.println("Pilih menu berikut dengan benar.\n");
      }
    }
  }

  static void manageApp() {
    while (true) {
      System.out.println("\n======== Pengelolaan Menu ========");
      System.out.println("1. Tampilkan menu");
      System.out.println("2. Tambah menu");
      System.out.println("3. Ubah harga");
      System.out.println("4. Hapus menu");
      System.out.println("0. Kembali ke menu utama");
      System.out.print("Pilih menu: ");

      String menu_option = input.nextLine().trim();
      switch (menu_option) {
        // 0. Kembali ke Menu Utama
        case "0":
          return;

        // 1. Lihat Menu
        case "1":
          showMenu();
          break;

        // 2. Tambah Menu
        case "2":
          createMenu();
          break;

        // 3. Ubah Harga Menu
        case "3":
          updateMenu();
          break;

        // 4. Hapus Menu
        case "4":
          deleteMenu();
          break;

        default:
          System.out.println("Pilih menu berikut dengan benar.");
          continue;
      }
    }
  }

  // -------- ORDER PROSES --------

  // proses pesanan
  static void startOrder() {
    showMenu();
    System.out.println(
        "======== Masukkan Pesanan ========");

    pesanan.clear();

    while (true) {
      Order order = createOrder();

      if (order == null) {
        break;
      }

      pesanan.add(order);
    }

    if (pesanan.isEmpty()) {
      System.out.println("Belum ada pesanan.");
      return;
    }

    printBill();
  }

  // membuat pesanan berdasarkan menu input user
  static Order createOrder() {
    while (true) {
      System.out.printf("Masukkan pesanan (Nama=Jumlah), atau 'selesai': ");

      String value = input.nextLine().trim();

      if (value.equalsIgnoreCase("selesai")) {
        return null;
      }

      String[] parts = value.split("=", -1);

      if (parts.length != 2) {
        System.out.println("Format salah. Contoh: Nasi Goreng=2");
        continue;
      }

      String menuName = parts[0].trim();
      String quantityText = parts[1].trim();

      if (menuName.isEmpty()) {
        System.out.println("Nama menu tidak boleh kosong.");
        continue;
      }

      Menu selectedMenu = findMenu(menuName, null);

      if (selectedMenu == null) {
        System.out.println("Menu tidak ditemukan.");
        showMenu();
        continue;
      }

      try {
        int quantity = Integer.parseInt(quantityText);

        if (quantity <= 0) {
          System.out.println("Jumlah harus lebih dari 0.");
          continue;
        }

        return new Order(selectedMenu, quantity, false);

      } catch (NumberFormatException e) {
        System.out.println("Jumlah harus berupa angka bulat.");
      }
    }
  }

  // cetak struk
  static void printBill() {
    int sub_total = 0;

    // Tawarkan promo berdasarkan subtotal pesanan berbayar
    if (calculateSubtotal() > 50000) {
      prosesPromo();
    }

    sub_total = calculateSubtotal();

    // Tampilkan semua pesanan, termasuk bonus
    listOrder();

    int tax = sub_total * 10 / 100;
    int diskon = sub_total > 100000
        ? sub_total * 10 / 100
        : 0;

    int admin_cost = 20000;
    int total = sub_total + tax + admin_cost - diskon;

    System.out.println("\n-------------- TOTAL --------------");
    System.out.println("Total Harga : " + toIDR(sub_total));
    System.out.println("Biaya Admin : " + toIDR(admin_cost));
    System.out.println("Tax (10%)   : " + toIDR(tax));

    if (diskon > 0) {
      System.out.println("Diskon (10%): " + toIDR(diskon));
    }

    System.out.println("Total Bayar : " + toIDR(total) + "\n");
  }

  // list pesanan
  static void listOrder() {
    System.out.println("\n============= PESANAN =============");

    for (int i = 0; i < pesanan.size(); i++) {
      printOrder(i + 1, pesanan.get(i));
    }
  }

  // cetak pesanan
  static void printOrder(int no, Order order) {
    Menu menu = order.menu;
    int subtotal = order.getSubtotal();

    if (order.isPromo) {
      System.out.printf(
          "%d. %s - %s x %d = GRATIS%n",
          no,
          menu.name,
          toIDR(menu.price),
          order.quantity);
      return;
    }

    System.out.printf(
        "%d. %s - %s x %d = %s%n",
        no,
        menu.name,
        toIDR(menu.price),
        order.quantity,
        toIDR(subtotal));
  }

  // show promo
  static void prosesPromo() {
    System.out.print(
        "Anda mendapatkan promo Beli 1 Gratis 1 untuk minuman. " +
            "Ingin mengambilnya? (y/n): ");

    String offering = input.nextLine().trim();

    if (!offering.equalsIgnoreCase("y")) {
      System.out.println("Promo tidak digunakan.");
      return;
    }

    // Tampilkan daftar minuman untuk bonus
    showMenu(Category.Minuman);

    while (true) {
      System.out.print("Pilih minuman bonus: ");

      String menuName = input.nextLine().trim();

      if (menuName.isEmpty()) {
        System.out.println("Nama minuman tidak boleh kosong.");
        continue;
      }

      Menu selectedMenu = findMenu(
          menuName,
          Category.Minuman);

      if (selectedMenu == null) {
        System.out.println(
            "Menu tidak ditemukan atau bukan kategori minuman.");
        continue;
      }

      boolean found_duplicate = false;
      for (Order order : pesanan) {
        if (order.menu.name.equalsIgnoreCase(selectedMenu.name)) {
          found_duplicate = true;
          order.quantity += 1;
          break;
        }
      }

      if (!found_duplicate) {
        pesanan.add(new Order(selectedMenu, 1, false));
      }
      pesanan.add(new Order(selectedMenu, 1, true));

      System.out.println(
          selectedMenu.name + " berhasil ditambahkan sebagai bonus!");

      return;

    }
  }

  // -------- MAIN FUNCTION --------
  public static void main(String[] args) {
    instanceMenu();
    showMainMenu();
  }
}