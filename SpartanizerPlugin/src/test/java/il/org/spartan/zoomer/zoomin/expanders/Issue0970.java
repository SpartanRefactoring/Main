package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

/** Test class for {@link MultiTypeCatchClause}
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class Issue0970 {
  @Test public void test0() {
    zoomingInto("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 | Type2 e){" + "int z;" + "z=2;" + "return z;" + "}")
        .gives("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 e){" + "int z;" + "z=2;" + "return z;" + "}" + "catch(Type2 e){" + "int z;"
            + "z=2;" + "return z;" + "}")
        .stays();
  }

  @Test public void test1() {
    zoomingInto("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 | Type2 e){" + "int z;" + "z=2;" + "return z;" + "}" + "finally {return t;}")
        .gives("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 e){" + "int z;" + "z=2;" + "return z;" + "}" + "catch(Type2 e){" + "int z;"
            + "z=2;" + "return z;" + "}finally {return t;}")
        .stays();
  }

  @Test public void test2() {
    zoomingInto(
        "try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 | Type2| Type3 e){" + "int z;" + "z=2;" + "return z;" + "}" + "finally {return t;}")
            .gives("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 e){" + "int z;" + "z=2;" + "return z;" + "}" + "catch(Type2 e){" + "int z;"
                + "z=2;" + "return z;" + "}" + "catch(Type3 e){" + "int z;" + "z=2;" + "return z;" + "}" + "finally {return t;}")
            .stays();
  }

  @Test public void test3() {
    zoomingInto("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch (Exception e){f();g();}" + "catch(Type1 | Type2 e){" + "int z;" + "z=2;"
        + "return z;" + "}" + "finally {return t;}")
            .gives("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch (Exception e){f();g();}" + "catch(Type1 e){" + "int z;" + "z=2;" + "return z;"
                + "}" + "catch(Type2 e){" + "int z;" + "z=2;" + "return z;" + "}finally {return t;}")
            .stays();
  }

  @Test public void test4() {
    zoomingInto("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 | Type2 e){" + "int z;" + "z=2;" + "return z;" + "}"
        + "catch (Exception e){f();g();}" + "finally {return t;}")
            .gives("try{int a;a=a+1;}catch(Type1 e){int z;z=2;return z;}catch(Type2 e)"
                + "{int z;z=2;return z;}catch(Exception e){f();g();}finally{return t;}")
            .stays();
  }

  @Test public void test5() {
    zoomingInto("try" + "{" + "int a;" + " a=a+1;" + "}" + "catch(Type1 | Type2 e){int z;}" + "catch(Type3 | Type4 e){int z;}")
        .gives("try{int a;a=a+1;}catch(Type1 e){int z;}catch(Type2 e){int z;}catch(Type3|Type4 e){int z;}")
        .gives("try{int a;a=a+1;}catch(Type1 e){int z;}catch(Type2 e){int z;}catch(Type3 e){int z;}catch(Type4 e){int z;}").stays();
  }
}
