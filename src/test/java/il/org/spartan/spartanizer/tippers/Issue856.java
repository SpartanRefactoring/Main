package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

/** Tests of inline into next statment even if not last in block
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue856 {
  @Test public void a() {
    trimmingOf(//
        "public static A a() {" + //
            " A b = \"one expression\";" + //
            " B.d(b);" + //
            " return \"and another\";" + //
            "}"//
    ).withTipper(VariableDeclarationFragment.class, new DeclarationFragmentInlineIntoNext())
        .gives(//
            "public static A a() {" + //
                " B.d(\"one expression\");" + //
                " return \"and another\";" + //
                "}"//
        ).stays();
  }

  @Test public void b() {
    trimmingOf(//
        "public static A foo() {" + //
            " A a = \"one expression\";" + //
            " C c = B.d(a);" + //
            " print(c);" + //
            " return \"and another\";" + //
            "}"//
    )//
        .withTipper(VariableDeclarationFragment.class, new DeclarationFragmentInlineIntoNext()) //
        .gives(//
            "public static A foo() {" + //
                " C c = B.d(\"one expression\");" + //
                " print(c);" + //
                " return \"and another\";" + //
                "}"//
        )//
        .withTipper(VariableDeclarationFragment.class, new DeclarationFragmentInlineIntoNext()) //
        .gives(//
            "public static A foo() {" + //
                " print(B.d(\"one expression\"));" + //
                " return \"and another\";" + //
                "}"//
        ).stays();
  }

  @Test public void c() {
    trimmingOf(//
        "public static A a() {" + //
            " A b = \"one expression\";" + //
            " B.d(b);" + //
            "print(b);" + //
            " return \"and another\";" + //
            "}"//
    ).withTipper(VariableDeclarationFragment.class, new DeclarationFragmentInlineIntoNext())//
        .stays();
  }

  @Test public void d() {
    trimmingOf("final List<Object> list = new ArrayList<>();" + "final int len = Array.getLength(defaultValue);" + "for (int ¢ = 0; ¢ <len; ++¢)"
        + "list.add(Array.get(defaultValue, ¢));" + "$.append(list);")//
            .stays();
  }

  @Test public void e() {
    trimmingOf("  final InflaterListener il = (InflaterListener) ((TypedListener) l).getEventListener();" + //
        "il.finalize(); " + //
        "return;").gives(//
            "((InflaterListener) ((TypedListener) l).getEventListener()).finalize();" + //
                "return;" //
    ).stays();
  }
}
