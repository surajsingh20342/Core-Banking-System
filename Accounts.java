import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
public class Accounts {
    private Connection con;
    Scanner sc;
    public Accounts(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public long openAccount(String email){
        if(!account_exists(email)){
            String sql = "insert into accounts(account_number,name,email,balance,pin) values(?,?,?,?,?)";
            long account_number = generateAccountNumber();
            System.out.print("Enter Full Name: ");
            String name = sc.next();
            System.out.print("Enter initial account of deposit: ");
            long amount = sc.nextLong();
            System.out.println("Enter account pin: ");
            int pin = sc.nextInt();

            try{
                PreparedStatement prep1 = con.prepareStatement(sql);
                prep1.setLong(1,account_number);
                prep1.setString(2,name);
                prep1.setString(3,email);
                prep1.setLong(4,amount);
                prep1.setInt(5,pin);
                int rowsAffected = prep1.executeUpdate();
                if(rowsAffected>0){
                    System.out.println("Account created successfully!!!");
                    return account_number;
                }
                else{
                    throw new RuntimeException("Account creation failed");
                }
            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        throw new RuntimeException("Account already exists");
    }

    public long getAccountNumber(String email){
        String sql = "select account_number from accounts where email = ?";
        try{
            PreparedStatement prep1 = con.prepareStatement(sql);
            prep1.setString(1,email);
            ResultSet result = prep1.executeQuery();
            if(result.next()){
                long account_number = result.getLong("account_number");
                return account_number;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account does not exists");
    }
    public long generateAccountNumber(){
        String sql = "select account_number from accounts order by account_number desc limit 1";
        try{
            Statement str = con.createStatement();
            ResultSet result = str.executeQuery(sql);
            if(result.next()){
                long account_number = result.getLong("account_number");
                return account_number+1;
            }
            else{
                return 1001020;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 1001020;
    }

    public boolean account_exists(String email){
        String sql = "select account_number from accounts where email = ?";
        try{
            PreparedStatement prep1 = con.prepareStatement(sql);
            prep1.setString(1,email);
            ResultSet result = prep1.executeQuery();
            if(result.next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }


}
