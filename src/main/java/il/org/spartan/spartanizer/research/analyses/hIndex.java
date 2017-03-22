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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since Dec 14, 2016 */
public interface hIndex {
  static int hindex(@NotNull final List<Pair<String, Int>> ¢) {
    for (int $ = 0; $ < ¢.size(); ++$) {
      if ($ > ¢.get($).second.inner)
        return $;
      System.out.println(¢.get($).first + " : " + ¢.get($).second.inner);
    }
    return ¢.size();
  }

  static void analyze() {
    @NotNull final Map<String, Pair<String, Int>> ranking = new HashMap<>();
    for (@NotNull final File f : inputFiles()) {
      @Nullable final CompilationUnit cu = az.compilationUnit(compilationUnit(f));
      descendants.whoseClassIs(MethodInvocation.class).from(cu).forEach(m -> {
        @NotNull final String key = declarationFile(cu, identifier(name(m)), f.getName()) + name(m) + "(" + arguments(m).size() + " params)";
        ranking.putIfAbsent(key, new Pair<>(key, new Int()));
        ++ranking.get(key).second.inner;
      });
    }
    @NotNull final List<Pair<String, Int>> rs = new ArrayList<>();
    rs.addAll(ranking.values());
    rs.sort((x, y) -> x.second.inner > y.second.inner ? -1 : as.bit(x.second.inner < y.second.inner));
    System.out.println("Max: " + first(rs).first + " [" + first(rs).second.inner + "]");
    System.out.println("min: " + last(rs).first + " [" + last(rs).second.inner + "]");
    System.out.println("h-index: " + hindex(rs));
  }

  @NotNull static String declarationFile(final CompilationUnit u, final String methodName, @NotNull final String fileName) {
    return !methodNames(u).contains(methodName) ? "" : fileName.replaceAll("\\.java", "") + ".";
  }
}
