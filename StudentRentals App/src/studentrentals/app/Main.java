package studentrentals.app;

import studentrentals.repository.PropertyRoomRepo;
import studentrentals.repository.UserRepo;
import studentrentals.authentication.Authentication;
import studentrentals.repository.BookingRepo;
import studentrentals.useriniteract.CLIapp;

public class Main {
    public static void main(String[] args) {
        UserRepo userRepo = new UserRepo();
        PropertyRoomRepo propertyRoomRepo = new PropertyRoomRepo();
        BookingRepo bookingRepo = new BookingRepo();

        Authentication auth = new Authentication(userRepo);

        CLIapp app = new CLIapp(userRepo, propertyRoomRepo, bookingRepo, auth);
        app.run();
    }
}
