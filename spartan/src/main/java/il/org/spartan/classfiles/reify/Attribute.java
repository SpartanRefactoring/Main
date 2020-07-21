package il.org.spartan.classfiles.reify;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

/** @author Yossi Gil
 * @since 28 November 2011 */
@Retention(RUNTIME)
public @interface Attribute {
  class Content {
    public final String name;
    public final String value;

    /** Instantiate {@link Content}.
     * @param value JD
     * @param name */
    public Content(final String name, final String value) {
      this.name = name;
      this.value = value;
    }
  }

  class Extractor {
    public static List<Content> attributes(final Object target) {
      final List<Content> $ = new ArrayList<>();
      for (final Method ¢ : target.getClass().getMethods())
        if (isAttribute(¢))
          $.add(new Content(¢.getName(), value(target, ¢)));
      return $;
    }
    private static boolean isAttribute(final Method ¢) {
      return ¢.getAnnotation(Attribute.class) != null;
    }
    private static String value(final Object target, final Method m) {
      try {
        return m.invoke(target) + "";
      } catch (final IllegalArgumentException $) {
        return "IllegalArgument: " + $.getMessage();
      } catch (final IllegalAccessException $) {
        return "IllegalAccess: " + $.getMessage();
      } catch (final InvocationTargetException $) {
        return "Exception in call: " + $.getMessage();
      }
    }
  }
}
