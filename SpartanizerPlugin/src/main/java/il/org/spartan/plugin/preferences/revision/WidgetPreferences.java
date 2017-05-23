package il.org.spartan.plugin.preferences.revision;

import static il.org.spartan.plugin.preferences.revision.PreferencesResources.*;

import java.io.*;
import java.util.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.plugin.widget.*;
import il.org.spartan.spartanizer.plugin.widget.operations.*;

/** An empty enum for fluent programing. Manages the preferences for the Spartan
 * Widget.
 * @author Niv Shalmon
 * @since 2017-05-20 */
public enum WidgetPreferences {
  ;
  private static final String OPERATIONS_CONFIGURATION = "WIDGET_OPERATIONS_CONFIGURATION";
  public static final String WIDGET_SIZE = "WIDGET_SIZE";
  private static final String OPERATIONS_ORDER = "WIDGET_OPERATIONS_ORDER";
  private static final WidgetOperation[] defaultOrder = { new GitPullOperation()//
      , new GitPushOperation()//
      , new GitCommitOperation()//
      , new SpartanizationOperation()//
      , new ZoomerOperation()//
      , new CleanOperation()//
      , null//
  };

  /** @param m - map from WidgetOperation to a configuration of that operation.
   *        calling key.register(value) should set the WidgetOperation for
   *        running */
  public static void storeOperationsConfiguration(final Map<WidgetOperation, Map<String, String>> m) {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      new ObjectOutputStream(out).writeObject(m);
    } catch (final IOException ¢) {
      note.bug(¢);
    }
    store().setValue(OPERATIONS_CONFIGURATION, String.valueOf(Base64.getEncoder().encode(out.toByteArray())));
  }
  /** @return map from WidgetOperation to a configuration of that operation.
   *         calling key.register(value) should set the WidgetOperation for
   *         running If an exception was throws, returns null. */
  @SuppressWarnings("unchecked") public static Map<WidgetOperation, Map<String, String>> readOperationsConfiguration() {
    final ByteArrayInputStream $ = new ByteArrayInputStream(Base64.getDecoder().decode(store().getString(OPERATIONS_CONFIGURATION).getBytes()));
    try {
      return (Map<WidgetOperation, Map<String, String>>) new ObjectInputStream($).readObject();
    } catch (final ClassNotFoundException ¢) {
      note.bug(¢);
    } catch (@SuppressWarnings("unused") final IOException x) {
      // not a bug in initial start
    }
    return null;
  }
  /** @param ¢ - the size of the widget */
  public static void storeSize(final int ¢) {
    store().setValue(WIDGET_SIZE, ¢);
  }
  /** @return the size of the widget or null if an exception occurred */
  public static int readSize() {
    return store().getInt(WIDGET_SIZE);
  }
  /** @param os - array of the widget operations, ordered by the order of
   *        buttons. Null if no button is set to that location. Must be an array
   *        of size 7 or less logically, but that is not enforced here. */
  public static void storeOperationsOrder(final WidgetOperation[] os) {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      new ObjectOutputStream(out).writeObject(os);
    } catch (final IOException ¢) {
      note.bug(¢);
    }
    store().setValue(OPERATIONS_ORDER, String.valueOf(Base64.getEncoder().encode(out.toByteArray())));
  }
  /** @return array of WidgetOperations or nulls if no operation is set for that
   *         location */
  public static WidgetOperation[] readOperationsOrder() {
    final ByteArrayInputStream $ = new ByteArrayInputStream(Base64.getDecoder().decode(store().getString(OPERATIONS_ORDER).getBytes()));
    try {
      return (WidgetOperation[]) new ObjectInputStream($).readObject();
    } catch (final ClassNotFoundException ¢) {
      note.bug(¢);
    } catch (@SuppressWarnings("unused") final IOException x) {
      // not a bug in initial start
    }
    return null;
  }
  public static void setDefaults() {
    if (readSize() < 60 || readSize() > 100)
      storeSize(70);
    if (readOperationsOrder() == null)
      storeOperationsOrder(defaultOrder);
    if (readOperationsConfiguration() != null)
      return;
    final Map<WidgetOperation, Map<String, String>> defaultConfigurations = new HashMap<>();
    for (final WidgetOperation ¢ : defaultOrder)
      defaultConfigurations.put(¢, new HashMap<>());
    storeOperationsConfiguration(defaultConfigurations);
  }
}
