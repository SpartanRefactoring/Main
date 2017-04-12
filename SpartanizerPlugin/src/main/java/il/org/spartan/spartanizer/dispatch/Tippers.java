package il.org.spartan.spartanizer.dispatch;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** TODO Ori Roth document
 * @author Yossi Gil
 * @since 2017-04-12 */
public interface Tippers {
  interface cache {
    Map<Class<? extends Tipper<?>>, Examples> tipperToExamples = the.lambdaResult(() -> {
      final Map<Class<? extends Tipper<?>>, Examples> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Configurations.allClone().getAllTippers())
        $.put(¢.myClass(), ¢.examples());
      return $;
    });
    Map<String, Class<? extends Tipper<?>>> serivalVersionUIDToTip = the.lambdaResult(() -> {
      final Map<String, Class<? extends Tipper<?>>> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> t : Configurations.allClone().getAllTippers()) {
        final String id = ObjectStreamClass.lookup(t.getClass()).getSerialVersionUID() + "";
        $.put(id, t.myClass());
      }
      return $;
    });
    Map<Class<? extends Tipper<?>>, String> tipperToDescription = the.lambdaResult(() -> {
      final Map<Class<? extends Tipper<?>>, String> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Configurations.allClone().getAllTippers())
        $.put(¢.myClass(), ¢.description());
      return $;
    });
    Map<Class<? extends Tipper<?>>, Tipper<?>> TipperObjectByClassCache = the.lambdaResult(() -> {
      final Map<Class<? extends Tipper<?>>, Tipper<?>> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Configurations.allClone().getAllTippers())
        $.put(¢.myClass(), ¢);
      return $;
    });
  }

  Map<String, String> TipperIDNameTranslationTable = the.lambdaResult(() -> {
    final Map<String, String> $ = new HashMap<>();
    for (final Tipper<? extends ASTNode> t : Configurations.allClone().getAllTippers()) {
      final String id = ObjectStreamClass.lookup(t.getClass()).getSerialVersionUID() + "";
      $.put(id, t.myClass() + "");
    }
    return $;
  });

  @SafeVarargs static <N extends ASTNode> Tipper<N> findTipper(final N n, final Tipper<N>... ts) {
    return Stream.of(ts).filter(λ -> λ.check(n)).findFirst().orElse(null);
  }

  static Tip extractTip(final Tipper<? extends ASTNode> t, final ASTNode n) {
    @SuppressWarnings("unchecked") final Tipper<ASTNode> $ = (Tipper<ASTNode>) t;
    return $.tip(n);
  }

  static <T extends Tipper<? extends ASTNode>> String name(final T ¢) {
    return ¢.getClass().getSimpleName();
  }
}
