package PrepaidTaxi;

import java.sql.SQLException;
import java.util.Scanner;

public class PrepaidTaxiSystem {
    private final DataBase db;
    private final Scanner scanner;
    private User currentUser;
    private Driver currentDriver;

    public PrepaidTaxiSystem() throws ClassNotFoundException {
        db = new DataBase();
        scanner = new Scanner(System.in);
    }

    public void run() throws ClassNotFoundException, SQLException {
        while (true) {
            System.out.println("\nPrepaid Taxi System");
            System.out.println("1. Register as User");
            System.out.println("2. Register as Driver");
            System.out.println("3. Login as User");
            System.out.println("4. Login as Driver");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1:
                    new User(0, "", "", "").register();
                    break;
                case 2:
                    new Driver(0, "", "", "", "").register();
                    break;
                case 3:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    currentUser = new User(0, username, "", "");
                    if (currentUser.login(username, password)) {
                        System.out.println("### Login Successfull ###");
                        userMenu();
                    } else {
                        System.out.println("!!! Invalid credentials !!!");
                        currentUser = null;
                    }
                    break;
                case 4:
                    System.out.print("Enter username: ");
                    String driverUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String driverPassword = scanner.nextLine();
                    currentDriver = new Driver(0, driverUsername, "", "", "");
                    if (currentDriver.login(driverUsername, driverPassword)) {
                        driverMenu();
                    } else {
                        System.out.println("Invalid credentials!");
                        currentDriver = null;
                    }
                    break;
                case 5:
                    db.closeConnection();
                    System.out.println("Exiting...");
                    return ;
                default :
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private void userMenu() throws ClassNotFoundException, SQLException {
        OUTER:
        while (true) {
            System.out.println("\nUser Menu");
            System.out.println("1. Book Taxi");
            System.out.println("2. View Ride History");
            System.out.println("3. Recharge Wallet");
            System.out.println("4. Display Balance");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    currentUser.bookTaxi();
                    break;
                case 2:
                    currentUser.viewBookingHistory();
                    break;
                case 3:
                    currentUser.rechargeWallet();
                    break;
                case 4:
                    System.out.println("Wallet Balance : ₹ "+ currentUser.getBalance());
                    break;
                case 5:
                    currentUser = null;
                    System.out.println("Logged out!");
                    break OUTER;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private void driverMenu() throws ClassNotFoundException, SQLException {
        OUTER:
        while (true) {
            System.out.println("\nDriver Menu");
            System.out.println("1. View Ride History");
            System.out.println("2. Withdraw Wallet");
            System.out.println("3. Change Status");
            System.out.println("4. Display Balance");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    currentDriver.viewBookingHistory();
                    break;
                case 2:
                    currentDriver.withdrawWallet();
                    break;
                case 3:
                    currentDriver.changeStatus();
                    break;
                case 4:
                    System.out.println("Wallet Balance : ₹ "+ currentDriver.getBalance());
                    break;
                case 5:
                    currentDriver = null;
                    System.out.println("Logged out!");
                    break OUTER;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        PrepaidTaxiSystem system = new PrepaidTaxiSystem();
        system.run();
    }
}