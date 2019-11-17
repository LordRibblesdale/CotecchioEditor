package Update;

import Interface.UserController;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class UploadThread extends Thread {
  private ProgressRenderer worker;
  private UserController ui;

  private static String file = "Data.cda";
  private static String fileBackup = "Data_Previous.cda";

  private String input;

  UploadThread(UserController ui, String input) {
    this.ui = ui;
    this.input = input;
    this.worker = new ProgressRenderer(ui.getFrame(),
        ui.getSettings().getResourceBundle().getString("uploadingStatus"),
        ui.getSettings().getResourceBundle().getString("initialisation"));

  }

  public void run() {
    /*
    Runnable r = new Runnable() {
      @Override
      public void run() {
        worker.setNote(ui.getSettings().getResourceBundle().getString("askingData"));
      }
    };

    ProgressThread.setProgress(worker, r, 10);

    if (input != null && !input.equals("")) {
      r = new Runnable() {
        @Override
        public void run() {
          worker.setNote(ui.getSettings().getResourceBundle().getString("preparingUpload"));
        }
      };

      ProgressThread.setProgress(worker, r, 20);

      File repoFolder = new File(ui.getSettings().getOpenedFile().substring(0, ui.getSettings().getOpenedFile().lastIndexOf("\\")+1) + "REPO_CDA");
      Git git = null;

      r = new Runnable() {
        @Override
        public void run() {
          if (repoFolder.exists()) {
            try {
              FileUtils.delete(repoFolder, 1);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      };

      ProgressThread.setNote(worker, r, ui.getSettings().getResourceBundle().getString("deletingFolder"));

      r = new Runnable() {
        @Override
        public void run() {
          repoFolder.mkdir();
          worker.setNote(ui.getSettings().getResourceBundle().getString("creatingFolder"));
        }
      };

      ProgressThread.setProgress(worker, r, 30);

      r = new Runnable() {
        @Override
        public void run() {
          try {
            Git.cloneRepository()
                .setURI("https://github.com/LordRibblesdale/CotecchioEditor.git")
                .setDirectory(repoFolder)
                .call();
          } catch (GitAPIException e) {
            e.printStackTrace();
          }

          worker.setNote(ui.getSettings().getResourceBundle().getString("creatingGitRepo"));
        }
      };

      ProgressThread.setProgress(worker, r, 40);

      r = new Runnable() {
        @Override
        public void run() {
          try {
            Files.copy(new File(repoFolder.getAbsolutePath() + File.separator + "Data" + File.separator + file).toPath(),
                new FileOutputStream(repoFolder.getAbsolutePath() + File.separator + "Data" + File.separator + fileBackup));

            Files.copy((new File(ui.getSettings().getOpenedFile())).toPath(),
                new FileOutputStream(repoFolder.getAbsolutePath() + File.separator + "Data" + File.separator + file));

          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      };

      ProgressThread.setProgress(worker, r, 50);

      r = new Runnable() {
        @Override
        public void run() {
          try {
            git.add().addFilepattern(".").call();
            git.commit().setMessage("+ Update Data file").call();
          } catch (GitAPIException e) {
            e.printStackTrace();
          }
        }
      };

      ProgressThread.setProgress(worker, r,  60);

      r = new Runnable() {
        @Override
        public void run() {
          String remoteUrl = "https://" + input + "@github.com/LordRibblesdale/CotecchioEditor.git";
          CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(input, "" );
          try {
            git.push().setRemote(remoteUrl).setCredentialsProvider(credentialsProvider).call();
          } catch (GitAPIException e) {
            e.printStackTrace();
          }
        }
      };

      ProgressThread.setProgress(worker, r, 70);
    }

    ui.getFrame().getSaveStatus().setText("Done!");
    worker.close();
    UploadThread.this.interrupt();

     */
  }
}
