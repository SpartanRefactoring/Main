package il.org.spartan.Leonidas.plugin.leonidas;

import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.auxilary_layer.iz;

import java.util.Optional;
import java.util.function.BinaryOperator;

/**
 * @author michalcohen
 * @since 30-04-2017.
 */
public class PruningTest extends PsiTypeHelper {
    public void testPruneExpression() throws Exception {
        Encapsulator root = Encapsulator.buildTreeFromPsi(createTestIfStatement("x > 2", "booleanExpression(0);"));
        Encapsulator pruned = Pruning.prune(root);
        assertEquals(pruned.getChildren().size(), 7);
        assertTrue(iz.generic(pruned.getChildren().get(6).getChildren().get(0).getChildren().get(1).getChildren().get(0)));
        assertFalse(iz.generic(pruned.getChildren().get(3)));
    }

    public void testPruneStatement() throws Exception {
        Encapsulator root = Encapsulator.buildTreeFromPsi(createTestIfStatement("x > 2", "statement(0);"));
        Encapsulator pruned = Pruning.prune(root);
        assertEquals(pruned.getChildren().size(), 7);
        assertTrue(iz.generic(pruned.getChildren().get(6).getChildren().get(0).getChildren().get(1)));
        assertFalse(iz.generic(pruned.getChildren().get(3)));
    }
//TODO move to tests of GenericEncapsulators
//    public void testGetRealParentExpression() throws Exception {
//        Encapsulator root = Encapsulator.buildTreeFromPsi(createTestIfStatement("x > 2", "booleanExpression(0);"));
//        Encapsulator expression = root.accept(new EncapsulatingNodeValueVisitor<Optional<Encapsulator>>() {
//            @Override
//            public Optional<Encapsulator> visit(Encapsulator n) {
//                return Optional.ofNullable(iz.expression(n.getInner()) && n.getInner().getText().equals("booleanExpression(0)") ? n : null);
//            }
//        }, new BinaryOperator<Optional<Encapsulator>>() {
//            @Override
//            public Optional<Encapsulator> apply(Optional<Encapsulator> t, Optional<Encapsulator> t2) {
//                return t == null || !t.isPresent() ? (t2 == null ? Optional.empty() : t2) : t;
//            }
//        }).orElse(null);
//        assertEquals(Pruning.getRealParent(expression, GenericPsiElementStub.StubName.BOOLEAN_EXPRESSION).getText(), "booleanExpression(0)");
//    }
//
//    public void testGetRealParentStatement() throws Exception {
//        Encapsulator root = Encapsulator.buildTreeFromPsi(createTestIfStatement("x > 2", "statement(0);"));
//        Encapsulator expression = root.accept(new EncapsulatingNodeValueVisitor<Optional<Encapsulator>>() {
//            @Override
//            public Optional<Encapsulator> visit(Encapsulator n) {
//                return Optional.ofNullable(iz.expression(n.getInner()) && n.getInner().getText().equals("statement(0)") ? n : null);
//            }
//        }, new BinaryOperator<Optional<Encapsulator>>() {
//            @Override
//            public Optional<Encapsulator> apply(Optional<Encapsulator> t, Optional<Encapsulator> t2) {
//                return t == null || !t.isPresent() ? (t2 == null ? Optional.empty() : t2) : t;
//            }
//        }).orElse(null);
//        assertEquals(Pruning.getRealParent(expression, GenericPsiElementStub.StubName.STATEMENT).getText(), "statement(0);");
//    }
//
//    public void testGetRealParentAnyBlock() throws Exception {
//        Encapsulator root = Encapsulator.buildTreeFromPsi(createTestIfStatement("x > 2", "anyBlock(0);"));
//        Encapsulator expression = root.accept(new EncapsulatingNodeValueVisitor<Optional<Encapsulator>>() {
//            @Override
//            public Optional<Encapsulator> visit(Encapsulator n) {
//                return Optional.ofNullable(iz.expression(n.getInner()) && n.getInner().getText().equals("anyBlock(0)") ? n : null);
//            }
//        }, new BinaryOperator<Optional<Encapsulator>>() {
//            @Override
//            public Optional<Encapsulator> apply(Optional<Encapsulator> t, Optional<Encapsulator> t2) {
//                return t == null || !t.isPresent() ? (t2 == null ? Optional.empty() : t2) : t;
//            }
//        }).orElse(null);
//        assertEquals(Pruning.getRealParent(expression, GenericPsiElementStub.StubName.ANY_BLOCK).getText(), "{anyBlock(0);}");
//    }
//
//    public void testGetRealParentIdentifier() throws Exception {
//        Encapsulator root = Encapsulator.buildTreeFromPsi(createTestIfStatement("x > 2", "System.out.println(identifier(0));"));
//        Encapsulator expression = root.accept(new EncapsulatingNodeValueVisitor<Optional<Encapsulator>>() {
//            @Override
//            public Optional<Encapsulator> visit(Encapsulator n) {
//                return Optional.ofNullable(iz.expression(n.getInner()) && n.getInner().getText().equals("identifier(0)") ? n : null);
//            }
//        }, new BinaryOperator<Optional<Encapsulator>>() {
//            @Override
//            public Optional<Encapsulator> apply(Optional<Encapsulator> t, Optional<Encapsulator> t2) {
//                return t == null || !t.isPresent() ? (t2 == null ? Optional.empty() : t2) : t;
//            }
//        }).orElse(null);
//        assertEquals(Pruning.getRealParent(expression, GenericPsiElementStub.StubName.IDENTIFIER).getText(), "identifier(0)");
//    }
//
//    public void testGetRealParentArrayIdentifier() throws Exception {
//        Encapsulator root = Encapsulator.buildTreeFromPsi(createTestIfStatement("x > 2", "System.out.println(arrayIdentifier(0)[3]);"));
//        Encapsulator expression = root.accept(new EncapsulatingNodeValueVisitor<Optional<Encapsulator>>() {
//            @Override
//            public Optional<Encapsulator> visit(Encapsulator n) {
//                return Optional.ofNullable(iz.expression(n.getInner()) && n.getInner().getText().equals("arrayIdentifier(0)") ? n : null);
//            }
//        }, new BinaryOperator<Optional<Encapsulator>>() {
//            @Override
//            public Optional<Encapsulator> apply(Optional<Encapsulator> t, Optional<Encapsulator> t2) {
//                return t == null || !t.isPresent() ? (t2 == null ? Optional.empty() : t2) : t;
//            }
//        }).orElse(null);
//        assertEquals(Pruning.getRealParent(expression, GenericPsiElementStub.StubName.ARRAY_IDENTIFIER).getText(), "arrayIdentifier(0)");
//    }
}