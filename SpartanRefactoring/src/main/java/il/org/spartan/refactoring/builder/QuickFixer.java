package il.org.spartan.refactoring.builder;

import il.org.spartan.refactoring.spartanizations.Spartanization;
import il.org.spartan.refactoring.spartanizations.Spartanizations;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

/**
 * A quickfix generator for spartanization refactoring
 *
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @since 2013/07/01
 */
public class QuickFixer implements IMarkerResolutionGenerator {
  @Override public IMarkerResolution[] getResolutions(final IMarker m) {
    try {
      final Spartanization $ = Spartanizations.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY));
      assert $ != null;
      return new IMarkerResolution[] { $.getFix(), $.getFixWithPreview() };
    } catch (final CoreException __) {
      return new IMarkerResolution[] {};
    }
  }
}