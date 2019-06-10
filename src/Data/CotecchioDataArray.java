package Data;

import java.io.Serializable;
import java.util.ArrayList;

public class CotecchioDataArray implements Serializable {
    private static final long serialVersionUID = 610L;

    private ArrayList<Player> players;
    private ArrayList<Game> game;

    public CotecchioDataArray() {
        this.players = new ArrayList<>();
        this.game = new ArrayList<>();
    }

    public CotecchioDataArray(ArrayList<Player> players, ArrayList<Game> game) {
        this.players = players;
        this.game = game;
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
}
