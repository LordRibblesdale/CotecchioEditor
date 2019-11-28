package Update;

import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UpdateButton extends AbstractAction {
    private UserController ui;
    private int version;

    public UpdateButton(UserController ui, int version) {
        this.ui = ui;
        this.version = version;

        putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("checkForUpdates"));
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(UpdateButton.class.getResource("Update.png")));
        putValue(Action.SMALL_ICON, new ImageIcon(UpdateButton.class.getResource("Update.png")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ui.getStatus().setText(ui.getSettings().getResourceBundle().getString("checkingUpdates"));
        new UpdateRepo(ui, version);
    }
}
