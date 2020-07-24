package il.org.spartan.spartanizer.ast.nodes.metrics;

import java.util.*;
import java.util.function.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** A description of an abstract metric of an ASTNode
 * @author Yossi Gil
 * @since 2017-08-20 */
public abstract class Metric {
  public static Metric.Boolean b(final String name, final Predicate<ASTNode> n) {
    return Metric.named(name).of(n);
  }
  /** @param pattern JD
   * @return */
  public static Set<String> dictionary(final ASTNode u) {
    final Set<String> $ = new LinkedHashSet<>();
    u.accept(new ASTVisitor(true) {
      @Override public void endVisit(final SimpleName node) {
        $.add(step.identifier(node));
      }
    });
    return $;
  }
  
  /** @param u JD
   * @return */
  public static ArrayList<String> dictionary_not_unique(final ASTNode u) {
    final ArrayList<String> $ = new ArrayList<>();
    u.accept(new ASTVisitor(true) {
      @Override public void endVisit(final SimpleName node) {
        $.add(step.identifier(node));
      }
    });
    return $;
  }
  
  public static Maker named(final String name) {
    return new Maker() {
      @Override public Metric.Boolean of(Predicate<ASTNode> ¢) {
        return new Metric.Boolean(name, ¢);
      }
      @Override public Metric.Integral of(ToIntFunction<ASTNode> ¢) {
        return new Metric.Integral(name, ¢);
      }
    };
  }

  public final String name;

  public Metric(final String name) {
    this.name = name;
  }
  public abstract String compute(ASTNode n);

  /** A metric of an {@link ASTNode} with boolean values
   * @author Yossi Gil
   * @since 2017-08-20 */
  public static class Boolean extends Metric {
    final Predicate<ASTNode> predicate;

    public Boolean(String name, Predicate<ASTNode> predicate) {
      super(name);
      this.predicate = predicate;
    }
    @Override public String compute(ASTNode ¢) {
      return predicate.test(¢) + "";
    }
  }

  public static class Integral extends Metric implements ToIntFunction<ASTNode> {
    final ToIntFunction<ASTNode> function;

    Integral(final String name, final ToIntFunction<ASTNode> function) {
      super(name);
      this.function = function;
    }
    public int apply(final ASTNode ¢) {
      return applyAsInt(¢);
    }
    @Override public int applyAsInt(final ASTNode ¢) {
      return function.applyAsInt(¢);
    }
    @Override public String compute(ASTNode ¢) {
      return apply(¢) + "";
    }
  }

  public interface Maker {
    Metric.Boolean of(Predicate<ASTNode> n);
    Metric.Integral of(ToIntFunction<ASTNode> n);
  }
}
