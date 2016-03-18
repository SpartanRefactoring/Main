package il.org.spartan.refactoring.utils;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.hamcrest.MatcherAssert.iz;
import static il.org.spartan.refactoring.utils.Funcs.asSimpleName;
import static il.org.spartan.refactoring.utils.Into.d;
import static il.org.spartan.refactoring.utils.Into.e;
import static il.org.spartan.refactoring.utils.Into.s;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import org.eclipse.jdt.core.dom.*;
import org.junit.Test;

import il.org.spartan.refactoring.utils.Collect;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Collect.Collector;

@SuppressWarnings({ "javadoc", "static-method" }) public class SearchTest {
  private final SimpleName n = asSimpleName(e("n"));
  @Test public void awful() {
    assertThat(searcher().in(d("Object n() {\n" + //
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
    assertThat(searcher().in(d("Object n() {\n" + //
        "    final int n = null; \n" + //
        "    if (n instanceof n)\n" + //
        "      new Object();\n" + //
        "    n();\n" + //
        "    return n;\n" + //
        "  }")).size(), is(0));
  }
  @Test public void awful2() {
    assertThat(searcher().in(d("Object n() {\n" + //
        "    if (n instanceof n)\n" + //
        "      new Object();\n" + //
        "    n();\n" + //
        "    return n;\n" + //
        "  }")).size(), is(2));
  }
  @Test public void awful3() {
    assertThat(searcher().in(d("Object f() {\n" + //
        "    return n;\n" + //
        "  }")).size(), is(1));
  }
  @Test public void awfulA() {
    assertThat(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "      n n;;\n" + //
        "      Object n(Object n) {\n" + //
        "        return n;\n" + //
        "      }\n" + //
        "    }\n" + //
        "  }")).size(), is(0));
  }
  @Test public void awfulB() {
    assertThat(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "      n n;;\n" + //
        "    }\n" + //
        "  }")).size(), is(0));
  }
  @Test public void awfulC() {
    assertThat(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "    }\n" + //
        "  }")).size(), is(0));
  }
  @Test public void awfulClassDestroyingFurtherUses() {
    assertThat(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "      n n;;\n" + //
        "    }\n" + //
        "    return n; // 1\n" + //
        "  }")).size(), is(1));
  }
  @Test public void awfulD() {
    assertThat(searcher().in(d("Object a() {\n" + //
        "    class n {\n" + //
        "    }\n" + //
        "  }")).size(), is(0));
  }
  @Test public void awfulE() {
    assertThat(searcher().in(d("Object n() {\n" + //
        "  }")).size(), is(0));
  }
  @Test public void awfulF() {
    assertThat(searcher().in(d("Object a() {\n" + //
        "  }")).size(), is(0));
  }
  @Test public void awfulNestedClass() {
    assertThat(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "      n n;;\n" + //
        "      Object n() {\n" + //
        "        return n;\n" + //
        "      }\n" + //
        "    }\n" + //
        "  }")).size(), is(0));
  }
  @Test public void awfulNestedClassVariableAfter() {
    assertThat(searcher().in(d("Object n() {\n" + //
        "    class n {\n" + //
        "      Object n() {\n" + //
        "        return n;\n" + //
        "      }\n" + //
        "      n n;;\n" + //
        "    }\n" + //
        "  }")).size(), is(0));
  }
  @Test public void awfulOriginal() {
    assertThat(searcher().in(d("Object n() {\n" + //
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
    assertThat(searcher().in(d("Object n() {\n" + //
        "    if (n != n) return n;\n" + //
        "  }")).size(), is(3));
  }
  @Test public void awfulShortWithParameter() {
    assertThat(searcher().in(d("Object n(int n) {\n" + //
        "    if (n != n) return n;\n" + //
        "  }")).size(), is(0));
  }
  @Test public void awfulVariantWithClass() {
    assertThat(searcher().in(d("Object n() {\n" + //
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
    assertThat(searcher().in(d("Object n() {\n" + //
        "    if (n != n) return n;\n" + //
        "  }")).size(), is(3));
  }
  @Test public void awfulWithClassAfter() {
    assertThat(searcher().in(d("Object n() {\n" + //
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
    assertThat(searcher().in(d("Object n() {\n" + //
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
    assertThat(searcher().in(d("void f() {class n {}}")).size(), is(0));
  }
  @Test public void constructorCall() {
    assertThat(searcher().in(e("new n(this)\n")).size(), is(0));
  }
  @Test public void declarationVoidsUse() {
    assertThat(nCount("final A n = n * 2; a = n;"), is(0));
  }
  @Test public void declarationVoidsUseA() {
    assertThat(nCount("final A n = n * 2;"), is(0));
  }
  @Test public void declarationVoidsUseB() {
    assertThat(nCount("final A n = 2; a = n;"), is(0));
  }
  @Test public void declarationVoidsUseC() {
    assertThat(nCount("final A n = 2;"), is(0));
  }
  @Test public void definedUntilEndOfBlock() {
    assertThat(nCount("a = n; { int n; a = n * n + n;} a = n;"), is(2));
  }
  @Test public void definedUntilEndOfBlockA() {
    assertThat(nCount("a = n; { int n; a = n;}"), is(1));
  }
  @Test public void delarationAndDoLoopEmptyBody() {
    assertThat(nCount("int n; do {  } while (b[i] != n);"), is(0));
  }
  @Test public void delarationAndDoLoopInMethod() {
    final String input = "void f() { int b = 3; do ; while(b != 0);  }";
    final MethodDeclaration d = d(input);
    assertThat(d, iz(input));
    final VariableDeclarationFragment f = Extract.firstVariableDeclarationFragment(d);
    assertNotNull(f);
    final SimpleName b = f.getName();
    assertThat(b, iz("b"));
    assertThat(Collect.usesOf(b).in(d).size(), is(2));
  }
  @Test public void delarationAndDoLoopInMethodWithoutTheDo() {
    final String input = "void f() { int b = 3;   }";
    final MethodDeclaration d = d(input);
    assertThat(d, iz(input));
    final VariableDeclarationFragment f = Extract.firstVariableDeclarationFragment(d);
    assertNotNull(f);
    final SimpleName b = f.getName();
    assertThat(b, iz("b"));
    assertThat(Collect.usesOf(b).in(d).size(), is(1));
  }
  @Test public void doLoopEmptyBody() {
    assertThat(nCount(" do {  } while (b[i] != n);"), is(1));
  }
  @Test public void doLoopEmptyStatementBody() {
    assertThat(nCount(" do {  } while (b[i] != n);"), is(1));
  }
  @Test public void doLoopVanilla() {
    assertThat(nCount(" do { b[i] = 2; i++; } while (b[i] != n);"), is(1));
  }
  @Test public void fieldAccess1() {
    assertThat(nCount("x = n.a;"), is(1));
  }
  @Test public void fieldAccess2() {
    assertThat(nCount("x = a.n;"), is(0));
  }
  @Test public void fieldAccessDummy() {
    assertThat(searcher().in(d("" + //
        "  public int y() {\n" + //
        "    final Z res = new Z(6);\n" + //
        "    S.out.println(res.j);\n" + //
        "    return res;\n" + //
        "  }\n" + //
        "}\n" + //
        "")).size(), is(0));
  }
  @Test public void fieldAccessReal() {
    assertThat(searcher().in(d("" + //
        "  public int y() {\n" + //
        "    final Z n = new Z(6);\n" + //
        "    S.out.println(n.j);\n" + //
        "    return n;\n" + //
        "  }\n" + //
        "}\n" + //
        "")).size(), is(0));
  }
  @Test public void fieldAccessSimplified() {
    assertThat(nCount("" + //
        "    S.out.println(n.j);\n" + //
        ""), is(1));
  }
  @Test public void forEnhancedAsParemeter() {
    final Statement s = s("for (int a: as) return a; ");
    final Block b = (Block) s;
    final EnhancedForStatement s2 = (EnhancedForStatement) b.statements().get(0);
    final SimpleName a = s2.getParameter().getName();
    assertThat(a, iz("a"));
    assertThat(Collect.usesOf(a).in(s).size(), is(2));
  }
  @Test public void forEnhancedAsParemeterInMethod() {
    final MethodDeclaration d = d("int f() { for (int a: as) return a;}");
    final Block b = d.getBody();
    final EnhancedForStatement s = (EnhancedForStatement) b.statements().get(0);
    final SimpleName a = s.getParameter().getName();
    assertThat(a, iz("a"));
    assertThat(Collect.usesOf(a).in(d).size(), is(2));
  }
  @Test public void forEnhancedLoop() {
    assertThat(nCount("for (int n:ns) a= n;"), is(0));
  }
  @Test public void forLoop() {
    assertThat(nCount("for (int a = n; a < n; a++);"), is(2));
  }
  @Test public void forLoop0() {
    assertThat(nCount("for (int a = 2; a < 2; a++);"), is(0));
  }
  @Test public void forLoop1() {
    assertThat(nCount("for (int a = n; a < 2; a++);"), is(1));
  }
  @Test public void forLoop1A() {
    assertThat(nCount("for (int a = n * n + n; a < 2; a++);"), is(3));
  }
  @Test public void forLoop2() {
    assertThat(nCount("for (int a = 1; a < n; a=1);"), is(1));
  }
  @Test public void forLoop3() {
    assertThat(nCount("for (int a = 1; a < 2; a=n);"), is(1));
  }
  @Test public void forLoop4() {
    assertThat(nCount("for (int a = 1; a < 2; a++) a=n;"), is(1));
  }
  @Test public void forLoop5() {
    assertThat(nCount("for (int a = 1; a < 2; a++) a=2; a = n;"), is(1));
  }
  @Test public void forLoop6() {
    assertThat(nCount("int a = n; for (int a = 1; a < 2; a++) a=2; a = 1;"), is(1));
  }
  @Test public void forLoop7() {
    assertThat(nCount("int a = 2; for (int a = 1; a < 2; a++) { a=2; } a = n;"), is(1));
  }
  @Test public void forLoopEnhanced0() {
    assertThat(nCount("for (int a: n) return n;"), is(2));
  }
  @Test public void forLoopEnhanced1() {
    assertThat(nCount("for (int a: x) return n;"), is(1));
  }
  @Test public void forLoopEnhanced2() {
    assertThat(nCount("for (int a: n) return 1;"), is(1));
  }
  @Test public void forLoopEnhanced3() {
    assertThat(nCount("for (int a: as) {++n;}"), is(1));
  }
  @Test public void function() {
    assertThat(nCount("b = n();"), is(0));
  }
  @Test public void instanceof1() {
    assertThat(nCount("b = n instanceof x;"), is(1));
  }
  @Test public void instanceof2() {
    assertThat(nCount("b = x instanceof n;"), is(0));
  }
  @Test public void minusMinus() {
    assertThat(Collect.forAllOccurencesExcludingDefinitions(n).in(s("n--;")).size(), is(0));
  }
  @Test public void minusMinusPre() {
    assertThat(Collect.forAllOccurencesExcludingDefinitions(n).in(s("--n;")).size(), is(0));
  }
  @Test public void plusPlus() {
    assertThat(Collect.forAllOccurencesExcludingDefinitions(n).in(s("n++;")).size(), is(0));
  }
  @Test public void plusPlusPre() {
    assertThat(Collect.forAllOccurencesExcludingDefinitions(n).in(s("++n;")).size(), is(0));
  }
  @Test public void superMethodInocation() {
    assertThat(searcher().in(e("super.n(this)\n")).size(), is(0));
  }
  @Test public void usedAsType() {
    assertThat(nCount("n n;"), is(0));
  }
  @Test public void usedAsType1() {
    assertThat(nCount("n a;"), is(0));
  }
  @Test public void usedAsType2() {
    assertThat(nCount("final n n = new n(n);"), is(0));
  }
  @Test public void vanilla() {
    final Collector findUses = searcher();
    assertThat(findUses, notNullValue());
    assertThat(findUses.in(s("b = n;")).size(), is(1));
  }
  @Test public void vanillaShortVersion() {
    assertThat(nCount("b = n;"), is(1));
  }
  private int nCount(final String statement) {
    return searcher().in(s(statement)).size();
  }
  private Collector searcher() {
    return Collect.usesOf(n);
  }
}
