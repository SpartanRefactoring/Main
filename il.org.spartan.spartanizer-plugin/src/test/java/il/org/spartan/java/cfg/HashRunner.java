package il.org.spartan.java.cfg;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.builder.*;
import org.junit.runners.*;
import org.junit.runners.model.*;

/** TODO Roth: document class TODO Roth: move to appropriate package
 * @author Ori Roth
 * @since 2017-10-03 */
public class HashRunner extends BlockJUnit4ClassRunner {
  Object testClassInstance;
  final List<Field> focusedFields;
  int[] focusedFieldsHashes;

  public HashRunner(Class<?> c) throws InitializationError {
    super(c);
    this.focusedFields = Arrays.stream(c.getDeclaredFields()).filter(f -> f.isAnnotationPresent(Focus.class)).collect(Collectors.toList());
    this.focusedFieldsHashes = new int[focusedFields.size()];
  }
  @Override protected Object createTest() throws Exception {
    if (testClassInstance == null) {
      testClassInstance = super.createTest();
      computeHashesInto(testClassInstance, focusedFieldsHashes);
    }
    return testClassInstance;
  }
  @Override protected Statement methodInvoker(FrameworkMethod method, Object test) {
    return new Statement() {
      @Override public void evaluate() throws Throwable {
        method.invokeExplosively(test);
        int[] focusedFieldsHashesAfter = new int[focusedFields.size()];
        computeHashesInto(test, focusedFieldsHashesAfter);
        for (int i = 0; i < focusedFields.size(); ++i)
          if (focusedFieldsHashes[i] == focusedFieldsHashesAfter[i])
            throw new SameFocusedHash(method, focusedFields.get(i));
        focusedFieldsHashes = focusedFieldsHashesAfter;
      }
    };
  }
  void computeHashesInto(Object j, int[] fhs) throws IllegalArgumentException, IllegalAccessException {
    for (int i = 0; i < focusedFields.size(); ++i)
      fhs[i] = hash(focusedFields.get(i).get(j));
  }
  private static int hash(Object o) {
    return HashCodeBuilder.reflectionHashCode(17, 37, o, true, Object.class);
  }
}
