package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Ignore;
import org.junit.Test;

import il.org.spartan.spartanizer.tippers.SwitchBranchSort;

/** Test case for {@link SwitchBranchSort}
 * @author YuvalSimon {@code  yuvaltechnion@gmail.com}
 * @since 2017-01-14 */
@Ignore("Yuval Simon")
@SuppressWarnings("static-method")
public class Issue0861 {
  @Test public void t1() {
    trimmingOf("switch(x) {case a:x=2;y=1;z=3;break;case b:y=1;break;case c:y=2;throw new Exception();case d:y=3;continue;case e:y=1+x+y+z;break;")
        .gives("switch(x) {case b:y=1;break;case a:x=2;y=1;z=3;break;case c:y=2;throw new Exception();case d:y=3;continue;case e:y=1+x+y+z;break;")
        .gives("switch(x) {case b:y=1;break;case c:y=2;throw new Exception();case a:x=2;y=1;z=3;break;case d:y=3;continue;case e:y=1+x+y+z;break;")
        .gives("switch(x) {case c:y=2;throw new Exception();case b:y=1;break;case a:x=2;y=1;z=3;break;case d:y=3;continue;case e:y=1+x+y+z;break;")
        .gives("switch(x) {case c:y=2;throw new Exception();case b:y=1;break;case a:x=2;y=1;z=3;break;case e:y=1+x+y+z;break;case d:y=3;continue;")
        .stays();
  }
  @Test public void t2() {
    trimmingOf("switch(x) {default:y=4;break;case a:case b:x=7;break;case c:x=2;break;case d:y=9;case e:x=5;break;")
        .gives("switch(x) {case a:case b:x=7;break;default:y=4;break;case c:x=2;break;case d:y=9;case e:x=5;break;")
        .gives("switch(x) {case a:case b:x=7;break;case c:x=2;break;default:y=4;break;case d:y=9;case e:x=5;break;")
        .gives("switch(x) {case c:x=2;break;case a:case b:x=7;break;default:y=4;break;case d:y=9;case e:x=5;break;")
        .gives("switch(x) {case c:x=2;break;case a:case b:x=7;break;case d:y=9;case e:x=5;break;default:y=4;break;").stays();
  }
  @Test public void t3() {
    trimmingOf("switch (state) {case DONE:return false;case READY:return true;default:}").stays();
  }
}
