package Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Game implements Serializable {
    private static final long serialVersionUID = 610L;

    private ArrayList<PlayerStateGame> results;
    private Date date;
    private boolean isEditable;

    public Game(ArrayList<PlayerStateGame> results, Date date, boolean isEditable) {
        this.results = results;
        this.date = date;
    }

    public ArrayList<PlayerStateGame> getResults() {
        return results;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
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
        StringBuilder w = new StringBuilder();

        for (int i = 0; i < results.size(); i++) {
            if (i != 0) {
                w.append(", ");
            }

            if (results.get(i).getPointsEndGame() == 10) {
                w.append(results.get(i).getUsername());
            }
        }

        return w.toString();
    }
}
