package il.org.spartan.spartanizer.cmdline;

import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Type;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/**
 * Collects metrics at different level of granularity: File, Class, Method
 * TODO Matteo: others? even finer?
 * @author Matteo Orru'
 * @since 2016
 */

public class TypeFeaturesCollector extends FilesASTVisitor {

  int classNesting;
  TypeDeclaration lastNode;
  private final CSVLineWriter writer = new CSVLineWriter(makeFile("class-properties"));

//  @Override public boolean visit(final MethodDeclaration node) {
////    ++methodNesting;
//    lastNode = node;
//    return super.visit(node);
//  }
  
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
//    final Type type = ¢.getReturnType2();
    writer.put("File", presentFile) //
        .put("Name", ¢.getName()) //
//        .put("Constructor", ¢.isConstructor()) //
//        .put("Varargs", ¢.isVarargs()) //
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
//        .put("side-effects", haz.sideEffects(¢)) //
//        .put("linear", !haz.unknownNumberOfEvaluations(¢)) //
//        .put("array", type != null && type.isArrayType()) //
//        .put("parameterized", type != null && type.isParameterizedType()) //
//        .put("primitive", type != null && type.isPrimitiveType()) //
//        .put("simple", type != null && type.isSimpleType()) //
//        .put("qualified", type != null && type.isQualifiedType()) //
//        .put("no-arguments", type != null && ¢.parameters().isEmpty()) //
//        .put("unary", ¢.parameters().size() == 1) //
//        .put("binary", ¢.parameters().size() == 2) //
//        .put("no-exceptions", ¢.thrownExceptionTypes().isEmpty())//
//        .put("one-exception", ¢.thrownExceptionTypes().size() == 1) //
        .put("getter", NameGuess.of(¢.getName() + "") == NameGuess.GETTER_METHOD) //
        .put("setter", NameGuess.of(¢.getName() + "") == NameGuess.SETTTER_METHOD) //
        .put("isMethod", NameGuess.of(¢.getName() + "") == NameGuess.IS_METHOD) //
        .put("method", NameGuess.of(¢.getName() + "") == NameGuess.METHOD_OR_VARIABLE) //
        .put("unknonwn", NameGuess.of(¢.getName() + "") == NameGuess.UNKNOWN) //
        .put("weirdo", NameGuess.of(¢.getName() + "") == NameGuess.WEIRDO) //
    ;
    writer.nl();
  }
//  @Override public void endVisit(final MethodDeclaration node) {
////    --methodNesting;
////    consider(node);
//    super.endVisit(node);
//  }
  
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
}
