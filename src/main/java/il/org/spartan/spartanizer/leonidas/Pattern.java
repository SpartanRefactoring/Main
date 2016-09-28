package il.org.spartan.spartanizer.leonidas;

/** @author Ori Marcovitch
 * @year 2016 */
import java.util.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

public class Pattern<N extends ASTNode> {
  ASTNode pattern;
  ASTNode replacement;
  String description;

  public Pattern(final String pattern, final String replacement, final String description) {
    this.pattern = into.e(pattern);
    this.replacement = into.e(replacement);
    this.description = description;
  }

  public UserDefinedTipper<N> getTipper() {
    return new UserDefinedTipper<N>() {
      @Override protected boolean prerequisite(N ¢) {
        return matches(pattern, ¢);
      }

      @Override public String description(@SuppressWarnings("unused") N __) {
        return description;
      }
      // @Override public Tip tip(final N e) {
      // return new Tip(description(e), e) {
      // @Override public void go(final ASTRewrite r, final TextEditGroup g) {
      // r.replace(e, into.e("If.True(" + az.comparison(step.expression(e)) +
      // ").then(" + step.then(e) + ").elze(" + step.elze(e) + ")"), g);
      // }
      // };
      // }
    };
  }

  public boolean matches(ASTNode n) {
    return matches(pattern, n);
  }

  public static boolean matches(ASTNode p, ASTNode n) {
    return new Matcher().matches(p, n);
  }
}
