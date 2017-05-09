package il.org.spartan.plugin.preferences.revision;

import static il.org.spartan.plugin.preferences.revision.PreferencesResources.*;

import java.util.*;
import java.util.Map.*;

import org.eclipse.core.resources.*;
import org.eclipse.jface.preference.*;
import org.eclipse.ui.*;

import il.org.spartan.plugin.preferences.revision.PreferencesPage.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.plugin.widget.*;

/** The preferences page for the Athenizer Widget
 * @author Raviv Rachmiel
 * @since 2017-04-30 */
public class WidgetPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
  private Changes changes;

  @Override public void init(@SuppressWarnings("unused") final IWorkbench __) {
    setPreferenceStore(Plugin.plugin().getPreferenceStore());
    setDescription(WIDGET_PAGE_DESCRIPTION);
    store().addPropertyChangeListener(λ -> {
      if (λ != null && λ.getProperty() != null && λ.getProperty() == WIDGET_SHORTCUT_METHOD_ID && λ.getNewValue() instanceof Boolean)
        ZOOMER_REVERT_METHOD_VALUE.set(((Boolean) λ.getNewValue()).booleanValue());
    });
  }
  @Override protected void createFieldEditors() {
    addField(new BooleanFieldEditor(WIDGET_SHORTCUT_METHOD_ID, WIDGET_SHORTCUT_METHOD_TEXT, getFieldEditorParent()));
    addField(new IntegerFieldEditor("WIDGET_SIZE", "Change widget size by radius - ", getFieldEditorParent()));
    final ListSelectionEditor lse = new ListSelectionEditor("X", "Configure operations for widget:", getFieldEditorParent(), getWidgetOperations(),
        p -> ProjectPreferencesHandler.execute((IProject) p, changes.getPreference((IProject) p), (pp, es) -> changes.update(pp, es)), //
        λ -> changes.isEnabled((IProject) λ), //
        λ -> changes.update((IProject) λ, Boolean.valueOf(!changes.isEnabled((IProject) λ).booleanValue())) //
    );
    lse.ableButton.setText("enable/disable operation");
    lse.configureButton.setText("configure operation");
    addField(lse);
  }
  /** @return all plugin widget operations */
  private static List<Entry<String, Object>> getWidgetOperations() {
    final List<Entry<String, Object>> $ = an.empty.list();
    for (final WidgetOperation ¢ : WidgetOperationPoint.allOperations)
      $.add(new AbstractMap.SimpleEntry<>(¢.description(), ¢));
    return $;
  }
}
