package il.org.spartan.spartanizer.research.metatester;

/** @author Oren Afek
 * @since 2017-04-12 **/
@SuppressWarnings("unused")
public interface TestClassGenerator {
  default Class<?> generate(final String testClassName) {
    return Object.class;
  }

  default Class<?> generate(final String testClassName, final String fileContent) {
    return Object.class;
  }
}
