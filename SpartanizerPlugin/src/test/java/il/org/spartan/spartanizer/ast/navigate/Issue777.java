package il.org.spartan.spartanizer.ast.navigate;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** Test for analyze.type
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue777 {
  @Test public void a() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
    azzertEquals("class C{void foo(){}}", addMethodToType("class C{}", "void foo(){}"));
  }

  @Test public void b() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
    azzertEquals("/**freaking javadoc\n*/class C{void foo(){}}", addMethodToType("/**freaking javadoc\n*/class C{}", "void foo(){}"));
  }

  private String addMethodToType(String type, String method) throws BadLocationException {
    Document $ = new Document(type);
    TypeDeclaration d = findFirst.typeDeclaration(makeAST.COMPILATION_UNIT.from($));
    ASTRewrite r = ASTRewrite.create(d.getAST());
    MethodDeclaration m = az.methodDeclaration(ast(method));
    wizard.addMethodToType(d, m, r, null);
    r.rewriteAST($, null).apply($);
    return $.get();
  }

  private void azzertEquals(String expected, String actual) {
    assertEquals(expected.replaceAll("[\n\t\r ]", ""), actual.replaceAll("[\n\t\r ]", ""));
  }
}
