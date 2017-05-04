package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIfStatement;
import com.intellij.psi.PsiMethodCallExpression;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.auxilary_layer.step;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Encapsulator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.KeyDescriptionParameters.ID;

/**
 * @author michalcohen
 * @since 26-04-2017.
 */
public class MatcherTest extends PsiTypeHelper {
    public void testMatch() throws Exception {
        Map<Integer, List<Matcher.Constraint>> constrains = new HashMap<>();
        PsiIfStatement ifs = createTestIfStatement("booleanExpression(0)", "statement(1);\n statement(2);");
        Encapsulator n = buildTemplate(ifs);
        constrains.putIfAbsent(1, new LinkedList<>());
        constrains.putIfAbsent(2, new LinkedList<>());
        constrains.putIfAbsent(3, new LinkedList<>());
        Encapsulator firstConstraint = buildTemplate(createTestIfStatement("booleanExpression(3)", "statement(4);"));
        Encapsulator secondConstraint = buildTemplate(createTestReturnStatement("null"));
        Encapsulator thirdConstraint = buildTemplate(createTestExpression("!booleanExpression(5)"));
        Encapsulator forthConstraint = buildTemplate(createTestReturnStatement("null"));

        constrains.get(1).add(new Matcher.Constraint(Matcher.Constraint.ConstraintType.IS_NOT, firstConstraint));
        constrains.get(2).add(new Matcher.Constraint(Matcher.Constraint.ConstraintType.IS, secondConstraint));
        constrains.get(3).add(new Matcher.Constraint(Matcher.Constraint.ConstraintType.IS_NOT, thirdConstraint));
        constrains.get(1).add(new Matcher.Constraint(Matcher.Constraint.ConstraintType.IS_NOT, forthConstraint));

        Matcher m = new Matcher(n, constrains);
        PsiIfStatement tm1 = createTestIfStatement("x > 2", "\nx++; \nreturn null;");
        assertTrue(m.match(tm1));
        PsiIfStatement tm2 = createTestIfStatement("x > 2", "\nif(!(x > 4)){x--;} \nreturn null;");
        assertTrue(m.match(tm2));
        PsiIfStatement tm3 = createTestIfStatement("x > 2", "\nx++; \nx--;");
        assertFalse(m.match(tm3));
        PsiIfStatement tm4 = createTestIfStatement("x > 2", "\nreturn null; \nreturn null;");
        assertFalse(m.match(tm4));
        PsiIfStatement tm5 = createTestIfStatement("x > 2", "\nif(x < 3){x--;} \nreturn null;");
        assertFalse(m.match(tm5));
        PsiIfStatement tm6 = createTestIfStatement("x > 2", "\nif(x > 4){x--;} \nreturn null;");
        assertFalse(m.match(tm6));
    }

    public void testExtractInfo() throws Exception {
        Map<Integer, List<Matcher.Constraint>> constrains = new HashMap<>();
        PsiIfStatement ifs = createTestIfStatement("booleanExpression(0)", "statement(1);\n statement(2);");
        Encapsulator n = buildTemplate(ifs);
        constrains.putIfAbsent(1, new LinkedList<>());
        constrains.putIfAbsent(2, new LinkedList<>());
        constrains.putIfAbsent(3, new LinkedList<>());
        Encapsulator firstConstraint = buildTemplate(createTestIfStatement("booleanExpression(3)", "statement(4);"));
        Encapsulator secondConstraint = buildTemplate(createTestReturnStatement("null"));
        Encapsulator thirdConstraint = buildTemplate(createTestExpression("!booleanExpression(5)"));
        Encapsulator forthConstraint = buildTemplate(createTestReturnStatement("null"));

        constrains.get(1).add(new Matcher.Constraint(Matcher.Constraint.ConstraintType.IS_NOT, firstConstraint));
        constrains.get(2).add(new Matcher.Constraint(Matcher.Constraint.ConstraintType.IS, secondConstraint));
        constrains.get(3).add(new Matcher.Constraint(Matcher.Constraint.ConstraintType.IS_NOT, thirdConstraint));
        constrains.get(1).add(new Matcher.Constraint(Matcher.Constraint.ConstraintType.IS_NOT, forthConstraint));

        Matcher m = new Matcher(n, constrains);
        PsiIfStatement tm1 = createTestIfStatement("x > 2", "\nx++; \nreturn null;");
        Map<Integer, PsiElement> map = m.extractInfo(tm1);
        assertEquals(map.get(0).getText(), "x > 2");
        assertEquals(map.get(1).getText(), "x++;");
        assertEquals(map.get(2).getText(), "return null;");
        PsiIfStatement tm2 = createTestIfStatement("x > 2", "\nif(!(x > 4)){x--;} \nreturn null;");
        map = m.extractInfo(tm2);
        assertEquals(map.get(1).getText(), "if(!(x > 4)){x--;}");
        assertEquals(map.get(2).getText(), "return null;");
        PsiIfStatement tm3 = createTestIfStatement("x > 2", "\nx++; \nx--;");
        map = m.extractInfo(tm3);
        assertEquals(map.get(1).getText(), "x++;");
        assertEquals(map.get(2).getText(), "x--;");
        PsiIfStatement tm4 = createTestIfStatement("x > 2", "\nreturn null; \nreturn null;");
        map = m.extractInfo(tm4);
        assertEquals(map.get(1).getText(), "return null;");
        assertEquals(map.get(2).getText(), "return null;");
        PsiIfStatement tm5 = createTestIfStatement("x > 2", "\nif(x < 3){x--;} \nreturn null;");
        map = m.extractInfo(tm5);
        assertEquals(map.get(1).getText(), "if(x < 3){x--;}");
        assertEquals(map.get(2).getText(), "return null;");
        PsiIfStatement tm6 = createTestIfStatement("x > 2", "\nif(x > 4){x--;} \nreturn null;");
        map = m.extractInfo(tm6);
        assertEquals(map.get(1).getText(), "if(x > 4){x--;}");
        assertEquals(map.get(2).getText(), "return null;");
    }

    private void giveIdToStubMethodCalls(PsiElement innerTree) {
        innerTree.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (!iz.stubMethodCall(expression)) {
                    return;
                }
                expression.putUserData(ID, az.integer(step.firstParamterExpression(expression)));
            }
        });
    }

    private Encapsulator buildTemplate(PsiElement e) {
        giveIdToStubMethodCalls(e);
        return Pruning.prune(Encapsulator.buildTreeFromPsi(e));
    }
}