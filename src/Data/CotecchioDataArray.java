package Data;

import java.io.Serializable;
import java.util.ArrayList;

public class CotecchioDataArray implements Serializable {
    private static final long serialVersionUID = 710L;

    private ArrayList<Player> players;
    private ArrayList<Game> game;
    private long saveNumber = 0;

    public CotecchioDataArray() {
        this.players = new ArrayList<>();
        this.game = new ArrayList<>();
    }

    public CotecchioDataArray(ArrayList<Player> players, ArrayList<Game> game, long saveNumber) {
        this.players = players;
        this.game = game;
        this.saveNumber = saveNumber;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Game> getGame() {
        return game;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setGame(ArrayList<Game> game) {
        this.game = game;
    }

    public long getSaveNumber() {
        return saveNumber;
    }

    public void setSaveNumber(long saveNumber) {
        this.saveNumber = saveNumber;
    }
}
