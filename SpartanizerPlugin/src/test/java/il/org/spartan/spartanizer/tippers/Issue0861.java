package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** Test case for {@link SwitchBranchSort}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-14 */
@SuppressWarnings("static-method")
public class Issue0861 {
  @Test public void t1() {
    trimmingOf(
        "switch(x) {case 1:x=2;y=1;z=3;break;case 2:y=1;break;case 3:y=2;throw new Exception();" + "case 4:y=3;continue;case 5:y=1+x+y+z;break;")
            .gives("switch(x) {case 2:y=1;break;case 1:x=2;y=1;z=3;break;case 3:y=2;throw new Exception();"
                + "case 4:y=3;continue;case 5:y=1+x+y+z;break;")
            .gives("switch(x) {case 2:y=1;break;case 3:y=2;throw new Exception();case 1:x=2;y=1;z=3;break;"
                + "case 4:y=3;continue;case 5:y=1+x+y+z;break;")
            .gives("switch(x) {case 3:y=2;throw new Exception();case 2:y=1;break;case 1:x=2;y=1;z=3;break;"
                + "case 4:y=3;continue;case 5:y=1+x+y+z;break;")
            .gives("switch(x) {case 3:y=2;throw new Exception();case 2:y=1;break;case 1:x=2;y=1;z=3;break;"
                + "case 5:y=1+x+y+z;break;case 4:y=3;continue;")
            .stays();
  }

  @Test public void t2() {
    trimmingOf("switch(x) {default:y=4;break;case 6:case 7:x=7;break;case 8:x=2;break;case 9:y=9;case 10:x=5;break;")
        .gives("switch(x) {case 6:case 7:x=7;break;default:y=4;break;case 8:x=2;break;" + "case 9:y=9;case 10:x=5;break;")
        .gives("switch(x) {case 6:case 7:x=7;break;case 8:x=2;break;default:y=4;break;" + "case 9:y=9;case 10:x=5;break;")
        .gives("switch(x) {case 8:x=2;break;case 6:case 7:x=7;break;default:y=4;break;" + "case 9:y=9;case 10:x=5;break;")
        .gives("switch(x) {case 8:x=2;break;case 6:case 7:x=7;break;case 9:y=9;case 10:x=5;break;" + "default:y=4;break;").stays();
  }
}
