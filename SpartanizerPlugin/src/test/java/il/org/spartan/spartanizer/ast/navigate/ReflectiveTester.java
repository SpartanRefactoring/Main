package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.lisp.*;

import java.io.*;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** An abstract class that allows a class to apply testing on its own code.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-18 */
public abstract class ReflectiveTester {
  private static final String JAVA_HOME = System.getProperty("java.home");
  private static Map<Class<? extends ReflectiveTester>, CompilationUnit> classToASTCompilationUnit = new LinkedHashMap<>();

  public final ASTNode myCompilationUnit() {
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

  public List<SingleMemberAnnotation> singleMemberAnnotations() {
    return searchDescendants.forClass(SingleMemberAnnotation.class).from(myCompilationUnit());
  }

  public List<Annotation> annotations() {
    return searchDescendants.forClass(Annotation.class).from(myCompilationUnit());
  }

  public static int value(final SingleMemberAnnotation ¢) {
    return az.throwing.int¢(az.numberLiteral(¢.getValue()).getToken());
  }

  public static String ancestry(final ASTNode n) {
    String $ = "";
    int i = 0;
    for (final ASTNode p : ancestors.of(n))
      $ += "\n\t + " + i++ + ": " + wizard.trim(p + "") + "/" + p.getClass().getSimpleName();
    return $;
  }

  private static CompilationUnit loadAST(final String fileName) {
    for (final File $ : new FilesGenerator(".java").from("."))
      if ($.getAbsolutePath().endsWith(fileName)) {
        final ASTParser p = Make.COMPILATION_UNIT.parser(makeAST.string($));
        p.setResolveBindings(true);
        p.setUnitName(fileName);
        p.setEnvironment(new String[] { JAVA_HOME + "/lib/rt.jar" }, new String[] { getSrcPath($) + "" }, new String[] { "UTF-8" }, true);
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

  protected static String[] values(final SingleMemberAnnotation ¢) {
    return values(¢.getValue());
  }

  private static String[] values(final Expression $) {
    return $ == null ? new String[] {}
        : iz.stringLiteral($) ? values(az.stringLiteral($)) : //
          iz.arrayInitializer($) ? values(az.arrayInitializer($)) : new String[] {};
  }

  private static String[] values(final StringLiteral ¢) {
    return as.array(¢.getLiteralValue());
  }

  private static String[] values(final ArrayInitializer ¢) {
    return values(step.expressions(¢));
  }

  private static String[] values(final List<Expression> xs) {
    return xs.stream().map(¢ -> az.stringLiteral(¢).getLiteralValue()).toArray(n -> new String[n]);
  }
}
