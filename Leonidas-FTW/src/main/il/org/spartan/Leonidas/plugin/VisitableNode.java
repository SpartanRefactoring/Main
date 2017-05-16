package il.org.spartan.Leonidas.plugin;

import java.util.function.BinaryOperator;

/**
 * Implements the Visitor design pattern.
 *
 * @author michalcohen
 * @since 29-04-2017.
 */
public interface VisitableNode {

    /**
     * Enable v visit this node.
     *
     * @param v - Visitor.
     */
    void accept(EncapsulatingNodeVisitor v);

    /**
     * @param v           visitor that returns vakue
     * @param accumulator the way two results are propagated, must be associative and commutative.
     * @param <T>         - the result type
     * @return the result driven from applying v on every node.
     */
    <T> T accept(EncapsulatingNodeValueVisitor v, BinaryOperator<T> accumulator);
}
