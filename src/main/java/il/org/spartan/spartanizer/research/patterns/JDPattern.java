package il.org.spartan.spartanizer.research.patterns;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.leonidas.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class JDPattern extends JavadocMarkerNanoPattern<MethodDeclaration> {
  static Set<UserDefinedTipper<Expression>> tippers;

  public JDPattern() {
    if (tippers != null)
      return;
    tippers = new HashSet<>();
    tippers.add(TipperFactory.tipper("$X == null", "", ""));
    tippers.add(TipperFactory.tipper("$X != null", "", ""));
    tippers.add(TipperFactory.tipper("null == $X", "", ""));
    tippers.add(TipperFactory.tipper("null == $X", "", ""));
  }

  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (step.parameters(d) == null || step.parameters(d).isEmpty())
      return false;
    final List<String> ps = step.parameters(d).stream().map(x -> x.getName() + "").collect(Collectors.toList());
    final Bool $ = new Bool();
    $.inner = true;
    d.accept(new ASTVisitor() {
      @Override public boolean visit(final IfStatement ¢) {
        return checkContainsParameter(¢.getExpression());
      }

      @Override public boolean visit(final ForStatement ¢) {
        return checkContainsParameter(step.condition(¢)) || checkContainsParameter(step.initializers(¢)) || checkContainsParameter(step.updaters(¢));
      }

      @Override public boolean visit(final WhileStatement ¢) {
        return checkContainsParameter(¢);
      }

      @Override public boolean visit(final EnhancedForStatement ¢) {
        return checkContainsParameter(¢);
      }

      @Override public boolean visit(final TryStatement ¢) {
        return checkContainsParameter(¢);
      }

      @Override public boolean visit(final AssertStatement ¢) {
        return checkContainsParameter(¢);
      }

      @Override public boolean visit(final DoStatement ¢) {
        return checkContainsParameter(¢);
      }

      boolean checkContainsParameter(final ASTNode ¢) {
        if (containsParameter(¢, ps))
          $.inner = false;
        return false;
      }

      boolean checkContainsParameter(List<Expression> xs) {
        for (Expression ¢ : xs)
          if (checkContainsParameter(¢))
            return true;
        return false;
      }
    });
    return $.inner;
  }

  /** @param n
   * @param ps
   * @return */
  protected static boolean containsParameter(ASTNode root, List<String> ps) {
    final Bool $ = new Bool();
    $.inner = false;
    root.accept(new ASTVisitor() {
      @Override public boolean visit(final SimpleName n) {
        for (String p : ps)
          if ((n + "").equals(p) && !nullCheckExpression(az.infixExpression(n.getParent())))
            $.inner = true;
        return false;
      }
    });
    return $.inner;
  }

  /** [[SuppressWarningsSpartan]] */
  static boolean nullCheckExpression(Expression ¢) {
    if (¢ == null)
      return false;
    for (UserDefinedTipper<Expression> t : tippers)
      if (t.canTip(¢))
        return true;
    return false;
  }

  @Override public String description(final MethodDeclaration ¢) {
    return ¢.getName() + " is a JD method";
  }

  @Override protected String javadoc() {
    return "[[JDPattern]]";
  }
}
