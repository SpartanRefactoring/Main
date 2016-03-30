package il.org.spartan.refactoring.wring;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static il.org.spartan.refactoring.wring.TrimmerTestsUtils.trimming;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExternalTests {
	@Test public void AnnotationsTest() {
		trimming("public class AnnotationSample {" + 
				"" + 
				"    private @interface MyAnnotation {" + 
				"        boolean booleanField() default true;" + 
				"        char charField() default 'c';" + 
				"        byte byteField() default 42;" + 
				"        short shortField() default 42;" + 
				"        int intField() default 42;" + 
				"        long longField() default 42;" + 
				"        float floatField() default 42;" + 
				"        double doubleField() default 42;" + 
				"" + 
				"        String stringField() default \"\";" + 
				"        String[] stringArrayField() default {};" + 
				"        String[] stringArrayFieldWithDefaults() default { \"a\", \"b\" };" + 
				"    }" + 
				"" + 
				"    @SuppressWarnings(value = \"javadoc\")" + 
				"    @Override()" + 
				"    public int hashCode() {" + 
				"        return super.hashCode();" + 
				"    }" + 
				"" + 
				"    @MyAnnotation(" + 
				"        booleanField = true," + 
				"        charField = 'c'," + 
				"        byteField = 42," + 
				"        shortField = 42," + 
				"        intField = 42," + 
				"        longField = 42," + 
				"        floatField = 42," + 
				"        doubleField = 42," + 
				"        stringArrayField = {}," + 
				"        stringArrayFieldWithDefaults = { \"a\", \"b\" })" + 
				"    public void refactorToMarkerAnnotation() throws Exception {" + 
				"        return;" + 
				"    }" + 
				"" + 
				"    @MyAnnotation(" + 
				"        booleanField = true," + 
				"        charField = 'c'," + 
				"        byteField = 0x2a," + 
				"        shortField = 42," + 
				"        intField = 42," + 
				"        longField = 42L," + 
				"        floatField = 42.0f," + 
				"        doubleField = 42.0d," + 
				"        stringArrayField = {}," + 
				"        stringArrayFieldWithDefaults = { \"a\", \"b\" })" + 
				"    public void refactorToMarkerAnnotation2() throws Exception {" + 
				"        return;" + 
				"    }" + 
				"" + 
				"    @MyAnnotation(" + 
				"        booleanField = false," + 
				"        charField = 'z'," + 
				"        byteField = 1," + 
				"        shortField = 1," + 
				"        intField = 1," + 
				"        longField = 1," + 
				"        floatField = 1," + 
				"        doubleField = 1," + 
				"        stringArrayField = { \"\", \"\" }," + 
				"        stringArrayFieldWithDefaults = {})" + 
				"    public void doNotRefactorNotUsingDefaults() throws Exception {" + 
				"        return;" + 
				"    }" + 
				"" + 
				"    @MyAnnotation(stringArrayField = { \"refactorToMarkerAnnotation\" })" + 
				"    public void removeCurlyBraces() throws Exception {" + 
				"        return;" + 
				"    }" + 
				"}").to("public class AnnotationSample {" + 
						"" + 
						"    private @interface MyAnnotation {" + 
						"        boolean booleanField() default true;" + 
						"        char charField() default 'c';" + 
						"        byte byteField() default 42;" + 
						"        short shortField() default 42;" + 
						"        int intField() default 42;" + 
						"        long longField() default 42;" + 
						"        float floatField() default 42;" + 
						"        double doubleField() default 42;" + 
						"" + 
						"        String stringField() default \"\";" + 
						"        String[] stringArrayField() default {};" + 
						"        String[] stringArrayFieldWithDefaults() default { \"a\", \"b\" };" + 
						"    }" + 
						"" + 
						"    @SuppressWarnings(\"javadoc\")" + 
						"    @Override" + 
						"    public int hashCode() {" + 
						"        return super.hashCode();" + 
						"    }" + 
						"" + 
						"    @MyAnnotation" + 
						"    public void refactorToMarkerAnnotation() throws Exception {" + 
						"        return;" + 
						"    }" + 
						"" + 
						"    @MyAnnotation" + 
						"    public void refactorToMarkerAnnotation2() throws Exception {" + 
						"        return;" + 
						"    }" + 
						"" + 
						"    @MyAnnotation(" + 
						"        booleanField = false," + 
						"        charField = 'z'," + 
						"        byteField = 1," + 
						"        shortField = 1," + 
						"        intField = 1," + 
						"        longField = 1," + 
						"        floatField = 1," + 
						"        doubleField = 1," + 
						"        stringArrayField = { \"\", \"\" }," + 
						"        stringArrayFieldWithDefaults = {})" + 
						"    public void doNotRefactorNotUsingDefaults() throws Exception {" + 
						"        return;" + 
						"    }" + 
						"" + 
						"    @MyAnnotation(stringArrayField = \"refactorToMarkerAnnotation\")" + 
						"    public void removeCurlyBraces() throws Exception {" + 
						"        return;" + 
						"    }" + 
						"}");
	}
}
