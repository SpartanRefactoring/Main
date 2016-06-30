package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.SpartanAssert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.wring.Wrings.*;
import static org.junit.Assert.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.Operator;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;

@SuppressWarnings({ "javadoc", "static-method" })//
public class WringsTest {
  @Test public void renameIntoDoWhile() throws IllegalArgumentException, MalformedTreeException, BadLocationException {
    final String input = "void f() { int b = 3; do ; while(b != 0); }";
    final Document d = Wrap.Method.intoDocument(input);
    final CompilationUnit u = (CompilationUnit) As.COMPILIATION_UNIT.ast(d);
    final MethodDeclaration m = Extract.firstMethodDeclaration(u);
    assertThat(m, iz(input));
    final VariableDeclarationFragment f = Extract.firstVariableDeclarationFragment(m);
    assertThat(f, notNullValue());
    final SimpleName b = f.getName();
    assertThat(Collect.usesOf(b).in(m).size(), is(2));
    final ASTRewrite r = ASTRewrite.create(b.getAST());
    Wrings.rename(b, newSimpleName(b, "c"), m, r, null);
    final TextEdit e = r.rewriteAST(d, null);
    e.apply(d);
    assertThat(Wrap.Method.off(d.get()), iz("void f() { int c = 3; do ; while(c != 0); }"));
  }
  @Test public void countInEnhancedFor() throws IllegalArgumentException, MalformedTreeException {
    final String input = "int f() { for (int a: as) return a; }";
    final Document d = Wrap.Method.intoDocument(input);
    final CompilationUnit u = (CompilationUnit) As.COMPILIATION_UNIT.ast(d);
    final MethodDeclaration m = Extract.firstMethodDeclaration(u);
    assertThat(m, iz(input));
    final Block b = m.getBody();
    final EnhancedForStatement s = (EnhancedForStatement) b.statements().get(0);
    final SingleVariableDeclaration p = s.getParameter();
    assertThat(p, notNullValue());
    final SimpleName a = p.getName();
    assertThat(a, iz("a"));
    assertThat(Collect.usesOf(a).in(m).size(), is(2));
  }
  @Test public void renameInEnhancedFor() throws IllegalArgumentException, MalformedTreeException, BadLocationException {
    final String input = "int f() { for (int a: as) return a; }";
    final Document d = Wrap.Method.intoDocument(input);
    final CompilationUnit u = (CompilationUnit) As.COMPILIATION_UNIT.ast(d);
    final MethodDeclaration m = Extract.firstMethodDeclaration(u);
    assertThat(m, iz(input));
    final Block b = m.getBody();
    final EnhancedForStatement s = (EnhancedForStatement) b.statements().get(0);
    final SingleVariableDeclaration p = s.getParameter();
    assertThat(p, notNullValue());
    final SimpleName n = p.getName();
    final ASTRewrite r = ASTRewrite.create(b.getAST());
    Wrings.rename(n, newSimpleName(n, "$"), m, r, null);
    final TextEdit e = r.rewriteAST(d, null);
    e.apply(d);
    final String output = Wrap.Method.off(d.get());
    assertThat(output, notNullValue());
    assertThat(output, iz(" int f() {for(int $:as)return $;}"));
  }
  @Test public void inlineExpressionWithSideEffect() {
    final Expression e = Into.e("f()");
    assertThat(Is.sideEffectFree(e), is(false));
    final String input = "int a = f(); return a += 2 * a;";
    final CompilationUnit u = Wrap.Statement.intoCompilationUnit(input);
    final VariableDeclarationFragment f = Extract.firstVariableDeclarationFragment(u);
    assertThat(f, iz("a=f()"));
    final SimpleName n = f.getName();
    assertThat(n, iz("a"));
    final Expression initializer = f.getInitializer();
    assertThat(initializer, iz("f()"));
    assertThat(Is.sideEffectFree(initializer), is(false));
    final ASTNode parent = f.getParent();
    assertThat(parent, iz("int a = f();"));
    final ASTNode block = parent.getParent();
    assertThat(block, iz("{int a = f(); return a += 2*a;}"));
    final ReturnStatement returnStatement = (ReturnStatement) ((Block) block).statements().get(1);
    assertThat(returnStatement, iz("return a += 2 *a;"));
    final Assignment a = (Assignment) returnStatement.getExpression();
    final Operator o = a.getOperator();
    assertThat(o, iz("+="));
    final InfixExpression alternateInitializer = Subject.pair(left(a), right(a)).to(
        Wring.VariableDeclarationFragementAndStatement.asInfix(o));
    assertThat(alternateInitializer, iz("a + 2 * a"));
    assertThat(Is.sideEffectFree(initializer), is(false));
    assertThat(Collect.usesOf(n).in(alternateInitializer).size(), is(2));
    assertThat(new LocalInliner(n).byValue(initializer).canInlineInto(alternateInitializer), is(false));
  }
  @Test public void mixedLiteralKindEmptyList() {
    assertThat(mixedLiteralKind(es()), is(false));
  }
  @Test public void mixedLiteralKindnPairList() {
    assertThat(mixedLiteralKind(es("1", "1.0")), is(false));
  }
  @Test public void mixedLiteralKindnTripleList() {
    assertThat(mixedLiteralKind(es("1", "1.0", "a")), is(true));
  }
  @Test public void mixedLiteralKindSingletonList() {
    assertThat(mixedLiteralKind(es("1")), is(false));
  }
}
