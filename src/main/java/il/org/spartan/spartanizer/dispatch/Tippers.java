package il.org.spartan.spartanizer.dispatch;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** TODO Ori Roth document
 * @author Yossi Gil
 * @since 2017-04-12 */
public interface Tippers {
  interface cache {
    Map<Class<? extends Tipper<?>>, Examples> tipperToExamples = the.lambdaResult(() -> {
      final Map<Class<? extends Tipper<?>>, Examples> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Configurations.freshCopyOfAllTippers().getAllTippers())
        $.put(¢.myClass(), ¢.examples());
      return $;
    });
    Map<String, Class<? extends Tipper<?>>> serivalVersionUIDToTip = the.lambdaResult(() -> {
      final Map<String, Class<? extends Tipper<?>>> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> t : Configurations.freshCopyOfAllTippers().getAllTippers()) {
        final String id = ObjectStreamClass.lookup(t.getClass()).getSerialVersionUID() + "";
        $.put(id, t.myClass());
      }
      return $;
    });
    Map<Class<? extends Tipper<?>>, String> tipperToDescription = the.lambdaResult(() -> {
      final Map<Class<? extends Tipper<?>>, String> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Configurations.freshCopyOfAllTippers().getAllTippers())
        $.put(¢.myClass(), ¢.description());
      return $;
    });
    Map<Class<? extends Tipper<?>>, Tipper<?>> TipperObjectByClassCache = the.lambdaResult(() -> {
      final Map<Class<? extends Tipper<?>>, Tipper<?>> $ = new HashMap<>();
      for (final Tipper<? extends ASTNode> ¢ : Configurations.freshCopyOfAllTippers().getAllTippers())
        $.put(¢.myClass(), ¢);
      return $;
    });
  }

  Map<String, String> TipperIDNameTranslationTable = the.lambdaResult(() -> {
    final Map<String, String> $ = new HashMap<>();
    for (final Tipper<? extends ASTNode> t : Configurations.freshCopyOfAllTippers().getAllTippers()) {
      final String id = ObjectStreamClass.lookup(t.getClass()).getSerialVersionUID() + "";
      $.put(id, t.myClass() + "");
    }
    return $;
  });
}
