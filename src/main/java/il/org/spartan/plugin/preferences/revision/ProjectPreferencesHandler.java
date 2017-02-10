package il.org.spartan.plugin.preferences.revision;

import java.util.*;
import java.util.List;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
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
  @Override public Object execute(@SuppressWarnings("unused") ExecutionEvent __) {
    IProject p = Selection.Util.project();
    CheckedTreeSelectionDialog d = getDialog(p);
    if (d == null)
      return null;
    d.open();
    Set<String> es = new HashSet<>();
    Object[] os = d.getResult();
    if (os == null)
      return null;
    for (Object ¢ : os)
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
  private static CheckedTreeSelectionDialog getDialog(IProject p) {
    final Shell s = Display.getCurrent().getActiveShell();
    Map<SpartanCategory, SpartanTipper[]> m = XMLSpartan.getTippersByCategories(p, false);
    if (s == null || m == null)
      return null;
    SpartanElement[] es = m.keySet().toArray(new SpartanElement[m.size()]);
    CheckedTreeSelectionDialog $ = new CheckedTreeSelectionDialog(s, new ILabelProvider() {
      @Override public void removeListener(@SuppressWarnings("unused") ILabelProviderListener listener) {
        //
      }

      @SuppressWarnings("unused") @Override public boolean isLabelProperty(Object element, String property) {
        return false;
      }

      @Override public void dispose() {
        //
      }

      @Override public void addListener(@SuppressWarnings("unused") ILabelProviderListener listener) {
        //
      }

      @Override public String getText(Object element) {
        return element == null ? "" : element instanceof SpartanElement ? ((SpartanElement) element).name() : element.toString();
      }

      @Override public Image getImage(@SuppressWarnings("unused") Object __) {
        return null;
      }
    }, new ITreeContentProvider() {
      @Override public boolean hasChildren(Object element) {
        return element instanceof SpartanCategory && ((SpartanCategory) element).hasChildren();
      }

      @Override public Object getParent(Object element) {
        return element instanceof SpartanTipper ? ((SpartanTipper) element).parent() : null;
      }

      @Override public Object[] getElements(@SuppressWarnings("unused") Object __) {
        return es;
      }

      @Override public Object[] getChildren(Object parentElement) {
        return parentElement instanceof SpartanCategory ? m.get(parentElement) : null;
      }
    });
    $.setTitle("Spartanization Preferences");
    $.setMessage("Choose the tippers you would like to use:");
    $.setEmptyListMessage("No tippers available...");
    $.setContainerMode(true);
    $.setInput(new Object()); // vio: very important object
    List<SpartanElement> et = new ArrayList<>();
    for (SpartanCategory c : m.keySet()) {
      boolean disabledTipper = false;
      for (SpartanTipper t : m.get(c))
        if (t.enabled())
          et.add(t);
        else
          disabledTipper = true;
      if (!disabledTipper)
        et.add(c);
    }
    $.setInitialSelections(et.toArray(new SpartanElement[et.size()]));
    $.setHelpAvailable(false);
    $.setComparator(new ViewerComparator(new Comparator<String>() {
      @Override public int compare(String o1, String o2) {
        return o1.compareToIgnoreCase(o2);
      }
    }));
    return $;
  }
}
