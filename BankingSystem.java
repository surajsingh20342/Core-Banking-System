import java.sql.*;
import java.util.Scanner;

public class BankingSystem {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/Banking_System";
        String username = "postgres";
        String password = "#Suraj5007";

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver loaded successfully!!!");
        }
        catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection con = DriverManager.getConnection(url,username,password);
            System.out.println("Connection formed successfully!!!");
            Scanner sc = new Scanner(System.in);
            User user = new User(con,sc);
            Accounts accounts = new Accounts(con,sc);
            Account_Manager accountManager = new Account_Manager(con,sc);
            String email;
            long account_number;

            while(true){
                System.out.println("WELCOME TO BANKING MANAGEMENT SYSTEM");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice1 = sc.nextInt();
                switch (choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        System.out.println("User Logged In.");
                        if(email!=null){
                            if(!accounts.account_exists(email)){
                                System.out.println("1. Open new Account");
                                System.out.println("2. Exit");
                                System.out.print("Enter your choice: ");
                                int choice2 = sc.nextInt();
                                switch (choice2){
                                    case 1:
                                        account_number = accounts.openAccount(email);
                                        System.out.println("Account created successfully!!!");
                                        System.out.println("Your account number - "+account_number);
                                        break;
                                    case 2:
                                        System.out.print("Exiting System");
                                        int i=5;
                                        while(i!=0){
                                            try{
                                                System.out.print(".");
                                                Thread.sleep(1000);
                                                i--;
                                            }
                                            catch (InterruptedException e){
                                                System.out.println(e.getMessage());
                                            }
                                        }
                                        System.out.println("\nTHANK YOU FOR USING BANKING SYSTEM APPLICATION");
                                        break;
                                    default:
                                        System.out.println("Enter a valid choice!!!");
                                        break;

                                }
                            }
                            account_number = accounts.getAccountNumber(email);
                            int choice3=0;
                            while(choice3!=5){
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. LogOut");
                                System.out.print("\nEnter your Choice: ");
                                choice3 = sc.nextInt();

                                switch (choice3){
                                    case 1:
                                        accountManager.debit(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter a valid choice");
                                        break;
                                }
                            }
                            break;
                        }
                        else{
                            System.out.println("Incorrect Email");
                            break;
                        }
                    case 3:
                        System.out.print("Exiting System");
                        int i=5;
                        while(i!=0){
                            try{
                                System.out.print(".");
                                Thread.sleep(1000);
                                i--;
                            }
                            catch (InterruptedException e){
                                System.out.println(e.getMessage());
                            }
                        }
                        System.out.println("\nTHANK YOU FOR USING BANKING SYSTEM APPLICATION");
                        return;
                    default:
                        System.out.println("Enter a valid choice!!!");
                        return;
                }
            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
