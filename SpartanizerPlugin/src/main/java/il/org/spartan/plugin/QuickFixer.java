package il.org.spartan.plugin;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.*;

/** A quickfix generator for spartanization refactoring
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @since 2013/07/01 */
public class QuickFixer implements IMarkerResolutionGenerator {
  @Override public IMarkerResolution[] getResolutions(final IMarker m) {
    try {
      final Spartanization $ = Spartanizations.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY));
      assert $ != null;
      return new IMarkerResolution[] { $.getFix(), $.getFixWithPreview(), $.getWringCommitDeclaration(), $.getWringCommitFile(),
          $.getWringCommitProject(), $.getToggleDeclaration(), $.getToggleClass(), $.getToggleFile() };
    } catch (@SuppressWarnings("unused") final CoreException ____) {
      return new IMarkerResolution[] {};
    }
  }
}