package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.*;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub.StubName;


import java.util.Optional;
import java.util.function.BinaryOperator;


/**
 * @author Oren Afek, michalcohen
 * @since 17-01-2017
 */
public class StubNameTest extends PsiTypeHelper {
    public void testValueOfMethodCall() throws Exception {
        PsiMethodCallExpression m1 = createTestMethodCallExpression("statement", "1");
        PsiMethodCallExpression m2 = createTestMethodCallExpression("booleanExpression", "5");
        PsiMethodCallExpression m3 = createTestMethodCallExpression("identifier", "0");
        PsiMethodCallExpression m4 = createTestMethodCallExpression("arrayIdentifier", "3");
        PsiMethodCallExpression m5 = createTestMethodCallExpression("anyBlock", "6");
        assertEquals(StubName.valueOfMethodCall(m1), StubName.STATEMENT);
        assertEquals(StubName.valueOfMethodCall(m2), StubName.BOOLEAN_EXPRESSION);
        assertEquals(StubName.valueOfMethodCall(m3), StubName.IDENTIFIER);
        assertEquals(StubName.valueOfMethodCall(m4), StubName.ARRAY_IDENTIFIER);
        assertEquals(StubName.valueOfMethodCall(m5), StubName.ANY_BLOCK);
    }

    public void testValueOfStringExpression() throws Exception {
        PsiMethodCallExpression m1 = createTestMethodCallExpression("statement");
        PsiMethodCallExpression m2 = createTestMethodCallExpression("booleanExpression");
        PsiMethodCallExpression m3 = createTestMethodCallExpression("identifier");
        PsiMethodCallExpression m4 = createTestMethodCallExpression("arrayIdentifier");
        PsiMethodCallExpression m5 = createTestMethodCallExpression("anyBlock");
        assertEquals(StubName.valueOfStringExpression(m1.getText()), StubName.STATEMENT);
        assertEquals(StubName.valueOfStringExpression(m2.getText()), StubName.BOOLEAN_EXPRESSION);
        assertEquals(StubName.valueOfStringExpression(m3.getText()), StubName.IDENTIFIER);
        assertEquals(StubName.valueOfStringExpression(m4.getText()), StubName.ARRAY_IDENTIFIER);
        assertEquals(StubName.valueOfStringExpression(m5.getText()), StubName.ANY_BLOCK);
    }

    public void testGetGeneralTye() throws Exception {
        PsiStatement s = createTestStatementFromString("x++;");
        PsiExpression e = createTestExpression("x--");
        PsiCodeBlock cb = createTestCodeBlockFromString("{\n\tx++;\n\tx--;\n}");
        PsiIdentifier id = createTestIdentifierFromString("y");
        PsiArrayAccessExpression aa = createTestArrayaAccess("a", "x++");

        assertEquals(StubName.getGeneralType(s), StubName.STATEMENT);
        assertEquals(StubName.getGeneralType(e), StubName.BOOLEAN_EXPRESSION);
        assertEquals(StubName.getGeneralType(cb), StubName.ANY_BLOCK);
        assertEquals(StubName.getGeneralType(id), StubName.IDENTIFIER);
        assertEquals(StubName.getGeneralType(aa), StubName.ARRAY_IDENTIFIER);
    }

    public void testStubMethodCallExpression() throws Exception {
        assertEquals(StubName.STATEMENT.stubMethodCallExpression(), "statement()");
        assertEquals(StubName.BOOLEAN_EXPRESSION.stubMethodCallExpression(), "booleanExpression()");
        assertEquals(StubName.ANY_BLOCK.stubMethodCallExpression(), "anyBlock()");
        assertEquals(StubName.IDENTIFIER.stubMethodCallExpression(), "identifier()");
        assertEquals(StubName.ARRAY_IDENTIFIER.stubMethodCallExpression(), "arrayIdentifier()");
    }

    public void testStubMethodCallExpressionStatement() throws Exception {
        assertEquals(StubName.STATEMENT.stubMethodCallExpressionStatement(), "statement();");
        assertEquals(StubName.BOOLEAN_EXPRESSION.stubMethodCallExpressionStatement(), "booleanExpression();");
        assertEquals(StubName.ANY_BLOCK.stubMethodCallExpressionStatement(), "anyBlock();");
        assertEquals(StubName.IDENTIFIER.stubMethodCallExpressionStatement(), "identifier();");
        assertEquals(StubName.ARRAY_IDENTIFIER.stubMethodCallExpressionStatement(), "arrayIdentifier();");

    }

    public void testGetGenericPsiType() throws Exception {
        PsiMethodCallExpression m1 = createTestMethodCallExpression("statement", "1");
        PsiMethodCallExpression m2 = createTestMethodCallExpression("booleanExpression", "5");
        PsiMethodCallExpression m3 = createTestMethodCallExpression("identifier", "0");

        assertTrue(iz.generic(StubName.STATEMENT.getGenericElement(m1, 1)));
        assertTrue(iz.generic(StubName.BOOLEAN_EXPRESSION.getGenericElement(m2, 5)));
        assertTrue(iz.generic(StubName.ANY_BLOCK.getGenericElement(m3, 0)));

        assertEquals(StubName.STATEMENT.getGenericElement(m1, 1).getClass(), GenericStatement.class);
        assertEquals(StubName.BOOLEAN_EXPRESSION.getGenericElement(m2, 5).getClass(), GenericExpression.class);
        assertEquals(StubName.ANY_BLOCK.getGenericElement(m3, 0).getClass(), GenericBlock.class);

        assert (StubName.STATEMENT.getGenericElement(m1, 1).getUserData(KeyDescriptionParameters.ID) == 1);
        assert (StubName.BOOLEAN_EXPRESSION.getGenericElement(m2, 5).getUserData(KeyDescriptionParameters.ID) == 5);
        assert (StubName.ANY_BLOCK.getGenericElement(m3, 0).getUserData(KeyDescriptionParameters.ID) == 0);
    }

    public void testGoUpwards1() throws Exception {
        Encapsulator root = Encapsulator.buildTreeFromPsi(createTestIfStatement("x > 2", "booleanExpression(0);"));
        Encapsulator expression = root.accept(new EncapsulatingNodeValueVisitor<Optional<Encapsulator>>() {
            @Override
            public Optional<Encapsulator> visit(Encapsulator n) {
                return Optional.ofNullable(iz.expression(n.getInner()) && n.getInner().getText().equals("booleanExpression(0)") ? n : null);
            }
        }, new BinaryOperator<Optional<Encapsulator>>() {
            @Override
            public Optional<Encapsulator> apply(Optional<Encapsulator> t, Optional<Encapsulator> t2) {
                return t == null || !t.isPresent() ? (t2 == null ? Optional.empty() : t2) : t;
            }
        }).orElse(null);
        assertFalse(StubName.BOOLEAN_EXPRESSION.goUpwards(expression, expression.getParent()));
    }

    public void testGoUpwards2() throws Exception {
        Encapsulator root = Encapsulator.buildTreeFromPsi(createTestIfStatement("x > 2", "statement(0);"));
        Encapsulator expression = root.accept(new EncapsulatingNodeValueVisitor<Optional<Encapsulator>>() {
            @Override
            public Optional<Encapsulator> visit(Encapsulator n) {
                return Optional.ofNullable(iz.expression(n.getInner()) && n.getInner().getText().equals("statement(0)") ? n : null);
            }
        }, new BinaryOperator<Optional<Encapsulator>>() {
            @Override
            public Optional<Encapsulator> apply(Optional<Encapsulator> t, Optional<Encapsulator> t2) {
                return t == null || !t.isPresent() ? (t2 == null ? Optional.empty() : t2) : t;
            }
        }).orElse(null);
        assertTrue(StubName.STATEMENT.goUpwards(expression, expression.getParent()));
    }

    public void testGoUpwards3() throws Exception {
        Encapsulator root = Encapsulator.buildTreeFromPsi(createTestIfStatement("x > 2", "anyBlock(0);"));
        Encapsulator expression = root.accept(new EncapsulatingNodeValueVisitor<Optional<Encapsulator>>() {
            @Override
            public Optional<Encapsulator> visit(Encapsulator n) {
                return Optional.ofNullable(iz.expression(n.getInner()) && n.getInner().getText().equals("anyBlock(0)") ? n : null);
            }
        }, new BinaryOperator<Optional<Encapsulator>>() {
            @Override
            public Optional<Encapsulator> apply(Optional<Encapsulator> t, Optional<Encapsulator> t2) {
                return t == null || !t.isPresent() ? (t2 == null ? Optional.empty() : t2) : t;
            }
        }).orElse(null);
        assertTrue(StubName.ANY_BLOCK.goUpwards(expression, expression.getParent()));
    }
}