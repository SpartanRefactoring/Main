package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.cmdline.GuessedContext.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** Unit tests for {@link GuessedContext}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 7, 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class GuessedContextTest {
  @Test public void a1() {
    trimmingOf(" public C(int i) { j = 2*i; public final int j; public C yada6() { final C res = new C(6); S.x.f(res.j); return res; ")
        .gives(" public C(int i) { j = 2*i; public final int j; public C yada6() { final C $ = new C(6); S.x.f($.j); return $; ");
  }

  @Test public void a2() {
    trimmingOf(
        "@Override public IMarkerResolution[] getResolutions(final IMarker m) { try { final Laconization s = All.get((String) m.getAttribute(Builder.Laconization_TYPE_KEY)); ")
            .gives(
                "@Override public IMarkerResolution[] getResolutions(final IMarker m) { try { final Laconization $ = All.get((String) m.getAttribute(Builder.Laconization_TYPE_KEY)); ");
  }

  @Test public void a3() {
    trimmingOf(
        " public C(int i) { j = 2*i; public final int j; public int yada7(final String blah) { final C res = new C(blah.length()); if (blah.contains(0xDEAD)) return res.j; int x = blah.length()/2; if (x==3) return x; x = y(res.j - x); return x; ")
            .gives(
                " public C(int i) { j = 2*i; public final int j; public int yada7(final String blah) { final C res = new C(blah.length()); if (blah.contains(0xDEAD)) return res.j; int $ = blah.length()/2; if ($==3) return $; $ = y(res.j - $); return $; ");
  }

  @Test public void a6() {
    trimmingOf(
        " public final int j; public void yada6() { final C res = new C(6); final Runnable r = new Runnable() { @Override public void system() { final C res2 = new C(res.j); S.x.f(res2.j); doStuff(res2); private int doStuff(final C r) { final C res = new C(r.j); return res.j + 1; S.x.f(res.j); ")
            .gives(
                " j = 2*i; } public final int j; public void yada6() { final C res = new C(6); final Runnable r = new Runnable() { @Override public void system() { final C res2 = new C(res.j); S.x.f(res2.j); doStuff(res2); private int doStuff(final C r) { final C $ = new C(r.j); return $.j + 1; S.x.f(res.j); ");
  }

  @Test public void a7() {
    trimmingOf(
        " public final int j; public C yada6() { final C res = new C(6); final Runnable r = new Runnable() { @Override public void system() { res = new C(8); S.x.f(res.j); doStuff(res); private void doStuff(C res2) { S.x.f(res2.j); private C res; S.x.f(res.j); return res; ")
            .gives(
                " j = 2*i; } public final int j; public C yada6() { final C $ = new C(6); final Runnable r = new Runnable() { @Override public void system() { res = new C(8); S.x.f(res.j); doStuff(res); private void doStuff(C res2) { S.x.f(res2.j); private C res; S.x.f($.j); return $; ");
  }

  @Test public void a8() {
    trimmingOf(
        " public C(int i) { j = 2*i; public final int j; public C yada6() { final C res = new C(6); if (res.j == 0) return null; S.x.f(res.j); return res; ")
            .gives(
                " public C(int i) { j = 2*i; public final int j; public C yada6() { final C $ = new C(6); if ($.j == 0) return null; S.x.f($.j); return $; ");
  }

  @Test public void a9() {
    trimmingOf(
        " public C(int i){j = 2*i;public final int j;public C yada6() { final C res = new C(6); if (res.j == 0) return null; S.x.f(res.j); return null;")
            .stays();
  }

  @Test public void bug() {
    azzert.that(STATEMENTS_LOOK_ALIKE.off(STATEMENTS_LOOK_ALIKE.on("int a;")), is("int a;"));
  }

  @Test public void complicated() {
    azzert.that(GuessedContext.find("public static int getFuzzyDistance(final CharSequence term,final CharSequence query,final Locale l){" //
        + "if (term == null || query == null)" //
        + "throw new IllegalArgumentException(\"Strings must not be null\");" //
        + "ExplodeOnNullWith(l, new IllegalArgumentException(\"Locale must not be null\"));"//
        + "final String termLowerCase = (term + \"\").toLowerCase(l);" //
        + "final String queryLowerCase = (query + \"\").toLowerCase(l);"//
        + "int $ = 0;" //
        + "  return $;" //
        + "for (int termIndex = 0, previousMatchingCharacterIndex = Integer.MIN_VALUE, queryIndex = 0; queryIndex < queryLowerCase"
        + ".length(); ++queryIndex)" //
        + "for (boolean termCharacterMatchFound = false; termIndex < termLowerCase.length()"//
        + "  && !termCharacterMatchFound; ++termIndex)"//
        + "if (queryLowerCase.charAt(queryIndex) == termLowerCase.charAt(termIndex)) {"//
        + "++$;"//
        + " if (previousMatchingCharacterIndex + 1 == termIndex)"//
        + "$ += 2;"//
        + "previousMatchingCharacterIndex = termIndex; "//
        + "termCharacterMatchFound = true; "//
        + "}"//
        + "  return $;"//
        + " }"), is(METHOD_LOOK_ALIKE));
  }

  @Test public void complicated2() {
    azzert.that(GuessedContext.find("for (int $N0 = 0; $N0 < $N1; ++$N0) $N2 ^= $N3.$N4($N5, $N6)[0];" //
    ), is(STATEMENTS_LOOK_ALIKE));
  }

  @Test public void dealWithComment() {
    azzert.that(find("if (b) {\n"), is(STATEMENTS_LOOK_ALIKE));
  }

  public void doNotInlineDeclarationWithAnnotationSimplified() {
    trimmingOf(" @SuppressWarnings() int $ = (Class<T>) findClass(className); return $;\n")//
        .stays();
  }

  @Test public void e03() {
    trimmingOf("/* * This is a comment */ int i = 5; int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m + i); ")
        .gives(" /* * This is a comment */ int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m + (5)); ");
  }

  @Test public void e09() {
    trimmingOf(
        " final A a = new D().new A(V){\nABRA\n{\nCADABRA\n{V;); wizard.assertEquals(5, a.new Context().lineCount()); final PureIterable&lt;Mutant&gt; ms = a.generateMutants(); wizard.assertEquals(2, count(ms)); final PureIterator&lt;Mutant&gt; i = ms.iterator(); assert (i.hasNext()); wizard.assertEquals(V;{\nABRA\nABRA\n{\nCADABRA\n{\nV;, i.next().text); assert (i.hasNext()); wizard.assertEquals(V;{\nABRA\n{\nCADABRA\nCADABRA\n{\nV;, i.next().text); assert !(i.hasNext()); ")
            .stays();
  }

  @Test public void e10() {
    trimmingOf(
        " final A a = new A(\"{\nABRA\n{\nCADABRA\n{\"); wizard.assertEquals(5, a.new Context().lineCount()); final PureIterable<Mutant> ms = a.mutantsGenerator(); wizard.assertEquals(2, count(ms)); final PureIterator<Mutant> i = ms.iterator(); assert (i.hasNext()); wizard.assertEquals(\"{\nABRA\nABRA\n{\nCADABRA\n{\n\", i.next().text); assert (i.hasNext()); wizard.assertEquals(\"{\nABRA\n{\nCADABRA\nCADABRA\n{\n\", i.next().text); assert !(i.hasNext());")
            .stays();
  }

  @Test(expected = AssertionError.class) public void error2() {
    GuessedContext.find("willBeResumed ? listeners : rImpl.atmosphereResourceEventListener().stream().forEach(¢ -> l.onBroadcast(e));");
  }

  @Test public void essenceTest() {
    azzert.that("if(b){;}throw new Exception();", is(trivia.essence("if (b) {\n /* empty */; \n} // no else \n throw new Exception();\n")));
  }

  @Test public void expression() {
    azzert.that(EXPRESSION_LOOK_ALIKE.off(EXPRESSION_LOOK_ALIKE.on("a+b")), is("a+b"));
  }

  @Test public void findAddition() {
    azzert.that(find("a+b"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findDivision() {
    azzert.that(GuessedContext.find("a/b"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findDivisionOfExpressions() {
    azzert.that(find("(a+b)/++b"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findEmptyBlock() {
    azzert.that(find("{}"), is(BLOCK_LOOK_ALIKE));
  }

  @Test(expected = AssertionError.class) public void findError() {
    azzert.that(find("}} f() { a();} b();}"), is(nullValue()));
  }

  @Test public void findExpression() {
    azzert.that(find("i++"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findLiteral0() {
    azzert.that(find("true"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findLiteral1() {
    azzert.that(find("1"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findLiteral2() {
    azzert.that(find("-0"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findLiteral3() {
    azzert.that(find("\"\""), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findLiteral4() {
    azzert.that(find("'\"'"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findMethod() {
    azzert.that(find("f() { a(); b();}"), is(METHOD_LOOK_ALIKE));
  }

  @Test public void findPlusPlus() {
    azzert.that(find("a++"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void findStatement() {
    azzert.that(find("for(;;);"), is(STATEMENTS_LOOK_ALIKE));
  }

  @Test public void findTwoStatements() {
    azzert.that(find("a(); b();"), is(STATEMENTS_LOOK_ALIKE));
  }

  @Test public void intMethod() {
    azzert.that(find("int f() { int s = 0; for (int i = 0; i <10; ++i) s += i; return s;}"), is(METHOD_LOOK_ALIKE));
  }

  @Test public void intMethod0() {
    azzert.that(find("int f() { return s;}"), is(METHOD_LOOK_ALIKE));
  }

  @Test public void intMethod1() {
    azzert.that(find("void f(){}"), is(METHOD_LOOK_ALIKE));
  }

  @Test public void intoCompilationUnit() {
    @NotNull final GuessedContext w = EXPRESSION_LOOK_ALIKE;
    @NotNull final String codeFragment = "a + b * c";
    @NotNull final CompilationUnit u = w.intoCompilationUnit(codeFragment);
    assert u != null;
    azzert.that(w.off(u + ""), containsString(codeFragment));
  }

  @Test public void intoDocument() {
    @NotNull final GuessedContext w = EXPRESSION_LOOK_ALIKE;
    @NotNull final String codeFragment = "a + b * c";
    @NotNull final Document d = w.intoDocument(codeFragment);
    assert d != null;
    azzert.that(w.off(d.get()), containsString(codeFragment));
  }

  @Test public void method() {
    azzert.that(METHOD_LOOK_ALIKE.off(METHOD_LOOK_ALIKE.on("int f() { return a; }")), is("int f() { return a; }"));
  }

  @Test public void method2() {
    azzert.that(GuessedContext.find(//
        "A a(){" + //
            "  b(c);" + //
            " }"//
    ), is(METHOD_LOOK_ALIKE));
  }

  @Test public void method3() {
    azzert.that(GuessedContext.find(//
        "public int hashCode(){return a(1 ^ 1 ^ (1 ^ 1));}"//
    ), is(METHOD_LOOK_ALIKE));
  }

  @Test public void method4() {
    azzert.that(GuessedContext.find(//
        "public int hashCode(){return 1;}"//
    ), is(METHOD_LOOK_ALIKE));
  }

  @Test public void method5() {
    azzert.that(GuessedContext.find(//
        "public int hashCode(){return a(1);}"//
    ), is(METHOD_LOOK_ALIKE));
  }

  @Test public void method6() {
    azzert.that(GuessedContext.find(//
        "public int hashCode(){return a(1 ^ 1 ^ 1);}"//
    ), is(METHOD_LOOK_ALIKE));
  }

  @Test public void methodInvocation() {
    azzert.that(GuessedContext.find("fuo()"), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void offDivision() {
    azzert.that("a/b", is(EXPRESSION_LOOK_ALIKE.off(EXPRESSION_LOOK_ALIKE.on("a/b"))));
  }

  @Test public void statement() {
    azzert.that(STATEMENTS_LOOK_ALIKE.off(STATEMENTS_LOOK_ALIKE.on("int a;")), is("int a;"));
  }

  @Test public void statement2() {
    azzert.that(GuessedContext.find(//
        "\"//\""), is(EXPRESSION_LOOK_ALIKE));
  }

  @Test public void statement3() {
    azzert.that(
        GuessedContext.find(//
            "willBeResumed ? listeners : rImpl.atmosphereResourceEventListener().stream().forEach(¢ -> l.onBroadcast(e))"),
        is(EXPRESSION_LOOK_ALIKE));
  }
}
