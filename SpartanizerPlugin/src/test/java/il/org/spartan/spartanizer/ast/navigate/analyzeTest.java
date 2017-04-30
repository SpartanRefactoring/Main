package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;

import static java.util.stream.Collectors.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import nano.ly.*;

/** TODO Vivian Shehadeh please add a description
 * @author Vivian Shehadeh
 * @author Ward Mattar
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class analyzeTest {
  private static final ASTNode AST = make.ast("public void m(int y){ y=5;}");

  @Test public void a() {
    final List<String> s = as.list(analyze.dependencies(make.ast("return x + y;")));
    assert s.contains("x");
    assert s.contains("y");
    azzert.that(s.size(), is(2));
  }

  @Test public void b() {
    final List<String> s = analyze.dependencies(make.ast("public void m(){return x + y;}")).stream().map(λ -> λ + "").collect(toList());
    assert s.contains("x");
    assert s.contains("y");
    azzert.that(s.size(), is(2));
  }

  @Test public void c() {
    final List<String> s = analyze.dependencies(make.ast("public void m(){a.b.c(x,y,\"g\");}")).stream().map(λ -> λ + "").collect(toList());
    assert s.contains("x");
    assert s.contains("y");
    assert s.contains("a");
    assert s.contains("b");
    assert s.contains("a.b");
    azzert.that(s.size(), is(5));
  }

  @Test public void findFirst() {
    azzert.that(find.first(VariableDeclaration.class).under(AST).getName(), iz("y"));
  }

  @Test public void findFirst1() {
    azzert.that(find//
        .first(SingleVariableDeclaration.class)//
        .under(make.ast("public void m(int y){ y=5;}")//
        ).getType(), iz("int")//
    );
  }

  @Test public void findFirst2() {
    azzert.that(find//
        .first(VariableDeclaration.class)//
        .under(make.ast("public void m(int y){ y=5;}")//
        ).getName(), //
        iz("y")//
    );
  }

  @Test public void testFindDeclarationInMethod0() {
    azzert.isNull(analyze.type(az.name(make.ast("x"))));
  }

  @Test public void testFindDeclarationInMethod1() {
    azzert.that("int", is(analyze
        .type(descendants.whoseClassIs(VariableDeclaration.class).from(make.ast("public class A{public void m(){ int x,y,z;} ")).get(1).getName())));
  }

  @Test public void testFindDeclarationInType0() {
    azzert.that("int", is(analyze.type(the.headOf(descendants.whoseClassIs(VariableDeclaration.class).from(AST)).getName())));
  }

  @Test public void testFindDeclarationInType1() {
    azzert.that("int", is(analyze.type(
        the.headOf(descendants.whoseClassIs(VariableDeclaration.class).from(make.ast("public class A{int x;public void m(){ x=5;}} "))).getName())));
  }

  @Test public void testFindDeclarationInType2() {
    azzert.that("int", is(analyze
        .type(the.headOf(descendants.whoseClassIs(VariableDeclaration.class).from(make.ast("public void m(int y){ int z = 5; }"))).getName())));
  }

  @Test public void testType0() {
    azzert.that("int",
        is(analyze.type(the.headOf(descendants.whoseClassIs(VariableDeclaration.class).from(make.ast("public void m(){ int x; }"))).getName())));
  }

  @Test public void testType1() {
    azzert.that("int",
        is(analyze.type(the.headOf(descendants.whoseClassIs(VariableDeclaration.class).from(make.ast(" public class A{ int x;} "))).getName())));
  }
}
