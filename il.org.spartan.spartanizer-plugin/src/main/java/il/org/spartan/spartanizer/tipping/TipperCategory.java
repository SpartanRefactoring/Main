package il.org.spartan.spartanizer.tipping;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.plugin.preferences.revision.PreferencesResources.*;
import il.org.spartan.spartanizer.tipping.TipperCategory.Theory.*;
import il.org.spartan.spartanizer.traversal.*;

/** Classification of tippers
 * @author Yossi Gil
 * @since Sep 28, 2016 */
public interface TipperCategory {
  Map<Class<? extends TipperCategory>, List<Class<? extends TipperCategory>>> children = anonymous.ly(() -> {
    final Map<Class<? extends TipperCategory>, List<Class<? extends TipperCategory>>> $ = new HashMap<>();
    Configurations.allTippers().map(λ -> categories(λ)).flatMap(x -> x.stream()).forEach(x -> $.put(x, an.empty.list()));
    $.put(Nominal.class, Arrays.asList(Nominal.Abbreviation.class, Nominal.Anonymization.class, Nominal.Result.class));
    $.put(Structural.class, Arrays.asList(Collapse.class, Loops.class, Deadcode.class, EarlyReturn.class, NOP.class, ScopeReduction.class,
        Shortcircuit.class, SyntacticBaggage.class));
    $.put(CommonFactorOut.class, Arrays.asList(Ternarization.class));
    $.put(Idiomatic.class, Arrays.asList(Sorting.class));
    $.put(NOP.class, Arrays.asList(NOP.onBooleans.class, NOP.onNumbers.class, Theory.Strings.class));
    $.put(Collapse.class, Arrays.asList(CommonFactorOut.class));
    return $;
  });
  Map<Class<? extends TipperCategory>, Class<? extends TipperCategory>> reversedHierarchy = anonymous.ly(() -> {
    final Map<Class<? extends TipperCategory>, Class<? extends TipperCategory>> $ = new HashMap<>();
    for (final Entry<Class<? extends TipperCategory>, List<Class<? extends TipperCategory>>> e : children.entrySet())
      for (final Class<? extends TipperCategory> ¢ : e.getValue())
        $.put(¢, e.getKey());
    return $;
  });
  Map<Class<? extends TipperCategory>, String> descriptions = anonymous.ly(() -> {
    final Map<Class<? extends TipperCategory>, String> $ = new HashMap<>();
    for (final Class<? extends TipperCategory> c : children.keySet())
      try {
        for (Field ¢ : c.getDeclaredFields())
          if (¢.getType() == String.class)
            $.put(c, (String) ¢.get(null));
      } catch (IllegalAccessException | IllegalArgumentException | SecurityException ¢) {
        note.bug(¢);
      }
    return $;
  });

  default String description() {
    return descriptions.get(lowestCategory());
  }
  static List<Class<? extends TipperCategory>> categories(Tipper<? extends ASTNode> ¢) {
    return categories(new ArrayList<>(), ¢.getClass());
  }
  static List<Class<? extends TipperCategory>> categories(List<Class<? extends TipperCategory>> $, Class<?> c) {
    for (Class<? extends TipperCategory> ¢ : parentCategories(c))
      if (!$.contains(¢)) {
        $.add(¢);
        categories($, ¢);
      }
    return $;
  }
  @SuppressWarnings("unchecked") static List<Class<? extends TipperCategory>> parentCategories(Class<?> c) {
    List<Class<? extends TipperCategory>> $ = new ArrayList<>();
    for (Class<?> x : c.getInterfaces())
      if (TipperCategory.class.isAssignableFrom(x))
        $.add((Class<? extends TipperCategory>) x);
    return $;
  }
  default Class<? extends TipperCategory> lowestCategory() {
    Class<? extends TipperCategory> $ = TipperCategory.class;
    for (final Class<? extends TipperCategory> ¢ : children.keySet())
      if (¢.isInstance(this) && $.isAssignableFrom(¢))
        $ = ¢;
    return $;
  }
  /** Returns the preference group to which the tipper belongs to. This method
   * should be overridden for each tipper and should return one of the values of
   * {@link TipperGroup}
   * @return preference group this tipper belongs to */
  default TipperGroup tipperGroup() {
    return TipperGroup.find(this);
  }

  interface Bloater extends TipperCategory {
    String ___ = "Make code as verbose as possible";
  }

  interface Collapse extends CommonFactorOut {
    String ___ = "Shorten code by merging two adjacent syntactical elements into one";
  }

  /** A specialized {@link Collapse} carried out, by factoring out some common
   * element */
  interface CommonFactorOut extends Structural { // S2
    String ___ = "Factor out a common syntactical element";
  }

  interface Deadcode extends Theory.Logical {
    String ___ = "Eliminate code that is never executed";
  }

  interface EarlyReturn extends Structural {
    String ___ = "Early return";
  }

  interface EmptyCycles extends TipperCategory {
    String ___ = "Eliminate code which seems whose functions look like CPU churn and nothing else";
  }

  interface Idiomatic extends TipperCategory {
    String ___ = "Change expression to a more familiar structure (often shorter)";
  }

  interface Inlining extends Structural {
    String ___ = "Structural";
  }

  interface Loops extends Structural {
    String ___ = "Spartan use of Java loop syntax";
  }

  interface Manipulation extends TipperCategory {
    interface Collapse extends Manipulation {
      String ___ = "Collapse";
    }

    interface FactorOut extends Manipulation {
      String ___ = "Factor out";
      
    }

    interface Prune extends Manipulation {
      String ___ = "Prune";
    }

    interface Reshape extends Manipulation {
      String ___ = "Take a different shape";
    }

    interface Sort extends Manipulation {
      String ___ = "Sort";
    }
  }

  interface Modular extends TipperCategory {
    String ___ = "Make modular changes to code";
  }

  interface Nanos extends TipperCategory {
    String ___ = "Nanos";
  }

  interface NOP extends TipperCategory {
    String ___ = "Eliminate an operation whose computation does nothing";

    interface onBooleans extends Logical {
      String ___ = "Eliminate an operation whose computation does nothing on booleans";
    }

    interface onNumbers extends Arithmetics.Symbolic {
      String ___ = "Eliminate an operation whose computation does nothing on numbers";
    }
  }

  interface ScopeReduction extends Structural {
    String ___ = "Reduction of scope to the smallest possible";
  }

  interface Shortcircuit extends Structural {
    String ___ = "Shortcut of control flow by combining unconditional sequencers, e.g., converting break into return";
  }

  /** Use alphabetical, or some other ordering, when order does not matter */
  interface Sorting extends Idiomatic {
    String ___ = "Sorting of Annotations and Modifiers";
  }

  interface SyntacticBaggage extends Structural {
    String ___ = "Remove syntactical element that contributes nothing to semantics";
  }

  interface Ternarization extends CommonFactorOut {
    String ___ = "Convert conditional statement to the conditional, ?:, operator";
  }

  @SuppressWarnings("hiding")
  interface Theory extends TipperCategory {
    String ___ = "Simplifcation using a theory of some kind";

    interface Arithmetics extends TipperCategory.Theory {
      String ___ = "Rewrite an arithmetical expressions in a more canonical form";

      interface Numeric extends Arithmetics {
        String ___ = "Numeric simplfication of an arithmetical expression";
      }

      interface Symbolic extends Arithmetics {
        String ___ = "Symbolic simplfication of an arithmetical expression";
      }
    }

    interface Logical extends TipperCategory.Theory {
      String ___ = "Rewrite a boolean expression in a more canonical form";
    }

    interface Strings extends TipperCategory.Theory {
      String ___ = "Rewrite a string expression in a more canonical form";
    }
  }
}
