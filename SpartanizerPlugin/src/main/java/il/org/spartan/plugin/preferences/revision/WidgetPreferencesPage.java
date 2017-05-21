package il.org.spartan.plugin.preferences.revision;

import static il.org.spartan.plugin.preferences.revision.PreferencesResources.*;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Map.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.preference.*;
import org.eclipse.swt.widgets.*;
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
  public static void onAble(final WidgetOperation o, final boolean valueNow, final ListEditor resLE) {
    store().setValue("IS_ENABLED_" + ObjectStreamClass.lookup(o.getClass()).getSerialVersionUID(), !valueNow);
    resLE.loadDefault();
  }
  @SuppressWarnings("boxing") public static Boolean isEnabled(final WidgetOperation ¢) {
    return store().getBoolean("IS_ENABLED_" + ObjectStreamClass.lookup(¢.getClass()).getSerialVersionUID());
  }
  public static void onConfigure(final WidgetOperation ¢) {
    new ConfigWidgetPreferencesDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), ¢.description(), ¢.configurationComponents(),
        ObjectStreamClass.lookup(¢.getClass()).getSerialVersionUID(), store()).open();
  }
  @Override @SuppressWarnings("boxing") protected void createFieldEditors() {
    final IntegerFieldEditor ife = new IntegerFieldEditor("WIDGET_SIZE", "Change widget size by radius - ", getFieldEditorParent());
    ife.setValidRange(WIDGET_MIN_SIZE, WIDGET_MAX_SIZE);
    addField(ife);
    final OperationListEditor ole = new OperationListEditor("X", "Configure operations for widget:", getFieldEditorParent());
    final ListEditor resLE = new ListEditor("X", "enabled operations:", getFieldEditorParent()) {
      @Override protected String[] parseString(@SuppressWarnings("unused") final String stringList) {
        final String[] $ = new String[7];
        int count = 0;
        for (final WidgetOperation ¢ : WidgetOperationPoint.allOperations)
          if (isEnabled(¢)) {
            if (count >= WIDGET_MAX_OPS) {
              MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Cannot enable more than "
                  + WIDGET_MAX_OPS + " widget operations. \n Taking the " + WIDGET_MAX_OPS + " first enabled widget operations ");
              return $;
            }
            $[count++] = ¢.description();
          }
        return Arrays.copyOfRange($, 0, count);
      }
      @Override protected String getNewInputObject() {
        return null;
      }
      @Override protected String createList(@SuppressWarnings("unused") final String[] items) {
        return null;
      }
      @Override protected void doFillIntoGrid(final Composite parent, final int numColumns) {
        super.doFillIntoGrid(parent, numColumns);
        getButtonBoxControl(parent).dispose();
      }
    };
    addField(ole.lazyConstruct(getFieldEditorParent(), getWidgetOperations(), λ -> onConfigure((WidgetOperation) λ),
        λ -> isEnabled((WidgetOperation) λ), λ -> onAble((WidgetOperation) λ, isEnabled((WidgetOperation) λ), resLE)));
    resLE.getButtonBoxControl(getFieldEditorParent());
    addField(resLE);
  }
  /** @return all plugin widget operations */
  private static List<Entry<String, Object>> getWidgetOperations() {
    final List<Entry<String, Object>> $ = an.empty.list();
    for (final WidgetOperation ¢ : WidgetOperationPoint.allOperations)
      $.add(new AbstractMap.SimpleEntry<>(¢.description(), ¢));
    return $;
  }
}
