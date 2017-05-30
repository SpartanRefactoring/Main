package il.org.spartan.spartanizer.research.metatester;

import static il.org.spartan.spartanizer.research.metatester.TestTransformator.replace;

/**
 * @author Oren Afek
 * @since 19.5.17
 */

@SuppressWarnings("unused")
        //These methods are invoked in runtime using reflection and never called statically.
class AssertJTransformator implements TestTransformator {

    @Order(0)
    private static String replaceAssertEquals_isEmpty(final String ¢) {
        // language=RegExp
        return replace(¢, "assertEquals\\(0,(.*).size\\(\\)\\);", "assertThat(%s).isEmpty\\(\\);");
    }

    @Order(1)
    private static String replaceAssertEquals_hasSize(final String ¢) {
        // language=RegExp
        return replace(¢, "assertEquals\\((.*),(.*).size\\(\\)\\);", "assertThat\\(%s\\).hasSize\\(%s\\);", new int[]{1, 0});
    }

    @Order(2)
    private static String replaceAssertEquals_isEqualTo(final String ¢) {
        // language=RegExp
        return replace(¢, "assertEquals\\((.*),(.*)\\);", "assertThat\\(%s\\).isEqualTo\\(%s\\);", new int[]{1, 0});
    }

    @Order(3)
    private static String replaceAssertNull_isNull(final String ¢) {
        // language=RegExp
        return replace(¢, "assertNull\\((.*)\\);", "assertThat\\(%s\\).isNull\\(\\);");
    }

    @Order(4)
    private static String replaceAssertNotNull_isNotNull(final String ¢) {
        // language=RegExp
        return replace(¢, "assertNotNull\\((.*)\\);", "assertThat\\(%s\\).isNotNull\\(\\);");
    }

    @Order(5)
    private static String replaceAssertTrue_isTrue(final String ¢) {
        // language=RegExp
        return replace(¢, "assertTrue\\((.*)\\);", "assertThat\\(%s\\).isTrue\\(\\);");
    }

    @Order(6)
    private static String replaceAssertFalse_isFalse(final String ¢) {
        // language=RegExp
        return replace(¢, "assertFalse\\((.*)\\);", "assertThat\\(%s\\).isFalse\\(\\);");
    }

    @Order(7)
    private static String replaceAssertWithAssertFalse(final String ¢) {
        return replace(¢, "assert !\\((.*)\\);", "assertThat\\(%s\\).isFalse\\(\\);");
    }

    @Order(8)
    private static String replaceAssertWithAssertTrue(final String ¢) {
        return replace(¢, "assert (.*);", "assertThat\\(%s\\).isTrue\\(\\);");
    }




}