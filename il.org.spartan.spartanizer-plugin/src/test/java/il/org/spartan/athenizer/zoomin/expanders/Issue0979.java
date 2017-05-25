package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.athenizer.bloaters.*;

/** Unit test for {@link MethodDeclarationNameExpander} also Unit test for
 * {@link VariableDeclarationStatementExpand}
 * @author Raviv Rachmiel
 * @since 15-01-2017 */
@Ignore
@SuppressWarnings("static-method")
public class Issue0979 {
  @Test public void Assign2() {
    bloatingOf("double r;r = 2;")//
        .needRenaming(false).gives("double d1; d1 = 2;");
  }
  @Test public void basicAss() {
    bloatingOf("int a; a = 5;")//
        .needRenaming(false).gives("int i1; i1 = 5;");
  }
  @Test public void basicRenameShortVar() {
    bloatingOf("class a{void foo(int ¢){ ¢ = 1;}}")//
        .needRenaming(false).gives("class a{void foo(int i1){ i1=1;}}");
  }
  @Test public void basicRet() {
    bloatingOf("class a{int foo(int $){ return $;}}")//
        .needRenaming(false).gives("class a{int foo(int result){ return result;}}");
  }
  @Test public void basicRet2params() {
    bloatingOf("class a{int foo(int $, int i1){ return $;}}")//
        .needRenaming(false).gives("class a{int foo(int result, int i1){ return result;}}");
  }
  @Test public void ParamsRenameShortVar2() {
    bloatingOf("class a{void foo(double b,int a){ b = 1.1; a = 4;}}")//
        .needRenaming(false).gives("class a{void foo(double d1,int i1){ d1=1.1; i1 = 4;}}");
  }
  @Test public void ParamsRenameShortVar3() {
    bloatingOf("class a{void foo(double b,int a,String t){ b = 1.1; a = 4;}}")//
        .needRenaming(false).gives("class a{void foo(double d1,int i1,String s1){ d1=1.1; i1 = 4;}}");
  }
  @Test public void RenameShortVar2() {
    bloatingOf("class a{void foo(double b){ b = 1.1;}}")//
        .needRenaming(false).gives("class a{void foo(double d1){ d1=1.1;}}");
  }
  @Test public void twoOfSame() {
    bloatingOf("class a{void foo(int b, int a){ b = 1; a =3;}}")//
        .needRenaming(false).gives("class a{void foo(int i1, int i2){ i1=1; i2=3;}}");
  }
  @Test public void isNameSpaceBug() {
    bloatingOf("void foo(ASTNode input, ASTNode output) { ReportGenerator.write((n1, n2) -> (n1 - n2));}")//
        .needRenaming(false).gives("void foo(ASTNode n3, ASTNode n4) { ReportGenerator.write((n1, n2) -> (n1 - n2));}");
  }
  @Test public void isNameSpaceBug2() {
    bloatingOf("void foo() {A a1; a1 = new A() { void foo2(ASTNode input, ASTNode output) {} }; ReportGenerator.write((n1, n2) -> (n1 - n2));}")//
        .needRenaming(false)
        .gives("void foo() {A a1; a1 = new A() { void foo2(ASTNode n1, ASTNode n2) {} }; ReportGenerator.write((n1, n2) -> (n1 - n2));}");
  }
}
