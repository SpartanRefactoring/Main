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
    //new Gson().toJson(object).getBytes("UTF-8"));
    List<WidgetOperationEntry> entries = new ArrayList<>();
    for(WidgetOperation wo : defaultOrder) {
      if(wo == null)
        continue;
      WidgetOperationEntry woe = new WidgetOperationEntry(getWidgetOpUID(wo), new HashMap<>(), wo.description()+" the name");
      woe.enable();
      entries.add(woe);
    }
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      new ObjectOutputStream(out).writeObject(entries); 
    } catch (final IOException x) {
      note.bug(x);
    }
    store().setDefault(PreferencesResources.WIDGET_OPERATION_CONFIGURATION, Base64.getEncoder().encodeToString(out.toByteArray()));
  }
  public static long getWidgetOpUID(final WidgetOperation ¢) {
    return ObjectStreamClass.lookup(¢.getClass()).getSerialVersionUID();
  }
}
