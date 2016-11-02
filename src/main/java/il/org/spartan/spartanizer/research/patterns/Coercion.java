package il.org.spartan.spartanizer.research.patterns;

import java.io.*;
import java.nio.file.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** Coercion pattern <br>
 * Whenever we have ((Clazz)obj) turn into az.Clazz(obj) <br>
 * So fluent, so delight.
 * @author Ori Marcovitch
 * @since 2016 */
public class Coercion extends NanoPatternTipper<CastExpression> {
  private static final String API_FILE = "apiFile";
  private static final String API_LEVEL = "apiLevel";
  static final Converter c = new Converter();

  @Override public boolean canTip(final CastExpression ¢) {
    if (!(step.type(¢) instanceof SimpleType))
      return false;
    final MethodDeclaration m = searchAncestors.forContainingMethod().from(¢);
    final Javadoc j = m.getJavadoc();
    return (j == null || !(j + "").contains(c.javadoc())) && c.cantTip(m);
  }

  @Override public Tip tip(final CastExpression ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(!iz.parenthesizedExpression(¢.getParent()) ? ¢ : ¢.getParent(), wizard.ast("az" + step.type(¢) + "(" + step.expression(¢) + ")"),
            g);
        if (!azMethodExist(¢))
          addAzMethod(¢, r, g);
        Logger.logNP(¢, getClass().getSimpleName());
      }
    };
  }

  static boolean azMethodExist(final CastExpression ¢) {
    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    System.out.println(containingType(¢));
    step.methods(containingType(¢));
    step.methods(containingType(¢)).stream()
        .filter(m -> ("az" + step.type(¢)).equals(m.getName() + "") && typesEqual(step.returnType(m), step.type(¢)));
    return step.methods(containingType(¢)).stream()
        .filter(m -> ("az" + step.type(¢)).equals(m.getName() + "") && typesEqual(step.returnType(m), step.type(¢))).count() != 0;
  }

  private static boolean typesEqual(final Type returnType, final Type t) {
    return (returnType + "").equals(t + "");
  }

  static void addAzMethod(final CastExpression ¢, final ASTRewrite r, final TextEditGroup g) {
    wizard.addMethodToType(containingType(¢), createAzMethod(¢), r, g);
  }

  private static MethodDeclaration createAzMethod(final CastExpression ¢) {
    return az.methodDeclaration(wizard.ast(azMethodModifier() + step.type(¢) + " " + azMethodName(¢) + azMethodBody(¢)));
  }

  private static String azMethodModifier() {
    return "static ";
  }

  private static String azMethodBody(final CastExpression ¢) {
    return "(Object ¢){ return (" + step.type(¢) + ")¢;}";
  }

  private static String azMethodName(final CastExpression ¢) {
    return (getProperty(API_LEVEL) == null ? "type" : !"type".equals(getProperty(API_LEVEL)) ? "" : "az") + step.type(¢);
  }

  private static AbstractTypeDeclaration containingType(final CastExpression ¢) {
    String s = getProperty(API_LEVEL) == null ? "type" : getProperty(API_LEVEL);
    switch (s) {
      case "type":
        return searchAncestors.forContainingType().from(¢);
      case "package":
        System.out.println(AnalyzerOptions.getMain("inputDir") + "/src/main/java/" + getContainingPackage(¢).replaceAll("\\.", "/") + "/az.java");
        System.out.println(getType(prepareFile(
            new File(AnalyzerOptions.getMain("inputDir") + "/src/main/java/" + getContainingPackage(¢).replaceAll("\\.", "/") + "/az.java"))));
        return getType(prepareFile(
            new File(AnalyzerOptions.getMain("inputDir") + "/src/main/java/" + getContainingPackage(¢).replaceAll("\\.", "/") + "/az.java")));
      case "file":
        return getType(new File(getProperty(API_FILE)));
      default:
        break;
    }
    assert false : "illegal apiLevel [" + s + "]";
    return null;
  }

  private static AbstractTypeDeclaration getType(File x) {
    return az.abstractTypeDeclaration(
        step.types(az.compilationUnit(makeAST.COMPILATION_UNIT.from(x))).stream().filter(t -> "az".equals((t.getName() + ""))).findFirst().get());
  }

  private static String getProperty(String property) {
    return AnalyzerOptions.get(Coercion.class.getSimpleName(), property);
  }

  private static File prepareFile(final File ¢) {
    return ¢.exists() ? ¢ : createFileFromTemplate(¢);
  }

  private static File createFileFromTemplate(final File f) {
    try {
      Files.copy(new File(System.getProperty("user.dir") + "/src/main/java/il/org/spartan/spartanizer/research/templates/az.template").toPath(),
          f.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException x) {
      x.printStackTrace();
    }
    return f;
  }

  /** @param ¢
   * @return */
  private static String getContainingPackage(final CastExpression ¢) {
    return searchAncestors.forContainingCompilationUnit().from(¢).getPackage().getName() + "";
  }

  @Override public String description(@SuppressWarnings("unused") final CastExpression __) {
    return "replace coercion with az()";
  }
}
