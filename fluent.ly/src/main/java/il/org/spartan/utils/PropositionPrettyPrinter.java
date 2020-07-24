package il.org.spartan.utils;

import java.util.function.BooleanSupplier;

import fluent.ly.English;
import il.org.spartan.utils.Proposition.Not;
import il.org.spartan.utils.Proposition.Singleton;
import il.org.spartan.utils.Proposition.Some;

public class PropositionPrettyPrinter {
  static class PropositionTreeTraversal {
    private final Listener<BooleanSupplier> listener;

    public PropositionTreeTraversal(final Listener<BooleanSupplier> listener) {
      this.listener = listener;
    }

    public void topDown(final BooleanSupplier ¢) {
      listener.in(¢);
      if (¢ instanceof Singleton)
        singleton((Singleton) ¢);
      else if (¢ instanceof Some)
        some((Some) ¢);
    }

    private void some(final Some ¢) {
      listener.down();
      ¢.stream().forEach(λ -> {
        topDown(λ);
        listener.next();
      });
      listener.up();
    }

    private void singleton(final Singleton ¢) {
      listener.down();
      if (¢.inner instanceof Proposition)
        topDown(¢.inner);
      listener.up();
    }
  }

  public interface Listener<T> {
    // @formatter:off
    default void down() {
      /**/}

    default void up() {
      /**/}

    default void next() {
      /**/}

    default void in(@SuppressWarnings("unused") final T __) {
      /**/}
    // @formatter:on
  }

  static class Number {
    private int number;
    private final Number base;

    public Number() {
      this(null);
    }

    public Number(final Number base) {
      this.base = base;
      number = 1;
    }

    public Number more() {
      return new Number(this);
    }

    public Number less() {
      return base;
    }

    public void next() {
      ++number;
    }

    @Override public String toString() {
      return (base == null ? "" : base + ".") + number;
    }
  }

  static class NumberWithTab {
    private final Tab tab;
    private Number number;

    public NumberWithTab() {
      tab = new Tab();
      number = new Number();
    }

    public void more() {
      tab.more();
      number = number.more();
    }

    public void less() {
      tab.less();
      number = number.less();
    }

    public void next() {
      number.next();
    }

    @Override public String toString() {
      return tab + "" + number + ") ";
    }
  }

  class NodePrettyPrinter implements Listener<BooleanSupplier> {
    private final NumberWithTab aligner;

    public NodePrettyPrinter() {
      aligner = new NumberWithTab();
    }

    @Override public void in(final BooleanSupplier ¢) {
      final StringBuilder sb = new StringBuilder(aligner + "");
      if (¢ instanceof Some || ¢ instanceof Not)
        sb.append("(" + English.selfName(¢.getClass()) + ")");
      if (!(¢ + "").contains(¢.getClass().getName()))
        sb.append(" " + ¢);
      sb.append(" ==> " + Truth.letterOf(¢));
      System.out.println(sb);
    }

    @Override public void down() {
      aligner.more();
    }

    @Override public void up() {
      aligner.less();
    }

    @Override public void next() {
      aligner.next();
    }
  }

  private final PropositionTreeTraversal traversal;

  public PropositionPrettyPrinter() {
    traversal = new PropositionTreeTraversal(new NodePrettyPrinter());
  }

  void present(final Proposition ¢) {
    traversal.topDown(¢);
  }

  public static void main(final String[] args) {
    final PropositionPrettyPrinter p = new PropositionPrettyPrinter();
    // example 1
    p.present( //
        Proposition.AND("MAIN", //
            Proposition.AND("SUB1", //
                Proposition.X, //
                Proposition.F), //
            Proposition.OR("SUB2", //
                Proposition.T, //
                Proposition.not(Proposition.N), //
                () -> true, //
                Proposition.that("OH NO", () -> {
                  throw new RuntimeException();
                }))));
    System.out.println();
    // example 2
    p.present( //
        Proposition.AND("MAIN", //
            Proposition.OR("SUB", Proposition.T, Proposition.F), //
            Proposition.T));
    System.out.println();
    // example 3
    p.present(Proposition.that("MAIN", Proposition.T));
    System.out.println();
    // example 4
    p.present( //
        Proposition.AND( //
            Proposition.OR("SUB", Proposition.T, Proposition.F), //
            Proposition.T));
    System.out.println();
    // example 5
    p.present( //
        Proposition.AND( //
            Proposition.F, //
            Proposition.X));
    System.out.println();
    // example 6
    p.present(Proposition.OR("SUB2", //
        Proposition.T, //
        Proposition.not(Proposition.not(Proposition.N)), //
        () -> true, //
        Proposition.that("OH NO", () -> {
          throw new RuntimeException();
        })));
    System.out.println();
  }
}
