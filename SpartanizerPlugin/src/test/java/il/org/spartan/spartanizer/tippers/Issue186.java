package il.org.spartan.spartanizer.tippers;

import org.junit.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

/** @author Dor Ma'ayan
 * @since 23-11-2016 */
@Ignore
@SuppressWarnings("static-method")
public class Issue186 {
  @Test public void test0() {
    trimmingOf("public void o(Object ¢) {" + "if (¢ == null)" + "System.out.println(\"null\");" + "else {" + "if (\"true\".equals(¢))"
        + " System.out.println(\"true\");" + "}" + "}")
            .gives("public void o(Object ¢) {" + "if (¢ == null)" + "System.out.println(\"null\");" + "else" + "if (\"true\".equals(¢))"
                + " System.out.println(\"true\");" + "}")
            .stays();
  }

  @Test public void test1() {
    trimmingOf("if(b==5){while(y==5)++i;}").gives("if(b==5)while(y==5)++i;").stays();
  }

  @Test public void test2() {
    trimmingOf("if(b==5){a+=5;}else{if(y==7){n+=1;}}").gives("if(b==5)a+=5;else {if(y==7)n+=1;}").gives("if(b==5)a+=5;else if(y==7)n+=1;").stays();
  }

  @Test public void test3() {
    trimmingOf("if(b==5){while(b)++i;}else{while(t)++p;}").gives("if(b==5)while(b)++i;else while(t)++p;").stays();
  }
}
