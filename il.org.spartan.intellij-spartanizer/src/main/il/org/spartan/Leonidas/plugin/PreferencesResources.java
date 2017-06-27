package il.org.spartan.Leonidas.plugin;

import il.org.spartan.Leonidas.plugin.tipping.TipperCategory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Yossi Gil, Roey Maor
 * @since 03-12-2016
 */
public class PreferencesResources {
    /**
     * Page description
     **/
    public static final String PAGE_DESCRIPTION = "Preferences for the laconization plug-in";
    /**
     * General preferences
     **/
    public static final String PLUGIN_STARTUP_BEHAVIOR_ID = "pref_startup_behavior";
    public static final String PLUGIN_STARTUP_BEHAVIOR_TEXT = "Plugin startup behavior:";
    public static final String[][] PLUGIN_STARTUP_BEHAVIOR_OPTIONS = { //
            {"Remember individual project settings", "remember"}, //
            {"Enable for all projects", "always_on"}, //
            {"Disable for all projects", "always_off"} //
    };
    public static final String NEW_PROJECTS_ENABLE_BY_DEFAULT_ID = "Preference_enable_by_default_for_new_projects";
    public static final String NEW_PROJECTS_ENABLE_BY_DEFAULT_TEXT = "Enable by default for newly created projects";
    public static final String TIPPER_CATEGORY_PREFIX = "il.org.spartan"; // NOT
    // SAFE
    public static AtomicBoolean NEW_PROJECTS_ENABLE_BY_DEFAULT_VALUE = new AtomicBoolean(true);

    /**
     * An enum holding together all the "enabled spartanizations" options, also
     * allowing to get the set preference value for each of them
     */
    public enum TipperGroup {
        Abbreviation(TipperCategory.Abbreviation.class), //
        Annonimaization(TipperCategory.Annonimization.class), //
        Canonicalization(TipperCategory.Collapse.class), //
        CommonFactoring(TipperCategory.CommonFactoring.class), //
        Centification(TipperCategory.Centification.class), //
        Dollarization(TipperCategory.Dollarization.class), //
        EarlyReturn(TipperCategory.EarlyReturn.class), //
        Idiomatic(TipperCategory.Idiomatic.class), //
        Inlining(TipperCategory.Inlining.class), //
        InVain(TipperCategory.InVain.class), //
        Nanopatterns(TipperCategory.Nanos.class), //
        ScopeReduction(TipperCategory.ScopeReduction.class), //
        Sorting(TipperCategory.Sorting.class), //
        SyntacticBaggage(TipperCategory.SyntacticBaggage.class), //
        Ternarization(TipperCategory.Ternarization.class), //
        ;
        final String id;
        final String label;
        private final Class<? extends TipperCategory> clazz;

        TipperGroup(final Class<? extends TipperCategory> clazz) {
            this.clazz = clazz;
            id = clazz.getCanonicalName();
            label = getLabel(clazz) + "";
        }

        public static TipperGroup find(final TipperCategory ¢) {
            return find(¢.getClass());
        }

        private static TipperGroup find(final Class<? extends TipperCategory> ¢) {
            for (final TipperGroup $ : TipperGroup.values())
                if ($.clazz.isAssignableFrom(¢))
                    return $;
            return null;
        }

        private Object getLabel(final Class<? extends TipperCategory> $) {
            try {
                return $.getField("label").get(null);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ¢) {
                return null;
            }
        }
    }
}