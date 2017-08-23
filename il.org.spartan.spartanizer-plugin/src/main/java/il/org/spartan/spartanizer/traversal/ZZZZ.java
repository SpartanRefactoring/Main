package il.org.spartan.spartanizer.traversal;

import java.lang.annotation.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-07-17 */
public interface ZZZZ {
  interface ID extends ZZZZ {}

  @Target({ ElementType.TYPE_PARAMETER, ElementType.TYPE_USE, ElementType.TYPE })
  @interface All {
  }

  @Target({ ElementType.TYPE_PARAMETER, ElementType.TYPE_USE, ElementType.TYPE })
  @interface Only {
    Class<?>[] value();
  }

  /** A POJO including all parameters of a function.
   * @author Yossi Gil
   * @since 2017-07-25 */
  @Target(ElementType.TYPE)
  @interface Operation {
  }

  @Operation
  interface f {
    default int id() {
      return name().hashCode();
    }
    default String name() {
      return id() + "";
    }
    
    default void value() {
        System.out.println(id());
    	System.out.println(name());
    }
  }
}
