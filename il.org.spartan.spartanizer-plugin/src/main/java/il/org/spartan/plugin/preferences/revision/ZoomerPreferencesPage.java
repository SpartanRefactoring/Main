package il.org.spartan.plugin.preferences.revision;

import static il.org.spartan.plugin.preferences.revision.PreferencesResources.*;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.*;

import il.org.spartan.spartanizer.plugin.*;

/** Preferences page for Zoomer tool.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-04-02 */
public class ZoomerPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
  @Override public void init(@SuppressWarnings("unused") final IWorkbench __) {
    setPreferenceStore(Plugin.plugin().getPreferenceStore());
    setDescription(ZOOMER_PAGE_DESCRIPTION);
    store().addPropertyChangeListener(λ -> {
      if (λ != null && λ.getProperty() != null && λ.getProperty() == WIDGET_SHORTCUT_METHOD_ID && λ.getNewValue() instanceof Boolean)
        WIDGET_SHORTCUT_METHOD_VALUE.set(((Boolean) λ.getNewValue()).booleanValue());
    });
  }
  @Override protected void createFieldEditors() {
    addField(new BooleanFieldEditor(ZOOMER_AUTO_ACTIVISION_ID, ZOOMER_AUTO_ACTIVISION_ID_TEXT, getFieldEditorParent()));
    addField(new BooleanFieldEditor(ZOOMER_REVERT_METHOD_ID, ZOOMER_REVERT_METHOD_TEXT, getFieldEditorParent()));
  }
}
