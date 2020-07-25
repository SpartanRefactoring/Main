package il.org.spartan.spartanizer.cmdline.collectors;

import static il.org.spartan.tide.clean;

import java.lang.reflect.InvocationTargetException;
import java.util.function.ToIntFunction;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import fluent.ly.as;
import il.org.spartan.CSVLineWriter;
import il.org.spartan.spartanizer.ast.navigate.countOf;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metric;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.cmdline.good.DeprecatedFolderASTVisitor;
import il.org.spartan.spartanizer.engine.nominal.guessName;

/** Collects metrics at different level of granularity: File, Class, Method
 * @author Matteo Orru'
 * @since 2016 */
public class TypeFeaturesCollector extends DeprecatedFolderASTVisitor implements FeatureCollector {
  static {
    clazz = TypeFeaturesCollector.class;
  }
  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    DeprecatedFolderASTVisitor.main(args);
  }
  private final CSVLineWriter writer = new CSVLineWriter(makeFile("class-properties"));

  int classNesting;
  TypeDeclaration lastNode;
  @Override public void endVisit(final TypeDeclaration node) {
    --classNesting;
    consider(node);
    super.endVisit(node);
  }
  public Metric[] functions() {
    return as.array(//
        Metric.named("length").of((ToIntFunction<ASTNode>) λ -> (λ + "").length()), //
        Metric.named("tokens").of((ToIntFunction<ASTNode>) λ -> Metrics.tokens(λ + "")), //
        Metric.named("nodes").of((ToIntFunction<ASTNode>) λ -> countOf.nodes(λ)), //
        Metric.named("body").of((ToIntFunction<ASTNode>) λ -> Metrics.bodySize(λ)),
        Metric.named("methodDeclaration").of((ToIntFunction<ASTNode>) λ -> az.methodDeclaration(λ) == null ? -1 : extract.statements(az.methodDeclaration(λ).getBody()).size()),
        Metric.named("tide").of((ToIntFunction<ASTNode>) λ -> clean(λ + "").length()), //
        Metric.b("abstract", λ -> iz.abstract¢((BodyDeclaration) λ)), //
        Metric.b("default", λ -> iz.default¢((BodyDeclaration) λ)), //
        Metric.b("final", λ -> iz.final¢((BodyDeclaration) λ)), //
        Metric.b("private", λ -> iz.private¢((BodyDeclaration) λ)), //
        Metric.b("protected", λ -> iz.protected¢((BodyDeclaration) λ)), //
        Metric.b("public", λ -> iz.public¢((BodyDeclaration) λ)), //
        Metric.b("static", λ -> iz.static¢((BodyDeclaration) λ)));
  }

  @Override public Metric.Integral[] functions(@SuppressWarnings("unused") final String id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public boolean visit(final TypeDeclaration ¢) {
    ++classNesting;
    lastNode = ¢;
    return super.visit(¢);
  }
  /** TODO Matteo: Please add here more boolean metrics such as
   * {@link #isJohnDoeWithResepctTo1stParameter}, {@ link
   * #isJohnDoeWithResepctTo2ndParameter}, --yg
   * @param ¢ JD */
  private void consider(final TypeDeclaration ¢) {
    dotter.click();
    writer //
        .put("File", presentFile) //
        .put("Name", ¢.getName()) //
        .put("abstract", iz.abstract¢(¢)) //
        .put("default", iz.default¢(¢)) //
        .put("final", iz.final¢(¢)) //
        .put("nested", classNesting > 0) //
        .put("nesting", ¢ == lastNode) //
        .put("private", iz.private¢(¢)) //
        .put("protected", iz.protected¢(¢)) //
        .put("public", iz.public¢(¢)) //
        .put("static", iz.static¢(¢)) //
        .put("synchronized", iz.synchronized¢(¢)) //
        .put("empty", extract.statements(¢).isEmpty()) //
        .put("single", extract.statements(¢).size() == 1) //
        .put("double", extract.statements(¢).size() == 1) //
        .put("getter", guessName.of(¢.getName() + "") == guessName.GETTER_METHOD) //
        .put("setter", guessName.of(¢.getName() + "") == guessName.SETTTER_METHOD) //
        .put("isMethod", guessName.of(¢.getName() + "") == guessName.IS_METHOD) //
        .put("method", guessName.of(¢.getName() + "") == guessName.METHOD_OR_VARIABLE) //
        .put("unknonwn", guessName.of(¢.getName() + "") == guessName.UNKNOWN) //
        .put("weirdo", guessName.of(¢.getName() + "") == guessName.WEIRDO) //
        .put("Non whites", countOf.nonWhiteCharacters(¢)) //
        .put("Condensed size", Metrics.condensedSize(¢)) //
        .put("Dexterity", Metrics.dexterity(¢)) //
        .put("Leaves", Metrics.leaves(¢)) //
        .put("Nodes", Metrics.nodes(¢)) //
        .put("Internals", Metrics.internals(¢)) //
        .put("Vocabulary", Metrics.vocabulary(¢)) //
        .put("Literacy", Metrics.literacy(¢)) //
    ;
    writer.nl();
  }
  @Override protected void done(final String path) {
    dotter.end();
    System.err.println("Done processing: " + path);
    System.err.println("Your output is in: " + writer.close());
  }
}
