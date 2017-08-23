package il.org.spartan.spartanizer.cmdline.visitor;

import static il.org.spartan.tide.*;

import java.io.*;
import java.nio.file.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import fluent.ly.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.nodes.metrics.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
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
public class HeadlessSpartanizer {
  static PrintWriter beforeWriter, afterWriter;
  static Table summaryTable;
  static Table tippersTable;
  public static Metric.Integral[] functions() {
    return as.array(//
        Metric.named("length - ").of((ToIntFunction<ASTNode>) Metrics::length), //
        Metric.named("essence - ").of((ToIntFunction<ASTNode>) λ -> Essence.of(λ + "").length()), //
        Metric.named("tokens - ").of((ToIntFunction<ASTNode>) λ -> Metrics.tokens(λ + "")), //
        Metric.named("nodes - ").of((ToIntFunction<ASTNode>) countOf::nodes), //
        Metric.named("body - ").of((ToIntFunction<ASTNode>) Metrics::bodySize), //
        Metric.named("methodDeclaration - ")
            .of((ToIntFunction<ASTNode>) λ -> !iz.methodDeclaration(λ) ? -1 : extract.statements(az.methodDeclaration(λ).getBody()).size()),
        Metric.named("tide - ").of((ToIntFunction<ASTNode>) λ -> clean(λ + "").length()));//
  }
  public static void main(final String[] args) {
    new HeadlessSpartanizer(args).goAll();
  }
  static void reset() {
    //
  }
  @External(alias = "cp", value = "copy file") @SuppressWarnings("CanBeFinal") boolean copy;

  TextualTraversals traversals = new TextualTraversals();
  @External(alias = "u", value = "unique") @SuppressWarnings("CanBeFinal") boolean unique;
  MasterVisitor v;
  public final String fixedPoint(final String from) {
    return traversals.fixed(from);
  }
  public final void go(final String dirPath) {
    // setUp();
    (new MasterVisitor(new String[] { dirPath }) {
      {
        listen(new Tapper() {
          @Override public void beginBatch() {
            // System.err.println(" --- Begin Batch Process --- ");
            tippersTable = new Table("tippers" // Table.classToNormalizedFileName(Table.class)
                + "-" + corpus, outputFolder);
            if (Traversal.table == null)
              Traversal.table = new Table("tippers2" // Table.classToNormalizedFileName(Table.class)
                  + "-" + corpus, outputFolder);
          }
          @Override public void beginFile() {
            // System.err.println("Begin " + fileName);
            traversals.traversal.notify.begin();
            tippersTable.col("Project", location);
            tippersTable.col("File", fileName);
            tippersTable.nl();
          }
          @Override public void beginLocation() {
            // System.err.println("Begin " + location);
          }
          @Override public void endBatch() {
            System.err.println(" --- End Batch Process --- ");
            Traversal.table.close();
            done();
          }
          @Override public void endFile() {
            // System.err.println("End " + fileName);
            traversals.traversal.notify.end();
          }
          @Override public void endLocation() {
            // System.err.println("End " + location);
          }
        });
      }

      public void summarize(final String project, final ASTNode before, final ASTNode after) {
        initializeWriter();
        summaryTable//
            .col("Project", project)//
            .col("File", fileName)//
            .col("Path", relativePath);
        reportCUMetrics(before, "before");
        reportCUMetrics(after, "after");
        summaryTable.nl();
      }
      @Override public void visitFile(final File f) {
        fileName = f.getName();
        traversals.traversal.fileName = f.getName();
        traversals.traversal.project = location;
        notify.beginFile();
        try {
          relativePath = Paths.get(f.getCanonicalPath()).subpath(Paths.get(inputFolder).getNameCount(),
              Paths.get(f.getCanonicalPath()).getNameCount()) + "";
          absolutePath = Paths.get(f.getCanonicalPath()).subpath(Paths.get(inputFolder).getNameCount(),
              Paths.get(f.getCanonicalPath()).getNameCount()) + "";
        } catch (IOException ¢) {
          ¢.printStackTrace();
        }
        if (!spartanize(f))
          return;
        try {
          before = FileUtils.read(f);
          after = perform(before);
          analyze(before, after);
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
            Path pathname = Paths.get(outputFolder + File.separator + Paths.get(relativePath).getParent());
            if (!Files.exists(pathname))
              new File(pathname + "").mkdirs();
            FileUtils.writeToFile(outputFolder + File.separator + relativePath, after);
          }
        } catch (final FileNotFoundException ¢) {
          note.io(¢);
          ¢.printStackTrace();
        }
      }
      protected void done() {
        summarize(location, before, after);
      }
      CompilationUnit asCu(final String before) {
        return (CompilationUnit) makeAST.COMPILATION_UNIT.from(before);
      }
      void initializeWriter() {
        if (summaryTable == null)
          summaryTable = new Table(Table.classToNormalizedFileName(Table_Summary.class) + "-" + corpus, outputFolder);
      }
      @SuppressWarnings("unused") void initializeWriter(final String name, PrintWriter w, Path p) throws FileNotFoundException {
        if (beforeWriter == null)
          beforeWriter = new PrintWriter(p + File.separator + name + ".java");
      }
      @SuppressWarnings({}) void reportCUMetrics(final ASTNode ¢, final String id) {
        for (final Metric.Integral f : functions())
          summaryTable.col(f.name + "-" + id, f.apply(¢));
      }
      void summarize(String project, String before, String after) {
        summarize(project, asCu(before), asCu(after));
      }
      void writeBeforeAfter(final String before, final String after) throws FileNotFoundException {
        if (beforeWriter == null)
          beforeWriter = new PrintWriter(outputFolder + File.separator + location + "-before.java");
        writeFile(before, "before", beforeWriter);
        if (afterWriter == null)
          afterWriter = new PrintWriter(outputFolder + File.separator + location + "-after.java");
        writeFile(after, "after", afterWriter);
      }
      @SuppressWarnings("unused") void writeFile(final String before, final String name, PrintWriter w) throws FileNotFoundException {
        Path path = Paths.get(outputFolder);
        if (Files.notExists(path))
          new File(path + File.separator + name + ".java").mkdirs();
        // initializeBeforeWriter(name, writer, path);
        w.append(before);
        w.flush();
      }
    }).visitAll(astVisitor());
    // tearDown();
  }
  private void goAll() {
    locations.stream().forEach(λ -> go(λ));
  }
  @SuppressWarnings("static-method") protected ASTVisitor astVisitor() {
    return new ASTVisitor() {/**/};
  }
  protected String perform(final String fileContent) {
    return fixedPoint(fileContent);
  }
  @SuppressWarnings("static-method") protected boolean spartanize(@SuppressWarnings("unused") final File __) {
    return true;
  }
}
