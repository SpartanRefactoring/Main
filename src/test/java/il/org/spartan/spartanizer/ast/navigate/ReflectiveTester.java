package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.lisp.*;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.engine.*;

/** An abstract class that allows a class to apply testing on its own code.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-18 */
public abstract class ReflectiveTester {
  private static Map<Class<? extends ReflectiveTester>, CompilationUnit> classToASTCompilationUnit = new LinkedHashMap<>();

  protected final ASTNode myCompilationUnit() {
    final Class<? extends ReflectiveTester> c = getClass();
    final CompilationUnit $ = classToASTCompilationUnit.get(c);
    if ($ != null)
      return $;
    classToASTCompilationUnit.put(c,
        loadAST((c.getDeclaringClass() == null ? c : c.getDeclaringClass()).getSimpleName() + ".java"));
    return classToASTCompilationUnit.get(c);
  }

  protected final <N extends ASTNode> N find(final Class<N> ¢) {
    return first(searchDescendants.forClass(¢).from(myCompilationUnit()));
  }

  public List<Annotation> annotations() {
    return searchDescendants.forClass(Annotation.class).from(myCompilationUnit());
  }

  private static CompilationUnit loadAST(final String fileName) {
    for (final File $ : new FilesGenerator(".java").from("."))
      if ($.getAbsolutePath().endsWith(fileName))
        return (CompilationUnit) makeAST.COMPILATION_UNIT.from($);
    return null;
  }
}
