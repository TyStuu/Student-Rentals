package studentrentals.app;

import studentrentals.repository.PropertyRoomRepo;
import studentrentals.repository.UserRepo;
import studentrentals.authentication.Authentication;
import studentrentals.repository.BookingRepo;
import studentrentals.useriniteract.CLIapp;
import studentrentals.util.Passwords;
import studentrentals.domain.Admin;

public class Main {
    public static void main(String[] args) {
        UserRepo userRepo = new UserRepo();
        PropertyRoomRepo propertyRoomRepo = new PropertyRoomRepo();
        BookingRepo bookingRepo = new BookingRepo();

        Admin admin = new Admin("SR_Admin", "Admin", "admin@studentrentals.com", Passwords.hashPassword("4Dm1NP@$$W0RD134!"));
        userRepo.saveUser(admin);

        Authentication auth = new Authentication(userRepo);

        CLIapp app = new CLIapp(userRepo, propertyRoomRepo, bookingRepo, auth);
        app.run();
    }
}
