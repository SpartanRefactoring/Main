package il.org.spartan.spartanizer.java.namespace;

import java.util.Map;
import java.util.Set;

import org.eclipse.jface.text.Document;
import org.junit.Ignore;
import org.junit.Test;

import il.org.spartan.spartanizer.ast.factory.makeAST;

/** TODO Dan Greenstein please add a description
 * @author Dan Greenstein
 * @author Alex Kopzon
 * @since 2016 */
@Ignore
@SuppressWarnings({ "static-method", "unlikely-arg-type" })
public class EnvironmentTestUse {
  @Test public void useTestMethodDefinition() {
    Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = 5;").get()));
  }
  @Test public void useTestUsesAndDefinitions() {
    final Set<Map.Entry<String, Binding>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int i = 3; x.foo()").get()));
    assert $.contains("x");
    assert $.contains("i");
  }
  @Test public void useTestUsesAndDefinitions2() {
    final Set<Map.Entry<String, Binding>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("for(int i = 0; i <10; ++i)x+=i").get()));
    assert $.contains("x");
    assert $.contains("i");
  }
  @Test public void useTestUsesAndDefinitions3() {
    final Set<Map.Entry<String, Binding>> $ = Environment
        .uses(makeAST.COMPILATION_UNIT.from(new Document("x=3; try{y=13; foo(x,y);}catch(final UnsupportedOperationException e){z=3;}").get()));
    assert $.contains("x");
    assert $.contains("y");
    assert $.contains("z");
  }
  @Test public void useTestWithDefinitionsOnly() {
    assert Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = 5;").get())).contains("x");
  }
  @Test public void useTestWithDefinitionsOnly2() {
    final Set<Map.Entry<String, Binding>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = 5,y=3,z;").get()));
    assert $.contains("x");
    assert $.contains("y");
    assert $.contains("z");
  }
  @Test public void useTestWithDefinitionsOnly3() {
    final Set<Map.Entry<String, Binding>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = y = z =5;").get()));
    assert $.contains("x");
    assert $.contains("y");
    assert $.contains("z");
  }
  @Test public void useTestWithDefinitionsOnly4() {
    final Set<Map.Entry<String, Binding>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = y = z =5; double k;").get()));
    assert $.contains("x");
    assert $.contains("y");
    assert $.contains("z");
    assert $.contains("k");
  }
  @Test public void useTestWithUsesOnly() {
    final Set<Map.Entry<String, Binding>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("x=5; y=3.5").get()));
    assert $.contains("x");
    assert $.contains("y");
  }
  @Test public void useTestWithUsesOnly2() {
    assert Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("foo(x)").get())).contains("x");
  }
  @Test public void useTestWithUsesOnly3() {
    final Set<Map.Entry<String, Binding>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("foo(x,y)").get()));
    assert $.contains("x");
    assert $.contains("y");
  }
  @Test public void useTestWithUsesOnly4() {
    final Set<Map.Entry<String, Binding>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("foo(goo(q,x),hoo(x,y,z))").get()));
    assert $.contains("q");
    assert $.contains("x");
    assert $.contains("y");
    assert $.contains("z");
  }
  @Test public void useTestWithUsesOnly5() {
    assert Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("x.foo()").get())).contains("x");
  }
}
