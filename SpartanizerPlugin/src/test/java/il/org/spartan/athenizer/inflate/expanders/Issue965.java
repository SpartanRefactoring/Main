package il.org.spartan.athenizer.inflate.expanders;

import org.junit.*;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

/** Test class for issue #965
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-20 */
@Ignore
@SuppressWarnings("static-method")
public class Issue965 {
  @Test public void testBinding0() {
    expansionOf((new Issue965Aux()).getCU()).givesWithBinding(
        "package il.org.spartan.athenizer.inflate.expanders;import java.util.*;import org.eclipse.jdt.core.dom.*;import il.org.spartan.spartanizer.ast.navigate.*;"
            + "public class Issue965Aux extends ReflectiveTester" + "{" + "  @SuppressWarnings({ \"static-method\", \"unused\" })"
            + "public void check1()" + "{" + "List<Integer> lst =new ArrayList<>();" + "String s = lst.toString();" + "}"
            + "public ASTNode getFirstMethod()" + "{" + "return find(MethodDeclaration.class);" + "}"
            + "public ASTNode getCU(){return myCompilationUnit();}}");
  }

  @Test public void test0() {
    expansionOf("a+\"\"").stays(); // no binding for now...
  }

  @Test public void test1() {
    expansionOf("\"\"+t").stays(); // no binding for now...
  }

  @Test public void test2() {
    expansionOf("\"abcd\"+t()").stays();
  }

  @Test public void test4() {
    expansionOf("true+\"\"").stays();
  }
}
