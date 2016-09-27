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
  
 }