package il.org.spartan.java.cfg;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/** TODO Roth: document class TODO Roth: move to appropriate package
 * @author Ori Roth
 * @since 2017-10-03 */
public class HashRunner extends BlockJUnit4ClassRunner {
  Object testClassInstance;
  final List<Field> focusedFields;
  List<Integer>[] focusedFieldsHashes;

  @SuppressWarnings("unchecked") public HashRunner(Class<?> c) throws InitializationError {
    super(c);
    this.focusedFields = Arrays.stream(c.getDeclaredFields()).filter(f -> f.isAnnotationPresent(Focus.class)).collect(Collectors.toList());
    this.focusedFieldsHashes = new List[focusedFields.size()];
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
        @SuppressWarnings("unchecked") List<Integer>[] focusedFieldsHashesAfter = new List[focusedFields.size()];
        computeHashesInto(test, focusedFieldsHashesAfter);
        for (int i = 0; i < focusedFields.size(); ++i)
          if (firstEquals(method, focusedFields.get(i), focusedFieldsHashes[i], focusedFieldsHashesAfter[i]) >= 0)
            throw new SameFocusedHash(method, focusedFields.get(i));
        focusedFieldsHashes = focusedFieldsHashesAfter;
      }
    };
  }
  void computeHashesInto(Object j, List<Integer>[] focusedFieldsHashes2) throws IllegalArgumentException, IllegalAccessException {
    for (int i = 0; i < focusedFields.size(); ++i) {
      final Field f = focusedFields.get(i);
      final Object o = f.get(j);
      if (!f.getAnnotation(Focus.class).wrapper())
        focusedFieldsHashes2[i] = a.singleton.list(Integer.valueOf(hash(o)));
      else {
        @SuppressWarnings("unchecked") Iterable<Object> t = (Iterable<Object>) o;
        focusedFieldsHashes2[i] = new LinkedList<>();
        for (Object x : t)
          focusedFieldsHashes2[i].add(Integer.valueOf(hash(x)));
      }
    }
  }
  private static int hash(Object o) {
    return HashCodeBuilder.reflectionHashCode(17, 37, o, true, Object.class);
  }
  static int firstEquals(FrameworkMethod method, Field f, List<Integer> l1, List<Integer> l2) throws FocusedWrapperSizeChanged {
    if (l1.size() != l2.size())
      throw new FocusedWrapperSizeChanged(method, f);
    for (int i = 0; i < l1.size(); ++i)
      if (l1.get(i).intValue() == l2.get(i).intValue())
        return i;
    return -1;
  }
}
