package il.org.spartan.refactoring.suggestions;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.annotation.*;

import static il.org.spartan.lazy.Cookbook.*;

import il.org.spartan.lazy.*;

/**
 * @author Yossi Gil
 *
 * @since 2016`
 */
public class Monitored implements Cookbook {
  /**
   * notify of work being done
   *
   * @param <T>
   *          JD
   * @return OK
   */
  public <@Nullable T> T work() {
    progressMonitor().worked(1);
    return null;
  }
  /**
   * notify task beginning
   *
   * @param <T>
   *          JD
   * @return <code><b>bottom</b></code>
   */
  public final <@Nullable T> T begin() {
    return begin(defaultName());
  }
  @SuppressWarnings("static-method") String defaultName() {
    return "";
  }
  @SuppressWarnings("static-method") int defaultWork() {
    return IProgressMonitor.UNKNOWN;
  }
  /**
   * notify task beginning
   *
   * @param name
   *          JD
   * @param <T>
   *          JD
   * @return <code><b>bottom</b></code>
   */
  public final <@Nullable T> T begin(final String name) {
    return begin(name, defaultWork());
  }
  /**
   * notify task beginning
   *
   * @param name
   *          name of task
   * @param totalWork
   *          total work to be executed
   * @param <T>
   *          JD
   * @return <code><b>bottom</b></code>
   */
  public <@Nullable T> T begin(final String name, final int totalWork) {
    progressMonitor().beginTask(name, totalWork);
    return null;
  }
  /**
   * notify task ending
   *
   * @param <T>
   *          JD
   * @return <code><b>bottom</b></code>
   */
  public <@Nullable T> T end() {
    progressMonitor().done();
    return null;
  }
  /** @return the underlying cell */
  public final IProgressMonitor progressMonitor() {
    return progressMonitor.get();
  }

  final Cell<IProgressMonitor> progressMonitor = cook(() -> new NullProgressMonitor());
}

