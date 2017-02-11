package il.org.spartan.plugin.preferences.revision;

import java.util.*;
import java.util.List;

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
    final Set<String> es = new HashSet<>();
    for (final Object ¢ : os)
      if (¢ instanceof SpartanTipper)
        es.add(((SpartanTipper) ¢).name());
    XMLSpartan.updateEnabledTippers(p, es);
    return null;
  }

  /** TODO Ori Roth: Stub 'ProjectPreferencesHandler::getDialog' (created on
   * 2017-02-09)." );
   * <p>
   * @param p
   * @return
   *         <p>
   *         [[SuppressWarningsSpartan]] */
  private static SpartanPreferencesDialog getDialog(final IProject p) {
    final Shell s = Display.getCurrent().getActiveShell();
    final Map<SpartanCategory, SpartanTipper[]> m = XMLSpartan.getTippersByCategories(p, false);
    if (s == null || m == null)
      return null;
    final SpartanElement[] es = m.keySet().toArray(new SpartanElement[m.size()]);
    final SpartanPreferencesDialog $ = new SpartanPreferencesDialog(Display.getDefault().getActiveShell(), new ILabelProvider() {
      @Override public void removeListener(@SuppressWarnings("unused") final ILabelProviderListener listener) {
        //
      }

      @SuppressWarnings("unused") @Override public boolean isLabelProperty(final Object element, final String property) {
        return false;
      }

      @Override public void dispose() {
        //
      }

      @Override public void addListener(@SuppressWarnings("unused") final ILabelProviderListener listener) {
        //
      }

      @Override public String getText(final Object element) {
        return element == null ? "" : element instanceof SpartanElement ? ((SpartanElement) element).name() : element.toString();
      }

      @Override public Image getImage(@SuppressWarnings("unused") final Object __) {
        return null;
      }
    }, new ITreeContentProvider() {
      @Override public boolean hasChildren(final Object element) {
        return element instanceof SpartanCategory && ((SpartanCategory) element).hasChildren();
      }

      @Override public Object getParent(final Object element) {
        return element instanceof SpartanTipper ? ((SpartanTipper) element).parent() : null;
      }

      @Override public Object[] getElements(@SuppressWarnings("unused") final Object __) {
        return es;
      }

      @Override public Object[] getChildren(final Object parentElement) {
        return parentElement instanceof SpartanCategory ? m.get(parentElement) : null;
      }
    });
    $.setTitle("Spartanization Preferences");
    $.setMessage("Choose the tippers you would like to use:");
    $.setEmptyListMessage("No tippers available...");
    $.setContainerMode(true);
    $.setInput(new Object()); // vio: very important object
    final List<SpartanElement> et = new ArrayList<>();
    for (final SpartanCategory c : m.keySet()) {
      boolean enabled = true;
      for (final SpartanTipper t : m.get(c))
        if (t.enabled())
          et.add(t);
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
      Map<SpartanTipper, ToolTip> tooltips = new HashMap<>();
      Map<ToolTip, Rectangle> bounds = new HashMap<>();
      $.getTree().addListener(SWT.MouseHover, new Listener() {
        @Override public void handleEvent(Event e) {
          if (e == null)
            return;
          Widget w = e.widget;
          Rectangle r = e.getBounds();
          if (!(w instanceof Tree) || r == null)
            return;
          TreeItem i = ((Tree) w).getItem(new Point(r.x, r.y));
          if (i == null)
            return;
          Object o = i.getData();
          if (o instanceof SpartanTipper)
            createTooltip((SpartanTipper) o, i.getBounds());
        }

        void createTooltip(SpartanTipper t, Rectangle r) {
          for (ToolTip ¢ : tooltips.values())
            ¢.setVisible(false);
          if (!tooltips.containsKey(t)) {
            ToolTip tt = new ToolTip(getShell(), SWT.ICON_INFORMATION);
            tt.setMessage(t.description());
            tt.setAutoHide(true);
            tooltips.put(t, tt);
          }
          Rectangle tp = $.getTree().getBounds();
          Point tl = Display.getCurrent().getActiveShell().toDisplay(tp.x + r.x, tp.y + r.y);
          Rectangle tr = new Rectangle(tl.x, tl.y, r.width, r.height);
          ToolTip tt = tooltips.get(t);
          bounds.put(tt, tr);
          tt.setLocation(tr.x + tr.width, tr.y);
          tt.setVisible(true);
        }
      });
      $.getTree().addListener(SWT.MouseMove, new Listener() {
        @Override public void handleEvent(Event e) {
          for (ToolTip ¢ : tooltips.values())
            if (¢.isVisible()) {
              Rectangle tp = $.getTree().getBounds();
              if (!bounds.get(¢).contains(Display.getCurrent().getActiveShell().toDisplay(tp.x + e.x, tp.y + e.y)))
                ¢.setVisible(false);
              break;
            }
        }
      });
      $.getTree().addListener(SWT.MouseWheel, new Listener() {
        @Override public void handleEvent(@SuppressWarnings("unused") Event __) {
          for (ToolTip ¢ : tooltips.values())
            ¢.setVisible(false);
        }
      });
      return $;
    }

    @Deprecated @SuppressWarnings("unused") private void addSelectionListener(final CheckboxTreeViewer $) {
      $.addSelectionChangedListener(e -> {
        final ISelection s = e.getSelection();
        final Object oo = e.getSource();
        if (s == null || s.isEmpty() || !(s instanceof TreeSelection) || !(oo instanceof ContainerCheckedTreeViewer))
          return;
        final Control v = ((ContainerCheckedTreeViewer) oo).getControl();
        if (v == null)
          return;
        final Object o = ((TreeSelection) s).getFirstElement();
        if (o instanceof SpartanTipper)
          tipperClicked((SpartanTipper) o, v);
      });
    }

    @Deprecated private void tipperClicked(final SpartanTipper t, final Control v) {
      final Display d = Display.getCurrent();
      if (d == null)
        return;
      final Point p = d.getCursorLocation();
      if (p == null)
        return;
      final ToolTip tt = new ToolTip(getShell(), SWT.BALLOON);
      tt.setMessage(t.description());
      tt.setLocation(p);
      tt.setAutoHide(false);
      // it is possible to set a delay, see
      // http://stackoverflow.com/questions/1351245/setting-swt-tooltip-delays
      v.addListener(SWT.MouseMove, new Listener() {
        @Override public void handleEvent(@SuppressWarnings("unused") final Event __) {
          v.removeListener(SWT.MouseMove, this);
          if (tt.isDisposed())
            return;
          tt.setVisible(false);
          tt.dispose();
        }
      });
      tt.setVisible(true);
    }
  }
}
