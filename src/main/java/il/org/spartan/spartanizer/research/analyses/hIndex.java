package il.org.spartan.spartanizer.research.analyses;

import static il.org.spartan.spartanizer.research.analyses.util.Files.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since Dec 14, 2016 */
public interface hIndex {
  static int hindex(final List<Pair<String, Int>> ¢) {
    for (int $ = 0; $ < ¢.size(); ++$) {
      if ($ > ¢.get($).second.inner)
        return $;
      System.out.println(¢.get($).first + " : " + ¢.get($).second.inner);
    }
    return ¢.size();
  }

  static void analyze() {
    final Map<String, Pair<String, Int>> ranking = new HashMap<>();
    for (final File f : inputFiles()) {
      final CompilationUnit cu = az.compilationUnit(compilationUnit(f));
      descendants.whoseClassIs(MethodInvocation.class).from(cu).forEach(m -> {
        final String key = declarationFile(cu, identifier(name(m)), f.getName()) + name(m) + "(" + arguments(m).size() + " params)";
        ranking.putIfAbsent(key, new Pair<>(key, new Int()));
        ++ranking.get(key).second.inner;
      });
    }
    final List<Pair<String, Int>> rs = an.empty.list();
    rs.addAll(ranking.values());
    rs.sort((x, y) -> x.second.inner > y.second.inner ? -1 : as.bit(x.second.inner < y.second.inner));
    System.out.println("Max: " + the.headOf(rs).first + " [" + the.headOf(rs).second.inner + "]");
    System.out.println("min: " + the.lastOf(rs).first + " [" + the.lastOf(rs).second.inner + "]");
    System.out.println("h-index: " + hindex(rs));
  }

  static String declarationFile(final CompilationUnit u, final String methodName, final String fileName) {
    return !methodNames(u).contains(methodName) ? "" : fileName.replaceAll("\\.java", "") + ".";
  }
}
