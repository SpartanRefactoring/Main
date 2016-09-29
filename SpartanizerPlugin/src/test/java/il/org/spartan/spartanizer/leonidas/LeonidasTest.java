package il.org.spartan.spartanizer.leonidas;

/** @author Ori Marcovitch
 * @year 2016 */
import static org.junit.Assert.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.tipping.*;

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

  @SuppressWarnings("static-method") @Test public void testNotTips1() {
    azzert.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").nottips("x17 == 7 ? 2*3 + 4*z().x : x17");
  }

  @SuppressWarnings("static-method") @Test public void testNotTips2() {
    azzert.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").nottips("null == x ? 2*3 + 4*z().x : x17");
  }

  @SuppressWarnings("static-method") @Test public void testNotTips3() {
    azzert.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").nottips("a(b(), c.d()).e == null ? 2*3 + 4*z().x : a(b(), c.d()).f");
  }
  // @SuppressWarnings("static-method") @Test public void testMutation1() {
  // azzert.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)",
  // "defaultsTo").turns("a == null ? y : a").into("a.defaultsTo(y)");
  // }
  //
  // @SuppressWarnings("static-method") @Test public void testMutation2() {
  // azzert.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)",
  // "defaultsTo").turns("a(b(), c.d()).e == null ? 2*3 + 4*z().x : a(b(),
  // c.d()).e")
  // .into("a(b(), c.d()).e.defaultsTo(2*3 + 4*z().x)");
  // }
}

class azzert {
  public static expression that(final String ¢) {
    return new expression(¢);
  }

  public static tipper tipper(final String p, final String s, final String d) {
    return new tipper(p, s, d);
  }
}

class expression {
  final String s;

  public expression(final String s) {
    this.s = s;
  }

  public void matches(final String s2) {
    assertTrue(Matcher.matches(wizard.ast(s), wizard.ast(s2)));
  }

  public void notmatches(final String s2) {
    assertFalse(Matcher.matches(wizard.ast(s), wizard.ast(s2)));
  }
}

class tipper {
  private final UserDefinedTipper<ASTNode> tipper;

  public tipper(final String p, final String r, final String d) {
    tipper = TipperFactory.tipper(p, r, d);
  }

  public void tips(final String ¢) {
    assertTrue(tipper.canTip(wizard.ast(¢)));
  }

  public void nottips(final String ¢) {
    assertFalse(tipper.canTip(wizard.ast(¢)));
  }

  public turns turns(String ¢) {
    return new turns(tipper, ¢);
  }
}

class turns {
  private final UserDefinedTipper<ASTNode> tipper;
  private final ASTNode n;

  public turns(UserDefinedTipper<ASTNode> tipper, String s) {
    this.tipper = tipper;
    this.n = wizard.ast(s);
  }

  public void into(String s) {
    try {
      ASTRewrite r = ASTRewrite.create(n.getAST());
      tipper.tip(n).go(r, null);
      r.rewriteAST();
      assertEquals(s, (n + ""));
    } catch (TipperFailure e) {
      e.printStackTrace();
      fail();
    } catch (JavaModelException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
  }
}
