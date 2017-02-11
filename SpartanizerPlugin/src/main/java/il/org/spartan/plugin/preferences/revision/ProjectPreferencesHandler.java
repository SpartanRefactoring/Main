package il.org.spartan.plugin.preferences.revision;

import java.util.*;
import java.util.stream.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.dialogs.*;

import static java.util.stream.Collectors.*;

import il.org.spartan.plugin.*;
import il.org.spartan.plugin.preferences.revision.XMLSpartan.*;

/** TODO Ori Roth: document class {@link }
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since b0a7-0b-0a */
public class ProjectPreferencesHandler extends AbstractHandler {
  /* (non-Javadoc)
   *
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
   * ExecutionEvent) */
  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    final IProject p = Selection.Util.project();
    final SpartanPreferencesDialog d = getDialog(p);
    if (d == null)
      return null;
    d.open();
    final Object[] os = d.getResult();
    if (os == null || d.getReturnCode() != Window.OK)
      return null;
    XMLSpartan.updateEnabledTippers(p,
        Stream.of(os)//
            .filter(SpartanTipper.class::isInstance)//
            .map(SpartanTipper.class::cast)//
            .map(SpartanTipper::name)//
            .collect(toSet())//
    );
    return null;
  }

  /** TODO Ori Roth: Stub 'ProjectPreferencesHandler::getDialog' (created on
   * 2017-02-09)." );
   * <p>
   * @param p
   * @return */
  private static SpartanPreferencesDialog getDialog(final IProject p) {
    final Shell s = Display.getCurrent().getActiveShell();
    final Map<SpartanCategory, SpartanTipper[]> m = XMLSpartan.getTippersByCategories(p, false);
    if (s == null || m == null)
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

      @Override public Image getImage(@SuppressWarnings("unused") final Object __) {
        return null;
      }
    }, new ITreeContentProvider() {
      @Override public boolean hasChildren(final Object ¢) {
        return ¢ instanceof SpartanCategory && ((SpartanCategory) ¢).hasChildren();
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

  /** TODO Ori Roth: document class {@link ProjectPreferencesHandler}
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
      return $;
    }
  }
}
