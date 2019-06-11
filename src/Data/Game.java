package Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Game implements Serializable {
    private static final long serialVersionUID = 610L;

    private ArrayList<PlayerStateGame> results;
    private Date date;

    Game(ArrayList<PlayerStateGame> results, Date date) {
        this.results = results;
        this.date = date;

        Arrays.sort(this.results.toArray(new PlayerStateGame[0]));
    }

    public ArrayList<PlayerStateGame> getResults() {
        return results;
    }

    public void setResults(ArrayList<PlayerStateGame> results) {
        this.results = results;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getWinner() {
        return results.get(0).getUsername();
    }
}
