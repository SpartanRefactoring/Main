package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.dispatch.Tippers.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "javadoc", "static-method" }) //
public final class TippersTest {
  @Test public void countInEnhancedFor() throws IllegalArgumentException, MalformedTreeException {
    final String input = "int f() { for (int a: as) return a; }";
    final MethodDeclaration m = findFirst.methodDeclaration((makeAST.COMPILATION_UNIT.from(Wrap.Method.intoDocument(input))));
    azzert.that(m, iz(input));
    final SingleVariableDeclaration p = ((EnhancedForStatement) first(statements(m.getBody()))).getParameter();
    assert p != null;
    final SimpleName a = p.getName();
    assert a != null;
    azzert.that(a, iz("a"));
    azzert.that(Collect.usesOf(a).in(m).size(), is(2));
  }

  @Test public void inlineExpressionWithSideEffect() {
    azzert.that(!haz.sideEffects(into.e("f()")), is(false));
    final VariableDeclarationFragment f = findFirst.variableDeclarationFragment(Wrap.Statement.intoCompilationUnit("int a = f(); return a += 2 * a;"));
    azzert.that(f, iz("a=f()"));
    final SimpleName n = f.getName();
    azzert.that(n, iz("a"));
    final Expression initializer = f.getInitializer();
    azzert.that(initializer, iz("f()"));
    azzert.that(!haz.sideEffects(initializer), is(false));
    final ASTNode parent = f.getParent();
    azzert.that(parent, iz("int a = f();"));
    final ASTNode block = parent.getParent();
    azzert.that(block, iz("{int a = f(); return a += 2*a;}"));
    final ReturnStatement returnStatement = (ReturnStatement) ((Block) block).statements().get(1);
    azzert.that(returnStatement, iz("return a += 2 *a;"));
    final Assignment a = (Assignment) returnStatement.getExpression();
    final Operator o = a.getOperator();
    azzert.that(o, iz("+="));
    final InfixExpression alternateInitializer = subject.pair(to(a), from(a)).to(wizard.assign2infix(o));
    azzert.that(alternateInitializer, iz("a + 2 * a"));
    azzert.that(!haz.sideEffects(initializer), is(false));
    azzert.that(Collect.usesOf(n).in(alternateInitializer).size(), is(2));
    azzert.that(new Inliner(n).byValue(initializer).canInlineinto(alternateInitializer), is(false));
  }

  @Test public void mixedLiteralKindEmptyList() {
    azzert.that(mixedLiteralKind(es()), is(false));
  }

  @Test public void mixedLiteralKindnPairList() {
    azzert.that(mixedLiteralKind(es("1", "1.0")), is(false));
  }

  @Test public void mixedLiteralKindnTripleList() {
    azzert.that(mixedLiteralKind(es("1", "1.0", "a")), is(true));
  }

  @Test public void mixedLiteralKindSingletonList() {
    azzert.that(mixedLiteralKind(es("1")), is(false));
  }

  @Test public void renameInEnhancedFor() throws IllegalArgumentException, MalformedTreeException, BadLocationException {
    final String input = "int f() { for (int a: as) return a; }";
    final Document d = Wrap.Method.intoDocument(input);
    final MethodDeclaration m = findFirst.methodDeclaration((makeAST.COMPILATION_UNIT.from(d)));
    azzert.that(m, iz(input));
    final Block b = m.getBody();
    final SingleVariableDeclaration p = ((EnhancedForStatement) first(statements(b))).getParameter();
    assert p != null;
    final SimpleName n = p.getName();
    final ASTRewrite r = ASTRewrite.create(b.getAST());
    Tippers.rename(n, n.getAST().newSimpleName("$"), m, r, null);
    r.rewriteAST(d, null).apply(d);
    final String output = Wrap.Method.off(d.get());
    assert output != null;
    azzert.that(output, iz(" int f() {for(int $:as)return $;}"));
  }

  @Test public void renameintoDoWhile() throws IllegalArgumentException, MalformedTreeException, BadLocationException {
    final String input = "void f() { int b = 3; do ; while(b != 0); }";
    final Document d = Wrap.Method.intoDocument(input);
    final MethodDeclaration m = findFirst.methodDeclaration((makeAST.COMPILATION_UNIT.from(d)));
    azzert.that(m, iz(input));
    final VariableDeclarationFragment f = findFirst.variableDeclarationFragment(m);
    assert f != null;
    final SimpleName b = f.getName();
    azzert.that(Collect.usesOf(b).in(m).size(), is(2));
    final ASTRewrite r = ASTRewrite.create(b.getAST());
    Tippers.rename(b, b.getAST().newSimpleName("c"), m, r, null);
    r.rewriteAST(d, null).apply(d);
    azzert.that(Wrap.Method.off(d.get()), iz("void f() { int c = 3; do ; while(c != 0); }"));
  }
}
