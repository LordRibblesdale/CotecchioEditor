package Interface;

import javax.swing.*;
import java.util.ArrayList;

public class HistoryDialog extends JDialog {
  private ArrayList<JComponent> components;

  HistoryDialog(JFrame frame, String title) {
    super(frame, title);


  }

  HistoryDialog(JFrame frame, String title, ArrayList<JComponent> components) {
    this(frame, title);

    
  }
}
