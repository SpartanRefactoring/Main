package il.org.spartan.spartanizer.leonidas;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;

public class Pattern<N extends ASTNode> {
  public static boolean matches(final ASTNode p, final ASTNode n) {
    return new Matcher().matches(p, n);
  }

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
      @Override public String description(@SuppressWarnings("unused") final N __) {
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

      @Override protected boolean prerequisite(final N ¢) {
        return matches(pattern, ¢);
      }
    };
  }

  public boolean matches(final ASTNode ¢) {
    return matches(pattern, ¢);
  }
}
