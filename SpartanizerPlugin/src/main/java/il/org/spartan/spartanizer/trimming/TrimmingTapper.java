package il.org.spartan.spartanizer.trimming;

public interface TrimmingTapper {
  /** @formatter:off */
  default void noTipper() {/**/}
  default void setNode()       {/**/}
  default void tipperAccepts() {/**/}
  default void tipperRejects() {/**/}
  default void tipperTip()     {/**/}
  default void tipPrune()      {/**/}
  default void tipRewrite()    {/**/}
  //@formatter:on
}