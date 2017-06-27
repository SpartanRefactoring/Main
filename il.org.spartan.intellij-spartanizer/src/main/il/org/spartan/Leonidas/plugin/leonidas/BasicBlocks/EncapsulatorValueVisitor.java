package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

/**
 * Implements the visitor design pattern on an Encapsulator element.
 * Will be used if return value is needed.
 * @author michalcohen
 * @since 22-02-2017.
 */
public interface EncapsulatorValueVisitor<T> {
    T visit(Encapsulator e);
}
