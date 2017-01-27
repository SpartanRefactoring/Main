package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

/** Tests {@link CountIf}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("static-method")
public class CountIfTest {
  @Test public void a() {
    trimmingOf("for (M ¢ : AA) if (!¢.isCtr()) ++a;")//
        .using(EnhancedForStatement.class, new CountIf(), new ForEachSuchThat())//
        .gives("a += AA.stream().filter(¢->!¢.isCtr()).count();")//
        .gives("a+=AA.stream().filter(λ->!λ.isCtr()).count();")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("for (M ¢ : AA.a() ? b : c) if (!¢.isCtr()) ++a;")//
        .using(EnhancedForStatement.class, new CountIf(), new ForEachSuchThat())//
        .gives("a += (AA.a() ? b : c).stream().filter(¢->!¢.isCtr()).count();")//
        .gives("a+=(AA.a() ? b : c).stream().filter(λ->!λ.isCtr()).count();")//
        .stays();
  }
}
