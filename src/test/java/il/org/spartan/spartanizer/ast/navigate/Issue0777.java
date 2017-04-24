package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Test for analyze.type
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
 //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0777 {
  @Test public void a() throws Exception {
    azzertEquals("class C{void foo(){}}", addMethodToType("class C{}", "void foo(){}"));
  }

  private String addMethodToType(final String type, final String method) throws BadLocationException {
    final IDocument $ = new Document(type);
    final TypeDeclaration d = findFirst.typeDeclaration(makeAST.COMPILATION_UNIT.from($));
    final ASTRewrite r = ASTRewrite.create(d.getAST());
    misc.addMethodToType(d, az.methodDeclaration(make.ast(method)), r, null);
    r.rewriteAST($, null).apply($);
    return $.get();
  }

  private void azzertEquals(final String expected, final String actual) {
    azzert.that(actual.replaceAll("[\n\t\r ]", ""), is(expected.replaceAll("[\n\t\r ]", "")));
  }

  @Test public void b() throws MalformedTreeException, IllegalArgumentException, BadLocationException {
    azzertEquals("/**freaking javadoc\n*/class C{void foo(){}}", addMethodToType("/**freaking javadoc\n*/class C{}", "void foo(){}"));
  }
}
