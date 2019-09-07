package Update;

import Interface.UserController;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class GitUpload {
  private UserController ui;
  private static String file = "Data.cda";

  public GitUpload(UserController ui) {
    this.ui = ui;

    String input = JOptionPane.showInputDialog(ui,
        ui.getSettings().getResourceBundle().getString("uploadText"),
        ui.getSettings().getResourceBundle().getString("uploadTitle"),
        JOptionPane.INFORMATION_MESSAGE);

    if (input != null && !input.equals("")) {
      try {
        File repoFolder = new File(ui.getSettings().getOpenedFile().substring(0, ui.getSettings().getOpenedFile().lastIndexOf("\\")+1) + "REPO_CDA");

        /*
        if (repoFolder.exists()) {
          String[] entries = repoFolder.list();

          for (String s: Objects.requireNonNull(entries)) {
            File currentFile = new File(repoFolder.getPath(), s);
            currentFile.delete();
          }

          /*
          for (String s: Objects.requireNonNull(entries)) {
            File currentFile = new File(repoFolder.getPath(), s);
            currentFile.delete();
          }

          if (repoFolder.delete()) {
            repoFolder.mkdir();
          }
        } else {
          repoFolder.mkdir();
        }
        */

        Git git = Git.cloneRepository()
            .setURI("https://github.com/LordRibblesdale/CotecchioEditor.git")
            .setDirectory(repoFolder)
            .call();

        System.out.println(repoFolder + File.separator + file);

        Files.copy((new File(ui.getSettings().getOpenedFile())).toPath(),
            new FileOutputStream(repoFolder.getAbsolutePath() + File.separator + file));

        git.add().addFilepattern((new File(repoFolder.getAbsolutePath() + File.separator + file)).getPath());
        git.commit().setMessage("+ Update Data file").call();

        String remoteUrl = "https://" + input + "@github.com/LordRibblesdale/CotecchioEditor.git";
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(input, "" );
        git.push().setRemote(remoteUrl).setCredentialsProvider(credentialsProvider).call();
      } catch (IOException | GitAPIException e) {
        e.printStackTrace();
      }
    }
  }
}
