package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link CountIf} and {@link While.CountIf}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class CountIfTest {
  @Test public void a() {
    trimmingOf("for (M λ : AA) if (!λ.isCtr()) ++a;")//
        .using(EnhancedForStatement.class, new CountIf(), new ForEachSuchThat())//
        .gives("a += (AA).stream().filter(λ->!λ.isCtr()).count();")//
        .gives("a += AA.stream().filter(λ->!λ.isCtr()).count();")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("for (M λ : AA.a() ? b : c) if (!λ.isCtr()) ++a;")//
        .using(EnhancedForStatement.class, new CountIf(), new ForEachSuchThat())//
        .gives("a += (AA.a() ? b : c).stream().filter(λ->!λ.isCtr()).count();")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("while (BlockFalling.canFall(w,i,j - 1,k) && j > 0) --j;")//
        .using(new While.CountIf(), WhileStatement.class)//
        .gives("j+=countWhile(()->BlockFalling.canFall(w,i,j-1,k)&&j>0);")//
        .stays();
  }
}
