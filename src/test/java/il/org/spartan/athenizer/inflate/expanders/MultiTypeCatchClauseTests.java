package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

import org.junit.*;

/** Tets class for issue #970
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-25 */
@Ignore
@SuppressWarnings("static-method")
public class MultiTypeCatchClauseTests {
  @Test public void test0() {
    expandingOf("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 | Type2 e){" + "int z;" + "z=2;" + "return z;" + "}")
        .gives("try" + "{" + "int a;" + " a+=1;" + "}" + "catch(Type1 e){" + "int z;" + "z=2;" + "return z;" + "}" + "catch(Type2 e){" + "int z;"
            + "z=2;" + "return z;" + "}")
        .stays();
  }

  @Test public void test1() {
    expandingOf("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 | Type2 e){" + "int z;" + "z=2;" + "return z;" + "}" + "finally {return t;}")
        .gives("try" + "{" + "int a;" + " a+=1;" + "}" + "catch(Type1 e){" + "int z;" + "z=2;" + "return z;" + "}" + "catch(Type2 e){" + "int z;"
            + "z=2;" + "return z;" + "}finally {return t;}")
        .stays();
  }
  
  @Test public void test2() {
    expandingOf("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 | Type2| Type3 e){" + "int z;" + "z=2;" + "return z;" + "}" + "finally {return t;}")
        .gives("try" + "{" + "int a;" + " a+=1;" + "}" + "catch(Type1 e){" + "int z;" + "z=2;" + "return z;" + "}" + "catch(Type2 e){" + "int z;"
            + "z=2;" + "return z;" + "}"+"catch(Type3 e){" + "int z;" + "z=2;" + "return z;" + "}"+"finally {return t;}")
        .stays();
  }
}
