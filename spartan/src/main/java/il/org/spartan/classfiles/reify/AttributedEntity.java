package il.org.spartan.classfiles.reify;

/** @author Yossi Gil
 * @since 25 November 2011 */
public class AttributedEntity extends NamedEntity {
  protected final AttributeInfo[] attributes;

  public AttributedEntity(final AttributeInfo[] attributes, final String name) {
    super(name);
    this.attributes = attributes;
  }

  public final AttributeInfo findAttribute(final String attributeName) {
    for (final AttributeInfo $ : attributes)
      if (attributeName.equals($.name))
        return $;
    return null;
  }

  public final boolean hasNo(final String attributeName) {
    return findAttribute(attributeName) == null;
  }
}
