package Interface;

import Data.Settings;
import FileManager.AutoSaveFile;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsFrame extends JFrame {
    private final String text = "Set autosave timer: ";
    private Timer autoSave;
    private JLabel timerInfo;
    private JSlider delay;
    private JCheckBox enableTimer;
    private Settings settings;
    private UserController ui;

    public SettingsFrame(UserController ui) {
        super("Settings");
        this.ui = ui;
        this.settings = ui.getSettings();

        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(timerInfo = new JLabel(text + settings.getRefreshSaveRate()/1000 + " seconds"), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 2;
        add(delay = new JSlider(JSlider.HORIZONTAL, 60000, 300000), constraints);
        delay.setValue(settings.getRefreshSaveRate());

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(enableTimer = new JCheckBox("Enabled?"));

        autoSave = new Timer(settings.getRefreshSaveRate(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new AutoSaveFile(settings.getOpenedFile(), ui.getPlayers());
            }
        });

        delay.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                autoSave.setDelay(((JSlider) changeEvent.getSource()).getValue());
                timerInfo.setText(text + delay.getValue());

                validate();
            }
        });

        enableTimer.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if (((JCheckBox) changeEvent.getSource()).isSelected()) {
                    autoSave.stop();
                    delay.setEnabled(false);
                    settings.setRefreshSaveRate(1);
                } else {
                    settings.setRefreshSaveRate(delay.getValue());
                    autoSave.setDelay(delay.getValue());
                    delay.setEnabled(true);
                }

                validate();
            }
        });

        setMinimumSize(new Dimension(300, 450));
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setVisible(false);
    }

    public void startTimer() {
        if (settings.getRefreshSaveRate() != 1) {
            autoSave.start();
        }
    }
    public void stopTimer() {
        autoSave.stop();
    }

    private void saveSettings() {
        ui.setSettings(settings);
    }
}
