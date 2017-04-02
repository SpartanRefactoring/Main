package il.org.spartan.plugin.preferences.revision;

import static il.org.spartan.plugin.preferences.revision.PreferencesResources.*;
import static il.org.spartan.plugin.preferences.revision.PreferencesResources.TipperGroup.*;

import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.*;
import org.eclipse.ui.*;

import il.org.spartan.plugin.*;

/** Preferences page for Zoomer tool.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-04-02 */
public class ZoomerPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
  @Override public void init(@SuppressWarnings("unused") IWorkbench w) {
    setPreferenceStore(Plugin.plugin().getPreferenceStore());
    setDescription(ZOOMER_PAGE_DESCRIPTION);
    store().addPropertyChangeListener(new IPropertyChangeListener() {
      @Override public void propertyChange(PropertyChangeEvent e) {
        if (e != null && e.getProperty() != null)
          switch (e.getProperty()) {
            case ZOOMER_REVERT_METHOD_ID:
              if (e.getNewValue() instanceof Boolean)
                ZOOMER_REVERT_METHOD_VALUE.set(((Boolean) e.getNewValue()).booleanValue());
              break;
            default:
              break;
          }
      }
    });
  }

  @Override protected void createFieldEditors() {
    addField(new BooleanFieldEditor(ZOOMER_REVERT_METHOD_ID, ZOOMER_REVERT_METHOD_TEXT, getFieldEditorParent()));
  }
}
