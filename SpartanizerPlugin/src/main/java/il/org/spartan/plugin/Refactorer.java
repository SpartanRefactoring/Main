package il.org.spartan.plugin;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.operation.*;
import org.eclipse.ui.*;
import org.eclipse.ui.progress.*;

/** A meta class containing handler and marker resolution strategies.
 * @author Ori Roth
 * @since 2016 */
@SuppressWarnings("static-method") public abstract class Refactorer extends AbstractHandler implements IMarkerResolution {
  /** Used to collect attributes from a Refactorer's run, used later in printing
   * actions (such as {@link eclipse#announce}) */
  enum attribute {
    EVENT, MARKER, CU, APPLICATOR, PASSES, CHANGES
  }

  /** @return true iff the refactorer is a handler */
  public static boolean isHandler() {
    return false;
  }

  /** @return true iff the refactorer is a marker resolution */
  public boolean isMarkerResolution() {
    return false;
  }

  /** @param e JD
   * @return the applicator used by this refactorer
   *         [[SuppressWarningsSpartan]] */
  public GUI$Applicator getApplicator(@SuppressWarnings("unused") final ExecutionEvent e) {
    return null;
  }

  /** @param m JD
   * @return the applicator used by this refactorer
   *         [[SuppressWarningsSpartan]] */
  public GUI$Applicator getApplicator(@SuppressWarnings("unused") final IMarker m) {
    return null;
  }

  /** @return the compilation units designated for refactorer
   *         [[SuppressWarningsSpartan]] */
  public List<ICompilationUnit> getTargetCompilationUnits() {
    return Collections.emptyList();
  }

  /** Return null for canceled message.
   * @return opening message for given attributes [[SuppressWarningsSpartan]] */
  public String getOpeningMessage(@SuppressWarnings("unused") final Map<attribute, Object> attributes) {
    return null;
  }

  /** Return null for canceled message.
   * @return ending message for given attributes [[SuppressWarningsSpartan]] */
  public String getEndingMessage(@SuppressWarnings("unused") final Map<attribute, Object> attributes) {
    return null;
  }

  /** @return how many pass the refactorer should conduct */
  public int passesCount() {
    return 1;
  }

  /** @param compilationUnits JD
   * @return message to be displayed by a {@link IProgressMonitor}
   *         [[SuppressWarningsSpartan]] */
  @SuppressWarnings("unused") public String getProgressMonitorMessage(List<ICompilationUnit> compilationUnits, int pass) {
    return getLabel();
  }

  /** @param compilationUnits
   * @param currentCompilationUnit
   * @return sub message to be displayed by a {@link IProgressMonitor}
   *         [[SuppressWarningsSpartan]] */
  @SuppressWarnings("unused") public String getProgressMonitorSubMessage(List<ICompilationUnit> currentCompilationUnits,
      ICompilationUnit currentCompilationUnit) {
    return null;
  }

  /** @param compilationUnits JD
   * @return work to be done by a {@link IProgressMonitor}
   *         [[SuppressWarningsSpartan]] */
  public int getProgressMonitorWork(@SuppressWarnings("unused") List<ICompilationUnit> compilationUnits) {
    return IProgressMonitor.UNKNOWN;
  }

  /** @return true iff the refactorer should run with busy cursor */
  public static boolean isBusy() {
    return false;
  }

  @Override public String getLabel() {
    return null;
  }

  @Override public Void execute(final ExecutionEvent ¢) {
    return !isHandler() ? null : go(¢, null);
  }

  @Override public void run(final IMarker ¢) {
    if (isMarkerResolution())
      go(null, ¢);
  }

  private Void go(final ExecutionEvent e, final IMarker m) {
    final List<ICompilationUnit> targetCompilationUnits = getTargetCompilationUnits();
    final GUI$Applicator applicator = either(getApplicator(e), getApplicator(m));
    if (!valid(targetCompilationUnits, applicator))
      return null;
    final Map<attribute, Object> attributes = new HashMap<>();
    put(attributes, attribute.EVENT, e);
    put(attributes, attribute.MARKER, m);
    put(attributes, attribute.CU, targetCompilationUnits);
    put(attributes, attribute.APPLICATOR, applicator);
    show(getOpeningMessage(attributes));
    final IProgressService ps = getProgressService();
    IRunnableWithProgress r = runnable(targetCompilationUnits, applicator, attributes);
    if (ps != null)
      try {
        if (isBusy())
          ps.run(true, true, r);
        else
          ps.busyCursorWhile(r);
      } catch (InterruptedException | InvocationTargetException x) {
        monitor.log(x);
        return null;
      }
    show(getEndingMessage(attributes));
    return null;
  }

  private IRunnableWithProgress runnable(final List<ICompilationUnit> us, final GUI$Applicator a, final Map<attribute, Object> attributes) {
    return new IRunnableWithProgress() {
      @SuppressWarnings("synthetic-access") @Override public void run(IProgressMonitor pm) {
        final int passesCount = passesCount();
        int pass;
        List<ICompilationUnit> deadCompilationUnits = new LinkedList<>();
        Set<ICompilationUnit> modifiedCompilationUnits = new HashSet<>();
        for (pass = 0; pass < passesCount && !done(pm); ++pass) {
          pm.beginTask(getProgressMonitorMessage(us, pass), getProgressMonitorWork(us));
          List<ICompilationUnit> currentCompilationUnits = currentCompilationUnits(us, deadCompilationUnits);
          if (currentCompilationUnits.isEmpty()) {
            done(pm);
            break;
          }
          for (ICompilationUnit currentCompilationUnit : currentCompilationUnits) {
            if (pm.isCanceled())
              break;
            pm.subTask(getProgressMonitorSubMessage(currentCompilationUnits, currentCompilationUnit));
            (!a.fuzzyImplementationApply(currentCompilationUnit, a.getSelection()) ? deadCompilationUnits : modifiedCompilationUnits)
                .add(currentCompilationUnit);
            pm.worked(1);
          }
        }
        put(attributes, attribute.CHANGES, modifiedCompilationUnits);
        put(attributes, attribute.PASSES, Integer.valueOf(pass));
      }
    };
  }

  private static <T> T either(T t1, T t2) {
    return t1 != null ? t1 : t2;
  }

  private static void put(final Map<attribute, Object> m, final attribute a, final Object o) {
    if (o != null)
      m.put(a, o);
  }

  private static void show(String ¢) {
    if (¢ != null)
      eclipse.announce(¢);
  }

  private static IProgressService getProgressService() {
    final IWorkbench wb = PlatformUI.getWorkbench();
    return wb == null ? null : wb.getProgressService();
  }

  private static boolean done(final IProgressMonitor pm) {
    boolean $ = pm.isCanceled();
    pm.done();
    return $;
  }

  private static List<ICompilationUnit> currentCompilationUnits(List<ICompilationUnit> us, List<ICompilationUnit> ds) {
    List<ICompilationUnit> $ = new LinkedList<>();
    $.addAll(us);
    $.removeAll(ds);
    return $;
  }

  private static boolean valid(Object... os) {
    for (Object ¢ : os)
      if (¢ == null)
        return false;
    return true;
  }
}
