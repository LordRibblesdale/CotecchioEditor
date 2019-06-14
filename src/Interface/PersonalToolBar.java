package Interface;

import Edit.Search;

import javax.swing.*;

public class PersonalToolBar extends JToolBar {
  private Search search;

  private UserController ui;

  PersonalToolBar(int location, UserController ui, PersonalMenu menu) {
    super(location);

    this.ui = ui;

    add(menu.getNewFile());
    add(menu.getOpenFile());
    add(menu.getSaveButton());
    add(menu.getPrint());
    add(menu.getExportWord());
    add(menu.getExportXls());
    add(search = new Search(this.ui));
    search.setEnabled(false);
  }
}
