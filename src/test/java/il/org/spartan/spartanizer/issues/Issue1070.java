package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Test case for {@link RemoveRedundantSwitchReturn} and
 * {@link RemoveRedundantSwitchContinue}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-15 */
@Ignore
@SuppressWarnings("static-method")
public class Issue1070 {
  @Test public void b1() {
    trimminKof("while(b) {z=1;switch(x) {case 1: y = 1; break;case 2: y = 2; break;case 3: default: case 5: continue;}}")
        .gives("while(b) {z=1;switch(x) {case 1: y = 1; break;case 2: y = 2; break;}}");
  }

  @Test public void b2() {
    trimminKof("while(b) {switch(x) {case 1: y = 1; break;case 2: y = 2; break;default:continue;}z=1;}").stays();
  }

  @Test public void b3() {
    trimminKof("while(b)switch(x) {case 1: y = 1; break;case 2: y = 2; break;case 3: default: case 5: continue;}")
        .gives("while(b)switch(x) {case 1: y = 1; break;case 2: y = 2; break;}");
  }

  @Test public void t1() {
    trimminKof("void apply(int x) {switch(y) {case 1: x=2; break;case 4: y=5; break;case 2: default: case 3: return;}}")
        .gives("void apply(int x) {switch(y) {case 1: x=2; break;case 4: y=5; break;}}");
  }

  @Test public void t2() {
    trimminKof("void apply(int x) {switch(y) {case 1: x=2; break;case 2: y=3; break;default: return;}z = 1;}").stays();
  }

  @Test public void t3() {
    trimminKof("int apply(int x) {switch(y) {case 1: x=2; break;case 4: y=5; break;default:return 1;}}").stays();
  }
}
