package il.org.spartan.spartanizer.research;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.Matcher.*;
import org.jetbrains.annotations.NotNull;

/** Factory to create tippers out of user strings! Much easier to implement
 * tippers with. <br>
 * $Xi for expression i.e. - foo(a,b,c)*d + 17 <br>
 * $M for MethodInvocation i.e. - func() <br>
 * $N for Name i.e. - func <br>
 * $B for block or statement i.e. - if(x) return 17; <br>
 * $A for method arguments i.e. - func($A) will match func(1,obj, 17+2) and even
 * func() <br>
 * @author Ori Marcovitch
 * @since 2016 */
public enum TipperFactory {
  ;
  @NotNull public static UserDefinedTipper<Block> statementsPattern(@NotNull final String _pattern, @NotNull final String _replacement,
      @NotNull final String description, final Option... os) {
    return newSubBlockTipper(_pattern, _replacement, description, os);
  }

  @NotNull private static UserDefinedTipper<Block> newSubBlockTipper(@NotNull final String pattern, @NotNull final String replacement,
      @NotNull final String description, final Option[] os) {
    final Matcher $ = Matcher.blockMatcher(pattern, replacement, os);
    return new UserDefinedTipper<Block>() {
      @Override @NotNull public Tip tip(@NotNull final Block n) {
        return new Tip(description(n), n, getClass(), $.getMatchedNodes(az.block(n))) {
          @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
            r.replace(n, $.blockReplacement(n), g);
          }
        };
      }

      @Override protected boolean prerequisite(final Block ¢) {
        return $.blockMatches(¢);
      }

      @Override @NotNull public String description(@SuppressWarnings("unused") final Block __) {
        return description;
      }

      @Override public ASTNode getMatching(final ASTNode n, final String s) {
        return $.getMatching(n, s);
      }

      @Override public ASTNode getMatching(final ASTNode ¢) {
        return $.replacement(¢);
      }

      @Override @NotNull public String pattern() {
        return pattern;
      }

      @Override @NotNull public String replacement() {
        return replacement;
      }
    };
  }

  @NotNull public static <N extends ASTNode> UserDefinedTipper<N> patternTipper(@NotNull final String pattern, @NotNull final String replacement) {
    return patternTipper(pattern, replacement, String.format("[%s] => [%s]", pattern, replacement));
  }

  /** Creates a tipper that can tip ASTNodes that can be matched against
   * <b>_pattern</b>,<br>
   * and transforms them to match the pattern <b>_replacement</b>, using the
   * same values<br>
   * for each pattern variable.
   * @param pattern Pattern to match against
   * @param replacement Replacement pattern
   * @param description Description of the tipper
   * @return {@link UserDefinedTipper} */
  @NotNull public static <N extends ASTNode> UserDefinedTipper<N> patternTipper(@NotNull final String pattern, @NotNull final String replacement,
      @NotNull final String description) {
    final Matcher $ = Matcher.patternMatcher(pattern, replacement);
    return new UserDefinedTipper<N>() {
      @Override @NotNull public String description(@SuppressWarnings("unused") final N __) {
        return description;
      }

      @Override @NotNull public Tip tip(@NotNull final N n) {
        return new Tip(description(n), n, getClass()) {
          @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
            r.replace(n, $.replacement(n), g);
          }
        };
      }

      @Override protected boolean prerequisite(final N ¢) {
        return $.matches(¢);
      }

      @Override public ASTNode getMatching(final ASTNode n, final String s) {
        return $.getMatching(n, s);
      }

      @Override public ASTNode getMatching(final ASTNode ¢) {
        return $.replacement(¢);
      }

      @Override @NotNull public String pattern() {
        return pattern;
      }

      @Override @NotNull public String replacement() {
        return replacement;
      }
    };
  }
}
