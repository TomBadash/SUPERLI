package PresentationLayer;
import ServiceLayer.ProductController;
import ServiceLayer.StockController;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Management {

//    help function:
    public static ArrayList<String> showCatalogChoices(Scanner scan){
        ArrayList<String> categoriesForSales = new ArrayList<>(3);
        String mainCat = readStrFromUsr("Please enter the main category", scan);
        String sub = readStrFromUsr("If you want to add a specific sub category, enter its name or press 0 if dont", scan);
        String size = readStrFromUsr("If you want to add a specific size of product, for liquid add the " +
                "word 'litters' else add the word 'grams' after the size number or press 0 if you dont", scan);
        categoriesForSales.add(mainCat);
        categoriesForSales.add(sub);
        categoriesForSales.add(size);
        return categoriesForSales;
    }
    private static String readStrFromUsr(String msg, Scanner scan){
        System.out.println(msg);
        return scan.nextLine();
    }
    private static int readIntFromUsr(String msg, Scanner scan){
        System.out.println(msg);
        return scan.nextInt();
    }

    private static JsonObject CreateJason(int iteration, ArrayList<String> msgLst, ArrayList<String> memberLst){
        JsonObject myJson = new JsonObject();
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < iteration; i++){
            String member = memberLst.get(i);
            String value = readStrFromUsr(msgLst.get(i), scanner);
            myJson.addProperty(member, value);
        }
        return myJson;
    }


//   *****Menu Functions****
    public static void AddProd(){
        ArrayList<String> msgLst = new ArrayList<>(9);
        msgLst.add("Enter main category: ");
        msgLst.add("Enter sub category: ");
        msgLst.add("Enter size, for liquid add the word 'litter' else add the word 'gram' after the size number: ");
        msgLst.add("Enter product's manufacturer: ");
        msgLst.add("Enter catalog number: ");
        msgLst.add("Enter initial market price: ");
        msgLst.add("Enter initial manufacturer price of product:");
        msgLst.add("Enter minimal amount in which to inform when its product about to run out: ");
        msgLst.add("Enter a discount from manufacturer in ratio (if you dont want to add please type '0'): ");
        ArrayList<String> memberLst = new ArrayList<>(9);
        memberLst.add("catName");
        memberLst.add("subCatName");
        memberLst.add("size");
        memberLst.add("manuFactor");
        memberLst.add("catalogNumProduct");
        memberLst.add("marketPriceConst");
        memberLst.add("manuPriceConst");
        memberLst.add("minimalAmount");
        memberLst.add("discount");
        JsonObject JsonObjProd = CreateJason(9, msgLst, memberLst);
        boolean bool = ProductController.createNewProd(JsonObjProd);
        if (bool){
            System.out.println("The product added successfully");
            return;
        }
        System.out.println("Added failed - the product already in stock.\n");

    }
    public static void AddItem(){
        ArrayList<String> msgLst = new ArrayList<>(4);
        msgLst.add("Enter item id: ");
        msgLst.add("Enter expiration date YYYY-MM-DD: ");
        msgLst.add("If you want to add this item to the warehouse Enter the aile (a letter from A-Z) and then PRESS backspace and enter the shelf number.\n" +
                "else enter item's main category and then PRESS backspace then" +
                " enter the shelf number:");
        msgLst.add("Enter catalog number from the Catalog Number Table you received with the program instructions: ");
        ArrayList<String> memberLst = new ArrayList<>(4);
        memberLst.add("id");
        memberLst.add("expirationDate");
        memberLst.add("place");
        memberLst.add("catalogNumItem");
        JsonObject JsonObjItem = CreateJason(4, msgLst, memberLst);
        boolean prodInStock = StockController.ProdInStockControl(JsonObjItem.get("catalogNumItem").getAsInt());
        if(prodInStock){
        ProductController.createNewItem(JsonObjItem);
        System.out.println("Item added successfully\n");
        return;
        }
        System.out.println("There is no such products in the market," +
                " if you want to add a proper product please type 'YES' and then press ENTER. " +
                "Else type 'NO' and then press ENTER." +
                "");
        Scanner scan = new Scanner(System.in);
        if(scan.nextLine().equals("YES"))
            {AddProd();
            ProductController.createNewItem(JsonObjItem);
            System.out.println("Thank you, the item and new product added successfully\n");
            return;}
        System.out.println("OK, item will not add\n");

    }
    public static void RemoveProd(){
        Scanner scan = new Scanner(System.in);
        int input = readIntFromUsr("Please enter the catalog number of the product you want to " +
                "cancel from selling", scan);
        StockController.removeProdControl(input);
    }

    public static void UpdateSales(){
        Scanner scan = new Scanner(System.in);
        ArrayList<String> askCat = showCatalogChoices(scan);
        String getStartMSG = "Please enter the start date of sale by the format: YYYY-MM-DD";
        String fromSTR = readStrFromUsr(getStartMSG, scan);
        LocalDate fromDate = LocalDate.parse(fromSTR);
        String getToMSG ="Please enter the due date of sale by the format: YYYY-MM-DD";
        String toSTR = readStrFromUsr(getToMSG, scan);
        LocalDate dueDate = LocalDate.parse(toSTR);
        System.out.println("Please enter the sale percentage: ");
        double percentage = scan.nextDouble();
        StockController.updateSaleControl(askCat.get(0), askCat.get(1), askCat.get(2), fromDate, dueDate, percentage);
    }
    public static void UpdateDiscount(){
        Scanner scan = new Scanner(System.in);
        ArrayList<String> askCat = showCatalogChoices(scan);
        System.out.println("Please enter the discount percentage: ");
        double ratio = scan.nextDouble();
        String manuMSG = "Please enter the name of the manufacturer you want to update its percentage: ";
        String manu = readStrFromUsr(manuMSG, scan);
        StockController.updateDisControl(askCat.get(0), askCat.get(1), askCat.get(2), ratio, manu);
    }
    public static void MoveStoreWare(){
        Scanner scan = new Scanner(System.in);
        int id = readIntFromUsr("Please enter the id number of the item you want to move from store to warehouse", scan);
        String getPassMSG = "Please type a letter in [A-Z] which present the pass in the warehouse where you want to put the item: ";
        String pass = readStrFromUsr(getPassMSG, scan);
        String shelfMSG = "Please type the number of shelf you want to put your item: ";
        Integer shelf = readIntFromUsr(shelfMSG, scan);
        StockController.moveItemFromSControl(id, pass, shelf);
    }
    public static void MoveWareStore(){
        Scanner scan = new Scanner(System.in);
        int id = readIntFromUsr("Please enter the id number of the item you want to move from store to warehouse", scan);
        String getPassMSG = "Please type a the pass in the store (its the item main category) where you want to put the item: ";
        String pass = readStrFromUsr(getPassMSG, scan);
        String shelfMSG = "Please type the number of shelf you want to put your item: ";
        Integer shelf = readIntFromUsr(shelfMSG, scan);
        StockController.moveItemFromWControl(id, pass, shelf);
    }
    public static void ReturnToMainMenu(){
        System.out.println("Returning to main menu...");
    }


    public static String PrintMenu() {
        return """
                What would you like to do today?
                1. Add a product to inventory
                2. Add an item to inventory
                3. Remove a product from inventory
                4. Update sales
                5. Update discount from manufacturers
                6. Move an item from store to warehouse
                7. Move an item from warehouse to store
                8. Return to main menu
                """;
    }
    public static void runMenu(){
        int choice = 0;
        while (choice!=8){
            choice = GetChoice();
        }
    }
    public static int GetChoice(){
        Scanner scanner = new Scanner(System.in);
        System.out.print(PrintMenu());
        int userInput = scanner.nextInt();;
        switch (userInput) {
            case 1 -> AddProd();
            case 2 -> AddItem();
            case 3 -> RemoveProd();
            case 4 -> UpdateSales();
            case 5 -> UpdateDiscount();
            case 6 -> MoveStoreWare();
            case 7 -> MoveWareStore();
            case 8 -> ReturnToMainMenu();
            default -> {
                System.out.println("Please choose valid number between 1-9");
            }
        }
        return userInput;
    }

}
