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

public class GitUpload implements Runnable {
  private UserController ui;
  private static String file = "Data.cda";
  private static String fileBackup = "Data_Previous.cda";
  private ProgressRenderer progressRenderer;
  private int status = 0;
  private SwingWorker<Void, Void> worker;

  public GitUpload(UserController ui) {
    this.ui = ui;


    worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
         progressRenderer = new ProgressRenderer(ui,
            ui.getSettings().getResourceBundle().getString("uploadingStatus"),
            ui.getSettings().getResourceBundle().getString("initialisation"));

         return null;
      }
    };

    worker.execute();
  }

  @Override
  public void run() {
    //TODO here
    worker.run();
    progressRenderer.setNote(ui.getSettings().getResourceBundle().getString("askingData"));

    String input = JOptionPane.showInputDialog(ui,
        ui.getSettings().getResourceBundle().getString("uploadText"),
        ui.getSettings().getResourceBundle().getString("uploadTitle"),
        JOptionPane.INFORMATION_MESSAGE);

    progressRenderer.setProgress(10);
    if (input != null && !input.equals("")) {
      progressRenderer.setNote(ui.getSettings().getResourceBundle().getString("preparingUpload"));
      progressRenderer.setProgress(10);

      try {
        progressRenderer.setNote(ui.getSettings().getResourceBundle().getString("deletingFolder"));

        File repoFolder = new File(ui.getSettings().getOpenedFile().substring(0, ui.getSettings().getOpenedFile().lastIndexOf("\\")+1) + "REPO_CDA");

        if (repoFolder.exists()) {
          FileUtils.delete(repoFolder, 1);
        }

        progressRenderer.setNote(ui.getSettings().getResourceBundle().getString("creatingFolder"));
        progressRenderer.setProgress(30);

        repoFolder.mkdir();

        progressRenderer.setNote(ui.getSettings().getResourceBundle().getString("creatingGitRepo"));
        progressRenderer.setProgress(40);

        Git git = Git.cloneRepository()
            .setURI("https://github.com/LordRibblesdale/CotecchioEditor.git")
            .setDirectory(repoFolder)
            .call();

        progressRenderer.setProgress(50);

        Files.copy(new File(repoFolder.getAbsolutePath() + File.separator + file).toPath(),
            new FileOutputStream(repoFolder.getAbsolutePath() + File.separator + file));

        Files.copy((new File(ui.getSettings().getOpenedFile())).toPath(),
            new FileOutputStream(repoFolder.getAbsolutePath() + File.separator + file));

        progressRenderer.setProgress(60);

        git.add().addFilepattern((new File(repoFolder.getAbsolutePath() + File.separator + file)).getPath()).call();
        git.add().addFilepattern((new File(repoFolder.getAbsolutePath() + File.separator + fileBackup)).getPath()).call();
        git.commit().setMessage("+ Update Data file").call();

        progressRenderer.setProgress(70);

        String remoteUrl = "https://" + input + "@github.com/LordRibblesdale/CotecchioEditor.git";
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(input, "" );
        git.push().setRemote(remoteUrl).setCredentialsProvider(credentialsProvider).call();

        progressRenderer.setProgress(90);
      } catch (IOException | GitAPIException e) {
        e.printStackTrace();
      }
    }

    progressRenderer.setProgress(100);
    progressRenderer.close();
    status = 1;
  }

  public int getStatus() {
    return status;
  }
}
