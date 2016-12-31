package il.org.spartan.athenizer.inflate.expanders;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;


/**
 * [[SuppressWarningsSpartan]]
 */
public class Issue965Aux extends ReflectiveTester {
  @SuppressWarnings({ "static-method", "unused" })
  public void check1() {
    List<Integer> lst =  new ArrayList<>();
    String s =  lst+"";
  }

  public ASTNode getFirstMethod() {
    return find(MethodDeclaration.class);
  }

  public ASTNode getCU() {
    return myCompilationUnit();
  }
}
