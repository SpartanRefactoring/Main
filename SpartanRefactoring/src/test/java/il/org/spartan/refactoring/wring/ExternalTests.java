package il.org.spartan.refactoring.wring;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import il.org.spartan.refactoring.spartanizations.Wrap;
import il.org.spartan.refactoring.wring.TrimmerTestsUtils.Operand;

import static il.org.spartan.refactoring.wring.TrimmerTestsUtils.trimming;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExternalTests {
	@Test public void AnnotationsTest() {
		trimming("package com.example;\n" + 
				"\n" + 
				"public class AnnotationSample {\n" + 
				"\n" + 
				"    private @interface MyAnnotation {\n" + 
				"        boolean booleanField() default true;\n" + 
				"        char charField() default 'c';\n" + 
				"        byte byteField() default 42;\n" + 
				"        short shortField() default 42;\n" + 
				"        int intField() default 42;\n" + 
				"        long longField() default 42;\n" + 
				"        float floatField() default 42;\n" + 
				"        double doubleField() default 42;\n" + 
				"\n" + 
				"        String stringField() default \"\";\n" + 
				"        String[] stringArrayField() default {};\n" + 
				"        String[] stringArrayFieldWithDefaults() default { \"a\", \"b\" };\n" + 
				"    }\n" + 
				"\n" + 
				"    @SuppressWarnings(value = \"javadoc\")\n" + 
				"    @Override()\n" + 
				"    public int hashCode() {\n" + 
				"        return super.hashCode();\n" + 
				"    }\n" + 
				"\n" + 
				"    @MyAnnotation(\n" + 
				"        booleanField = true,\n" + 
				"        charField = 'c',\n" + 
				"        byteField = 42,\n" + 
				"        shortField = 42,\n" + 
				"        intField = 42,\n" + 
				"        longField = 42,\n" + 
				"        floatField = 42,\n" + 
				"        doubleField = 42,\n" + 
				"        stringArrayField = {},\n" + 
				"        stringArrayFieldWithDefaults = { \"a\", \"b\" })\n" + 
				"    public void refactorToMarkerAnnotation() throws Exception {\n" + 
				"        return;\n" + 
				"    }\n" + 
				"\n" + 
				"    @MyAnnotation(\n" + 
				"        booleanField = true,\n" + 
				"        charField = 'c',\n" + 
				"        byteField = 0x2a,\n" + 
				"        shortField = 42,\n" + 
				"        intField = 42,\n" + 
				"        longField = 42L,\n" + 
				"        floatField = 42.0f,\n" + 
				"        doubleField = 42.0d,\n" + 
				"        stringArrayField = {},\n" + 
				"        stringArrayFieldWithDefaults = { \"a\", \"b\" })\n" + 
				"    public void refactorToMarkerAnnotation2() throws Exception {\n" + 
				"        return;\n" + 
				"    }\n" + 
				"\n" + 
				"    @MyAnnotation(\n" + 
				"        booleanField = false,\n" + 
				"        charField = 'z',\n" + 
				"        byteField = 1,\n" + 
				"        shortField = 1,\n" + 
				"        intField = 1,\n" + 
				"        longField = 1,\n" + 
				"        floatField = 1,\n" + 
				"        doubleField = 1,\n" + 
				"        stringArrayField = { \"\", \"\" },\n" + 
				"        stringArrayFieldWithDefaults = {})\n" + 
				"    public void doNotRefactorNotUsingDefaults() throws Exception {\n" + 
				"        return;\n" + 
				"    }\n" + 
				"\n" + 
				"    @MyAnnotation(stringArrayField = { \"refactorToMarkerAnnotation\" })\n" + 
				"    public void removeCurlyBraces() throws Exception {\n" + 
				"        return;\n" + 
				"    }\n" + 
				"}").toCompilationUnit("package com.example;\n" + 
						"\n" + 
						"public class AnnotationSample {\n" + 
						"\n" + 
						"    private @interface MyAnnotation {\n" + 
						"        boolean booleanField() default true;\n" + 
						"        char charField() default 'c';\n" + 
						"        byte byteField() default 42;\n" + 
						"        short shortField() default 42;\n" + 
						"        int intField() default 42;\n" + 
						"        long longField() default 42;\n" + 
						"        float floatField() default 42;\n" + 
						"        double doubleField() default 42;\n" + 
						"\n" + 
						"        String stringField() default \"\";\n" + 
						"        String[] stringArrayField() default {};\n" + 
						"        String[] stringArrayFieldWithDefaults() default { \"a\", \"b\" };\n" + 
						"    }\n" + 
						"\n" + 
						"    @SuppressWarnings(\"javadoc\")\n" + 
						"    @Override\n" + 
						"    public int hashCode() {\n" + 
						"        return super.hashCode();\n" + 
						"    }\n" + 
						"\n" + 
						"    @MyAnnotation\n" + 
						"    public void refactorToMarkerAnnotation() throws Exception {\n" + 
						"        return;\n" + 
						"    }\n" + 
						"\n" + 
						"    @MyAnnotation\n" + 
						"    public void refactorToMarkerAnnotation2() throws Exception {\n" + 
						"        return;\n" + 
						"    }\n" + 
						"\n" + 
						"    @MyAnnotation(\n" + 
						"        booleanField = false,\n" + 
						"        charField = 'z',\n" + 
						"        byteField = 1,\n" + 
						"        shortField = 1,\n" + 
						"        intField = 1,\n" + 
						"        longField = 1,\n" + 
						"        floatField = 1,\n" + 
						"        doubleField = 1,\n" + 
						"        stringArrayField = { \"\", \"\" },\n" + 
						"        stringArrayFieldWithDefaults = {})\n" + 
						"    public void doNotRefactorNotUsingDefaults() throws Exception {\n" + 
						"        return;\n" + 
						"    }\n" + 
						"\n" + 
						"    @MyAnnotation(stringArrayField = \"refactorToMarkerAnnotation\")\n" + 
						"    public void removeCurlyBraces() throws Exception {\n" + 
						"        return;\n" + 
						"    }\n" + 
						"}");
	}
	
	@Test public void BooleanTest() {
		trimming("package com.example;\n" + 
				"\n" + 
				"public class BooleanSample {\n" + 
				"\n" + 
				"    public boolean f;\n" + 
				"    public Boolean g;\n" + 
				"\n" + 
				"    public void useBooleanConstants() {\n" + 
				"        Boolean b1 = Boolean.valueOf(true);\n" + 
				"        Boolean b2 = Boolean.valueOf(false);\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean returnIfConditionBooleanPrimitive(boolean b) {\n" + 
				"        if (b) {\n" + 
				"            return true;\n" + 
				"        } else {\n" + 
				"            return false;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean returnIfConditionBooleanPrimitive2(boolean b) {\n" + 
				"        if (b) {\n" + 
				"            return false;\n" + 
				"        } else {\n" + 
				"            return true;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean returnIfConditionWithInfixExpressionBooleanPrimitive(int i) {\n" + 
				"        if (0 < i && i < 12) {\n" + 
				"            return false;\n" + 
				"        } else {\n" + 
				"            return true;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean returnIfConditionWithInstanceofExpressionBooleanPrimitive(Object o) {\n" + 
				"        if (o instanceof String) {\n" + 
				"            return false;\n" + 
				"        } else {\n" + 
				"            return true;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean returnIfConditionAddCurlyBraces(Object o) {\n" + 
				"        if (o instanceof Integer) {\n" + 
				"            return true;\n" + 
				"        } else if (o instanceof String) {\n" + 
				"            return false;\n" + 
				"        } else {\n" + 
				"            return true;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean returnIfConditionThatRevertsInstanceofExpressionBooleanPrimitive(Object o) {\n" + 
				"        if (!(/* do not lose me */o instanceof String)) {\n" + 
				"            return false;\n" + 
				"        } else {\n" + 
				"            return true;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public Boolean returnIfConditionBooleanObject(boolean b) {\n" + 
				"        if (b) {\n" + 
				"            return Boolean.TRUE;\n" + 
				"        } else {\n" + 
				"            return Boolean.FALSE;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public Boolean returnIfConditionBooleanObject2(boolean b) {\n" + 
				"        if (b) {\n" + 
				"            return Boolean.FALSE;\n" + 
				"        } else {\n" + 
				"            return Boolean.TRUE;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean returnIfConditionMixedBoolean1(boolean b) {\n" + 
				"        if (b) {\n" + 
				"            return Boolean.TRUE;\n" + 
				"        } else {\n" + 
				"            return false;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean returnIfConditionMixedBoolean2(boolean b) {\n" + 
				"        if (b) {\n" + 
				"            return true;\n" + 
				"        } else {\n" + 
				"            return Boolean.FALSE;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean returnIfConditionBooleanPrimitive3(boolean b) {\n" + 
				"        if (b) {\n" + 
				"            return true;\n" + 
				"        }\n" + 
				"        return false;\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean returnIfConditionBooleanPrimitive4(boolean b) {\n" + 
				"        if (b) {\n" + 
				"            return false;\n" + 
				"        }\n" + 
				"        return true;\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean returnIfConditionBooleanObject3(boolean b) {\n" + 
				"        if (b) {\n" + 
				"            return Boolean.TRUE;\n" + 
				"        }\n" + 
				"        return Boolean.FALSE;\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean returnIfConditionBooleanObject4(boolean b) {\n" + 
				"        if (b) {\n" + 
				"            return Boolean.FALSE;\n" + 
				"        }\n" + 
				"        return Boolean.TRUE;\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessUseOfTernaryOperatorWithBooleanPrimitive1(\n" + 
				"            boolean bo) {\n" + 
				"        boolean b = bo ? true : false;\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessUseOfTernaryOperatorWithBooleanPrimitive2(\n" + 
				"            boolean bo) {\n" + 
				"        boolean b = bo ? false : true;\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessUseOfTernaryOperatorWithBooleanObject1(boolean bo) {\n" + 
				"        Boolean b = bo ? Boolean.TRUE : Boolean.FALSE;\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessUseOfTernaryOperatorWithBooleanObject2(boolean bo) {\n" + 
				"        Boolean b = bo ? Boolean.FALSE : Boolean.TRUE;\n" + 
				"    }\n" + 
				"\n" + 
				"    public void doNotRemoveIfInBooleanPrimitiveAssignment1(boolean bo) {\n" + 
				"        boolean b = true;\n" + 
				"        if (bo) {\n" + 
				"            b = false;\n" + 
				"        } else {\n" + 
				"            System.out.println();\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanPrimitiveAssignment1(boolean bo) {\n" + 
				"        boolean b = true;\n" + 
				"        if (bo) {\n" + 
				"            b = false;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanPrimitiveAssignment2(boolean bo) {\n" + 
				"        boolean b = false;\n" + 
				"        if (bo) {\n" + 
				"            b = true;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanObjectAssignment1(boolean bo) {\n" + 
				"        Boolean b = Boolean.TRUE;\n" + 
				"        if (bo) {\n" + 
				"            b = Boolean.FALSE;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanObjectAssignment2(boolean bo) {\n" + 
				"        boolean b = Boolean.FALSE;\n" + 
				"        if (bo) {\n" + 
				"            b = Boolean.TRUE;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanPrimitiveAssignment3(boolean bo,\n" + 
				"            boolean b) {\n" + 
				"        b = true;\n" + 
				"        if (bo) {\n" + 
				"            b = false;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanPrimitiveAssignment4(boolean bo,\n" + 
				"            boolean b) {\n" + 
				"        b = false;\n" + 
				"        if (bo) {\n" + 
				"            b = true;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanPrimitiveAssignmentSearchFurtherAwayForPreviousSibling(\n" + 
				"            boolean bo, boolean b) {\n" + 
				"        b = false;\n" + 
				"        char c = 'a';\n" + 
				"        byte by = 0;\n" + 
				"        double d = 0.0;\n" + 
				"        if (bo) {\n" + 
				"            b = true;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanObjectAssignment3(boolean bo, Boolean b) {\n" + 
				"        b = Boolean.TRUE;\n" + 
				"        if (bo) {\n" + 
				"            b = Boolean.FALSE;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanObjectAssignment4(boolean bo, Boolean b) {\n" + 
				"        b = Boolean.FALSE;\n" + 
				"        if (bo) {\n" + 
				"            b = Boolean.TRUE;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanPrimitiveAssignment5(boolean bo) {\n" + 
				"        this.f = Boolean.FALSE;\n" + 
				"        if (bo) {\n" + 
				"            this.f = Boolean.TRUE;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanObjectAssignment5(boolean bo) {\n" + 
				"        this.g = Boolean.FALSE;\n" + 
				"        if (bo) {\n" + 
				"            this.g = Boolean.TRUE;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanPrimitiveAssignment6(boolean bo) {\n" + 
				"        f = Boolean.FALSE;\n" + 
				"        if (bo) {\n" + 
				"            f = Boolean.TRUE;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanObjectAssignment6(boolean bo) {\n" + 
				"        g = Boolean.FALSE;\n" + 
				"        if (bo) {\n" + 
				"            g = Boolean.TRUE;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanObjectAssignment7(boolean bo) {\n" + 
				"        BooleanSample.this.g = Boolean.FALSE;\n" + 
				"        if (bo) {\n" + 
				"            BooleanSample.this.g = Boolean.TRUE;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean removeUselessIfInBooleanPrimitiveAssignment7(boolean bo) {\n" + 
				"        if (bo) {\n" + 
				"            return aMethodThatReturnsBoolean();\n" + 
				"        }\n" + 
				"        return false;\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean removeUselessIfInBooleanPrimitiveAssignment8(boolean bo) {\n" + 
				"        if (bo) {\n" + 
				"            return aMethodThatReturnsBoolean();\n" + 
				"        }\n" + 
				"        return true;\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean removeUselessIfInBooleanPrimitiveAssignment9(boolean bo) {\n" + 
				"        if (bo) {\n" + 
				"            return false;\n" + 
				"        }\n" + 
				"        return aMethodThatReturnsBoolean();\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean removeUselessIfInBooleanPrimitiveAssignment10(boolean bo) {\n" + 
				"        if (bo) {\n" + 
				"            return true;\n" + 
				"        }\n" + 
				"        return aMethodThatReturnsBoolean();\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanPrimitiveExpression10(boolean bo) {\n" + 
				"        if (bo) {\n" + 
				"            aMethodThatAcceptsABoolean(true);\n" + 
				"        } else {\n" + 
				"            aMethodThatAcceptsABoolean(false);\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanPrimitiveExpression11(boolean bo) {\n" + 
				"        if (bo) {\n" + 
				"            aMethodThatAcceptsABoolean(false);\n" + 
				"        } else {\n" + 
				"            aMethodThatAcceptsABoolean(true);\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanPrimitiveExpression12(boolean bo) {\n" + 
				"        if (bo) {\n" + 
				"            aMethodThatAcceptsABoolean(true);\n" + 
				"        } else {\n" + 
				"            aMethodThatAcceptsABoolean(aMethodThatReturnsBoolean());\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanPrimitiveExpression13(boolean bo) {\n" + 
				"        if (bo) {\n" + 
				"            aMethodThatAcceptsABoolean(false);\n" + 
				"        } else {\n" + 
				"            aMethodThatAcceptsABoolean(aMethodThatReturnsBoolean());\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanPrimitiveExpression14(boolean bo) {\n" + 
				"        if (bo) {\n" + 
				"            aMethodThatAcceptsABoolean(aMethodThatReturnsBoolean());\n" + 
				"        } else {\n" + 
				"            aMethodThatAcceptsABoolean(true);\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void removeUselessIfInBooleanPrimitiveExpression15(boolean bo) {\n" + 
				"        if (bo) {\n" + 
				"            aMethodThatAcceptsABoolean(aMethodThatReturnsBoolean());\n" + 
				"        } else {\n" + 
				"            aMethodThatAcceptsABoolean(false);\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean invertConditionalExpression(int i, boolean res1, boolean res2) {\n" + 
				"        if (i == 0 ? res1 : res2) {\n" + 
				"            return false;\n" + 
				"        }\n" + 
				"        return true;\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean invertAssignment(boolean b1, boolean b2) {\n" + 
				"        if (b1 = b2) {\n" + 
				"            return false;\n" + 
				"        }\n" + 
				"        return true;\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean invertCast(Object o) {\n" + 
				"        if ((Boolean) o) {\n" + 
				"            return false;\n" + 
				"        }\n" + 
				"        return true;\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean doNotRefactor(Object o) {\n" + 
				"        if (o instanceof Double) {\n" + 
				"            return ((Double) o).doubleValue() != 0;\n" + 
				"        } else if (o instanceof Float) {\n" + 
				"            return ((Float) o).floatValue() != 0;\n" + 
				"        }\n" + 
				"        return false;\n" + 
				"    }\n" + 
				"\n" + 
				"    public Boolean doNotThrowAnyException(boolean bo) {\n" + 
				"        class ClassWithBooleanField {\n" + 
				"            Boolean b;\n" + 
				"        }\n" + 
				"        ClassWithBooleanField objWithBooleanField = new ClassWithBooleanField();\n" + 
				"        return bo ? objWithBooleanField.b : Boolean.TRUE;\n" + 
				"    }\n" + 
				"\n" + 
				"    protected boolean aMethodThatReturnsBoolean() {\n" + 
				"        return false;\n" + 
				"    }\n" + 
				"\n" + 
				"    protected void aMethodThatAcceptsABoolean(boolean b) {\n" + 
				"    }\n" + 
				"}").toCompilationUnit("package com.example;\n" + 
						"\n" + 
						"public class BooleanSample {\n" + 
						"\n" + 
						"    public boolean f;\n" + 
						"    public Boolean g;\n" + 
						"\n" + 
						"    public void useBooleanConstants() {\n" + 
						"        Boolean b1 = Boolean.TRUE;\n" + 
						"        Boolean b2 = Boolean.FALSE;\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean returnIfConditionBooleanPrimitive(boolean b) {\n" + 
						"        return b;\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean returnIfConditionBooleanPrimitive2(boolean b) {\n" + 
						"        return !b;\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean returnIfConditionWithInfixExpressionBooleanPrimitive(int i) {\n" + 
						"        return !(0 < i && i < 12);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean returnIfConditionWithInstanceofExpressionBooleanPrimitive(Object o) {\n" + 
						"        return !(o instanceof String);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean returnIfConditionAddCurlyBraces(Object o) {\n" + 
						"        return (o instanceof Integer) || (!(o instanceof String));\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean returnIfConditionThatRevertsInstanceofExpressionBooleanPrimitive(Object o) {\n" + 
						"        return /* do not lose me */o instanceof String;\n" + 
						"    }\n" + 
						"\n" + 
						"    public Boolean returnIfConditionBooleanObject(boolean b) {\n" + 
						"        return Boolean.valueOf(b);\n" + 
						"    }\n" + 
						"\n" + 
						"    public Boolean returnIfConditionBooleanObject2(boolean b) {\n" + 
						"        return Boolean.valueOf(!b);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean returnIfConditionMixedBoolean1(boolean b) {\n" + 
						"        return Boolean.valueOf(b);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean returnIfConditionMixedBoolean2(boolean b) {\n" + 
						"        return b;\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean returnIfConditionBooleanPrimitive3(boolean b) {\n" + 
						"        return b;\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean returnIfConditionBooleanPrimitive4(boolean b) {\n" + 
						"        return !b;\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean returnIfConditionBooleanObject3(boolean b) {\n" + 
						"        return b;\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean returnIfConditionBooleanObject4(boolean b) {\n" + 
						"        return !b;\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessUseOfTernaryOperatorWithBooleanPrimitive1(\n" + 
						"            boolean bo) {\n" + 
						"        boolean b = bo;\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessUseOfTernaryOperatorWithBooleanPrimitive2(\n" + 
						"            boolean bo) {\n" + 
						"        boolean b = !bo;\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessUseOfTernaryOperatorWithBooleanObject1(boolean bo) {\n" + 
						"        Boolean b = Boolean.valueOf(bo);\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessUseOfTernaryOperatorWithBooleanObject2(boolean bo) {\n" + 
						"        Boolean b = Boolean.valueOf(!bo);\n" + 
						"    }\n" + 
						"\n" + 
						"    public void doNotRemoveIfInBooleanPrimitiveAssignment1(boolean bo) {\n" + 
						"        boolean b = true;\n" + 
						"        if (bo) {\n" + 
						"            b = false;\n" + 
						"        } else {\n" + 
						"            System.out.println();\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanPrimitiveAssignment1(boolean bo) {\n" + 
						"        boolean b = !bo;\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanPrimitiveAssignment2(boolean bo) {\n" + 
						"        boolean b = bo;\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanObjectAssignment1(boolean bo) {\n" + 
						"        Boolean b = Boolean.valueOf(!bo);\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanObjectAssignment2(boolean bo) {\n" + 
						"        boolean b = bo;\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanPrimitiveAssignment3(boolean bo,\n" + 
						"            boolean b) {\n" + 
						"        b = !bo;\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanPrimitiveAssignment4(boolean bo,\n" + 
						"            boolean b) {\n" + 
						"        b = bo;\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanPrimitiveAssignmentSearchFurtherAwayForPreviousSibling(\n" + 
						"            boolean bo, boolean b) {\n" + 
						"        b = false;\n" + 
						"        char c = 'a';\n" + 
						"        byte by = 0;\n" + 
						"        double d = 0.0;\n" + 
						"        if (bo) {\n" + 
						"            b = true;\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanObjectAssignment3(boolean bo, Boolean b) {\n" + 
						"        b = Boolean.valueOf(!bo);\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanObjectAssignment4(boolean bo, Boolean b) {\n" + 
						"        b = Boolean.valueOf(bo);\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanPrimitiveAssignment5(boolean bo) {\n" + 
						"        this.f = bo;\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanObjectAssignment5(boolean bo) {\n" + 
						"        this.g = Boolean.valueOf(bo);\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanPrimitiveAssignment6(boolean bo) {\n" + 
						"        f = bo;\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanObjectAssignment6(boolean bo) {\n" + 
						"        g = Boolean.valueOf(bo);\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanObjectAssignment7(boolean bo) {\n" + 
						"        BooleanSample.this.g = Boolean.valueOf(bo);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean removeUselessIfInBooleanPrimitiveAssignment7(boolean bo) {\n" + 
						"        return (bo) && (aMethodThatReturnsBoolean());\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean removeUselessIfInBooleanPrimitiveAssignment8(boolean bo) {\n" + 
						"        return !(bo) || (aMethodThatReturnsBoolean());\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean removeUselessIfInBooleanPrimitiveAssignment9(boolean bo) {\n" + 
						"        return !(bo) && (aMethodThatReturnsBoolean());\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean removeUselessIfInBooleanPrimitiveAssignment10(boolean bo) {\n" + 
						"        return (bo) || (aMethodThatReturnsBoolean());\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanPrimitiveExpression10(boolean bo) {\n" + 
						"        aMethodThatAcceptsABoolean(bo);\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanPrimitiveExpression11(boolean bo) {\n" + 
						"        aMethodThatAcceptsABoolean(!bo);\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanPrimitiveExpression12(boolean bo) {\n" + 
						"        if (bo) {\n" + 
						"            aMethodThatAcceptsABoolean(true);\n" + 
						"        } else {\n" + 
						"            aMethodThatAcceptsABoolean(aMethodThatReturnsBoolean());\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanPrimitiveExpression13(boolean bo) {\n" + 
						"        if (bo) {\n" + 
						"            aMethodThatAcceptsABoolean(false);\n" + 
						"        } else {\n" + 
						"            aMethodThatAcceptsABoolean(aMethodThatReturnsBoolean());\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanPrimitiveExpression14(boolean bo) {\n" + 
						"        if (bo) {\n" + 
						"            aMethodThatAcceptsABoolean(aMethodThatReturnsBoolean());\n" + 
						"        } else {\n" + 
						"            aMethodThatAcceptsABoolean(true);\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public void removeUselessIfInBooleanPrimitiveExpression15(boolean bo) {\n" + 
						"        if (bo) {\n" + 
						"            aMethodThatAcceptsABoolean(aMethodThatReturnsBoolean());\n" + 
						"        } else {\n" + 
						"            aMethodThatAcceptsABoolean(false);\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean invertConditionalExpression(int i, boolean res1, boolean res2) {\n" + 
						"        return !(i == 0 ? res1 : res2);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean invertAssignment(boolean b1, boolean b2) {\n" + 
						"        return !(b1 = b2);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean invertCast(Object o) {\n" + 
						"        return !(Boolean) o;\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean doNotRefactor(Object o) {\n" + 
						"        if (o instanceof Double) {\n" + 
						"            return ((Double) o).doubleValue() != 0;\n" + 
						"        } else if (o instanceof Float) {\n" + 
						"            return ((Float) o).floatValue() != 0;\n" + 
						"        }\n" + 
						"        return false;\n" + 
						"    }\n" + 
						"\n" + 
						"    public Boolean doNotThrowAnyException(boolean bo) {\n" + 
						"        class ClassWithBooleanField {\n" + 
						"            Boolean b;\n" + 
						"        }\n" + 
						"        ClassWithBooleanField objWithBooleanField = new ClassWithBooleanField();\n" + 
						"        return bo ? objWithBooleanField.b : Boolean.TRUE;\n" + 
						"    }\n" + 
						"\n" + 
						"    protected boolean aMethodThatReturnsBoolean() {\n" + 
						"        return false;\n" + 
						"    }\n" + 
						"\n" + 
						"    protected void aMethodThatAcceptsABoolean(boolean b) {\n" + 
						"    }\n" + 
						"}");
	}
	
	@Test public void CollapseIfStatementTest() {
		trimming("package com.example;\n" + 
				"\n" + 
				"public class CollapseIfStatementSample {\n" + 
				"\n" + 
				"    public void collapseIfStatements(boolean b1, boolean b2) {\n" + 
				"        // keep this comment 1\n" + 
				"        if (b1) {\n" + 
				"            // keep this comment 2\n" + 
				"            if (b2) {\n" + 
				"                // keep this comment 3\n" + 
				"                int i = 0;\n" + 
				"            }\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void collapseIfStatementsAddParenthesesIfDifferentConditionalOperator(boolean b1, boolean b2, boolean b3) {\n" + 
				"        // keep this comment 1\n" + 
				"        if (b1) {\n" + 
				"            // keep this comment 2\n" + 
				"            if (b2 || b3) {\n" + 
				"                // keep this comment 3\n" + 
				"                int i = 0;\n" + 
				"            }\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void doNotCollapseOuterIfWithElseStatement(boolean b1, boolean b2) {\n" + 
				"        if (b1) {\n" + 
				"            if (b2) {\n" + 
				"                int i = 0;\n" + 
				"            }\n" + 
				"        } else {\n" + 
				"            int i = 0;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void doNotCollapseIfWithElseStatement2(boolean b1, boolean b2) {\n" + 
				"        if (b1) {\n" + 
				"            if (b2) {\n" + 
				"                int i = 0;\n" + 
				"            } else {\n" + 
				"                int i = 0;\n" + 
				"            }\n" + 
				"        }\n" + 
				"    }\n" + 
				"}").toCompilationUnit("package com.example;\n" + 
						"\n" + 
						"public class CollapseIfStatementSample {\n" + 
						"\n" + 
						"    public void collapseIfStatements(boolean b1, boolean b2) {\n" + 
						"        // keep this comment 1\n" + 
						"        if (b1 && b2) {\n" + 
						"            // keep this comment 3\n" + 
						"            int i = 0;\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public void collapseIfStatementsAddParenthesesIfDifferentConditionalOperator(boolean b1, boolean b2, boolean b3) {\n" + 
						"        // keep this comment 1\n" + 
						"        if (b1 && (b2 || b3)) {\n" + 
						"            // keep this comment 3\n" + 
						"            int i = 0;\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public void doNotCollapseOuterIfWithElseStatement(boolean b1, boolean b2) {\n" + 
						"        if (b1) {\n" + 
						"            if (b2) {\n" + 
						"                int i = 0;\n" + 
						"            }\n" + 
						"        } else {\n" + 
						"            int i = 0;\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public void doNotCollapseIfWithElseStatement2(boolean b1, boolean b2) {\n" + 
						"        if (b1) {\n" + 
						"            if (b2) {\n" + 
						"                int i = 0;\n" + 
						"            } else {\n" + 
						"                int i = 0;\n" + 
						"            }\n" + 
						"        }\n" + 
						"    }\n" + 
						"}");
	}
	
	@Test public void CommonCodeInIfElseStatementTest() {
		trimming("package com.example;\n" + 
				"\n" + 
				"public class CommonCodeInIfElseStatementSample {\n" + 
				"\n" + 
				"    /** no code at all, remove all */\n" + 
				"    public void emptyIfOrElseClauses(Boolean b, int i, int j) {\n" + 
				"        if (b.booleanValue()) {\n" + 
				"            System.out.println();\n" + 
				"        } else {\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    /** no common code, Do not remove anything */\n" + 
				"    public void ifElseRemoveIf(Boolean b, int i, int j) {\n" + 
				"        if (b.booleanValue()) {\n" + 
				"            i++;\n" + 
				"        } else {\n" + 
				"            j++;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    /** common code: i++, Remove if statement */\n" + 
				"    public void ifElseRemoveIfNoBrackets(Boolean b, int i) {\n" + 
				"        // keep this!\n" + 
				"        if (b.booleanValue())\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"        else\n" + 
				"            i++;\n" + 
				"    }\n" + 
				"\n" + 
				"    /** common code: i++, Remove if statement */\n" + 
				"    public void ifElseRemoveIf(Boolean b, int i) {\n" + 
				"        if (b.booleanValue()) {\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"        } else {\n" + 
				"            i++;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    /** common code: i++, Remove then case */\n" + 
				"    public void ifElseRemoveThen(Boolean b, int i, int j) {\n" + 
				"        if (b.booleanValue()) {\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"        } else {\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"            j++;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    /** common code: i++, Remove else case */\n" + 
				"    public void ifElseRemoveElse(Boolean b, int i, int j) {\n" + 
				"        if (b.booleanValue()) {\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"            j++;\n" + 
				"        } else {\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    /**\n" + 
				"     * common code: put i++ before if statement, put l++ after if statement. Do\n" + 
				"     * not remove if statement.\n" + 
				"     */\n" + 
				"    public void ifElseRemoveIf(Boolean b, int i, int j, int k, int l) {\n" + 
				"        if (b.booleanValue()) {\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"            j++;\n" + 
				"            // keep this comment\n" + 
				"            l++;\n" + 
				"        } else {\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"            k++;\n" + 
				"            // keep this comment\n" + 
				"            l++;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    /** only common code, Remove if statement */\n" + 
				"    public void ifElseRemoveIfSeveralStatements(Boolean b, int i, int j) {\n" + 
				"        if (b.booleanValue()) {\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"            j++;\n" + 
				"        } else {\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"            j++;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    /** not all cases covered, Do not remove anything */\n" + 
				"    public void ifElseIfNoElseDoNotTouch(Boolean b, int i, int j) {\n" + 
				"        if (b.booleanValue()) {\n" + 
				"            i++;\n" + 
				"            j++;\n" + 
				"        } else if (!b.booleanValue()) {\n" + 
				"            i++;\n" + 
				"            j++;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    /** only common code: remove if statement */\n" + 
				"    public void ifElseIfElseRemoveIf(Boolean b, int i, int j) {\n" + 
				"        if (b.booleanValue()) {\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"            j++;\n" + 
				"        } else if (!b.booleanValue()) {\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"            j++;\n" + 
				"        } else {\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"            j++;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public int doNotRefactorDifferentVariablesInReturn(boolean b) {\n" + 
				"        if (b) {\n" + 
				"            int i = 1;\n" + 
				"            return i;\n" + 
				"        } else {\n" + 
				"            int i = 2;\n" + 
				"            return i;\n" + 
				"        }\n" + 
				"    }\n" + 
				"}").toCompilationUnit("package com.example;\n" + 
						"\n" + 
						"public class CommonCodeInIfElseStatementSample {\n" + 
						"\n" + 
						"    /** no code at all, remove all */\n" + 
						"    public void emptyIfOrElseClauses(Boolean b, int i, int j) {\n" + 
						"        if (b.booleanValue()) {\n" + 
						"            System.out.println();\n" + 
						"        } else {\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    /** no common code, Do not remove anything */\n" + 
						"    public void ifElseRemoveIf(Boolean b, int i, int j) {\n" + 
						"        if (b.booleanValue()) {\n" + 
						"            i++;\n" + 
						"        } else {\n" + 
						"            j++;\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    /** common code: i++, Remove if statement */\n" + 
						"    public void ifElseRemoveIfNoBrackets(Boolean b, int i) {\n" + 
						"        // keep this comment\n" + 
						"        i++;\n" + 
						"    }\n" + 
						"\n" + 
						"    /** common code: i++, Remove if statement */\n" + 
						"    public void ifElseRemoveIf(Boolean b, int i) {\n" + 
						"        // keep this comment\n" + 
						"        i++;\n" + 
						"    }\n" + 
						"\n" + 
						"    /** common code: i++, Remove then case */\n" + 
						"    public void ifElseRemoveThen(Boolean b, int i, int j) {\n" + 
						"        // keep this comment\n" + 
						"        i++;\n" + 
						"        if (!b.booleanValue()) {\n" + 
						"            j++;\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    /** common code: i++, Remove else case */\n" + 
						"    public void ifElseRemoveElse(Boolean b, int i, int j) {\n" + 
						"        // keep this comment\n" + 
						"        i++;\n" + 
						"        if (b.booleanValue()) {\n" + 
						"            j++;\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    /**\n" + 
						"     * common code: put i++ before if statement, put l++ after if statement. Do\n" + 
						"     * not remove if statement.\n" + 
						"     */\n" + 
						"    public void ifElseRemoveIf(Boolean b, int i, int j, int k, int l) {\n" + 
						"        // keep this comment\n" + 
						"        i++;\n" + 
						"        if (b.booleanValue()) {\n" + 
						"            j++;\n" + 
						"        } else {\n" + 
						"            k++;\n" + 
						"        }\n" + 
						"        // keep this comment\n" + 
						"        l++;\n" + 
						"    }\n" + 
						"\n" + 
						"    /** only common code, Remove if statement */\n" + 
						"    public void ifElseRemoveIfSeveralStatements(Boolean b, int i, int j) {\n" + 
						"        // keep this comment\n" + 
						"        i++;\n" + 
						"        j++;\n" + 
						"    }\n" + 
						"\n" + 
						"    /** not all cases covered, Do not remove anything */\n" + 
						"    public void ifElseIfNoElseDoNotTouch(Boolean b, int i, int j) {\n" + 
						"        if (b.booleanValue()) {\n" + 
						"            i++;\n" + 
						"            j++;\n" + 
						"        } else if (!b.booleanValue()) {\n" + 
						"            i++;\n" + 
						"            j++;\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    /** only common code: remove if statement */\n" + 
						"    public void ifElseIfElseRemoveIf(Boolean b, int i, int j) {\n" + 
						"        // keep this comment\n" + 
						"        i++;\n" + 
						"        j++;\n" + 
						"    }\n" + 
						"\n" + 
						"    public int doNotRefactorDifferentVariablesInReturn(boolean b) {\n" + 
						"        if (b) {\n" + 
						"            int i = 1;\n" + 
						"            return i;\n" + 
						"        } else {\n" + 
						"            int i = 2;\n" + 
						"            return i;\n" + 
						"        }\n" + 
						"    }\n" + 
						"}");
	}
	
	@Test public void CommonIfInIfElseTest() {
		trimming("package com.example;\n" + 
				"\n" + 
				"public class CommonIfInIfElseSample {\n" + 
				"\n" + 
				"    public void refactorCommonInnerIf(boolean b1, boolean b2) throws Exception {\n" + 
				"        if (b1) {\n" + 
				"            if (b2) {\n" + 
				"                // keep this comment\n" + 
				"                System.out.println(b1);\n" + 
				"            }\n" + 
				"        } else {\n" + 
				"            if (b2) {\n" + 
				"                // keep this comment\n" + 
				"                System.out.println(!b1);\n" + 
				"            }\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void doNotRefactorBecauseOfInnerElse1(boolean b1, boolean b2) throws Exception {\n" + 
				"        if (b1) {\n" + 
				"            if (b2) {\n" + 
				"                System.out.println(b2);\n" + 
				"            } else {\n" + 
				"                System.out.println(b1);\n" + 
				"            }\n" + 
				"        } else {\n" + 
				"            if (b2) {\n" + 
				"                System.out.println(!b1);\n" + 
				"            }\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void doNotRefactorBecauseOfInnerElse2(boolean b1, boolean b2) throws Exception {\n" + 
				"        if (b1) {\n" + 
				"            if (b2) {\n" + 
				"                System.out.println(b1);\n" + 
				"            }\n" + 
				"        } else {\n" + 
				"            if (b2) {\n" + 
				"                System.out.println(b2);\n" + 
				"            } else {\n" + 
				"                System.out.println(!b1);\n" + 
				"            }\n" + 
				"        }\n" + 
				"    }\n" + 
				"}").toCompilationUnit("package com.example;\n" + 
						"\n" + 
						"public class CommonIfInIfElseSample {\n" + 
						"\n" + 
						"    public void refactorCommonInnerIf(boolean b1, boolean b2) throws Exception {\n" + 
						"        if (b2) {\n" + 
						"            if (b1) {\n" + 
						"                // keep this comment\n" + 
						"                System.out.println(b1);\n" + 
						"            } else {\n" + 
						"                // keep this comment\n" + 
						"                System.out.println(!b1);\n" + 
						"            }\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public void doNotRefactorBecauseOfInnerElse1(boolean b1, boolean b2) throws Exception {\n" + 
						"        if (b1) {\n" + 
						"            if (b2) {\n" + 
						"                System.out.println(b2);\n" + 
						"            } else {\n" + 
						"                System.out.println(b1);\n" + 
						"            }\n" + 
						"        } else {\n" + 
						"            if (b2) {\n" + 
						"                System.out.println(!b1);\n" + 
						"            }\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public void doNotRefactorBecauseOfInnerElse2(boolean b1, boolean b2) throws Exception {\n" + 
						"        if (b1) {\n" + 
						"            if (b2) {\n" + 
						"                System.out.println(b1);\n" + 
						"            }\n" + 
						"        } else {\n" + 
						"            if (b2) {\n" + 
						"                System.out.println(b2);\n" + 
						"            } else {\n" + 
						"                System.out.println(!b1);\n" + 
						"            }\n" + 
						"        }\n" + 
						"    }\n" + 
						"}");
	}
	
	@Test public void DeadCodeEliminationTest() {
		trimming("package com.example;\n" + 
				"\n" + 
				"import java.io.FileInputStream;\n" + 
				"import java.io.IOException;\n" + 
				"import java.util.AbstractList;\n" + 
				"\n" + 
				"public class DeadCodeEliminationSample {\n" + 
				"\n" + 
				"    private class Parent {\n" + 
				"        void removeUselessOverride() {\n" + 
				"        }\n" + 
				"        void removeOverrideWithInsignificantAnnotations() {\n" + 
				"        }\n" + 
				"        void doNotRemoveSignificantAnnotation() {\n" + 
				"        }\n" + 
				"        protected void doNotRemoveVisibilityChange() {\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    private class Child extends Parent {\n" + 
				"        @Override\n" + 
				"        void removeUselessOverride() {\n" + 
				"            super.removeUselessOverride();\n" + 
				"        }\n" + 
				"\n" + 
				"        @Override\n" + 
				"        @SuppressWarnings(\"javadoc\")\n" + 
				"        void removeOverrideWithInsignificantAnnotations() {\n" + 
				"            super.removeOverrideWithInsignificantAnnotations();\n" + 
				"        }\n" + 
				"\n" + 
				"        @Deprecated\n" + 
				"        @Override\n" + 
				"        void doNotRemoveSignificantAnnotation() {\n" + 
				"            super.doNotRemoveSignificantAnnotation();\n" + 
				"        }\n" + 
				"\n" + 
				"        @Override\n" + 
				"        public void doNotRemoveVisibilityChange() {\n" + 
				"            super.doNotRemoveVisibilityChange();\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    private int removeEmptyElseClause(boolean b) {\n" + 
				"        int i = 0;\n" + 
				"        if (b) {\n" + 
				"            i++;\n" + 
				"        } else {\n" + 
				"        }\n" + 
				"        return i;\n" + 
				"    }\n" + 
				"\n" + 
				"    private int removeEmptyThenClause(boolean b) {\n" + 
				"        int i = 0;\n" + 
				"        if (b) {\n" + 
				"        } else {\n" + 
				"            i++;\n" + 
				"        }\n" + 
				"        return i;\n" + 
				"    }\n" + 
				"\n" + 
				"    private int removeEmptyIfStatement(boolean b) {\n" + 
				"        int i = 0;\n" + 
				"        if (b) {\n" + 
				"        } else {\n" + 
				"        }\n" + 
				"        return i;\n" + 
				"    }\n" + 
				"\n" + 
				"    private int removeImpossibleIfClauses() {\n" + 
				"        int i = 0;\n" + 
				"        int j = 0;\n" + 
				"        if (true) {\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"        } else {\n" + 
				"            j++;\n" + 
				"        }\n" + 
				"\n" + 
				"        if (true)\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"        else\n" + 
				"            j++;\n" + 
				"\n" + 
				"        if (false) {\n" + 
				"            i++;\n" + 
				"        }\n" + 
				"\n" + 
				"        if (false) {\n" + 
				"            i++;\n" + 
				"        } else {\n" + 
				"            // keep this comment\n" + 
				"            j++;\n" + 
				"        }\n" + 
				"\n" + 
				"        if (false)\n" + 
				"            i++;\n" + 
				"        else\n" + 
				"            // keep this comment\n" + 
				"            j++;\n" + 
				"\n" + 
				"        return i + j;\n" + 
				"    }\n" + 
				"\n" + 
				"    public int removeDeadCodeAfterIfTrueWithReturn(int i) {\n" + 
				"        if (true) {\n" + 
				"            System.out.println(i);\n" + 
				"            return 1;\n" + 
				"        }\n" + 
				"        return 2;\n" + 
				"    }\n" + 
				"\n" + 
				"    public int removeDeadCodeAfterEmbeddedIfTrueWithThrow(int i) {\n" + 
				"        if (true) {\n" + 
				"            if (true) {\n" + 
				"                System.out.println(i);\n" + 
				"                throw new RuntimeException();\n" + 
				"            }\n" + 
				"        }\n" + 
				"        return 2;\n" + 
				"    }\n" + 
				"\n" + 
				"    public int removeDeadCodeAfterIfFalseWithThrow(int i) {\n" + 
				"        if (false) {\n" + 
				"            i++;\n" + 
				"        } else {\n" + 
				"            System.out.println(i);\n" + 
				"            throw new RuntimeException();\n" + 
				"        }\n" + 
				"        return 2;\n" + 
				"    }\n" + 
				"\n" + 
				"    public int doNotRemoveDeadCodeAfterEmbeddedIfTrueNoThrowOrReturn(int i) {\n" + 
				"        if (true) {\n" + 
				"            if (true) {\n" + 
				"                System.out.println(i);\n" + 
				"            }\n" + 
				"        }\n" + 
				"        return 2;\n" + 
				"    }\n" + 
				"\n" + 
				"    public int doNotRemoveAfterIfFalseNoThrowOrReturn(int i) {\n" + 
				"        if (false) {\n" + 
				"            i++;\n" + 
				"        } else {\n" + 
				"            System.out.println(i);\n" + 
				"        }\n" + 
				"        return 2;\n" + 
				"    }\n" + 
				"\n" + 
				"    public int removeDeadCodeAfterEmbeddedIfThrowOrReturn(boolean b, int i) {\n" + 
				"        if (true) {\n" + 
				"            if (b) {\n" + 
				"                toString();\n" + 
				"                return 1;\n" + 
				"            } else {\n" + 
				"                System.out.println(i);\n" + 
				"                throw new RuntimeException();\n" + 
				"            }\n" + 
				"        }\n" + 
				"        return 2;\n" + 
				"    }\n" + 
				"\n" + 
				"    public int doNotRemoveDeadCodeAfterEmbeddedIfNoThrowNOrReturn(boolean b, int i) {\n" + 
				"        if (true) {\n" + 
				"            if (b) {\n" + 
				"                toString();\n" + 
				"            } else {\n" + 
				"                System.out.println(i);\n" + 
				"            }\n" + 
				"        }\n" + 
				"        return 2;\n" + 
				"    }\n" + 
				"\n" + 
				"    private int removeEmptyTryEmptyFinally() {\n" + 
				"        int i = 0;\n" + 
				"        try {\n" + 
				"        } catch (Exception e) {\n" + 
				"            i++;\n" + 
				"        } finally {\n" + 
				"        }\n" + 
				"        return i;\n" + 
				"    }\n" + 
				"\n" + 
				"    private int removeEmptyTryNonEmptyFinally() {\n" + 
				"        int i = 0;\n" + 
				"        try {\n" + 
				"        } catch (Exception e) {\n" + 
				"            i++;\n" + 
				"        } finally {\n" + 
				"            // keep this comment\n" + 
				"            i++;\n" + 
				"        }\n" + 
				"        return i;\n" + 
				"    }\n" + 
				"\n" + 
				"    void removeEmptyStatement(boolean b, String[] args) {\n" + 
				"        ;\n" + 
				"        if (b);\n" + 
				"        if (b);\n" + 
				"        else;\n" + 
				"        if (b) System.out.println(b);\n" + 
				"        else;\n" + 
				"        try {\n" + 
				"            ;\n" + 
				"        } catch (Exception e) {\n" + 
				"            e.printStackTrace();\n" + 
				"        }\n" + 
				"        for (String arg : args);\n" + 
				"        for (int i = 0; i < 10; i++);\n" + 
				"        int i = 0;\n" + 
				"        while (i < 10);\n" + 
				"    }\n" + 
				"\n" + 
				"    void doNotRemoveEmptyStatement(boolean b) {\n" + 
				"        if (b);\n" + 
				"        else System.out.println(b);\n" + 
				"    }\n" + 
				"\n" + 
				"    private void doNotRemoveTryWithResources() throws IOException {\n" + 
				"        try (FileInputStream f = new FileInputStream(\"file.txt\")) {\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    private interface MethodDeclarationWithoutBody {\n" + 
				"        void aMethod();\n" + 
				"    }\n" + 
				"\n" + 
				"    public void doNotRemovePackageAccessedMethodOverride() {\n" + 
				"        MyAbstractList<String> l = new MyAbstractList<>();\n" + 
				"        l.removeRange(0, l.size());\n" + 
				"    }\n" + 
				"\n" + 
				"    private static class MyAbstractList<E> extends AbstractList<E> {\n" + 
				"        @Override\n" + 
				"        public E get(int index) {\n" + 
				"            return null;\n" + 
				"        }\n" + 
				"\n" + 
				"        @Override\n" + 
				"        public int size() {\n" + 
				"            return 0;\n" + 
				"        }\n" + 
				"\n" + 
				"        @Override\n" + 
				"        protected void removeRange(int fromIndex, int toIndex) {\n" + 
				"            super.removeRange(fromIndex, toIndex);\n" + 
				"        }\n" + 
				"    }\n" + 
				"}").toCompilationUnit("package com.example;\n" + 
						"\n" + 
						"import java.io.FileInputStream;\n" + 
						"import java.io.IOException;\n" + 
						"import java.util.AbstractList;\n" + 
						"\n" + 
						"public class DeadCodeEliminationSample {\n" + 
						"\n" + 
						"    private class Parent {\n" + 
						"        void removeUselessOverride() {\n" + 
						"        }\n" + 
						"        void removeOverrideWithInsignificantAnnotations() {\n" + 
						"        }\n" + 
						"        void doNotRemoveSignificantAnnotation() {\n" + 
						"        }\n" + 
						"        protected void doNotRemoveVisibilityChange() {\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    private class Child extends Parent {\n" + 
						"        @Deprecated\n" + 
						"        @Override\n" + 
						"        void doNotRemoveSignificantAnnotation() {\n" + 
						"            super.doNotRemoveSignificantAnnotation();\n" + 
						"        }\n" + 
						"\n" + 
						"        @Override\n" + 
						"        public void doNotRemoveVisibilityChange() {\n" + 
						"            super.doNotRemoveVisibilityChange();\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    private int removeEmptyElseClause(boolean b) {\n" + 
						"        int i = 0;\n" + 
						"        if (b) {\n" + 
						"            i++;\n" + 
						"        }\n" + 
						"        return i;\n" + 
						"    }\n" + 
						"\n" + 
						"    private int removeEmptyThenClause(boolean b) {\n" + 
						"        int i = 0;\n" + 
						"        if (!b) {\n" + 
						"            i++;\n" + 
						"        }\n" + 
						"        return i;\n" + 
						"    }\n" + 
						"\n" + 
						"    private int removeEmptyIfStatement(boolean b) {\n" + 
						"        int i = 0;\n" + 
						"        return i;\n" + 
						"    }\n" + 
						"\n" + 
						"    private int removeImpossibleIfClauses() {\n" + 
						"        int i = 0;\n" + 
						"        int j = 0;\n" + 
						"        {\n" + 
						"            // keep this comment\n" + 
						"            i++;\n" + 
						"        }\n" + 
						"\n" + 
						"        // keep this comment\n" + 
						"        i++;\n" + 
						"\n" + 
						"        {\n" + 
						"            // keep this comment\n" + 
						"            j++;\n" + 
						"        }\n" + 
						"\n" + 
						"        // keep this comment\n" + 
						"        j++;\n" + 
						"\n" + 
						"        return i + j;\n" + 
						"    }\n" + 
						"\n" + 
						"    public int removeDeadCodeAfterIfTrueWithReturn(int i) {\n" + 
						"        {\n" + 
						"            System.out.println(i);\n" + 
						"            return 1;\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public int removeDeadCodeAfterEmbeddedIfTrueWithThrow(int i) {\n" + 
						"        {\n" + 
						"            {\n" + 
						"                System.out.println(i);\n" + 
						"                throw new RuntimeException();\n" + 
						"            }\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public int removeDeadCodeAfterIfFalseWithThrow(int i) {\n" + 
						"        {\n" + 
						"            System.out.println(i);\n" + 
						"            throw new RuntimeException();\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public int doNotRemoveDeadCodeAfterEmbeddedIfTrueNoThrowOrReturn(int i) {\n" + 
						"        {\n" + 
						"            {\n" + 
						"                System.out.println(i);\n" + 
						"            }\n" + 
						"        }\n" + 
						"        return 2;\n" + 
						"    }\n" + 
						"\n" + 
						"    public int doNotRemoveAfterIfFalseNoThrowOrReturn(int i) {\n" + 
						"        {\n" + 
						"            System.out.println(i);\n" + 
						"        }\n" + 
						"        return 2;\n" + 
						"    }\n" + 
						"\n" + 
						"    public int removeDeadCodeAfterEmbeddedIfThrowOrReturn(boolean b, int i) {\n" + 
						"        {\n" + 
						"            if (b) {\n" + 
						"                toString();\n" + 
						"                return 1;\n" + 
						"            } else {\n" + 
						"                System.out.println(i);\n" + 
						"                throw new RuntimeException();\n" + 
						"            }\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    public int doNotRemoveDeadCodeAfterEmbeddedIfNoThrowNOrReturn(boolean b, int i) {\n" + 
						"        {\n" + 
						"            if (b) {\n" + 
						"                toString();\n" + 
						"            } else {\n" + 
						"                System.out.println(i);\n" + 
						"            }\n" + 
						"        }\n" + 
						"        return 2;\n" + 
						"    }\n" + 
						"\n" + 
						"    private int removeEmptyTryEmptyFinally() {\n" + 
						"        int i = 0;\n" + 
						"        return i;\n" + 
						"    }\n" + 
						"\n" + 
						"    private int removeEmptyTryNonEmptyFinally() {\n" + 
						"        int i = 0;\n" + 
						"        {\n" + 
						"            // keep this comment\n" + 
						"            i++;\n" + 
						"        }\n" + 
						"        return i;\n" + 
						"    }\n" + 
						"\n" + 
						"    void removeEmptyStatement(boolean b, String[] args) {\n" + 
						"        if (b) System.out.println(b);\n" + 
						"        int i = 0;\n" + 
						"    }\n" + 
						"\n" + 
						"    void doNotRemoveEmptyStatement(boolean b) {\n" + 
						"        if (b);\n" + 
						"        else System.out.println(b);\n" + 
						"    }\n" + 
						"\n" + 
						"    private void doNotRemoveTryWithResources() throws IOException {\n" + 
						"        try (FileInputStream f = new FileInputStream(\"file.txt\")) {\n" + 
						"        }\n" + 
						"    }\n" + 
						"\n" + 
						"    private interface MethodDeclarationWithoutBody {\n" + 
						"        void aMethod();\n" + 
						"    }\n" + 
						"\n" + 
						"    public void doNotRemovePackageAccessedMethodOverride() {\n" + 
						"        MyAbstractList<String> l = new MyAbstractList<>();\n" + 
						"        l.removeRange(0, l.size());\n" + 
						"    }\n" + 
						"\n" + 
						"    private static class MyAbstractList<E> extends AbstractList<E> {\n" + 
						"        @Override\n" + 
						"        public E get(int index) {\n" + 
						"            return null;\n" + 
						"        }\n" + 
						"\n" + 
						"        @Override\n" + 
						"        public int size() {\n" + 
						"            return 0;\n" + 
						"        }\n" + 
						"\n" + 
						"        @Override\n" + 
						"        protected void removeRange(int fromIndex, int toIndex) {\n" + 
						"            super.removeRange(fromIndex, toIndex);\n" + 
						"        }\n" + 
						"    }\n" + 
						"}");
	}
	
	@Test public void IfElseIfTest() {
		trimming("package com.example;\n" + 
				"\n" + 
				"public class IfElseIfSample {\n" + 
				"\n" + 
				"    public void refactor(boolean b1, boolean b2) throws Exception {\n" + 
				"        if (b1) {\n" + 
				"            // keep this comment\n" + 
				"            System.out.println(b1);\n" + 
				"        } else {\n" + 
				"            if (b2) {\n" + 
				"                // keep this comment\n" + 
				"                System.out.println(b2);\n" + 
				"            }\n" + 
				"        }\n" + 
				"    }\n" + 
				"}").toCompilationUnit("package com.example;\n" + 
						"\n" + 
						"public class IfElseIfSample {\n" + 
						"\n" + 
						"    public void refactor(boolean b1, boolean b2) throws Exception {\n" + 
						"        if (b1) {\n" + 
						"            // keep this comment\n" + 
						"            System.out.println(b1);\n" + 
						"        } else if (b2) {\n" + 
						"            // keep this comment\n" + 
						"            System.out.println(b2);\n" + 
						"        }\n" + 
						"    }\n" + 
						"}");
	}
	
	@Test public void InvertEqualsTest() {
		trimming("package com.example;\n" + 
				"\n" + 
				"public class InvertEqualsSample {\n" + 
				"\n" + 
				"    public static interface Itf {\n" + 
				"        int primitiveConstant = 1;\n" + 
				"        String objConstant = \"fkjfkjf\";\n" + 
				"        String objNullConstant = null;\n" + 
				"        MyEnum enumConstant = MyEnum.NOT_NULL;\n" + 
				"        MyEnum enumNullConstant = null;\n" + 
				"    }\n" + 
				"\n" + 
				"    private static enum MyEnum {\n" + 
				"        NOT_NULL\n" + 
				"    }\n" + 
				"\n" + 
				"    private int primitiveField;\n" + 
				"\n" + 
				"    public boolean invertEquals(Object obj) {\n" + 
				"        return obj.equals(\"\")\n" + 
				"                && obj.equals(Itf.objConstant)\n" + 
				"                && obj.equals(\"\" + Itf.objConstant)\n" + 
				"                && obj.equals(MyEnum.NOT_NULL);\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean doNotInvertEqualsWhenParameterIsNull(Object obj) {\n" + 
				"        return obj.equals(Itf.objNullConstant) && obj.equals(Itf.enumNullConstant);\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean doNotInvertEqualsWithPrimitiveParameter(Object obj) {\n" + 
				"        return obj.equals(1)\n" + 
				"            && obj.equals(Itf.primitiveConstant)\n" + 
				"            && obj.equals(primitiveField);\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean invertEqualsIgnoreCase(String s) {\n" + 
				"        return s.equalsIgnoreCase(\"\")\n" + 
				"                && s.equalsIgnoreCase(Itf.objConstant)\n" + 
				"                && s.equalsIgnoreCase(\"\" + Itf.objConstant);\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean doNotInvertEqualsIgnoreCaseWhenParameterIsNull(String s) {\n" + 
				"        return s.equalsIgnoreCase(Itf.objNullConstant);\n" + 
				"    }\n" + 
				"}").toCompilationUnit("package com.example;\n" + 
						"\n" + 
						"public class InvertEqualsSample {\n" + 
						"\n" + 
						"    public static interface Itf {\n" + 
						"        int primitiveConstant = 1;\n" + 
						"        String objConstant = \"fkjfkjf\";\n" + 
						"        String objNullConstant = null;\n" + 
						"        MyEnum enumConstant = MyEnum.NOT_NULL;\n" + 
						"        MyEnum enumNullConstant = null;\n" + 
						"    }\n" + 
						"\n" + 
						"    private static enum MyEnum {\n" + 
						"        NOT_NULL\n" + 
						"    }\n" + 
						"\n" + 
						"    private int primitiveField;\n" + 
						"\n" + 
						"    public boolean invertEquals(Object obj) {\n" + 
						"        return \"\".equals(obj)\n" + 
						"                && Itf.objConstant.equals(obj)\n" + 
						"                && (\"\" + Itf.objConstant).equals(obj)\n" + 
						"                && MyEnum.NOT_NULL.equals(obj);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean doNotInvertEqualsWhenParameterIsNull(Object obj) {\n" + 
						"        return obj.equals(Itf.objNullConstant) && obj.equals(Itf.enumNullConstant);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean doNotInvertEqualsWithPrimitiveParameter(Object obj) {\n" + 
						"        return obj.equals(1)\n" + 
						"            && obj.equals(Itf.primitiveConstant)\n" + 
						"            && obj.equals(primitiveField);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean invertEqualsIgnoreCase(String s) {\n" + 
						"        return \"\".equalsIgnoreCase(s)\n" + 
						"                && Itf.objConstant.equalsIgnoreCase(s)\n" + 
						"                && (\"\" + Itf.objConstant).equalsIgnoreCase(s);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean doNotInvertEqualsIgnoreCaseWhenParameterIsNull(String s) {\n" + 
						"        return s.equalsIgnoreCase(Itf.objNullConstant);\n" + 
						"    }\n" + 
						"}");
	}
	
	@Test public void PrimitiveWrapperCreationTest() {
		trimming("package com.example;\n" + 
				"\n" + 
				"public class PrimitiveWrapperCreationSample {\n" + 
				"\n" + 
				"    public static void replaceWrapperConstructorsWithValueOf() {\n" + 
				"        // Replace all calls to wrapper constructors with calls to .valueOf() methods\n" + 
				"        byte b = 4;\n" + 
				"        Byte by = new Byte(b);\n" + 
				"        Boolean bo = new Boolean(true);\n" + 
				"        Character c = new Character('c');\n" + 
				"        Double d = new Double(1);\n" + 
				"        Float f1 = new Float(1f);\n" + 
				"        Float f2 = new Float(1d);\n" + 
				"        Long l = new Long(1);\n" + 
				"        short s = 1;\n" + 
				"        Short sh = new Short(s);\n" + 
				"        Integer i = new Integer(1);\n" + 
				"    }\n" + 
				"\n" + 
				"    public static void removeUnnecessaryObjectCreation() {\n" + 
				"        new Byte(\"0\").byteValue();\n" + 
				"        new Boolean(\"true\").booleanValue();\n" + 
				"        new Integer(\"42\").intValue();\n" + 
				"        new Long(\"42\").longValue();\n" + 
				"        // nothing for Short?\n" + 
				"        new Float(\"42.42\").floatValue();\n" + 
				"        new Double(\"42.42\").doubleValue();\n" + 
				"    }\n" + 
				"\n" + 
				"    public static void convertValueOfCallsToParseCallsInPrimitiveContext() {\n" + 
				"        byte by1 = Byte.valueOf(\"0\");\n" + 
				"        byte by2 = Byte.valueOf(\"0\", 10);\n" + 
				"        boolean bo = Boolean.valueOf(\"true\");\n" + 
				"        int i1 = Integer.valueOf(\"42\");\n" + 
				"        int i2 = Integer.valueOf(\"42\", 10);\n" + 
				"        long l1 = Long.valueOf(\"42\");\n" + 
				"        long l2 = Long.valueOf(\"42\", 10);\n" + 
				"        short s1 = Short.valueOf(\"42\");\n" + 
				"        short s2 = Short.valueOf(\"42\", 10);\n" + 
				"        float f = Float.valueOf(\"42.42\");\n" + 
				"        double d = Double.valueOf(\"42.42\");\n" + 
				"    }\n" + 
				"\n" + 
				"    public static void removeUnnecessaryValueOfCallsInPrimitiveContext() {\n" + 
				"        byte by = Byte.valueOf((byte) 0);\n" + 
				"        boolean bo1 = Boolean.valueOf(true);\n" + 
				"        boolean bo2 = Boolean.TRUE;\n" + 
				"        int i = Integer.valueOf(42);\n" + 
				"        long l = Long.valueOf(42);\n" + 
				"        short s = Short.valueOf((short) 42);\n" + 
				"        float f = Float.valueOf(42.42F);\n" + 
				"        double d = Double.valueOf(42.42);\n" + 
				"    }\n" + 
				"\n" + 
				"    public static void removeUnnecessaryConstructorInvocationsInPrimitiveContext() {\n" + 
				"        byte by = new Byte((byte) 0);\n" + 
				"        boolean bo = new Boolean(true);\n" + 
				"        int i = new Integer(42);\n" + 
				"        long l = new Long(42);\n" + 
				"        short s = new Short((short) 42);\n" + 
				"        float f = new Float(42.42F);\n" + 
				"        double d = new Double(42.42);\n" + 
				"    }\n" + 
				"}").toCompilationUnit("package com.example;\n" + 
						"\n" + 
						"public class PrimitiveWrapperCreationSample {\n" + 
						"\n" + 
						"    public static void replaceWrapperConstructorsWithValueOf() {\n" + 
						"        // Replace all calls to wrapper constructors with calls to .valueOf() methods\n" + 
						"        byte b = 4;\n" + 
						"        Byte by = Byte.valueOf(b);\n" + 
						"        Boolean bo = Boolean.valueOf(true);\n" + 
						"        Character c = Character.valueOf('c');\n" + 
						"        Double d = Double.valueOf(1);\n" + 
						"        Float f1 = Float.valueOf(1f);\n" + 
						"        Float f2 = Float.valueOf((float) 1d);\n" + 
						"        Long l = Long.valueOf(1);\n" + 
						"        short s = 1;\n" + 
						"        Short sh = Short.valueOf(s);\n" + 
						"        Integer i = Integer.valueOf(1);\n" + 
						"    }\n" + 
						"\n" + 
						"    public static void removeUnnecessaryObjectCreation() {\n" + 
						"        Byte.parseByte(\"0\");\n" + 
						"        Boolean.valueOf(\"true\");\n" + 
						"        Integer.parseInt(\"42\");\n" + 
						"        Long.parseLong(\"42\");\n" + 
						"        // nothing for Short?\n" + 
						"        Float.parseFloat(\"42.42\");\n" + 
						"        Double.parseDouble(\"42.42\");\n" + 
						"    }\n" + 
						"\n" + 
						"    public static void convertValueOfCallsToParseCallsInPrimitiveContext() {\n" + 
						"        byte by1 = Byte.parseByte(\"0\");\n" + 
						"        byte by2 = Byte.parseByte(\"0\", 10);\n" + 
						"        boolean bo = Boolean.parseBoolean(\"true\");\n" + 
						"        int i1 = Integer.parseInt(\"42\");\n" + 
						"        int i2 = Integer.parseInt(\"42\", 10);\n" + 
						"        long l1 = Long.parseLong(\"42\");\n" + 
						"        long l2 = Long.parseLong(\"42\", 10);\n" + 
						"        short s1 = Short.parseShort(\"42\");\n" + 
						"        short s2 = Short.parseShort(\"42\", 10);\n" + 
						"        float f = Float.parseFloat(\"42.42\");\n" + 
						"        double d = Double.parseDouble(\"42.42\");\n" + 
						"    }\n" + 
						"\n" + 
						"    public static void removeUnnecessaryValueOfCallsInPrimitiveContext() {\n" + 
						"        byte by = (byte) 0;\n" + 
						"        boolean bo1 = true;\n" + 
						"        boolean bo2 = true;\n" + 
						"        int i = 42;\n" + 
						"        long l = 42;\n" + 
						"        short s = (short) 42;\n" + 
						"        float f = 42.42F;\n" + 
						"        double d = 42.42;\n" + 
						"    }\n" + 
						"\n" + 
						"    public static void removeUnnecessaryConstructorInvocationsInPrimitiveContext() {\n" + 
						"        byte by = (byte) 0;\n" + 
						"        boolean bo = true;\n" + 
						"        int i = 42;\n" + 
						"        long l = 42;\n" + 
						"        short s = (short) 42;\n" + 
						"        float f = 42.42F;\n" + 
						"        double d = 42.42;\n" + 
						"    }\n" + 
						"}");
	}
	
	@Test public void PushNegationDownTest() {
		trimming("package com.example;\n" + 
				"\n" + 
				"\n" + 
				"public class PushNegationDownSample {\n" + 
				"\n" + 
				"    public boolean replaceDoubleNegation(boolean b) {\n" + 
				"        return !!b;\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean replaceDoubleNegationWithParentheses(boolean b) {\n" + 
				"        return !(!(b /* another refactoring removes the parentheses */));\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean replaceNegationWithInfixAndOperator(boolean b1, boolean b2, boolean b3) {\n" + 
				"        return !(b1 && b2 && b3); // another refactoring removes the parentheses\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean replaceNegationRevertInnerExpressions(boolean b1, boolean b2) {\n" + 
				"        return !(!b1 && !b2 /* another refactoring removes the parentheses */);\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean replaceNegationLeaveParentheses(boolean b1, boolean b2) {\n" + 
				"        return !(!(b1 && b2 /* another refactoring removes the parentheses */));\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean replaceNegationRemoveParentheses(boolean b1, boolean b2) {\n" + 
				"        return !((!b1) && (!b2));\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean doNotNegateNonBooleanExprs(Object o) {\n" + 
				"        return !(o != null /* another refactoring removes the parentheses */);\n" + 
				"    }\n" + 
				"\n" + 
				"    public boolean doNotNegateNonBooleanPrimitiveExprs(Boolean b) {\n" + 
				"        return !(b != null /* another refactoring removes the parentheses */);\n" + 
				"    }\n" + 
				"}").toCompilationUnit("package com.example;\n" + 
						"\n" + 
						"\n" + 
						"public class PushNegationDownSample {\n" + 
						"\n" + 
						"    public boolean replaceDoubleNegation(boolean b) {\n" + 
						"        return b;\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean replaceDoubleNegationWithParentheses(boolean b) {\n" + 
						"        return (b /* another refactoring removes the parentheses */);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean replaceNegationWithInfixAndOperator(boolean b1, boolean b2, boolean b3) {\n" + 
						"        return (!b1 || !b2 || !b3); // another refactoring removes the parentheses\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean replaceNegationRevertInnerExpressions(boolean b1, boolean b2) {\n" + 
						"        return (b1 || b2 /* another refactoring removes the parentheses */);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean replaceNegationLeaveParentheses(boolean b1, boolean b2) {\n" + 
						"        return (b1 && b2 /* another refactoring removes the parentheses */);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean replaceNegationRemoveParentheses(boolean b1, boolean b2) {\n" + 
						"        return (b1 || b2);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean doNotNegateNonBooleanExprs(Object o) {\n" + 
						"        return (o == null /* another refactoring removes the parentheses */);\n" + 
						"    }\n" + 
						"\n" + 
						"    public boolean doNotNegateNonBooleanPrimitiveExprs(Boolean b) {\n" + 
						"        return (b == null /* another refactoring removes the parentheses */);\n" + 
						"    }\n" + 
						"}");
	}
}
