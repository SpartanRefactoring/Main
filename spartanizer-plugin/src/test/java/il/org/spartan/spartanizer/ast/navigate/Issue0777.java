package il.org.spartan.spartanizer.ast.navigate;

import static fluent.ly.azzert.is;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.factory.misc;
import il.org.spartan.spartanizer.ast.safety.az;

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
