package il.org.spartan.classfiles.reify;

/** @author Yossi Gil
 * @since 21 November 2011 */
public class InitializerInfo extends ExecutableEntity {
  public InitializerInfo(final TypedEntity t) {
    super(t.constantPool, t.flags, "", TypeInfo.makeInitializer(t.type), t.descriptor, t.attributes);
  }
}
