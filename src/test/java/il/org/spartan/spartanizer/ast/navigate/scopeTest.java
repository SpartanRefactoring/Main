package il.org.spartan.spartanizer.ast.navigate;

import java.io.*;

import org.junit.*;

/** TDD of {@link scope}
 * @author Yossi Gil
 * @since 2016-12-15 */
public class scopeTest extends ReflectionTester {
  /** This code is never used, it is to model our test */
  {
    if (hashCode() != hashCode()) // This should never happen
      try (FileReader f1 = new FileReader("a"); FileReader f2 = new FileReader("b")) {
        int c1 = f1.read();
        final int z = 2 * c1;
        int c2 = f2.read() + new Object() {
          int x, y;

          int sum(final int a, final int b) {
            return z + a + b + x + y;
          }

          @Override public int hashCode() {
            return sum(super.hashCode(), hashCode());
          }
        }.hashCode();
        c1 ^= c2;
        ++c1;
        final int c0 = c1 - c2;
        --c2;
        c2 ^= c1;
        int c3 = c1 + c2;
        ++c3;
        if (c1 == c2)
          throw new CloneNotSupportedException(c0 * c3 + "");
      } catch (final FileNotFoundException x) {
        for (int j = 0, ¢ = 0; ¢ < 10; j = 1, --j)
          ¢ += j;
        x.printStackTrace();
      } catch (final CloneNotSupportedException | IOException ¢) {
        ¢.printStackTrace();
      }
  }

  @Test public void a() {
    // System.err.println(initializer);
  }

  @Test public void b() {
    // System.err.println(myCompilationUnit());
  }
}
