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
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** __ this class you can spartanize a directory easily. Or you can extends this
 * class and configure it to fit to your own needs.
 * @author oran1248
 * @author Matteo Orru'
 * @since 2017-04-11 */
public class HeadlesSpartanizer extends GrandVisitor {
  
  @External(alias = "cp", value = "copy file") @SuppressWarnings("CanBeFinal") boolean copy;
  static Table table;
  static Table tippersTable;
  static HeadlesSpartanizer hs;
  JavaProductionFilesVisitor v;
  TextualTraversals traversals = new TextualTraversals();
  
  public static void main(final String[] args){
    hs = new HeadlesSpartanizer(args);
    System.err.println(hs.current.locations.size());
     hs.goAll();
  }

  private void goAll() {
    hs.current.locations.stream().forEach(λ -> {
      hs.current.location = λ; 
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
    table.close();
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
    setUp();
    (new GrandVisitor(new String[] {dirPath}) {
      {
        listen(new Tapper() {
          @Override public void beginBatch(){
            System.err.println(" --- Begin Batch Process --- ");
            tippersTable = new Table("tippers" //Table.classToNormalizedFileName(Table.class) 
                  + "-" + corpus, outputFolder);
            traversals.traversal.table = new Table("tippers2" //Table.classToNormalizedFileName(Table.class) 
                + "-" + corpus, outputFolder);
            
          }
          @Override public void beginFile() {
            System.err.println("Begin " + current.fileName);
            tippersTable.col("Project",current.location);
            tippersTable.col("File",current.fileName);
            tippersTable.nl();
          }
          @Override public void beginLocation() {
            System.err.println("Begin " + current.location);
          }
          @Override public void endBatch() {
            System.err.println(" --- End Batch Process --- ");
            traversals.traversal.table.close();
            done();
          }
          @Override public void endFile() {
            System.err.println("End " + current.fileName);
          }
          @Override public void endLocation() {
            System.err.println("End " + current.location);
          }
        });
      }
      protected void done() {
        summarize(current.location,current.before,current.after);
        tippersTable.close();
      }     
      void summarize(String project, String before, String after) {
        summarize(project,asCu(before),asCu(after)); 
      }
      public void summarize(final String project, final ASTNode before, final ASTNode after) {
        initializeWriter();
        table//
            .col("Project", project)//
            .col("File", current.fileName)//
            .col("Path", current.relativePath);
        reportCUMetrics(before, "before");
        reportCUMetrics(after, "after");
        table.nl();
      }
      @SuppressWarnings({ "rawtypes", "unchecked" })
      void reportCUMetrics(final ASTNode ¢, final String id) {
        for (final NamedFunction f : functions())
          table.col(f.name() + "-" + id, f.function().run(¢));
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(Table.classToNormalizedFileName(Table_Summary.class) + "-" 
                        + corpus, outputFolder);
      }
      @Override public void visitFile(final File f) {
        current.fileName = f.getName();
        traversals.traversal.notify.begin();
        notify.beginFile();
        try {
          current.relativePath = Paths.get(f.getCanonicalPath()).subpath(Paths.get(inputFolder).getNameCount(), Paths.get(f.getCanonicalPath()).getNameCount()) + "";
        } catch (IOException ¢) {
          ¢.printStackTrace();
        }
        if (!spartanize(f))
          return;
        try {
          current.before = FileUtils.read(f);
          current.after = perform(current.before);
          analyze(current.before, current.after);
        } catch (final IOException ¢) {
          note.io(¢);
        }
        notify.endFile();
      }
      protected void analyze(@SuppressWarnings("unused") final String before, final String after) {
        try {
          if(copy){
            Path pathname = Paths.get(outputFolder + File.separator + Paths.get(current.relativePath).getParent());
            if (!Files.exists(pathname)) new File(pathname + "").mkdirs();
            FileUtils.writeToFile(outputFolder + File.separator + current.relativePath , after);
          }
         } catch (final FileNotFoundException ¢) {
          note.io(¢);
        }
      }
      CompilationUnit asCu(final String before) {
        return (CompilationUnit) makeAST.COMPILATION_UNIT.from(before);
      }
    }).visitAll(astVisitor());
    tearDown();
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
