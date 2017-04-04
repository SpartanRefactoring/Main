package il.org.spartan.plugin.preferences.revision;

import static il.org.spartan.plugin.preferences.revision.PreferencesResources.*;
import static il.org.spartan.plugin.preferences.revision.PreferencesResources.TipperGroup.*;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.*;

import il.org.spartan.plugin.*;

/** Preferences page for Zoomer tool.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-04-02 */
public class ZoomerPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
  @Override public void init(@SuppressWarnings("unused") final IWorkbench __) {
    setPreferenceStore(Plugin.plugin().getPreferenceStore());
    setDescription(ZOOMER_PAGE_DESCRIPTION);
    store().addPropertyChangeListener(λ -> {
      if (λ != null && λ.getProperty() != null)
        switch (λ.getProperty()) {
          case ZOOMER_REVERT_METHOD_ID:
            if (λ.getNewValue() instanceof Boolean)
              ZOOMER_REVERT_METHOD_VALUE.set(((Boolean) λ.getNewValue()).booleanValue());
            break;
          default:
            break;
        }
    });
  }

  @Override protected void createFieldEditors() {
    addField(new BooleanFieldEditor(ZOOMER_REVERT_METHOD_ID, ZOOMER_REVERT_METHOD_TEXT, getFieldEditorParent()));
  }
}
