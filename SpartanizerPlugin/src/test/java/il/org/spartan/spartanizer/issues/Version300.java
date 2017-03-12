package il.org.spartan.spartanizer.issues;
import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.nominal.English.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.tippers.*;

/** Misc unit tests with no better other place for version 3.00
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
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

  @Test public void abcd() {
     trimmingOf("a = !(b ? c : d)") //
   .using(PrefixExpression.class,new PrefixNotPushdown()) //
   .gives("a=b?!c:!d") //
    .stays() //
    ;
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
        theSpartanizer.fixedPoint(//
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
        theSpartanizer.fixedPoint(//
            "boolean a(int[] b, int c){ if (d == c) return true; return false; }"), //
        iz(//
            "boolean a(int[] b, int c){ return d == c; }") //
    );
  }

  @Test public void stB() {
    azzert.that(
        theSpartanizer.fixedPoint(//
            "A a(A b) throws B { A $; $ = b; return $; }"), //
        iz("A a(A b) throws B { return b; }"));
  }

  @Test public void stC() {
    azzert.that(
        theSpartanizer.fixedPoint(//
            "A a(A b) throws B { A $ = b; return $; }"), //
        iz("A a(A b) throws B { return b; }"));
  }

  @Test public void stZ() {
    azzert.that(
        theSpartanizer.fixedPoint(//
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
   
  
}
