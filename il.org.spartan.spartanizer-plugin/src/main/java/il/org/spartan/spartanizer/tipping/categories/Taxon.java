package il.org.spartan.spartanizer.tipping.categories;

import java.lang.reflect.*;
import java.util.*;

import fluent.ly.*;
import il.org.spartan.*;

/** Runtime reification of sub-interfaces of {@link Category}, it is a rewrite
 * of the defunct {@link TipperGroup}.
 * @author Yossi Gil
 * @since 2017-06-10 */
public final class Taxon extends Wrapper<Class<? extends Category>> implements Selfie<Taxon> {
  public static Taxon of(final Class<? extends Category> ¢) {
    Taxon ret = Taxon.instances.get(¢);
    if (ret != null)
      return ret;
    ret = new Taxon(¢);
    Taxon.instances.put(¢, ret);
    Taxa.hierarchy.add(ret);
    return ret;
  }

  public final String id;
  public final String label;
  private String description;
  public static Map<Class<? extends Category>, Taxon> instances = new HashMap<>();

  private Taxon(final Class<? extends Category> clazz) {
    super(clazz);
    id = clazz.getCanonicalName();
    label = clazz.getSimpleName();
  }
  /** [[SuppressWarningsSpartan]] */
  public String description() {
    return description = description != null ? description : anonymous.ly(() -> {
      try {
        for (final Field ¢ : get().getDeclaredFields())
          if (java.lang.reflect.Modifier.isStatic(¢.getModifiers()) && ¢.getType() == String.class)
            return (String) ¢.get(null);
      } catch (IllegalAccessException | IllegalArgumentException | SecurityException ¢) {
        note.bug(¢);
      }
      return English.name(get());
    });
  }
  public String label() {
    return label;
  }
  public Taxa parents() {
    return parents(get());
  }
  @SuppressWarnings("unchecked") public static Taxa parents(final Class<? extends Category> c) {
    final Taxa ret = new Taxa();
    for (final Class<?> x : c.getInterfaces())
      if (Taxa.isTaxon(x))
        ret.add(Taxon.of((Class<? extends Category>) x));
    return ret;
  }
  public static Taxon of(final Category ¢) {
    return Taxon.of(Taxa.isTaxon(¢.getClass()) ? ¢.getClass() : ¢.lowestCategory());
  }
}