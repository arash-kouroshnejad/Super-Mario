package game.model;

public class BowserState {
    private int HP;

    public BowserState(int HP) {
        this.HP = HP;
    }

    public void takeDamage(int damage) {
        HP -= damage;
    }

    public boolean isAlive() {
        return HP > 0;
    }

    public int getHP() {
        return HP;
    }
}
