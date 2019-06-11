package Data;

import java.io.Serializable;

public class PlayerStateGame implements Serializable, Comparable<PlayerStateGame> {
    private static final long serialVersionUID = 610L;

    private String username;
    private int pointsEndGame;
    private int pelliccionsTaken;
    private int cappottensTaken;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPointsEndGame(int pointsEndGame) {
        this.pointsEndGame = pointsEndGame;
    }

    public void setPelliccionsTaken(int pelliccionsTaken) {
        this.pelliccionsTaken = pelliccionsTaken;
    }

    public void setCappottensTaken(int cappottensTaken) {
        this.cappottensTaken = cappottensTaken;
    }

    public String getUsername() {
        return username;
    }

    public int getPointsEndGame() {
        return pointsEndGame;
    }

    public int getPelliccionsTaken() {
        return pelliccionsTaken;
    }

    public int getCappottensTaken() {
        return cappottensTaken;
    }

    @Override
    public int compareTo(PlayerStateGame o) {
        return Integer.compare(this.pointsEndGame, o.getPointsEndGame());
    }
}
