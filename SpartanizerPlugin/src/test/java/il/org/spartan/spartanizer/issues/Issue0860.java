package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Test case for {@link SwitchCaseLocalSort}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-09 */
@SuppressWarnings("static-method")
public class Issue0860 {
  @Test public void t1() {
    trimmingOf("switch(x){case 8:x=3;break;case 2:case 1:case 3:case 10:x=2;break;default:x=4;break;}")
        .using(new SwitchCaseLocalSort(), SwitchCase.class)
        .gives("switch(x){case 8:x=3;break;case 1:case 2:case 3:case 10:x=2;break;default:x=4;break;}")//
        .stays();
  }

  @Test public void t2() {
    trimmingOf("switch(x) {case 2:case 1:case 3:case 10:x = 2;break;}").using(new SwitchCaseLocalSort(), SwitchCase.class)
        .gives("switch(x) {case 1:case 2:case 3:case 10:x = 2;break;}");
  }

  @Test public void t3() {
    trimmingOf("switch(x) {case 'b':case 'c':case 'a':case 'd':x = 2;break;}").using(new SwitchCaseLocalSort(), SwitchCase.class)
        .gives("switch(x) {case 'b':case 'a':case 'c':case 'd':x = 2;break;}").using(new SwitchCaseLocalSort(), SwitchCase.class)
        .gives("switch(x) {case 'a':case 'b':case 'c':case 'd':x = 2;break;}");
  }

  @Test public void t4() {
    trimmingOf("switch(x) {case C5:case C2:case C9:case C1:case B:x = 2;break;}")//
        .using(new SwitchCaseLocalSort(), SwitchCase.class)//
        .gives("switch(x){case C2:case C5:case C1:case C9:case B:x=2;break;}") //
        .using(new SwitchCaseLocalSort(), SwitchCase.class)//
        .gives("switch(x){case C2:case C1:case C5:case B:case C9:x=2;break;}") //
        .using(new SwitchCaseLocalSort(), SwitchCase.class)//
        .gives("switch(x){case C1:case C2:case B:case C5:case C9:x=2;break;}") //
        .using(new SwitchCaseLocalSort(), SwitchCase.class)//
        .gives("switch(x){case C1:case B:case C2:case C5:case C9:x=2;break;}") //
        .using(new SwitchCaseLocalSort(), SwitchCase.class)//
        .gives("switch(x){case B:case C1:case C2:case C5:case C9:x=2;break;}") //
        .using(new SwitchCaseLocalSort(), SwitchCase.class)//
        .stays();
  }
}
