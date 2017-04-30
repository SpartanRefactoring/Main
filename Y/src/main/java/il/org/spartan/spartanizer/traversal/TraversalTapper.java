package il.org.spartan.spartanizer.traversal;

public interface TraversalTapper {
  /** @formatter:off */
  default void begin() {/**/}
  default void end() {/**/}
  default void noTipper()      {/**/}
  default void setNode()       {/**/}
  default void tipperAccepts() {/**/}
  default void tipperRejects() {/**/}
  default void tipperTip()     {/**/}
  default void tipPrune()      {/**/}
  default void tipRewrite()    {/**/}
  //@formatter:on
}