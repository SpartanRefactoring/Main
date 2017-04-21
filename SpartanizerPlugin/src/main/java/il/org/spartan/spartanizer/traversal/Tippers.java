package il.org.spartan.spartanizer.traversal;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** This interface {@link Tippers} contain Tippers functionality for extracting
 * tip from {@link ASTNode}, finding Tipper's serialVersionUID, getting Tipper's
 * name, and more. {@link Tipper}.<br>
 * The inner interface {@link cache} contains few tipper mapping tables:
 * <ul>
 * <li>serialVersionUID -> Tipper</li>
 * <li>Tipper's class -> Tipper</li>
 * <li>Tipper's class -> Tipper's description</li>
 * <li>Tipper's class -> Tipper's examples</li>
 * </ul>
 * NOTE: the tables are built lazily
 * @author oran1248
 * @since 2017-04-21 */
public interface Tippers {
  interface cache {
    Map<String, Class<? extends Tipper<?>>> serivalVersionUIDToTipper = anonymous.ly(() -> {
      final Map<String, Class<? extends Tipper<?>>> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Configurations.all().getAllTippers())
        $.put(find(¢) + "", ¢.myClass());
      return $;
    });
    Map<Class<? extends Tipper<?>>, Tipper<?>> TipperObjectByClassCache = anonymous.ly(() -> {
      final Map<Class<? extends Tipper<?>>, Tipper<?>> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Configurations.all().getAllTippers())
        $.put(¢.myClass(), ¢);
      return $;
    });
    Map<Class<? extends Tipper<?>>, String> tipperToDescription = anonymous.ly(() -> {
      final Map<Class<? extends Tipper<?>>, String> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Configurations.all().getAllTippers())
        $.put(¢.myClass(), ¢.description());
      return $;
    });
    Map<Class<? extends Tipper<?>>, Examples> tipperToExamples = anonymous.ly(() -> {
      final Map<Class<? extends Tipper<?>>, Examples> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Configurations.all().getAllTippers())
        $.put(¢.myClass(), ¢.examples());
      return $;
    });
    Map<String, String> TipperIDNameTranslationTable = anonymous.ly(() -> {
      final Map<String, String> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Configurations.allClone().getAllTippers())
        $.put(find(¢) + "", ¢.tipperName());
      return $;
    });
  }

<<<<<<< HEAD
  Map<String, String> TipperIDNameTranslationTable = anonymous.ly(() -> {
    final Map<String, String> $ = new HashMap<>();
    for (final Tipper<? extends ASTNode> ¢ : Configurations.all().getAllTippers())
      $.put(find(¢) + "", ¢.tipperName());
    return $;
  });

=======
  /** Returns the tip tipper {@code t} can apply to the given node {@code n}. */
>>>>>>> branch 'master' of git@github.com:SpartanRefactoring/Spartanizer.git
  static Tip extractTip(final Tipper<? extends ASTNode> t, final ASTNode n) {
    @SuppressWarnings("unchecked") final Tipper<ASTNode> $ = (Tipper<ASTNode>) t;
    return $.tip(n);
  }

  /** Returns the serialVersionUID field's value for the given tipper. */
  static long find(final Tipper<? extends ASTNode> ¢) {
    return ObjectStreamClass.lookup(¢.getClass()).getSerialVersionUID();
  }

  /** Returns the first tipper that can apply to node {@code n}. */
  @SafeVarargs static <N extends ASTNode> Tipper<N> findTipper(final N n, final Tipper<N>... ts) {
    return Stream.of(ts).filter(λ -> λ.check(n)).findFirst().orElse(null);
  }

  /** Returns the given tipper's name. */
  static <T extends Tipper<? extends ASTNode>> String name(final T ¢) {
    return ¢.getClass().getSimpleName();
  }
}
