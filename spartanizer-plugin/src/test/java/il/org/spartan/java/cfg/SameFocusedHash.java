package il.org.spartan.java.cfg;

import java.lang.reflect.Field;

import org.junit.runners.model.FrameworkMethod;

/** TODO Roth: document class
 * TODO Roth: move to appropriate package
 * @author Ori Roth
 * @since 2017-10-03 */
public class SameFocusedHash extends Exception {
  private static final long serialVersionUID = -9109817977086551314L;
  final Field f;
  final FrameworkMethod m;

  public SameFocusedHash(FrameworkMethod method, Field f) {
    this.f = f;
    this.m = method;
  }
  @Override public String getMessage() {
    return "field " + f.getName() + " did not changed during the execution of " + m.getName();
  }
}
