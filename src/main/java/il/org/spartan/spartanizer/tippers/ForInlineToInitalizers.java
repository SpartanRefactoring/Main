package il.org.spartan.spartanizer.tippers;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.patterns.*;

/** Test case is {@link Issue0456}
 * function documented in {@link #examples} 
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-04-06
 */
public class ForInlineToInitalizers extends ForStatementPattern implements TipperCategory.Inlining {
  private static final long serialVersionUID = -0x185A5E964CCBD32L;
  
  List<SimpleName> updatedNotDeclared = new ArrayList<>();
  List<SimpleName> candidates = new ArrayList<>();
  
  public ForInlineToInitalizers() {
    updaters.forEach(λ -> candidates.addAll(updatedVars(λ)));
    // remove variables declared in initialization list
    candidates.removeAll(initializers.stream().filter(iz::variableDeclarationExpression)
      .map(az::variableDeclarationExpression)
      .flatMap(λ->step.fragments(λ).stream())
      .map(step::name).collect(Collectors.toList()));
    
    andAlso("Exists updated variable not declared in for initalizers list", 
        () -> !updatedNotDeclared.isEmpty());
//    andAlso("Exists not declared variable that its value is not used after the for",
//        () -> {
//          List<Statement> l = step.statements(current.getParent());
//          if(l == null)
//            return false;
////          for(Statement s : l)
////            if
////          l.subList(l.indexOf(current) + 1, l.size()).stream().filter(predicate)
//          return true;
//        });
  }
    
  private static List<SimpleName> updatedVars(Expression u) {
    List<SimpleName> $ = new ArrayList<>();
    Expression e = extract.core(u), d;
    if((d = step.operand(az.postfixExpression(e))) != null || 
        (d = step.operand(az.prefixExpression(e))) != null || (d = step.left(az.assignment(e))) != null)
      $.add(az.simpleName(d));
    return $;
  }
  
  @Override protected ASTRewrite go(@SuppressWarnings("unused") ASTRewrite __, @SuppressWarnings("unused") TextEditGroup g) {
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") ForStatement __) {
    return "Inline to for initalizers list";
  }
}
