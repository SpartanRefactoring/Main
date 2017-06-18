package il.org.spartan.Leonidas.plugin.GUI.ActionMonitor;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.plugin.SpartanizerBatch;
import il.org.spartan.Leonidas.plugin.SpartanizerBatchListener;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;

/**
 * @author RoeiRaz
 * @since 18/06/17
 */
public class ActionMonitorBatchListener implements SpartanizerBatchListener {

    private final ActionMonitor monitor;
    private int numElements;
    private int numCompleted;

    public ActionMonitorBatchListener() {
        this.monitor = new ActionMonitor();
    }

    @Override
    public void onInvoke(SpartanizerBatch batch) {
        monitor.pack();
        monitor.setVisible(true);
        // TODO this is code smell and bad design. it will be changed @RoeiRaz
        numElements = batch.getElements().size();
        numCompleted = 0;
        monitor.progressBar.setValue(0);
        updateStatusText(0, 0, 0);
    }

    @Override
    public void onTip(SpartanizerBatch batch, PsiElement element, Tipper t) {
        numCompleted++;
        monitor.progressBar.setValue((numCompleted * 100) / numElements);
        consolePrintln("Processing " + element.getClass().getSimpleName() + " in " + element.getContainingFile().getName());
    }

    private void consolePrintln(String line) {
        monitor.OutputConsole.append(line + "\r\n");
    }

    private void updateStatusText(int files, int doneCharacters, int numCharacters) {
        monitor.statusLabel.setText("Performing spartanization across "
                + files
                + "("
                + doneCharacters
                + " / "
                + numCharacters
                + " characters done)");
    }
}
