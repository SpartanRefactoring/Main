package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.utils.*;

/**
 * TODO Yossi Gil: document class 
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-04-01
 */
public enum compute {
  ;

  public static List<String> usedNames(final Expression x) {
    return new ExpressionMapReducer<List<String>>() {
      @Override public List<String> reduce() {
        return new ArrayList<>();
      }
  
      @Override public List<String> reduce(final List<String> ss1, final List<String> ss2) {
        if (ss1 == null && ss2 == null)
          return new ArrayList<>();
        if (ss1 == null)
          return ss2;
        if (ss2 == null)
          return ss1;
        ss1.addAll(ss2);
        return ss1;
      }
  
      @Override protected List<String> map(final SimpleName ¢) {
        final String $ = ¢.getIdentifier();
        return guessName.of($) != guessName.METHOD_OR_VARIABLE ? reduce() : as.list($);
      }
  
      @Override protected List<String> map(@SuppressWarnings("unused") final ThisExpression ¢) {
        return reduce();
      }
    }.map(x);
  }

  public static List<ASTNode> updatedVariables(final Expression x) {
    final List<ASTNode> $ = new ExpressionMapReducer<List<ASTNode>>() {
      @Override public List<ASTNode> reduce() {
        return new LinkedList<>();
      }
  
      @Override public List<ASTNode> reduce(final List<ASTNode> l1, final List<ASTNode> l2) {
        if (l1 == null)
          return l2;
        if (l2 == null)
          return l1;
        l1.addAll(l2);
        return l1;
      }
  
      @Override protected List<ASTNode> map(final Assignment ¢) {
        return reduce(list(¢), super.map(¢));
      }
  
      @Override protected List<ASTNode> map(final PostfixExpression ¢) {
        return reduce(Collections.singletonList(step.expression(¢)), super.map(¢));
      }
  
      @Override protected List<ASTNode> map(final PrefixExpression ¢) {
        return reduce(!updating(¢) ? reduce() : atomic(¢.getOperand()), super.map(¢));
      }
  
      List<ASTNode> atomic(final Expression ¢) {
        return Collections.singletonList(¢);
      }
  
      List<ASTNode> list(final ASTNode ¢) {
        return new ArrayList<>(Collections.singletonList(¢));
      }
  
      boolean updating(final PrefixExpression ¢) {
        return in(¢.getOperator(), INCREMENT, DECREMENT);
      }
    }.map(x);
    return $ != null ? $ : empty.list();
  }
}
