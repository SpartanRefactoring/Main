package il.org.spartan.plugin.preferences.revision;

import java.io.*;
import java.util.*;

import il.org.spartan.spartanizer.plugin.widget.*;

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
  
  public void toggleEnabled() {
    isEnabled = !isEnabled;
  }
  
  public void setEnabled(boolean flag) {
    isEnabled = flag;
  }
  
  public void setName(final String name) {
    this.name = name;
  }
  public String getName() {
    return name;
  }
  
  public WidgetOperation getWidgetOp() {
    for (final WidgetOperation wo : WidgetOperationPoint.allOperations)
      if (widgetSUID == ObjectStreamClass.lookup(wo.getClass()).getSerialVersionUID())
        return wo;
    return null;
  }
  
  @Override public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + (int) (widgetSUID ^ (widgetSUID >>> 32));
    return result;
  }
  @Override public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    WidgetOperationEntry other = (WidgetOperationEntry) obj;
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
    if (widgetSUID != other.widgetSUID)
      return false;
    return true;
  }
}
