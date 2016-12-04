package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Dor Ma'ayan
 * @since 2016-09-26 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue251 {
  @Test public void Issue302_test() {
    trimmingOf("if(b()){int i;}")//
        .gives("if(b()){}")
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
        .gives("if(!b)return;int i;int b=i+1;b=i+1;g();");
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
    .gives("if(b()){}");
    
  }

  @Test public void t07() {
    trimmingOf("if(b){int i;int j;}else{g();}")//
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
        .gives("if(b){int i=g();}")//
        .stays() //
    ;
  }

  @Test public void t12() {
    trimmingOf("if(b==true){int i=5,q=g();}")//
        .gives("if(b){int q=g();}");
  }

  @Test public void t13() {
    trimmingOf("if(b)" + "{int i" + ";int j;" + "if(s){" + "int q;" + "}" + "}else{int q;int tipper;}")//
        .gives("if(!b)" + "{int q;int tipper;}" + "else{int i" + ";int j;" + "if(s){" + "int q;" + "}}");
  }

  @Test public void t14() {
    trimmingOf("if(b)" + "{int i;" + "int j;" + "while(s){" + "int q;" + "}" + "}else{int q;int tipper;}")//
        .gives("if(!b)" + "{int q;int tipper;}" + "else{int i" + ";int j;" + "while(s){" + "int q;" + "}}");
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
    trimmingOf("while(b==q){if(tipper==q()){int i;}}")//
        .gives("while(b==q)if(tipper==q()){int i;}")//
        .gives("while(b==q)if(tipper==q()){}");//
  }

  @Test public void t19() {
    trimmingOf("while(b==q){g();if(tipper==q){int i;int j;}}")//
        .gives("while(b==q){g();{}}")//
        .gives("while(b==q)g();")//
        .stays();
  }

  @Test public void t21() {
    trimmingOf("for(i=1;b==q;++i){if(tipper==q()){int i;}}")//
        .gives("for(i=1;b==q;++i)if(tipper==q()){int i;}")//
        .gives("for(i=1;b==q;++i)if(tipper==q()){}");//
        
  }

  @Test public void t22() {
    trimmingOf("for(;b==q;){g();if(tipper==q){int i;int j;}}")//
        .gives("for(;b==q;){g();{}}")//
        .gives("for(;b==q;)g();")//
        .stays();
  }

  @Test public void t23() {
    trimmingOf("for(i=1;b==q();++i){if(tipper==q()){int i;}}")//
        .gives("for(i=1;b==q();++i)if(tipper==q()){int i;}")//
        .gives("for(i=1;b==q();++i)if(tipper==q()){}");//
        
  }

  @Test public void t24() {
    trimmingOf("for(i=tipper();b==q;++i){if(tipper==q()){int i;}}")//
        .gives("for(i=tipper();b==q;++i)if(tipper==q()){int i;}")//
        .gives("for(i=tipper();b==q;++i)if(tipper==q()){}");//
        
  }

  @Test public void t25() {
    trimmingOf("for(i=4;b==q;f=i()){if(tipper==q()){int i;}}")//
        .gives("for(i=4;b==q;f=i())if(tipper==q()){int i;}")//
        .gives("for(i=4;b==q;f=i())if(tipper==q()){}");//
  }
}