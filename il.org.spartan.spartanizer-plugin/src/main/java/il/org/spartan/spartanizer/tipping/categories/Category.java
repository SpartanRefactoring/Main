package il.org.spartan.spartanizer.tipping.categories;

import il.org.spartan.spartanizer.tipping.categories.Category.Theory.*;

/** Classification of tippers
 * @author Yossi Gil
 * @since Sep 28, 2016 */
public interface Category {
  default String description() {
    return Taxon.of(this).description();
  }
  default Class<? extends Category> lowestCategory() {
    Class<? extends Category> $ = Category.class;
    for (final Taxon ¢ : Taxa.hierarchy.nodes())
      if (¢.get().isInstance(this) && $.isAssignableFrom(¢.get()))
        $ = ¢.get();
    return $;
  }
  /** Returns the preference group to which the tipper belongs to. This method
   * should be overridden for each tipper and should return one of the values of
   * {@link Taxon}
   * @return preference group this tipper belongs to */
  default Taxon tipperGroup() {
    return Taxon.of(this);
  }

  interface Bloater extends Category {
    String ___ = "Make code as verbose as possible";
  }

  interface Collapse extends CommonFactorOut {
    String ___ = "Shorten code by merging two adjacent syntactical elements into one";
  }

  /** A specialized {@link Collapse} carried out, by factoring out some common
   * element */
  interface CommonFactorOut extends Java { // S2
    String ___ = "Factor out a common syntactical element";
  }

  interface Deadcode extends Theory.Logical {
    String ___ = "Eliminate code that is never executed";
  }

  interface EarlyReturn extends Java {
    String ___ = "Early return";
  }

  interface EmptyCycles extends Category {
    String ___ = "Eliminate code which seems whose functions look like CPU churn and nothing else";
  }

  interface Idiomatic extends Category {
    String ___ = "Change expression to a more familiar structure (often shorter)";
  }

  interface Inlining extends Java {
    String ___ = "Structural";
  }

  interface Loops extends Java {
    String ___ = "Spartan use of Java loop syntax";
  }

  interface Transofrmation extends Category {
    interface Collapse extends Transofrmation {
      String ___ = "Collapse two syntactical structures, such as declaration and subsequent initialization, into one";

      interface FactorOut extends Collapse {
        @SuppressWarnings("hiding") String ___ = "Collapse two syntactical elements into one by factoring common elements, as in the application of the distrubtive rule";
      }
    }

    interface Reshape extends Transofrmation {
      String ___ = "Reshape a syntactical element, such as a for loop, to a simpler or smaller element of the same syntactical kind";
    }

    interface Prune extends Transofrmation {
      interface Eliminate extends Prune {
        @SuppressWarnings("hiding") String ___ = "Eliminate a single distinguished syntatical element which does nothing, such as call to super() with no arguments";
      }

      String ___ = "Prune an operation which does nothing, e.g., adding zero or multiplying by one";
    }

    interface Remorph extends Transofrmation {
      String ___ = "Replace a syntactical elemnt such as while and if, with another, such as for loop and ternarry";
    }

    interface Sort extends Transofrmation {
      String ___ = "Place components in a syntatical elements in a specified order";
    }
  }

  interface Modular extends Category {
    String ___ = "Make modular changes to code";
  }

  interface Nanos extends Category {
    String ___ = "Nanos";
  }

  interface NOP extends Category {
    String ___ = "Eliminate an operation whose computation does nothing";

    interface onBooleans extends Logical {
      @SuppressWarnings("hiding") String ___ = "Eliminate an operation whose computation does nothing on booleans";
    }

    interface onNumbers extends Arithmetics.Symbolic {
      @SuppressWarnings("hiding") String ___ = "Eliminate an operation whose computation does nothing on numbers";
    }
  }

  interface ScopeReduction extends Java {
    String ___ = "Reduction of scope to the smallest possible";
  }

  interface Shortcircuit extends Java {
    String ___ = "Shortcut of control flow by combining unconditional sequencers, e.g., converting break into return";
  }

  /** Use alphabetical, or some other ordering, when order does not matter */
  interface Sorting extends Idiomatic {
    String ___ = "Sorting of Annotations and Modifiers";
  }

  interface SyntacticBaggage extends Java {
    String ___ = "Remove syntactical element that contributes nothing to semantics";
  }

  interface Ternarization extends CommonFactorOut {
    String ___ = "Convert conditional statement to the conditional, ?:, operator";
  }

  @SuppressWarnings("hiding")
  interface Theory extends Category {
    String ___ = "Simplifcation using a theory of some kind";

    interface Arithmetics extends Category.Theory {
      String ___ = "Rewrite an arithmetical expressions in a more canonical form";

      interface Numeric extends Arithmetics {
        String ___ = "Numeric simplfication of an arithmetical expression";
      }

      interface Symbolic extends Arithmetics {
        String ___ = "Symbolic simplfication of an arithmetical expression";
      }
    }

    interface Logical extends Category.Theory {
      String ___ = "Rewrite a boolean expression in a more canonical form";
    }

    interface Strings extends Category.Theory {
      String ___ = "Rewrite a string expression in a more canonical form";
    }
  }
}
