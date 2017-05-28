package il.org.spartan.athenizer.zoomin.expanders;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.athenizer.bloaters.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tipping.*;

/** Unit test for {@link MethodDeclarationNameExpander} also Unit test for
 * {@link VariableDeclarationStatementExpand}
 * @author Raviv Rachmiel
 * @since 15-01-2017 */
public class Issue0979 extends BloaterTest<MethodDeclaration> {
  @Test public void Assign2() {
    bloatingOf("double r;r = 2;").stays();
  }
  @Test public void basicAss() {
    bloatingOf("int a; a = 5;").stays();
  }
  @Test public void basicRenameShortVar() {
    bloatingOf("class a{void foo(int ¢){ ¢ = 1;}}")//
        .gives("class a{void foo(int int0){ int0=1;}}");
  }
  @Test public void basicRet() {
    bloatingOf("class a{int foo(int $){ return $;}}")//
        .gives("class a{int foo(int int0){ return int0;}}");
  }
  @Test public void basicRet2params() {
    bloatingOf("class a{int foo(int $, int int0){ return $;}}")//
        .gives("class a{int foo(int int1, int int0){ return int1;}}");
  }
  @Test public void ParamsRenameShortVar2() {
    bloatingOf("class a{void foo(double b,int a){ b = 1.1; a = 4;}}")//
        .gives("class a{void foo(double double0,int int1){ double0=1.1; int1 = 4;}}");
  }
  @Test public void ParamsRenameShortVar3() {
    bloatingOf("class a{void foo(double b,int a,String t){ b = 1.1; a = 4;}}")//
        .gives("class a{void foo(double double0,int int1,String string2){ double0=1.1; int1 = 4;}}");
  }
  @Test public void RenameShortVar2() {
    bloatingOf("class a{void foo(double doubled1){ b = 1.1;}}").stays();//
  }
  @Test public void twoOfSame() {
    bloatingOf("class a{void foo(int b, int a){ b = 1; a =3;}}")//
        .gives("class a{void foo(int int0, int int1){ int0=1; int1=3;}}");
  }
  @Test public void isNameSpaceBug() {
    bloatingOf("void foo(ASTNode input, ASTNode output) { ReportGenerator.write((n1, n2) -> (n1 - n2));}").stays();
  }
  @Test public void isNameSpaceBug2() {
    bloatingOf("void foo() {A a1; a1 = new A() { void foo2(ASTNode input, ASTNode output) {} }; ReportGenerator.write((n1, n2) -> (n1 - n2));}")//
        .stays();
  }
  @Override public Tipper<MethodDeclaration> bloater() {
    return new MethodDeclarationNameExpander();
  }
  @Override public Class<MethodDeclaration> tipsOn() {
    return MethodDeclaration.class;
  }
}
