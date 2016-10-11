package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;

/** Scans files named by folder, ignore test files, and collect statistics, on
 * classes, methods, etc.
 * @author Yossi Gil
 * @year 2015 */
public final class Spartanizer extends AbstractBatch {
  static final List<Class<? extends BodyDeclaration>> selectedNodeTypes = as.list(MethodDeclaration.class);

  public static void main(final String[] args) {
    for (final String ¢ : args.length != 0 ? args : new String[] { "." })
      new Spartanizer(¢).fire();
  }

  Spartanizer(final String path) {
    super(path);
  }

  @Override protected boolean check(final ASTNode ¢) {
    return !selectedNodeTypes.contains(¢.getClass());
  }
}
