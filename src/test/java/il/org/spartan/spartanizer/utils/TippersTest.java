package il.org.spartan.spartanizer.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.ast.factory.misc.*;
import static il.org.spartan.spartanizer.engine.into.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;

/** TODO Yossi Gil Document Class
 * @author Yossi Gil
 * @since Oct 2, 2016 */
@SuppressWarnings({ "javadoc", "static-method" }) //
public final class TippersTest {
  @Test public void countInEnhancedFor() {
    final String input = "int f() { for (int a: as) return a; }";
    final MethodDeclaration m = findFirst.instanceOf(MethodDeclaration.class)
        .in(makeAST.COMPILATION_UNIT.from(WrapIntoComilationUnit.Method.intoDocument(input)));
    azzert.that(m, iz(input));
    final SingleVariableDeclaration p = ((EnhancedForStatement) the.headOf(statements(body(m)))).getParameter();
    assert p != null;
    final SimpleName a = p.getName();
    assert a != null;
    azzert.that(a, iz("a"));
    azzert.that(collect.usesOf(a).in(m).size(), is(2));
  }

  @Test public void inlineExpressionWithSideEffect() {
    assert !sideEffects.free(e("f()"));
    final VariableDeclarationFragment f = findFirst
        .variableDeclarationFragment(WrapIntoComilationUnit.Statement.intoCompilationUnit("int a = f(); return a += 2 * a;"));
    azzert.that(f, iz("a=f()"));
    final SimpleName n = f.getName();
    azzert.that(n, iz("a"));
    final Expression initializer = f.getInitializer();
    azzert.that(initializer, iz("f()"));
    assert !sideEffects.free(initializer);
    final ASTNode parent = f.getParent();
    azzert.that(parent, iz("int a = f();"));
    final ASTNode block = parent.getParent();
    azzert.that(block, iz("{int a = f(); return a += 2*a;}"));
    final ReturnStatement returnStatement = (ReturnStatement) statements((Block) block).get(1);
    azzert.that(returnStatement, iz("return a += 2 *a;"));
    final Assignment a = (Assignment) returnStatement.getExpression();
    final Operator o = a.getOperator();
    azzert.that(o, iz("+="));
    final InfixExpression alternateInitializer = subject.pair(to(a), from(a)).to(op.assign2infix(o));
    azzert.that(alternateInitializer, iz("a + 2 * a"));
    assert !sideEffects.free(initializer);
    azzert.that(collect.usesOf(n).in(alternateInitializer).size(), is(2));
    assert !new Inliner(n).byValue(initializer).canInlineinto(alternateInitializer);
  }

  @Test public void mixedLiteralKindEmptyList() {
    assert !mixedLiteralKind(es());
  }

  @Test public void mixedLiteralKindnPairList() {
    assert !mixedLiteralKind(es("1", "1.0"));
  }

  @Test public void mixedLiteralKindnTripleList() {
    assert mixedLiteralKind(es("1", "1.0", "a"));
  }

  @Test public void mixedLiteralKindSingletonList() {
    assert !mixedLiteralKind(es("1"));
  }

  @Test public void renameInEnhancedFor() throws Exception {
    final String input = "int f() { for (int a: as) return a; }";
    final Document d = WrapIntoComilationUnit.Method.intoDocument(input);
    final MethodDeclaration m = findFirst.instanceOf(MethodDeclaration.class).in(makeAST.COMPILATION_UNIT.from(d));
    azzert.that(m, iz(input));
    final Block b = body(m);
    final SingleVariableDeclaration p = ((EnhancedForStatement) the.headOf(statements(b))).getParameter();
    assert p != null;
    final SimpleName n = p.getName();
    final ASTRewrite r = ASTRewrite.create(b.getAST());
    rename(n, n.getAST().newSimpleName("$"), m, r, null);
    r.rewriteAST(d, null).apply(d);
    final String output = WrapIntoComilationUnit.Method.off(d.get());
    assert output != null;
    azzert.that(output, iz(" int f() {for(int $:as)return $;}"));
  }

  @Test public void renameintoDoWhile() throws Exception {
    final String input = "void f() { int b = 3; do ; while(b != 0); }";
    final Document d = WrapIntoComilationUnit.Method.intoDocument(input);
    final MethodDeclaration m = findFirst.instanceOf(MethodDeclaration.class).in(makeAST.COMPILATION_UNIT.from(d));
    azzert.that(m, iz(input));
    final VariableDeclarationFragment f = findFirst.variableDeclarationFragment(m);
    assert f != null;
    final SimpleName b = f.getName();
    azzert.that(collect.usesOf(b).in(m).size(), is(2));
    final ASTRewrite r = ASTRewrite.create(b.getAST());
    rename(b, b.getAST().newSimpleName("c"), m, r, null);
    r.rewriteAST(d, null).apply(d);
    azzert.that(WrapIntoComilationUnit.Method.off(d.get()), iz("void f() { int c = 3; do ; while(c != 0); }"));
  }
}
