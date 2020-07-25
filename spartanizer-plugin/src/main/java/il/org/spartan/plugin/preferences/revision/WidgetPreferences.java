package il.org.spartan.plugin.preferences.revision;

import static il.org.spartan.plugin.preferences.revision.PreferencesResources.store;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import fluent.ly.note;
import il.org.spartan.spartanizer.plugin.widget.WidgetOperation;
import il.org.spartan.spartanizer.plugin.widget.WidgetOperationEntry;
import il.org.spartan.spartanizer.plugin.widget.operations.AthenizerOperation;
import il.org.spartan.spartanizer.plugin.widget.operations.CleanOperation;
import il.org.spartan.spartanizer.plugin.widget.operations.GitCommitOperation;
import il.org.spartan.spartanizer.plugin.widget.operations.GitPullOperation;
import il.org.spartan.spartanizer.plugin.widget.operations.GitPushOperation;
import il.org.spartan.spartanizer.plugin.widget.operations.SpartanizationOperation;

/** An empty enum for fluent programing. Manages the preferences for the Spartan
 * Widget.
 * @author Niv Shalmon
 * @since 2017-05-20 */
public enum WidgetPreferences {
  ;
  private static final WidgetOperation[] defaultOrder = { //
      new GitPullOperation()//
      , new GitPushOperation()//
      , new GitCommitOperation()//
      , new SpartanizationOperation()//
      , new AthenizerOperation()//
      , new CleanOperation()//
      , null//
  };
  private static final int defaultWidgetSize = 70;

  /** @param ¢ - the size of the widget */
  public static void storeSize(final int ¢) {
    store().setValue(PreferencesResources.WIDGET_SIZE, ¢);
  }
  public static void storeDefaultSize() {
    store().setToDefault(PreferencesResources.WIDGET_SIZE);
  }
  /** @return the size of the widget or null if an exception occurred */
  public static int readSize() {
    return store().getInt(PreferencesResources.WIDGET_SIZE);
  }
  public static void storeEntries(final List<WidgetOperationEntry> es) {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      new ObjectOutputStream(out).writeObject(es);
    } catch (final IOException ¢) {
      note.bug(¢);
    }
    store().setValue(PreferencesResources.WIDGET_OPERATION_CONFIGURATION, Base64.getEncoder().encodeToString(out.toByteArray()));
  }
  public static void storeDefaultEntries() {
    store().setToDefault(PreferencesResources.WIDGET_OPERATION_CONFIGURATION);
  }
  @SuppressWarnings("unchecked") public static List<WidgetOperationEntry> readEntries() {
    final byte[] theOutBarr = Base64.getDecoder().decode(store().getString(PreferencesResources.WIDGET_OPERATION_CONFIGURATION));
    final ByteArrayInputStream in = new ByteArrayInputStream(theOutBarr);
    List<WidgetOperationEntry> $ = an.empty.list();
    try {
      $ = (List<WidgetOperationEntry>) new ObjectInputStream(in).readObject();
    } catch (final IOException | ClassNotFoundException ¢) {
      note.bug(¢);
    }
    return $;
  }
  public static void setDefaults() {
    store().setDefault(PreferencesResources.WIDGET_SIZE, defaultWidgetSize);
    final List<WidgetOperationEntry> entries = new ArrayList<>();
    for (final WidgetOperation wo : defaultOrder) {
      if (wo == null)
        continue;
      final WidgetOperationEntry woe = new WidgetOperationEntry(getWidgetOpUID(wo), new HashMap<>(), wo.description());
      woe.enable();
      entries.add(woe);
    }
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      new ObjectOutputStream(out).writeObject(entries);
    } catch (final IOException ¢) {
      note.bug(¢);
    }
    store().setDefault(PreferencesResources.WIDGET_OPERATION_CONFIGURATION, Base64.getEncoder().encodeToString(out.toByteArray()));
  }
  private static long getWidgetOpUID(final WidgetOperation ¢) {
    return ObjectStreamClass.lookup(¢.getClass()).getSerialVersionUID();
  }
}
