package il.org.spartan.spartanizer.research.nanos.deprecated;

import java.io.*;
import java.nio.file.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.nanos.methods.*;
import il.org.spartan.spartanizer.utils.*;

/** Coercion pattern <br>
 * Whenever we have ((Clazz)obj) turn into az.Clazz(obj) <br>
 * So fluent, so delight. <br>
 * Oh, and create az.Clazz method on the fly.
 * @author Ori Marcovitch
 * @since 2016 */
@Deprecated
@SuppressWarnings("deprecation")
public class Coercion extends NanoPatternTipper<CastExpression> {
  private static final long serialVersionUID = 4421426553044419392L;
  private static final String API_LEVEL_FILE = "file";
  private static final String API_LEVEL_PACKAGE = "package";
  private static final String API_LEVEL_TYPE = "type";
  private static final String API_FILE = "apiFile";
  private static final String API_LEVEL = "apiLevel";
  private static final Down.Caster c = new Down.Caster();

  @Override public boolean interesting(final CastExpression ¢) {
    if (!(step.type(¢) instanceof SimpleType))
      return false;
    final MethodDeclaration $ = yieldAncestors.untilContainingMethod().from(¢);
    final Javadoc j = $.getJavadoc();
    return (j == null || !(j + "").contains(c.tag())) && c.cantTip($) && !(step.type(¢) + "").contains(".");
  }

  @Override public Tip pattern(final CastExpression ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        if (!azMethodExist(¢))
          addAzMethod(¢, r, g);
        r.replace(!iz.parenthesizedExpression(¢.getParent()) ? ¢ : ¢.getParent(), wizard.ast(azMethodName(¢) + "(" + step.expression(¢) + ")"), g);
      }
    };
  }

  static void addAzMethod(final CastExpression ¢, final ASTRewrite r, final TextEditGroup g) {
    final String s = getProperty(API_LEVEL) == null ? API_LEVEL_TYPE : getProperty(API_LEVEL);
    switch (s) {
      case API_LEVEL_TYPE:
        addAzMethodToType(¢, r, g);
        break;
      case API_LEVEL_FILE:
        prepareFile(fileAzFile());
        addAzMethodToFile(¢, fileAzFilePath());
        break;
      case API_LEVEL_PACKAGE:
        prepareFile(packageAzFile(¢));
        addAzMethodToFile(¢, packageAzFilePath(¢));
        break;
      default:
        assert false : "illegal apiLevel [" + s + "]";
        break;
    }
  }

  private static String fileAzFilePath() {
    return getProperty(API_FILE);
  }

  private static File fileAzFile() {
    return new File(fileAzFilePath());
  }

  static boolean azMethodExist(final CastExpression ¢) {
    return step.methods(containingType(¢)).stream()
        .filter(λ -> azMethodName(¢).equals(λ.getName() + "") && typesEqual(step.returnType(λ), step.type(¢))).count() != 0;
  }

  private static boolean typesEqual(final Type returnType, final Type t) {
    return (returnType + "").equals(t + "");
  }

  private static void addAzMethodToType(final CastExpression ¢, final ASTRewrite r, final TextEditGroup g) {
    final AbstractTypeDeclaration t = containingType(¢);
    wizard.addMethodToType(t, az.methodDeclaration(ASTNode.copySubtree(t.getAST(), createAzMethod(¢))), r, g);
  }

  private static void addAzMethodToFile(final CastExpression ¢, final String path) {
    wizard.addMethodToFile(path, az.methodDeclaration(createAzMethod(¢)));
  }

  private static MethodDeclaration createAzMethod(final CastExpression ¢) {
    return az.methodDeclaration(ASTNode.copySubtree(¢.getAST(),
        az.methodDeclaration(wizard.ast(azMethodModifier() + step.type(¢) + " " + azMethodName(¢) + azMethodBody(¢)))));
  }

  private static String azMethodModifier() {
    return "static ";
  }

  private static String azMethodBody(final CastExpression ¢) {
    return "(Object ¢){return (" + step.type(¢) + ")¢;}";
  }

  static String azMethodName(final CastExpression ¢) {
    return (getProperty(API_LEVEL) == null ? API_LEVEL_TYPE : !API_LEVEL_TYPE.equals(getProperty(API_LEVEL)) ? "" : "az")
        + (step.type(¢) + "").replaceAll("//.", "•");
  }

  private static AbstractTypeDeclaration containingType(final CastExpression $) {
    final String s = getProperty(API_LEVEL) == null ? API_LEVEL_TYPE : getProperty(API_LEVEL);
    switch (s) {
      case API_LEVEL_FILE:
        return getType(prepareFile(fileAzFile()));
      case API_LEVEL_PACKAGE:
        return getType(prepareFile(packageAzFile($)));
      case API_LEVEL_TYPE:
        return yieldAncestors.untilContainingType().from($);
      default:
        assert false : "illegal apiLevel [" + s + "]";
        return null;
    }
  }

  private static File packageAzFile(final CastExpression ¢) {
    return new File(packageAzFilePath(¢));
  }

  private static String packageAzFilePath(final CastExpression ¢) {
    return AnalyzerOptions.get(AnalyzerOptions.INPUT_DIR) + "/src/main/java/" + containing.package¢(¢).replaceAll("\\.", File.separator) + "/az.java";
  }

  private static AbstractTypeDeclaration getType(final File x) {
    return az.abstractTypeDeclaration(
        step.types(az.compilationUnit(makeAST.COMPILATION_UNIT.from(x))).stream().filter(λ -> "az".equals(λ.getName() + "")).findFirst().get());
  }

  private static String getProperty(final String property) {
    return AnalyzerOptions.get(Coercion.class.getSimpleName(), property);
  }

  private static File prepareFile(final File ¢) {
    return updatePackage(¢.exists() ? ¢ : createFileFromTemplate(¢));
  }

  private static File updatePackage(final File ¢) {
    return ¢;
  }

  private static File createFileFromTemplate(final File $) {
    // TODO: Marco update package declaration to match actual package...
    try {
      Files.copy(new File(System.getProperty("user.dir") + "/src/main/java/il/org/spartan/spartanizer/research/templates/az.template").toPath(),
          $.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (final IOException ¢) {
      monitor.infoIOException(¢, "/src/main/java/il/org/spartan/spartanizer/research/templates/az.template");
    }
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final CastExpression __) {
    return "replace coercion with az()";
  }
}
