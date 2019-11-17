package Update;

import javax.swing.*;

public class ProgressThread {
  public static void setProgress(ProgressRenderer renderer, Runnable runnable, int progress) {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        runnable.run();
        return null;
      }

      @Override
      protected void done() {
        super.done();

        renderer.setProgress(progress);
      }
    };

    worker.execute();
  }

  public static void setNote(ProgressRenderer renderer, Runnable runnable, String note) {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        runnable.run();
        return null;
      }

      @Override
      protected void done() {
        super.done();

        renderer.setNote(note);
      }
    };

    worker.execute();
  }
}
