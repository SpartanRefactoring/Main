package il.org.spartan.plugin.revision;

import static il.org.spartan.plugin.revision.Linguistic.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import il.org.spartan.plugin.*;

/** Both {@link AbstractHandler} and {@link IMarkerResolution} implementations
 * that uses {@link EventApplicator} as its applicator.
 * @author Ori Roth
 * @since 2016 */
public class SpartanizationHandler extends AbstractHandler implements IMarkerResolution {
  private static final String NAME = "Laconic";
  private static final int PASSES = 20;
  private static final int DIALOG_THRESHOLD = 2;

  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    applicator().defaultSelection().go();
    return null;
  }

  @Override public String getLabel() {
    return "Apply Spartanization";
  }

  @Override public void run(final IMarker ¢) {
    applicator().passes(1).selection(Selection.Util.by(¢)).go();
  }

  /** Creates and configures an applicator, without configuring the selection.
   * @return applicator for this handler */
  @SuppressWarnings("boxing") protected static EventApplicator applicator() {
    final EventApplicator $ = new EventApplicator();
    $.passes(PASSES);
    final ProgressMonitorDialog d = Dialogs.progress(false);
    $.listener(EventMapper.empty(event.class) //
        .expend(EventMapper.recorderOf(event.visit_cu).rememberBy(ICompilationUnit.class).does((__, ¢) -> {
          if ($.selection().size() >= DIALOG_THRESHOLD)
            asynch(() -> {
              d.getProgressMonitor().subTask("Spartanizing " + ¢.getElementName());
              d.getProgressMonitor().worked(1);
            });
        })) //
        .expend(EventMapper.recorderOf(event.visit_node).rememberBy(ASTNode.class)) //
        .expend(EventMapper.recorderOf(event.visit_root).rememberLast(String.class)) //
        .expend(EventMapper.recorderOf(event.run_pass).counter().does(¢ -> {
          if ($.selection().size() >= DIALOG_THRESHOLD)
            asynch(() -> {
              d.getProgressMonitor().beginTask(NAME, $.selection().size());
            });
        })) //
        .expend(EventMapper.inspectorOf(event.run_start).does(¢ -> {
          if ($.selection().size() >= DIALOG_THRESHOLD && !Dialogs.ok(Dialogs.message("Spartanizing " + nanable(¢.get(event.visit_root)))))
            $.stop();
        }).does(¢ -> {
          if ($.selection().size() >= DIALOG_THRESHOLD && $.shouldRun())
            asynch(() -> {
              d.open();
            });
        })) //
        .expend(EventMapper.inspectorOf(event.run_finish).does(¢ -> {
          if ($.selection().size() >= DIALOG_THRESHOLD)
            asynch(() -> {
              d.close();
            });
        }).does(¢ -> {
          if ($.selection().size() >= DIALOG_THRESHOLD)
            Dialogs.message("Done spartanizing " + nanable(¢.get(event.visit_root)) //
                + ". Spartanized " + nanable(¢.get(event.visit_root)) //
                + " with " + nanable((Collection<?>) ¢.get(event.visit_cu), c -> {
                  return c.size();
                }) + " files" //
                + " in " + plurales("pass", (AtomicInteger) ¢.get(event.run_pass))).open();
        })));
    $.runContext(r -> {
      // TODO Roth: allow cancellation
      try {
        d.run(true, false, __ -> {
          r.run();
        });
      } catch (InvocationTargetException | InterruptedException x) {
        monitor.log(x);
        x.printStackTrace();
      }
    });
    return $;
  }

  private static void asynch(Runnable ¢) {
    Display.getDefault().asyncExec(¢);
  }
}
