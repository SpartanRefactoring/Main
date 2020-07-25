package il.org.spartan.spartanizer.tipping.categories;

import java.util.Set;

import il.org.spartan.spartanizer.plugin.Builder;
import il.org.spartan.spartanizer.traversal.Toolboxes;

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
      if (¢.get().isInstance(this) && ¢.get() != getClass() && $.isAssignableFrom(¢.get()))
        $ = ¢.get();
    return $;
  }
  /** Returns the preference group to which the tipper belongs to. This method
   * should be overridden for each tipper and should return one of the values of
   * {@link Taxon}
   * @return preference group this tipper belongs to */
  default Taxon tipperGroup() {
    return Taxon.of(lowestCategory());
  }

  interface Bloater extends Category {
    String ___ = "Make code as verbose as possible";
  }

  interface Junit extends Category {
    String ___ = "Transformations for JunitTests";
  }

  interface Collapse extends CommonFactorOut {
    String ___ = "Shorten code by merging two adjacent syntactical elements into one";
  }

  /** A specialized {@link Collapse} carried out, by factoring out some common
   * element */
  interface CommonFactorOut extends Theory.Java { // S2
    String ___ = "Factor out a common syntactical element";
  }

  interface Deadcode extends Theory.Logical {
    String ___ = "Eliminate code that is never executed";
  }

  interface EarlyReturn extends Theory.Java {
    String ___ = "Early return";
  }

  interface EmptyCycles extends Category {
    String ___ = "Eliminate code which seems whose functions look like CPU churn and nothing else";
  }

  interface Idiomatic extends Category {
    String ___ = "Change expression to a more familiar structure (often shorter)";
  }

  interface Inlining extends Theory.Java {
    String ___ = "Structural";
  }

  interface Loops extends Theory.Java {
    String ___ = "Spartan use of Java loop syntax";
  }

  interface Transformation extends Category {
    interface Reshape extends Transformation {
      String ___ = "Reshape a syntactical element, such as a for loop, to a simpler or smaller element of the same syntactical kind";
    }

    interface Prune extends Transformation {
      interface Eliminate extends Prune {
        @SuppressWarnings("hiding") String ___ = "Eliminate a single distinguished syntatical element which does nothing, such as call to super() with no arguments";
      }

      String ___ = "Prune an operation which does nothing, e.g., adding zero or multiplying by one";
    }

    interface Remorph extends Transformation {
      String ___ = "Replace a syntactical elemnt such as 'while' and 'if', with another, such as for loop and ternarry";
    }

    /** Use alphabetical, or some other ordering, when order does not matter */
    interface Sort extends Transformation {
      String ___ = "Sorting of annotations, modifiers, commutative elements, etc";
    }
  }

  interface Modular extends Category {
    String ___ = "Make modular changes to code";
  }

  interface Nanos extends Category {
    String ___ = "Nanos";
  }

  interface ScopeReduction extends Theory.Java {
    String ___ = "Reduction of scope to the smallest possible";
  }

  interface Shortcircuit extends Theory.Java {
    String ___ = "Shortcut of control flow by combining unconditional sequencers, e.g., converting break into return";
  }

  interface SyntacticBaggage extends Theory.Java {
    String ___ = "Remove syntactical element that contributes nothing to semantics";
  }

  interface Ternarization extends CommonFactorOut {
    String ___ = "Convert conditional statement to the conditional, ?:, operator";
  }

  interface Theory extends Category {
    interface Java extends Category.Theory {
      @SuppressWarnings("hiding") String ___ = "General Java simplifcations";
    }

    String ___ = "Simplifcation using a theory of some kind";

    interface Arithmetics extends Category.Theory {
      @SuppressWarnings("hiding") String ___ = "Rewrite an arithmetical expressions in a more canonical form";

      interface Numeric extends Arithmetics {
        @SuppressWarnings("hiding") String ___ = "Numeric simplfication of an arithmetical expression";
      }

      interface Symbolic extends Arithmetics {
        @SuppressWarnings("hiding") String ___ = "Symbolic simplfication of an arithmetical expression";
      }
    }

    interface Logical extends Category.Theory {
      @SuppressWarnings("hiding") String ___ = "Rewrite a boolean expression in a more canonical form";
    }

    interface Strings extends Category.Theory {
      @SuppressWarnings("hiding") String ___ = "Rewrite a string expression in a more canonical form";
    }
  }

  /** Generate marker declarations for plugin.xml. */
  static void main(String[] args) {
    Set<Taxon> seen = an.empty.set();
    System.out.println("<!-- Marker types -->");
    System.out.println("<extension\n    id=\"" + Builder.MARKER_TYPE + "\"\n" //
        + "    name=\"Spartanize\"\n" //
        + "    point=\"org.eclipse.core.resources.markers\">\n" //
        + "  <super type=\"org.eclipse.core.resources.problemmarker\"/>\n" //
        + "</extension>");
    for (final Taxon ¢ : Toolboxes.categoryMap.values()) {
      if (seen.contains(¢))
        continue;
      seen.add(¢);
      System.out.println("<extension\n    id=\"" + Builder.MARKER_TYPE + "." + ¢.label() + "\"\n" //
          + "    name=\"" + ¢.label() + "\"\n" //
          + "    point=\"org.eclipse.core.resources.markers\">\n" //
          + "  <super type=\"" + Builder.MARKER_TYPE + "\"/>\n" //
          + "</extension>");
    }
    System.out.println("<!-- Marker resolution: this is where the quick fixg menus gets bound to the\n"
        + "     Java class which generates several quick fix for tips. -->");
    System.out.println(
        "<extension point=\"org.eclipse.ui.ide.markerResolution\">\n  <markerResolutionGenerator\n      markerType=\"" + Builder.MARKER_TYPE + "\"\n" //
            + "      class=\"il.org.spartan.spartanizer.plugin.QuickFixer\"\n" //
            + "  />\n" //
            + "</extension>");
    seen.clear();
    for (final Taxon ¢ : Toolboxes.categoryMap.values()) {
      if (seen.contains(¢))
        continue;
      seen.add(¢);
      System.out.println("<extension point=\"org.eclipse.ui.ide.markerResolution\">\n  <markerResolutionGenerator\n      markerType=\""
          + Builder.MARKER_TYPE + "." + ¢.label() + "\"\n" //
          // TODO Roth: reference qf id
          + "      class=\"il.org.spartan.spartanizer.plugin.QuickFixer\"\n" //
          + "  />\n" //
          + "</extension>");
    }
  }
}
