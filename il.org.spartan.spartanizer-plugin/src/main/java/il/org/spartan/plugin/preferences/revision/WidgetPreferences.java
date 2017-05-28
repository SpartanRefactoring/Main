package il.org.spartan.plugin.preferences.revision;

import static il.org.spartan.plugin.preferences.revision.PreferencesResources.*;

import java.io.*;
import java.util.*;
import java.util.List;

import fluent.ly.*;
import il.org.spartan.spartanizer.plugin.widget.*;
import il.org.spartan.spartanizer.plugin.widget.operations.*;

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
      , new ZoomerOperation()//
      , new CleanOperation()//
      , null//
  };
  private static final int defaultWidgetSize = 70;

  /** @param ¢ - the size of the widget */
  public static void storeSize(final int ¢) {
    store().setValue(PreferencesResources.WIDGET_SIZE, ¢);
  }
  public static void storeDefaultSize(){
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
  public static void storeDefaultEntries(){
    store().setToDefault(PreferencesResources.WIDGET_OPERATION_CONFIGURATION);
  }
  @SuppressWarnings("unchecked") public static List<WidgetOperationEntry> readEntries() {
    final String forString = store().getString(PreferencesResources.WIDGET_OPERATION_CONFIGURATION);
    final byte[] theOutBarr = Base64.getDecoder().decode(forString);
    final ByteArrayInputStream in = new ByteArrayInputStream(theOutBarr);
    List<WidgetOperationEntry> $ = null;
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
