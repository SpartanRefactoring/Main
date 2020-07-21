package il.org.spartan.spartanizer.traversal;

import static java.util.stream.Collectors.*;
import il.org.spartan.spartanizer.cmdline.CurrentData;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.plugin.preferences.revision.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** The interface of * {@link TraversalImplementation}, conducting a traversal
 * of an AST for two distinct purposes, {@link #collectTips(CompilationUnit)},
 * to present the tips in the problems view, and {@link #go(CompilationUnit)},
 * which collects a maximal set of tips for rewriting.
 * <p>
 * The search is restricted by the {@link #range} field. If the field is null,
 * no restriction. Otherwise, search only within the tree range specified by
 * this field.
 * <ol>
 * <li>
 * @author Yossi Gil
 * @since 2015/07/10 */
public abstract class Traversal implements Selfie<Traversal> {
  
  protected final Int rewriteCount = new Int();
  private Tip otherTip;
  private CompilationUnit compilationUnit;
  private TextEditGroup editGroup;
  private Range range;
  protected final Tips tips = Tips.empty();
  protected final Map<IProject, Toolbox> configurations = new HashMap<>();
  public String fileName;
  protected boolean firstAddition = true;
  protected ASTNode node;
  protected ASTRewrite rewrite;
  protected Tip tip;
  protected Tipper<?> tipper;
  protected boolean useProjectPreferences;
  public static Table table = new Table("Dummy");  
  int tipReject;
  int noTipper;

  public Toolbox toolbox = Toolboxes.allClone();
  protected String currentFileName;
  /** A list of all listeners to actions carried out by this instance. */
  public final TraversalTappers notify = new TraversalTappers()//
      .push(new TraversalTapper() {
        @Override public void begin() {
          rewriteCount.clear();
        }
        @Override public void tipRewrite() {
          rewriteCount.step();
        }
      }) //
      .push(new TraversalTapper() {
        @Override public void begin() {
          if (!useProjectPreferences)
            return;
          final Toolbox $ = getPreferredConfiguration(compilationUnit());
          toolbox = $ != null ? $ : toolbox;
        }
      }) //
      .push(new TraversalTapper() {
        @Override public void begin() {
          //System.err.println(" --- Begin Tippization --- ");
          //System.err.println("filename: " + fileName);
          table.col("Project", project);
          table.col("File", fileName);
          currentFileName = fileName;
          tipReject = 0;
          noTipper = 0;
          }
        @Override public void setNode(){
          //System.err.println(" --- setNode --- ");
          }
        @Override public void noTipper()      {
          //System.err.println(" --- noTipper --- ");
          ++noTipper;
          }
        @Override public void tipperAccepts() {
          //System.err.println(" --- tipperAccepts --- ");
          table.col("Project", project);
          table.col("File", currentFileName);
          table.col("Absolute Path",CurrentData.absolutePath);
          table.col("Relative Path",CurrentData.relativePath);
          table.col("Tips",tips.size()+1);
          table.col("Tipper",tipper.tipperName());
          table.col("Group", tipper.tipperGroup().label());
          table.col("Parents",tipper.tipperGroup().parents().size());
          }
        @Override public void tipperRejects() {
          //System.err.println(" --- tipperRejects --- ");
          //System.err.println(currentFileName);
          ++tipReject;
          }
        @Override public void tipperTip()     {
          //System.err.println(" --- tipperTip --- ");
          table.col("Tipper Class",tip.tipperClass + "");
          table.col("Tip Highlight (from)",tip.highlight.from + "");
          table.col("Tip Highlight (to)",tip.highlight.to + "");
          table.col("Tip Span (from)", tip.span.from + "");
          table.col("Tip Span (to)", tip.span.to + "");
          table.col("Tip Description", "\"" + tip.description + "\"");
          table.col("Tip lineNumber", tip.lineNumber + "");
          table.col("Tip lastComponent", cCamelCase.lastComponent(tip.tipperClass + "") + "");
          }
        @Override public void tipPrune()      {
          //System.err.println(" --- tipPrune --- ");
          }
        @Override public void tipRewrite()    {
          //System.err.println(" --- tipRewrite --- ");
          table.nl();
          }
        @Override public void end(){
          //System.err.println(" --- End Tippization --- ");
          System.err.println("tipReject:\t" + tipReject);
          System.err.println("noTipper:\t" + tipReject);
        }
       });
  public String project;

  /** Checks a Compilation Unit (outermost ASTNode in the Java Grammar) for
   * tipper tips
   * @param u what to check
   * @return a collection of non-overlapping {@link Tip} objects each containing
   *         a spartanization tip */
  public Tips collectTips(final CompilationUnit ¢) {
    tips.clear();
    setCompilationUnit(¢);
    ¢.accept(tipsCollector());
    return tips;
  }
  public abstract ASTRewrite go(CompilationUnit u);
  public TraversalTappers pop() {
    return notify.pop();
  }
  public Traversal push(final TraversalTapper ¢) {
    notify.push(¢);
    return this;
  }
  @Override public Traversal self() {
    return this;
  }
  public Traversal setRange(final Range ¢) {
    return self(() -> range = ¢);
  }
  public Tipper<? extends ASTNode> tipper() {
    return tipper;
  }
  protected abstract ASTVisitor tipsCollector();
  public Traversal useProjectPreferences() {
    useProjectPreferences = true;
    configurations.clear();
    return this;
  }
  public Traversal notUseProjectPreferences() {
    useProjectPreferences = false;
    configurations.clear();
    return this;
  }
  @SuppressWarnings("static-method") protected <N extends ASTNode> boolean check(@SuppressWarnings("unused") final N __) {
    return true;
  }
  protected void clearTipper() {
    tipper = null;
  }
  protected CompilationUnit compilationUnit() {
    return compilationUnit;
  }
  protected Tip getAuxiliaryTip() {
    return otherTip;
  }
  protected TextEditGroup getCurrentEditGroup() {
    return editGroup;
  }
  public ASTNode getNode() {
    return node;
  }
  /** @param u JD
   * @return {@link Toolbox} by project's preferences */
  protected Toolbox getPreferredConfiguration(final CompilationUnit u) {
    if (u == null)
      return null;
    final ITypeRoot r = u.getTypeRoot();
    if (r == null)
      return null;
    final IJavaProject jp = r.getJavaProject();
    if (jp == null)
      return null;
    final IProject ret = jp.getProject();
    if (ret == null)
      return null;
    if (configurations.containsKey(ret))
      return configurations.get(ret);
    final Toolbox $ = Toolboxes.allClone();
    final Set<Class<Tipper<? extends ASTNode>>> es = XMLSpartan.enabledTippers(ret);
    final Collection<Tipper<?>> xs = $.getAllTippers().stream().filter(λ -> !es.contains(λ.getClass())).collect(toList());
    for (final List<Tipper<? extends ASTNode>> ¢ : $.implementation)
      ¢.removeAll(xs);
    configurations.put(ret, $);
    return $;
  }
  protected Range getRange() {
    return range;
  }
  protected ASTRewrite getRewrite() {
    return rewrite;
  }
  protected Tip getTip() {
    return tip;
  }
  /** [[SuppressWarningsSpartan]] */
  protected Tip setAuxiliaryTip(final Tip auxiliaryTip) {
    return otherTip = auxiliaryTip;
  }
  protected void setCompilationUnit(final CompilationUnit ¢) {
    compilationUnit = ¢;
    fileName = English.unknownIfNull(¢.getJavaElement(), IJavaElement::getElementName);
    notify.begin();
  }
  protected void setNode(final ASTNode currentNode) {
    node = currentNode;
    notify.setNode();
  }
  protected void setRewrite(final ASTRewrite currentRewrite) {
    rewrite = currentRewrite;
  }
  protected void setTipper(final Tipper<?> currentTipper) {
    tipper = currentTipper;
    if (tipper() == null)
      notify.noTipper();
    else
      notify.tipperAccepts();
  }

  public abstract class __ {
    // @formatter:off
    protected Tip auxiliaryTip() { return getAuxiliaryTip(); }
    protected ASTRewrite rewrite() { return getRewrite(); }
    protected Traversal self() { return Traversal.this.self(); }
    protected Tip tip() { return getTip(); }
    // @formatter:on
    protected String node() {
      final ASTNode $ = self().getNode();
      return String.format("%s(%s)", English.name($), Trivia.gist($));
    }
  }

  public int rewriteCount() {
    return rewriteCount.get();
  }
}