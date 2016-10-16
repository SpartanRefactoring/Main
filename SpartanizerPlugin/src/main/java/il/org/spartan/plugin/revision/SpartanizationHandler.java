package il.org.spartan.plugin.revision;

import static il.org.spartan.plugin.revision.Linguistic.*;

import java.lang.reflect.*;
import java.math.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import il.org.spartan.plugin.*;

/** Both {@link AbstractHandler} and {@link IMarkerResolution} implementations
 * that uses {@link EventApplicator} as its applicator.
 * @author Ori Roth
 * @since 2.6 */
public class SpartanizationHandler extends AbstractHandler implements IMarkerResolution {
  private static final String NAME = "Laconic";
  private static final int PASSES = 20;
  private static final int DIALOG_THRESHOLD = 2;

  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    final EventApplicator a = applicator().defaultSelection();
    a.passes(a.selection().textSelection != null ? 1 : PASSES);
    a.go();
    return null;
  }

  @Override public String getLabel() {
    return "Apply";
  }

  @Override public void run(final IMarker ¢) {
    applicator().passes(1).selection(Selection.Util.by(¢)).go();
  }

  /** Creates and configures an applicator, without configuring the selection.
   * @return applicator for this handler [[SuppressWarningsSpartan]] */
  protected static EventApplicator applicator() {
    final EventApplicator $ = new EventApplicator();
    final ProgressMonitorDialog d = Dialogs.progress(false);
    final Time time = new Time();
    final Flag openDialog = new Flag(false);
    $.listener(EventMapper.empty(event.class) //
        .expend(EventMapper.recorderOf(event.visit_cu).rememberBy(WrappedCompilationUnit.class).does((__, ¢) -> {
          if (openDialog.flag)
            asynch(() -> {
              d.getProgressMonitor().subTask($.selection().compilationUnits.indexOf(¢) + "/" + $.selection().size() + "\tSpartanizing " + ¢.name());
              d.getProgressMonitor().worked(1);
              if (d.getProgressMonitor().isCanceled())
                $.stop();
            });
        })) //
        .expend(EventMapper.recorderOf(event.visit_node).rememberBy(ASTNode.class)) //
        .expend(EventMapper.recorderOf(event.visit_root).rememberLast(String.class)) //
        .expend(EventMapper.recorderOf(event.run_pass).counter().does(¢ -> {
          if (openDialog.flag)
            asynch(() -> {
              d.getProgressMonitor().beginTask(NAME, $.selection().size());
              if (d.getProgressMonitor().isCanceled())
                $.stop();
            });
        })) //
        .expend(EventMapper.inspectorOf(event.run_start).does(¢ -> {
          if ($.selection().size() >= DIALOG_THRESHOLD)
            if (!Dialogs.ok(Dialogs.message("Spartanizing " + nanable(¢.get(event.visit_root)))))
              $.stop();
            else {
              asynch(() -> d.open());
              openDialog.flag = true;
            }
          time.set(System.nanoTime());
        })) //
        .expend(EventMapper.inspectorOf(event.run_finish).does(¢ -> {
          if (openDialog.flag)
            asynch(() -> {
              d.close();
            });
        }).does(¢ -> {
          if (openDialog.flag)
            Dialogs.message("Done spartanizing " + nanable(¢.get(event.visit_root)) //
                + "\nSpartanized " + nanable(¢.get(event.visit_root)) //
                + " with " + nanable((Collection<?>) ¢.get(event.visit_cu), c -> {
                  return Integer.valueOf(c.size());
                }) + " files" //
                + " in " + plurales("pass", (AtomicInteger) ¢.get(event.run_pass)) //
                + "\nTotal run time: " + time.intervalInSeconds(System.nanoTime()) + " seconds").open();
        })));
    $.runContext(r -> {
      try {
        d.run(true, true, __ -> {
          r.run();
        });
      } catch (InvocationTargetException | InterruptedException e) {
        monitor.log(e);
        e.printStackTrace();
      }
    });
    $.defaultRunAction();
    return $;
  }

  /** Run asynchronously in UI thread.
   * @param ¢ JD */
  private static void asynch(final Runnable ¢) {
    Display.getDefault().asyncExec(¢);
  }

  /** Used to measure run time.
   * @author Ori Roth
   * @since 2.6 */
  private static class Time {
    long time;

    public Time() {
      //
    }

    void set(final long ¢) {
      time = ¢;
    }

    double intervalInSeconds(final long ¢) {
      return round((¢ - time) / 1000000000.0, 2);
    }

    private static double round(final double value, final int places) {
      if (places < 0)
        throw new IllegalArgumentException();
      return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }
  }

  /** Mutable boolean.
   * @author Ori Roth
   * @since 2.6 */
  private static class Flag {
    boolean flag;

    public Flag(final boolean b) {
      flag = b;
    }
  }
}
