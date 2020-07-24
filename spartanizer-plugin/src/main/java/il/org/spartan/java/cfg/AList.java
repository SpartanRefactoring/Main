package il.org.spartan.java.cfg;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** Implementation of Alist for the CFG
 * @author Dor Ma'ayan
 * @since 2017-10-01 */
public class AList<T> {
  private final List<Entry> implementation = new ArrayList<>();

  public void append(String key, T n) {
    for (int ¢ = 0; ¢ < implementation.size(); ++¢)
      if (implementation.get(¢).getKey().equals(key)) {
        implementation.get(¢).getValue().add(n);
        break;
      }
  }
  public List<T> peek(String key) {
    for (int ¢ = 0; ¢ < implementation.size(); ++¢)
      if (implementation.get(¢).getKey().equals(key))
        return implementation.get(¢).getValue();
    return null;
  }
  public List<T> pop(String key) {
    for (int ¢ = 0; ¢ < implementation.size(); ++¢)
      if (implementation.get(¢).getKey().equals(key))
        return implementation.remove(¢).getValue();
    return null;
  }
  public void push(String key) {
    implementation.add(0, new Entry(key));
  }

  private class Entry {
    private final String key;
    private final List<T> value = new ArrayList<>();

    public Entry(String key) {
      this.key = key;
    }
    public String getKey() {
      return key;
    }
    public List<T> getValue() {
      return value;
    }
  }
}
