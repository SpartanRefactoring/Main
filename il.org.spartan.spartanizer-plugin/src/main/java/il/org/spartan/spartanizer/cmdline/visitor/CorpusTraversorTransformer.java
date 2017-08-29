package il.org.spartan.spartanizer.cmdline.visitor;

import static il.org.spartan.tide.*;

import java.io.*;
import java.nio.file.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.nodes.metrics.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.tables.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.tables.*;
import op.*;
import op.traverse.*;

/**  A traversal where there each java file is also converted textual to something else.
 * @author Yossi Gil
 * @since 2017-08-24 */
public abstract class CorpusTraversorTransformer extends FeaturedTraversor<FeaturedTraversor<CorpusT>>{
  private String transformedContent;  
  
  @External(alias = "cp") boolean copy;
  @External(alias = "u") boolean unique;

  public CorpusTraversorTransformer(String[] arguments) {
    super(arguments);
    withTapper(new Run.IdleTapper() {
      @Override @SuppressWarnings("synthetic-access") public void beginFile() {
        transformedContent = output();
      }
    });
    if (copy)
      withHierarchyCopy();
    if (unique)
      withBeforeAfter();
  }
  public String transformedContent() {
    return transformedContent;
  }

  public CorpusTraversorTransformer withBeforeAfter() {
    try (PrintWriter inputWriter = new PrintWriter(inputFolder + File.separator + projectName() + "-input.java");
        PrintWriter outputWriter = new PrintWriter(outputFolder + File.separator + projectName() + "-output.java")) {
      withTapper(new Traverse.Tapper() {
        @Override public void beginFile() {
          inputWriter.append(fileContents());
        }
        @Override public void endFile() {
          outputWriter.append(transformedContent());
        }
        @Override public void endProject() {
          inputWriter.flush();
          outputWriter.flush();
        }
      });
    } catch (FileNotFoundException ¢) {
      note.io(¢);
    }
    return this;
  }

  public CorpusTraversorTransformer withCopy() {
    try (PrintWriter inputWriter = new PrintWriter(inputFolder + File.separator + projectName() + "-input.java");
        PrintWriter outputWriter = new PrintWriter(outputFolder + File.separator + projectName() + "-output.java")) {
      withTapper(new Traverse.Tapper() {
        @Override public void beginFile() {
          inputWriter.append(fileContents());
        }
        @Override public void endFile() {
          outputWriter.append(transformedContent());
        }
        @Override public void endProject() {
          inputWriter.flush();
          outputWriter.flush();
        }
      });
    } catch (FileNotFoundException ¢) {
      note.io(¢);
    }
    return this;
  }
  public CorpusTraversorTransformer withHierarchyCopy() {
    withTapper(new Traverse.Tapper() {
      @Override public void endFile() {
        try (PrintWriter w = new PrintWriter(new File(outputPath()))) {
          w.append(transformedContent());
        } catch (FileNotFoundException ¢) {
          note.io(¢);
        }
      }
      String outputPath() {
        Path filePath = Paths.get(fileAbsolutePath());
        Path locationPath = Paths.get(new File(inputFolder + File.separator + projectName()).getAbsolutePath());
        return locationPath.relativize(filePath) + "";
      }
    });
    return this;
  }
  public abstract String output();
  protected Execution withMetricsTapper() {
    return withTapper(new Traverse.Tapper() {
      final Table table = new Table(Table.classToNormalizedFileName(Table_Summary.class) + "-" + corpusName(), outputFolder);

      @Override public void endBatch() {
        table.close();
      }
      @Override public void endFile() {
        final ASTNode input = wizard.asCu(fileContents());
        final ASTNode output = wizard.asCu(transformedContent());
        table.col("Project", projectName())//
            .col("File", fileName())//
            .col("Path", relativePath());
        for (final Metric.Integral m : metrics())
          table.col(m.name + "-input", m.apply(input));
        for (final Metric.Integral m : metrics())
          table.col(m.name + "-output", m.apply(output));
        table.nl();
      }
      Metric.Integral[] metrics() {
        return as.array(//
            Metric.named("length").withBody((ToIntFunction<ASTNode>) Metrics::length), //
            Metric.named("essence").withBody((ToIntFunction<ASTNode>) λ -> Essence.of(λ + "").length()), //
            Metric.named("tokens").withBody((ToIntFunction<ASTNode>) λ -> Metrics.tokens(λ + "")), //
            Metric.named("nodes").withBody((ToIntFunction<ASTNode>) countOf::nodes), //
            Metric.named("body").withBody((ToIntFunction<ASTNode>) Metrics::bodySize), //
            Metric.named("methodDeclaration")
                .withBody((ToIntFunction<ASTNode>) λ -> !iz.methodDeclaration(λ) ? -1 : extract.statements(az.methodDeclaration(λ).getBody()).size()),
            Metric.named("tide").withBody((ToIntFunction<ASTNode>) λ -> clean(λ + "").length()));//
      }
    });
  }

}
