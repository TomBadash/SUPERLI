package PresentationLayer;
import java.util.Scanner;
public class Report {

    @Override
    public String toString() {
        return "What report would like to produce today?" +
                "1. Produce an inventory report" +
                "2. Produce an expired report" +
                "3. Produce a damaged report" +
                "4. Return to main menu";
    }
    public void runMenu(){
        int choice = 0;
        while (choice!=4){
            choice = GetChoice();
        }
    }
    public int GetChoice(){
        Scanner scanner = new Scanner(System.in);
        System.out.print(this);
        int userInput = scanner.nextInt();;
        switch (userInput){
            case 1:
                InventoryReport();
                break;
            case 2:
                ExpiredReport();
                break;
            case 3:
                DamagedReport();
                break;
            case 4:
                ReturnToMainMenu();
                break;
            default:
                //there is a outside while or not? that runs the main menu
                System.out.println("Please choose valid number between 1-3");;
                break;
        }
        return userInput;
    }
}
