package il.org.spartan.refactoring.contexts;

import il.org.spartan.lazy.*;

import static il.org.spartan.lazy.Environment.function;
import il.org.spartan.lazy.Environment.Property;
import il.org.spartan.refactoring.contexts.Described.*;
import il.org.spartan.refactoring.contexts.Described.Monitored.*;

import static il.org.spartan.lazy.Cookbook.cook;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.annotation.*;

/** @author Yossi Gil
 * @since 2016` */
public class Monitored implements Environment {
  /** Inner class, inheriting all of its container's {@link Property}s, and
   * possibly adding some of its own. Access to container's c {@link Property}
   * is through the {@link #parent} variable.
   * <p>
   * Clients extend this class to create more specialized contexts, adding more
   * {@link Property}s and {@link ¢#recipe(Supplier)}'s.
   * @author Yossi Gil
   * @since 2016` */
  public abstract class ¢ {
    /** the containing instance */
    @SuppressWarnings("hiding") protected final Monitored parent = Monitored.this;
  }
  /** notify of work being done
   * @param <T> JD
   * @return OK */
  public <@Nullable T> T work() {
    progressMonitor().worked(1);
    return null;
  }
  /** notify task beginning
   * @param <T> JD
   * @return <code><b>bottom</b></code> */
  public final <@Nullable T> T begin() {
    return begin(defaultName());
  }
  @SuppressWarnings("static-method") String defaultName() {
    return "";
  }
  @SuppressWarnings("static-method") int defaultWork() {
    return IProgressMonitor.UNKNOWN;
  }
  /** notify task beginning
   * @param name JD
   * @param <T> JD
   * @return <code><b>bottom</b></code> */
  public final <@Nullable T> T begin(final String name) {
    return begin(name, defaultWork());
  }
  /** notify task beginning
   * @param name name of task
   * @param totalWork total work to be executed
   * @param <T> JD
   * @return <code><b>bottom</b></code> */
  public <@Nullable T> T begin(final String name, final int totalWork) {
    progressMonitor().beginTask(name, totalWork);
    return null;
  }
  /** notify task ending
   * @param <T> JD
   * @return <code><b>bottom</b></code> */
  public <@Nullable T> T end() {
    progressMonitor().done();
    return null;
  }
  /** @return the underlying cell */
  public final IProgressMonitor progressMonitor() {
    return progressMonitor.get();
  }

  final Property<IProgressMonitor> progressMonitor = function(() -> new NullProgressMonitor());
}
