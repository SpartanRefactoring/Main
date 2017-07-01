package il.org.spartan.spartanizer.plugin;

import static java.util.stream.Collectors.*;

import java.lang.reflect.*;
import java.text.*;
import java.util.*;
import java.util.List;
import java.util.function.*;
import java.util.stream.*;

import javax.tools.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.text.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.commands.*;
import org.eclipse.ui.handlers.*;
import org.eclipse.ui.texteditor.*;

import fluent.ly.*;
import il.org.spartan.*;

/** Eclipse common utilities.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since 2017-03-21 */
public class Eclipse {
  public static Set<IProject> waitingForRefresh = Collections.synchronizedSet(new HashSet<>());
  private static final boolean REFRESH_OPENS_DIALOG = false;
  /** Height of default tooltips. */
  public static final int TOOLTIP_HEIGHT = 25;

  // TODO Roth: switch from system user to eclipse template user
  /** @return user name */
  public static String user() {
    return System.getProperty("user.name");
  }
  /** @return current date */
  public static String date() {
    return date("dd/MM/yyyy");
  }
  // TODO Roth: switch from system date to eclipse template date
  /** @param format date format
   * @return current date */
  public static String date(final String format) {
    return new SimpleDateFormat(format).format(new Date());
  }
  /** @return current mouse location */
  public static Point mouseLocation() {
    return Optional.ofNullable(Display.getCurrent()) //
        .map(Display::getCursorLocation).orElse(new Point(0, 0));
  }
  /** @param mouseUp mouse up operation
   * @param mouseDown mouse down operation
   * @param mouseDoubleClick mouse double click operation
   * @return a {@link MouseListener} that does those actions */
  public static MouseListener mouseListener(final Consumer<MouseEvent> mouseUp, final Consumer<MouseEvent> mouseDown,
      final Consumer<MouseEvent> mouseDoubleClick) {
    return new MouseListener() {
      @Override public void mouseUp(final MouseEvent ¢) {
        mouseUp.accept(¢);
      }
      @Override public void mouseDown(final MouseEvent ¢) {
        mouseDown.accept(¢);
      }
      @Override public void mouseDoubleClick(final MouseEvent ¢) {
        mouseDoubleClick.accept(¢);
      }
    };
  }
  /** Refreshes project, while applying new configuration.
   * @param p JD
   * @throws CoreException
   * @throws InvocationTargetException
   * @throws InterruptedException */
  public static void refreshProject(final IProject p) throws CoreException, InvocationTargetException, InterruptedException {
    if (p != null && p.isOpen() && p.getNature(Nature.NATURE_ID) != null)
      if (!REFRESH_OPENS_DIALOG)
        new Job("Refreshing " + p.getName()) {
          @Override protected IStatus run(final IProgressMonitor m) {
            try {
              waitingForRefresh.add(p);
              p.touch(m);
              return Status.OK_STATUS;
            } catch (final CoreException ¢) {
              note.bug(¢);
              return Status.CANCEL_STATUS;
            }
          }
        }.schedule();
      else {
        final ProgressMonitorDialog d = Dialogs.progress(true);
        d.run(true, true, m -> {
          runAsynchronouslyInUIThread(() -> {
            final Shell s = d.getShell();
            if (s != null)
              s.setText("Refreshing project");
          });
          try {
            p.touch(m);
          } catch (final CoreException ¢) {
            note.bug(¢);
          }
        });
      }
  }
  /** @return current {@link IWorkbenchPage} */
  public static IWorkbenchPage getPage() {
    final IWorkbench w = PlatformUI.getWorkbench();
    if (w == null)
      return null;
    final IWorkbenchWindow ret = w.getActiveWorkbenchWindow();
    final IWorkbenchWindow[] wds = w.getWorkbenchWindows();
    return ret != null ? ret.getActivePage() : wds != null && wds.length > 0 ? wds[0].getActivePage() : null;
  }
  /** @return opened text editors */
  public static Iterable<ITextEditor> openedTextEditors() {
    final IWorkbenchPage ret = getPage();
    return ret == null ? an.empty.list()
        : Stream.of(ret.getEditorReferences()).map(λ -> λ.getEditor(false)).filter(ITextEditor.class::isInstance).map(ITextEditor.class::cast)
            .collect(toList());
  }
  /** @return document for editor */
  public static IDocument document(final ITextEditor ¢) {
    return ¢.getDocumentProvider().getDocument(¢.getEditorInput());
  }
  /** Run asynchronously in UI thread.
   * @param ¢ JD */
  public static void runAsynchronouslyInUIThread(final Runnable ¢) {
    Display.getDefault().asyncExec(¢);
  }
  public static ASTNode coveredNodeByRange(final CompilationUnit u, final ITextSelection s) {
    return new NodeFinder(u, s.getOffset(), Math.max(1, s.getLength())).getCoveredNode();
  }
  public static ASTNode coveringNodeByRange(final CompilationUnit u, final ITextSelection s) {
    return new NodeFinder(u, s.getOffset(), Math.max(1, s.getLength())).getCoveringNode();
  }
  public static boolean recursiveCreateFolder(final IFolder f, final IProgressMonitor m) {
    if (f == null)
      return false;
    if (f.exists())
      return true;
    final IContainer parent = f.getParent();
    if (parent == null)
      return false;
    if (!(parent instanceof IFolder))
      return true;
    if (!recursiveCreateFolder((IFolder) parent, m))
      return false;
    try {
      f.create(IResource.NONE, true, m);
    } catch (final CoreException ¢) {
      note.bug(¢);
      return false;
    }
    return true;
  }
  public static boolean isCompiling(final String filePath) {
    final JavaCompiler c = ToolProvider.getSystemJavaCompiler();
    return c != null && c.run(null, null, null, Objects.requireNonNull(filePath)) == 0;
  }
  public static void commandSetToggle(final String commandId, final boolean toggle) {
    PlatformUI.getWorkbench().getService(ICommandService.class).getCommand(commandId).getState(RegistryToggleState.STATE_ID)
        .setValue(Boolean.valueOf(toggle));
  }
  public static List<IProject> getAllSpartanizerProjects() {
    return Arrays.stream(ResourcesPlugin.getWorkspace().getRoot().getProjects()).filter(p -> {
      try {
        return p.isOpen() && p.getNature(Nature.NATURE_ID) != null;
      } catch (final CoreException ret) {
        note.bug(ret);
        return false;
      }
    }).collect(Collectors.toList());
  }
  @Deprecated @SuppressWarnings("deprecation") public static IProgressMonitor newSubMonitor(final IProgressMonitor ¢) {
    return new SubProgressMonitor(¢, 1, SubProgressMonitor.PREPEND_MAIN_LABEL_TO_SUBTASK);
  }
  public static void refreshAllSpartanizeds() {
    as.list(ResourcesPlugin.getWorkspace().getRoot().getProjects()).forEach(λ -> refreshSpartanized(λ));
  }
  public static void refreshSpartanized(final IProject p) {
    final IProgressMonitor npm = new NullProgressMonitor();
    new Thread(() -> {
      try {
        if (p.isOpen() && p.getNature(Nature.NATURE_ID) != null) {
          waitingForRefresh.add(p);
          p.touch(npm);
        }
        // see issue #767
        // p.build(IncrementalProjectBuilder.FULL_BUILD, npm);
      } catch (final CoreException ¢) {
        note.bug(new Eclipse(), ¢);
      }
    }).run();
  }
  public static void addSpartanizerNature(final IProject p) throws CoreException {
    final IProjectDescription d = p.getDescription();
    final String[] natures = d.getNatureIds();
    if (as.list(natures).contains(Nature.NATURE_ID))
      return; // Already got the nature
    d.setNatureIds(Utils.append(natures, Nature.NATURE_ID));
    p.setDescription(d, null);
  }
}
