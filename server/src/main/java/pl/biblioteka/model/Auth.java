package pl.biblioteka.model;

import java.util.Objects;

public class Auth {
	private static Auth instance;
    private User currentUser;

    public Auth() {
    }
    
    public static synchronized Auth getInstance () {
    	if(instance==null)
    		instance= new Auth();
    	return instance;
    } 
    public Auth(User currentUser) {
        this.currentUser = currentUser;
    }
//
    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    @Override
    public String toString() {
        return "Auth{" +
            "currentUser=" + currentUser +
            '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Auth auth)) {
            return false;
        }
        return Objects.equals(currentUser, auth.currentUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentUser);
    }
}