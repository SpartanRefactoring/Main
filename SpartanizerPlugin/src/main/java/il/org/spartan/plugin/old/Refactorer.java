package il.org.spartan.plugin.old;

import java.lang.reflect.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.operation.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.plugin.*;
import nano.ly.*;

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

  /** @return whether the refactorer is a handler */
  public static boolean isHandler() {
    return false;
  }

  /** @return whether the refactorer is a marker resolution */
  public boolean isMarkerResolution() {
    return false;
  }

  /** @param e JD
   * @return the applicator used by this refactorer */
  public GUITraversal getApplicator(@SuppressWarnings("unused") final ExecutionEvent __) {
    return null;
  }

  /** @param m JD
   * @return the applicator used by this refactorer */
  public GUITraversal getApplicator(@SuppressWarnings("unused") final IMarker __) {
    return null;
  }

  /** @return the compilation units designated for refactorer */
  public Selection getSelection() {
    return null;
  }

  /** @return the compilation units designated for refactorer */
  public Selection getSelection(@SuppressWarnings("unused") final IMarker __) {
    return null;
  }

  /** Return null for canceled message.
   * @return opening message for given attributes */
  public String getOpeningMessage(@SuppressWarnings("unused") final Map<attribute, Object> __) {
    return null;
  }

  /** Return null for canceled message.
   * @return ending message for given attributes */
  public String getEndingMessage(@SuppressWarnings("unused") final Map<attribute, Object> __) {
    return null;
  }

  /** @return how many pass the refactorer should conduct */
  public int passesCount() {
    return 1;
  }

  /** @param us JD
   * @return message to be displayed by a {@link IProgressMonitor} */
  @SuppressWarnings("unused") public String getProgressMonitorMessage(final List<ICompilationUnit> __, final int pass) {
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

  /** @return whether the refactorer shows a display while working */
  public boolean hasDisplay() {
    return false;
  }

  /** @param m JD
   * @param targetCompilationUnits JD
   * @param helloString JD
   * @return work to be done before running the refactorer main loop */
  @SuppressWarnings("unused") public IRunnableWithProgress initialWork(final GUITraversal __, final List<ICompilationUnit> targetCompilationUnits,
      final Map<attribute, Object> m) {
    return null;
  }

  /** @param m JD
   * @param targetCompilationUnits JD
   * @param helloString JD
   * @return work to be done after running the refactorer main loop */
  @SuppressWarnings("unused") public IRunnableWithProgress finalWork(final GUITraversal __, final List<ICompilationUnit> targetCompilationUnits,
      final Map<attribute, Object> m) {
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
    final GUITraversal applicator = either(getApplicator(e), getApplicator(m));
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
    } catch (InterruptedException | InvocationTargetException $) {
      return note.bug($);
    }
    closeDialog(initialDialog);
    doWork(finalWork(applicator, selection.getCompilationUnits(), attributes), eclipse.progressMonitorDialog(hasDisplay()));
    show(getEndingMessage(attributes));
    return null;
  }

  private Map<attribute, Object> unknowns() {
    final Map<attribute, Object> $ = new HashMap<>();
    Stream.of(attribute.values()).forEach(λ -> $.put(λ, unknown));
    return $;
  }

  private boolean doWork(final IRunnableWithProgress p, final IRunnableContext d) {
    if (p != null)
      try {
        d.run(true, true, p);
        return true;
      } catch (final InvocationTargetException ¢) {
        note.bug(¢);
      } catch (final InterruptedException ¢) {
        note.cancel(this, ¢);
      }
    return false;
  }

  private IRunnableWithProgress runnable(final Selection s, final GUITraversal t, final Map<attribute, Object> m) {
    return pm -> {
      final int $ = passesCount();
      int pass, totalTips = 0;
      final Collection<ICompilationUnit> doneCompilationUnits = an.empty.list(), modifiedCompilationUnits = new HashSet<>();
      for (pass = 0; pass < $ && !finish(pm); ++pass) {
        pm.beginTask(getProgressMonitorMessage(s.getCompilationUnits(), pass), getProgressMonitorWork(s.getCompilationUnits()));
        final List<ICompilationUnit> currentCompilationUnits = currentCompilationUnits(s.getCompilationUnits(), doneCompilationUnits);
        if (currentCompilationUnits.isEmpty()) {
          finish(pm);
          break;
        }
        for (final ICompilationUnit u : currentCompilationUnits) {
          if (pm.isCanceled())
            break;
          pm.subTask(getProgressMonitorSubMessage(currentCompilationUnits, u));
          final int tipsCommited = t.run(u, s.textSelection);
          totalTips += tipsCommited;
          (tipsCommited == 0 ? doneCompilationUnits : modifiedCompilationUnits).add(u);
          (t.run(u, s.textSelection) != 0 ? doneCompilationUnits : modifiedCompilationUnits).add(u);
          pm.worked(1);
        }
      }
      put(m, attribute.CHANGES, modifiedCompilationUnits);
      put(m, attribute.PASSES, Integer.valueOf(pass));
      put(m, attribute.TOTAL_TIPS, Integer.valueOf(totalTips));
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

  private static List<ICompilationUnit> currentCompilationUnits(final Collection<ICompilationUnit> us, final Collection<ICompilationUnit> ds) {
    final List<ICompilationUnit> $ = as.list(us);
    $.removeAll(ds);
    return $;
  }

  private static boolean valid(final Object... ¢) {
    return Stream.of(¢).allMatch(Objects::nonNull);
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
