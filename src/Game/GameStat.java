package Game;


public class GameStat {
    private int score;
    private int lives = 3;
    private long timeElapsed;
    private int level;
    private int coinsEarned;
    private final int ID;
    private int killCount;

    private boolean finished;

    public GameStat(int score, int lives, long timeElapsed, int ID) {
        this.score = score;
        this.lives = lives;
        this.timeElapsed = timeElapsed;
        this.ID = ID;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getID() {
        return ID;
    }

    public int getCoinsEarned() {
        return coinsEarned;
    }

    public void earnCoin() {
        coinsEarned++;
    }

    public int getKillCount() {
        return killCount;
    }

    public void killEnemy(){
        killCount++;}

    public boolean isFinished() {
        return finished;
    }

    public void terminate(){finished = true;}
}
