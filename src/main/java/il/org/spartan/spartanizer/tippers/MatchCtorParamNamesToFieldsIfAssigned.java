package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/**
 * @since 07-Dec-16
 * @author Doron Meshulam
 *
 */


public class MatchCtorParamNamesToFieldsIfAssigned extends CarefulTipper<MethodDeclaration> implements TipperCategory.Idiomatic {
  @Override public String description(final MethodDeclaration ¢) {
    return "Match constructor parameter names to fields";
  }

  @Override public Tip tip(final MethodDeclaration d) {
    if (!d.isConstructor())
      return null; 
     
    List<SingleVariableDeclaration> ctorParams = parameters(d);
    List<Statement> bodyStatements = d.getBody().statements();
    for (Statement s : bodyStatements) {
      if (!(s instanceof ExpressionStatement))
        continue;
      Expression e = ((ExpressionStatement) s).getExpression();
      if (!(e instanceof Assignment))
        continue;
      
      Assignment a = (Assignment) e;
      Expression leftAss = a.getLeftHandSide(); // LOL ^_^
      Expression rightAss = a.getRightHandSide();
      if(!(leftAss instanceof FieldAccess)) {
        continue;
      }
      
      if(!(((FieldAccess)leftAss).getExpression() instanceof ThisExpression)) {
        continue;
      }
      
      SimpleName fieldName = ((FieldAccess)leftAss).getName();
      if (!(rightAss instanceof SimpleName)) {
        continue;
      }
      SimpleName paramName = (SimpleName)a.getRightHandSide();
//    Check if right is parameter and also there wasn't a local variable in the name of the field4
//        
//      
//    List<String> uses = Collect.usesOf("y").inside(d.getBody());
    }
    
    
    return new Tip(description(d), d, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        //TODO: Change of code here
        System.out.println(r);
        System.out.println(g);
      }
    };
  }
}
