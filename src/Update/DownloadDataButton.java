package Update;

import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class DownloadDataButton extends AbstractAction {
    private UserController ui;

    public DownloadDataButton(UserController ui) {
        this.ui = ui;

        putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("downloadData"));
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(DownloadDataButton.class.getResource("WebComponent24.gif")));
        putValue(Action.SMALL_ICON, new ImageIcon(DownloadDataButton.class.getResource("WebComponent16.gif")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ui.getStatus().setText(ui.getSettings().getResourceBundle().getString("downloadingStatus"));
        new DownloadData(ui);
    }
}
