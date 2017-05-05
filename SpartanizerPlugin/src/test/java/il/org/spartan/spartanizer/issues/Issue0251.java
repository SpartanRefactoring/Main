package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tippers.*;

/** Checking that the tipper of removing unmeaningful statements from blocks is
 * working properly
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0251 {
  @Test public void Issue302_test() {
    trimmingOf("if(b()){int i;}")//
        .gives("if (b()){}")//
    ;
  }
  @Test public void t01() {
    trimmingOf("if(b==true){int i;}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }
  @Test public void t02() {
    trimmingOf("if(b){int i;int b; b=i+1;g();}")//
        .gives("if(!b)return;int i;int b;b=i+1;g();");
  }
  @Test public void t03() {
    trimmingOf("if(b){int i;int j;int k;}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }
  @Test public void t04() {
    trimmingOf("if(b){int i;int j;}else{int tipper;}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }
  @Test public void t05() {
    trimmingOf("if(b)g();")//
        .stays() //
    ;
  }
  @Test public void t06() {
    trimmingOf("if(b()){int i;}")//
        .gives("if(b()){}")//
    ;
  }
  @Test public void t07() {
    trimmingOf("if(b){int i;int j;}else{g();}")//
        .gives("if(!b)g();else {int i;int j;}")//
        .gives("if(!b)g();else{}")//
        .gives("if(!b)g();")//
        .stays();
  }
  @Test public void t10() {
    trimmingOf("if(b==true){int i=5;}")//
        .gives("{}")//
        .gives("")//
        .stays() //
    ;
  }
  @Test public void t11() {
    trimmingOf("if(b==true){int i=g();}")//
        .gives("if(b){g();}")//
        .gives("if(b)g();")//
        .stays() //
    ;
  }
  @Test public void t12() {
    trimmingOf("if(b==true){int i=5,q=g();}")//
        .gives("if(b){g(); int i=5;}")//
        .gives("if(!b)return;g(); int i=5;")//
        .gives("if(!b)return;g();")//
        .gives("if(!b);else g();")//
        .gives("if(b)g();")//
        .stays() //
    ;
  }
  @Test public void t13() {
    trimmingOf("if(b){int i;int j;if(s){int q;}}else{int q;int tipper;}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }
  @Test public void t14() {
    trimmingOf("if(b){int i;int j;while(s){int q;}}else{int q;int tipper;}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }
  @Test public void t15() {
    trimmingOf("if(b==q()){int i;}")//
        .gives("if(b==q()){}")//
    ;
  }
  @Test public void t16() {
    trimmingOf("while(b==q){int i;}")//
        .gives("{}");
  }
  @Test public void t17() {
    final String variable = "while(b==q){if(tipper==q()){int i;}}";
    assert !sideEffects.free(into.s(variable));
    trimmingOf(variable)//
        .gives("while(b==q)if(tipper==q()){int i;}")//
        .gives("while(b==q)if(tipper==q()){}")//
    ;
  }
  @Test public void t17a() {
    assert !sideEffects.free(into.e("q()"));
  }
  @Test public void t17b() {
    assert !sideEffects.free(into.s("while(b==q){if(tipper==q()){int i;}}"));
  }
  /** Introduced by Yossi on Sat-Mar-25-05:15:04-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_whileabc() {
    trimmingOf("while (a == b) { c(); { } }") //
        .using(new WhileToForUpdaters(), WhileStatement.class) //
        .gives("for(;a==b;c()){c();}") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("for(;a==b;c())c();") //
        .stays() //
    ;
  }
  @Test public void t21() {
    trimmingOf("for(i=1;b==q;++i){if(tipper==q()){int i;}}")//
        .gives("for(i=1;b==q;++i)if(tipper==q()){int i;}")//
        .gives("for(i=1;b==q;++i)if(tipper==q()){}")//
        .stays();
  }
  /** Introduced by Yossi on Sat-Mar-25-05:16:02-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void whileabc() {
    trimmingOf("while (a == b) { c(); { } }") //
        .using(new WhileToForUpdaters(), WhileStatement.class) //
        .gives("for(;a==b;c()){c();}") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("for(;a==b;c())c();") //
        .stays() //
    ;
  }
  @Test public void t23() {
    trimmingOf("for(i=1;b==q();++i){if(tipper==q()){int i;}}")//
        .gives("for(i=1;b==q();++i)if(tipper==q()){int i;}")//
        .gives("for(i=1;b==q();++i)if(tipper==q()){}")//
        .stays();
  }
  @Test public void t24() {
    trimmingOf("for(i=tipper();b==q;++i){if(tipper==q()){int i;}}")//
        .gives("for(i=tipper();b==q;++i)if(tipper==q()){int i;}")//
        .gives("for(i=tipper();b==q;++i)if(tipper==q()){}")//
        .stays();
  }
  @Test public void t25() {
    trimmingOf("for(i=4;b==q;f=i()){if(tipper==q()){int i;}}")//
        .gives("for(i=4;b==q;f=i())if(tipper==q()){int i;}")//
        .gives("for(i=4;b==q;f=i())if(tipper==q()){}")//
        .stays();
  }
}
