package PrepaidTaxi;

class Wallet {
    private double balance;

    Wallet() {
        this.balance = 0.0;
    }

    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double amt) {
        if (amt > 0)
            balance = amt;
    }

    public void rechargeWallet(double amount) {
        balance += amount;
    }

    public boolean withdrawWallet(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}