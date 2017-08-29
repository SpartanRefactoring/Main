package il.org.spartan.spartanizer.cmdline.visitor;

import java.io.*;
import java.util.function.*;

import fluent.ly.*;
import il.org.spartan.bench.*;
import il.org.spartan.external.*;
import op.*;
import op.traverse.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-08-26 */
public class FeaturedTraversor<Self extends FeaturedTraversor<?>> extends op.Arguments.Execution implements Selfie<Self> {

  public static void main(String[] arguments) {
    new FeaturedTraversor<>().withArguments(arguments).execute();
  }

  @External(alias = "s") protected boolean dots = true; // @formatter:on
  @External(alias = "l") protected boolean log = true; // @formatter:on

  public FeaturedTraversor() {
    if (dots)
    withDotter();
    if (log)
    withStderrTapper();
    withFileAction(λ -> System.out.println(λ));
  }
  public final Self withDotter() {
    withTapper(new Traverse.Tapper() {
      final Dotter d = new Dotter();

      /** @formatter:off */
            // vim: +;/:on/-!sort|column -t|awk '{print "        " $0}'
            @Override  public  void  beginBatch()     {    d.click();  }
            @Override  public  void  beginCorpus()  {    d.click();  }
            @Override  public  void  beginFile()      {    d.click();  }
            @Override  public  void  beginProject()  {    d.click();  }
            @Override  public  void  endBatch()       {    d.end();    }
            @Override public void endCorpus() {d.click();}
            @Override  public  void  endFile()        {    d.click();  }
            @Override public void endProject() {d.click();}
            /** @formatter:on */
    });
    return self();
  }
  public Action withFileAction(Consumer<? super File> ¢) {
    withTapper(new Tapper.Stub() {
      @Override public void beginFile() {
        ¢.accept(file());
      }
    });
    return this;
  }
  public final Self withStderrTapper() {
    withTapper(new Traverse.Tapper() {
      @Override public void beginBatch() {
        System.err.println(" --- Begin Batch Process --- ");
      }
      @Override public void beginCorpus() {
        System.err.println("Begin " + corpusName());
      }
      @Override public void beginFile() {
        System.err.println("Begin " + fileName());
      }
      @Override public void beginProject() {
        System.err.println("Begin " + projectName());
      }
      @Override public void endBatch() {
        System.err.println(" --- End Batch Process --- ");
      }
      @Override public void endCorpus() {
        System.err.println("End " + corpusName());
      }
      @Override public void endFile() {
        System.err.println("End " + fileName());
      }
      @Override public void endProject() {
        System.err.println("End " + projectName());
      }
    });
    return self();
  }
}