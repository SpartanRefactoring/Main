package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

import org.junit.*;

/** Tets class for issue #970
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-25 */
@Ignore
@SuppressWarnings("static-method")
public class Issue970 {
  @Test public void test0() {
    expansionOf("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 | Type2 e){" + "int z;" + "z=2;" + "return z;" + "}")
        .gives("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 e){" + "int z;" + "z=2;" + "return z;" + "}" + "catch(Type2 e){" + "int z;"
            + "z=2;" + "return z;" + "}")
        .stays();
  }

  @Test public void test1() {
    expansionOf("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 | Type2 e){" + "int z;" + "z=2;" + "return z;" + "}" + "finally {return t;}")
        .gives("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 e){" + "int z;" + "z=2;" + "return z;" + "}" + "catch(Type2 e){" + "int z;"
            + "z=2;" + "return z;" + "}finally {return t;}")
        .stays();
  }

  @Test public void test2() {
    expansionOf(
        "try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 | Type2| Type3 e){" + "int z;" + "z=2;" + "return z;" + "}" + "finally {return t;}")
            .gives("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 e){" + "int z;" + "z=2;" + "return z;" + "}" + "catch(Type2 e){" + "int z;"
                + "z=2;" + "return z;" + "}" + "catch(Type3 e){" + "int z;" + "z=2;" + "return z;" + "}" + "finally {return t;}")
            .stays();
  }

  @Test public void test3() {
    expansionOf("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch (Exception e){f();g();}" + "catch(Type1 | Type2 e){" + "int z;" + "z=2;"
        + "return z;" + "}" + "finally {return t;}")
            .gives("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch (Exception e){f();g();}" + "catch(Type1 e){" + "int z;" + "z=2;" + "return z;"
                + "}" + "catch(Type2 e){" + "int z;" + "z=2;" + "return z;" + "}finally {return t;}")
            .stays();
  }

  @Test public void test4() {
    expansionOf("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 | Type2 e){" + "int z;" + "z=2;" + "return z;" + "}"
        + "catch (Exception e){f();g();}" + "finally {return t;}")
            .gives("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch (Exception e){f();g();}" + "catch(Type1 e){" + "int z;" + "z=2;" + "return z;"
                + "}" + "catch(Type2 e){" + "int z;" + "z=2;" + "return z;" + "}" + "finally {return t;}")
            .stays();
  }

  @Test public void test5() {
    expansionOf("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 | Type2 e){int z;}" + "catch(Type3 | Type4 e){int z;}")
        .gives("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type3 | Type4 e){int z;}" + "catch(Type1 e){int z;}" + "catch(Type2 e){" + "int z;}")
        .gives("try{int a;a=a+1;}catch(Type1 e){int z;}catch(Type2 e){int z;}catch(Type3 e){int z;}catch(Type4 e){int z;}").stays();
  }
}
