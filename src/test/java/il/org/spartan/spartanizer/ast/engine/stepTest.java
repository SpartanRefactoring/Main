package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/**
 * A test suite for class  {@link step}
 * @author  Yossi Gil
 * @since  2015-07-18
 * @see step  
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class stepTest {
  @Test public void chainComparison() {
    assertEquals("c", right(i("a == true == b == c")) + "");
  }

  @Test public void imports() {
    final List<ImportDeclaration> li = step.importDeclarations(cu("import a.b.c; class c{}"));
    assertEquals(1, li.size());
    assertEquals("a.b.c", li.get(0).getName() + "");
  }

  @Test public void importsNames() {
    final List<String> li = step.importDeclarationsNames(cu("import a.b.c; class c{}"));
    assertEquals(1, li.size());
    assertEquals("a.b.c", li.get(0));
  }

  @Test public void importsNames2() {
    final List<String> li = step.importDeclarationsNames(cu("import a.b.c; import static f.g.*; import java.util.*; class c{}"));
    assertEquals(3, li.size());
    assertEquals("a.b.c", li.get(0));
    assertEquals("static f.g.*", li.get(1));
    assertEquals("java.util.*", li.get(2));
  }
}
