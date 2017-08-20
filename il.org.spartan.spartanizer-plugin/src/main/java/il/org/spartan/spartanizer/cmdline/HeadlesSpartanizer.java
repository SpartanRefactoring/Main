package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import java.io.*;
import java.nio.file.*;
import org.eclipse.jdt.core.dom.*;
import fluent.ly.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.SpartanizationComparator.*;
import il.org.spartan.spartanizer.cmdline.tables.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.traversal.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** This class you can spartanize a directory easily. Or you can extends this
 * class and configure it to fit to your own needs.
 * @author oran1248
 * @author Matteo Orru'
 * @since 2017-04-11 */
public class HeadlesSpartanizer extends GrandVisitor {
  
  @External(alias = "cp", value = "copy file") @SuppressWarnings("CanBeFinal") boolean copy;
  @External(alias = "u", value = "unique") @SuppressWarnings("CanBeFinal") boolean unique;

  static Table summaryTable;
  static Table tippersTable;
  static HeadlesSpartanizer hs;
  JavaProductionFilesVisitor v;
  TextualTraversals traversals = new TextualTraversals();
  CurrentData data = new CurrentData();
  static PrintWriter beforeWriter, afterWriter;
  
  public static void main(final String[] args){
    hs = new HeadlesSpartanizer(args);
    hs.goAll();
  }

  private void goAll() {
    System.err.println("eccolo!");
    CurrentData.locations.stream().forEach(λ -> {
      CurrentData.location = λ; 
      go(λ);
    });
  }
  
  public HeadlesSpartanizer(final String[] args){
    super(args);
  }
  public HeadlesSpartanizer() {
  }
  protected void setUp() {
    /**/
  }
  protected static void tearDown() {
    /**/
  }
  @SuppressWarnings("static-method") 
  protected boolean spartanize(@SuppressWarnings("unused") final File __) {
    return true;
  }
  @SuppressWarnings("static-method") protected ASTVisitor astVisitor() {
    return new ASTVisitor() {/**/};
  }
  protected String perform(final String fileContent) {
    return fixedPoint(fileContent);
  }
  
  
  static void reset() {
    //
  }
  
  public final void go(final String dirPath) {
    //setUp();
    (new GrandVisitor(new String[] {dirPath}) {
      
      {
        listen(new Tapper() {
          @Override public void beginBatch(){
            //System.err.println(" --- Begin Batch Process --- ");
            tippersTable = new Table("tippers" //Table.classToNormalizedFileName(Table.class) 
                  + "-" + corpus, outputFolder);
            if(Traversal.table == null)
              Traversal.table = new Table("tippers2" //Table.classToNormalizedFileName(Table.class) 
                  + "-" + corpus, outputFolder);
          }
          @Override public void beginFile() {
            //System.err.println("Begin " + CurrentData.fileName);
            traversals.traversal.notify.begin();
            tippersTable.col("Project",CurrentData.location);
            tippersTable.col("File",CurrentData.fileName);
            tippersTable.nl();
          }
          @Override public void beginLocation() {
            // System.err.println("Begin " + CurrentData.location);
          }
          @Override public void endBatch() {
            System.err.println(" --- End Batch Process --- ");
            Traversal.table.close();
            done();
          }
          @Override public void endFile() {
            //System.err.println("End " + CurrentData.fileName);
            traversals.traversal.notify.end();
          }
          @Override public void endLocation() {
            //System.err.println("End " + CurrentData.location);
          }
        });
      }
      protected void done() {
        summarize(CurrentData.location,CurrentData.before,CurrentData.after);
      }     
      void summarize(String project, String before, String after) {
        summarize(project,asCu(before),asCu(after)); 
      }
      public void summarize(final String project, final ASTNode before, final ASTNode after) {
        initializeWriter();
        summaryTable//
            .col("Project", project)//
            .col("File", CurrentData.fileName)//
            .col("Path", CurrentData.relativePath);
        reportCUMetrics(before, "before");
        reportCUMetrics(after, "after");
        summaryTable.nl();
      }
      @SuppressWarnings({ "rawtypes", "unchecked" })
      void reportCUMetrics(final ASTNode ¢, final String id) {
        for (final NamedFunction f : functions())
          summaryTable.col(f.name() + "-" + id, f.function().run(¢));
      }
      void initializeWriter() {
        if (summaryTable == null)
          summaryTable = new Table(Table.classToNormalizedFileName(Table_Summary.class) + "-" 
                        + corpus, outputFolder);
      }
      @Override public void visitFile(final File f) {
        CurrentData.fileName = f.getName();
        traversals.traversal.fileName = f.getName();
        traversals.traversal.project = CurrentData.location;
        notify.beginFile();
        try {
          CurrentData.relativePath = Paths.get(f.getCanonicalPath()).subpath(Paths.get(inputFolder).getNameCount(), Paths.get(f.getCanonicalPath()).getNameCount()) + "";
          CurrentData.absolutePath = Paths.get(f.getCanonicalPath()).subpath(Paths.get(inputFolder).getNameCount(), Paths.get(f.getCanonicalPath()).getNameCount()) + "";
        } catch (IOException ¢) {
          ¢.printStackTrace();
        }
        if (!spartanize(f))
          return;
        try {
          CurrentData.before = FileUtils.read(f);
          CurrentData.after = perform(CurrentData.before);
          analyze(CurrentData.before, CurrentData.after);
        } catch (final IOException ¢) {
          note.io(¢);
        }
        notify.endFile();
      }
      protected void analyze(final String before, final String after) {
        try {
          if (!copy || unique) {
            if (copy && unique)
              writeBeforeAfter(before, after);
          } else {
            Path pathname = Paths.get(outputFolder + File.separator + Paths.get(CurrentData.relativePath).getParent());
            if (!Files.exists(pathname))
              new File(pathname + "").mkdirs();
            FileUtils.writeToFile(outputFolder + File.separator + CurrentData.relativePath, after);
          }
         } catch (final FileNotFoundException ¢) {
          note.io(¢);
          ¢.printStackTrace();
        }
      }
      void writeBeforeAfter(final String before, final String after) throws FileNotFoundException {
        if (beforeWriter == null) 
          beforeWriter = new PrintWriter(outputFolder + File.separator + CurrentData.location + "-before.java");
        writeFile(before, "before", beforeWriter);
        if (afterWriter == null) 
          afterWriter = new PrintWriter(outputFolder + File.separator + CurrentData.location + "-after.java");
        writeFile(after, "after", afterWriter);
      }
      @SuppressWarnings("unused") void writeFile(final String before, final String name, PrintWriter w) throws FileNotFoundException {
        Path path = Paths.get(outputFolder);
        if (Files.notExists(path)) 
          new File(path + File.separator + name + ".java").mkdirs();
        //initializeBeforeWriter(name, writer, path);
        w.append(before);
        w.flush();
      }
      
      @SuppressWarnings("unused") void initializeWriter(final String name, PrintWriter w, Path p) throws FileNotFoundException {
        if (beforeWriter == null) 
          beforeWriter = new PrintWriter(p +  File.separator + name + ".java");
      }
     
      CompilationUnit asCu(final String before) {
        return (CompilationUnit) makeAST.COMPILATION_UNIT.from(before);
      }
    }).visitAll(astVisitor());
    //tearDown();
  }
  

  static NamedFunction<ASTNode> m(final String name, final ToInt<ASTNode> f) {
    return new NamedFunction<>(name, f);
  }
  
  public static NamedFunction<?>[] functions() {
    return as.array(//
        m("length - ", metrics::length), //
        m("essence - ", λ -> Essence.of(λ + "").length()), //
        m("tokens - ", λ -> metrics.tokens(λ + "")), //
        m("nodes - ", countOf::nodes), //
        m("body - ", metrics::bodySize), //
        m("methodDeclaration - ", λ -> !iz.methodDeclaration(λ) ? -1 : extract.statements(az.methodDeclaration(λ).getBody()).size()),
        m("tide - ", λ -> clean(λ + "").length()));//
  }
  
  public final String fixedPoint(final String from) {
    return traversals.fixed(from);
  }
}
