package il.org.spartan.plugin.old;

import static il.org.spartan.plugin.old.RefactorerUtil.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.operation.*;
import org.eclipse.jface.text.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Single tipper applicator implementation using modified {@link Trimmer}
 * @author Ori Roth
 * @since 2016 */
@SuppressWarnings("deprecation")
public class SingleTipper<N extends ASTNode> extends Trimmer {
  final Tipper<N> tipper;

  public SingleTipper(final Tipper<N> tipper) {
    this.tipper = tipper;
  }

  @Override protected boolean check(@NotNull final ASTNode ¢) {
    return Toolbox.defaultInstance().get(¢.getNodeType()).contains(tipper);
  }

  @Override @SuppressWarnings("unchecked") @Nullable protected Tipper<N> getTipper(@NotNull final ASTNode ¢) {
    assert check(¢);
    return !tipper.canTip((N) ¢) ? null : tipper;
  }

  /** Marker resolution. Commits single tipper in current function.
   * @author Ori Roth
   * @since 2016 */
  public static class InDeclaration extends Resolution {
    @Override @Nullable protected ITextSelection domain(@NotNull final IMarker m) {
      final ICompilationUnit u = eclipse.currentCompilationUnit();
      if (u == null)
        return null;
      final ASTNode n = eclipse.getNodeByMarker(u, m);
      if (n == null)
        return null;
      final ASTNode $ = yieldAncestors.untilClass(BodyDeclaration.class).from(n);
      return $ == null ? null : new TextSelection($.getStartPosition(), $.getLength());
    }

    @Override @NotNull public String getLabelSuffix() {
      return "enclosing function";
    }

    @Override public Selection getSelection(@NotNull final IMarker ¢) {
      return Selection.Util.getCurrentCompilationUnit().setTextSelection(domain(¢));
    }

    private static InDeclaration instance;

    @NotNull public static InDeclaration instance() {
      return instance = instance != null ? instance : new InDeclaration();
    }
  }

  /** Marker resolution. Commits single tipper in current file.
   * @author Ori Roth
   * @since 2016 */
  public static class InFile extends Resolution {
    @Override protected ITextSelection domain(@SuppressWarnings("unused") final IMarker __) {
      return TextSelection.emptySelection();
    }

    @Override @NotNull public String getLabelSuffix() {
      return "compilation unit";
    }

    @Override public Selection getSelection() {
      return Selection.Util.getCurrentCompilationUnit();
    }

    private static InFile instance;

    @NotNull public static InFile instance() {
      return instance = instance != null ? instance : new InFile();
    }
  }

  /** Marker resolution. Commits single tipper in current project.
   * @author Ori Roth
   * @since 2016 */
  public static class InProject extends Resolution {
    @Override protected ITextSelection domain(@SuppressWarnings("unused") final IMarker __) {
      return TextSelection.emptySelection();
    }

    @Override @NotNull public String getLabelSuffix() {
      return "entire project";
    }

    @Override public Selection getSelection() {
      return Selection.Util.getAllCompilationUnits();
    }

    @Override @NotNull public String getOpeningMessage(@NotNull final Map<attribute, Object> ¢) {
      final int $ = getCUsCount(¢);
      return "Applying " + getTipperName(¢) + " to " + projectName(¢) + " with " + $ + " " + Linguistic.plurals("file", $) + "\n" //
          + "Tips before:\t" + ¢.get(attribute.TIPS_BEFORE);
    }

    @Override @NotNull @SuppressWarnings("boxing") public String getEndingMessage(@NotNull final Map<attribute, Object> ¢) {
      final int $ = getChangesCount(¢);
      return "Done applying " + getTipperName(¢) + " to " + projectName(¢) + "\n" + $ + " " + Linguistic.plurals("file", $) + " spartanized in "
          + ¢.get(attribute.PASSES) + " " + Linguistic.plurales("pass", (int) ¢.get(attribute.PASSES)) + "\n" + "Tips commited:\t"
          + ¢.get(attribute.TOTAL_TIPS) + "\n" + "Total tips before:\t" + ¢.get(attribute.TIPS_BEFORE) + "\n" + "Total tips after:\t"
          + ¢.get(attribute.TIPS_AFTER);
    }

    @Override @NotNull public String getProgressMonitorSubMessage(final List<ICompilationUnit> currentCompilationUnits,
        @NotNull final ICompilationUnit currentCompilationUnit) {
      return wizard.completionIndex(currentCompilationUnits, currentCompilationUnit) + " : " + currentCompilationUnit.getElementName();
    }

    @Override public int getProgressMonitorWork(@NotNull final List<ICompilationUnit> ¢) {
      return ¢.size();
    }

    @Override public boolean hasDisplay() {
      return true;
    }

    @Override @Nullable public IRunnableWithProgress initialWork(final AbstractGUIApplicator a, final List<ICompilationUnit> us,
        final Map<attribute, Object> m) {
      return countTipsInProject(a, us, m, attribute.TIPS_BEFORE);
    }

    @Override @Nullable public IRunnableWithProgress finalWork(final AbstractGUIApplicator a, final List<ICompilationUnit> us,
        final Map<attribute, Object> m) {
      return countTipsInProject(a, us, m, attribute.TIPS_AFTER);
    }

    private static InProject instance;

    @NotNull public static InProject instance() {
      return instance = instance != null ? instance : new InProject();
    }
  }

  abstract static class Resolution extends Refactorer {
    @Override public boolean isMarkerResolution() {
      return true;
    }

    @Override @NotNull public String getLabel() {
      return "Apply to " + getLabelSuffix();
    }

    @Nullable protected abstract ITextSelection domain(IMarker m);

    @NotNull public abstract String getLabelSuffix();

    @Override @SuppressWarnings({ "unchecked", "rawtypes" }) @Nullable public AbstractGUIApplicator getApplicator(@NotNull final IMarker $) {
      try {
        assert $.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY) != null;
        return $.getResource() == null ? null : getSingleTipper((Class<? extends Tipper>) $.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY));
      } catch (@NotNull final CoreException ¢) {
        monitor.log(¢);
      }
      return null;
    }

    @Override public int passesCount() {
      return MANY_PASSES;
    }

    private static <X extends ASTNode, T extends Tipper<X>> SingleTipper<X> getSingleTipper(@NotNull final Class<T> $) {
      try {
        return new SingleTipper<>($.newInstance());
      } catch (@NotNull InstantiationException | IllegalAccessException ¢) {
        monitor.log(¢);
      }
      return null;
    }
  }
}
