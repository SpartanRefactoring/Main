package il.org.spartan.plugin.preferences.revision;

import static il.org.spartan.plugin.preferences.revision.PreferencesResources.*;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Map.*;

import org.eclipse.jface.preference.*;

import org.eclipse.ui.*;

import il.org.spartan.spartanizer.plugin.Plugin;
import il.org.spartan.spartanizer.plugin.widget.*;

/** The preferences page for the Athenizer Widget
 * @author Raviv Rachmiel
 * @since 2017-04-30 */
public class WidgetPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
  @Override public void init(@SuppressWarnings("unused") final IWorkbench __) {
    setPreferenceStore(Plugin.plugin().getPreferenceStore());
    setDescription(WIDGET_PAGE_DESCRIPTION);
    store().addPropertyChangeListener(λ -> {
      if (λ != null && λ.getProperty() != null && λ.getProperty() == WIDGET_SHORTCUT_METHOD_ID && λ.getNewValue() instanceof Boolean)
        ZOOMER_REVERT_METHOD_VALUE.set(((Boolean) λ.getNewValue()).booleanValue());
    });
  }
  public static void onAble(final WidgetOperation o, final boolean valueNow) {
    store().setValue("IS_ENABLED_" + ObjectStreamClass.lookup(o.getClass()).getSerialVersionUID(), !valueNow);
    // String prefOpsIDs = store().getString("prefOpsIDs"), prefOpsMapConfs =
    // store().getString("prefOpsMapConfs");
    // if (prefOpsIDs == null)
    // store().putValue(prefOpsIDs, "stub");
    // if (prefOpsMapConfs == null)
    // store().putValue(prefOpsMapConfs, "stub");
  }
  @SuppressWarnings("boxing") public static Boolean isEnabled(final WidgetOperation ¢) {
    return store().getBoolean("IS_ENABLED_" + ObjectStreamClass.lookup(¢.getClass()).getSerialVersionUID());
  }
  public static void onConfigure(final WidgetOperation ¢) {
    new ConfigWidgetPreferencesDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), ¢.description(), ¢.configurationComponents(),
        ObjectStreamClass.lookup(¢.getClass()).getSerialVersionUID(), store()).open();
  }
  @Override @SuppressWarnings("boxing") protected void createFieldEditors() {
    addField(new BooleanFieldEditor(WIDGET_SHORTCUT_METHOD_ID, WIDGET_SHORTCUT_METHOD_TEXT, getFieldEditorParent()));
    IntegerFieldEditor ife = new IntegerFieldEditor("WIDGET_SIZE", "Change widget size by radius - ", getFieldEditorParent());
    ife.setValidRange(60, 100);
    addField(ife);
    addField(new OperationListEditor("X", "Configure operations for widget:", getFieldEditorParent(), getWidgetOperations(),
        λ -> onConfigure((WidgetOperation) λ), λ -> isEnabled((WidgetOperation) λ),
        λ -> onAble((WidgetOperation) λ, isEnabled((WidgetOperation) λ))));
  }
  /** @return all plugin widget operations */
  private static List<Entry<String, Object>> getWidgetOperations() {
    final List<Entry<String, Object>> $ = an.empty.list();
    for (final WidgetOperation ¢ : WidgetOperationPoint.allOperations)
      $.add(new AbstractMap.SimpleEntry<>(¢.description(), ¢));
    return $;
  }
}
