package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link DeclarationInitializerStatementTerminatingScope}
 * @author Ori Roth
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue155Test {
  @Test public void InlineFinalIntoClassInstanceCreation() {
    trimming("" //
        + "void h(int x) {\n" //
        + "  ++x;\n" //
        + "  final int y = x;\n" //
        + "  new Object() {\n" //
        + "    @Override\n" //
        + "    public int hashCode() {\n" //
        + "      return y;\n" //
        + "    }\n" //
        + "  };\n" //
        + "}").stays();
  }
  
  @Test public void issue64a() {
    trimming("void f() {" + //
        "    final int a = f();\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + "}").stays();
  }
  
  @Test public void issue64b() {
    trimming("void f() {" + //
        "    final int a = 3;\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + "}").stays();
  }
  
  @Test public void issue64c() {
    trimming("void f(int x) {" + //
        "    ++x;\n" + //
        "    final int a = x;\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + "}").stays();
  }
}
