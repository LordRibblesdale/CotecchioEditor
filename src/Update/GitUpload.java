package Update;

import Interface.UserController;

import javax.swing.*;

public class GitUpload implements Runnable {
  private UserController ui;
  private int status = 0;
  private UploadThread uploadThread;

  public GitUpload(UserController ui) {
    String input = JOptionPane.showInputDialog(ui.getFrame(),
        ui.getSettings().getResourceBundle().getString("uploadText"),
        ui.getSettings().getResourceBundle().getString("uploadTitle"),
        JOptionPane.INFORMATION_MESSAGE);

    uploadThread = new UploadThread(ui, input);
  }

  @Override
  public void run() {
    uploadThread.start();

    while (uploadThread.isAlive()) {
      if (uploadThread.isInterrupted()) {
        break;
      }
    }

    status = 1;
  }

  public int getStatus() {
    return status;
  }
}
