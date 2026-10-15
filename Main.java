
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
  static List<Order> pesanan = new ArrayList<>();

  static Menu[] menu = {
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

  // -------- UTILS --------

  // format rupiah
  static String toIDR(int nominal) {
    NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
    formatter.setMaximumFractionDigits(0);
    formatter.setMinimumFractionDigits(0);
    return formatter.format(nominal);
  }

  // Jumlah Pesanan
  static int readOrderCount() {
    while (true) {
      int count = readPositiveInt(
          "Jumlah menu yang ingin dipesan (1-4): ");

      if (count >= 1 && count <= 4) {
        return count;
      }

      System.out.println(
          "Jumlah pesanan harus antara 1 sampai 4.");
    }
  }

  // Cek positive input
  static int readPositiveInt(String message) {
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

  // hitung sub total
  static int calculateSubtotal() {
    int total = 0;

    for (Order order : pesanan) {
      total += order.getSubtotal();
    }

    return total;
  }

  // -------- READ MENU --------

  // list menu
  static void listMenu(Menu[] menu, Category category) {
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
    System.out.println("=========   List    =========");
    System.out.println("=========   Menu    =========");
    if (category == null || category == Category.Makanan) {
      System.out.println("\n");
      System.out.println("--------- Makanan ---------");
      listMenu(menu, Category.Makanan);
    }
    if (category == null || category == Category.Minuman) {
      System.out.println("\n");
      System.out.println("--------- Minuman ---------");
      listMenu(menu, Category.Minuman);
    }
    System.out.println("\n");
  }

  // -------- ORDER PROSES --------

  // proses pesanan
  static void startOrder() {
    System.out.println(
        "======== Masukkan Pesanan (Maksimal 4 menu) ========");

    int jumlahPesanan = readOrderCount();

    pesanan.clear();

    for (int i = 1; i <= jumlahPesanan; i++) {
      Order order = createOrder(i);
      pesanan.add(order);
    }

    printBill();
  }

  // membuat pesanan berdasarkan menu input user
  static Order createOrder(int no) {
    while (true) {
      System.out.printf(
          "Masukkan menu %d (Nama=Jumlah): ",
          no);

      String value = input.nextLine().trim();
      String[] parts = value.split("=", -1);

      if (parts.length != 2) {
        System.out.println(
            "Format salah. Contoh: Nasi Goreng=2");
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
          System.out.println(
              "Jumlah harus lebih dari 0.");
          continue;
        }

        return new Order(selectedMenu, quantity, false);

      } catch (NumberFormatException e) {
        System.out.println(
            "Jumlah harus berupa angka bulat.");
      }
    }
  }

  // cetak struk
  static void printBill() {
    int sub_total = calculateSubtotal();

    // Tawarkan promo berdasarkan subtotal pesanan berbayar
    if (sub_total >= 50000) {
      prosesPromo();
    }

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

    System.out.println("Total Bayar : " + toIDR(total));
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
      return;
    }

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

      pesanan.add(new Order(selectedMenu, 1, true));

      System.out.println(
          selectedMenu.name + " berhasil ditambahkan sebagai bonus!");

      return;

    }
  }

  // -------- MAIN FUNCTION --------
  public static void main(String[] args) {
    showMenu();
    startOrder();
  }
}