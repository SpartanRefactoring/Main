package il.org.spartan.sequence;

import org.eclipse.jdt.annotation.*;

public abstract class Sequence implements Cloneable {
  protected static final int MAX_VALUE = Integer.MAX_VALUE / 3;

   public static Sequence merge(final Sequence s1, final Sequence s2) {
    return new Merged(s1, s2);
  }

  protected final int threshold;
  protected int current;

  public Sequence() {
    this(MAX_VALUE);
  }
  public Sequence(final int threshold) {
    this.threshold = threshold;
  }
   public abstract Sequence advance();
   public final Sequence advanceTo(final int ¢) {
    while (current() < ¢ && more())
      advance();
    return this;
  }
  @Override  public final Sequence clone() {
    try {
      return (Sequence) super.clone();
    } catch ( final CloneNotSupportedException ¢) {
      throw new RuntimeException(¢);
    }
  }
  public final int count() {
    return clone().__count();
  }
  public final int count(final int from) {
    return clone().startAt(from).__count();
  }
  public final int current() {
    return current;
  }
  public final boolean more() {
    return current() <= threshold;
  }
   public abstract Sequence reset();
   public Sequence startAt(final int ¢) {
    return reset().advanceTo(¢);
  }
  public final int[] toArray() {
    return clone().fill(new int[count()]);
  }
  private int __count() {
    for (int $ = 0;; ++$) {
      if (!more())
        return $;
      advance();
    }
  }
  private int[] fill(final int[] $) {
    for (int ¢ = 0; more(); ++¢, advance())
      $[¢] = current();
    return $;
  }

  public static class Binary extends Sequence {
    public Binary(final int threshold) {
      super(threshold);
      current = 1;
    }
    @Override  public Sequence advance() {
      current += current;
      return this;
    }
    @Override  public Sequence reset() {
      current = 1;
      return this;
    }
  }

  private static class Merged extends Sequence {
    final Sequence s1;
    final Sequence s2;

    public Merged(final Sequence __s1, final Sequence __s2) {
      s1 = __s1;
      s2 = __s2;
      current = Math.min(s1.current(), s2.current());
    }
    @Override  public Sequence advance() {
      if (current < s1.current())
        s2.advance();
      else {
        s1.advance();
        if (current >= s2.current())
          s2.advance();
      }
      current = Math.min(s1.current(), s2.current());
      return this;
    }
    @Override  public Sequence reset() {
      s1.reset();
      s2.reset();
      return this;
    }
  }
}