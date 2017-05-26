package il.org.spartan.Leonidas.plugin.tipping;

import il.org.spartan.Leonidas.plugin.PreferencesResources.TipperGroup;

/**
 * @author Roey Maor
 * @since 03-12-2016.
 */
public interface TipperCategory {
    String categoryDescription();

    /**
     * Returns the preference group to which the tipper belongs to. This method
     * should be overridden for each tipper and should return one of the values of
     * {@link TipperGroup}
     *
     * @return preference group this tipper belongs to
     */
    default TipperGroup tipperGroup() {
        return TipperGroup.find(this);
    }

    interface Abbreviation extends Nominal {
        String label = "Abbreviation";

        @Override
        default String categoryDescription() {
            return label;
        }
    }

    interface Annonimization extends Nominal {
        String label = "Unused arguments";

        @Override
        default String categoryDescription() {
            return label;
        }
    }

    interface Centification extends Nominal {
        String label = "Centification";

        @Override
        default String categoryDescription() {
            return label;
        }
    }

    /**
     * Merge two syntactical elements into one, whereby achieving shorter core
     */
    interface Collapse extends Structural {
        String label = "Collapse";

        @Override
        default String categoryDescription() {
            return label;
        }
    }

    /**
     * A specialized {@link Collapse} carried out, by factoring out some common
     * element
     */
    interface CommonFactoring extends Collapse { // S2
        String label = "Distributive refactoring";

        @Override
        default String categoryDescription() {
            return label;
        }
    }

    interface Dollarization extends Nominal {
        String label = "Dollarization";

        @Override
        default String categoryDescription() {
            return label;
        }
    }

    interface EarlyReturn extends Structural {
        String label = "Early return";

        @Override
        default String categoryDescription() {
            return label;
        }
    }

    /**
     * Change expression to a more familiar structure, which is not necessarily
     * shorter
     */
    interface Idiomatic extends Structural {
        String label = "Idiomatic";

        @Override
        default String categoryDescription() {
            return label;
        }
    }

    interface Inlining extends Structural {
        String label = "Structural";

        @Override
        default String categoryDescription() {
            return label;
        }
    }

    interface InVain extends Structural {
        String label = "NOP";

        @Override
        default String categoryDescription() {
            return label;
        }
    }

    interface Nanos extends Modular {
        String label = "Nanos";

        @Override
        default String categoryDescription() {
            return label;
        }
    }

    interface ScopeReduction extends Structural {
        String label = "Scope reduction";

        @Override
        default String categoryDescription() {
            return label;
        }
    }

    /**
     * Use alphabetical, or some other ordering, when order does not matter
     */
    interface Sorting extends Idiomatic {
        String label = "Sorting";

        @Override
        default String categoryDescription() {
            return label;
        }
    }

    /**
     * Remove syntactical elements that do not change the code semantics
     */
    interface SyntacticBaggage extends Structural {// S1
        String label = "Syntactic baggage";

        @Override
        default String categoryDescription() {
            return label;
        }
    }

    /**
     * Replace conditional statement with the conditional operator
     */
    interface Ternarization extends Structural { // S3
        String label = "Ternarization";

        @Override
        default String categoryDescription() {
            return label;
        }
    }
}