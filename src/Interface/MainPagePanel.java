package Interface;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

class MainPagePanel extends JPanel {
  private JScrollPane leaderboard;
  private JEditorPane textArea;

  private UserController ui;

  MainPagePanel(UserController ui) {
    super();
    setLayout(new BorderLayout());

    this.ui = ui;

    leaderboard = new JScrollPane(textArea = new JEditorPane());
    textArea.setContentType("text/html");
    textArea.setText(setUpText());
    textArea.setEditable(false);

    add(leaderboard);
  }

  String setUpText() {
    StringBuilder text = new StringBuilder();
    Scanner s = new Scanner(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("messageIT"))));

    while (s.hasNext()) {
      text.append(s.nextLine());
    }

    return text.toString();
  }
}
