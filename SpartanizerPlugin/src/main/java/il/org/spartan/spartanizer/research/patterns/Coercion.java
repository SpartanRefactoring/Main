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

/** @author Ori Marcovitch
 * @since 2016 */
public class Coercion extends NanoPatternTipper<CastExpression> {
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
    return az.methodDeclaration(wizard.ast("static " + step.type(¢) + " az" + step.type(¢) + "(Object ¢){ return (" + step.type(¢) + ")¢;}"));
  }

  private static TypeDeclaration containingType(final CastExpression ¢) {
    final String s = AnalyzerOptions.get(Coercion.class.getSimpleName(), "apiLevel");
    if (s == null)
      return null;
    switch (s) {
      case "type":
        return searchAncestors.forContainingType().from(¢);
      case "package":
        return az.typeDeclaration(searchDescendants.forClass(TypeDeclaration.class)
            .from(az.compilationUnit(makeAST.COMPILATION_UNIT.from(prepareFile(new File(getContainingPackage(¢) + ".iz.java"))))).get(0));
      case "file":
        return az.typeDeclaration(searchDescendants.forClass(TypeDeclaration.class)
            .from(az.compilationUnit(makeAST.COMPILATION_UNIT.from(new File(AnalyzerOptions.get(Coercion.class.getSimpleName(), "apiFile")))))
            .get(0));
      default:
        break;
    }
    assert false : "illegal apiLevel [" + s + "]";
    return null;
  }

  private static File prepareFile(final File ¢) {
    return ¢.exists() ? ¢ : createFileFromTemplate(¢);
  }

  private static File createFileFromTemplate(final File f) {
    try {
      f.createNewFile();
    } catch (IOException x1) {
      x1.printStackTrace();
    }
    try (FileWriter w = new FileWriter(f)) {
      w.write(String.valueOf(Files.readAllBytes(Paths.get("/src/main/java/il/org/spartan/spartanizer/research/az.template"))));
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
