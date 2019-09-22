package Interface;

import Data.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MatchInfoPanel extends JDialog {
  MatchInfoPanel(UserController ui, Game game) {
    setLayout(new BorderLayout());

    JPanel master = new JPanel();
    master.setLayout(new BoxLayout(master, BoxLayout.Y_AXIS));
    JPanel bottom = new JPanel();

    ArrayList<SinglePanel> players = new ArrayList<>(game.getResults().size());

    JPanel p = new JPanel();
    p.add(new JLabel(ui.getSettings().getResourceBundle().getString("playersNumber")));

    JTextField txt = new JTextField(String.valueOf(game.getResults().size()));
    txt.setColumns(2);
    txt.setEditable(false);
    p.add(txt);
    master.add(p);

    for (int i = 0; i < game.getResults().size(); i++) {
      players.add(new SinglePanel(ui, game.getResults().get(i), false));
      master.add(players.get(i));
    }

    add(master);

    JButton back;
    bottom.add(back = new JButton(ui.getSettings().getResourceBundle().getString("goBack")));

    back.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        MatchInfoPanel.this.dispose();
      }
    });


    add(bottom, BorderLayout.PAGE_END);

    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    /*
    int WIDTH = 343;
    int HEIGHT = 600;
    setMinimumSize(new Dimension(WIDTH, HEIGHT));
    */
    setResizable(false);
    pack();
    setLocationRelativeTo(ui);
    setVisible(true);
  }
}
