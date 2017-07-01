package il.org.spartan.spartanizer.meta;

import static il.org.spartan.spartanizer.java.namespace.Vocabulary.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.utils.*;

/** An abstract class that allows a class to apply testing on its own code. To
 * use, extend it. See examples of current extenders to see how.
 * <p>
 * The main method is {@link #reflectedCompilationUnit()} which returns a handle
 * to the AST of the {@link CompilationUnit} in which the instance was defined.
 * @author Yossi Gil
 * @since 2016-12-18 */
public abstract class MetaFixture {
  private static final String JAVA_HOME = System.getProperty("java.home");
  private static final Map<Class<? extends MetaFixture>, CompilationUnit> classToASTCompilationUnit = new LinkedHashMap<>();
  private static final Map<Class<? extends MetaFixture>, String> classToText = new LinkedHashMap<>();
  protected static final MetaFixture[] fixtures = { new FixtureBlock(), new FixtureEnhancedFor(), //
      new FixturePlainFor(), //
      new FixtureCatchBlock(), //
      new FixtureFinally(), //
      new NamespaceTest(), //
      new definitionTest(), //
      new KnowsTest(null, null, null), //
  };

  /** Returns a string of a list of n's ancestors in the AST. For each ancestor
   * a partial path will appear.
   * @param n AST node
   * @return a string as described */
  public static String ancestry(final ASTNode n) {
    final Int ret = new Int();
    return Stream.of(ancestors.of(n)).map(λ -> "\n\t + " + ret.next() + ": " + Trivia.gist(λ) + "/" + λ.getClass().getSimpleName())
        .reduce((x, y) -> x + y).get();
  }
  /** Looks for all the annotees of annotationName in all the metafixtures in fs
   * and collects information in an array for each of them Each array is in the
   * next format: [<the name of the annotee>, <the annotation value>, < <name of
   * the metafixture class>:<full path of the annotee> >]
   * @param annotationName the wanted annotation
   * @param fs the metafixtures to search
   * @return a collection of arrays as described */
  protected static Collection<Object[]> collect(final String annotationName, final MetaFixture... fs) {
    @knows({ "ts", "shouldKnow", "collect/1", "h/2" }) final Collection<Object[]> ret = an.empty.list();
    for (@knows({ "t", "ts", "$" }) final MetaFixture t : fs)
      if (t != null)
        for (@knows({ "t", "a", "$" }) final SingleMemberAnnotation a : t.singleMemberAnnotations())
          if ((a.getTypeName() + "").equals(annotationName))
            for (@knows({ "t", "a", "s" }) final String s : values(a))
              for (@knows({ "t", "a", "s", "¢" }) final SimpleName ¢ : annotees.of(a))
                ret.add(as.array(¢, s, t.getClass().getSimpleName() + ":" + Environment.of(¢).fullName()));
    return ret;
  }
  /** Returns the path of ¢'s src directory.
   * @param ¢ a File
   * @return the path of its source directory */
  private static IPath getSrcPath(final File ¢) {
    IPath ret = new Path(¢.getAbsolutePath());
    while (!ret.isEmpty() && !"src".equals(ret.lastSegment()))
      ret = ret.removeLastSegments(1);
    return ret;
  }
  /** Finds the wanted file in current directory and creates a cu out of its
   * content
   * @param fileName the name of the wanted file
   * @return the created cu or null if the wanted file wasn't found */
  private static CompilationUnit loadAST(final String fileName) {
    for (final File $ : new FilesGenerator(".java").from("."))
      if ($.getAbsolutePath().endsWith(fileName)) {
        final ASTParser ret = make.COMPILATION_UNIT.parser(makeAST.string($));
        ret.setResolveBindings(true);
        ret.setUnitName(fileName);
        ret.setEnvironment(new String[] { JAVA_HOME + "/lib/rt.jar" }, new String[] { getSrcPath($) + "" }, new String[] { "UTF-8" }, true);
        return (CompilationUnit) ret.createAST(null);
      }
    return null;
  }
  /** Finds the wanted file in current directory and creates a string that
   * contains its content
   * @param fileName the wanted file
   * @return a string of this file's content */
  private static String loadText(final String fileName) {
    for (final File ret : new FilesGenerator(".java").from("."))
      if (ret.getAbsolutePath().endsWith(fileName))
        return makeAST.string(ret);
    return null;
  }
  /** Gets the value of the parameter in the annotation ¢ as an integer
   * @param ¢ the single member annotation
   * @return the value in the parameter, if the value isn't an integer an
   *         exception will be raised */
  public static int value(final SingleMemberAnnotation ¢) {
    return az.throwing.int¢(az.numberLiteral(¢.getValue()).getToken());
  }
  /** Converts an array initializer to the strings corresponding to its
   * expressions
   * @param ¢ array initializer
   * @return a string array as described */
  private static String[] values(final ArrayInitializer ¢) {
    return values(step.expressions(¢));
  }
  /** Converts a collection of expressions to an array of the corresponding
   * strings
   * @param xs a collection of expressions
   * @return a string array with the expressions as strings */
  private static String[] values(final Collection<Expression> xs) {
    return xs.stream().map(λ -> az.stringLiteral(λ).getLiteralValue()).toArray(String[]::new);
  }
  /** Converts an expression to a string array containing the corresponding
   * string
   * @param $ an expression
   * @return a string array as described if $ is a string literal or an array
   *         initializer, otherwise an empty string array */
  private static String[] values(final Expression $) {
    return $ == null ? new String[0] : iz.stringLiteral($) ? values(az.stringLiteral($)) : //
        iz.arrayInitializer($) ? values(az.arrayInitializer($)) : new String[0];
  }
  /** Converts a single member annotation to a string array corresponding to its
   * value
   * @param ¢ a single member annotation
   * @return a string array as described */
  protected static String[] values(final SingleMemberAnnotation ¢) {
    return values(¢.getValue());
  }
  /** Converts a string literal to an array that contains the corresponding
   * string
   * @param ¢ a string literal
   * @return an array as described */
  private static String[] values(final StringLiteral ¢) {
    return as.array(¢.getLiteralValue());
  }
  /** Gets all the annotations from current runtime class's cu
   * @return an iterable of these annotations */
  public Iterable<Annotation> annotations() {
    return descendants.whoseClassIs(Annotation.class).from(reflectedCompilationUnit());
  }
  /** Creates a vocabulary (map) that maps strings to method declarations. The
   * strings are the name of the method and the number of parameters it gets
   * @param cd anonymous class declaration
   * @return a vocabulary as described */
  public Vocabulary asVocabulary(final AnonymousClassDeclaration cd) {
    final String name = name();
    final Vocabulary ret = new Vocabulary();
    for (final BodyDeclaration ¢ : bodyDeclarations(cd)) {
      assert ¢ instanceof MethodDeclaration : fault.specifically("Unexpected " + extract.name(¢), ¢);
      ret.put(name + "::" + mangle((MethodDeclaration) ¢), (MethodDeclaration) ¢);
    }
    return ret;
  }
  /** Finds the first element of __ ¢ in current runtime class's cu
   * @param ¢ the wanted class
   * @return the first element of this __ */
  protected final <N extends ASTNode> N find(final Class<N> ¢) {
    return the.firstOf(descendants.whoseClassIs(¢).from(reflectedCompilationUnit()));
  }
  /** Gets the name of the most outer class of the current one
   * @return returns this name */
  public String name() {
    return extract.name(types(reflectedCompilationUnit()).stream().filter(AbstractTypeDeclaration::isPackageMemberTypeDeclaration).findFirst().get());
  }
  /** If a mapping of this runtime class to a cu exists returns it, otherwise
   * adds a mapping to cu of this class or of its containing class if exists
   * @return the new cu or the existing mapping */
  public final CompilationUnit reflectedCompilationUnit() {
    final Class<? extends MetaFixture> ret = getClass();
    final CompilationUnit $ = classToASTCompilationUnit.get(ret);
    if ($ != null)
      return $;
    classToASTCompilationUnit.put(ret, loadAST((ret.getDeclaringClass() == null ? ret : ret.getDeclaringClass()).getSimpleName() + ".java"));
    return classToASTCompilationUnit.get(ret);
  }
  /** If a mapping of this runtime class to a string exists returns it,
   * otherwise adds a mapping to a string of this class or of its containing
   * class if exists
   * @return the new string or the existing mapping */
  public final String reflectedCompilationUnitText() {
    final Class<? extends MetaFixture> ret = getClass();
    final String $ = classToText.get(ret);
    if ($ != null)
      return $;
    classToText.put(ret, loadText((ret.getDeclaringClass() == null ? ret : ret.getDeclaringClass()).getSimpleName() + ".java"));
    return classToText.get(ret);
  }
  /** Gets all the single member annotations from current runtime class's cu
   * @return an iterable of these annotations */
  public Iterable<SingleMemberAnnotation> singleMemberAnnotations() {
    return descendants.whoseClassIs(SingleMemberAnnotation.class).from(reflectedCompilationUnit());
  }
}
