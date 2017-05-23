package il.org.spartan.plugin.preferences.revision;

import java.io.*;
import java.util.*;

/** A widget operation with it's configuration
 * @author Niv Shalmon
 * @author Raviv Rachmiel
 * @since 2017-05-21 */
public class WidgetOperationEntry implements Serializable {
  private static final long serialVersionUID = -0x295E9D34CC34A530L;
  public final long widgetSUID;
  public final Map<String, String> configuration;
  private String name;
  private boolean isEnabled;

  public WidgetOperationEntry(final long widgetSUID, final Map<String, String> configuration, final String name) {
    this.widgetSUID = widgetSUID;
    this.configuration = configuration;
    this.name = name;
  }
  public void enable() {
    isEnabled = true;
  }
  public void disable() {
    isEnabled = false;
  }
  public boolean isEnabled() {
    return isEnabled;
  }
  public void setName(final String name) {
    this.name = name;
  }
  public String getName() {
    return name;
  }
}
