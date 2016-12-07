package il.org.spartan.plugin;

import static il.org.spartan.spartanizer.engine.Linguistic.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Both {@link AbstractHandler} and {@link IMarkerResolution} implementations
 * that uses {@link GUIBatchLaconizer} as its applicator.
 * @author Ori Roth
 * @since 2.6 */
public class SpartanizationHandler extends AbstractHandler implements IMarkerResolution {
  private static final int PASSES = 20;
  private static final int DIALOG_THRESHOLD = 2;

  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    final GUIBatchLaconizer a = applicator().defaultSelection();
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
   * @return applicator for this handler */
  public static GUIBatchLaconizer applicator() {
    final GUIBatchLaconizer $ = new GUIBatchLaconizer();
    final Trimmer t = new Trimmer();
    final ProgressMonitorDialog d = Dialogs.progress(false);
    $.runContext(r -> {
      try {
        d.run(true, true, __ -> r.run());
      } catch (InvocationTargetException | InterruptedException ¢) {
        monitor.log(¢);
        ¢.printStackTrace();
      }
    });
    $.defaultRunAction(t);
    $.listener(new Listener() {
      static final int DIALOG_CREATION = 1;
      static final int DIALOG_PROCESSING = 2;
      int level;
      boolean dialogOpen;
      int passes;
      int compilationUnitCount;
      long startTime;

      @Override public void tick(final Object... ¢) {
        runAsynchronouslyInUIThread(() -> {
          d.getProgressMonitor().subTask(Linguistic.trim(separate.these(¢).by(Linguistic.SEPARATOR)));
          d.getProgressMonitor().worked(1);
          if (d.getProgressMonitor().isCanceled())
            $.stop();
        });
        if (passes == 1)
          ++compilationUnitCount;
      }

      @Override public void push(final Object... ¢) {
        switch (++level) {
          case DIALOG_CREATION:
            if ($.selection().size() >= DIALOG_THRESHOLD)
              if (!Dialogs.ok(Dialogs.message(separate.these(¢).by(Linguistic.SEPARATOR))))
                $.stop();
              else {
                dialogOpen = true;
                runAsynchronouslyInUIThread(() -> d.open());
              }
            startTime = System.nanoTime();
            break;
          case DIALOG_PROCESSING:
            if (dialogOpen)
              runAsynchronouslyInUIThread(() -> {
                d.getProgressMonitor().beginTask(Linguistic.trim($.name()) + " : " + separate.these(¢).by(Linguistic.SEPARATOR),
                    $.selection().size());
                if (d.getProgressMonitor().isCanceled())
                  $.stop();
              });
            ++passes;
            break;
          default:
            break;
        }
      }

      /** see issue #467 [[SuppressWarningsSpartan]] */
      @Override public void pop(final Object... ¢) {
        switch (level--) {
          case DIALOG_CREATION:
            if (dialogOpen)
              Dialogs.message(separate.these(new Object[] { //
                  message.title.get(separate.these(¢).by(Linguistic.SEPARATOR)), //
                  message.passes.get(Integer.valueOf(compilationUnitCount), Integer.valueOf(passes)), //
                  message.time.get(Linguistic.time(System.nanoTime() - startTime)) }).by("\n")).open();
            break;
          case DIALOG_PROCESSING:
            break;
          default:
            break;
        }
      }
    });
    return $;
  }

  /** Run asynchronously in UI thread.
   * @param ¢ JD */
  static void runAsynchronouslyInUIThread(final Runnable ¢) {
    Display.getDefault().asyncExec(¢);
  }

  /** Creates and configures an applicator, without configuring the selection.
   * @return applicator for this handler */
  @Deprecated @SuppressWarnings("deprecation") public static GUIBatchLaconizer applicatorMapper() {
    final GUIBatchLaconizer $ = new GUIBatchLaconizer();
    final Trimmer t = new Trimmer();
    final ProgressMonitorDialog d = Dialogs.progress(false);
    final AtomicBoolean openDialog = new AtomicBoolean(false);
    $.listener(EventMapper.empty(event.class).expand(EventMapper.recorderOf(event.visit_cu).rememberBy(WrappedCompilationUnit.class).does((__, ¢) -> {
      if (openDialog.get())
        runAsynchronouslyInUIThread(() -> {
          d.getProgressMonitor().subTask(Linguistic.trim($.selection().inner.indexOf(¢) + "/" + $.selection().size() + "\tSpartanizing " + ¢.name()));
          d.getProgressMonitor().worked(1);
          if (d.getProgressMonitor().isCanceled())
            $.stop();
        });
    })).expand(EventMapper.recorderOf(event.visit_node).rememberBy(ASTNode.class))
        .expand(EventMapper.recorderOf(event.visit_root).rememberLast(String.class))
        .expand(EventMapper.recorderOf(event.run_pass).counter().does(¢ -> {
          if (openDialog.get())
            runAsynchronouslyInUIThread(() -> {
              d.getProgressMonitor().beginTask(Linguistic.trim($.name()), $.selection().size());
              if (d.getProgressMonitor().isCanceled())
                $.stop();
            });
        })).expand(EventMapper.inspectorOf(event.run_start).does(¢ -> {
          if ($.selection().size() >= DIALOG_THRESHOLD)
            if (!Dialogs.ok(Dialogs.message("Spartanizing " + unknownIfNull(¢.get(event.visit_root)))))
              $.stop();
            else {
              runAsynchronouslyInUIThread(() -> d.open());
              openDialog.set(true);
            }
        })).expand(EventMapper.inspectorOf(event.run_finish).does(¢ -> {
          if (openDialog.get())
            runAsynchronouslyInUIThread(() -> d.close());
        }).does(¢ -> {
          if (openDialog.get())
            Dialogs.message("Done spartanizing " + unknownIfNull(¢.get(event.visit_root)) + "\nSpartanized " + unknownIfNull(¢.get(event.visit_root))
                + " with " + unknownIfNull((Collection<?>) ¢.get(event.visit_cu), c -> Integer.valueOf(c.size())) + " files" + " in "
                + plurales("pass", (AtomicInteger) ¢.get(event.run_pass))).open();
        })));
    $.runContext(r -> {
      try {
        d.run(true, true, __ -> r.run());
      } catch (InvocationTargetException | InterruptedException ¢) {
        monitor.log(¢);
        ¢.printStackTrace();
      }
    });
    $.defaultRunAction(t);
    return $;
  }

  /** Printing definition.
   * @author Ori Roth
   * @since 2.6 */
  private enum message {
    title(1, inp -> inp[0] + ""), //
    passes(2, inp -> "Spartanized " + inp[0] + " compilation units in " + Linguistic.plurales("pass", (Integer) inp[1])), //
    time(1, inp -> "Run time " + inp[0] + " seconds");
    private final int inputCount;
    private final Function<Object[], String> printing;

    message(final int inputCount, final Function<Object[], String> printing) {
      this.inputCount = inputCount;
      this.printing = printing;
    }

    public String get(final Object... ¢) {
      assert ¢.length == inputCount;
      return printing.apply(¢);
    }
  }
}
