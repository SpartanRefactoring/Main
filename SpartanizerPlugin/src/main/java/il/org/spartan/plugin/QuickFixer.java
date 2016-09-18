package il.org.spartan.plugin;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.*;

/** A quickfix generator for spartanization refactoring
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @since 2013/07/01 */
public final class QuickFixer implements IMarkerResolutionGenerator {
  @Override public IMarkerResolution[] getResolutions(final IMarker m) {
    try {
      final Applicator $ = Spartanizations.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY));
      assert $ != null;
      return new IMarkerResolution[] { //
          $.getFix(), //
          $.getFixWithPreview(), //
          $.getWringCommitDeclaration(), //
          $.getWringCommitFile(), //
          $.getWringCommitProject(), //
          $.disableFunctionFix(), //
          $.disableClassFix(), //
          $.disableFileFix(), //
      }; //
    } catch (final CoreException x) {
      Plugin.log(x);
      return new IMarkerResolution[] {};
    }
  }
}