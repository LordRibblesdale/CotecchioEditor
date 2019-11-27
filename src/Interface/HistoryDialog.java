package Interface;

import Data.HistoryData;
import Data.InsertionValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HistoryDialog extends JDialog implements InsertionValues {
  class InsertionDialog extends JDialog implements InsertionValues {
    private JPanel buttons;
    private JButton abort, add;

    InsertionDialog(UserController ui, JDialog dialog, int type) {
      super(dialog, true);
      setLayout(new BorderLayout());

      buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

      switch (type) {
        case TEXT:
          setTitle(
              ui.getSettings().getResourceBundle().getString("addDialogTitle")
                  + ui.getSettings().getResourceBundle().getString("addTextDialog"));
          break;
        case IMAGE:
          setTitle(
              ui.getSettings().getResourceBundle().getString("addDialogTitle")
                  + ui.getSettings().getResourceBundle().getString("addImageDialog"));
          break;
      }

      setMinimumSize(new Dimension(
          (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.4),
          (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.50)));
      setLocationRelativeTo(ui.getFrame());
      setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      setVisible(true);
    }
  }

  private ArrayList<HistoryData> components;
  private UserController ui;
  private JPanel buttons;
  private JSplitPane splitPane;
  private JButton addText, addImage, remove, removeAll, close;

  HistoryDialog(UserController ui, String title) {
    super(ui.getFrame(), title);
    this.ui = ui;

    setLayout(new BorderLayout());
    buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttons.add(close = new JButton(ui.getSettings().getResourceBundle().getString("goBack")));
    buttons.add(new JSeparator(JSeparator.VERTICAL));
    buttons.add(remove = new JButton(ui.getSettings().getResourceBundle().getString("removeComponent")));
    buttons.add(removeAll = new JButton(ui.getSettings().getResourceBundle().getString("removeAllComponents")));
    buttons.add(new JSeparator(JSeparator.VERTICAL));
    buttons.add(addImage = new JButton(ui.getSettings().getResourceBundle().getString("addImageButton")));
    buttons.add(addText = new JButton(ui.getSettings().getResourceBundle().getString("addTextButton")));

    close.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(
            HistoryDialog.this,
            ui.getSettings().getResourceBundle().getString("askSaveChanges"),
            ui.getSettings().getResourceBundle().getString("askSave"),
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE
        );

        switch (choice) {
          case JOptionPane.YES_OPTION:

            break;
          case JOptionPane.NO_OPTION:
            HistoryDialog.this.dispose();
            break;
        }
      }
    });

    add(buttons, BorderLayout.PAGE_END);

    setMinimumSize(new Dimension(
        (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.3),
        (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.65)));
    setLocationRelativeTo(ui.getFrame());
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    setVisible(true);
  }

  HistoryDialog(UserController ui, String title, ArrayList<HistoryData> components) {
    this(ui, title);
    this.components = components;
  }
}
