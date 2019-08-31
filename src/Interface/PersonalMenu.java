package Interface;

import Edit.Search;
import Export.ExportLeaderboard;
import Export.ExportWordLeaderboard;
import Export.ExportXls;
import Export.PrintThread;
import FileManager.*;
import Game.GameStarter;
import Game.OpenGameFile;
import Update.UpdateButton;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class PersonalMenu extends JMenuBar {
  private JMenu file, edit, about, export, game;

  private SaveFile saveButton;
  private SaveAsFile saveAsButton;
  private Search search;
  private PrintThread print;
  private GameStarter start;
  private OpenGameFile openGame;
  private NewFile newFile;
  private OpenFile openFile;
  private ExportXls exportXls;
  private ExportWordLeaderboard exportWord;
  private JCheckBoxMenuItem showList;

  private UserController ui;

  PersonalMenu(UserController ui) {
    super();

    this.ui = ui;

    add(file = new JMenu("File"));
    file.add(newFile = new NewFile(this.ui));
    file.add(openFile = new OpenFile(this.ui));
    file.add(saveButton = new SaveFile(this.ui));
    file.add(saveAsButton = new SaveAsFile(this.ui));
    file.add(new JSeparator());
    file.add(export = new JMenu(this.ui.getSettings().getResourceBundle().getString("export")));
    file.add(print = new PrintThread(this.ui));
    export.add(exportWord = new ExportWordLeaderboard(this.ui));
    export.add(exportXls = new ExportXls(this.ui));
    export.add(new ExportLeaderboard(this.ui));

    export.setEnabled(false);
    print.setEnabled(false);
    saveButton.setEnabled(false);
    saveAsButton.setEnabled(false);
    exportXls.setEnabled(false);
    exportWord.setEnabled(false);

    add(edit = new JMenu(this.ui.getSettings().getResourceBundle().getString("edit")));
    edit.add(search = new Search(this.ui));
    edit.add(showList = new JCheckBoxMenuItem(this.ui.getSettings().getResourceBundle().getString("showPlayersList")));
    edit.add(new JSeparator());
    edit.add(new SettingsButton(this.ui));

    search.setEnabled(false);

    showList.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        if (ui.getListPlayers() != null) {
          ui.getListPlayers().updateList();
          if (((JCheckBoxMenuItem) e.getSource()).getState()) {
            ui.getListPlayers().setVisible(true);
          } else {
            ui.getListPlayers().setVisible(false);
          }
        } else {
          ui.setListPlayers(new PanelList(ui));
          ui.getListPlayers().updateList();
          ui.getListPlayers().setVisible(true);
        }

        validate();
      }
    });

    add(game = new JMenu(this.ui.getSettings().getResourceBundle().getString("game")));
    game.add(start = new GameStarter(this.ui));
    game.add(openGame = new OpenGameFile(this.ui));
    openGame.setEnabled(false);
    start.setEnabled(false);

    add(about = new JMenu(this.ui.getSettings().getResourceBundle().getString("about")));
    about.add(new UpdateButton(this.ui, this.ui.getRelease()));
    about.add(new About(this.ui));
  }

  public JMenu getFile() {
    return file;
  }

  public JMenu getEdit() {
    return edit;
  }

  public JMenu getAbout() {
    return about;
  }

  public JMenu getExport() {
    return export;
  }

  ExportWordLeaderboard getExportWord() {
    return exportWord;
  }

  SaveFile getSaveAsButton() {
    return saveAsButton;
  }

  public JMenu getGame() {
    return game;
  }

  SaveFile getSaveButton() {
    return saveButton;
  }

  Search getSearch() {
    return search;
  }

  PrintThread getPrint() {
    return print;
  }

  GameStarter getStart() {
    return start;
  }

  OpenGameFile getOpenGame() {
    return openGame;
  }

  NewFile getNewFile() {
    return newFile;
  }

  OpenFile getOpenFile() {
    return openFile;
  }

  ExportXls getExportXls() {
    return exportXls;
  }

  JCheckBoxMenuItem getShowList() {
    return showList;
  }
}