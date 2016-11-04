package il.org.spartan.spartanizer.cmdline;

import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Collects boolean features of methods 
 * @author Yossi Gil
 * @year 2016 */
public class MethodFeaturesCollector extends FilesASTVisitor {
  int methodNesting;
  MethodDeclaration lastNode;
  private CSVLineWriter writer;

  @Override public boolean visit(MethodDeclaration node) {
    ++methodNesting;
    lastNode = node;
    return super.visit(node);
  }

  private void consider(MethodDeclaration ¢) {
    dotter.click();
    writer.put("File", presentFile) //
        .put("Name", ¢.getName()) //
        .put("Constructor", ¢.isConstructor()) //
        .put("Varargs", ¢.isVarargs()) //
        .put("abstract", iz.abstract¢(¢)) //
        .put("default", iz.default¢(¢)) //
        .put("final", iz.final¢(¢)) //
        .put("nested", methodNesting > 0) //
        .put("nesting", ¢ == lastNode) //
        .put("private", iz.private¢(¢)) //
        .put("protected", iz.protected¢(¢)) //
        .put("public", iz.public¢(¢)) //
        .put("static", iz.static¢(¢)) //
        .put("synchronized", iz.synchronized¢(¢)) //
        .put("empty", extract.statements(¢).isEmpty()) //
        .put("single", extract.statements(¢).size() == 1) //
        .put("double", extract.statements(¢).size() == 1) //
        .put("side-effects", haz.sideEffects(¢)) //
        .put("linear", !haz.unknownNumberOfEvaluations(¢)) //
    ;
    writer.nl();
  }

  @Override public void endVisit(MethodDeclaration node) {
    --methodNesting;
    consider(node);
    super.endVisit(node);
  }

  @Override protected void done() {
    System.err.println("Your output is in: " +writer.close());
    super.done();
  }

  @Override protected void init() {
    super.init();
    writer = new CSVLineWriter(makeFile("method-properties"));
  }

  public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    clazz = MethodFeaturesCollector.class;
    FilesASTVisitor.main(args);
  }
}
