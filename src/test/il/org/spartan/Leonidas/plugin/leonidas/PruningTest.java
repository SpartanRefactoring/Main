package il.org.spartan.Leonidas.plugin.leonidas;

import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.EncapsulatingNode;
import il.org.spartan.Leonidas.plugin.EncapsulatingNodeValueVisitor;

import java.util.Optional;
import java.util.function.BinaryOperator;

/**
 * @author michalcohen
 * @since 30-04-2017.
 */
public class PruningTest extends PsiTypeHelper {
    public void testPruneExpression() throws Exception {
        EncapsulatingNode root = EncapsulatingNode.buildTreeFromPsi(createTestIfStatement("x > 2", "booleanExpression(0);"));
        EncapsulatingNode pruned = Pruning.prune(root);
        assertEquals(pruned.getChildren().size(), 7);
        assertTrue(iz.generic(pruned.getChildren().get(6).getChildren().get(0).getChildren().get(1).getChildren().get(0).getInner()));
        assertFalse(iz.generic(pruned.getChildren().get(3).getInner()));
    }

    public void testPruneStatement() throws Exception {
        EncapsulatingNode root = EncapsulatingNode.buildTreeFromPsi(createTestIfStatement("x > 2", "statement(0);"));
        EncapsulatingNode pruned = Pruning.prune(root);
        assertEquals(pruned.getChildren().size(), 7);
        assertTrue(iz.generic(pruned.getChildren().get(6).getChildren().get(0).getChildren().get(1).getInner()));
        assertFalse(iz.generic(pruned.getChildren().get(3).getInner()));
    }

    public void testGetRealParentExpression() throws Exception {
        EncapsulatingNode root = EncapsulatingNode.buildTreeFromPsi(createTestIfStatement("x > 2", "booleanExpression(0);"));
        EncapsulatingNode expression = root.accept(new EncapsulatingNodeValueVisitor<Optional<EncapsulatingNode>>() {
            @Override
            public Optional<EncapsulatingNode> visit(EncapsulatingNode n) {
                return Optional.ofNullable(iz.expression(n.getInner()) && n.getInner().getText().equals("booleanExpression(0)") ? n : null);
            }
        }, new BinaryOperator<Optional<EncapsulatingNode>>() {
            @Override
            public Optional<EncapsulatingNode> apply(Optional<EncapsulatingNode> t, Optional<EncapsulatingNode> t2) {
                return t == null || !t.isPresent() ? (t2 == null ? Optional.empty() : t2) : t;
            }
        }).orElse(null);
        assertEquals(Pruning.getRealParent(expression, GenericPsiElementStub.StubName.BOOLEAN_EXPRESSION).getText(), "booleanExpression(0)");
    }

    public void testGetRealParentStatement() throws Exception {
        EncapsulatingNode root = EncapsulatingNode.buildTreeFromPsi(createTestIfStatement("x > 2", "statement(0);"));
        EncapsulatingNode expression = root.accept(new EncapsulatingNodeValueVisitor<Optional<EncapsulatingNode>>() {
            @Override
            public Optional<EncapsulatingNode> visit(EncapsulatingNode n) {
                return Optional.ofNullable(iz.expression(n.getInner()) && n.getInner().getText().equals("statement(0)") ? n : null);
            }
        }, new BinaryOperator<Optional<EncapsulatingNode>>() {
            @Override
            public Optional<EncapsulatingNode> apply(Optional<EncapsulatingNode> t, Optional<EncapsulatingNode> t2) {
                return t == null || !t.isPresent() ? (t2 == null ? Optional.empty() : t2) : t;
            }
        }).orElse(null);
        assertEquals(Pruning.getRealParent(expression, GenericPsiElementStub.StubName.STATEMENT).getText(), "statement(0);");
    }

    public void testGetRealParentAnyBlock() throws Exception {
        EncapsulatingNode root = EncapsulatingNode.buildTreeFromPsi(createTestIfStatement("x > 2", "anyBlock(0);"));
        EncapsulatingNode expression = root.accept(new EncapsulatingNodeValueVisitor<Optional<EncapsulatingNode>>() {
            @Override
            public Optional<EncapsulatingNode> visit(EncapsulatingNode n) {
                return Optional.ofNullable(iz.expression(n.getInner()) && n.getInner().getText().equals("anyBlock(0)") ? n : null);
            }
        }, new BinaryOperator<Optional<EncapsulatingNode>>() {
            @Override
            public Optional<EncapsulatingNode> apply(Optional<EncapsulatingNode> t, Optional<EncapsulatingNode> t2) {
                return t == null || !t.isPresent() ? (t2 == null ? Optional.empty() : t2) : t;
            }
        }).orElse(null);
        assertEquals(Pruning.getRealParent(expression, GenericPsiElementStub.StubName.ANY_BLOCK).getText(), "{anyBlock(0);}");
    }

    public void testGetRealParentIdentifier() throws Exception {
        EncapsulatingNode root = EncapsulatingNode.buildTreeFromPsi(createTestIfStatement("x > 2", "System.out.println(identifier(0));"));
        EncapsulatingNode expression = root.accept(new EncapsulatingNodeValueVisitor<Optional<EncapsulatingNode>>() {
            @Override
            public Optional<EncapsulatingNode> visit(EncapsulatingNode n) {
                return Optional.ofNullable(iz.expression(n.getInner()) && n.getInner().getText().equals("identifier(0)") ? n : null);
            }
        }, new BinaryOperator<Optional<EncapsulatingNode>>() {
            @Override
            public Optional<EncapsulatingNode> apply(Optional<EncapsulatingNode> t, Optional<EncapsulatingNode> t2) {
                return t == null || !t.isPresent() ? (t2 == null ? Optional.empty() : t2) : t;
            }
        }).orElse(null);
        assertEquals(Pruning.getRealParent(expression, GenericPsiElementStub.StubName.IDENTIFIER).getText(), "identifier(0)");
    }

    public void testGetRealParentArrayIdentifier() throws Exception {
        EncapsulatingNode root = EncapsulatingNode.buildTreeFromPsi(createTestIfStatement("x > 2", "System.out.println(arrayIdentifier(0)[3]);"));
        EncapsulatingNode expression = root.accept(new EncapsulatingNodeValueVisitor<Optional<EncapsulatingNode>>() {
            @Override
            public Optional<EncapsulatingNode> visit(EncapsulatingNode n) {
                return Optional.ofNullable(iz.expression(n.getInner()) && n.getInner().getText().equals("arrayIdentifier(0)") ? n : null);
            }
        }, new BinaryOperator<Optional<EncapsulatingNode>>() {
            @Override
            public Optional<EncapsulatingNode> apply(Optional<EncapsulatingNode> t, Optional<EncapsulatingNode> t2) {
                return t == null || !t.isPresent() ? (t2 == null ? Optional.empty() : t2) : t;
            }
        }).orElse(null);
        assertEquals(Pruning.getRealParent(expression, GenericPsiElementStub.StubName.ARRAY_IDENTIFIER).getText(), "arrayIdentifier(0)");
    }
}