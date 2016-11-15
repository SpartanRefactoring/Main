package il.org.spartan.spartanizer.cmdline.collector;

import static il.org.spartan.tide.*;

import java.lang.reflect.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;

/** Collects metrics at different level of granularity: File, Class, Method TODO
 * Matteo: others? even finer?
 * @author Matteo Orru'
 * @since 2016 */
@SuppressWarnings("rawtypes")
public class TypeFeaturesCollector extends FilesASTVisitor implements FeatureCollector {
  int classNesting;
  TypeDeclaration lastNode;
  private final CSVLineWriter writer = new CSVLineWriter(makeFile("class-properties"));

  @Override public boolean visit(final TypeDeclaration ¢) {
    ++classNesting;
    lastNode = ¢;
    return super.visit(¢);
  }

  /** TODO: Matteo: Please add here more boolean metrics such as
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
        .put("getter", NameGuess.of(¢.getName() + "") == NameGuess.GETTER_METHOD) //
        .put("setter", NameGuess.of(¢.getName() + "") == NameGuess.SETTTER_METHOD) //
        .put("isMethod", NameGuess.of(¢.getName() + "") == NameGuess.IS_METHOD) //
        .put("method", NameGuess.of(¢.getName() + "") == NameGuess.METHOD_OR_VARIABLE) //
        .put("unknonwn", NameGuess.of(¢.getName() + "") == NameGuess.UNKNOWN) //
        .put("weirdo", NameGuess.of(¢.getName() + "") == NameGuess.WEIRDO) //
        .put("Non whites", count.nonWhiteCharacters(¢)) //
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

  @Override protected void done() {
    dotter.end();
    System.err.println("Your output is in: " + writer.close());
    super.done();
  }

  static {
    clazz = TypeFeaturesCollector.class;
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FilesASTVisitor.main(args);
  }

  @SuppressWarnings({ "boxing", "unchecked" }) 
  @Override public NamedFunction<ASTNode, Object>[] functions() {
    return as.array( //
        m("length", (¢) -> (¢ + "").length()), //
        m("essence", (¢) -> Essence.of(¢ + "").length()), //
        m("tokens", (¢) -> metrics.tokens(¢ + "")), //
        m("nodes", (¢) -> count.nodes((ASTNode) ¢)), //
        m("body", (¢) -> metrics.bodySize((ASTNode) ¢)), //
        m("methodDeclaration",
            (¢) -> az.methodDeclaration((ASTNode) ¢) == null ? -1 : extract.statements(az.methodDeclaration((ASTNode) ¢).getBody()).size()), //
        m("tide", (¢) -> clean(¢ + "").length()), // , //
        m("abstract", (¢) -> iz.abstract¢((BodyDeclaration) ¢)), //
        m("default", (¢) -> iz.default¢((BodyDeclaration) ¢)), //
        m("final", (¢) -> iz.final¢((BodyDeclaration) ¢)) //
    ); //
  }
}
