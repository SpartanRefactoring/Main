package il.org.spartan.spartanizer.research.nanos.characteristics;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.condition;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.initializer;
import static il.org.spartan.spartanizer.ast.navigate.step.initializers;
import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.parametersNames;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;
import static il.org.spartan.spartanizer.ast.navigate.step.right;
import static il.org.spartan.spartanizer.ast.navigate.step.updaters;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

import fluent.ly.as;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.research.UserDefinedTipper;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;
import il.org.spartan.utils.Bool;

/** Catches methods which their control flow is not affected by parameters
 * @author Ori Marcovitch */
public class JDPattern extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x3E08C971278DC2C5L;
  static final Collection<UserDefinedTipper<Expression>> tippers = as.list( //
      patternTipper("$X == null", "", ""), //
      patternTipper("$X != null", "", ""), //
      patternTipper("null == $X", "", ""), //
      patternTipper("null == $X", "", "") //
  );

  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (hazNoParameters(d))
      return false;
    final Collection<String> ps = new HashSet<>(parametersNames(d)), set = new HashSet<>(ps);
    set.addAll(getInfluenced(d, ps));
    final Bool $ = new Bool();
    $.inner = true;
    // noinspection SameReturnValue
    d.accept(new ASTVisitor(true) {
      @Override public boolean visit(final IfStatement ¢) {
        return checkContainsParameter(expression(¢));
      }
      @Override public boolean visit(final ForStatement ¢) {
        return checkContainsParameter(condition(¢)) || checkContainsParameter(initializers(¢)) || checkContainsParameter(updaters(¢));
      }
      @Override public boolean visit(final WhileStatement ¢) {
        return checkContainsParameter(expression(¢));
      }
      @Override public boolean visit(final AssertStatement ¢) {
        return checkContainsParameter(expression(¢));
      }
      @Override public boolean visit(final DoStatement ¢) {
        return checkContainsParameter(expression(¢));
      }
      @Override public boolean visit(final ConditionalExpression ¢) {
        return checkContainsParameter(expression(¢));
      }
      boolean checkContainsParameter(final ASTNode ¢) {
        if (containsParameter(¢, set))
          $.inner = false;
        return false;
      }
      boolean checkContainsParameter(final Iterable<Expression> ¢) {
        return Stream.of(¢).anyMatch(this::checkContainsParameter);
      }
    });
    return $.inner;
  }
  /** @param root node to search in
   * @param ss variable names which are influenced by parameters
   */
  static boolean containsParameter(final ASTNode root, final Collection<String> ss) {
    final Bool $ = new Bool();
    $.inner = false;
    // noinspection SameReturnValue
    root.accept(new ASTVisitor(true) {
      @Override public boolean visit(final SimpleName n) {
        ss.stream().filter(λ -> (n + "").equals(λ) && !nullCheckExpression(az.infixExpression(parent(n)))).forEach(λ -> $.inner = true);
        return false;
      }
    });
    return $.inner;
  }
  static Collection<String> getInfluenced(final MethodDeclaration root, final Collection<String> ps) {
    final Collection<String> $ = new HashSet<>(ps);
    // noinspection SameReturnValue,SameReturnValue,SameReturnValue
    body(root).accept(new ASTVisitor(true) {
      @Override public boolean visit(final Assignment ¢) {
        if (containsParameter(right(¢), $))
          $.add(extractName(left(¢)));
        return true;
      }
      @Override public boolean visit(final VariableDeclarationFragment ¢) {
        if (containsParameter(initializer(¢), $))
          $.add(extractName(name(¢)));
        return true;
      }
      @Override public boolean visit(final SingleVariableDeclaration ¢) {
        if (containsParameter(initializer(¢), $))
          $.add(extractName(initializer(¢)));
        return true;
      }
    });
    return $;
  }
  protected static String extractName(final Expression root) {
    final StringBuilder $ = new StringBuilder();
    // noinspection SameReturnValue
    root.accept(new ASTVisitor(true) {
      @Override public boolean visit(final SimpleName ¢) {
        $.append(¢);
        return false;
      }
    });
    return $ + "";
  }
  static boolean nullCheckExpression(final Expression ¢) {
    return anyTips(tippers, ¢);
  }
}
