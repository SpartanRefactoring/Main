package il.org.spartan.classfiles.reify;

import org.jetbrains.annotations.*;

/** @author Yossi Gil
 * @since 21 November 2011 */
public class InitializerInfo extends ExecutableEntity {
  public InitializerInfo(@NotNull final TypedEntity t) {
    super(t.constantPool, t.flags, "", TypeInfo.makeInitializer(t.type), t.descriptor, t.attributes);
  }
}
