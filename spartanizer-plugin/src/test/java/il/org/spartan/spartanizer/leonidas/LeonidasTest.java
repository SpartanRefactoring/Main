/* TODO orimarco <marcovitch.ori@gmail.com> please add a description
 *
 * @author orimarco <marcovitch.ori@gmail.com>
 *
 * @since Sep 28, 2016 */
package il.org.spartan.spartanizer.leonidas;

import org.junit.Test;

@SuppressWarnings("static-method")
public class LeonidasTest {
  @Test public void testMatches1() {
    leonidasSays.that("$X ? y == 17 : $X2").matches("x == 7 ? y == 17 : 9");
  }
  @Test public void testMatches10() {
    leonidasSays.that("for($N1 $N2 : $X) $N4.$N3($N2);").matches("for (Expression ¢ : hop.operands(flatten.of(inner))) make.notOf(¢);");
  }
  @Test public void testMatches11() {
    leonidasSays.that("for($N1 $N2 : $X1) $B").matches("for (A b : C) print();");
  }
  @Test public void testMatches12() {
    leonidasSays.that("for($N1 $N2 : $X1) if($X2) $B").matches("for (A b : C) if(b!=null) b.print();");
  }
  @Test public void testMatches2() {
    leonidasSays.that("$X ? 8 : $X2").notmatches("x == 7 ? y == 17 : 9");
  }
  @Test public void testMatches3() {
    leonidasSays.that("w = $X ? y == 17 : $X2;").matches("w = x == 7 ? y == 17 : 9;");
  }
  @Test public void testMatches4() {
    leonidasSays.that("w = $X ? 8 : $X2;").notmatches("w = x == 7 ? y == 17 : 9;");
  }
  @Test public void testMatches5() {
    leonidasSays.that("x == $X ? $X2 : $X").matches("x == null ? 17 : null");
  }
  @Test public void testMatches6() {
    leonidasSays.that("x == $X ? $X2 : $X").notmatches("x == null ? 17 : 18");
  }
  @Test public void testMatches7() {
    leonidasSays.that("x == $X ? $X : $X").notmatches("x == null ? 17 : null");
  }
  @Test public void testMatches8() {
    leonidasSays.that("$X ? y == 17 : $M").matches("x == 7 ? y == 17 : foo()");
  }
  @Test public void testMatches9() {
    leonidasSays.that("if(true) $B").matches("if(true) foo();");
  }
  @Test public void testMutation1() {
    leonidasSays.tipper("$X1 == null ? $X2 : $X1", "$X1.defaultsTo($X2)", "defaultsTo").turns("a == null ? y : a").into("a.defaultsTo(y)");
  }
  @Test public void testMutation10() {
    leonidasSays.tipper("for($N1 $N2 : $X1) $B", "$X1.stream().forEach($N2 -> $B);", "").turns("for (A b : C) b.print();")
        .into("C.stream().forEach(b -> {b.print();} );");
  }
  @Test public void testMutation11() {
    leonidasSays.tipper("for($N1 $N2 : $X1) if($X2) $N3.$N4($A);", "$X1.stream().filter($N2 -> $X2).forEach($N2 -> $N3.$N4($A));", "")
        .turns("for (A b : C) if(b!=null) b.print();").into("C.stream().filter(b -> b != null).forEach(b -> b.print() );");
  }
  @Test public void testMutation2() {
    leonidasSays.tipper("$X1 == null ? $X2 : $X1", "$X1.defaultsTo($X2)", "defaultsTo")
        .turns("a(b(), c.d()).e == null ? 2*3 + 4*z().x : a(b(),c.d()).e").into("a(b(), c.d()).e.defaultsTo(2 * 3 + 4 * z().x)");
  }
  @Test public void testMutation3() {
    leonidasSays.tipper("$X1 = $X1 != null ? $X1 : $X2", "lazyEvaluatedTo($X1,$X2)", "lazy evaluation")
        .turns("defaultInstance = defaultInstance != null ? defaultInstance : freshCopyOfAllTippers()")
        .into("lazyEvaluatedTo(defaultInstance, freshCopyOfAllTippers())");
  }
  @Test public void testMutation4() {
    leonidasSays.tipper("$X1 = $X1 == null ? $X2 : $X1", "lazyEvaluatedTo($X1,$X2)", "lazy evaluation")
        .turns("defaultInstance = defaultInstance == null ? freshCopyOfAllTippers() : defaultInstance")
        .into("lazyEvaluatedTo(defaultInstance, freshCopyOfAllTippers())");
  }
  @Test public void testMutation5() {
    leonidasSays.tipper("$X1 = $X1 == null ? $X2 : $X1", "lazyEvaluatedTo($X1,$X2)", "lazy evaluation")
        .turns("return defaultInstance = defaultInstance == null ? freshCopyOfAllTippers() : defaultInstance;")
        .into("return lazyEvaluatedTo(defaultInstance, freshCopyOfAllTippers());");
  }
  @Test public void testMutation6() {
    leonidasSays.tipper("$X1 ? $X2 : $X1", "$X1", "").turns("return y ? z : y;").into("return y;");
  }
  @Test public void testMutation7() {
    leonidasSays.statementsTipper("if($X1 == null) $X1 = $X2; return $X1;", "return $X1 = $X1 == null ? $X2 : $X1;", "")
        .turns("if (instance == null) instance = allTippers(); return instance;")
        .into("return instance = instance == null ? allTippers() : instance;");
  }
  @Test public void testMutation8() {
    leonidasSays.statementsTipper("$X = $X.$N1($A1); $X = $X.$N2($A2);", "$X = $X.$N1($A1).$N2($A2);", "")
        .turns("$ = $.replaceFirst(\"^[\\\\[]+L\", \"\");\n $ = $.replaceAll(\";$\", \"\");")
        .into("$= $.replaceFirst(\"^[\\\\[]+L\", \"\").replaceAll(\";$\", \"\");");
  }
  @Test public void testMutation9() {
    leonidasSays.tipper("for($N1 $N2 : $X1) if($X2) $B", "$X1.stream().filter($N2 -> $X2).forEach($N2 -> $B);", "")
        .turns("for (A b : C) if(b!=null) b.print();").into("C.stream().filter(b -> b != null).forEach(b -> {b.print();} );");
  }
  @Test public void testNotTips1() {
    leonidasSays.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").nottips("x17 == 7 ? 2*3 + 4*z().x : x17");
  }
  @Test public void testNotTips2() {
    leonidasSays.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").nottips("null == x ? 2*3 + 4*z().x : x17");
  }
  @Test public void testNotTips3() {
    leonidasSays.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo")
        .nottips("a(b(), c.d()).e == null ? 2*3 + 4*z().x : a(b(), c.d()).f");
  }
  @Test public void testNotTips4() {
    leonidasSays.tipper("if($X) return y; print(7);", "", "").nottips("if(a || b && c) return z; print(7);");
  }
  @Test public void testNotTips5() {
    leonidasSays.tipper("x", "", "").nottips("y");
  }
  @Test public void testNotTips6() {
    leonidasSays.tipper("print(7); print(8);", "", "").nottips("print(7); print(9);");
  }
  @Test public void testNotTips7() {
    leonidasSays.tipper("print(7);", "", "").nottips("print(8);");
  }
  @Test public void testNotTips8() {
    leonidasSays.tipper("7", "", "").nottips("8");
  }
  @Test public void testTips1() {
    leonidasSays.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").tips("x17 == null ? 2*3 + 4*z().x : x17");
  }
  @Test public void testTips10() {
    leonidasSays.tipper("$X = $X.$N1($A1); $X = $X.$N2($A2);", "", "")
        .tips("$ = $.replaceFirst(\"^[\\\\[]+L\", \"\");\n $ = $.replaceAll(\";$\", \"\");");
  }
  @Test public void testTips10WithWhitespaces() {
    leonidasSays.tipper("$X = $X.$N1($A1); $X = $X.$N2($A2);", "", "")
        .tips("$ =$.replaceFirst(\"^[\\\\[]+L\", \"\");\n\t\t\t\n     $=    \t$.replaceAll(\";$\", \"\");");
  }
  @Test public void testTips11() {
    leonidasSays.tipper("if($X == null) throw new $N();", "ExplodeOnNullWith($N, $X)", "").tips("if (o == null) throw new RuntimeErrorException();");
  }
  @Test public void testTips12() {
    leonidasSays.tipper("if($X) $N($A);", "when($X).execute((x) -> $N($A));", "").tips("if (o == null) print(8);");
  }
  @Test public void testTips13() {
    leonidasSays.tipper("if($X1) $X2.$N($A);", "when($X1).execute((x) -> $X2.$N($A));", "").tips("if (o == null) o.print(8);");
  }
  @Test public void testTips14() {
    leonidasSays.tipper("$N.c.$N3.$N2()", "", "").tips("a.b.c.d.e()");
  }
  @Test public void testTips15() {
    leonidasSays.tipper("$N.$N2()", "", "").tips("a.b.c.d.e()");
  }
  @Test public void testTips16() {
    leonidasSays.tipper("$M", "", "").tips("a.b.c.d.e()");
  }
  @Test public void testTips17() {
    leonidasSays.tipper("$N($M)", "", "").tips("x(a.b.c.d.e())");
  }
  @Test public void testTips18() {
    leonidasSays.tipper("return $N($A);", "", "").tips("return bar();");
  }
  @Test public void testTips19() {
    leonidasSays.tipper("for($N1 $N2 : $X1) if($X2) $B", "$X1.stream().filter($N2 -> $X2).foreach($N2 -> $B)", "")
        .tips("for (A b : C) if(b!=null) b.print();");
  }
  @Test public void testTips2() {
    leonidasSays.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo")
        .tips("a(b(), c.d()).e == null ? 2*3 + 4*z().x : a(b(), c.d()).e");
  }
  @Test public void testTips20() {
    leonidasSays.tipper("$X;", "", "").tips("bar();");
  }
  @Test public void testTips21() {
    leonidasSays.tipper("$X1.indexOf($X2) >= 0", "$X1.contains($X2)", "").tips("s1.indexOf(s2) >= 0");
  }
  @Test public void testTips3() {
    leonidasSays.tipper("$X == null ? $X2 : $X", "$X.defaultsTo($X2)", "defaultsTo").tips("x17 == null ? 2*3 + 4*z().x : x17");
  }
  @Test public void testTips4() {
    leonidasSays.tipper("$X1 == $X2 && $X1 == $X3", "$X1.equals($X2, $X3)", "equalsToFew").tips("x1 == x2 && x1 == 789");
  }
  @Test public void testTips5() {
    leonidasSays.tipper("if($X == null) return null;", "if($X == null) return nil;", "assertNonNull")
        .tips("if(g().f.b.c(1,g(), 7) == null) return null;");
  }
  @Test public void testTips6() {
    leonidasSays.tipper("if(!$X1) $B1 else $B2", "if($X1) $B2 else $B1", "change If order").tips("if(!(x==0)) return; else print(7);");
  }
  @Test public void testTips7() {
    leonidasSays.tipper("if(x) return y; print(7);", "", "").tips("if(x) return y; print(7);");
  }
  @Test public void testTips8() {
    leonidasSays.tipper("if($X) return y; print(7);", "", "").tips("if(a || b && c) return y; print(7);");
  }
  @Test public void testTips9() {
    leonidasSays.tipper("if($X1 == null) $X1 = $X2; return $X1;", "", "").tips("if (instance == null) instance = allTippers(); return instance;");
  }
}
