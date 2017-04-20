package il.org.spartan.proposition;

import java.util.function.*;

/** Demonstrates lambda
 * @author Yossi Gil
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