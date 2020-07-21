package il.org.spartan.java.cfg;

import java.lang.reflect.*;

import org.junit.runners.model.*;

/** TODO Roth: document class TODO Roth: move to appropriate package
 * @author Ori Roth
 * @since 2017-10-03 */
public class FocusedWrapperSizeChanged extends Exception {
  private static final long serialVersionUID = -8249602037143882711L;
  final Field f;
  final FrameworkMethod m;

  public FocusedWrapperSizeChanged(FrameworkMethod method, Field f) {
    this.f = f;
    this.m = method;
  }
  @Override public String getMessage() {
    return "wrapped field " + f.getName() + " changed size during the execution of " + m.getName();
  }
}
