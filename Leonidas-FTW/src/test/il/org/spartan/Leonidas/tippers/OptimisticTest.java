package il.org.spartan.Leonidas.tippers;

import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.auxilary_layer.CompilationCenter;
import il.org.spartan.Leonidas.auxilary_layer.haz;
import il.org.spartan.spartanizer.ast.factory.make;

/**
 * @author Roey Maor
 * @since 04-01-2017.
 */
public class OptimisticTest extends PsiTypeHelper {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        make.ast("void foo() {}");
        CompilationCenter.initialize();
    }

    public void testDetectsCompilationErrors() throws Exception {
        assert haz.syntaxErrors(createTestMethodFromString("boolean dummy(){return (5=!=5);}"));
        assert haz.compilationErrors(createTestFileFromString("package test; public class Test { public Test() { final int x=3; x=5; System.out.println(\"banana\"); } }"));
    }

    public void testNoCompilationErrors(){
        assert !haz.syntaxErrors(createTestExpressionFromString("5 == 5"));
        assert !haz.syntaxErrors(createTestMethodFromString("boolean dummy(){return false;}"));
        assert !haz.syntaxErrors(createTestMethodFromString("boolean dummy(){return (5==5);}"));
        assert !haz.compilationErrors(createTestFileFromString("package test; public class Test { public Test() { final int x=3; System.out.println(\"lalala\"); } }"));

    }

}
