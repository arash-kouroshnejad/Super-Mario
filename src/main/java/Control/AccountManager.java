package Control;

import Persistence.Persistence;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class AccountManager {
    private final static AccountManager instance = new AccountManager();
    private boolean isLogged;
    private User currentUser;
    private AccountManager(){}
    private final ArrayList<User> users = new ArrayList<>();
    public boolean createUser(String username, String password) {
        User user = Persistence.getInstance().findUser(username);
        if (user != null) {
            return false;
        }
        user = new User(username, hash(password));
        users.add(user);
        return true;
    }
    public boolean Login(String username, String password) {
        User user = Persistence.getInstance().findUser(username);
        if (user == null) {
            return false;
        }
        if (compareHashes(user.getPASSHASH(),hash(password))) {
            isLogged = true;
            currentUser = user;
            return true;
        }
        return false;
    }
    private byte[] hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] output = md.digest();
            md.reset();
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean compareHashes(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i=0;i<a.length;i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }
    public static AccountManager getInstance() {
        return instance;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    public ArrayList<User> getUsers() {
        return users;
    }
}
