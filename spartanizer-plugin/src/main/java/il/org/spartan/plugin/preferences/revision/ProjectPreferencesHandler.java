package il.org.spartan.plugin.preferences.revision;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.DocumentChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;

import fluent.ly.lazy;
import fluent.ly.note;
import il.org.spartan.plugin.preferences.revision.XMLSpartan.SpartanCategory;
import il.org.spartan.plugin.preferences.revision.XMLSpartan.SpartanElement;
import il.org.spartan.plugin.preferences.revision.XMLSpartan.SpartanTipper;
import il.org.spartan.spartanizer.plugin.Dialogs;
import il.org.spartan.spartanizer.plugin.Eclipse;
import il.org.spartan.spartanizer.plugin.Selection;
import il.org.spartan.spartanizer.plugin.Wizard;
import il.org.spartan.utils.Example;
import il.org.spartan.utils.Example.Converts;
import il.org.spartan.utils.Examples;

/** An handler for project configuration. User configuration is saved in a
 * designated XML file, see {@link XMLSpartan}.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since b0a7-0b-0a */
public class ProjectPreferencesHandler extends AbstractHandler {
  private static final lazy<CodeFormatter> formatter = lazy.get(() -> ToolFactory.createCodeFormatter(null));

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
    final Map<SpartanCategory, SpartanElement[]> m = XMLSpartan.getElementsByCategories(p);
    final SpartanPreferencesDialog d = getDialog(m);
    if (d == null)
      return null;
    final Set<String> $ = getPreferencesChanges(d, toEnabledSet(m));
    return $ == null ? null : commit(p, $);
  }
  /** Initiates configuration change for the project. This includes one dialog
   * opening. This method does not open the XML file, but uses given enabled
   * tippers collection. It also uses given commit method. This execution method
   * is used in order to allow more flexible uses of thi x* @param p JDsi
   * * @param m enabled tippers to be used in dialog
   * @param commit what to do with the dialog's result
   * @return null */
  public static Object execute(final IProject p, final Map<SpartanCategory, SpartanElement[]> m,
      final BiFunction<IProject, Set<String>, Void> commit) {
    final SpartanPreferencesDialog d = getDialog(m);
    if (d == null)
      return null;
    final Set<String> $ = getPreferencesChanges(d, toEnabledSet(m));
    return $ == null ? null : commit.apply(p, $);
  }
  /** Commits enabled tippers for the project, see
   * {@link XMLSpartan#updateEnabledTippers}.
   * @param p JD
   * @param pc enabled tippers
   * @return null */
  public static Void commit(final IProject p, final Collection<String> pc) {
    XMLSpartan.updateEnabledTippers(p, pc);
    try {
      Eclipse.refreshProject(p);
    } catch (InvocationTargetException | CoreException ¢) {
      note.bug(¢);
    } catch (final InterruptedException ¢) {
      note.cancel(¢);
    }
    return null;
  }
  /** @param ¢ dialog
   * @param initialPreferences preferences at the start of the process
   * @return dialog's result: either enabled tippers, or null if the operation
   *         has been canceled by the user or nothing has been changed */
  public static Set<String> getPreferencesChanges(final SpartanPreferencesDialog ¢, final Set<String> initialPreferences) {
    ¢.open();
    final Object[] changes = ¢.getResult();
    final Set<String> $ = changes == null || ¢.getReturnCode() != Window.OK ? null
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
  private static SpartanPreferencesDialog getDialog(final Map<SpartanCategory, SpartanElement[]> m) {
    if (Display.getCurrent().getActiveShell() == null || m == null)
      return null;
    final List<SpartanElement> _es = m.keySet().stream()
        .filter(λ -> λ.parent == null && λ.categoryClass() != il.org.spartan.spartanizer.tipping.categories.Category.class)
        .collect(Collectors.toList());
    final SpartanElement[] es = _es.toArray(new SpartanElement[_es.size()]);
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
        return ¢ instanceof SpartanElement && ((SpartanElement) ¢).hasChildren();
      }
      @Override public Object getParent(final Object ¢) {
        return !(¢ instanceof SpartanElement) ? null : ((SpartanElement) ¢).parent;
      }
      @Override public Object[] getElements(@SuppressWarnings("unused") final Object __) {
        return es;
      }
      @Override public Object[] getChildren(final Object parentElement) {
        return !(parentElement instanceof SpartanCategory) ? null : ((SpartanCategory) parentElement).getChildren();
      }
    });
    return getDialog(m, $);
  }
private static SpartanPreferencesDialog getDialog(final Map<SpartanCategory, SpartanElement[]> m,
		final SpartanPreferencesDialog $) {
	$.setTitle("Spartanization Preferences");
    $.setMessage("Choose the tippers you would like to use:\n(Tip: double click a tipper to see usage examples)");
    $.setEmptyListMessage("No tippers available... something went totally wrong!");
    $.setContainerMode(true);
    $.setInput(new Object()); // vio: very important object
    final Collection<SpartanElement> et = an.empty.list();
    for (final SpartanCategory ¢ : m.keySet())
      collectEnabledTippersInto(¢, et);
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
      final CheckboxTreeViewer ret = super.createTreeViewer(parent);
      // addSelectionListener($); // deprecated method- by click
      final Map<SpartanElement, ToolTip> tooltips = new HashMap<>();
      final Map<ToolTip, Rectangle> bounds = new HashMap<>();
      ret.getTree().addListener(SWT.MouseHover, new Listener() {
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
          if (o instanceof SpartanElement)
            createTooltip((SpartanElement) o, i.getBounds());
        }
        void createTooltip(final SpartanElement e, final Rectangle r) {
          tooltips.values().forEach(λ -> λ.setVisible(false));
          if (!tooltips.containsKey(e)) {
            final ToolTip tt = new ToolTip(getShell(), SWT.ICON_INFORMATION);
            tt.setMessage(e.description());
            tt.setAutoHide(true);
            tooltips.put(e, tt);
          }
          final Rectangle tp = ret.getTree().getBounds();
          final Point tl = Display.getCurrent().getActiveShell().toDisplay(tp.x + r.x, tp.y + r.y);
          final Rectangle tr = new Rectangle(tl.x, tl.y, r.width, r.height);
          final ToolTip tt = tooltips.get(e);
          bounds.put(tt, tr);
          tt.setLocation(tr.x + tr.width, tr.y);
          tt.setVisible(true);
        }
      });
      ret.getTree().addListener(SWT.MouseMove, e -> {
        for (final ToolTip ¢ : tooltips.values())
          if (¢.isVisible()) {
            final Rectangle tp = ret.getTree().getBounds();
            if (!bounds.get(¢).contains(Display.getCurrent().getActiveShell().toDisplay(tp.x + e.x, tp.y + e.y)))
              ¢.setVisible(false);
            break;
          }
      });
      ret.getTree().addListener(SWT.MouseWheel, e -> tooltips.values().forEach(λ -> λ.setVisible(false)));
      ret.addDoubleClickListener(new IDoubleClickListener() {
        @Override public void doubleClick(final DoubleClickEvent e) {
          final ISelection s = e.getSelection();
          if (s == null || s.isEmpty() || !(s instanceof TreeSelection))
            return;
          final Object o = ((IStructuredSelection) s).getFirstElement();
          if (!(o instanceof SpartanTipper))
            return;
          final SpartanTipper st = (SpartanTipper) o;
          final String before = getPreviewString(st.preview, λ -> Boolean.valueOf(λ instanceof Converts), λ -> prettify(((Converts) λ).from()));
          final IDocument d = new Document(before);
          try {
            final String after = getPreviewString(st.preview, λ -> Boolean.valueOf(λ instanceof Converts), λ -> prettify(((Converts) λ).to()));
            if (new RefactoringWizardOpenOperation(new Wizard(new Refactoring() {
              @Override public String getName() {
                return st.name();
              }
              @Override public Change createChange(@SuppressWarnings("unused") final IProgressMonitor pm) throws OperationCanceledException {
                 final DocumentChange $ = new DocumentChange(st.name(), d);
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
              ret.setChecked(st, true);
          } catch (final InterruptedException ¢¢) {
            note.cancel(this, ¢¢);
          }
        }
      });
      return ret;
    }
  }

  /** Returns preview as a single string.
   * @param preview tipper's preview
   * @param filter examples filter
   * @param converter Example --> String converter
   * @return unified examples string */
  static String getPreviewString(final Examples preview, final Function<Example, Boolean> filter, final Function<Example, String> converter) {
    if (preview == null || StreamSupport.stream(preview.spliterator(), false).filter(λ -> filter.apply(λ).booleanValue()).count() == 0)
      return "[no available examples]";
    final StringBuilder $ = new StringBuilder();
    int c = 1;
    for (final Example ¢ : preview)
      if (filter.apply(¢).booleanValue())
        $.append("/* Example ").append(c++).append(" */\n").append(converter.apply(¢)).append("\n\n");
    return ($ + "").trim();
  }
  static String prettify(final String code) {
    final TextEdit e = formatter.get().format(CodeFormatter.K_UNKNOWN, code, 0, code.length(), 0, null);
    if (e == null)
      return code;
    final IDocument $ = new Document(code);
    try {
      e.apply($);
    } catch (MalformedTreeException | BadLocationException ¢) {
      note.bug(¢);
    }
    return $.get();
  }
  private static Set<String> toEnabledSet(final Map<SpartanCategory, SpartanElement[]> m) {
    return m.values().stream().reduce(new HashSet<String>(), (s, a) -> {
      s.addAll(Arrays.stream(a).filter(SpartanElement::enabled).map(SpartanElement::name).collect(toList()));
      return s;
    }, (s1, s2) -> {
      s1.addAll(s2);
      return s1;
    });
  }
  private static void collectEnabledTippersInto(final SpartanElement c, final Collection<SpartanElement> et) {
    if (c instanceof SpartanTipper && c.enabled())
      et.add(c);
    else
      Arrays.stream(c.getChildren()).forEach(λ -> collectEnabledTippersInto(λ, et));
  }
}
