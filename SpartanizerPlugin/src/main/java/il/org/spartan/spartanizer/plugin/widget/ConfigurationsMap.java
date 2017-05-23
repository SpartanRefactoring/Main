package il.org.spartan.spartanizer.plugin.widget;

import java.util.*;

/** configurations for a {@link WidgetOperation}
 * @author Niv Shalmon
 * @since 2017-05-23 */
public class ConfigurationsMap {
  private final Map<String, String> configurations;

  public ConfigurationsMap(final Map<String, String> configurations) {
    this.configurations = configurations;
  }
  public ConfigurationsMap(final String[][] configurations) {
    this.configurations = new HashMap<>();
    for (final String[] configuration : configurations)
      this.configurations.put(configuration[0], configuration[1]);
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
  public boolean isEmpty() {
    return configurations.isEmpty();
  }
}
