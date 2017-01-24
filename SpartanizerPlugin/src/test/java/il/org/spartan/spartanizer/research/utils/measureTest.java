package il.org.spartan.spartanizer.research.utils;

import static il.org.spartan.azzert.*;

import org.junit.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.research.util.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class measureTest {
  @Test public void a() {
    azzert.that(measure.statements(ast("public void foo(){}")), is(0));
  }

  @Test public void b() {
    azzert.that(measure.statements(ast("public void foo(){use();}")), is(1));
  }
}
