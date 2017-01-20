package il.org.spartan.spartanizer.meta;

import static il.org.spartan.lisp.*;

import java.io.*;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.*;

/** An abstract class that allows a class to apply testing on its own code.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-18 */
public abstract class MetaFixture {
  private static final String JAVA_HOME = System.getProperty("java.home");
  private static final Map<Class<? extends MetaFixture>, CompilationUnit> classToASTCompilationUnit = new LinkedHashMap<>();
  private static final Map<Class<? extends MetaFixture>, String> classToText = new LinkedHashMap<>();

  public final CompilationUnit reflectedCompilationUnit() {
    final Class<? extends MetaFixture> c = getClass();
    final CompilationUnit $ = classToASTCompilationUnit.get(c);
    if ($ != null)
      return $;
    classToASTCompilationUnit.put(c, loadAST((c.getDeclaringClass() == null ? c : c.getDeclaringClass()).getSimpleName() + ".java"));
    return classToASTCompilationUnit.get(c);
  }

  public final String myClassText() {
    final Class<? extends MetaFixture> c = getClass();
    final String $ = classToText.get(c);
    if ($ != null)
      return $;
    classToText.put(c, loadText((c.getDeclaringClass() == null ? c : c.getDeclaringClass()).getSimpleName() + ".java"));
    return classToText.get(c);
  }

  protected final <N extends ASTNode> N find(final Class<N> ¢) {
    return first(searchDescendants.forClass(¢).from(reflectedCompilationUnit()));
  }

  public List<SingleMemberAnnotation> singleMemberAnnotations() {
    return searchDescendants.forClass(SingleMemberAnnotation.class).from(reflectedCompilationUnit());
  }

  public List<Annotation> annotations() {
    return searchDescendants.forClass(Annotation.class).from(reflectedCompilationUnit());
  }

  public static int value(final SingleMemberAnnotation ¢) {
    return az.throwing.int¢(az.numberLiteral(¢.getValue()).getToken());
  }

  public static String ancestry(final ASTNode n) {
    String $ = "";
    int i = 0;
    for (final ASTNode p : ancestors.of(n))
      $ += "\n\t + " + i++ + ": " + trivia.gist(p) + "/" + p.getClass().getSimpleName();
    return $;
  }

  private static CompilationUnit loadAST(final String fileName) {
    for (final File $ : new FilesGenerator(".java").from(".")) // Should be NANO: Find First
      if ($.getAbsolutePath().endsWith(fileName)) {
        final ASTParser p = Make.COMPILATION_UNIT.parser(makeAST.string($));
        p.setResolveBindings(true);
        p.setUnitName(fileName);
        p.setEnvironment(new String[] { JAVA_HOME + "/lib/rt.jar" }, new String[] { getSrcPath($) + "" }, new String[] { "UTF-8" }, true);
        return (CompilationUnit) p.createAST(null);
      }
    return null;
  }

  private static String loadText(final String fileName) {
    for (final File $ : new FilesGenerator(".java").from("."))
      if ($.getAbsolutePath().endsWith(fileName))
        return makeAST.string($);
    return null;
  }

  private static IPath getSrcPath(final File ¢) {
    IPath $ = new Path(¢.getAbsolutePath());
    while (!$.isEmpty() && !"src".equals($.lastSegment()))
      $ = $.removeLastSegments(1);
    return $;
  }

  protected static String[] values(final SingleMemberAnnotation ¢) {
    return values(¢.getValue());
  }

  private static String[] values(final Expression $) {
    return $ == null ? new String[] {} : iz.stringLiteral($) ? values(az.stringLiteral($)) : //
        iz.arrayInitializer($) ? values(az.arrayInitializer($)) : new String[] {};
  }

  private static String[] values(final StringLiteral ¢) {
    return as.array(¢.getLiteralValue());
  }

  private static String[] values(final ArrayInitializer ¢) {
    return values(step.expressions(¢));
  }

  private static String[] values(final List<Expression> xs) {
    return xs.stream().map(¢ -> az.stringLiteral(¢).getLiteralValue()).toArray(String[]::new);
  }

  protected static final MetaFixture[] fixtures = { new FixtureBlock(), new FixtureEnhancedFor(), //
      new FixturePlainFor(), //
      new FixtureCatchBlock(), //
      new FixtureFinally(), //
      new NamespaceTest(), //
      new definitionTest(), //
      new KnowsTest(null, null, null), //
  };

  protected static Collection<Object[]> collect(final String annotationName, final MetaFixture... fs) {
    @knows({ "ts", "shouldKnow", "collect/1", "h/2" }) final List<Object[]> $ = new ArrayList<>();
    for (@knows({ "t", "ts", "$" }) final MetaFixture t : fs)
      if (t != null)
        for (@knows({ "t", "a", "$" }) final SingleMemberAnnotation a : t.singleMemberAnnotations())
          if ((a.getTypeName() + "").equals(annotationName))
            for (@knows({ "t", "a", "s" }) final String s : values(a))
              for (@knows({ "t", "a", "s", "¢" }) final SimpleName ¢ : annotees.of(a))
                $.add(as.array(¢, s, t.getClass().getSimpleName() + ":" + Environment.of(¢).fullName()));
    return $;
  }
}
