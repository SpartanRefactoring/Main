package il.org.spartan.spartanizer.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static il.org.spartan.spartanizer.wring.dispatch.Wrings.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.spartanizations.*;
import il.org.spartan.spartanizer.wring.dispatch.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class WringsTest {
  @Test public void countInEnhancedFor() throws IllegalArgumentException, MalformedTreeException {
    final String input = "int f() { for (int a: as) return a; }";
    final Document d = Wrap.Method.intoDocument(input);
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(d);
    final MethodDeclaration m = extract.firstMethodDeclaration(u);
    azzert.that(m, iz(input));
    final Block b = m.getBody();
    final EnhancedForStatement s = (EnhancedForStatement) first(statements(b));
    final SingleVariableDeclaration p = s.getParameter();
    assert p != null;
    final SimpleName a = p.getName();
    assert a != null;
    azzert.that(a, iz("a"));
    azzert.that(Collect.usesOf(a).in(m).size(), is(2));
  }

  @Test public void inlineExpressionWithSideEffect() {
    final Expression e = into.e("f()");
    azzert.that(sideEffects.free(e), is(false));
    final String input = "int a = f(); return a += 2 * a;";
    final CompilationUnit u = Wrap.Statement.intoCompilationUnit(input);
    final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(u);
    azzert.that(f, iz("a=f()"));
    final SimpleName n = f.getName();
    azzert.that(n, iz("a"));
    final Expression initializer = f.getInitializer();
    azzert.that(initializer, iz("f()"));
    azzert.that(sideEffects.free(initializer), is(false));
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
    azzert.that(sideEffects.free(initializer), is(false));
    azzert.that(Collect.usesOf(n).in(alternateInitializer).size(), is(2));
    azzert.that(new LocalInliner(n).byValue(initializer).canInlineinto(alternateInitializer), is(false));
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
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(d);
    final MethodDeclaration m = extract.firstMethodDeclaration(u);
    azzert.that(m, iz(input));
    final Block b = m.getBody();
    final EnhancedForStatement s = (EnhancedForStatement) first(statements(b));
    final SingleVariableDeclaration p = s.getParameter();
    assert p != null;
    final SimpleName n = p.getName();
    final ASTRewrite r = ASTRewrite.create(b.getAST());
    Wrings.rename(n, n.getAST().newSimpleName("$"), m, r, null);
    final TextEdit e = r.rewriteAST(d, null);
    e.apply(d);
    final String output = Wrap.Method.off(d.get());
    assert output != null;
    azzert.that(output, iz(" int f() {for(int $:as)return $;}"));
  }

  @Test public void renameintoDoWhile() throws IllegalArgumentException, MalformedTreeException, BadLocationException {
    final String input = "void f() { int b = 3; do ; while(b != 0); }";
    final Document d = Wrap.Method.intoDocument(input);
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(d);
    final MethodDeclaration m = extract.firstMethodDeclaration(u);
    azzert.that(m, iz(input));
    final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(m);
    assert f != null;
    final SimpleName b = f.getName();
    azzert.that(Collect.usesOf(b).in(m).size(), is(2));
    final ASTRewrite r = ASTRewrite.create(b.getAST());
    Wrings.rename(b, b.getAST().newSimpleName("c"), m, r, null);
    final TextEdit e = r.rewriteAST(d, null);
    e.apply(d);
    azzert.that(Wrap.Method.off(d.get()), iz("void f() { int c = 3; do ; while(c != 0); }"));
  }
}
