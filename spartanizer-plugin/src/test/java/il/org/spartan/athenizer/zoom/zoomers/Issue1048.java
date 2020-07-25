package il.org.spartan.athenizer.zoom.zoomers;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.bloatingOf;

import org.junit.Test;

import il.org.spartan.athenizer.zoomers.AssignmentTernaryBloater;

/** Test case for bug in {@link AssignmentTernaryBloater} .
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-07 */
@SuppressWarnings("static-method")
public class Issue1048 {
  @Test public void test() {
    bloatingOf("public static InDeclaration instance() {instance = instance != null ? instance : new InDeclaration();return instance;}")
        .gives("public static InDeclaration instance(){if(instance==null) instance=new InDeclaration();return instance;}");
  }
}
