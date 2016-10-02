package il.org.spartan.spartanizer.leonidas;

import org.junit.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

public class LeonidasTest {
  @SuppressWarnings("static-method") @Test public void testMatches1() {
    azzert.that("$X ? y == 17 : $X2").matches("x == 7 ? y == 17 : 9");
  }

  @SuppressWarnings("static-method") @Test public void testMatches2() {
    azzert.that("$X ? 8 : $X2").notmatches("x == 7 ? y == 17 : 9");
  }

  @SuppressWarnings("static-method") @Test public void testMatches3() {
    azzert.that("w = $X ? y == 17 : $X2;").matches("w = x == 7 ? y == 17 : 9;");
  }

  @SuppressWarnings("static-method") @Test public void testMatches4() {
    azzert.that("w = $X ? 8 : $X2;").notmatches("w = x == 7 ? y == 17 : 9;");
  }

  @SuppressWarnings("static-method") @Test public void testMatches5() {
    azzert.that("x == $X ? $X2 : $X").matches("x == null ? 17 : null");
  }

  @SuppressWarnings("static-method") @Test public void testMatches6() {
    azzert.that("x == $X ? $X2 : $X").notmatches("x == null ? 17 : 18");
  }

  @SuppressWarnings("static-method") @Test public void testMatches7() {
    azzert.that("x == $X ? $X : $X").notmatches("x == null ? 17 : null");
  }

  @SuppressWarnings("static-method") @Test public void testMatches8() {
    azzert.that("$X ? y == 17 : $M").matches("x == 7 ? y == 17 : foo()");
  }

  @SuppressWarnings("static-method") @Test public void testMutation1() {
    azzert.tipper("$X1 == null ? $X2 : $X1", "$X1.defaultsTo($X2)", "defaultsTo").turns("a == null ? y : a").into("a.defaultsTo(y)");
  }

  @SuppressWarnings("static-method") @Test public void testMutation2() {
    azzert.tipper("$X1 == null ? $X2 : $X1", "$X1.defaultsTo($X2)", "defaultsTo").turns("a(b(), c.d()).e == null ? 2*3 + 4*z().x : a(b(),c.d()).e")
        .into("a(b(), c.d()).e.defaultsTo(2 * 3 + 4 * z().x)");
  }

  @SuppressWarnings("static-method") @Test public void testNotTips1() {
    azzert.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").nottips("x17 == 7 ? 2*3 + 4*z().x : x17");
  }

  @SuppressWarnings("static-method") @Test public void testNotTips2() {
    azzert.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").nottips("null == x ? 2*3 + 4*z().x : x17");
  }

  @SuppressWarnings("static-method") @Test public void testNotTips3() {
    azzert.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").nottips("a(b(), c.d()).e == null ? 2*3 + 4*z().x : a(b(), c.d()).f");
  }

  @SuppressWarnings("static-method") @Test public void testTips1() {
    azzert.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").tips("x17 == null ? 2*3 + 4*z().x : x17");
  }

  @SuppressWarnings("static-method") @Test public void testTips2() {
    azzert.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").tips("a(b(), c.d()).e == null ? 2*3 + 4*z().x : a(b(), c.d()).e");
  }

  @SuppressWarnings("static-method") @Test public void testTips3() {
    azzert.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").tips("x17 == null ? 2*3 + 4*z().x : x17");
  }

  @SuppressWarnings("static-method") @Test public void testTips4() {
    azzert.tipper("$X1 == $X2 && $X1 == $X3", "$X1.equals($X2, $X3)", "equalsToFew").tips("x1 == x2 && x1 == 789");
  }

  @SuppressWarnings("static-method") @Test public void testTips5() {
    azzert.tipper("if($X == null) return null;", "if($X == null) return Null;", "assertNotNull").tips("if(g().f.b.c(1,g(), 7) == null) return null;");
  }
  
  @SuppressWarnings("static-method") @Test public void testBlockMutation1() {
    wizard.ast("if(!$X1) $B1();");
//    azzert.tipper("if(!$X1) $B1; else $B2;", "if($X1) $B2; else $B1;", "change If order").turns("if(!(x==0)) return;").into("a.defaultsTo(y)");
  }
}
