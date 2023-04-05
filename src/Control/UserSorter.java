package Control;

import java.util.Comparator;

public class UserSorter implements Comparator<User> {
    @Override
    public int compare(User o1, User o2) {
        return o1.getHighestScore() - o2.getHighestScore();
    }
}
