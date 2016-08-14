package il.org.spartan.refactoring.builder;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.*;

import il.org.spartan.*;

/**
 * A quickfix generator for spartanization refactoring
 *
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @since 2013/07/01
 */
public class QuickFixer implements IMarkerResolutionGenerator {
  @Override public IMarkerResolution[] getResolutions(final IMarker m) {
    try {
      final String attribute = (String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY);
      azzert.notNull(attribute);
      final CurrentAST $ = new CurrentAST();
      assert $ != null;
      System.err.println(attribute);
      azzert.notNull(attribute, $);
      return new IMarkerResolution[] { $.getFix(), $.getFixWithPreview() };
    } catch (final CoreException __) {
      return new IMarkerResolution[] {};
    }
  }
}