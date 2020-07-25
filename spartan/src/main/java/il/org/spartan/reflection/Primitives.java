package il.org.spartan.reflection;

import java.util.HashMap;
import java.util.Map;

/** @author Yossi Gil
 * @since 28/07/2007 */
public abstract class Primitives {
  public static final Map<String, Class<?>> value = new HashMap<>();
  static {
    value.put("byte", byte.class);
    value.put("short", short.class);
    value.put("int", int.class);
    value.put("long", long.class);
    value.put("float", float.class);
    value.put("double", double.class);
    value.put("char", char.class);
    value.put("boolean", boolean.class);
    value.put("void", void.class);
  }
}
