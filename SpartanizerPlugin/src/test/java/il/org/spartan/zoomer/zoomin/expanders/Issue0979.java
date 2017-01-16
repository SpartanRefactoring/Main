package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** Unit test for {@link RenameShortNames}
 * @author Raviv Rachmiel
 * @since 15-01-2017 */
@SuppressWarnings("static-method")
public class Issue0979 {
  @Test public void basicRet() {
    bloatingOf("int foo(int $){ return $;}")//
        .gives("int foo(int ret){ return ret;}");
  }

  @Test public void basicRet2params() {
    bloatingOf("int foo(int $, int ba){ return $;}")//
        .gives("int foo(int ret, int ba){ return ret;}");
  }

  // TODO: dor maaya, Doron - should this name be final or will the name
  // generation change?
  @Test public void basicRenameShortVar() {
    bloatingOf("void foo(int b){ b = 1;}")//
        .gives("void foo(int i1){ i1=1;}");
  }
  
  @Test public void RenameShortVar2() {
    bloatingOf("void foo(double b){ b = 1.1;}")//
        .gives("void foo(double d1){ d1=1.1;}");
  }
  
  @Test public void ParamsRenameShortVar2() {
    bloatingOf("void foo(double b,int a){ b = 1.1; a = 4;}")//
        .gives("void foo(double d1,int i1){ d1=1.1; i1 = 4;}");
  }
  
  @Ignore
  @Test public void twoOfSame() {
    bloatingOf("void foo(int b, int a){ b = 1; a =3;}")// 
        .gives("void foo(int i1, int i2){ i1=1; i2=3;}");
  }
  
}
