package Interface;

import javax.swing.*;
import java.awt.*;

public class ManagementPanel extends JPanel implements PageList {
  private UserController ui;

  private JPanel panel;
  private MainPagePanel main;
  private EditPanel editPanel;
  private CalendarPanel calendarPanel;

  private JPanel bottomPanel;
  private JButton /*backButton, */calendarButton, editButton;

  private CardLayout primaryLayout;

  ManagementPanel(UserController ui) {
    super();
    setLayout(new BorderLayout());

    panel = new JPanel(primaryLayout = new CardLayout());

    this.ui = ui;

    editPanel = new EditPanel(ui);
    calendarPanel = new CalendarPanel(ui);
    main = new MainPagePanel(ui);

    panel.add(main, "BACK");

    bottomPanel = new JPanel();
    //backButton = new JButton(ui.getSettings().getResourceBundle().getString("goBack"));
    //backButton.setEnabled(false);
    calendarButton = new JButton(new CalendarButton(ui));
    editButton = new JButton(new TabbedListButton(ui));
    //bottomPanel.add(backButton);
    bottomPanel.add(calendarButton);
    bottomPanel.add(editButton);

    /*
    backButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ui.askForNextPage("BACK");
      }
    });

     */

    add(bottomPanel, BorderLayout.PAGE_END);

    add(panel);
  }

  void setNextPage(String next) { //TODO: optimise here
    ui.setPage(next);

    switch (next) {
      case "CALENDAR":
        panel.add(calendarPanel, "CALENDAR");
        ui.checkDifferentUsernames();
        calendarPanel.getModelTable().fireTableStructureChanged();
        //backButton.setEnabled(true);
        break;
      case "EDIT":
        panel.add(editPanel, "EDIT");
        //backButton.setEnabled(true);
        break;
      case "BACK":
        //backButton.setEnabled(false);
        break;
    }

    primaryLayout.show(ManagementPanel.this.panel, next);
  }

  EditPanel getEditPanel() {
    return editPanel;
  }

  CalendarPanel getCalendarPanel() {
    return calendarPanel;
  }

  JPanel getBottomPanel() {
    return bottomPanel;
  }

  JPanel getPanel() {
    return panel;
  }
}
