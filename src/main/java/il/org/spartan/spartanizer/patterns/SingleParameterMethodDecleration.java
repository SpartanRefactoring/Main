package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

/**
 * A single parameter method decleration pattern 
 * @author Raviv Rachmiel <tt>raviv.rachmiel@gmail.com</tt>
 * @since 2017-03-29
 */
public class SingleParameterMethodDecleration extends AbstractPattern<MethodDeclaration> {

  private static final long serialVersionUID = 9180181890030651528L;
  protected Expression initializer;
  protected SimpleName name;
  protected Type type;

  protected SingleParameterMethodDecleration() {
    
  }

  protected Type type() {
    return type;
  }

  final SimpleName name() {
    return name;
  }

  //TODO: Raviv, implement -rr
  @SuppressWarnings({ "unused", "unused" })
  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    return null;
  }

  //TODO: Raviv, implement -rr
  @Override public String description(@SuppressWarnings("unused") MethodDeclaration n) {
    return null;
  }

  
}
