package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Alex Kopzon
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue426 {
  @Test public void a() {
    trimmingOf("for(int i = 0; i <10; ++i){System.out.println(i);System.out.println(i);}")
        .gives("for(int ¢ = 0; ¢ <10; ++¢){System.out.println(¢);System.out.println(¢);}").stays();
  }

  @Test public void b() {
    trimmingOf("for(int i = 0; i <10; ++i){System.out.println(¢);System.out.println(i);}").stays();
  }
}
