package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;
import static il.org.spartan.utils.English.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.utils.*;

/** Misc unit tests with no better other place for version 3.00
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-09 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Version300 {
  // @Ignore("Unignore one by one")
  @Test public void negationPushdownTernary() {
    trimmingOf("a = !(b ? c: d)")//
        .using(PrefixExpression.class, new PrefixNotPushdown())//
        .gives("a=b?!c:!d") //
    ;
  }

  @Ignore @Test public void inlineArrayInitialization1() {
    trimmingOf("public void multiDimensionalIntArraysAreEqual(){ " //
        + " int[][] int1={{1, 2, 3}, {4, 5, 6}}; " //
        + " int[][] int2={{1, 2, 3}, {4, 5, 6}}; " //
        + " assertArrayEquals(int1, int2); " //
        + "}")
            .gives("public void multiDimensionalIntArraysAreEqual(){ " //
                + " int[][] int1={{1, 2, 3}, {4, 5, 6}}" //
                + " , int2={{1, 2, 3}, {4, 5, 6}}; " //
                + " assertArrayEquals(int1, int2); " //
                + "}")
            .gives("public void multiDimensionalIntArraysAreEqual(){ " //
                + " assertArrayEquals(new int[][]{{1,2,3},{4,5,6}},new int[][]{{1,2,3},{4,5,6}}); " //
                + "}");
  }

  /** trimmer wraps with void method so it is tipped by
   * {@link RemoveRedundantSwitchReturn} */
  @Ignore("Yuval Simon") @Test public void switchSimplifyCaseAfterDefault1() {
    trimmingOf("switch(n.getNodeType()){case BREAK_STATEMENT:return 0;case CONTINUE_STATEMENT:return 1;case RETURN_STATEMENT:return 2;"
        + "case THROW_STATEMENT:return 3;default:return-1;}")//
            .stays();
  }

  @Test public void myClassName() {
    azzert.that(system.callingClassName(), is(getClass().getCanonicalName()));
  }

  @Test public void abcd() {
    trimmingOf("a = !(b ? c : d)") //
        .using(PrefixExpression.class, new PrefixNotPushdown()) //
        .gives("a=b?!c:!d") //
        .stays() //
    ;
  }

  @Ignore @Test public void ifab() {
    trimmingOf("if (a++ == b++) { }") //
        .using(IfStatement.class, new IfEmptyThenEmptyElse()) //
        .gives("a++;b++;") //
        .stays() //
    ;
  }

  /** Automatically generated on Thu-Mar-16-08:15:41-IST-2017, copied by
   * Yossi */
  @Test public void ifaAb() {
    trimmingOf("if (a) { A b; }") //
        .using(IfStatement.class, new IfDeadRemove()) //
        .gives("{}") //
        .gives("") //
        .stays() //
    ;
  }

  /** Automatically generated on Thu-Mar-16-08:26:53-IST-2017, copied by
   * Yossi */
  @Test public void genererated13() {
    trimmingOf("{}") //
        .gives("") //
        .stays() //
    ;
  }

  @Test public void ifDoNotRemoveBracesWithVariableDeclarationStatement() {
    trimmingOf("if(a) { int i = 3; }")//
        .gives("{}") //
        .gives("") //
        .stays();
  }

  @Test public void ifDoNotRemoveBracesWithVariableDeclarationStatement2() {
    trimmingOf("if(a) { Object o; }")//
        .gives("{}") //
        .gives("") //
        .stays();
  }

  @Test public void a() {
    azzert.that(
        theSpartanizer.once(//
            "boolean a(int[] b, int c){ if (d == c) return true; return false; }"), //
        iz(//
            "boolean a(int[] b, int c){ return d == c? true : false; }") //
    );
  }

  @Test public void b() {
    azzert.that(
        theSpartanizer.twice(//
            "boolean a(int[] b, int c){ if (d == c) return true; return false; }"), //
        iz(//
            "boolean a(int[] b, int c){ return d == c || false; }") //
    );
  }

  @Test public void c() {
    azzert.that(
        theSpartanizer.thrice(//
            "boolean a(int[] b, int c){ if (d == c) return true; return false; }"), //
        iz(//
            "boolean a(int[] b, int c){ return d == c; }") //
    );
  }

  @Test public void d() {
    azzert.that(
        theSpartanizer.repetitively(//
            "boolean a(int[] b, int c){ if (d == c) return true; return false; }"), //
        iz(//
            "boolean a(int[] b, int c){ return d == c; }") //
    );
  }

  @Test public void stAgives() {
    trimmingOf("boolean a(int[] b, int c){ if (d == c) return true; return false; }") //
        .gives("boolean a(int[] b, int c){ return d == c ? true: false; }") //
        .gives("boolean a(int[] b, int c){ return d == c || false; }") //
        .gives("boolean a(int[] b, int c){ return d == c; }") //
        .stays() //
    ;
  }

  @Test public void stA() {
    azzert.that(
        theSpartanizer.repetitively(//
            "boolean a(int[] b, int c){ if (d == c) return true; return false; }"), //
        iz(//
            "boolean a(int[] b, int c){ return d == c; }") //
    );
  }

  @Test public void stB() {
    azzert.that(
        theSpartanizer.repetitively(//
            "A a(A b) throws B { A $; $ = b; return $; }"), //
        iz("A a(A b) throws B { return b; }"));
  }

  @Test public void stC() {
    azzert.that(
        theSpartanizer.repetitively(//
            "A a(A b) throws B { A $ = b; return $; }"), //
        iz("A a(A b) throws B { return b; }"));
  }

  @Test public void stZ() {
    azzert.that(
        theSpartanizer.repetitively(//
            "A a(A b)throws B{ A c; c = b; return c; }"), //
        iz(//
            "A a(A b) throws B { return b; }"));
  }

  @Test public void lowerUpperNamer() {
    azzert.that(lowerFirstLetter("Hello"), is("hello"));
    azzert.that(upperFirstLetter("Hello"), is("Hello"));
    azzert.that(lowerFirstLetter("hello"), is("hello"));
    azzert.that(upperFirstLetter("hello"), is("Hello"));
  }

  @Test public void x() {
    trimmingOf("int f(int i) { for(;i<100;i=i+1) if(false) break; return i; }")//
        .gives("int f(int ¢){for(;¢<100;¢=¢+1)if(false)break;return ¢;}") //
        .gives("int f(int ¢){for(;¢<100;¢+=1){}return ¢;}");
  }

  /** Introduced by Yossi on Thu-Mar-16-12:37:12-IST-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void x1() {
    trimmingOf("int a(int b) { for (; b < 100; b = b + 1) if (false) break; return b; }") //
        .using(Assignment.class, new AssignmentToFromInfixIncludingTo()) //
        .gives("int a(int b){for(;b<100;b+=1)if(false)break;return b;}") //
        .using(IfStatement.class, new IfTrueOrFalse()) //
        .gives("int a(int b){for(;b<100;b+=1){}return b;}");
  }

  @Test public void intaIntbForb100bb1IfFalseBreakReturnb() {
    trimmingOf("int a(int b) { for (; b < 100; b = b + 1) if (false) break; return b; }") //
        .using(Assignment.class, new AssignmentToFromInfixIncludingTo()) //
        .gives("int a(int b){for(;b<100;b+=1)if(false)break;return b;}") //
        .using(IfStatement.class, new IfTrueOrFalse()) //
        .gives("int a(int b){for(;b<100;b+=1){}return b;}")//
    ;
  }
}
