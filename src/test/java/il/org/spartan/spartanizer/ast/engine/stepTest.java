package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.azzert.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.engine.into.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** A test suite for class {@link step}
 * @author Yossi Gil
 * @since 2015-07-18
 * @see step */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class stepTest {
  @Test public void chainComparison() {
    azzert.that(right(i("a == true == b == c")) + "", is("c"));
    // TODO Auto-generated method stub
  }

  @Test public void imports() {
    final List<ImportDeclaration> li = step.importDeclarations(cu("import a.b.c; class c{}"));
    azzert.that(li.size(), is(1));
    // TODO Auto-generated method stub
    azzert.that(first(li).getName() + "", is("a.b.c"));
    // TODO Auto-generated method stub
  }

  @Test public void importsNames() {
    final List<String> li = step.importDeclarationsNames(cu("import a.b.c; class c{}"));
    azzert.that(li.size(), is(1));
    // TODO Auto-generated method stub
    azzert.that(first(li), is("a.b.c"));
    // TODO Auto-generated method stub
  }

  @Test public void importsNames2() {
    final List<String> li = step.importDeclarationsNames(cu("import a.b.c; import static f.g.*; import java.util.*; class c{}"));
    azzert.that(li.size(), is(3));
    // TODO Auto-generated method stub
    azzert.that(first(li), is("a.b.c"));
    // TODO Auto-generated method stub
    azzert.that(li.get(1), is("static f.g.*"));
    // TODO Auto-generated method stub
    azzert.that(li.get(2), is("java.util.*"));
    // TODO Auto-generated method stub
  }
}
