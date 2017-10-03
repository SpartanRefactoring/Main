package il.org.spartan.java.cfg;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.builder.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;

/** TODO: document class
 * @author Ori Roth
 * @since 2017-10-03 */
public class HashRunner extends Runner {
  final Class<?> testclass;
  final List<Field> focused;
  Object testClassInstance;
  int[] focusedHashesBefore;
  int[] focusedHashesAfter;

  public HashRunner(Class<?> c) {
    this.testclass = c;
    this.focused = Arrays.stream(c.getDeclaredFields()).filter(f -> f.isAnnotationPresent(Focus.class)).collect(Collectors.toList());
    this.focusedHashesBefore = new int[focused.size()];
    this.focusedHashesAfter = new int[focused.size()];
  }
  @Override public Description getDescription() {
    return Description.createTestDescription(testclass, "Hash Runner");
  }
  @Override public void run(RunNotifier n) {
    testClassInstance = null;
    try {
      testClassInstance = testclass.newInstance();
      fillfields(testClassInstance, focusedHashesBefore);
    } catch (Exception x) {
      n.fireTestFailure(new Failure(Description.createTestDescription(testclass, "initialization failed"), x));
      return;
    }
    for (Method method : testclass.getMethods()) {
      if (method.isAnnotationPresent(Test.class)) {
        try {
          n.fireTestStarted(Description.createTestDescription(testclass, method.getName()));
          test(method);
          n.fireTestFinished(Description.createTestDescription(testclass, method.getName()));
        } catch (Exception x) {
          n.fireTestFailure(new Failure(Description.createTestDescription(testclass, method.getName()), x));
        }
      }
    }
  }
  private void fillfields(Object j, int[] fhs) throws IllegalArgumentException, IllegalAccessException {
    for (int i = 0; i < focused.size(); ++i)
      fhs[i] = hash(focused.get(i).get(j));
  }
  private void test(Method method) throws IllegalAccessException, InvocationTargetException, SameFocusedHash {
    method.invoke(testClassInstance);
    fillfields(testClassInstance, focusedHashesAfter);
    for (int i = 0; i < focused.size(); ++i)
      if (focusedHashesBefore[i] == focusedHashesAfter[i])
        throw new SameFocusedHash(method, i);
    focusedHashesBefore = focusedHashesAfter;
  }
  private static int hash(Object o) {
    return HashCodeBuilder.reflectionHashCode(17, 37, o, true, Object.class);
  }

  private class SameFocusedHash extends Exception {
    private static final long serialVersionUID = -9109817977086551314L;
    final int i;
    final Method m;

    public SameFocusedHash(Method m, int i) {
      this.i = i;
      this.m = m;
    }
    @Override public String getMessage() {
      return "field " + focused.get(i).getName() + " did not changed during the execution of " + m.getName();
    }
  }
}
