package il.org.spartan.spartanizer.research;

import java.util.*;
import il.org.spartan.utils.Unbox;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

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
  public static <N extends ASTNode> UserDefinedTipper<N> subBlockTipper(final String _pattern, final String _replacement, final String description) {
    return newSubBlockTipper(_pattern, _replacement, description);
  }

  private static <N extends ASTNode> UserDefinedTipper<N> newSubBlockTipper(final String _pattern, final String _replacement,
      final String description) {
    return new UserDefinedTipper<N>() {
      final ASTNode pattern = wizard.ast(reformat$Bs(_pattern));
      final String replacement = reformat$Bs(_replacement);

      @Override public Tip tip(final N n) {
        final Pair<Integer, Integer> p = Matcher.getBlockMatching(az.block(pattern), az.block(n));
        final String matching = stringifySubBlock(n, Unbox.it(p.first), Unbox.it(p.second));
        return new Tip(description(n), n, this.getClass(), getMatchedNodes(az.block(n), p)) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            final Map<String, String> enviroment = collectEnviroment(wizard.ast(matching));
            final Wrapper<String> $ = new Wrapper<>(replacement);
            for (final String ¢ : enviroment.keySet())
              if (¢.startsWith("$B"))
                $.set($.get().replace(¢, enviroment.get(¢) + ""));
            wizard.ast(replacement).accept(new ASTVisitor() {
              @Override public boolean preVisit2(final ASTNode ¢) {
                if (iz.name(¢) && enviroment.containsKey(¢ + ""))
                  $.set($.get().replaceFirst((¢ + "").replace("$", "\\$"), enviroment.get(¢ + "").replace("\\", "\\\\").replace("$", "\\$") + ""));
                return true;
              }
            });
            r.replace(n, wizard.ast(stringifySubBlock(n, 0, p.first) + $.get() + stringifySubBlock(n, p.second)), g);
          }
        };
      }

      @Override protected boolean prerequisite(final N ¢) {
        return Matcher.blockMatches(pattern, ¢);
      }

      @Override public String description(@SuppressWarnings("unused") final N __) {
        return description;
      }

      @Override public ASTNode getMatching(final ASTNode n, final String s) {
        return Matcher.collectEnviromentNodes(pattern, n, new HashMap<>()).get(s);
      }

      Map<String, String> collectEnviroment(final ASTNode ¢) {
        return Matcher.collectEnviroment(pattern, ¢, new HashMap<>());
      }
    };
  }

  public static <N extends ASTNode> UserDefinedTipper<N> patternTipper(final String pattern, final String replacement) {
    return patternTipper(pattern, replacement, String.format("[%s] => [%s]", pattern, replacement));
  }

  public static <N extends ASTNode> UserDefinedTipper<N> patternTipper(final String _pattern, final String _replacement, final String description) {
    final ASTNode pattern = extractStatementIfOne(wizard.ast(reformat$Bs(_pattern)));
    final String replacement = reformat$Bs(_replacement);
    return new UserDefinedTipper<N>() {
      @Override public String description(@SuppressWarnings("unused") final N __) {
        return description;
      }

      @Override public Tip tip(final N n) {
        return new Tip(description(n), n, this.getClass()) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            final Map<String, String> enviroment = collectEnviroment(n);
            final Wrapper<String> $ = new Wrapper<>();
            $.set(replacement);
            for (final String ¢ : enviroment.keySet())
              if (¢.startsWith("$B"))
                $.set($.get().replace(¢, enviroment.get(¢) + ""));
            wizard.ast(replacement).accept(new ASTVisitor() {
              @Override public boolean preVisit2(final ASTNode ¢) {
                if (iz.name(¢) && enviroment.containsKey(¢ + ""))
                  $.set($.get().replaceFirst((¢ + "").replace("$", "\\$"), enviroment.get(¢ + "").replace("\\", "\\\\").replace("$", "\\$") + ""));
                return true;
              }
            });
            r.replace(n, extractStatementIfOne(wizard.ast($.get())), g);
          }
        };
      }

      @Override protected boolean prerequisite(final N ¢) {
        return Matcher.matches(pattern, ¢);
      }

      @Override public ASTNode getMatching(final ASTNode n, final String s) {
        return Matcher.collectEnviromentNodes(pattern, n, new HashMap<>()).get(s);
      }

      Map<String, String> collectEnviroment(final ASTNode ¢) {
        return Matcher.collectEnviroment(pattern, ¢, new HashMap<>());
      }
    };
  }

  public static <N extends ASTNode> String stringifySubBlock(final N n, final int start) {
    final int end = az.block(n).statements().size();
    return start >= end ? "" : stringifySubBlock(n, start, end);
  }

  public static <N extends ASTNode> String stringifySubBlock(final N n, final int start, final int end) {
    if (start >= end)
      return "";
    @SuppressWarnings("unchecked") final List<Statement> ss = az.block(n).statements().subList(start, end);
    return ss.stream().map(x -> x + "").reduce("", (x, y) -> x + y);
  }

  /** @param b
   * @param idxs
   * @return */
  @SuppressWarnings("boxing") protected static ASTNode[] getMatchedNodes(final Block b, final Pair<Integer, Integer> idxs) {
    final ASTNode[] $ = new ASTNode[idxs.second - idxs.first];
    for (int ¢ = idxs.first; ¢ < idxs.second; ++¢)
      $[¢ - idxs.first] = (ASTNode) b.statements().get(idxs.first);
    return $;
  }

  static boolean isBlockVariable(final ASTNode p) {
    return iz.expressionStatement(p) && iz.methodInvocation(az.expressionStatement(p).getExpression()) && blockName(p).startsWith("$B");
  }

  static String blockName(final ASTNode p) {
    return az.methodInvocation(az.expressionStatement(p).getExpression()).getName().getFullyQualifiedName();
  }

  static String reformat$Bs(final String ¢) {
    return ¢.replaceAll("\\$B\\d*", "$0\\(\\);");
  }

  static ASTNode extractStatementIfOne(final ASTNode ¢) {
    return !iz.block(¢) || az.block(¢).statements().size() != 1 ? ¢ : (ASTNode) az.block(¢).statements().get(0);
  }
}
