package PrepaidTaxi;

class RideBooking {
    private final int bookingId;
    private final int userId;
    private final int driverId;
    private final String from;
    private final String to;
    private final double distance;
    private final double fare;
    private final String status;
    private final String vehicle_number;

    RideBooking(int bookingId, int userId, int driverId, 
                String from, String to, double distance, 
                double fare, String status, String vehicle_number) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.driverId = driverId;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.fare = this.distance * 10;
        this.status = status;
        this.vehicle_number = vehicle_number;
    }

    public double getFare(){
        return this.fare; 
    }
    
    public void print(){    
        System.out.print(this.userId);
        System.out.print("\t" +this.bookingId);
        System.out.print("\t" + this.driverId);
        System.out.print("\t" + this.from);
        System.out.print("\t" + this.to);
        System.out.print("\t" + this.distance);
        System.out.print("\t₹ " + this.fare);
        System.out.print("\t" + this.status);
        System.out.print("\t" + this.vehicle_number);
        System.out.println();
    }
}