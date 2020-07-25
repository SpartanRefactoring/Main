package il.org.spartan.spartanizer.traversal;

import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;

import fluent.ly.anonymous;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.spartanizer.tipping.categories.Taxon;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-12 */
public interface Toolboxes {
  static Toolbox empty() {
    return new Toolbox();
  }
  static Toolbox allClone() {
    return Toolbox.full().clone();
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
    return Toolbox.fullStream().count();
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


  @SuppressWarnings("rawtypes") Map<Class<? extends Tipper>, Taxon> categoryMap = anonymous.ly(()->{
    HashMap<Class<? extends Tipper>, Taxon>  $ = new HashMap<>() ;
      Toolbox.fullStream().forEach(λ -> $.put(λ.getClass(), λ.tipperGroup()));
      return $;
  });

  static Taxon groupOf(final Tip ¢) {
    return groupOf(¢.tipperClass);
  }
}
