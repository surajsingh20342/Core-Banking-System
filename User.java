import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection con;
    private Scanner sc;

    public User(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public void register(){
        System.out.print("Enter Full Name: ");
        String name = sc.next();
        System.out.print("Enter email: ");
        String email = sc.next();
        System.out.print("Set password: ");
        int password = sc.nextInt();

        if(userExists(email)){
            System.out.println("User already exists");
            return;
        }

        String sql = "insert into users(name, email, password) values(?, ?, ?)";
        try{
            PreparedStatement prep1 = con.prepareStatement(sql);
            prep1.setString(1,name);
            prep1.setString(2,email);
            prep1.setInt(3,password);
            int rowsAffected = prep1.executeUpdate();
            if(rowsAffected>0){
                System.out.println("Registration successful!!!");
            }
            else{
                System.out.println("Registration FAIL");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }


    }

    public String login(){
        System.out.print("Email: ");
        String email = sc.next();
        System.out.print("Password: ");
        int password = sc.nextInt();

        String sql = "select * from users where email = ? and password = ?";
        try{
            PreparedStatement prep2 = con.prepareStatement(sql);
            prep2.setString(1, email);
            prep2.setInt(2, password);
            ResultSet result = prep2.executeQuery();
            if(result.next()){
                return email;
            }
            else{
                return null;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;

    }
    public boolean userExists(String email){
        String sql = "SELECT * FROM users WHERE email = ?";
        try{
            PreparedStatement prep3 = con.prepareStatement(sql);
            prep3.setString(1,email);
            ResultSet result = prep3.executeQuery();
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