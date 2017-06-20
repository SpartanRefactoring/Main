package il.org.spartan.spartanizer.traversal;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-12 */
public interface Toolboxes {
  static Toolbox empty() {
    return new Toolbox();
  }
  static Toolbox allClone() {
    return Toolbox.all().clone();
  }
  static List<String> get(final Taxon ¢) {
    final List<String> $ = an.empty.list();
    if (¢ == null)
      return $;
    final Toolbox t = allClone();
    assert t.implementation != null;
    Stream.of(t.implementation).filter(Objects::nonNull)
        .forEach(element -> $.addAll(element.stream().filter(λ -> ¢.equals(λ.tipperGroup())).map(Tipper::technicalName).collect(toList())));
    return $;
  }
  static Taxon groupOf(@SuppressWarnings("rawtypes") final Class<? extends Tipper> tipperClass) {
    return categoryMap == null || !categoryMap.containsKey(tipperClass) ? null : categoryMap.get(tipperClass);
  }
  static long hooksCount() {
    return Toolbox.allTippers().count();
  }
  static void main(final String[] args) {
    final Toolbox t = Toolbox.all();
    System.out.printf("Currently, there are a total of %d tippers offered on %d classes", box.it(t.tippersCount()), box.it(t.nodesTypeCount()));
  }
  /** Make a for a specific kind of tippers
   * @param clazz JD
   * @param w JS
   * @return a new configuration containing only the tippers passed as
   *         parameter */
  @SafeVarargs static <N extends ASTNode> Toolbox make(final Class<N> clazz, final Tipper<N>... ts) {
    return empty().add(clazz, ts);
  }
  static String name(final Class<? extends Tipper<?>> ¢) {
    return ¢.getSimpleName();
  }

  @SuppressWarnings("rawtypes") Map<Class<? extends Tipper>, Taxon> categoryMap = new HashMap<Class<? extends Tipper>, Taxon>() {
    static final long serialVersionUID = -0x185C3A40849E91FAL;
    {
      Stream.of(allClone().implementation).filter(Objects::nonNull).forEach(ts -> ts.forEach(λ -> put(λ.getClass(), λ.tipperGroup())));
    }
  };

  static Taxon groupOf(final Tip ¢) {
    return groupOf(¢.tipperClass);
  }
}
