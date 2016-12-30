package il.org.spartan.athenizer.inflate.expanders;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;

public class Issue965Aux extends ReflectiveTester {
  @SuppressWarnings("static-method")
  public void check1() {
    @SuppressWarnings("unused") String s = new ArrayList<>() + "";
  }

  public ASTNode getFirstMethod() {
    return find(MethodDeclaration.class);
  }

  public ASTNode getCU() {
    return myCompilationUnit();
  }
}
