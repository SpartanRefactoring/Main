package il.org.spartan.classfiles.reify;

import org.eclipse.jdt.annotation.*;

/** @author Yossi Gil
 * @since 21 November 2011 */
public class ConstructorInfo extends ExecutableEntity {
  public ConstructorInfo( final TypedEntity t) {
    super(t.constantPool, t.flags, "", TypeInfo.makeConstructor(t.type), t.descriptor, t.attributes);
  }
}
