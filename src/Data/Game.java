package Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Game implements Serializable {
    private static final long serialVersionUID = 610L;

    private ArrayList<PlayerStateGame> results;
    private Date date;

    Game() {

    }
}
