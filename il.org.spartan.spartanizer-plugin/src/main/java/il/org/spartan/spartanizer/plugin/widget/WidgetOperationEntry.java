package il.org.spartan.spartanizer.plugin.widget;

import java.io.*;
import java.util.*;


/** A widget operation with it's configuration
 * @author Niv Shalmon
 * @author Raviv Rachmiel
 * @since 2017-05-21 */
public class WidgetOperationEntry implements Serializable {
  private static final long serialVersionUID = -0x295E9D34CC34A530L;
  public final long widgetSUID;
  public Map<String, String> configuration;
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
  public void toggleEnabled() {
    isEnabled = !isEnabled;
  }
  

  public void setEnabled(final boolean flag) {
    isEnabled = flag;
  }
  public void setName(final String name) {
    this.name = name;
  }
  public String getName() {
    return name;
  }
  
  public void setConfMap(Map<String,String> ¢) {
    this.configuration = ¢;
  }
  
  public WidgetOperation getWidgetOp() {
    for (final WidgetOperation $ : WidgetOperationPoint.allOperations)
      if (widgetSUID == ObjectStreamClass.lookup($.getClass()).getSerialVersionUID())
        return $.clone();
    return null;
  }
  public ConfigurationsMap getConfigurationMap() {
    return new ConfigurationsMap(configuration);
  }
  public Map<String,String> getConfiguration() {
    return configuration;
  }
  @Override public int hashCode() {
    return (int) (widgetSUID ^ widgetSUID >>> 32)
        + 31 * ((name == null ? 0 : name.hashCode()) + 31 * ((configuration == null ? 0 : configuration.hashCode()) + 31));
  }
  @Override public boolean equals(final Object o) {
    if (o == this)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    final WidgetOperationEntry other = (WidgetOperationEntry) o;
    if (configuration == null) {
      if (other.configuration != null)
        return false;
    } else if (!configuration.equals(other.configuration))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return widgetSUID == other.widgetSUID;
  }
}
