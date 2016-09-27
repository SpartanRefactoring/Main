package il.org.spartan.spartanizer.tippers;
import org.junit.*;


import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
/**
 * @author Dor Ma'ayan
 * @since 2016-09-26
 */

@SuppressWarnings({ "static-method", "javadoc" }) public class Issue251 {
  
  @Test public void t10() {
    trimmingOf("if(b){int i;}").gives("{}").gives("").stays();
  }
  
  @Test public void t20() {
    trimmingOf("if(b){int i;int b; b=i+1;g();}").gives("if(!b)return;int i;int b=i+1;b=i+1;g();");
  }
  
  @Test public void t30() {
    trimmingOf("if(b){int i;int j;int k;}").gives("{}").gives("").stays();
  }
  
  @Test public void t40() {
    trimmingOf("if(b){int i;int j;}else{int t;}").gives("{}").gives("").stays();
  }
 }