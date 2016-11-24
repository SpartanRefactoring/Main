package il.org.spartan.plugin.old;

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

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.utils.*;

/** A meta class containing handler and marker resolution strategies.
 * @author Ori Roth
 * @since 2016 */
@SuppressWarnings("static-method")
public abstract class Refactorer extends AbstractHandler implements IMarkerResolution {
  public static final attribute unknown = attribute.UNKNOWN;

  /** Used to collect attributes from a Refactorer's run, used later in printing
   * actions (such as {@link eclipse#announce}) */
  enum attribute {
    EVENT, MARKER, CU, APPLICATOR, PASSES, CHANGES, TIPS_COMMITED, TIPS_BEFORE, TIPS_AFTER, TOTAL_TIPS, TIPPER, UNKNOWN
  }
  // public static final String UNKNOWN = "???";

  /** @return <code><b>true</b></code> <em>iff</em> the refactorer is a
   *         handler */
  public static boolean isHandler() {
    return false;
  }

  /** @return <code><b>true</b></code> <em>iff</em> the refactorer is a marker
   *         resolution */
  public boolean isMarkerResolution() {
    return false;
  }

  /** @param e JD
   * @return the applicator used by this refactorer */
  public AbstractGUIApplicator getApplicator(@SuppressWarnings("unused") final ExecutionEvent e) {
    return null;
  }

  /** @param m JD
   * @return the applicator used by this refactorer */
  public AbstractGUIApplicator getApplicator(@SuppressWarnings("unused") final IMarker m) {
    return null;
  }

  /** @return the compilation units designated for refactorer */
  public Selection getSelection() {
    return null;
  }

  /** @return the compilation units designated for refactorer */
  public Selection getSelection(@SuppressWarnings("unused") final IMarker m) {
    return null;
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

  /** @param us JD
   * @return message to be displayed by a {@link IProgressMonitor} */
  @SuppressWarnings("unused") public String getProgressMonitorMessage(final List<ICompilationUnit> us, final int pass) {
    return getLabel();
  }

  /** @param inner
   * @param currentCompilationUnit
   * @return sub message to be displayed by a {@link IProgressMonitor} */
  @SuppressWarnings("unused") public String getProgressMonitorSubMessage(final List<ICompilationUnit> currentCompilationUnits,
      final ICompilationUnit currentCompilationUnit) {
    return null;
  }

  /** @param us JD
   * @return work to be done by a {@link IProgressMonitor} */
  public int getProgressMonitorWork(@SuppressWarnings("unused") final List<ICompilationUnit> __) {
    return IProgressMonitor.UNKNOWN;
  }

  /** @return <code><b>true</b></code> <em>iff</em> the refactorer shows a
   *         display while working */
  public boolean hasDisplay() {
    return false;
  }

  /** @param a JD
   * @param targetCompilationUnits JD
   * @param a JD
   * @return work to be done before running the refactorer main loop */
  @SuppressWarnings("unused") public IRunnableWithProgress initialWork(final AbstractGUIApplicator __,
      final List<ICompilationUnit> targetCompilationUnits, final Map<attribute, Object> a) {
    return null;
  }

  /** @param a JD
   * @param targetCompilationUnits JD
   * @param a JD
   * @return work to be done after running the refactorer main loop */
  @SuppressWarnings("unused") public IRunnableWithProgress finalWork(final AbstractGUIApplicator __,
      final List<ICompilationUnit> targetCompilationUnits, final Map<attribute, Object> a) {
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
    final Selection selection = either(getSelection(), getSelection(m));
    final AbstractGUIApplicator applicator = either(getApplicator(e), getApplicator(m));
    if (!valid(selection, applicator) || selection.inner.isEmpty())
      return null;
    final Map<attribute, Object> attributes = unknowns();
    put(attributes, attribute.EVENT, e);
    put(attributes, attribute.MARKER, m);
    put(attributes, attribute.CU, selection.inner);
    put(attributes, attribute.APPLICATOR, applicator);
    doWork(initialWork(applicator, selection.getCompilationUnits(), attributes), eclipse.progressMonitorDialog(hasDisplay()));
    final ProgressMonitorDialog progressMonitorDialog = eclipse.progressMonitorDialog(hasDisplay());
    final IRunnableWithProgress r = runnable(selection, applicator, attributes);
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
    doWork(finalWork(applicator, selection.getCompilationUnits(), attributes), eclipse.progressMonitorDialog(hasDisplay()));
    show(getEndingMessage(attributes));
    return null;
  }

  private Map<attribute, Object> unknowns() {
    final Map<attribute, Object> $ = new HashMap<>();
    for (final attribute ¢ : attribute.values())
      $.put(¢, unknown);
    return $;
  }

  private boolean doWork(final IRunnableWithProgress p, final ProgressMonitorDialog d) {
    if (p != null)
      try {
        d.run(true, true, p);
      } catch (InvocationTargetException | InterruptedException x) {
        monitor.log(x);
        x.printStackTrace();
        return false;
      }
    return true;
  }

  private IRunnableWithProgress runnable(final Selection s, final AbstractGUIApplicator a, final Map<attribute, Object> attributes) {
    return new IRunnableWithProgress() {
      @SuppressWarnings("synthetic-access") @Override public void run(final IProgressMonitor pm) {
        final int passesCount = passesCount();
        int pass;
        int totalTips = 0;
        final List<ICompilationUnit> deadCompilationUnits = new ArrayList<>();
        final Set<ICompilationUnit> modifiedCompilationUnits = new HashSet<>();
        for (pass = 0; pass < passesCount && !finish(pm); ++pass) {
          pm.beginTask(getProgressMonitorMessage(s.getCompilationUnits(), pass), getProgressMonitorWork(s.getCompilationUnits()));
          final List<ICompilationUnit> currentCompilationUnits = currentCompilationUnits(s.getCompilationUnits(), deadCompilationUnits);
          if (currentCompilationUnits.isEmpty()) {
            finish(pm);
            break;
          }
          for (final ICompilationUnit u : currentCompilationUnits) {
            if (pm.isCanceled())
              break;
            pm.subTask(getProgressMonitorSubMessage(currentCompilationUnits, u));
            final int tipsCommited = a.fuzzyImplementationApply(u, s.textSelection);
            totalTips += tipsCommited;
            (tipsCommited == 0 ? deadCompilationUnits : modifiedCompilationUnits).add(u);
            (a.fuzzyImplementationApply(u, s.textSelection) != 0 ? deadCompilationUnits : modifiedCompilationUnits).add(u);
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
    final List<ICompilationUnit> $ = new ArrayList<>();
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
