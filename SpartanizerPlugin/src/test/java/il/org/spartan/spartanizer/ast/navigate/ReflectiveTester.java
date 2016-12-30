package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.lisp.*;

import java.io.*;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.engine.*;

/** An abstract class that allows a class to apply testing on its own code.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-18 */
public abstract class ReflectiveTester {
  private static final String JAVA_HOME = System.getProperty("java.home");
  private static Map<Class<? extends ReflectiveTester>, CompilationUnit> classToASTCompilationUnit = new LinkedHashMap<>();

  protected final ASTNode myCompilationUnit() {
    final Class<? extends ReflectiveTester> c = getClass();
    final CompilationUnit $ = classToASTCompilationUnit.get(c);
    if ($ != null)
      return $;
    classToASTCompilationUnit.put(c, loadAST((c.getDeclaringClass() == null ? c : c.getDeclaringClass()).getSimpleName() + ".java"));
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
      if ($.getAbsolutePath().endsWith(fileName)) {
        final ASTParser p = Make.COMPILATION_UNIT.parser(makeAST.string($));
        p.setResolveBindings(true);
        p.setUnitName(fileName);
        p.setEnvironment(new String[] { JAVA_HOME + "\\lib\\rt.jar" }, new String[] { getSrcPath($) + "" }, new String[] { "UTF-8" }, true);
        return (CompilationUnit) p.createAST(null);
      }
    return null;
  }

  private static IPath getSrcPath(File ¢) {
    IPath $ = new Path(¢.getAbsolutePath());
    while (!$.isEmpty() && !"src".equals($.lastSegment()))
      $ = $.removeLastSegments(1);
    return $;
  }
}
