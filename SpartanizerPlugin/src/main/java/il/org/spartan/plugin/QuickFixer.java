package il.org.spartan.plugin;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.*;

import il.org.spartan.plugin.SuppressSpartanizationOnOff.*;

/** A quickfix generator for spartanization refactoring
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @since 2013/07/01 */
public final class QuickFixer implements IMarkerResolutionGenerator {
  @Override public IMarkerResolution[] getResolutions(final IMarker m) {
    try {
      final GUI$Applicator $ = Spartanizations.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY));
      assert $ != null;
      return new IMarkerResolution[] { //
          $.getFix(), //
          $.getFixWithPreview(), //
          GUI$Applicator.getWringCommitDeclaration(), //
          GUI$Applicator.getWringCommitFile(), //
          GUI$Applicator.getWringCommitProject(), //
          QuickFixer.disableFunctionFix(), //
          QuickFixer.disableClassFix(), //
          QuickFixer.disableFileFix(), //
      }; //
    } catch (final CoreException x) {
      Plugin.log(x);
      return new IMarkerResolution[] {};
    }
  }

  public static IMarkerResolution disableFunctionFix() {
    return GUI$Applicator.getToggle(SuppressSpartanizationOnOff.Type.FUNCTION, "Disable spartanization for scope");
  }

  public static IMarkerResolution disableFileFix() {
    return GUI$Applicator.getToggle(SuppressSpartanizationOnOff.Type.FILE, "Disable spartanization for file");
  }

  public static IMarkerResolution disableClassFix() {
    return GUI$Applicator.getToggle(SuppressSpartanizationOnOff.Type.CLASS, "Disable spartanization for class");
  }
}