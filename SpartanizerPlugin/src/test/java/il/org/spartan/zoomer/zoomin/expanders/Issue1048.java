package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** Test case for bug in {@link AssignmentTernaryBloater} .
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-07 */
@SuppressWarnings("static-method")
public class Issue1048 {
  @Test public void test() {
    bloatingOf("public static InDeclaration instance() {instance = instance != null ? instance : new InDeclaration();return instance;}")
        .gives("public static InDeclaration instance(){if(instance!=null)instance=instance;else instance=new InDeclaration();return instance;}");
  }
}
