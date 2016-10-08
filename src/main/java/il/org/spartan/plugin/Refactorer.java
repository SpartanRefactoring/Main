package il.org.spartan.plugin;

import java.lang.reflect.*;
import java.util.*;
import java.util.List;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.operation.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

/** A meta class containing handler and marker resolution strategies.
 * @author Ori Roth
 * @since 2016 */
@SuppressWarnings("static-method") public abstract class Refactorer extends AbstractHandler implements IMarkerResolution {

  public static final attribute UNKNOWN = attribute.UNKNOWN;

  /** Used to collect attributes from a Refactorer's run, used later in printing
   * actions (such as {@link eclipse#announce}) */
  enum attribute {
    EVENT, MARKER, CU, APPLICATOR, PASSES, CHANGES, TIPS_COMMITED, TIPS_BEFORE, TIPS_AFTER, TOTAL_TIPS, TIPPER, UNKNOWN
  }
// public static final String UNKNOWN = "???";

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
  @SuppressWarnings("unused") public String getProgressMonitorMessage(final List<ICompilationUnit> compilationUnits, final int pass) {
    return getLabel();
  }

  /** @param compilationUnits
   * @param currentCompilationUnit
   * @return sub message to be displayed by a {@link IProgressMonitor}
   *         [[SuppressWarningsSpartan]] */
  @SuppressWarnings("unused") public String getProgressMonitorSubMessage(final List<ICompilationUnit> currentCompilationUnits,
      final ICompilationUnit currentCompilationUnit) {
    return null;
  }

  /** @param compilationUnits JD
   * @return work to be done by a {@link IProgressMonitor}
   *         [[SuppressWarningsSpartan]] */
  public int getProgressMonitorWork(@SuppressWarnings("unused") final List<ICompilationUnit> compilationUnits) {
    return IProgressMonitor.UNKNOWN;
  }

  /** @return true iff the refactorer shows a display while working */
  public boolean hasDisplay() {
    return false;
  }

  /** @param applicator JD
   * @param targetCompilationUnits JD
   * @param attributes JD
   * @return work to be done before running the refactorer main loop
   *         [[SuppressWarningsSpartan]] */
  @SuppressWarnings("unused") public IRunnableWithProgress initialWork(final GUI$Applicator applicator,
      final List<ICompilationUnit> targetCompilationUnits, final Map<attribute, Object> attributes) {
    return null;
  }

  /** @param applicator JD
   * @param targetCompilationUnits JD
   * @param attributes JD
   * @return work to be done after running the refactorer main loop
   *         [[SuppressWarningsSpartan]] */
  @SuppressWarnings("unused") public IRunnableWithProgress finalWork(final GUI$Applicator applicator,
      final List<ICompilationUnit> targetCompilationUnits, final Map<attribute, Object> attributes) {
    return null;
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
    final Map<attribute, Object> attributes = unknowns();
    put(attributes, attribute.EVENT, e);
    put(attributes, attribute.MARKER, m);
    put(attributes, attribute.CU, targetCompilationUnits);
    put(attributes, attribute.APPLICATOR, applicator);
    doWork(initialWork(applicator, targetCompilationUnits, attributes), eclipse.progressMonitorDialog(hasDisplay()));
    final ProgressMonitorDialog progressMonitorDialog = eclipse.progressMonitorDialog(hasDisplay());
    final IRunnableWithProgress r = runnable(targetCompilationUnits, applicator, attributes);
    final MessageDialog initialDialog = show(getOpeningMessage(attributes));
    if (hasDisplay())
      initializeProgressDialog(progressMonitorDialog);
    try {
      progressMonitorDialog.run(true, true, r);
    } catch (InterruptedException | InvocationTargetException x) {
      monitor.log(x);
      return null;
    }
    closeDialog(initialDialog);
    doWork(finalWork(applicator, targetCompilationUnits, attributes), eclipse.progressMonitorDialog(hasDisplay()));
    show(getEndingMessage(attributes));
    return null;
  }

  private Map<attribute, Object> unknowns() {
    final Map<attribute, Object> $ = new HashMap<>();
    for (attribute ¢ : attribute.values())
      $.put(¢, UNKNOWN);
    return $;
  }

  /** [[SuppressWarningsSpartan]] */
  private boolean doWork(final IRunnableWithProgress r, final ProgressMonitorDialog d) {
    if (r != null)
      try {
        d.run(true, true, r);
      } catch (InvocationTargetException | InterruptedException x) {
        monitor.log(x);
        x.printStackTrace();
        return false;
      }
    return true;
  }

  private IRunnableWithProgress runnable(final List<ICompilationUnit> us, final GUI$Applicator a, final Map<attribute, Object> attributes) {
    return new IRunnableWithProgress() {
      @SuppressWarnings("synthetic-access") @Override public void run(final IProgressMonitor pm) {
        final int passesCount = passesCount();
        int pass;
        int totalTips = 0;
        final List<ICompilationUnit> deadCompilationUnits = new LinkedList<>();
        final Set<ICompilationUnit> modifiedCompilationUnits = new HashSet<>();
        for (pass = 0; pass < passesCount && !finish(pm); ++pass) {
          pm.beginTask(getProgressMonitorMessage(us, pass), getProgressMonitorWork(us));
          final List<ICompilationUnit> currentCompilationUnits = currentCompilationUnits(us, deadCompilationUnits);
          if (currentCompilationUnits.isEmpty()) {
            finish(pm);
            break;
          }
          for (final ICompilationUnit currentCompilationUnit : currentCompilationUnits) {
            if (pm.isCanceled())
              break;
            pm.subTask(getProgressMonitorSubMessage(currentCompilationUnits, currentCompilationUnit));
            int tipsCommited = a.fuzzyImplementationApply(currentCompilationUnit, a.getSelection());
            totalTips += tipsCommited;
            (tipsCommited == 0 ? deadCompilationUnits : modifiedCompilationUnits).add(currentCompilationUnit);
            (0!=a.fuzzyImplementationApply(currentCompilationUnit, a.getSelection()) ? deadCompilationUnits : modifiedCompilationUnits)
            (a.fuzzyImplementationApply(currentCompilationUnit, a.getSelection()) != 0 ? deadCompilationUnits : modifiedCompilationUnits)
                .add(currentCompilationUnit);
            pm.worked(1);
          }
        }
        put(attributes, attribute.CHANGES, modifiedCompilationUnits);
        put(attributes, attribute.PASSES, Integer.valueOf(pass));
        put(attributes, attribute.TOTAL_TIPS, Integer.valueOf(totalTips));
      }
    };
  }

  private static <T> T either(final T t1, final T t2) {
    return t1 != null ? t1 : t2;
  }

  private static void put(final Map<attribute, Object> m, final attribute a, final Object o) {
    if (o != null)
      m.put(a, o);
  }

  private static MessageDialog show(final String ¢) {
    if (¢ == null)
      return null;
    final MessageDialog $ = eclipse.announceNonBusy(¢);
    $.open();
    return $;
  }

  private void closeDialog(final MessageDialog initialDialog) {
    if (initialDialog != null)
      initialDialog.close();
  }

  private static boolean finish(final IProgressMonitor pm) {
    final boolean $ = pm.isCanceled();
    pm.done();
    return $;
  }

  private static List<ICompilationUnit> currentCompilationUnits(final List<ICompilationUnit> us, final List<ICompilationUnit> ds) {
    final List<ICompilationUnit> $ = new LinkedList<>();
    $.addAll(us);
    $.removeAll(ds);
    return $;
  }

  private static boolean valid(final Object... os) {
    for (final Object ¢ : os)
      if (¢ == null)
        return false;
    return true;
  }

  private static void initializeProgressDialog(final ProgressMonitorDialog d) {
    d.open();
    final Shell s = d.getShell();
    if (s == null)
      return;
    s.setText(eclipse.NAME);
    s.setImage(eclipse.iconNonBusy());
  }
}
