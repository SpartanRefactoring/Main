package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.wring.Wrings.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;

import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;

@SuppressWarnings({ "javadoc", "static-method" }) //
public class WringsTest {
  @Test public void renameIntoDoWhile() throws IllegalArgumentException, MalformedTreeException, BadLocationException {
    final String input = "void f() { int b = 3; do ; while(b != 0); }";
    final Document d = Wrap.Method.intoDocument(input);
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from(d);
    final MethodDeclaration m = extract.firstMethodDeclaration(u);
    that(m, iz(input));
    final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(m);
    that(f, notNullValue());
    final SimpleName b = f.getName();
    that(Collect.usesOf(b).in(m).size(), is(2));
    final ASTRewrite r = ASTRewrite.create(b.getAST());
    Wrings.rename(b, newSimpleName(b, "c"), m, r, null);
    final TextEdit e = r.rewriteAST(d, null);
    e.apply(d);
    that(Wrap.Method.off(d.get()), iz("void f() { int c = 3; do ; while(c != 0); }"));
  }
  @Test public void countInEnhancedFor() throws IllegalArgumentException, MalformedTreeException {
    final String input = "int f() { for (int a: as) return a; }";
    final Document d = Wrap.Method.intoDocument(input);
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from(d);
    final MethodDeclaration m = extract.firstMethodDeclaration(u);
    that(m, iz(input));
    final Block b = m.getBody();
    final EnhancedForStatement s = (EnhancedForStatement) b.statements().get(0);
    final SingleVariableDeclaration p = s.getParameter();
    that(p, notNullValue());
    final SimpleName a = p.getName();
    that(a, iz("a"));
    that(Collect.usesOf(a).in(m).size(), is(2));
  }
  @Test public void renameInEnhancedFor() throws IllegalArgumentException, MalformedTreeException, BadLocationException {
    final String input = "int f() { for (int a: as) return a; }";
    final Document d = Wrap.Method.intoDocument(input);
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from(d);
    final MethodDeclaration m = extract.firstMethodDeclaration(u);
    that(m, iz(input));
    final Block b = m.getBody();
    final EnhancedForStatement s = (EnhancedForStatement) b.statements().get(0);
    final SingleVariableDeclaration p = s.getParameter();
    that(p, notNullValue());
    final SimpleName n = p.getName();
    final ASTRewrite r = ASTRewrite.create(b.getAST());
    Wrings.rename(n, newSimpleName(n, "$"), m, r, null);
    final TextEdit e = r.rewriteAST(d, null);
    e.apply(d);
    final String output = Wrap.Method.off(d.get());
    that(output, notNullValue());
    that(output, iz(" int f() {for(int $:as)return $;}"));
  }
  @Test public void inlineExpressionWithSideEffect() {
    final Expression e = Into.e("f()");
    that(Is.sideEffectFree(e), is(false));
    final String input = "int a = f(); return a += 2 * a;";
    final CompilationUnit u = Wrap.Statement.intoCompilationUnit(input);
    final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(u);
    that(f, iz("a=f()"));
    final SimpleName n = f.getName();
    that(n, iz("a"));
    final Expression initializer = f.getInitializer();
    that(initializer, iz("f()"));
    that(Is.sideEffectFree(initializer), is(false));
    final ASTNode parent = f.getParent();
    that(parent, iz("int a = f();"));
    final ASTNode block = parent.getParent();
    that(block, iz("{int a = f(); return a += 2*a;}"));
    final ReturnStatement returnStatement = (ReturnStatement) ((Block) block).statements().get(1);
    that(returnStatement, iz("return a += 2 *a;"));
    final Assignment a = (Assignment) returnStatement.getExpression();
    final Operator o = a.getOperator();
    that(o, iz("+="));
    final InfixExpression alternateInitializer = Subject.pair(left(a), right(a))
        .to(Wring.VariableDeclarationFragementAndStatement.asInfix(o));
    that(alternateInitializer, iz("a + 2 * a"));
    that(Is.sideEffectFree(initializer), is(false));
    that(Collect.usesOf(n).in(alternateInitializer).size(), is(2));
    that(new LocalInliner(n).byValue(initializer).canInlineInto(alternateInitializer), is(false));
  }
  @Test public void mixedLiteralKindEmptyList() {
    that(mixedLiteralKind(es()), is(false));
  }
  @Test public void mixedLiteralKindnPairList() {
    that(mixedLiteralKind(es("1", "1.0")), is(false));
  }
  @Test public void mixedLiteralKindnTripleList() {
    that(mixedLiteralKind(es("1", "1.0", "a")), is(true));
  }
  @Test public void mixedLiteralKindSingletonList() {
    that(mixedLiteralKind(es("1")), is(false));
  }
}
