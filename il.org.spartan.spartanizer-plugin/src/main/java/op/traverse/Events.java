package op.traverse;

import op.example.*;
import op.example.Events.*;

/** Do not sort
 * @author Yossi Gil
 * @since 2017-08-24 */
interface Events {
  interface Set extends ParentEvents.Set {
    void beginCorpus();
    void beginFile();
    void beginProject();
    void endCorpus();
    void endFile();
    void endProject();
  }
  interface Listener extends Set {

    interface Idle extends Events.Set, ParentEvents.Listener.Idle {
      @Override default void beginCorpus() {/**/}
      @Override default void beginFile() {/**/}
      @Override default void beginProject() {/**/}
      @Override default void endCorpus() {/**/}
      @Override default void endFile() {/**/}
      @Override default void endProject() {/**/}
    }
 }

  interface Delegator<S extends Events.Set> extends ParentEvents.Delegator<S>  implements Traverse.Events.Set{
      //@formatter:off
        // vim: +;/ter:on/-!sort|column -t|awk '{print "\t\t\t\t"$0}'|expand -t2
          @Override  default  void  beginCorpus()   {  delegate(S::beginCorpus);   }
          @Override  default  void  beginFile()     {  delegate(S::beginFile);     }
          @Override  default  void  beginProject()  {  delegate(S::beginProject);  }
          @Override  default  void  endCorpus()     {  delegate(S::endCorpus);     }
          @Override  default  void  endFile()       {  delegate(S::endFile);       }
          @Override  default  void  endProject()    {  delegate(S::endProject);    }
        //@formatter:on
      void delegate(Consumer<? super S> action);

      class Many<S extends Traverse.Set> extends ParentEvents.Delegator.Many<S> implements Traverse.Events.Delegator<S> {
        /** Delegation @formatter:off */
          // vim: /ter:off/+;/ter:on/-!sort|column -t|awk '{print "\t\t\t\t" $0}'|expand -t2
        //@formatter:off
          @Override public void beginCorpus() { Traverse.Events.Delegator.super.beginCorpus(); } 
          @Override public void beginFile() { Traverse.Events.Delegator.super.beginFile(); } 
          @Override public void beginProject() { Traverse.Events.Delegator.super.beginProject(); } 
          @Override public void endCorpus() { Traverse.Events.Delegator.super.endCorpus(); } 
          @Override public void endFile() { Traverse.Events.Delegator.super.endFile(); } 
          @Override public void endProject() { Traverse.Events.Delegator.super.endProject(); } 
      }

      /** @formatter:on */
      // vim: /ter:off/+;/ter:on/-!sort|column -t|awk '{print "\t\t\t\t"
      // $0}'|expand -t2
      //@formatter:off
        //@formatter:on
      abstract class ToInner<S extends Events.Set> extends Implementation {
        @Override public void delegate(Consumer<? super Events> action) {
          action.accept(inner());
        }
        abstract S inner();
      }
    }

  class Many<S extends Traverse.Events.Set> extends ParentEvents.Delegator.Many<S> implements Events.Set {
    // vim: /ter:off/+;/ter:on/-!sort|column -t|awk '{print "\t\t\t\t"
    // $0}'|expand -t2
    //@formatter:off
        @Override  public  void  beginCorpus()   {  delegate(S::beginCorpus);   }
        @Override  public  void  beginFile()     {  delegate(S::beginFile);     }
        @Override  public  void  beginProject()  {  delegate(S::beginProject);  }
        @Override  public  void  endCorpus()     {  delegate(S::endCorpus);     }
        @Override  public  void  endFile()       {  delegate(S::endFile);       }
        @Override  public  void  endProject()    {  delegate(S::endProject);    }
       /** @formatter:on */
  }
}