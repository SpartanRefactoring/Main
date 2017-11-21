package metatester;

import static metatester.Transformation.from;

/**
 * String transformations from normal Java assert statements and JUnit Assert.*
 * phrases.
 *
 * @author Oren Afek
 * @since 19.5.17
 **/
// These methods are invoked in runtime using reflection and never called
// statically.

class AssertJTransformator implements TestTransformator {

	public static final String ASSERTJ_STATIC_IMPORT_STATEMENT = "org.assertj.core.api.Assertions.assertThat";
	public static final String ASSERTJ_STATIC_IMPORT_STATEMENT_SIMPLE_NAME = "Assertions";
	static Transformation[] transformations = { //
			// language=RegExp
			from("assertEquals\\(0,(.*).size\\(\\)\\);").to("assertThat\\(%s\\).isEmpty\\(\\);"), //
			// language=RegExp
			from("assertTrue\\((.*) < (.*)\\);").to("assertThat\\(%s\\).isLessThan\\(%s\\);"), //
			// language=RegExp
			from("assertTrue\\((.*) > (.*)\\);").to("assertThat\\(%s\\).isGreaterThan\\(%s\\);"), //
			// language=RegExp
			from("assertEquals\\((.*),(.*).size\\(\\)\\);").to("assertThat\\(%s\\).hasSize\\(%s\\);").reorder(1, 0),
			// language=RegExp
			from("assertEquals\\((.*),(.*)\\);").to("assertThat\\(%s\\).isEqualTo\\(%s\\);").reorder(1, 0),
			// language=RegExp
			from("assertNotEquals\\((.*),(.*)\\);").to("assertThat\\(%s\\).isNotEqualTo\\(%s\\);").reorder(1, 0),
			// language=RegExp
			from("assertNull\\((.*)\\);").to("assertThat\\(%s\\).isNull\\(\\);"),
			// language=RegExp
			from("assertNotNull\\((.*)\\);").to("assertThat\\(%s\\).isNotNull\\(\\);"),
			// language=RegExp
			from("assertTrue\\((.*)\\);").to("assertThat\\(%s\\).isTrue\\(\\);"),
			// language=RegExp
			from("assertFalse\\((.*)\\);").to("assertThat\\(%s\\).isFalse\\(\\);"),
			// language=RegExp
			from("assert (.*) <= (.*) && (.*) <= (.*);").to("assertThat\\(%s\\).isBetween\\(%s, %s\\);").reorder(1, 0,
					3),
			// language=RegExp
			from("assert (.*) < (.*) && (.*) < (.*);").to("assertThat\\(%s\\).isStrictlyBetween\\(%s, %s\\);")
					.reorder(1, 0, 3),
			// language=RegExp
			from("assert (.*) > (.*);").to("assertThat\\(%s\\).isGreaterThan\\(%s\\);"),
			// language=RegExp
			from("assert (.*) == (.*);").to("assertThat\\(%s\\).isEqualTo\\(%s\\);"),
			// language=RegExp
			from("assert (.*) < (.*);").to("assertThat\\(%s\\).isLessThan\\(%s\\);"),
			// language=RegExp
			from("assert !\\((.*)\\);").to("assertThat\\(%s\\).isFalse\\(\\);"),
			// language=RegExp
			from("assert (.*);").to("assertThat\\(%s\\).isTrue\\(\\);") };

	public static final String ASSERT_NOT_NULL_TEMPLATE = "assertThat(%s).isNotNull();";

}