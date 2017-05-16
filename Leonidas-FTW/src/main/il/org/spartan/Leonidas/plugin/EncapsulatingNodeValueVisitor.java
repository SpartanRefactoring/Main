package il.org.spartan.Leonidas.plugin;

/**
 * Implements the visitor design pattern on an EncapsulatingNode element.
 * Will be used if return value is needed.
 * @author michalcohen
 * @since 22-02-2017.
 */
public interface EncapsulatingNodeValueVisitor<T> {
    T visit(EncapsulatingNode n);
}
