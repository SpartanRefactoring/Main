package il.org.spartan.plugin.preferences.revision;

import static il.org.spartan.plugin.preferences.revision.PreferencesResources.*;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.preference.*;
import org.eclipse.jface.window.*;
import org.eclipse.ui.*;

import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.plugin.widget.*;

/** The preferences page for the Athenizer Widget
 * @author Raviv Rachmiel
 * @since 2017-05-09 */
public class WidgetNewPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
  @Override public void init(@SuppressWarnings("unused") IWorkbench __) {
    setPreferenceStore(Plugin.plugin().getPreferenceStore());
    setDescription(WIDGET_PAGE_DESCRIPTION);
  }
  @Override protected void createFieldEditors() {
    BooleanFieldEditor onButton = new BooleanFieldEditor("WIDGET_ON_BUTTON", WIDGET_SHORTCUT_METHOD_TEXT, getFieldEditorParent());
    addField(onButton);
    IntegerFieldEditor widgetSizeFE= new IntegerFieldEditor("WIDGET_SIZE", "Change widget size by radius - ", getFieldEditorParent());
    widgetSizeFE.setValidRange(60, 1000);
    addField(widgetSizeFE);
    
  }
  
  static List<Entry<String, Object>> getWidgetOperations() {
    final List<Entry<String, Object>> $ = an.empty.list();
    for (final WidgetOperation ¢ : WidgetOperationPoint.allOperations)
      $.add(new AbstractMap.SimpleEntry<>(¢.description(), ¢));
    return $;
  }
  
  
}
