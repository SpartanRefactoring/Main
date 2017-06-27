package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.psi.PsiConditionalExpression;
import il.org.spartan.Leonidas.PsiTypeHelper;

import java.util.Arrays;


/**
 * @author Oren Afek
 * @since 2016.12.26
 */


public class DefaultToTest extends PsiTypeHelper {

    private final String legalCaseString1 = "x != null ? x : y";
    private final String legalCaseString2 = "x == null ? y : x";
    private final String legalCaseString3 = "null == x ? y : x";
    private final String legalCaseString4 = "null != x ? x : y";

    private final String[] legalCasesStrings = {legalCaseString1, legalCaseString2, legalCaseString3, legalCaseString4};
    private final String legalReplacement = "defaults(x).to(y)";

    public void testCanTipFirstElementIsNotNullSecondIsNEOperator() {
        assert new DefaultsTo().canTip(createTestExpressionFromString(legalCaseString1));
    }

    public void testCanTipFirstElementIsNotNullSecondIsEQOperator() {
        assert new DefaultsTo().canTip(createTestExpressionFromString(legalCaseString2));
    }

    public void testCanTipFirstElementIsNullSecondIsNotEQOperator() {
        assert new DefaultsTo().canTip(createTestExpressionFromString(legalCaseString3));
    }

    public void testCanTipFirstElementIsNullSecondIsNotNEOperator() {
        assert new DefaultsTo().canTip(createTestExpressionFromString(legalCaseString4));
    }

    public void testCannotTipTwoElementsAreNullEQOperator() {
        assert !new DefaultsTo().canTip(createTestExpressionFromString("null == null ? x : y"));
    }

    public void testCannotTipTwoElementsAreNullNEOperator() {
        assert !new DefaultsTo().canTip(createTestExpressionFromString("null != null ? x : y"));
    }

    public void testCannotTipTwoElementsAreNotNullEQOperator() {
        assert !new DefaultsTo().canTip(createTestExpressionFromString("x == y ? x : y"));
    }

    public void testCannotTipTwoElementsAreNotNullNEOperator() {
        assert !new DefaultsTo().canTip(createTestExpressionFromString("x != y ? x : y"));
    }

    public void testCannotTipCondVarsAndThenElseVarsDoNotMatch() {
        assert !new DefaultsTo().canTip(createTestExpressionFromString("x != null ? y : z"));
    }

    public void testCannotTipOperatorAndOrderMismatch1() {
        assert !new DefaultsTo().canTip(createTestExpressionFromString("x != null ? y : x"));
    }

    public void testCannotTipOperatorAndOrderMismatch2() {
        assert !new DefaultsTo().canTip(createTestExpressionFromString("x == null ? x : y"));
    }

    public void testCannotTipOperatorAndOrderMismatch3() {
        assert !new DefaultsTo().canTip(createTestExpressionFromString("null == x ? x : y"));
    }

    public void testCannotTipOperatorAndOrderMismatch4() {
        assert !new DefaultsTo().canTip(createTestExpressionFromString("null != x ? y : x"));
    }

    public void testCreateReplacementLegalCases() {
        Arrays.stream(legalCasesStrings).forEach(λ ->
                assertEqualsByText(new DefaultsTo().createReplacement(
                        createConditionalExpressionFromLegalString(λ)),
                        createTestExpression(legalReplacement)));
    }

    private PsiConditionalExpression createConditionalExpressionFromLegalString(String conditionalString) {
        int indexOfQuestionMark = conditionalString.indexOf('?'), indexOfColon = conditionalString.indexOf(':');
        return createTestConditionalExpression(conditionalString.substring(0, indexOfQuestionMark).trim(),
				conditionalString.substring(indexOfQuestionMark + 1, indexOfColon).trim(),
				conditionalString.substring(indexOfColon + 1).trim());
    }

}
