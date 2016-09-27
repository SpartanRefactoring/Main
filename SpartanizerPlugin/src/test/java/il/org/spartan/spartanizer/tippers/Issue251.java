package il.org.spartan.spartanizer.tippers;
import org.junit.*;


import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
/**
 * @author Dor Ma'ayan
 * @since 2016-09-26
 */

@SuppressWarnings({ "static-method", "javadoc" }) public class Issue251 {
  
  @Test public void t01() {
    trimmingOf("if(b==true){int i;}").gives("{}").gives("").stays();
  }
  
  @Test public void t02() {
    trimmingOf("if(b){int i;int b; b=i+1;g();}").gives("if(!b)return;int i;int b=i+1;b=i+1;g();");
  }
  
  @Test public void t03() {
    trimmingOf("if(b){int i;int j;int k;}").gives("{}").gives("").stays();
  }
  
  @Test public void t04() {
    trimmingOf("if(b){int i;int j;}else{int t;}").gives("{}").gives("").stays();
  }
  
  @Test public void t05() {
    trimmingOf("if(b)g();").stays();
  }
  
  @Test public void t06() {
    trimmingOf("if(b()){int i;}").gives("if(b())int i;");
  }
  
  @Test public void t07() {
    trimmingOf("if(b){int i;int j;}else{g();}").gives("if(!b)g();").stays();
  }
  
  @Test public void t08() {
    trimmingOf("if(b){int i;int j;}else{g();int t;}").gives("if(!b){g();int t;}");
  }
  
  @Test public void t09() {
    trimmingOf("if(b){int i;int j;g();}else{int q;int t;}")//
    .gives("if(!b){int q;int t;}else{int i;int j;g();}")//
    .gives("if(b){int i;int j;g();}");
  }
  
  @Test public void t10() {
    trimmingOf("if(b==true){int i=5;}").gives("{}").gives("").stays();
  }
  
  @Test public void t11() {
    trimmingOf("if(b==true){int i=g();}").gives("if(b)int i=g();");
  }
  
  @Test public void t12() {
    trimmingOf("if(b==true){int i=5,q=g();}").gives("if(b)int i=5,q=g();");
  }
  
  @Test public void t13() {
    trimmingOf("if(b)"
        + "{int i"
        + ";int j;"
        + "if(s){"
        + "int q;"
        + "}"
        + "}else{int q;int t;}")//
    .gives("if(!b)"
        +"{int q;int t;}"
        + "else{int i"
        + ";int j;"
        + "if(s){"
        + "int q;"
        + "}}");
  }
  
  @Test public void t14() {
    trimmingOf("if(b)"
        + "{int i;"
        + "int j;"
        + "while(s){"
        + "int q;"
        + "}"
        + "}else{int q;int t;}")//
    .gives("if(!b)"
        +"{int q;int t;}"
        + "else{int i"
        + ";int j;"
        + "while(s){"
        + "int q;"
        + "}}");
  }
  
  @Test public void t15() {
    trimmingOf("if(b==q()){int i;}").gives("if(b==q())int i;");
  }
  
  @Test public void t16() {
    trimmingOf("while(b==q){int i;}").gives("{}");
  }
  
  @Test public void t17() {
    trimmingOf("while(b==q){if(t==q()){int i;}}").gives("while(b==q)if(t==q()){int i;}");
  }
  
  @Test public void t18() {
    trimmingOf("while(b==q){int i;double t; x=t+i;}").gives("for(;b==q;x=t+i){int i;double t;}");
  }
  
 }