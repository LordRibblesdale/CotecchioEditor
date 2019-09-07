package Update;

import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UploadButton extends AbstractAction {
    private UserController ui;
    private int version;

    public UploadButton(UserController ui) {
        this.ui = ui;

        putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("uploadButtonText"));
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(UploadButton.class.getResource("WebComponent24.gif")));
        putValue(Action.SMALL_ICON, new ImageIcon(UploadButton.class.getResource("WebComponent16.gif")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ui.getStatus().setText(ui.getSettings().getResourceBundle().getString("uploadButtonText"));
        new GitUpload(ui);
    }
}
