package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** @author Shimon Azulay
 * @author Idan Atias
 * @since 16-11-3 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue741 {
  static TypeDeclaration getTypeDeclaration(final String td) {
    return findFirst.typeDeclaration(wizard.ast(td));
  }

  @Test public void publicFields_test0() {
    getAll2.publicFields(null);
    assert true;
  }

  @Test @SuppressWarnings("cast") public void publicFields_test1() {
    assert getAll2.publicFields(null) instanceof List<?>;
  }

  @Test public void publicFields_test2() {
    assert getAll2.publicFields(null) != null;
  }

  @Test public void publicFields_test3() {
    azzert.that(getAll2.publicFields(null).size(), is(0));
  }

  @Test public void publicFields_test4() {
    azzert.that(getAll2.publicFields(getTypeDeclaration("public class A {}")).size(), is(0));
  }

  @Test public void publicFields_test5() {
    azzert.that(getAll2.publicFields(getTypeDeclaration("public class A { private int x; protected String s; }")).size(), is(0));
  }

  @Test public void publicFields_test6() {
    final List<String> pFields = getAll2
        .publicFields(getTypeDeclaration("public class A { private int x; public static char y; public static void f(){}}"));
    assert pFields.contains("y");
    assert !pFields.contains("x");
    assert !pFields.contains("f");
  }

  @Test public void publicFields_test7() {
    final List<String> pFields = getAll2.publicFields(getTypeDeclaration("public class A { public int x; class B { public int y; } }"));
    assert pFields.contains("x");
    assert !pFields.contains("y");
  }

  @Test public void publicFields_test8() {
    final List<String> pFields = getAll2.publicFields(getTypeDeclaration("public class A { public class B { public int x; } }"));
    assert !pFields.contains("B");
    assert !pFields.contains("x");
  }
}
