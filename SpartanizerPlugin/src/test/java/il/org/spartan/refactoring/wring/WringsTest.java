package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.expose.*;
import static il.org.spartan.refactoring.wring.Wrings.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;

@SuppressWarnings({ "javadoc", "static-method" }) public class WringsTest {
  @Test public void countInEnhancedFor() throws IllegalArgumentException, MalformedTreeException {
    final String input = "int f() { for (int a: as) return a; }";
    final Document d = Wrap.Method.intoDocument(input);
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(d);
    final MethodDeclaration m = extract.firstMethodDeclaration(u);
    azzert.that(m, iz(input));
    final Block b = m.getBody();
    final EnhancedForStatement s = (EnhancedForStatement) first(statements(b));
    final SingleVariableDeclaration p = s.getParameter();
    azzert.notNull(p);
    final SimpleName a = p.getName();
    azzert.that(a, iz("a"));
    azzert.that(Collect.usesOf(a).in(m).size(), is(2));
  }

  @Test public void inlineExpressionWithSideEffect() {
    final Expression e = Into.e("f()");
    azzert.that(Is.sideEffectFree(e), is(false));
    final String input = "int a = f(); return a += 2 * a;";
    final CompilationUnit u = Wrap.Statement.intoCompilationUnit(input);
    final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(u);
    azzert.that(f, iz("a=f()"));
    final SimpleName n = f.getName();
    azzert.that(n, iz("a"));
    final Expression initializer = f.getInitializer();
    azzert.that(initializer, iz("f()"));
    azzert.that(Is.sideEffectFree(initializer), is(false));
    final ASTNode parent = f.getParent();
    azzert.that(parent, iz("int a = f();"));
    final ASTNode block = parent.getParent();
    azzert.that(block, iz("{int a = f(); return a += 2*a;}"));
    final ReturnStatement returnStatement = (ReturnStatement) ((Block) block).statements().get(1);
    azzert.that(returnStatement, iz("return a += 2 *a;"));
    final Assignment a = (Assignment) returnStatement.getExpression();
    final Operator o = a.getOperator();
    azzert.that(o, iz("+="));
    final InfixExpression alternateInitializer = subject.pair(left(a), right(a)).to(Wring.VariableDeclarationFragementAndStatement.asInfix(o));
    azzert.that(alternateInitializer, iz("a + 2 * a"));
    azzert.that(Is.sideEffectFree(initializer), is(false));
    azzert.that(Collect.usesOf(n).in(alternateInitializer).size(), is(2));
    azzert.that(new LocalInliner(n).byValue(initializer).canInlineInto(alternateInitializer), is(false));
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
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(d);
    final MethodDeclaration m = extract.firstMethodDeclaration(u);
    azzert.that(m, iz(input));
    final Block b = m.getBody();
    final EnhancedForStatement s = (EnhancedForStatement) first(statements(b));
    final SingleVariableDeclaration p = s.getParameter();
    azzert.notNull(p);
    final SimpleName n = p.getName();
    final ASTRewrite r = ASTRewrite.create(b.getAST());
    Wrings.rename(n, n.getAST().newSimpleName("$"), m, r, null);
    final TextEdit e = r.rewriteAST(d, null);
    e.apply(d);
    final String output = Wrap.Method.off(d.get());
    azzert.notNull(output);
    azzert.that(output, iz(" int f() {for(int $:as)return $;}"));
  }

  @Test public void renameIntoDoWhile() throws IllegalArgumentException, MalformedTreeException, BadLocationException {
    final String input = "void f() { int b = 3; do ; while(b != 0); }";
    final Document d = Wrap.Method.intoDocument(input);
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(d);
    final MethodDeclaration m = extract.firstMethodDeclaration(u);
    azzert.that(m, iz(input));
    final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(m);
    azzert.notNull(f);
    final SimpleName b = f.getName();
    azzert.that(Collect.usesOf(b).in(m).size(), is(2));
    final ASTRewrite r = ASTRewrite.create(b.getAST());
    Wrings.rename(b, b.getAST().newSimpleName("c"), m, r, null);
    final TextEdit e = r.rewriteAST(d, null);
    e.apply(d);
    azzert.that(Wrap.Method.off(d.get()), iz("void f() { int c = 3; do ; while(c != 0); }"));
  }
}
