package il.org.spartan.java.cfg;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** TODO dormaayn: document class
 * @author dormaayn
 * @since 2017-10-01 */
public class Touple<T extends ASTNode> {
  String key;
  List<T> value;

  public Touple(String key) {
    this.key = key;
    this.value = new ArrayList<>();
  }
  public List<T> getValue() {
    return this.value;
  }
  public String getKey() {
    return this.key;
  }
}
