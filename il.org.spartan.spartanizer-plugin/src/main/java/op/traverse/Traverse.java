package op.traverse;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

import fluent.ly.*;
import il.org.spartan.collections.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.cmdline.library.*;
import il.org.spartan.utils.*;

/** This interface groups together the main classes and protocol for a traversal
 * system.
 * @author Yossi Gil
 * @since 2017-08-24 */
public interface Traverse {
  /** Implements a multi-layer traversal of Java files. A variety of query
   * functions and variables reflect the state. Inheritors and clients can use
   * information provided in {@link Traverse} to determine the current state of
   * the traversal.
   * <p>
   * Do not sort members
   * @see #go()
   * @author Yossi Gil
   * @since 2017-07-04 */
  interface Arguments<Self extends Arguments<Self>> extends Selfie<Self>, Traverse {
    Self withArguments(String[] arguments);
    Self withFilter(BooleanSupplier filter);
    Self withHook(Events h);
  }

  interface Checkpoints {
    interface Full extends Variables.Computed, Arguments.Computed {
      default Path filePath() {
        return projectAbsolutePath().relativize(filePathAbsolute());
      }
      default String corpusName() {
        return corpora().get(corpusIndex());
      }
      default Path corpusPath() {
        return inputAbsolutePath().relativize(corpusAbsolutePath());
      }
      default Path corpusAbsolutePath() {
        return inputAbsolutePath().resolve(corpusName());
      }
      default Path projectAbsolutePath() {
        return corpusAbsolutePath().resolve(projectName());
      }
      default Path filePathRelative() {
        return filePathAbsolute().relativize(inputAbsolutePath());
      }
    }

    interface Arguments {
      List<String> corpora();
      Path inputPath();
      Path outputPath();

      interface Computed extends Arguments {
        default Path inputAbsolutePath() {
          return inputPath().toAbsolutePath();
        }
      }
    }

    interface Variables {
      /** The projectIndex of the current project among all other
       * {@link #projects()} */
      int corpusIndex();
      /** The current file we work on, maybe null */
      File file();
      /** The file content, maybe null */
      String fileContents();
      /** The index of the current corpus among all other {@link #projects()} */
      int projectIndex();
      /** The list of all projects to be examined */
      List<String> projects();

      interface Computed extends Variables {
        /* Compound properties  @formatter:off */
            // vim: +;/ter:on/-!sort|column -t|awk '{print "\t\t\t\t\t" $0}'|expand -t 2
            default  String  fileName()             {  return  file().getName();                                      }
            default  Path    filePathAbsolute()     {  return  file().toPath().toAbsolutePath();                      }

            default  String  projectName()          {  return  projects().get(projectIndex());                        }
              // @formatter:on
      }
    }

    abstract class Mutables<Self extends Mutables<Self>> implements Variables, Arguments {
      /** Fields  @formatter:off */
        // vim: +;/ter:on/-!sort|column -t|awk '{print"\t\t\t"$0}'|expand -t2
        File          file;
        int           corpusIndex;
        int           projectIndex;
        List<String>  projects       =  new  ArrayList<>();
        String        fileContents;
        //@formatter:on

      /** Accessors @formatter:off */
        // vim: +;/ter:on/-!sort|column -t|awk '{print"\t\t\t"$0}'|expand -t2
        @Override  public  final  File          file()          {  return  file;                                 }
        @Override  public  final  int           corpusIndex()   {  return  corpusIndex;                          }
        @Override  public  final  int           projectIndex()  {  return  projectIndex;                         }
        @Override  public  final  List<String>  projects()      {  return  projects.subList(0,projects.size());  }
        @Override  public  final  String        fileContents()  {  return  fileContents;                         }
        //@formatter:on

      abstract class Lock extends Arguments implements Checkpoints.Full {
        /** Lock delegation @formatter:off */
          // vim: +;/ter:on/-!sort|column -t|awk '{print"\t\t\t\t"$0}'|expand -t2
          @Override  public  final  Path    corpusAbsolutePath()   {  return  Checkpoints.Full.super.corpusAbsolutePath();   }
          @Override  public  final  Path    corpusPath()           {  return  Checkpoints.Full.super.corpusPath();           }
          @Override  public  final  Path    filePathAbsolute()     {  return  Checkpoints.Full.super.filePathAbsolute();     }
          @Override  public  final  Path    filePathRelative()     {  return  Checkpoints.Full.super.filePathRelative();     }
          @Override  public  final  Path    filePath()             {  return  Checkpoints.Full.super.filePath();             }
          @Override  public  final  Path    projectAbsolutePath()  {  return  Checkpoints.Full.super.projectAbsolutePath();  }
          @Override  public  final  String  corpusName()           {  return  Checkpoints.Full.super.corpusName();           }
          @Override  public  final  String  fileName()             {  return  Checkpoints.Full.super.fileName();             }
          @Override  public  final  String  projectName()          {  return  Checkpoints.Full.super.projectName();          }
          //@formatter:on
      }

      abstract class Delegator extends Lock {
        public abstract Checkpoints.Full inner();
        /** Accessors @formatter:off */
          // vim: +;/ter:on/-!sort|column -t|awk '{print "\t\t\t\t"$0}'|expand -t2
          @Override  public  File          file()          {  return  inner().file();          }
          @Override  public  int           corpusIndex()   {  return  inner().corpusIndex();   }
          @Override  public  int           projectIndex()  {  return  inner().projectIndex();  }
          @Override  public  List<String>  projects()      {  return  inner().projects();      }
          //@formatter:on 
      }

      class Arguments implements Checkpoints.Arguments {
        /** Fields @formatter:off*/
          // vim: +;/ter:on/-!sort|column -t|awk '{print "\t\t\t\t"$0}'|expand -t2
          @External(alias="c")  protected  List<String>  corpora       =  new                    ArrayList<>();
          @External(alias="i")  protected  String        inputFolder   =  system.cwd()           +               "";
          @External(alias="o")  protected  String        outputFolder  =  Paths.get(system.tmp)  +               "";
           //@formatter:on

        /** Accessors @formatter:off*/
          // vim: +;/ter:on/-!sort|column -t|awk '{print "\t\t\t\t"$0}'|expand -t2
          //         @formatter:off
          @Override  public          final  List<String>  corpora()     {  return  corpora.subList(0,corpora.size());  }
          @Override  public          final  Path          inputPath()   {  return  Paths.get(inputFolder);             }
          @Override  public          final  Path          outputPath()  {  return  Paths.get(outputFolder);            }
          // @formatter:on
      }
    }
  }
}