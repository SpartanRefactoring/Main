package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.zoomers.*;

// TODO Raviv: add @link to tested expander class (also in the opposite
// direction if not exists) and change test class name to Issue#
/** Unit Test for the ForBlock expander {@link ReturnTernaryExpander}, issue
 * #883 Also, Unit Test for the ForBlock expander
 * {@link AssignmentTernaryExpander}
 * @author Raviv Rachmiel
 * @author Dor Ma'ayan
 * @since 8-12-2016 */
@Ignore
@SuppressWarnings("static-method")
public class Issue0883 {
  @Test public void test0() {
    expansionOf("return a==0? 2:3;").gives("if(a==0)return 2;else return 3;");
  }

  @Test public void test1() {
    expansionOf("a = a==0? 2:3;").gives("if(a==0)a=2;else a=3;");
  }

  @Test public void test2() {
    expansionOf("a = a==0? (b==2? 4: 5 ):3;").gives("if(a==0)a=(b==2?4:5);else a=3;");
  }

  @Test public void test3() {
    expansionOf("a = (a==0? (b==2? 4: 5 ):3);").gives("if(a==0)a=(b==2?4:5);else a=3;");
  }

  @Test public void test4() {
    expansionOf("a = a==0? 1:2;").gives("if(a==0)a=1;else a=2;");
  }

  @Test public void test5() {
    expansionOf("a = b==0? (a==0? 1:2) : 4;").gives("if(b==0)a=(a==0?1:2);else a=4;");
  }
  // @Test public void test6() {
  // expansionOf("static Kind kind(final SimpleName ¢) { final ASTNode $ =
  // parent(¢);
  // switch ($.getNodeType()) {
  // case ASTNode.VARIABLE_DECLARATION_FRAGMENT:
  // return kind((VariableDeclarationFragment) $);
  // case ASTNode.SINGLE_VARIABLE_DECLARATION:
  // return kind((SingleVariableDeclaration) $);
  // case ASTNode.METHOD_DECLARATION:
  // return !parameters((MethodDeclaration) $).contains(¢) ? Kind.method :
  // Kind.parameter;
  // case ASTNode.TYPE_DECLARATION:
  // return !((TypeDeclaration) $).isInterface() ? Kind.class¢ :
  // Kind.interface¢;
  // case ASTNode.ENUM_CONSTANT_DECLARATION:
  // return Kind.enumConstant;
  // case ASTNode.ENUM_DECLARATION:
  // return Kind.enum¢;
  // case ASTNode.ANNOTATION_TYPE_DECLARATION:
  // return Kind.annotation;
  // case ASTNode.ANNOTATION_TYPE_MEMBER_DECLARATION:
  // return Kind.annotationMemberDeclaration;
  // default:
  // assert false : $.getClass().getSimpleName();
  // return null;
  // }
  // }")
  // }
}
