package il.org.spartan.spartanizer.research.nanos.characteristics;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.utils.*;
import org.jetbrains.annotations.NotNull;

/** Catches methods which their control flow is not affected by parameters
 * @author Ori Marcovitch */
public class JDPattern extends JavadocMarkerNanoPattern {
  static final Set<UserDefinedTipper<Expression>> tippers = new HashSet<UserDefinedTipper<Expression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X == null", "", ""));
      add(patternTipper("$X != null", "", ""));
      add(patternTipper("null == $X", "", ""));
      add(patternTipper("null == $X", "", ""));
    }
  };

  @Override protected boolean prerequisites(@NotNull final MethodDeclaration d) {
    if (hazNoParameters(d))
      return false;
    final Set<String> ps = new HashSet<>(parametersNames(d)), set = new HashSet<>(ps);
    set.addAll(getInfluenced(d, ps));
    final Bool $ = new Bool();
    $.inner = true;
    d.accept(new ASTVisitor() {
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

      boolean checkContainsParameter(@NotNull final ASTNode ¢) {
        if (containsParameter(¢, set))
          $.inner = false;
        return false;
      }

      boolean checkContainsParameter(@NotNull final List<Expression> xs) {
        for (final Expression ¢ : xs)
          if (checkContainsParameter(¢))
            return true;
        return false;
      }
    });
    return $.inner;
  }

  /** @param root node to search in
   * @param ss variable names which are influenced by parameters
   * @return */
  static boolean containsParameter(@NotNull final ASTNode root, @NotNull final Set<String> ss) {
    final Bool $ = new Bool();
    $.inner = false;
    root.accept(new ASTVisitor() {
      @Override public boolean visit(final SimpleName n) {
        ss.stream().filter(λ -> (n + "").equals(λ) && !nullCheckExpression(az.infixExpression(parent(n)))).forEach(λ -> $.inner = true);
        return false;
      }
    });
    return $.inner;
  }

  @NotNull static Set<String> getInfluenced(final MethodDeclaration root, @NotNull final Set<String> ps) {
    final Set<String> $ = new HashSet<>();
    $.addAll(ps);
    body(root).accept(new ASTVisitor() {
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

  @NotNull protected static String extractName(@NotNull final Expression root) {
    final StringBuilder $ = new StringBuilder();
    root.accept(new ASTVisitor() {
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
