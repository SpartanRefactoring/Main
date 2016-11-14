package il.org.spartan.spartanizer.ast.navigate;

import static org.junit.Assert.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

@SuppressWarnings({ "static-method", "javadoc" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class analyzeTest {
  @Test public void a() {
    final List<String> s = analyze.dependencies(wizard.ast("return x + y;")).stream().map(x -> x + "").collect(Collectors.toList());
    assertTrue(s.contains("x"));
    assertTrue(s.contains("y"));
    assertEquals(2, s.size());
  }

  @Test public void b() {
    final List<String> s = analyze.dependencies(wizard.ast("public void m(){return x + y;}")).stream().map(x -> x + "").collect(Collectors.toList());
    assertTrue(s.contains("x"));
    assertTrue(s.contains("y"));
    assertEquals(2, s.size());
  }

  @Test public void c() {
    final List<String> s = analyze.dependencies(wizard.ast("public void m(){a.b.c(x,y,\"g\");}")).stream().map(x -> x + "")
        .collect(Collectors.toList());
    assertTrue(s.contains("x"));
    assertTrue(s.contains("y"));
    assertTrue(s.contains("a"));
    assertTrue(s.contains("b"));
    assertTrue(s.contains("a.b"));
    assertEquals(5, s.size());
  }

  @Test public void testType0() {
    assertEquals(analyze.type(searchDescendants.forClass(VariableDeclaration.class).from(wizard.ast("public void m(){ int x; }")).get(0).getName()),
        "int");
  }

  @Test public void testType1() {
    assertEquals(analyze.type(searchDescendants.forClass(VariableDeclaration.class).from(wizard.ast(" public class A{ int x;} ")).get(0).getName()),
        "int");
  }
}
