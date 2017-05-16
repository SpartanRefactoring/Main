package il.org.spartan.Leonidas.tippers;

import com.intellij.psi.PsiConditionalExpression;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.tippers.SafeReference;

/**
 * @author amirsagiv
 * @since 24-12-2016.
 */
public class SafeReferenceTest extends PsiTypeHelper {

    public void testFirstScenarioLegal() {
        assert new SafeReference().canTip(createTestExpressionFromString("x == null ? null : x.y"));
        assertEquals("nullConditional(x , ¢ -> ¢.y)",new SafeReference().createReplacement((PsiConditionalExpression)createTestExpressionFromString("x == null ? null : x.y")).getText());
    }

    public void testSecondScenarioLegal() {
        assert new SafeReference().canTip(createTestExpressionFromString("null == x ? null : x.y"));
        assertEquals("nullConditional(x , ¢ -> ¢.y)",new SafeReference().createReplacement((PsiConditionalExpression)createTestExpressionFromString("null == x ? null : x.y")).getText());
    }

    public void testThirdScenarioLegal() {
        assert new SafeReference().canTip(createTestExpressionFromString("x != null ? x.y : null"));
        assertEquals("nullConditional(x , ¢ -> ¢.y)",new SafeReference().createReplacement((PsiConditionalExpression)createTestExpressionFromString("x != null ? x.y : null")).getText());
    }

    public void testFourthScenarioLegal() {
        assert new SafeReference().canTip(createTestExpressionFromString("null != x ? x.y : null"));
        assertEquals("nullConditional(x , ¢ -> ¢.y)",new SafeReference().createReplacement((PsiConditionalExpression)createTestExpressionFromString("null != x ? x.y : null")).getText());
    }

    public void testCanTipIllegal1() {
        assert !new SafeReference().canTip(createTestExpressionFromString("x == null ? null : null"));
    }

    public void testCanTipIllegal2() {
        assert !new SafeReference().canTip(createTestExpressionFromString("x == null ? x.y : null"));

    }

    public void testCanTipIllegal3() {
        assert !new SafeReference().canTip(createTestExpressionFromString("x != null ? null : x.y"));
    }

    public void testCanTipIllegal4() {
        assert !new SafeReference().canTip(createTestExpressionFromString("x != null ? null : null"));
    }

    public void testCanTipIllegal5() {
        assert !new SafeReference().canTip(createTestExpressionFromString("y == null ? null : x.y"));
    }

    public void testCanTipIllegal6() {
        assert !new SafeReference().canTip(createTestExpressionFromString("y != null ? x.y: null"));
    }

    public void testCanTipIllegal7() {
        assert !new SafeReference().canTip(createTestExpressionFromString("x >= null ? x.y: null"));
    }

    public void testCanTipIllegal8() {
        assert !new SafeReference().canTip(createTestExpressionFromString("null < x ? x.y: null"));
    }

    public void testCanTipIllegal9() {
        assert !new SafeReference().canTip(createTestExpressionFromString("x == null ? null: a.y"));
    }

    public void testCanTipIllegal10() {
        assert !new SafeReference().canTip(createTestExpressionFromString("x != null ? a.y: null"));
    }

    public void testCanTipIllegal11() {
        assert !new SafeReference().canTip(createTestExpressionFromString("null == x ? null: a.y"));
    }

    public void testCanTipIllegal12() {
        assert !new SafeReference().canTip(createTestExpressionFromString("null != x ? a.y: null"));
    }

    public void testFirstScenarioWithLegalMethod(){
        assert new SafeReference().canTip(createTestExpressionFromString("x == null ? null : x.y()"));
        assertEquals("nullConditional(x , ¢ -> ¢.y())",new SafeReference().createReplacement((PsiConditionalExpression)createTestExpressionFromString("x == null ? null : x.y()")).getText());
    }

    public void testSecondScenarioWithLegalMethod(){
        assert new SafeReference().canTip(createTestExpressionFromString("null == x ? null : x.y()"));
        assertEquals("nullConditional(x , ¢ -> ¢.y())",new SafeReference().createReplacement((PsiConditionalExpression)createTestExpressionFromString("null == x ? null : x.y()")).getText());
    }

    public void testThirdScenarioWithLegalMethod(){
        assert new SafeReference().canTip(createTestExpressionFromString("x != null ? x.y() : null"));
        assertEquals("nullConditional(x , ¢ -> ¢.y())",new SafeReference().createReplacement((PsiConditionalExpression)createTestExpressionFromString("x != null ? x.y() : null")).getText());
    }

    public void testFourthScenarioWithLegalMethod(){
        assert new SafeReference().canTip(createTestExpressionFromString("null != x ? x.y() : null"));
        assertEquals("nullConditional(x , ¢ -> ¢.y())",new SafeReference().createReplacement((PsiConditionalExpression)createTestExpressionFromString("null != x ? x.y() : null")).getText());
    }

    //Notice!! these next tests only pass because I limited the pattern to work with methods with zero params. delete
    // if you make it work with any method!
    public void testCanTipIllegal13() {
        assert !new SafeReference().canTip(createTestExpressionFromString("x == null ? null : x.y(p1)"));
    }

    public void testCanTipIllegal14() {
        assert !new SafeReference().canTip(createTestExpressionFromString("x == null ? null : x.y(p1,p2)"));
    }

    public void testCanTipIllegal15() {
        assert !new SafeReference().canTip(createTestExpressionFromString("x != null ? x.y(p1) : null"));
    }

    public void testCanTipIllegal16() {
        assert !new SafeReference().canTip(createTestExpressionFromString("x != null ? x.y(p1 ,p2) : null"));
    }

    public void testCanTipIllegal17() {
        assert !new SafeReference().canTip(createTestExpressionFromString("x == null ? x.y() : x.y(p1)"));
    }

    public void testCanTipIllegal18() {
        assert !new SafeReference().canTip(createTestExpressionFromString("x != null ? x.y(p1 ,p2) : x.y"));
    }




}
