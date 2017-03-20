package il.org.spartan.utils;

import java.util.function.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-19 */
final class B00LJavaNotation extends B00LReducingGear<String> {
  B00LJavaNotation() {
    super(new ReduceStringConcatenate());
  }

  @Override protected String ante(B00L.Not ¢) {
    return "!" + (¢.inner instanceof B00L.C ? "(" : "");
  }

  @Override protected String ante(B00L.P ¢) {
    return ¢.inner instanceof B00L.C ? "(" : "";
  }

  @Override protected String inter(@SuppressWarnings("unused") B00L.And __) {
    return " && ";
  }
  @Override protected String inter(@SuppressWarnings("unused") B00L.Or __) {
    return " || ";
  }

  @Override protected String map(final BooleanSupplier ¢) {
    return ¢ + "";
  }

  @Override protected String post(B00L.Not ¢) {
    return ¢.inner instanceof B00L.C ? ")" : "";
  }

  @Override protected String post(B00L.P ¢) {
    return ¢.inner instanceof B00L.C ? ")" : "";
  }
}