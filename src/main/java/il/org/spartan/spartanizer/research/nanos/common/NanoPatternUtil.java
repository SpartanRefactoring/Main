package il.org.spartan.spartanizer.research.nanos.common;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.nanos.*;

public interface NanoPatternUtil {
  static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢)//
        || body(¢) == null//
        || anyTips(NanoPatternsConfiguration.excluded, ¢);
  }

  static boolean anyTips(final Collection<JavadocMarkerNanoPattern> ps, final MethodDeclaration d) {
    return d != null && ps.stream().anyMatch(λ -> λ.canTip(d));
  }
}
