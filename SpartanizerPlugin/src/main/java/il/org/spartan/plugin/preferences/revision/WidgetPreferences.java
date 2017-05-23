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
  
  @SuppressWarnings("unused")
  private static final WidgetOperation[] defaultOrder = { new GitPullOperation()//
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
  /** @return the size of the widget or null if an exception occurred */
  public static int readSize() {
    return store().getInt(PreferencesResources.WIDGET_SIZE);
  }
  public static void storeEntries(final List<WidgetOperationEntry> es){
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      new ObjectOutputStream(out).writeObject(es);
    } catch (final IOException x) {
      note.bug(x);
    }
    store().setValue(PreferencesResources.WIDGET_OPERATION_CONFIGURATION, Base64.getEncoder().encodeToString(out.toByteArray()));
  }
  @SuppressWarnings("unchecked")
  public static List<WidgetOperationEntry> readEntries(){
    String forString = store().getString(PreferencesResources.WIDGET_OPERATION_CONFIGURATION);
    byte[] theOutBarr = Base64.getDecoder().decode(forString);
    final ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(Base64.getEncoder().encode(theOutBarr)));
    
    List<WidgetOperationEntry> res = null;
    try {
      res = (List<WidgetOperationEntry>)new ObjectInputStream(in).readObject();
    } catch (@SuppressWarnings("unused") final IOException | ClassNotFoundException x) {
      note.bug();
    }
    return res;
  }
  public static void setDefaults() {
    store().setDefault(PreferencesResources.WIDGET_SIZE,defaultWidgetSize);
    //TODO: add defualt List of entries here
  }
}
