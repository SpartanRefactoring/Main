package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.ast.step.*;
import static il.org.spartan.refactoring.engine.into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.engine.Collect.*;

@SuppressWarnings({ "javadoc", "static-method" }) public class SearchTest {
  private final SimpleName n = az.simpleName(e("n"));

  @Test public void awful() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "      n n;;\n" + //
        "      Object n() {\n" + //
        "        return n;\n" + //
        "      }\n" + //
        "    }\n" + //
        "    if (n != n) return n;\n" + //
        "    final n n = new n();\n" + //
        "    if (n instanceof n)\n" + //
        "      new Object();\n" + //
        "    n();\n" + //
        "    return n;\n" + //
        "  }")).size(), is(3));
  }

  @Test public void awful1() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    final int n = null; \n" + //
        "    if (n instanceof n)\n" + //
        "      new Object();\n" + //
        "    n();\n" + //
        "    return n;\n" + //
        "  }")).size(), is(0));
  }

  @Test public void awful2() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    if (n instanceof n)\n" + //
        "      new Object();\n" + //
        "    n();\n" + //
        "    return n;\n" + //
        "  }")).size(), is(2));
  }

  @Test public void awful3() {
    azzert.that(searcher().in(d("Object f() {\n" + //
        "    return n;\n" + //
        "  }")).size(), is(1));
  }

  @Test public void awfulA() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "      n n;;\n" + //
        "      Object n(Object n) {\n" + //
        "        return n;\n" + //
        "      }\n" + //
        "    }\n" + //
        "  }")).size(), is(0));
  }

  @Test public void awfulB() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "      n n;;\n" + //
        "    }\n" + //
        "  }")).size(), is(0));
  }

  @Test public void awfulC() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "    }\n" + //
        "  }")).size(), is(0));
  }

  @Test public void awfulClassDestroyingFurtherUses() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "      n n;;\n" + //
        "    }\n" + //
        "    return n; // 1\n" + //
        "  }")).size(), is(1));
  }

  @Test public void awfulD() {
    azzert.that(searcher().in(d("Object a() {\n" + //
        "    class n {\n" + //
        "    }\n" + //
        "  }")).size(), is(0));
  }

  @Test public void awfulE() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "  }")).size(), is(0));
  }

  @Test public void awfulF() {
    azzert.that(searcher().in(d("Object a() {\n" + //
        "  }")).size(), is(0));
  }

  @Test public void awfulNestedClass() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "      n n;;\n" + //
        "      Object n() {\n" + //
        "        return n;\n" + //
        "      }\n" + //
        "    }\n" + //
        "  }")).size(), is(0));
  }

  @Test public void awfulNestedClassVariableAfter() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "      Object n() {\n" + //
        "        return n;\n" + //
        "      }\n" + //
        "      n n;;\n" + //
        "    }\n" + //
        "  }")).size(), is(0));
  }

  @Test public void awfulOriginal() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "      n n;;\n" + //
        "      Object n() {\n" + //
        "        return n;\n" + //
        "      }\n" + //
        "    }\n" + //
        "    final n n = new n();\n" + //
        "    if (n instanceof n)\n" + //
        "      new Object();\n" + //
        "    n();\n" + //
        "    return n;\n" + //
        "  }")).size(), is(0));
  }

  @Test public void awfulShort() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    if (n != n) return n;\n" + //
        "  }")).size(), is(3));
  }

  @Test public void awfulShortWithParameter() {
    azzert.that(searcher().in(d("Object n(int n) {\n" + //
        "    if (n != n) return n;\n" + //
        "  }")).size(), is(0));
  }

  @Test public void awfulVariantWithClass() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "      n n;;\n" + //
        "      Object n() {\n" + //
        "        return n;\n" + //
        "      }\n" + //
        "    }\n" + //
        "    if (n != n) return n;\n" + //
        "  }")).size(), is(3));
  }

  @Test public void awfulVariantWithoutClass() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    if (n != n) return n;\n" + //
        "  }")).size(), is(3));
  }

  @Test public void awfulWithClassAfter() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    if (n != n) return n;\n" + //
        "    final n n = new n();\n" + //
        "    if (n instanceof n)\n" + //
        "      new Object();\n" + //
        "    n();\n" + //
        "    class n {\n" + //
        "      n n;;\n" + //
        "      Object n() {\n" + //
        "        return n;\n" + //
        "      }\n" + //
        "    }\n" + //
        "    return n;\n" + //
        "  }")).size(), is(3));
  }

  @Test public void awfulWithClassAfterNoRedefinition() {
    azzert.that(searcher().in(d("Object n() {\n" + //
        "    if (n != n) return n; // 3\n" + //
        "    if (n instanceof n) // 1\n" + //
        "      new Object();\n" + //
        "    n(); // 0\n " + //
        "    class n {\n" + //
        "      n n;;\n" + //
        "      Object n() {\n" + //
        "        return n;\n" + //
        "      }\n" + //
        "    }\n" + //
        "    return n; // 1\n" + //
        "  }")).size(), is(5));
  }

  @Test public void classInMethod() {
    azzert.that(searcher().in(d("void f() {class n {}}")).size(), is(0));
  }

  @Test public void constructorCall() {
    azzert.that(searcher().in(e("new n(this)\n")).size(), is(0));
  }

  @Test public void declarationVoidsUse() {
    azzert.that(nCount("final A n = n * 2; a = n;"), is(0));
  }

  @Test public void declarationVoidsUseA() {
    azzert.that(nCount("final A n = n * 2;"), is(0));
  }

  @Test public void declarationVoidsUseB() {
    azzert.that(nCount("final A n = 2; a = n;"), is(0));
  }

  @Test public void declarationVoidsUseC() {
    azzert.that(nCount("final A n = 2;"), is(0));
  }

  @Test public void definedUntilEndOfBlock() {
    azzert.that(nCount("a = n; { int n; a = n * n + n;} a = n;"), is(2));
  }

  @Test public void definedUntilEndOfBlockA() {
    azzert.that(nCount("a = n; { int n; a = n;}"), is(1));
  }

  @Test public void delarationAndDoLoopEmptyBody() {
    azzert.that(nCount("int n; do {  } while (b[i] != n);"), is(0));
  }

  @Test public void delarationAndDoLoopInMethod() {
    final String input = "void f() { int b = 3; do ; while(b != 0);  }";
    final MethodDeclaration d = d(input);
    azzert.that(d, iz(input));
    final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(d);
    azzert.notNull(f);
    final SimpleName b = f.getName();
    azzert.that(b, iz("b"));
    azzert.that(Collect.usesOf(b).in(d).size(), is(2));
  }

  @Test public void delarationAndDoLoopInMethodWithoutTheDo() {
    final String input = "void f() { int b = 3;   }";
    final MethodDeclaration d = d(input);
    azzert.that(d, iz(input));
    final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(d);
    azzert.notNull(f);
    final SimpleName b = f.getName();
    azzert.that(b, iz("b"));
    azzert.that(Collect.usesOf(b).in(d).size(), is(1));
  }

  @Test public void doLoopEmptyBody() {
    azzert.that(nCount(" do {  } while (b[i] != n);"), is(1));
  }

  @Test public void doLoopEmptyStatementBody() {
    azzert.that(nCount(" do {  } while (b[i] != n);"), is(1));
  }

  @Test public void doLoopVanilla() {
    azzert.that(nCount(" do { b[i] = 2; i++; } while (b[i] != n);"), is(1));
  }

  @Test public void fieldAccess1() {
    azzert.that(nCount("x = n.a;"), is(1));
  }

  @Test public void fieldAccess2() {
    azzert.that(nCount("x = a.n;"), is(0));
  }

  @Test public void fieldAccessDummy() {
    azzert.that(searcher().in(d("" + //
        "  public int y() {\n" + //
        "    final Z res = new Z(6);\n" + //
        "    S.out.println(res.j);\n" + //
        "    return res;\n" + //
        "  }\n" + //
        "}\n" + //
        "")).size(), is(0));
  }

  @Test public void fieldAccessReal() {
    azzert.that(searcher().in(d("" + //
        "  public int y() {\n" + //
        "    final Z n = new Z(6);\n" + //
        "    S.out.println(n.j);\n" + //
        "    return n;\n" + //
        "  }\n" + //
        "}\n" + //
        "")).size(), is(0));
  }

  @Test public void fieldAccessSimplified() {
    azzert.that(nCount("" + //
        "    S.out.println(n.j);\n" + //
        ""), is(1));
  }

  @Test public void forEnhancedAsParemeter() {
    final Statement s = s("for (int a: as) return a; ");
    final Block b = (Block) s;
    final EnhancedForStatement s2 = (EnhancedForStatement) lisp.first(statements(b));
    final SimpleName a = s2.getParameter().getName();
    azzert.that(a, iz("a"));
    azzert.that(Collect.usesOf(a).in(s).size(), is(2));
  }

  @Test public void forEnhancedAsParemeterInMethod() {
    final MethodDeclaration d = d("int f() { for (int a: as) return a;}");
    final Block b = d.getBody();
    final EnhancedForStatement s = (EnhancedForStatement) b.statements().get(0);
    final SimpleName a = s.getParameter().getName();
    azzert.that(a, iz("a"));
    azzert.that(Collect.usesOf(a).in(d).size(), is(2));
  }

  @Test public void forEnhancedLoop() {
    azzert.that(nCount("for (int n:ns) a= n;"), is(0));
  }

  @Test public void forLoop() {
    azzert.that(nCount("for (int a = n; a < n; a++);"), is(2));
  }

  @Test public void forLoop0() {
    azzert.that(nCount("for (int a = 2; a < 2; a++);"), is(0));
  }

  @Test public void forLoop1() {
    azzert.that(nCount("for (int a = n; a < 2; a++);"), is(1));
  }

  @Test public void forLoop1A() {
    azzert.that(nCount("for (int a = n * n + n; a < 2; a++);"), is(3));
  }

  @Test public void forLoop2() {
    azzert.that(nCount("for (int a = 1; a < n; a=1);"), is(1));
  }

  @Test public void forLoop3() {
    azzert.that(nCount("for (int a = 1; a < 2; a=n);"), is(1));
  }

  @Test public void forLoop4() {
    azzert.that(nCount("for (int a = 1; a < 2; a++) a=n;"), is(1));
  }

  @Test public void forLoop5() {
    azzert.that(nCount("for (int a = 1; a < 2; a++) a=2; a = n;"), is(1));
  }

  @Test public void forLoop6() {
    azzert.that(nCount("int a = n; for (int a = 1; a < 2; a++) a=2; a = 1;"), is(1));
  }

  @Test public void forLoop7() {
    azzert.that(nCount("int a = 2; for (int a = 1; a < 2; a++) { a=2; } a = n;"), is(1));
  }

  @Test public void forLoopEnhanced0() {
    azzert.that(nCount("for (int a: n) return n;"), is(2));
  }

  @Test public void forLoopEnhanced1() {
    azzert.that(nCount("for (int a: x) return n;"), is(1));
  }

  @Test public void forLoopEnhanced2() {
    azzert.that(nCount("for (int a: n) return 1;"), is(1));
  }

  @Test public void forLoopEnhanced3() {
    azzert.that(nCount("for (int a: as) {++n;}"), is(1));
  }

  @Test public void function() {
    azzert.that(nCount("b = n();"), is(0));
  }

  @Test public void instanceof1() {
    azzert.that(nCount("b = n instanceof x;"), is(1));
  }

  @Test public void instanceof2() {
    azzert.that(nCount("b = x instanceof n;"), is(0));
  }

  @Test public void minusMinus() {
    azzert.that(Collect.forAllOccurencesExcludingDefinitions(n).in(s("n--;")).size(), is(0));
  }

  @Test public void minusMinusPre() {
    azzert.that(Collect.forAllOccurencesExcludingDefinitions(n).in(s("--n;")).size(), is(0));
  }

  @Test public void plusPlus() {
    azzert.that(Collect.forAllOccurencesExcludingDefinitions(n).in(s("n++;")).size(), is(0));
  }

  @Test public void plusPlusPre() {
    azzert.that(Collect.forAllOccurencesExcludingDefinitions(n).in(s("++n;")).size(), is(0));
  }

  @Test public void superMethodInocation() {
    azzert.that(searcher().in(e("super.n(this)\n")).size(), is(0));
  }

  @Test public void usedAsType() {
    azzert.that(nCount("n n;"), is(0));
  }

  @Test public void usedAsType1() {
    azzert.that(nCount("n a;"), is(0));
  }

  @Test public void usedAsType2() {
    azzert.that(nCount("final n n = new n(n);"), is(0));
  }

  @Test public void vanilla() {
    final Collector findUses = searcher();
    azzert.notNull(findUses);
    azzert.that(findUses.in(s("b = n;")).size(), is(1));
  }

  @Test public void vanillaShortVersion() {
    azzert.that(nCount("b = n;"), is(1));
  }

  private int nCount(final String statement) {
    return searcher().in(s(statement)).size();
  }

  private Collector searcher() {
    return Collect.usesOf(n);
  }
}
