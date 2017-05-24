package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;

/** Anonyze all the relevant parameters in the method declaration </br>
 * For example transform: </br>
 * <code>
 * void f(int a,int b, int c) {}
 * </code> </br>
 * Into: </br>
 * <code>
 * void f(int _1, int _2, int _3) {}
 * </code> </br>
 * @author Dor Ma'ayan
 * @since 2017-05-24 */
public class AnonymizeAllParameters extends ReplaceCurrentNodeSpanning<MethodDeclaration> {
  private static final long serialVersionUID = 1;

  @Override public ASTNode replacement(MethodDeclaration n) {
    // List<SingleVariableDeclaration> lst = extract.methodArguments(n);
    // Namespace nameSpace = Environment.of(n);
    // nameSpace.contains("dsfg");
    return null;
  }
  @Override public String description(@SuppressWarnings("unused") MethodDeclaration __) {
    return "Anonyze all the relevant parameters in the method declaration";
  }
}
