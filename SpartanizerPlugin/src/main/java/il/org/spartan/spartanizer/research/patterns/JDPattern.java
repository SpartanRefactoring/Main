package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.leonidas.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class JDPattern extends JavadocMarkerNanoPattern<MethodDeclaration> {
  Set<UserDefinedTipper<Statement>> tippers;

  @Override protected boolean prerequisites(final MethodDeclaration d) {
    @SuppressWarnings("unchecked") final List<String> ps = (List<String>) step.parameters(d).stream().map(x -> x.getName() + "");
    final Bool $ = new Bool();
    $.inner = true;
    d.accept(new ASTVisitor() {
      @Override public boolean visit(final IfStatement ¢) {
        if (containsParameter(¢, ps))
          $.inner = false;
        return false;
      }

      @Override public boolean visit(@SuppressWarnings("unused") final ForStatement __) {
        $.inner = false;
        return false;
      }

      @Override public boolean visit(@SuppressWarnings("unused") final WhileStatement __) {
        $.inner = false;
        return false;
      }

      @Override public boolean visit(@SuppressWarnings("unused") final EnhancedForStatement __) {
        $.inner = false;
        return false;
      }

      @Override public boolean visit(@SuppressWarnings("unused") final TryStatement __) {
        $.inner = false;
        return false;
      }

      @Override public boolean visit(@SuppressWarnings("unused") final AssertStatement __) {
        $.inner = false;
        return false;
      }

      @Override public boolean visit(@SuppressWarnings("unused") final DoStatement __) {
        $.inner = false;
        return false;
      }
    });
    return $.inner;
  }

  /** @param n
   * @param ps
   * @return */
  protected static boolean containsParameter(final ASTNode root, final List<String> ps) {
    final Bool $ = new Bool();
    $.inner = false;
    root.accept(new ASTVisitor() {
      @Override public boolean visit(final SimpleName n) {
        for (final String p : ps)
          if ((n + "").equals(p))
            $.inner = true;
        return false;
      }
    });
    return $.inner;
  }

  @Override public String description(final MethodDeclaration ¢) {
    return ¢.getName() + " is a JD method";
  }

  @Override protected String javadoc() {
    return "[[JDPattern]]";
  }
}
