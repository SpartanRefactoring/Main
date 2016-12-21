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
abstract class ReflectionTester {
  private static Map<Class<? extends ReflectionTester>, CompilationUnit> classToASTCompilationUnit = new LinkedHashMap<>();

  protected final ASTNode myCompilationUnit() {
    final Class<? extends ReflectionTester> c = getClass();
    final CompilationUnit $ = classToASTCompilationUnit.get(c);
    if ($ != null)
      return $;
    classToASTCompilationUnit.put(c, loadAST(c.getSimpleName() + ".java"));
    return classToASTCompilationUnit.get(c);
  }

  protected Initializer initializer = second(searchDescendants.forClass(Initializer.class).from(myCompilationUnit()));

  private static CompilationUnit loadAST(final String fileName) {
    for (final File $ : new FilesGenerator(".java").from("."))
      if ($.getAbsolutePath().endsWith(fileName))
        return (CompilationUnit) makeAST.COMPILATION_UNIT.from($);
    return null;
  }
}
