package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jface.text.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.Environment.*;

/** @author Dan Greenstein
 * @author Alex Kopzon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) @Ignore public class EnvironmentTestUse {
  @Test public void useTestMethodDefinition() {
    Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = 5;").get()));
  }
  @Test public void useTestUsesAndDefinitions() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int i = 3; x.foo()").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("i"), is(true));
  }
  @Test public void useTestUsesAndDefinitions2() {
    final Set<Map.Entry<String, Information>> $ = Environment
        .uses(makeAST.COMPILATION_UNIT.from(new Document("for(int i = 0; i <10; ++i)x+=i").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("i"), is(true));
  }
  @Test public void useTestUsesAndDefinitions3() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(
        makeAST.COMPILATION_UNIT.from(new Document("x=3; try{y=13; foo(x,y);}" + "catch(final UnsupportedOperationException e)" + "{z=3;}").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
  }
  @Test public void useTestWithDefinitionsOnly() {
    azzert.that(Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = 5;").get())).contains("x"), is(true));
  }
  @Test public void useTestWithDefinitionsOnly2() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = 5,y=3,z;").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
  }
  @Test public void useTestWithDefinitionsOnly3() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = y = z =5;").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
  }
  @Test public void useTestWithDefinitionsOnly4() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = y = z =5; double k;").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
    azzert.that($.contains("k"), is(true));
  }
  @Test public void useTestWithUsesOnly() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("x=5; y=3.5").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
  }
  @Test public void useTestWithUsesOnly2() {
    azzert.that(Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("foo(x)").get())).contains("x"), is(true));
  }
  @Test public void useTestWithUsesOnly3() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("foo(x,y)").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
  }
  @Test public void useTestWithUsesOnly4() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("foo(goo(q,x),hoo(x,y,z))").get()));
    azzert.that($.contains("q"), is(true));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
  }
  @Test public void useTestWithUsesOnly5() {
    azzert.that(Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("x.foo()").get())).contains("x"), is(true));
  }
}
