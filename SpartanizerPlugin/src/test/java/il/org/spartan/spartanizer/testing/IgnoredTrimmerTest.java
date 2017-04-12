package il.org.spartan.spartanizer.testing;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil
 * @since 2014-07-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class IgnoredTrimmerTest {
  public void doNotInlineDeclarationWithAnnotationSimplified() {
    trimminKof("@SuppressWarnings() int $ = (Class<T>) findClass(className);\n")//
        .stays();
  }

  @Test public void a2() {
    trimminKof(
        " int res = blah.length(); if (blah.contains(0xDEAD)) return res * 2; if (res % 2 ==0) return ++res; if (blah.startsWith(\"y\")) { return y(res); int x = res + 6; if (x>1) return res + x; res -= 1; return res; ")
            .gives(
                " int $ = blah.length(); if (blah.contains(0xDEAD)) return $ * 2; if ($ % 2 ==0) return ++$; if (blah.startsWith(\"y\")) { return y($); int x = $ + 6; if (x>1) return $ + x; $ -= 1; return $; ");
  }

  @Test public void inlineSingleUse01() {
    trimminKof("/* * This is a comment */ int i = y(0); int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m + i); ")
        .gives("/* * This is a comment */ int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m + (y(0))); ");
  }

  @Test public void inlineSingleUse02() {
    trimminKof("/* * This is a comment */ int i = 5,j=3; int k = j+2; int m = k + j -19 +i; y(k); ")
        .gives("/* * This is a comment */ int j=3; int k = j+2; int m = k + j -19 +(5); y(k); ");
  }

  @Test public void inlineSingleUse03() {
    trimminKof("/* * This is a comment */ int i = 5; int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m + i); ")
        .gives("/* * This is a comment */ int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m + (5)); ");
  }

  @Test public void inlineSingleUse04() {
    trimminKof("int x = 6; final C b = new C(x); int y = 2+b.j; y(y-b.j); y(y*2); ")
        .gives("final C b = new C((6)); int y = 2+b.j; y(y-b.j); y(y*2); ");
  }

  @Test public void inlineSingleUse05() {
    trimminKof("int x = 6; final C b = new C(x); int y = 2+b.j; y(y+x); y(y*x); ")//
        .gives("int x = 6; int y = 2+(new C(x)).j; y(y+x); y(y*x); ");
  }

  @Test public void inlineSingleUse09() {
    trimminKof(
        " final A a = new D().new A(V){\nABRA\n{\nCADABRA\n{V;); wizard.assertEquals(5, a.new Context().lineCount()); final PureIterable&lt;Mutant&gt; ms = a.generateMutants(); wizard.assertEquals(2, count(ms)); final PureIterator&lt;Mutant&gt; i = ms.iterator(); assert (i.hasNext()); wizard.assertEquals(V;{\nABRA\nABRA\n{\nCADABRA\n{\nV;, i.next().text); assert (i.hasNext()); wizard.assertEquals(V;{\nABRA\n{\nCADABRA\nCADABRA\n{\nV;, i.next().text); assert !(i.hasNext()); ")
            .stays();
  }

  @Test public void inlineSingleUse10() {
    trimminKof(
        " final A a = new A(\"{\nABRA\n{\nCADABRA\n{\"); wizard.assertEquals(5, a.new Context().lineCount()); final PureIterable<Mutant> ms = a.mutantsGenerator(); wizard.assertEquals(2, count(ms)); final PureIterator<Mutant> i = ms.iterator(); assert (i.hasNext()); wizard.assertEquals(\"{\nABRA\nABRA\n{\nCADABRA\n{\n\", i.next().text); assert (i.hasNext()); wizard.assertEquals(\"{\nABRA\n{\nCADABRA\nCADABRA\n{\n\", i.next().text); assert !(i.hasNext());")
            .stays();
  }

  @Test public void reanmeReturnVariableToDollar01() {
    trimminKof("public C(int i) { j = 2*i; public final int j; public C yada6() { final C res = new C(6); S.x.f(res.j); return res; ")
        .gives("public C(int i) { j = 2*i; public final int j; public C yada6() { final C $ = new C(6); S.x.f($.j); return $; ");
  }

  @Test public void reanmeReturnVariableToDollar02() {
    trimminKof(
        " int res = blah.length(); if (blah.contains(0xDEAD)) return res * 2; if (res % 2 ==0) return ++res; if (blah.startsWith(\"y\")) { return y(res); int x = res + 6; if (x>1) return res + x; res -= 1; return res; ")
            .gives(
                " int $ = blah.length(); if (blah.contains(0xDEAD)) return $ * 2; if ($ % 2 ==0) return ++$; if (blah.startsWith(\"y\")) { return y($); int x = $ + 6; if (x>1) return $ + x; $ -= 1; return $; ");
  }

  @Test public void reanmeReturnVariableToDollar03() {
    trimminKof(
        " public C(int i) { j = 2*i; public final int j; public int yada7(final String blah) { final C res = new C(blah.length()); if (blah.contains(0xDEAD)) return res.j; int x = blah.length()/2; if (x==3) return x; x = y(res.j - x); return x; ")
            .gives(
                " public C(int i) { j = 2*i; public final int j; public int yada7(final String blah) { final C res = new C(blah.length()); if (blah.contains(0xDEAD)) return res.j; int $ = blah.length()/2; if ($==3) return $; $ = y(res.j - $); return $; ");
  }

  @Test public void reanmeReturnVariableToDollar06() {
    trimminKof(
        " j = 2*i; } public final int j; public void yada6() { final C res = new C(6); final Runnable r = new Runnable() { @Override public void system() { final C res2 = new C(res.j); S.x.f(res2.j); doStuff(res2); private int doStuff(final C r) { final C res = new C(r.j); return res.j + 1; S.x.f(res.j); ")
            .gives(
                " j = 2*i; } public final int j; public void yada6() { final C res = new C(6); final Runnable r = new Runnable() { @Override public void system() { final C res2 = new C(res.j); S.x.f(res2.j); doStuff(res2); private int doStuff(final C r) { final C $ = new C(r.j); return $.j + 1; S.x.f(res.j); ");
  }

  @Test public void reanmeReturnVariableToDollar07() {
    trimminKof(
        " j = 2*i; } public final int j; public C yada6() { final C res = new C(6); final Runnable r = new Runnable() { @Override public void system() { res = new C(8); S.x.f(res.j); doStuff(res); private void doStuff(C res2) { S.x.f(res2.j); private C res; S.x.f(res.j); return res; ")
            .gives(
                " j = 2*i; } public final int j; public C yada6() { final C $ = new C(6); final Runnable r = new Runnable() { @Override public void system() { res = new C(8); S.x.f(res.j); doStuff(res); private void doStuff(C res2) { S.x.f(res2.j); private C res; S.x.f($.j); return $; ");
  }

  @Test public void reanmeReturnVariableToDollar08() {
    trimminKof(
        " public C(int i) { j = 2*i; public final int j; public C yada6() { final C res = new C(6); if (res.j == 0) return null; S.x.f(res.j); return res; ")
            .gives(
                " public C(int i) { j = 2*i; public final int j; public C yada6() { final C $ = new C(6); if ($.j == 0) return null; S.x.f($.j); return $; ");
  }

  @Test public void reanmeReturnVariableToDollar09() {
    trimminKof(
        " public C(int i){j = 2*i;public final int j;public C yada6() { final C res = new C(6); if (res.j == 0) return null; S.x.f(res.j); return null;")
            .stays();
  }

  @Test public void reanmeReturnVariableToDollar10() {
    trimminKof(
        "@Override public IMarkerResolution[] getResolutions(final IMarker m) { try { final L s = All.get((String) m.getAttribute(Builder.L_TYPE_KEY)); ")
            .gives(
                "@Override public IMarkerResolution[] getResolutions(final IMarker m) { try { final L $ = All.get((String) m.getAttribute(Builder.L_TYPE_KEY)); ");
  }

  @Test public void renameVariableUnderscore2() {
    trimminKof("class A {int __; int f(int __) {return __;}}")//
        .gives("class A {int ____; int f(int ____) {return ____;}}");
  }

  @Test public void replaceClassInstanceCreationWithFactoryClassInstanceCreation() {
    trimminKof("Character x = new Character(new Character(f()));")//
        .gives("Character x = Character.valueOf(Character.valueOf(f()));");
  }

  @Test public void stringFromBuilderAddParenthesis() {
    trimminKof("new StringBuilder(f()).append(1+1).toString()")//
        .gives("\"\" + f() + (1+1)");
  }

  @Test public void stringFromBuilderGeneral() {
    trimminKof("new StringBuilder(myName).append(\"\'s grade is \").append(100).toString()")//
        .gives("myName + \"\'s grade is \" + 100");
  }

  @Test public void stringFromBuilderNoStringComponents() {
    trimminKof("new StringBuilder(0).append(1).toString()")//
        .gives("\"\" + 0 + 1");
  }
}