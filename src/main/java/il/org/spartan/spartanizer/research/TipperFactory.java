package il.org.spartan.spartanizer.research;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

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
public class TipperFactory {
  public static UserDefinedTipper<Block> statementsPattern(final String _pattern, final String _replacement, final String description) {
    return newSubBlockTipper(_pattern, _replacement, description);
  }

  private static UserDefinedTipper<Block> newSubBlockTipper(final String pattern, final String replacement, final String description) {
    Matcher m = new Matcher(pattern, replacement);
    return new UserDefinedTipper<Block>() {
      @Override public Tip tip(final Block n) {
        return new Tip(description(n), n, this.getClass(), m.getMatchedNodes(az.block(n))) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            ASTNode ast = m.blockReplacement(n);
            r.replace(n, ast, g);
          }
        };
      }

      @Override protected boolean prerequisite(final Block ¢) {
        return m.blockMatches(¢);
      }

      @Override public String description(@SuppressWarnings("unused") final Block __) {
        return description;
      }

      @Override public ASTNode getMatching(final ASTNode n, final String s) {
        return m.collectEnviromentNodes(n, new HashMap<>()).get(s);
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
   * @param _pattern Pattern to match against
   * @param _replacement Replacement pattern
   * @param description Description of the tipper
   * @return {@link UserDefinedTipper} */
  public static <N extends ASTNode> UserDefinedTipper<N> patternTipper(final String _pattern, final String _replacement, final String description) {
    final Matcher m = new Matcher(_pattern, _replacement);
    return new UserDefinedTipper<N>() {
      @Override public String description(@SuppressWarnings("unused") final N __) {
        return description;
      }

      @Override public Tip tip(final N n) {
        return new Tip(description(n), n, this.getClass()) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            r.replace(n, m.replacement(n), g);
          }
        };
      }

      @Override protected boolean prerequisite(final N ¢) {
//        System.out.println(m.pattern);
//        System.out.println(¢);
        return m.matches(¢);
      }

      @Override public ASTNode getMatching(final ASTNode n, final String s) {
        return m.collectEnviromentNodes(n, new HashMap<>()).get(s);
      }
    };
  }
}
