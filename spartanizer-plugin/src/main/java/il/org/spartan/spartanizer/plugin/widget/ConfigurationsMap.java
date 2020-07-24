package il.org.spartan.spartanizer.plugin.widget;

import java.util.HashMap;
import java.util.Map;

/** configurations for a {@link WidgetOperation}
 * @author Niv Shalmon
 * @since 2017-05-23 */
public class ConfigurationsMap {
  private final Map<String, String> configurations;

  public ConfigurationsMap() {
    configurations = new HashMap<>();
  }
  public ConfigurationsMap(final Map<String, String> configurations) {
    this.configurations = configurations;
  }
  /** @return the boolean value saved under key, or null if such key doesn't
   *         exist */
  @SuppressWarnings("boxing") public Boolean getBoolean(final String key) {
    return !configurations.containsKey(key) ? null : Boolean.parseBoolean(configurations.get(key));
  }
  /** @return the String value saved under key, or null if such key doesn't
   *         exist */
  public String getString(final String key) {
    return configurations.get(key);
  }
  /** @return true iff the configuration is empty */
  public boolean isEmpty() {
    return configurations.isEmpty();
  }
  public ConfigurationsMap put(final String key, final String value) {
    configurations.put(key, value);
    return this;
  }
  public ConfigurationsMap put(final String key, final boolean value) {
    configurations.put(key, value + "");
    return this;
  }
}
