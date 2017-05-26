package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** Collects metrics at different level of granularity: File, Class, Method
 * @author Matteo Orru'
 * @since 2016 */
// TODO Matteo: others? even finer?
@SuppressWarnings("rawtypes")
public class TypeFeaturesCollector extends DeprecatedFolderASTVisitor implements FeatureCollector {
  int classNesting;
  TypeDeclaration lastNode;
  private final CSVLineWriter writer = new CSVLineWriter(makeFile("class-properties"));

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
        .put("Condensed size", metrics.condensedSize(¢)) //
        .put("Dexterity", metrics.dexterity(¢)) //
        .put("Leaves", metrics.leaves(¢)) //
        .put("Nodes", metrics.nodes(¢)) //
        .put("Internals", metrics.internals(¢)) //
        .put("Vocabulary", metrics.vocabulary(¢)) //
        .put("Literacy", metrics.literacy(¢)) //
    ;
    writer.nl();
  }
  @Override public void endVisit(final TypeDeclaration node) {
    --classNesting;
    consider(node);
    super.endVisit(node);
  }
  @Override protected void done(final String path) {
    dotter.end();
    System.err.println("Done processing: " + path);
    System.err.println("Your output is in: " + writer.close());
  }

  static {
    clazz = TypeFeaturesCollector.class;
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    DeprecatedFolderASTVisitor.main(args);
  }
  @Override @SuppressWarnings({ "boxing", "unchecked" }) public NamedFunction<ASTNode, Object>[] functions() {
    return as.array(//
        m("length", λ -> (λ + "").length()), //
        m("essence", λ -> Essence.of(λ + "").length()), //
        m("tokens", λ -> metrics.tokens(λ + "")), //
        m("nodes", λ -> countOf.nodes((ASTNode) λ)), //
        m("body", λ -> metrics.bodySize((ASTNode) λ)),
        m("methodDeclaration",
            λ -> az.methodDeclaration((ASTNode) λ) == null ? -1 : extract.statements(az.methodDeclaration((ASTNode) λ).getBody()).size()),
        m("tide", λ -> clean(λ + "").length()), //
        m("abstract", λ -> iz.abstract¢((BodyDeclaration) λ)), //
        m("default", λ -> iz.default¢((BodyDeclaration) λ)), //
        m("final", λ -> iz.final¢((BodyDeclaration) λ)), //
        m("private", λ -> iz.private¢((BodyDeclaration) λ)), //
        m("protected", λ -> iz.protected¢((BodyDeclaration) λ)), //
        m("public", λ -> iz.public¢((BodyDeclaration) λ)), //
        m("static", λ -> iz.static¢((BodyDeclaration) λ)));
  }
  @Override public NamedFunction[] functions(@SuppressWarnings("unused") final String id) {
    // TODO Auto-generated method stub
    return null;
  }
}
