package org.spartan.text;
import static org.spartan._.nonNull;

import org.eclipse.jdt.annotation.Nullable;
import org.spartan.As;

/**
 * A trivial utility module with functions to scatter a text into an array of
 * lines, and, conversely, gather an array of lines into text. Scattering and
 * gathering obey the rule that a line is a {@link String} which does not
 * contain the end-of-line marker, while the text a strings in which all 
 * lines, including the last, are terminated by the end-of-line marker.
 * 
 * @author Yossi Gil
 * @since Jul 7, 2014
 * 
 */
public enum Lines {
	// No enum values in this fake module
	;
	/**
	 * The string which this module considers as line separator.
	 */
	// TODO: Make this configurable?
	// TODO: Default value should be computed from system properties
	public static final String END_OF_LINE_MARKER = "\n";
  /**
   * @param text
   * @return
   */
  public static String[] scatter(final @Nullable String text) {
		return text == null || text.isEmpty() ? NO_LINES : nonNull(text.split(END_OF_LINE_MARKER));
  }
  public static String gather(final String... lines) {
    final StringBuilder $ = new StringBuilder();
    for (final String line : lines)
      $.append(line).append(END_OF_LINE_MARKER);
    return As.nonNulString($);
  }
	/**
	 * A longer and more meaningful name for the array of length zero with {@String} elements.
	 */
	public static final String[] NO_LINES = new String[0];
}
