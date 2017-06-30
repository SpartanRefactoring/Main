package il.org.spartan.spartanizer.tipping.categories;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;

/** A set of {@link Taxa}
 * @author Yossi Gil
 * @since 2017-06-10 */
public class Taxa extends LinkedHashSet<Taxon> {
  private static final long serialVersionUID = -0x21F078992FCB9A1DL;
  public static Hierarchy<Taxon> hierarchy = new Hierarchy<>(Taxon::parents);
  static {
    Toolbox.fullStream().forEach(Taxa::categories);
  }

  @SuppressWarnings("unchecked") private static Taxa categories(final Tipper<? extends ASTNode> t) {
    final Taxa $ = new Taxa();
    for (final Class<?> ¢ : t.getClass().getInterfaces())
      if (isTaxon(¢))
        $.add(Taxon.of((Class<? extends Category>) ¢));
    return $;
  }
  static Taxa categories(final Taxa $, final Taxon t) {
    for (final Taxon ¢ : t.parents())
      if (!$.contains(¢)) {
        $.add(¢);
        categories($, ¢);
      }
    return $;
  }
  public static Stream<Taxon> categories(final Class<? extends Category> ¢) {
    return categories(new Taxa(), Taxon.of(¢)).stream();
  }
  static boolean isTaxon(final Class<?> x) {
    return Category.class.isAssignableFrom(x);
  }
}
