package il.org.spartan.spartanizer.research;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.Matcher.*;
import il.org.spartan.spartanizer.tipping.*;

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
  public static UserDefinedTipper<Block> statementsPattern(final String _pattern, final String _replacement, final String description,
      final Option... os) {
    return newSubBlockTipper(_pattern, _replacement, description, os);
  }
  private static UserDefinedTipper<Block> newSubBlockTipper(final String pattern, final String replacement, final String description,
      final Option... os) {
    final Matcher $ = Matcher.blockMatcher(pattern, replacement, os);
    return new UserDefinedTipper<Block>() {
      static final long serialVersionUID = 0x428682F150219098L;

      @Override public Tip tip(final Block n) {
        return new Tip(description(n), myClass(), n, $.getMatchedNodes(az.block(n))) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            r.replace(n, $.blockReplacement(n), g);
          }
        };
      }
      @Override protected boolean prerequisite(final Block ¢) {
        return $.blockMatches(¢);
      }
      @Override public String description(@SuppressWarnings("unused") final Block __) {
        return description;
      }
      @Override public ASTNode getMatching(final ASTNode n, final String s) {
        return $.getMatching(n, s);
      }
      @Override public ASTNode getMatching(final ASTNode ¢) {
        return $.replacement(¢);
      }
      @Override public String pattern() {
        return pattern;
      }
      @Override public String replacement() {
        return replacement;
      }
    };
  }
  public static <N extends ASTNode> UserDefinedTipper<N> patternTipper(final String pattern, final String replacement) {
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
  public static <N extends ASTNode> UserDefinedTipper<N> patternTipper(final String pattern, final String replacement, final String description) {
    final Matcher $ = Matcher.patternMatcher(pattern, replacement);
    return new UserDefinedTipper<N>() {
      static final long serialVersionUID = 0x22BF0E55D353CA68L;

      @Override public String description(@SuppressWarnings("unused") final N __) {
        return description;
      }
      @Override public Tip tip(final N n) {
        return new Tip(description(n), getClass(), n) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
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
      @Override public String pattern() {
        return pattern;
      }
      @Override public String replacement() {
        return replacement;
      }
    };
  }
}
