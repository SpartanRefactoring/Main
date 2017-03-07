package il.org.spartan.plugin.preferences.revision;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.ltk.ui.refactoring.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.text.edits.*;
import org.eclipse.ui.dialogs.*;

import static java.util.stream.Collectors.*;

import il.org.spartan.plugin.*;
import il.org.spartan.plugin.preferences.revision.XMLSpartan.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.Tipper.*;
import il.org.spartan.spartanizer.tipping.Tipper.Example.*;
import il.org.spartan.spartanizer.utils.*;

/** An handler for project configuration. User configuration is saved in a
 * designated XML file, see {@link XMLSpartan}.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since b0a7-0b-0a */
public class ProjectPreferencesHandler extends AbstractHandler {
  private static final boolean REFRESH_OPENS_DIALOG = false;

  /* (non-Javadoc)
   *
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
   * ExecutionEvent) */
  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    return execute(Selection.Util.project());
  }

  /** Initiates configuration change for the project. This includes one dialog
   * opening.
   * @param p JD
   * @return null */
  public static Object execute(final IProject p) {
    final SpartanPreferencesDialog d = getDialog(p);
    if (d == null)
      return null;
    final Set<String> $ = getPreferencesChanges(d);
    return $ == null ? null : commit(p, $);
  }

  /** Initiates configuration change for the project. This includes one dialog
   * opening. This method does not open the XML file, but uses given enabled
   * tippers collection. It also uses given commit method. This execution method
   * is used in order to allow more flexible uses of thi x* @param p JDsi
   * * @param m enabled tippers to be used in dialog
   * @param commit what to do with the dialog's result
   * @return null */
  public static Object execute(final IProject p, final Map<SpartanCategory, SpartanTipper[]> m,
      final BiFunction<IProject, Set<String>, Void> commit) {
    final SpartanPreferencesDialog d = getDialog(m);
    if (d == null)
      return null;
    final Set<String> $ = getPreferencesChanges(d);
    return $ == null ? null : commit.apply(p, $);
  }

  /** Commits enabled tippers for the project, see
   * {@link XMLSpartan#updateEnabledTippers}.
   * @param p JD
   * @param pc enabled tippers
   * @return null */
  public static Object commit(final IProject p, final Collection<String> pc) {
    XMLSpartan.updateEnabledTippers(p, pc);
    try {
      refreshProject(p);
    } catch (InvocationTargetException | CoreException | InterruptedException ¢) {
      monitor.log(¢);
    }
    return null;
  }

  /** @param ¢ dialog
   * @return dialog's result: either enabled tippers, or null if the operation
   *         has been cancled by the user */
  public static Set<String> getPreferencesChanges(final SpartanPreferencesDialog ¢) {
    ¢.open();
    final Object[] $ = ¢.getResult();
    return $ == null || ¢.getReturnCode() != Window.OK ? null
        : Stream.of($)//
            .filter(SpartanTipper.class::isInstance)//
            .map(SpartanTipper.class::cast)//
            .map(SpartanTipper::name)//
            .collect(toSet());
  }

  /** @param ¢ JD
   * @return preferences configuration dialog for project */
  private static SpartanPreferencesDialog getDialog(final IProject ¢) {
    return getDialog(XMLSpartan.getTippersByCategories(¢));
  }

  /** @param m enabled tippers collection
   * @return preferences configuration dialog for project, using given enabled
   *         tippers */
  private static SpartanPreferencesDialog getDialog(final Map<SpartanCategory, SpartanTipper[]> m) {
    if (Display.getCurrent().getActiveShell() == null || m == null)
      return null;
    final SpartanElement[] es = m.keySet().toArray(new SpartanElement[m.size()]);
    final SpartanPreferencesDialog $ = new SpartanPreferencesDialog(Display.getDefault().getActiveShell(), new ILabelProvider() {
      @Override public void removeListener(@SuppressWarnings("unused") final ILabelProviderListener __) {
        //
      }

      @Override @SuppressWarnings("unused") public boolean isLabelProperty(final Object __, final String property) {
        return false;
      }

      @Override public void dispose() {
        //
      }

      @Override public void addListener(@SuppressWarnings("unused") final ILabelProviderListener __) {
        //
      }

      @Override public String getText(final Object ¢) {
        return ¢ == null ? "" : !(¢ instanceof SpartanElement) ? ¢ + "" : ((SpartanElement) ¢).name();
      }

      @Override public Image getImage(final Object ¢) {
        return ¢ instanceof SpartanTipper ? Dialogs.image(Dialogs.ICON) : ¢ instanceof SpartanCategory ? Dialogs.image(Dialogs.CATEGORY) : null;
      }
    }, new ITreeContentProvider() {
      @Override public boolean hasChildren(final Object ¢) {
        return ¢ instanceof SpartanCategory && ((SpartanElement) ¢).hasChildren();
      }

      @Override public Object getParent(final Object ¢) {
        return !(¢ instanceof SpartanTipper) ? null : ((SpartanTipper) ¢).parent();
      }

      @Override public Object[] getElements(@SuppressWarnings("unused") final Object __) {
        return es;
      }

      @Override public Object[] getChildren(final Object parentElement) {
        return !(parentElement instanceof SpartanCategory) ? null : m.get(parentElement);
      }
    });
    $.setTitle("Spartanization Preferences");
    $.setMessage("Choose the tippers you would like to use:");
    $.setEmptyListMessage("No tippers available...");
    $.setContainerMode(true);
    $.setInput(new Object()); // vio: very important object
    final Collection<SpartanElement> et = new ArrayList<>();
    for (final SpartanCategory c : m.keySet()) {
      boolean enabled = true;
      for (final SpartanTipper ¢ : m.get(c))
        if (¢.enabled())
          et.add(¢);
        else
          enabled = false;
      if (enabled)
        et.add(c);
    }
    $.setInitialSelections(et.toArray(new SpartanElement[et.size()]));
    $.setHelpAvailable(false);
    $.setComparator(new ViewerComparator(String::compareToIgnoreCase));
    return $;
  }

  /** Dialog used for the plugin's preferences change by the user.
   * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
   * @since 2017-02-10 */
  static class SpartanPreferencesDialog extends CheckedTreeSelectionDialog {
    public SpartanPreferencesDialog(final Shell parent, final ILabelProvider labelProvider, final ITreeContentProvider contentProvider) {
      super(parent, labelProvider, contentProvider);
    }

    /* (non-Javadoc)
     *
     * @see
     * org.eclipse.ui.dialogs.CheckedTreeSelectionDialog#createTreeViewer(org.
     * eclipse.swt.widgets.Composite) */
    @Override protected CheckboxTreeViewer createTreeViewer(final Composite parent) {
      final CheckboxTreeViewer $ = super.createTreeViewer(parent);
      // addSelectionListener($); // deprecated method- by click
      final Map<SpartanTipper, ToolTip> tooltips = new HashMap<>();
      final Map<ToolTip, Rectangle> bounds = new HashMap<>();
      $.getTree().addListener(SWT.MouseHover, new Listener() {
        @Override public void handleEvent(final Event e) {
          if (e == null)
            return;
          final Widget w = e.widget;
          final Rectangle r = e.getBounds();
          if (!(w instanceof Tree) || r == null)
            return;
          final TreeItem i = ((Tree) w).getItem(new Point(r.x, r.y));
          if (i == null)
            return;
          final Object o = i.getData();
          if (o instanceof SpartanTipper)
            createTooltip((SpartanTipper) o, i.getBounds());
        }

        void createTooltip(final SpartanTipper t, final Rectangle r) {
          tooltips.values().forEach(λ -> λ.setVisible(false));
          if (!tooltips.containsKey(t)) {
            final ToolTip tt = new ToolTip(getShell(), SWT.ICON_INFORMATION);
            tt.setMessage(t.description());
            tt.setAutoHide(true);
            tooltips.put(t, tt);
          }
          final Rectangle tp = $.getTree().getBounds();
          final Point tl = Display.getCurrent().getActiveShell().toDisplay(tp.x + r.x, tp.y + r.y);
          final Rectangle tr = new Rectangle(tl.x, tl.y, r.width, r.height);
          final ToolTip tt = tooltips.get(t);
          bounds.put(tt, tr);
          tt.setLocation(tr.x + tr.width, tr.y);
          tt.setVisible(true);
        }
      });
      $.getTree().addListener(SWT.MouseMove, e -> {
        for (final ToolTip ¢ : tooltips.values())
          if (¢.isVisible()) {
            final Rectangle tp = $.getTree().getBounds();
            if (!bounds.get(¢).contains(Display.getCurrent().getActiveShell().toDisplay(tp.x + e.x, tp.y + e.y)))
              ¢.setVisible(false);
            break;
          }
      });
      $.getTree().addListener(SWT.MouseWheel, e -> tooltips.values().forEach(λ -> λ.setVisible(false)));
      $.addDoubleClickListener(new IDoubleClickListener() {
        @Override public void doubleClick(final DoubleClickEvent e) {
          final ISelection s = e.getSelection();
          if (s == null || s.isEmpty() || !(s instanceof TreeSelection))
            return;
          final Object o = ((IStructuredSelection) s).getFirstElement();
          if (!(o instanceof SpartanTipper))
            return;
          final SpartanTipper st = (SpartanTipper) o;
          final String before = getPreviewString(st.preview(), ex -> Boolean.valueOf(ex instanceof Converts), ex -> ((Converts) ex).from());
          final IDocument d = new Document(before);
          try {
            final String after = getPreviewString(st.preview(), ex -> Boolean.valueOf(ex instanceof Converts), ex -> ((Converts) ex).to());
            if (new RefactoringWizardOpenOperation(new Wizard(new Refactoring() {
              @Override public String getName() {
                return st.name();
              }

              @Override public Change createChange(@SuppressWarnings("unused") final IProgressMonitor pm) throws OperationCanceledException {
                @SuppressWarnings("hiding") final DocumentChange $ = new DocumentChange(st.name(), d);
                $.setEdit(new ReplaceEdit(0, before.length(), after));
                return $;
              }

              @Override public RefactoringStatus checkInitialConditions(@SuppressWarnings("unused") final IProgressMonitor pm)
                  throws OperationCanceledException {
                return new RefactoringStatus();
              }

              @Override public RefactoringStatus checkFinalConditions(@SuppressWarnings("unused") final IProgressMonitor pm)
                  throws OperationCanceledException {
                return new RefactoringStatus();
              }
            })).run(Display.getCurrent().getActiveShell(), "Tipper Preview") == Window.OK)
              $.setChecked(st, true);
          } catch (final InterruptedException ¢¢) {
            monitor.logCancellationRequest(this, ¢¢);
          }
        }
      });
      return $;
    }
  }

  /** Refreshes project, while applying new configuration.
   * @param p JD
   * @throws CoreException
   * @throws InvocationTargetException
   * @throws InterruptedException */
  private static void refreshProject(final IProject p) throws CoreException, InvocationTargetException, InterruptedException {
    if (p != null && p.isOpen() && p.getNature(Nature.NATURE_ID) != null)
      if (!REFRESH_OPENS_DIALOG)
        new Job("Refreshing " + p.getName()) {
          @Override protected IStatus run(final IProgressMonitor m) {
            try {
              p.build(IncrementalProjectBuilder.FULL_BUILD, m);
              return Status.OK_STATUS;
            } catch (final CoreException ¢) {
              monitor.log(¢);
              return Status.CANCEL_STATUS;
            }
          }
        }.schedule();
      else {
        final ProgressMonitorDialog d = Dialogs.progress(true);
        d.run(true, true, m -> {
          SpartanizationHandler.runAsynchronouslyInUIThread(() -> {
            final Shell s = d.getShell();
            if (s != null)
              s.setText("Refreshing project");
          });
          try {
            p.build(IncrementalProjectBuilder.FULL_BUILD, m);
          } catch (final CoreException ¢) {
            monitor.log(¢);
          }
        });
      }
  }

  /** Returns preview as a single string.
   * @param preview tipper's preview
   * @param filter examples filter
   * @param converter Example --> String converter
   * @return unified examples string */
  static String getPreviewString(final Tipper.Example[] preview, final Function<Example, Boolean> filter, final Function<Example, String> converter) {
    if (preview == null)
      return null;
    final StringBuilder $ = new StringBuilder();
    for (int i = 0, c = 1; i < preview.length; ++i)
      if (filter.apply(preview[i]).booleanValue())
        $.append("/* Example ").append(c++).append(" */\n").append(converter.apply(preview[i])).append("\n\n");
    return ($ + "").trim();
  }
}
