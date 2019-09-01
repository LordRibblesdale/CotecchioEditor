package Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Game implements Serializable {
    private static final long serialVersionUID = 710L;

    private ArrayList<PlayerStateGame> results;
    private byte hands;
    private int time = 0;
    private LocalDate date;
    private boolean isEditable;

    public Game(ArrayList<PlayerStateGame> results, LocalDate date, byte hands, int time, boolean isEditable) {
        this.results = results;
        this.hands = hands;
        this.time = time;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public byte getHands() {
        return hands;
    }

    public void setHands(byte hands) {
        this.hands = hands;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getPlayers() {
        StringBuilder w = new StringBuilder();

        for (int i = 0; i < results.size(); i++) {
            if (i != 0) {
                w.append(", ");
            }

            w.append(results.get(i).getUsername());
        }

        return w.toString();
    }
}
