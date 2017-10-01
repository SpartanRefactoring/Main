package il.org.spartan.java.cfg;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** TODO dormaayn: document class
 * @author dormaayn
 * @since 2017-10-01 */
public class Alist {
  private class Touple {
    String key;
    List<ASTNode> value;

    public Touple(String key) {
      this.key = key;
      this.value = new ArrayList<>();
    }
    public List<ASTNode> getValue() {
      return this.value;
    }
    public String getKey() {
      return this.key;
    }
  }

  private List<Touple> lst;

  public Alist() {
    this.lst = new ArrayList<>();
  }
  public void push(String key) {
    lst.add(0, new Touple(key));
  }
  public List<ASTNode> pop(String key) {
    for (int ¢ = 0; ¢ < lst.size(); ++¢)
      if (lst.get(¢).getKey().equals(key)) {
        return lst.remove(¢).getValue();
      }
    return null;
  }
  public List<ASTNode> peek(String key) {
    for (int ¢ = 0; ¢ < lst.size(); ++¢)
      if (lst.get(¢).getKey().equals(key)) {
        return lst.get(¢).getValue();
      }
    return null;
  }
  public void append(String key, ASTNode n) {
    for (int ¢ = 0; ¢ < lst.size(); ++¢)
      if (lst.get(¢).getKey().equals(key)) {
        lst.get(¢).getValue().add(n);
        break;
      }
  }
}
