package il.org.spartan.spartanizer.research;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.Matcher.*;

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
      @NotNull final String description, final Option... os) {
    @NotNull final Matcher $ = Matcher.blockMatcher(pattern, replacement, os);
    return new UserDefinedTipper<Block>() {
      static final long serialVersionUID = 4793662826325577880L;

      @NotNull @Override public Fragment tip(final Block n) {
        return new Fragment(description(n), n, getClass(), $.getMatchedNodes(az.block(n))) {
          @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
            r.replace(n, $.blockReplacement(n), g);
          }
        };
      }

      @Override protected boolean prerequisite(final Block ¢) {
        return $.blockMatches(¢);
      }

      @NotNull @Override public String description(@SuppressWarnings("unused") final Block __) {
        return description;
      }

      @Override public ASTNode getMatching(final ASTNode n, final String s) {
        return $.getMatching(n, s);
      }

      @Override public ASTNode getMatching(final ASTNode ¢) {
        return $.replacement(¢);
      }

      @NotNull @Override public String pattern() {
        return pattern;
      }

      @NotNull @Override public String replacement() {
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
    @NotNull final Matcher $ = Matcher.patternMatcher(pattern, replacement);
    return new UserDefinedTipper<N>() {
      static final long serialVersionUID = 2503735679621778024L;

      @NotNull @Override public String description(@SuppressWarnings("unused") final N __) {
        return description;
      }

      @NotNull @Override public Fragment tip(@NotNull final N n) {
        return new Fragment(description(n), n, getClass()) {
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

      @NotNull @Override public String pattern() {
        return pattern;
      }

      @NotNull @Override public String replacement() {
        return replacement;
      }
    };
  }
}
