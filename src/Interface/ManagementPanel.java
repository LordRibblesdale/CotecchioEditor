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
  private JButton calendarButton, editButton;

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
    calendarButton = new JButton(new CalendarButton(ui));
    editButton = new JButton(new TabbedListButton(ui));
    bottomPanel.add(calendarButton);
    bottomPanel.add(editButton);

    add(bottomPanel, BorderLayout.PAGE_END);

    add(panel);
  }

  void setNextPage(String next) { //TODO: optimise here
    ui.setPage(next);

    switch (next) {
      case "CALENDAR":
        panel.add(calendarPanel, "CALENDAR");
        ui.checkDifferentUsernames();
        calendarPanel.getModelTable().fireChanges();
        break;
      case "EDIT":
        panel.add(editPanel, "EDIT");
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
