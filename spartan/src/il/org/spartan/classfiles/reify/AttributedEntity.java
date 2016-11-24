package il.org.spartan.classfiles.reify;

import org.jetbrains.annotations.*;

/** @author Yossi Gil
 * @since 25 November 2011 */
public class AttributedEntity extends NamedEntity {
  protected final AttributeInfo[] attributes;

  public AttributedEntity(final AttributeInfo[] attributes, final String name) {
    super(name);
    this.attributes = attributes;
  }

  public final AttributeInfo findAttribute(@NotNull final String attributeName) {
    for (@NotNull final AttributeInfo $ : attributes)
      if (attributeName.equals($.name))
        return $;
    return null;
  }

  public final boolean hasNo(@NotNull final String attributeName) {
    return findAttribute(attributeName) == null;
  }
}
