package il.org.spartan.Leonidas.plugin.tippers;

import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * Created by  on 4/29/2017.
 */
public class LeonidasTipperTest extends PsiTypeHelper {
    public void testCanTip() throws Exception {

    }

    public void testDescription() throws Exception {

    }

    public void testTip() throws Exception {

    }

    public void testGetPsiClass() throws Exception {

    }


    /*
    private LeonidasTipper $;
    private PsiTreeTipperBuilder builderMock = Mockito.mock(PsiTreeTipperBuilder.class);
    private File fileMock = Mockito.mock(File.class);
    private Class<? extends PsiElement> testClassType = PsiConditionalExpression.class;
    private String statement = "System.out.println(\"Hello, World!\")";
    private String expression = "69 > 69 ? 69 : 69";
    private String testFileName = "testFileName.java";
    @SuppressWarnings("FieldCanBeLocal")
    private String mockDescription = "This is just for fun. TEAM 8 IS THE BEST!!!";

    public LeonidasTipperTest() {
        configureMocks();
    }

    private void configureMocks() {
        Mockito.when(fileMock.getName()).thenReturn(testFileName);
        try {
            Mockito.when(builderMock.buildTipperPsiTree(fileMock.getName())).thenReturn(builderMock);
        } catch (IOException ignore) {
        }


    }

    private LeonidasTipper createTestObject() {
        configureMocks();
        return new LeonidasTipper(builderMock, fileMock);
    }

    public void testBuilderExceptionToIncreaseCoverage() throws Exception {
        PsiTreeTipperBuilder b = Mockito.mock(PsiTreeTipperBuilder.class);
        Mockito.when(b.buildTipperPsiTree(testFileName)).thenThrow(IOException.class);
        $ = new LeonidasTipper(b, fileMock);

    }

    public void testCanTip() {
        $ = createTestObject();
        Mockito.when(builderMock.getFromPsiTree()).thenReturn(Encapsulator.buildTreeFromPsi(createTestStatementFromString(statement)));
        assert $.canTip(createTestStatementFromString(statement));
    }

    public void testCanNotTip() {
        $ = createTestObject();
        Mockito.when(builderMock.getFromPsiTree()).thenReturn(Encapsulator.buildTreeFromPsi(createTestStatementFromString(statement)));
        assert !$.canTip(createTestExpressionFromString(expression));
    }

    public void testGetPsiClass() {
        $ = createTestObject();
        Mockito.when(builderMock.getRootElementType()).then(x -> testClassType);
        assertEquals(testClassType, $.getPsiClass());
    }


    public void testDescription() {
        $ = createTestObject();
        assertEquals(testFileName, $.description());
    }

    public void testDescriptionOfElement() {
        $ = createTestObject();
        Mockito.when(builderMock.getDescription()).thenReturn(mockDescription);
        assertEquals(mockDescription, $.description(createTestExpressionFromString(expression)));
    }
*/


}