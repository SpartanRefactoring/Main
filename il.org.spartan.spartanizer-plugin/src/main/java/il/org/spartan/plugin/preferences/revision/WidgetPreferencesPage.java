package il.org.spartan.plugin.preferences.revision;

import static il.org.spartan.plugin.preferences.revision.PreferencesResources.*;

import java.util.*;
import java.util.List;
import java.util.Map.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.preference.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.plugin.widget.*;

/** The preferences page for the Athenizer Widget We read from store only at
 * listEditor creation time We write on every change both in the listEditor and
 * the store
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
  public static void onAble(final WidgetOperationEntry e, final boolean valueNow, final ListEditor resLE) {
    final List<WidgetOperationEntry> l = WidgetPreferences.readEntries();
    l.get(l.indexOf(e)).setEnabled(!valueNow);
    e.setEnabled(!valueNow);
    WidgetPreferences.storeEntries(l);
    resLE.loadDefault();
  }
  @SuppressWarnings("boxing") public static Boolean isEnabled(final WidgetOperationEntry ¢) {
    return ¢.isEnabled();
  }
  public static void onConfigure(final WidgetOperationEntry ¢) {
    if (¢.getWidgetOp() != null)
      new ConfigWidgetPreferencesDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), ¢, ¢.widgetSUID, store()).open();
  }
  @Override @SuppressWarnings("boxing") protected void createFieldEditors() {
    final IntegerFieldEditor ife = new IntegerFieldEditor(PreferencesResources.WIDGET_SIZE, "Change widget size by radius - ",
        getFieldEditorParent());
    ife.setValidRange(WIDGET_MIN_SIZE, WIDGET_MAX_SIZE);
    addField(ife);
    final OperationListEditor ole = new OperationListEditor("ListOps", "Configure operations for widget:", getFieldEditorParent());
    final ListEditor resLE = new ListEditor("EnabledOps", "enabled operations:", getFieldEditorParent()) {
      @Override protected String[] parseString(@SuppressWarnings("unused") final String stringList) {
        final String[] $ = new String[7];
        int count = 0;
        for (final WidgetOperationEntry ¢ : WidgetPreferences.readEntries()) // store
          if (¢.isEnabled()) {
            if (count >= WIDGET_MAX_OPS) {
              MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Cannot enable more than "
                  + WIDGET_MAX_OPS + " widget operations. \n Taking the " + WIDGET_MAX_OPS + " first enabled widget operations ");
              return $;
            }
            $[count++] = ¢.getName();
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
    addField(ole.lazyConstruct(getFieldEditorParent(), getWidgetOperations(), λ -> onConfigure((WidgetOperationEntry) λ),
        λ -> isEnabled((WidgetOperationEntry) λ), λ -> onAble((WidgetOperationEntry) λ, isEnabled((WidgetOperationEntry) λ), resLE)));
    resLE.getButtonBoxControl(getFieldEditorParent());
    addField(resLE);
  }
  /** @return all plugin widget operations */
  private static List<Entry<String, Object>> getWidgetOperations() {
    final List<Entry<String, Object>> $ = an.empty.list();
    for (final WidgetOperationEntry ¢ : WidgetPreferences.readEntries())
      $.add(new AbstractMap.SimpleEntry<>(¢.getName(), ¢));
    return $;
  }
}
