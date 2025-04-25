package PrepaidTaxi;

import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;

class Driver {
    private int id;
    private final String username;
    private final String password;
    private String phone;
    private final Wallet wallet;
    private boolean status;
    private String vehicle_number;

    Driver (int id, String username, String password, String phone, String vehicle_number) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.wallet = new Wallet();
        this.vehicle_number = vehicle_number;
    }

    public boolean login(String username, String password) throws ClassNotFoundException {
        try {
            String sql = "SELECT id, phone, wallet_balance, status, vehicle_Number FROM driver WHERE username = '"+ username +
                    "' AND password = '" + password + "'";
            DataBase db = new DataBase();
            ResultSet rs = db.getData(sql);
            if (rs.next()) {
                this.id = rs.getInt("id");
                this.phone = rs.getString("phone");
                this.wallet.setBalance(rs.getDouble("wallet_balance"));
                this.status = rs.getBoolean("status");
                this.vehicle_number = rs.getString("vehicle_Number");
                return true;
            }
            
            db.closeConnection();
            System.out.println("### Login Successfully ###");
            return false;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public void register() throws ClassNotFoundException {
        Scanner scan = new Scanner(System.in);

        DataBase db = new DataBase();

        this.id = (int)(Math.random() * 10000);
        System.out.print("Enter username: ");
        String username = scan.nextLine();
        System.out.print("Enter password: ");
        String password = scan.nextLine();
        System.out.print("Enter phone number: ");
        String phone = scan.nextLine();
        System.out.print("Enter Vehicle Number: ");
        String veh_number = scan.nextLine();
        double balance = 0;

        String sql = "INSERT INTO driver (id, username, password, phone, wallet_balance, vehicle_Number) VALUES ("+
               id + ",'"+ username +"','" + password +"','"+ phone +"',"+balance+",'"+veh_number+"')";

        db.insertNew(sql);
        System.out.println("Driver registered successfully!");

        db.closeConnection();   
    }

    public void withdrawWallet() throws ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        DataBase db = new DataBase();
        
        System.out.print("Enter amount to withdraw: ");
        double amount = scan.nextDouble();
        scan.nextLine();
        wallet.withdrawWallet(amount);
        
        String sql = "UPDATE driver SET wallet_balance = "+this.wallet.getBalance()+" WHERE id = "+this.id;
        db.updateData(sql);
        System.out.println("Amount Withdraw Successfully! New balance: $" + wallet.getBalance());
    }

    public void viewBookingHistory() throws ClassNotFoundException {
        try {
            DataBase db = new DataBase();
            String sql = "SELECT * FROM bookings WHERE driver_id = "+this.id+"";
            ResultSet rs = db.getData(sql);

            System.out.println("\nBooking History:");
            System.out.println("UserId  BookingId  DriverId  From  To  Distance Fare Status VehicleNumber");
            while (rs.next()) {
                RideBooking booking = new RideBooking(
                    rs.getInt("booking_id"),
                    rs.getInt("user_id"),
                    rs.getInt("driver_id"),
                    rs.getString("pickup"),
                    rs.getString("dropoff"),
                    rs.getDouble("distance"),
                    rs.getDouble("fare"),
                    rs.getString("status"),
                    rs.getString("vehicle_Number")
                );
                booking.print();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public void changeStatus() throws ClassNotFoundException{
        DataBase db = new DataBase();
        
        String sql = "UPDATE driver SET status = "+ !this.status+" WHERE id = "+this.id;
        db.updateData(sql);
        
        System.out.println("Status Changed Successfully");
    }
    
   public double getBalance() throws ClassNotFoundException, SQLException {  
        DataBase db = new DataBase();  

        String sql = "SELECT wallet_balance FROM driver WHERE id = " + this.id;  
        System.out.println(sql);  

        ResultSet rs = db.getData(sql);  
        if (rs.next())   
            this.wallet.setBalance(rs.getDouble("wallet_balance"));  
        else  
            throw new SQLException("No user found with id: " + this.id);  

        return this.wallet.getBalance();  
    }  
}