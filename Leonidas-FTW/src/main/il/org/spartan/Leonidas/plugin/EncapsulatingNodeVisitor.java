package il.org.spartan.Leonidas.plugin;

/**
 * Implements the visitor design pattern on an EncapsulatingNode element.
 * Will be used if no return value is needed.
 * @author michalcohen
 * @since 22-02-2017
 */
public interface EncapsulatingNodeVisitor {

    void visit(EncapsulatingNode n);
}
