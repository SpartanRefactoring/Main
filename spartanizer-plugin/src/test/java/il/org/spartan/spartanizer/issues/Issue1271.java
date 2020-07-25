package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.included;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.Assignment;
import org.junit.Test;

import il.org.spartan.spartanizer.tippers.AssignmentAndAssignmentOfSameValue;

/** Unit tests of {@link AssignmentAndAssignmentOfSameValue}
 * @author Yossi Gil
 * @since 2017-04-21 */
@SuppressWarnings("static-method")
public class Issue1271 {
  @Test public void a() {
    trimmingOf("a=13;b=13;")//
        .gives("b=a=13;");
  }
  @Test public void assignmentAssignmentChain1() {
    trimmingOf("c=a=13;b=13;")//
        .gives("b=c=a=13;");
  }
  @Test public void assignmentAssignmentChain2() {
    trimmingOf("a=13;b=c=13;")//
        .gives("b=c=a=13;");
  }
  @Test public void assignmentAssignmentChain3() {
    trimmingOf("a=b=13;c=d=13;")//
        .gives("c=d=a=b=13;");
  }
  @Test public void assignmentAssignmentChain4() {
    trimmingOf("a1=a2=a3=a4=13;b1=b2=b3=b4=b5=13;")//
        .gives("b1=b2=b3=b4=b5=a1=a2=a3=a4=13;");
  }
  @Test public void assignmentAssignmentChain5() {
    trimmingOf("a1=(a2=(a3=(a4=13)));b1=b2=b3=((((b4=(b5=13)))));")//
        .gives("a1=(a2=(a3=(a4=13)));b1=b2=b3=(((b4=(b5=13))));") //
        .gives("a1=(a2=(a3=(a4=13)));b1=b2=b3=((b4=(b5=13)));") //
        .gives("a1=(a2=(a3=(a4=13)));b1=b2=b3=(b4=(b5=13));") //
        .stays()//
    ;
  }
  @Test public void assignmentAssignmentNew() {
    trimmingOf("a=new B();b=new B();")//
        .stays();
  }
  @Test public void assignmentAssignmentNewArray() {
    trimmingOf("a=new A[3];b=new A[3];")//
        .stays();
  }
  @Test public void assignmentAssignmentNull() {
    trimmingOf("c=a=null;b=null;")//
        .stays();
  }
  @Test public void assignmentAssignmentSideEffect() {
    trimmingOf("a=f();b=f();")//
        .stays();
  }
  @Test public void assignmentAssignmentVanilla() {
    trimmingOf("a=13;b=13;")//
        .gives("b=a=13;");
  }
  @Test public void assignmentAssignmentVanilla0() {
    trimmingOf("a=0;b=0;")//
        .gives("b=a=0;");
  }
  @Test public void assignmentAssignmentVanillaScopeIncludes() {
    included("a=3;b=3;", Assignment.class).in(new AssignmentAndAssignmentOfSameValue());
  }
  @Test public void assignmentAssignmentVanillaScopeIncludesNull() {
    included("a=null;b=null;", Assignment.class).notIn(new AssignmentAndAssignmentOfSameValue());
  }
}
