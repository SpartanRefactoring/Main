package il.org.spartan.plugin.preferences.revision;

import static java.util.stream.Collectors.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.formatter.*;
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
import org.jetbrains.annotations.*;

import il.org.spartan.plugin.*;
import il.org.spartan.plugin.preferences.revision.XMLSpartan.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.Example.*;

/** An handler for project configuration. User configuration is saved in a
 * designated XML file, see {@link XMLSpartan}.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since b0a7-0b-0a */
public class ProjectPreferencesHandler extends AbstractHandler {
  private static final boolean REFRESH_OPENS_DIALOG = false;
  private static final lazy<CodeFormatter> formatter = lazy.get(() -> ToolFactory.createCodeFormatter(null));

  /* (non-Javadoc)
   *
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
   * ExecutionEvent) */
  @Override @Nullable public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    return execute(Selection.Util.project());
  }

  /** Initiates configuration change for the project. This includes one dialog
   * opening.
   * @param p JD
   * @return null */
  public static Object execute(@NotNull final IProject p) {
    final Map<SpartanCategory, SpartanTipper[]> m = XMLSpartan.getTippersByCategories(p);
    @Nullable final SpartanPreferencesDialog d = getDialog(m);
    if (d == null)
      return null;
    @Nullable final Set<String> $ = getPreferencesChanges(d, toEnabledSet(m));
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
      @NotNull final BiFunction<IProject, Set<String>, Void> commit) {
    @Nullable final SpartanPreferencesDialog d = getDialog(m);
    if (d == null)
      return null;
    @Nullable final Set<String> $ = getPreferencesChanges(d, toEnabledSet(m));
    return $ == null ? null : commit.apply(p, $);
  }

  /** Commits enabled tippers for the project, see
   * {@link XMLSpartan#updateEnabledTippers}.
   * @param p JD
   * @param pc enabled tippers
   * @return null */
  public static Object commit(@NotNull final IProject p, @NotNull final Collection<String> pc) {
    XMLSpartan.updateEnabledTippers(p, pc);
    try {
      refreshProject(p);
    } catch (@NotNull InvocationTargetException | CoreException | InterruptedException ¢) {
      monitor.log(¢);
    }
    return null;
  }

  /** @param ¢ dialog
   * @param initialPreferences preferences at the start of the process
   * @return dialog's result: either enabled tippers, or null if the operation
   *         has been canceled by the user or nothing has been changed */
  public static Set<String> getPreferencesChanges(@NotNull final SpartanPreferencesDialog ¢, final Set<String> initialPreferences) {
    ¢.open();
    final Object[] changes = ¢.getResult();
    Set<String> $ = changes == null || ¢.getReturnCode() != Window.OK ? null
        : Stream.of(changes)//
            .filter(SpartanTipper.class::isInstance)//
            .map(SpartanTipper.class::cast)//
            .map(SpartanTipper::name)//
            .collect(toSet());
    return $ == null || $.containsAll(initialPreferences) && initialPreferences.containsAll($) ? null : $;
  }

  /** @param m enabled tippers collection
   * @return preferences configuration dialog for project, using given enabled
   *         tippers */
  private static SpartanPreferencesDialog getDialog(@Nullable final Map<SpartanCategory, SpartanTipper[]> m) {
    if (Display.getCurrent().getActiveShell() == null || m == null)
      return null;
    @NotNull final SpartanElement[] es = m.keySet().toArray(new SpartanElement[m.size()]);
    @NotNull final SpartanPreferencesDialog $ = new SpartanPreferencesDialog(Display.getDefault().getActiveShell(), new ILabelProvider() {
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

      @Override @NotNull public String getText(@Nullable final Object ¢) {
        return ¢ == null ? "" : !(¢ instanceof SpartanElement) ? ¢ + "" : ((SpartanElement) ¢).name();
      }

      @Override @Nullable public Image getImage(final Object ¢) {
        return ¢ instanceof SpartanTipper ? Dialogs.image(Dialogs.ICON) : ¢ instanceof SpartanCategory ? Dialogs.image(Dialogs.CATEGORY) : null;
      }
    }, new ITreeContentProvider() {
      @Override public boolean hasChildren(final Object ¢) {
        return ¢ instanceof SpartanCategory && ((SpartanElement) ¢).hasChildren();
      }

      @Override @Nullable public Object getParent(final Object ¢) {
        return !(¢ instanceof SpartanTipper) ? null : ((SpartanTipper) ¢).parent();
      }

      @Override @NotNull public Object[] getElements(@SuppressWarnings("unused") final Object __) {
        return es;
      }

      @Override @Nullable public Object[] getChildren(final Object parentElement) {
        return !(parentElement instanceof SpartanCategory) ? null : m.get(parentElement);
      }
    });
    $.setTitle("Spartanization Preferences");
    $.setMessage("Choose the tippers you would like to use:");
    $.setEmptyListMessage("No tippers available...");
    $.setContainerMode(true);
    $.setInput(new Object()); // vio: very important object
    @NotNull final Collection<SpartanElement> et = new ArrayList<>();
    for (final SpartanCategory c : m.keySet()) {
      boolean enabled = true;
      for (@NotNull final SpartanTipper ¢ : m.get(c))
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
   * @author Ori Roth {@code ori.rothh@gmail.com}
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
      @NotNull final Map<SpartanTipper, ToolTip> tooltips = new HashMap<>();
      @NotNull final Map<ToolTip, Rectangle> bounds = new HashMap<>();
      $.getTree().addListener(SWT.MouseHover, new Listener() {
        @Override public void handleEvent(@Nullable final Event e) {
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

        void createTooltip(@NotNull final SpartanTipper t, @NotNull final Rectangle r) {
          tooltips.values().forEach(λ -> λ.setVisible(false));
          if (!tooltips.containsKey(t)) {
            @NotNull final ToolTip tt = new ToolTip(getShell(), SWT.ICON_INFORMATION);
            tt.setMessage(t.description());
            tt.setAutoHide(true);
            tooltips.put(t, tt);
          }
          final Rectangle tp = $.getTree().getBounds();
          final Point tl = Display.getCurrent().getActiveShell().toDisplay(tp.x + r.x, tp.y + r.y);
          @NotNull final Rectangle tr = new Rectangle(tl.x, tl.y, r.width, r.height);
          final ToolTip tt = tooltips.get(t);
          bounds.put(tt, tr);
          tt.setLocation(tr.x + tr.width, tr.y);
          tt.setVisible(true);
        }
      });
      $.getTree().addListener(SWT.MouseMove, e -> {
        for (@NotNull final ToolTip ¢ : tooltips.values())
          if (¢.isVisible()) {
            final Rectangle tp = $.getTree().getBounds();
            if (!bounds.get(¢).contains(Display.getCurrent().getActiveShell().toDisplay(tp.x + e.x, tp.y + e.y)))
              ¢.setVisible(false);
            break;
          }
      });
      $.getTree().addListener(SWT.MouseWheel, e -> tooltips.values().forEach(λ -> λ.setVisible(false)));
      $.addDoubleClickListener(new IDoubleClickListener() {
        @Override public void doubleClick(@NotNull final DoubleClickEvent e) {
          final ISelection s = e.getSelection();
          if (s == null || s.isEmpty() || !(s instanceof TreeSelection))
            return;
          final Object o = ((IStructuredSelection) s).getFirstElement();
          if (!(o instanceof SpartanTipper))
            return;
          @NotNull final SpartanTipper st = (SpartanTipper) o;
          @Nullable final String before = getPreviewString(st.preview(), λ -> Boolean.valueOf(λ instanceof Converts),
              λ -> prettify(((Converts) λ).from()));
          @NotNull final IDocument d = new Document(before);
          try {
            @Nullable final String after = getPreviewString(st.preview(), λ -> Boolean.valueOf(λ instanceof Converts),
                λ -> prettify(((Converts) λ).to()));
            if (new RefactoringWizardOpenOperation(new Wizard(new Refactoring() {
              @Override public String getName() {
                return st.name();
              }

              @Override @NotNull public Change createChange(@SuppressWarnings("unused") final IProgressMonitor pm) throws OperationCanceledException {
                @Nullable @SuppressWarnings("hiding") final DocumentChange $ = new DocumentChange(st.name(), d);
                $.setEdit(new ReplaceEdit(0, before.length(), after));
                return $;
              }

              @Override @NotNull public RefactoringStatus checkInitialConditions(@SuppressWarnings("unused") final IProgressMonitor pm)
                  throws OperationCanceledException {
                return new RefactoringStatus();
              }

              @Override @NotNull public RefactoringStatus checkFinalConditions(@SuppressWarnings("unused") final IProgressMonitor pm)
                  throws OperationCanceledException {
                return new RefactoringStatus();
              }
            })).run(Display.getCurrent().getActiveShell(), "Tipper Preview") == Window.OK)
              $.setChecked(st, true);
          } catch (@NotNull final InterruptedException ¢¢) {
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
  private static void refreshProject(@Nullable final IProject p) throws CoreException, InvocationTargetException, InterruptedException {
    if (p != null && p.isOpen() && p.getNature(Nature.NATURE_ID) != null)
      if (!REFRESH_OPENS_DIALOG)
        new Job("Refreshing " + p.getName()) {
          @Override protected IStatus run(final IProgressMonitor m) {
            try {
              p.build(IncrementalProjectBuilder.FULL_BUILD, m);
              return Status.OK_STATUS;
            } catch (@NotNull final CoreException ¢) {
              monitor.log(¢);
              return Status.CANCEL_STATUS;
            }
          }
        }.schedule();
      else {
        @NotNull final ProgressMonitorDialog d = Dialogs.progress(true);
        d.run(true, true, m -> {
          SpartanizationHandler.runAsynchronouslyInUIThread(() -> {
            final Shell s = d.getShell();
            if (s != null)
              s.setText("Refreshing project");
          });
          try {
            p.build(IncrementalProjectBuilder.FULL_BUILD, m);
          } catch (@NotNull final CoreException ¢) {
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
  @Nullable static String getPreviewString(@Nullable final Example[] preview, @NotNull final Function<Example, Boolean> filter,
      @NotNull final Function<Example, String> converter) {
    if (preview == null)
      return null;
    @NotNull final StringBuilder $ = new StringBuilder();
    for (int ¢ = 0, c = 1; ¢ < preview.length; ++¢)
      if (filter.apply(preview[¢]).booleanValue())
        $.append("/* Example ").append(c++).append(" */\n").append(converter.apply(preview[¢])).append("\n\n");
    return ($ + "").trim();
  }

  static String prettify(@NotNull final String code) {
    final TextEdit e = formatter.get().format(CodeFormatter.K_UNKNOWN, code, 0, code.length(), 0, null);
    if (e == null)
      return code;
    @NotNull final IDocument $ = new Document(code);
    try {
      e.apply($);
    } catch (@NotNull MalformedTreeException | BadLocationException ¢) {
      monitor.log(¢);
    }
    return $.get();
  }

  private static Set<String> toEnabledSet(final Map<SpartanCategory, SpartanTipper[]> m) {
    return m.values().stream().reduce(new HashSet<String>(), (s, a) -> {
      s.addAll(Arrays.stream(a).filter(λ -> λ.enabled()).map(λ -> λ.name()).collect(Collectors.toList()));
      return s;
    }, (s1, s2) -> {
      s1.addAll(s2);
      return s1;
    });
  }
}
