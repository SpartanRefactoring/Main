package il.org.spartan.spartanizer.traversal;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** TODO Ori Roth document
 * @author Yossi Gil
 * @since 2017-04-12 */
public interface Tippers {
  interface cache {
    Map<String, Class<? extends Tipper<?>>> serivalVersionUIDToTipper = anonymous.ly(() -> {
      final Map<String, Class<? extends Tipper<?>>> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Toolbox.full().getAllTippers())
        $.put(find(¢) + "", ¢.myClass());
      return $;
    });
    Map<Class<? extends Tipper<?>>, Tipper<?>> tipperClassToTipperInstance = anonymous.ly(() -> {
      final Map<Class<? extends Tipper<?>>, Tipper<?>> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Toolbox.full().getAllTippers())
        $.put(¢.myClass(), ¢);
      return $;
    });
    Map<Class<? extends Tipper<?>>, String> tipperToDescription = anonymous.ly(() -> {
      final Map<Class<? extends Tipper<?>>, String> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Toolbox.full().getAllTippers())
        $.put(¢.myClass(), ¢.description());
      return $;
    });
    Map<Class<? extends Tipper<?>>, Examples> tipperToExamples = anonymous.ly(() -> {
      final Map<Class<? extends Tipper<?>>, Examples> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Toolbox.full().getAllTippers())
        $.put(¢.myClass(), ¢.examples());
      return $;
    });
    Map<String, String> idToNameOriWhatsThisFindAGoodName = anonymous.ly(() -> {
      final Map<String, String> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Toolbox.full().getAllTippers())
        $.put(Tippers.find(¢) + "", ¢.tipperName());
      return $;
    });
  }

  static Tip extractTip(final Tipper<? extends ASTNode> t, final ASTNode n) {
    @SuppressWarnings("unchecked") final Tipper<ASTNode> $ = (Tipper<ASTNode>) t;
    return $.tip(n);
  }
  static long find(final Tipper<? extends ASTNode> ¢) {
    return ObjectStreamClass.lookup(¢.getClass()).getSerialVersionUID();
  }
  @SafeVarargs static <N extends ASTNode> Tipper<N> findTipper(final N n, final Tipper<N>... ts) {
    return Stream.of(ts).filter(λ -> λ.check(n)).findFirst().orElse(null);
  }
  static <T extends Tipper<? extends ASTNode>> String name(final T ¢) {
    return ¢.getClass().getSimpleName();
  }
}
