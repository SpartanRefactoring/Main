package il.org.spartan.utils;

import java.util.function.*;

/** Demonstrates lambda
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-23 */
public interface DemonsrateLambdaChains {
  Consumer<String> c = λ -> {/**/};
  Consumer<Consumer<String>> cc = λ -> {/**/};
  Consumer<Consumer<Consumer<String>>> ccc = λ -> {/**/};
  Supplier<String> s = () -> "s";
  Supplier<Supplier<String>> ss = () -> () -> "ss";
  Supplier<Supplier<Supplier<String>>> sss = () -> () -> () -> "AS";
  Supplier<Consumer<String>> sc = () -> λ -> {/**/};
  Consumer<Supplier<String>> cs = λ -> {/**/};
}