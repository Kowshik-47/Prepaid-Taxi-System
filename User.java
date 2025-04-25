package PrepaidTaxi;

import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;

class User {
    private int id;
    private final String username;
    private final String password;
    private String phone;
    private final Wallet wallet;

    User(int id, String username, String password, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.wallet = new Wallet();
    }

    public boolean login(String username, String password) throws ClassNotFoundException {
        try {
            String sql = "SELECT id, phone, wallet_balance FROM user WHERE username = '" 
                            + username +"' AND password = '" + password +"'";
            DataBase db = new DataBase();
            
            ResultSet rs = db.getData(sql);
            if (rs.next()) {
                this.id = rs.getInt("id");
                this.phone = rs.getString("phone");
                this.wallet.setBalance(rs.getDouble("wallet_balance")) ;
                
                return true;
            }
            
            db.closeConnection();
            return false;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public void register() throws ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        DataBase db = new DataBase();
        
        this.id = (int) (Math.random() * 100000);
        System.out.print("Enter username: ");
        String username = scan.nextLine();
        System.out.print("Enter password: ");
        String password = scan.nextLine();
        System.out.print("Enter phone number: ");
        String phone = scan.nextLine();
        double balance = 0.0;
        
        String sql = "INSERT INTO user (id, username, password, phone, wallet_balance) VALUES ("
               + id + ",'" + username + "','"+ password + "','" + phone + "'," + balance +")";

        db.insertNew(sql);
        System.out.println("User registered successfully!");
        
        db.closeConnection();
    }

    public void rechargeWallet() throws ClassNotFoundException {
        Scanner scan = new Scanner(System.in);       
        System.out.print("Enter amount to top up: ");
        double amount = scan.nextDouble();
        scan.nextLine();
        wallet.rechargeWallet(amount);
        String sql = "UPDATE user SET wallet_balance = "
                + wallet.getBalance() + " WHERE id = " + this.id +"";
        DataBase db = new DataBase();
        if (db.updateData(sql) == 1)
            System.out.println("Wallet Recharged Succesfully ! New balance: ₹ " + wallet.getBalance());
        else
            System.out.println("Something Went Wrong");
        db.closeConnection();
    }

    public void viewBookingHistory() throws ClassNotFoundException {
        try {
            String sql = "SELECT * FROM bookings WHERE user_id = "+ this.id +"";
            DataBase db = new DataBase();

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
                    rs.getString("vehicle_number")
                );
                booking.print();
            }
            
            db.closeConnection();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void bookTaxi() throws ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        
        try {
            DataBase db = new DataBase();
            
            System.out.print("Enter pickup location: ");
            String pickup = scan.nextLine();
            System.out.print("Enter drop-off location: ");
            String dropoff = scan.nextLine();
            System.out.print("Enter distance (in km): ");
            double distance = scan.nextDouble();
            scan.nextLine();

            double fare = distance * 10;
            if (wallet.withdrawWallet(fare)) {
                String sql = "SELECT id, wallet_balance, username, vehicle_Number FROM driver WHERE status = 1 LIMIT 1";
                ResultSet rs = db.getData(sql);
                if (rs.next()) {
                    int driverId = rs.getInt("id");
                    double driverBalance = rs.getDouble("wallet_balance");
                    int bookingId = (int)(Math.random() * 1000000);
                    String driverName = rs.getString("username");
                    String veh_no = rs.getString("vehicle_Number");
                    
                    sql = "INSERT INTO bookings (booking_id, user_id, driver_id, pickup, dropoff, distance, fare, status, vehicle_Number) VALUES ("
                            + bookingId +","+ this.id +", " + driverId + ",'"+ pickup + "','" + dropoff + "'," + distance +
                            "," + fare + ",'Confirmed','" + veh_no + "')";
                    db.insertNew(sql);

                    sql = "UPDATE user SET wallet_balance = "+ wallet.getBalance() +" WHERE id = " + this.id +"";
                    db.updateData(sql);

                    driverBalance += fare;
                    sql = "UPDATE driver SET wallet_balance = "+ driverBalance + " WHERE id = "+ driverId +"";
                    db.updateData(sql);
                    
                    sql = "UPDATE driver SET status = 0 WHERE id = "+ driverId +"";
                    db.updateData(sql);

                    System.out.println("Booking confirmed! Fare: ₹" + fare);
                    System.out.println("Driver Name : " + driverName);
                    System.out.println("Vehicle Number : " + veh_no);
                } else {
                    System.out.println("No drivers available!");
                    wallet.rechargeWallet(fare); 
                    sql = "UPDATE user SET wallet_balance = "+ wallet.getBalance() +" WHERE id = " + this.id +"";
                    db.updateData(sql);
                }
            } else {
                System.out.println("Insufficient wallet balance! Please top up.");
            }
            
            db.closeConnection();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public double getBalance() throws ClassNotFoundException, SQLException {  
        DataBase db = new DataBase();  

        String sql = "SELECT wallet_balance FROM user WHERE id = " + this.id;  
        System.out.println(sql);  

        ResultSet rs = db.getData(sql);  
        if (rs.next())   
            this.wallet.setBalance(rs.getDouble("wallet_balance"));  
        else  
            throw new SQLException("No user found with id: " + this.id);    

        return this.wallet.getBalance();  
    }  
}
