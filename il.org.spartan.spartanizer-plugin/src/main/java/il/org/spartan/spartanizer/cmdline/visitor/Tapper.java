package il.org.spartan.spartanizer.cmdline.visitor;

public interface Tapper {
  //@formatter:off
  default void beginBatch(){/**/}
  default void beginFile() {/**/}
  default void beginLocation() {/**/}
  default void endBatch() {/**/}
  default void endFile() {/**/}
  default void endLocation() {/**/}
  //@formatter:on
}