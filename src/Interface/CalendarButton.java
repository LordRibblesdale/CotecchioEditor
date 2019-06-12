package Interface;

import FileManager.SaveFile;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CalendarButton extends AbstractAction {
    private UserController ui;

    CalendarButton(UserController ui) {
        this.ui = ui;

        putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("calendarButton"));
        putValue(Action.SHORT_DESCRIPTION, ui.getSettings().getResourceBundle().getString("calendarDescription"));
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(CalendarButton.class.getResource("History24.gif")));
        putValue(Action.SMALL_ICON, new ImageIcon(CalendarButton.class.getResource("History16.gif")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!ui.hasBeenSaved()) {
            int result = JOptionPane.showConfirmDialog(ui,
                    ui.getSettings().getResourceBundle().getString("saveBeforeClosing"),
                    ui.getSettings().getResourceBundle().getString("exitConfirmation"),
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                new SaveFile(ui).actionPerformed(null);
                ui.setUpData();
                ui.askForNextPage("CALENDAR");
            }
        } else {
            ui.setUpData();
            ui.askForNextPage("CALENDAR");
        }
    }
}
