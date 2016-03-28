package il.org.spartan.refactoring.preferences;

import java.util.ArrayList;
import java.util.List;

public class PluginPreferencesStrings {
	/** Page description **/
	public static final String PAGE_DESCRIPTION = "Preferences for the Spartan Refactoring plug-in";
	
	/** General preferences **/
	public static final String PLUGIN_STARTUP_BEHAVIOR_ID = "pref_startup_behavior";
	public static final String PLUGIN_STARTUP_BEHAVIOR_TEXT = "Plugin startup behavior:";
	public static final String[][] PLUGIN_STARTUP_BEHAVIOR_OPTIONS = {{"Remember individual project settings", "remember"},{"Enable for all projects", "always_on"},{"Disable for all projects", "always_off"}};
	
	public static final String NEW_PROJECTS_ENABLE_BY_DEFAULT_ID = "pref_enable_by_default_for_new_projects";
	public static final String NEW_PROJECTS_ENABLE_BY_DEFAULT_TEXT = "Enable by default for newly created projects";
	
	/** Enabled wrings **/
	public static final String CONSOLIDATE_ASSIGNMENTS_STATEMENTS_ID = "pref_wrings_consolidate_assignments_statements";
	public static final String CONSOLIDATE_ASSIGNMENTS_STATEMENTS_TEXT = "Consolidate assignments and repeating statements";
	
	public static final String SIMPLIFY_NESTED_BLOCKS_ID = "pref_wrings_simplify_nested_blocks";
	public static final String SIMPLIFY_NESTED_BLOCKS_TEXT = "Simplify nested code blocks";
	
	public static final String ELIMINATE_TEMP_ID = "pref_wrings_eliminate_temp";
	public static final String ELIMINATE_TEMP_TEXT = "Eliminate temporary variables";
	
	public static final String REMOVE_REDUNDANT_PUNCTUATION_ID = "pref_wrings_remove_redundant_punctuation";
	public static final String REMOVE_REDUNDANT_PUNCTUATION_TEXT = "Remove redundant curly braces and semicolons";
	
	public static final String IF_TO_TERNARY_ID = "pref_wrings_if_to_ternary";
	public static final String IF_TO_TERNARY_TEXT = "Convert if statements to ternary operators";
	
	public static final String REFACTOR_INEFFECTIVE_ID = "pref_wrings_refactor_ineffective";
	public static final String REFACTOR_INEFFECTIVE_TEXT = "Refactor empty or ineffective code";
	
	public static final String REORDER_EXPRESSIONS_ID = "pref_wrings_reorder_expressions";
	public static final String REORDER_EXPRESSIONS_TEXT = "Reorder expressions for better readability";
	
	public static final String RENAME_PARAMETERS_ID = "pref_wrings_rename_parameters";
	public static final String RENAME_PARAMETERS_TEXT = "Rename method parameters";
	
	public static final String RENAME_RETURN_VARIABLE_ID = "pref_wrings_rename_return_variable";
	public static final String RENAME_RETURN_VARIABLE_TEXT = "Rename return variable to $";
	
	public static final String WRING_COMBO_OPTIONS[][] = {{"Enabled","on"},{"Disabled", "off"}};
	
	
	public static List<String[]> getAllWringComboOptions() {
		List<String[]> $ = new ArrayList<>();
		$.add(new String[] {CONSOLIDATE_ASSIGNMENTS_STATEMENTS_ID, CONSOLIDATE_ASSIGNMENTS_STATEMENTS_TEXT});
		$.add(new String[] {SIMPLIFY_NESTED_BLOCKS_ID, SIMPLIFY_NESTED_BLOCKS_TEXT});
		$.add(new String[] {ELIMINATE_TEMP_ID, ELIMINATE_TEMP_TEXT});
		$.add(new String[] {REMOVE_REDUNDANT_PUNCTUATION_ID, REMOVE_REDUNDANT_PUNCTUATION_TEXT});
		$.add(new String[] {IF_TO_TERNARY_ID, IF_TO_TERNARY_TEXT});
		$.add(new String[] {REFACTOR_INEFFECTIVE_ID, REFACTOR_INEFFECTIVE_TEXT});
		$.add(new String[] {REORDER_EXPRESSIONS_ID, REORDER_EXPRESSIONS_TEXT});
		$.add(new String[] {RENAME_PARAMETERS_ID, RENAME_PARAMETERS_TEXT});
		$.add(new String[] {RENAME_RETURN_VARIABLE_ID, RENAME_RETURN_VARIABLE_TEXT});
		
		return $;
	}
}
