package studentrentals.useriniteract;

import studentrentals.domain.User;

public class Session {

    private User currentUser;

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void loginSetCurrentUser(User user) {
        this.currentUser = user;
    }

    public void logoutClearCurrentUser() {
        this.currentUser = null;
    }
}
