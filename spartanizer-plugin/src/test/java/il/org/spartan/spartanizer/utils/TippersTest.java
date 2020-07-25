package il.org.spartan.spartanizer.utils;

import static fluent.ly.azzert.is;
import static fluent.ly.azzert.iz;
import static il.org.spartan.spartanizer.ast.factory.misc.mixedLiteralKind;
import static il.org.spartan.spartanizer.ast.factory.misc.rename;
import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.from;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.ast.navigate.step.to;
import static il.org.spartan.spartanizer.engine.parse.e;
import static il.org.spartan.spartanizer.engine.parse.es;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Assignment.Operator;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.junit.Test;

import fluent.ly.azzert;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.findFirst;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.engine.Inliner;
import il.org.spartan.spartanizer.engine.collect;
import il.org.spartan.spartanizer.java.sideEffects;

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
    final SingleVariableDeclaration p = ((EnhancedForStatement) the.firstOf(statements(body(m)))).getParameter();
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
    final SingleVariableDeclaration p = ((EnhancedForStatement) the.firstOf(statements(b))).getParameter();
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
