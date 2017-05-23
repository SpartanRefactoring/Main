package il.org.spartan.spartanizer.plugin.widget;

import java.util.*;

/** configurations for a {@link WidgetOperation} 
 * 
 * @author Niv Shalmon
 * @since 2017-05-23 */
public class ConfigurationsMap {
  private Map<String,String> configurations;
  
  public ConfigurationsMap(Map<String,String> configurations){
    this.configurations = configurations;
  }
  
  public ConfigurationsMap(String[][] configurations){
    this.configurations = new HashMap<>();
    for (int ¢ = 0; ¢ <configurations.length; ++¢)
      this.configurations.put(configurations[¢][0], configurations[¢][1]);
  }
  
  /**
   * @return the boolean value saved under key, or null if such key doesn't exist
   */
  @SuppressWarnings("boxing")
  public Boolean getBoolean(String key){
    return !configurations.containsKey(key) ? null : Boolean.parseBoolean(configurations.get(key));
  }
  
  /**
   * @return the String value saved under key, or null if such key doesn't exist
   */
  public String getString(String key){
    return configurations.get(key);
  }
}
