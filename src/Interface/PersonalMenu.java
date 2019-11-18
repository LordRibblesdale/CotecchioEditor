package Interface;

import Data.Game;
import Edit.Search;
import Export.ExportWordLeaderboard;
import FileManager.*;
import Update.DownloadDataButton;
import Update.UpdateButton;
import Update.UploadButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class PersonalMenu extends JMenuBar {
  private UserController ui;

  private JMenu file, edit, about, export;

  private SaveFile saveButton;
  private SaveAsFile saveAsButton;
  private Search search;
  private NewFile newFile;
  private OpenFile openFile;
  private ExportWordLeaderboard exportWord;
  private JCheckBoxMenuItem showList;
  private DownloadDataButton downloadDataButton;
  private UploadButton uploadButton;

  private AbstractAction reset;

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
    file.add(downloadDataButton = new DownloadDataButton(ui));
    file.add(new JSeparator());
    file.add(uploadButton = new UploadButton(ui));
    export.add(exportWord = new ExportWordLeaderboard(this.ui));

    export.setEnabled(false);
    saveButton.setEnabled(false);
    saveAsButton.setEnabled(false);
    exportWord.setEnabled(false);
    //uploadButton.setEnabled(false);

    add(edit = new JMenu(this.ui.getSettings().getResourceBundle().getString("edit")));
    edit.add(search = new Search(this.ui));
    edit.add(showList = new JCheckBoxMenuItem(this.ui.getSettings().getResourceBundle().getString("showPlayersList")));
    edit.add(new JSeparator());
    edit.add(reset = new AbstractAction(ui.getSettings().getResourceBundle().getString("resetEditableGames")) {
      @Override
      public void actionPerformed(ActionEvent e) {
        for (Game g : ui.getData().getGame()) {
          g.setEditable(true);
          ui.getFrame().revalidate();
          ui.setHasBeenSaved(false);
        }
      }
    });

    edit.add(new JSeparator());
    edit.add(new SettingsButton(this.ui));

    search.setEnabled(false);
    reset.setEnabled(false);

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

    showList.setEnabled(false);

    add(about = new JMenu(this.ui.getSettings().getResourceBundle().getString("about")));
    about.add(new UpdateButton(this.ui, this.ui.getRelease()));
    about.add(new About(this.ui));
  }

  public JMenu getFile() {
    return file;
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

  SaveFile getSaveButton() {
    return saveButton;
  }

  Search getSearch() {
    return search;
  }

  NewFile getNewFile() {
    return newFile;
  }

  OpenFile getOpenFile() {
    return openFile;
  }

  JCheckBoxMenuItem getShowList() {
    return showList;
  }

  UploadButton getUploadButton() {
    return uploadButton;
  }

  AbstractAction getResetButton() {
    return reset;
  }
}
