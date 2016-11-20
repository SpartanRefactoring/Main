package il.org.spartan.spartanizer.ast.navigate;

import static org.junit.Assert.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** @author Vivian Shehadeh
 * @author Ward Mattar
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class analyzeTest {
  @Test public void a() {
    final List<String> s = analyze.dependencies(wizard.ast("return x + y;")).stream().map(x -> x + "").collect(Collectors.toList());
    assert s.contains("x");
    assert s.contains("y");
    assertEquals(2, s.size());
  }

  @Test public void b() {
    final List<String> s = analyze.dependencies(wizard.ast("public void m(){return x + y;}")).stream().map(x -> x + "").collect(Collectors.toList());
    assert s.contains("x");
    assert s.contains("y");
    assertEquals(2, s.size());
  }

  @Test public void c() {
    final List<String> s = analyze.dependencies(wizard.ast("public void m(){a.b.c(x,y,\"g\");}")).stream().map(x -> x + "")
        .collect(Collectors.toList());
    assert s.contains("x");
    assert s.contains("y");
    assert s.contains("a");
    assert s.contains("b");
    assert s.contains("a.b");
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

  @Test public void testEnviromentVariables() {
    assertNull(analyze.enviromentVariables((ASTNode) null));
  }

  @Test public void testFindDeclarationInType0() {
    assertEquals(analyze.type(searchDescendants.forClass(VariableDeclaration.class).from(wizard.ast("public void m(int y){ y=5;}")).get(0).getName()),
        "int");
  }

  @Test public void testFindDeclarationInType1() {
    assertEquals(
        analyze.type(
            searchDescendants.forClass(VariableDeclaration.class).from(wizard.ast("public class A{int x;public void m(){ x=5;}} ")).get(0).getName()),
        "int");
  }

  @Test public void testFindDeclarationInType2() {
    assertEquals(
        analyze.type(searchDescendants.forClass(VariableDeclaration.class).from(wizard.ast("public void m(int y){ int z = 5; }")).get(0).getName()),
        "int");
  }

  @Test public void testFindDeclarationInMethod0() {
    assertNull(analyze.type(az.name(wizard.ast("x"))));
  }

  @Test public void testFindDeclarationInMethod1() {
    assertEquals(
        analyze.type(
            searchDescendants.forClass(VariableDeclaration.class).from(wizard.ast("public class A{public void m(){ int x,y,z;} ")).get(1).getName()),
        "int");
  }
}
