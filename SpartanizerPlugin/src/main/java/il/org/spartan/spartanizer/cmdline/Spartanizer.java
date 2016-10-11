package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;

/** Scans files named by folder, ignore test files, and collect statistics, on
 * classes, methods, etc.
 * @author Yossi Gil
 * @year 2015 */
public final class Spartanizer extends AbstractBatch {
  static List<Class<? extends BodyDeclaration>> selectedNodeTypes = as.list(MethodDeclaration.class, TypeDeclaration.class);
  
  @SuppressWarnings("static-method") public List<Class<? extends BodyDeclaration>> selectedNodes(@SuppressWarnings("unchecked") final Class<? extends BodyDeclaration> ... n){
    return as.list(n);
  }
//  static final List<Class<? extends ASTNode>> selNodeTypes = as.list(MethodDeclaration.class, TypeDeclaration.class);
  public static void main(final String[] args) {
    for (final String ¢ : args.length != 0 ? args : new String[] { "." })
      new Spartanizer(¢).fire();
  }

  Spartanizer(final String path) {
    super(path);
  }

  @Override protected boolean check(final ASTNode ¢) {
    // if astnode is in selectedNodeType return false
    return !selectedNodeTypes.contains(¢.getClass());
  }
}
