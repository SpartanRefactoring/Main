package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

public class CommandLine$Applicator {
  static List<Class<? extends BodyDeclaration>> selectedNodeTypes = as.list(MethodDeclaration.class);
  public Toolbox toolbox;
  @SuppressWarnings("unused") public int tippersAppliedOnCurrentObject;
  
  protected String folder = "/tmp/";
  protected String afterFileName;
  protected String beforeFileName;
  protected String inputPath;
  protected String reportFileName;
  protected String spectrumFileName;
  protected PrintWriter afters;
  protected PrintWriter befores;
  
  File currentFile;
  int done;
  
  CSVStatistics report;
  CSVStatistics spectrumStats;
  CSVStatistics coverageStats;
  
  final ChainStringToIntegerMap spectrum = new ChainStringToIntegerMap();
  final ChainStringToIntegerMap coverage = new ChainStringToIntegerMap();
  
  CommandLine$Applicator(final String path) {
    this(path, system.folder2File(path));
  }

  CommandLine$Applicator(final String inputPath, final String name){
    this.inputPath = inputPath;
    beforeFileName = folder + name + ".before.java";
    afterFileName = folder + name + ".after.java";
    reportFileName = folder + name + ".CSV";
    spectrumFileName = folder + name + ".spectrum.CSV";
    try {
      befores = new PrintWriter(beforeFileName);
      afters = new PrintWriter(afterFileName);
    } catch (final FileNotFoundException x) {
      x.printStackTrace();
    }
    
    // Matteo: Please do not delete the following instructions. 
    // They are needed to instantiate report in commandline classes
    
    try {
      report = new CSVStatistics(reportFileName, "property");
      spectrumStats = new CSVStatistics(spectrumFileName, "property");
    } catch (IOException x) {
      x.printStackTrace();
      System.err.println("problem in setting up reports");
    }
  }
  
 
  // TODO Matteo (reminder for himself): same as AbstractCommandLineSpartanizer (code duplication to be resolved)
  
  void go(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
//        System.out.println(!selectedNodeTypes.contains(¢.getClass()) || go(¢));
        assert ¢ != null;
        return !selectedNodeTypes.contains(¢.getClass()) || go(¢);
      }
    });
  }
  
  boolean go(final ASTNode input) {
    tippersAppliedOnCurrentObject = 0;
    final String output = fixedPoint(input);
    final ASTNode outputASTNode = makeAST.CLASS_BODY_DECLARATIONS.from(output);
    befores.print(input);
    afters.print(output);
    computeMetrics(input, outputASTNode);
    return false;
  }
  
  ASTNodeMetrics nm1, nm2;
  
  protected void computeMetrics(final ASTNode input, final ASTNode output) {
    // input metrics
    nm1 = new ASTNodeMetrics(input);
    nm1.computeMetrics();
//    final int length = input.getLength();
//    final int tokens = metrics.tokens(input + "");
//    final int nodes = count.nodes(input);
//    final int body = metrics.bodySize(input);
//    final int statements = extract.statements(az.methodDeclaration(input).getBody()).size();
//    final int tide = clean(input + "").length();
//    final int essence = Essence.of(input + "").length();
    // output metrics
//    final String outputString = output + "";
    nm2 = new ASTNodeMetrics(output);
    nm2.computeMetrics();
//    final int length2 = outputString.length();
//    final int tokens2 = metrics.tokens(outputString);
//    final int nodes2 = count.nodes(output);
//    final int body2 = metrics.bodySize(output);
//    final MethodDeclaration methodDeclaration = az.methodDeclaration(output);
//    final int statements2 = methodDeclaration == null ? -1 : extract.statements(methodDeclaration.getBody()).size();
//    final int tide2 = clean(outputString).length();
//    final int essence2 = Essence.of(outputString).length();
//    final int wordCount = code.wc(il.org.spartan.spartanizer.cmdline.Essence.of(outputString));
    // final ASTNode to = makeAST.CLASS_BODY_DECLARATIONS.from(output);
    System.err.println(++done + " " + extract.category(input) + " " + extract.name(input));
    System.out.println(befores.checkError());
    report.summaryFileName();
    reportMetrics(nm1, "1");
    reportMetrics(nm2, "2");
    reportDifferences(nm1, nm2);
    reportRatio(nm1, "1");
    reportRatio(nm2, "2");
//    report//
//        // .put("File", currentFile)//
//        .put("Category", extract.category(input))//
//        .put("Name", extract.name(input))//
//        .put("# Tippers", tippersAppliedOnCurrentObject) //
//        .put("Nodes1", nodes)//
//        .put("Nodes2", nodes2)//
//        .put("Δ Nodes", nodes - nodes2)//
//        .put("δ Nodes", system.d(nodes, nodes2))//
//        .put("δ Nodes %", system.p(nodes, nodes2))//
//        .put("Body", body)//
//        .put("Body2", body2)//
//        .put("Δ Body", body - body2)//
//        .put("δ Body", system.d(body, body2))//
//        .put("% Body", system.p(body, body2))//
//        .put("Length1", length)//
//        .put("Tokens1", tokens)//
//        .put("Tokens2", tokens2)//
//        .put("Δ Tokens", tokens - tokens2)//
//        .put("δ Tokens", system.d(tokens, tokens2))//
//        .put("% Tokens", system.p(tokens, tokens2))//
//        .put("Length1", length)//
//        .put("Length2", length2)//
//        .put("Δ Length", length - length2)//
//        .put("δ Length", system.d(length, length2))//
//        .put("% Length", system.p(length, length2))//
//        .put("Tide1", tide)//
//        .put("Tide2", tide2)//
//        .put("Δ Tide2", tide - tide2)//
//        .put("δ Tide2", system.d(tide, tide2))//
//        .put("δ Tide2", system.p(tide, tide2))//
//        .put("Essence1", essence)//
//        .put("Essence2", essence2)//
//        .put("Δ Essence", essence - essence2)//
//        .put("δ Essence", system.d(essence, essence2))//
//        .put("% Essence", system.p(essence, essence2))//
//        .put("Statements1", statements)//
//        .put("Statement2", statements2)//
//        .put("Δ Statement", statements - statements2)//
//        .put("δ Statement", system.d(statements, statements2))//
//        .put("% Statement", system.p(essence, essence2))//
//        .put("Words)", wordCount).put("R(T/L)", system.ratio(length, tide)) //
//        .put("R(E/L)", system.ratio(length, essence)) //
//        .put("R(E/T)", system.ratio(tide, essence)) //
//        .put("R(B/S)", system.ratio(nodes, body)) //
//    ;
    report.nl();
  }
  
  /**
   * 
   * @param nm
   * @param id
   */
  
  public void reportMetrics(final ASTNodeMetrics nm, final String id){
    report//
    .put("Nodes" + id, nm.nodes())//
    .put("Body" + id, nm.body())//
    .put("Length" + id, nm.length())//
    .put("Tokens" + id, nm.tokens())//
    .put("Tide" + id, nm.tide())//
    .put("Essence" + id, nm.essence())//
    .put("Statements" + id, nm.statements());//
  }
  
  /**
   * 
   * @param nm1
   * @param nm2
   */
  
  public void reportDifferences(@SuppressWarnings("hiding") final ASTNodeMetrics nm1, @SuppressWarnings("hiding") final ASTNodeMetrics nm2){
    report //
    .put("Δ Nodes", nm1.nodes() - nm2.nodes())//
    .put("δ Nodes", system.d(nm1.nodes(), nm2.nodes()))//
    .put("δ Nodes %", system.p(nm1.nodes(), nm2.nodes()))//
    .put("Δ Body", nm1.body() - nm2.body())//
    .put("δ Body", system.d(nm1.body(), nm2.body()))//
    .put("% Body", system.p(nm1.body(), nm2.body()))//
    .put("Δ Tokens", nm1.tokens() - nm2.tokens())//
    .put("δ Tokens", system.d(nm1.tokens(), nm2.tokens()))//
    .put("% Tokens", system.p(nm1.tokens(), nm2.tokens()))//
    .put("Δ Length", nm1.length() - nm2.length())//
    .put("δ Length", system.d(nm1.length(), nm2.length()))//
    .put("% Length", system.p(nm1.length(), nm2.length()))//
    .put("Δ Tide2", nm1.tide() - nm2.tide())//
    .put("δ Tide2", system.d(nm1.tide(), nm2.tide()))//
    .put("δ Tide2", system.p(nm1.tide(), nm2.tide()))//
    .put("Δ Essence", nm1.essence() - nm2.essence())//
    .put("δ Essence", system.d(nm1.essence(), nm2.essence()))//
    .put("% Essence", system.p(nm1.essence(), nm2.essence()))//
    .put("Δ Statement", nm1.statements() - nm2.statements())//
    .put("δ Statement", system.d(nm1.statements(), nm2.statements()))//
    .put("% Statement", system.p(nm1.statements(), nm2.statements()));//
  }
  
  /**
   * 
   * @param nm
   */
  
  public void reportRatio(final ASTNodeMetrics nm, final String id){
    report
//    .put("Words)", wordCount).put("R(T/L)", system.ratio(length, tide)) //
    .put("R(E/L)" + id, system.ratio(nm.length(), nm.essence())) //
    .put("R(E/T)" + id, system.ratio(nm.tide(), nm.essence())) //
    .put("R(B/S)" + id, system.ratio(nm.nodes(), nm.body())); //
  }
  
  String fixedPoint(final ASTNode ¢) {
    System.out.println(¢);
    return fixedPoint(¢ + "");
  }
  
  public String fixedPoint(final String from) {
    for (final Document $ = new Document(from);;) {
      final BodyDeclaration u = (BodyDeclaration) makeAST.CLASS_BODY_DECLARATIONS.from($.get());
      final ASTRewrite r = createRewrite(u);
      final TextEdit e = r.rewriteAST($, null);
      try {
        e.apply($);
      } catch (final MalformedTreeException | IllegalArgumentException | BadLocationException x) {
        monitor.logEvaluationError(this, x);
        throw new AssertionError(x);
      }
      if (!e.hasChildren())
        return $.get();
    }
  }
  
  public ASTRewrite createRewrite(final BodyDeclaration u) {
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    consolidateTips($, u);
    return $;
  }
    
  public void consolidateTips(final ASTRewrite r, final BodyDeclaration u) {
    toolbox = Toolbox.defaultInstance();
    u.accept(new DispatchingVisitor() {
      @SuppressWarnings("synthetic-access") @Override protected <N extends ASTNode> boolean go(final N n) {
        TrimmerLog.visitation(n);
        if (disabling.on(n))
          return true;
        Tipper<N> tipper = null;
        try {
          tipper = getTipper(n);
        } catch (final Exception x) {
          monitor.debug(this, x);
        }
        if (tipper == null)
          return true;
        Tip s = null;
        try {
          s = tipper.tip(n, exclude);
//          tick(n, tipper);
        } catch (final TipperFailure f) {
          monitor.debug(this, f);
        } catch (final Exception x) {
          monitor.debug(this, x);
        }
        if (s != null) {
          ++tippersAppliedOnCurrentObject;
//          tick2(tipper); // save coverage info
          TrimmerLog.application(r, s);
        }
        return true;
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }
  
  <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return toolbox.firstTipper(¢);
  }

}