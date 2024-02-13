import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;
public class Account_Manager {
    private Connection con;
    private Scanner sc;
    public Account_Manager(Connection con,Scanner sc){
        this.con = con;
        this.sc = sc;
    }
    public void credit(long account_number){
        System.out.println("Enter amount: ");
        long amount = sc.nextLong();
        System.out.println("Enter pin: ");
        int pin = sc.nextInt();
        if(account_number>0){
            String sql = "select * from accounts where account_number = ? and pin = ?";
            try{
                con.setAutoCommit(false);
                PreparedStatement prep1 = con.prepareStatement(sql);
                prep1.setLong(1,account_number);
                prep1.setInt(2,pin);
                ResultSet result = prep1.executeQuery();
                if(result.next()){
                    String sql_new = "update accounts set balance = balance + ? where account_number = ?";
                    PreparedStatement prep2 = con.prepareStatement(sql_new);
                    prep2.setLong(1,amount);
                    prep2.setLong(2,account_number);
                    int rowsAffected = prep2.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Amount "+amount+" credited successfully!!!");
                        con.commit();
                        con.setAutoCommit(true);
                    }
                    else{
                        System.out.println("Transaction failed");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                }
                else{
                    System.out.println("Invalid pin");
                }

            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }

    }
    public void debit(long account_number){
        System.out.print("Enter amount: ");
        long amount = sc.nextLong();
        System.out.print("Enter pin: ");
        int pin = sc.nextInt();
        if(account_number>0){
            String sql = "select * from accounts where account_number = ? and pin = ?";
            try{
                PreparedStatement prep1 = con.prepareStatement(sql);
                prep1.setLong(1,account_number);
                prep1.setInt(2,pin);
                ResultSet result = prep1.executeQuery();
                if(result.next()){
                    long balance = result.getLong("balance");
                    if(balance>=amount){
                        String sql_new = "update accounts set balance = balance - ? where account_number = ?";
                        try{
                            PreparedStatement prep2 = con.prepareStatement(sql_new);
                            prep2.setLong(1,amount);
                            prep2.setLong(2,account_number);
                            int rowsAffected = prep2.executeUpdate();
                            if(rowsAffected>0){
                                System.out.println("Amount "+amount+" debited successfully!!!");
                                con.commit();
                                con.setAutoCommit(true);
                            }
                            else{
                                System.out.println("Transaction fail");
                                con.rollback();
                                con.setAutoCommit(true);
                            }
                        }
                        catch (SQLException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    else{
                        System.out.println("Insufficient balance");
                    }
                }
                else{
                    System.out.println("Invalid pin");
                }
            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
    public void transfer_money(long sender_account_number){
        System.out.print("Enter receiver account number: ");
        long receiver_account_number = sc.nextLong();
        System.out.print("Enter amount: ");
        long amount = sc.nextLong();
        System.out.print("Enter pin: ");
        int pin = sc.nextInt();
        if(sender_account_number>0 && receiver_account_number>0){
            try{
                con.setAutoCommit(false);
                String sql = "select * from accounts where account_number = ? and pin = ?";
                PreparedStatement prep1 = con.prepareStatement(sql);
                prep1.setLong(1,sender_account_number);
                prep1.setInt(2,pin);
                ResultSet result = prep1.executeQuery();
                if(result.next()){
                    long balance = result.getLong("balance");
                    if(balance>=amount){
                        String credit = "update accounts set balance = balance + ? where account_number = ?";
                        PreparedStatement cred = con.prepareStatement(credit);
                        cred.setLong(1,amount);
                        cred.setLong(2,receiver_account_number);
                        int rowsAffectedCredit = cred.executeUpdate();
                        String debit = "update accounts set balance = balance - ? where account_number = ?";
                        PreparedStatement deb = con.prepareStatement(debit);
                        deb.setLong(1,amount);
                        deb.setLong(2,sender_account_number);
                        int rowsAffectedDebit = deb.executeUpdate();
                        if(rowsAffectedDebit > 0 && rowsAffectedCredit > 0){
                            System.out.println("Amount "+amount+" transferred successfully!!!");
                            con.commit();
                            con.setAutoCommit(true);
                        }
                        else{
                            System.out.println("Transaction failed");
                            con.rollback();
                            con.setAutoCommit(true);
                        }

                    }
                    else{
                        System.out.println("Insufficient balance");
                    }
                }
                else{
                    System.out.print("Invalid pin");
                    con.setAutoCommit(true);
                }
            }
            catch (SQLException e){
                System.out.print(e.getMessage());
            }
        }
        else{
            System.out.print("Account does not exists");
        }
    }
    public void getBalance(long account_number){
        System.out.print("Enter pin: ");
        int pin = sc.nextInt();
        try{
            String sql = "select balance from accounts where account_number = ? and pin = ?";
            PreparedStatement prep = con.prepareStatement(sql);
            prep.setLong(1,account_number);
            prep.setInt(2,pin);
            ResultSet result = prep.executeQuery();
            if(result.next()){
                long balance = result.getLong("balance");
                System.out.println("Balance: "+balance);
            }
            else{
                System.out.println("Account does not exists");
            }

        }
        catch (SQLException e){
            System.out.print(e.getMessage());
        }
    }

}
