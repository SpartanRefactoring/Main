package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Dor Ma'ayan
 * @since 2017-04-09 */
public class Issue1157 extends TipperTest<SingleVariableDeclaration> {
  /** [[SuppressWarningsSpartan]] - see #1245 */
  @Test public void t1() {
    trimmingOf("" //
        + "@Override public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> function) {" //
        + "  checkNonNull(key);" //
        + "  checkNonNull(function);" //
        + "  return compute(key, (k, oldValue) -> (oldValue == null) ? null : function.apply(k, oldValue));" //
        + "}").stays();
  }
  /** [[SuppressWarningsSpartan]] - see #1245 */
  @Test public void t2() {
    trimmingOf("" //
        + "private void setTextParams(StringFieldEditor r) {" //
        + "  r.setStringValue(\"$\");" //
        + "  Text t = r.getTextControl(getFieldEditorParent());" //
        + "  t.addVerifyListener(new VerifyListener() {     " //
        + "    @Override public void verifyText(VerifyEvent e) {" //
        + "      String val = r.getStringValue();" //
        + "      if(val.isEmpty()) {" //
        + "        if(!Character.isJavaIdentifierStart(e.character))" //
        + "          e.doit = false;" //
        + "      } else if(!Character.isJavaIdentifierPart(e.character))" //
        + "        e.doit = false;" //
        + "    }" //
        + "  });" //
        + "  t.addFocusListener(new FocusListener() {" //
        + "    @Override public void focusLost(@SuppressWarnings(\"unused\") FocusEvent e) {" //
        + "      Names.returnNameSelect = ReturnNameSelect.byConst;" //
        + "      if(r.getStringValue().isEmpty())" //
        + "        Names.returnName = \"$\";" //
        + "      else" //
        + "        Names.returnName = r.getStringValue();" //
        + "    }" //
        + "  });" //
        + "}").stays();
  }
  @Override public Tipper<SingleVariableDeclaration> tipper() {
    return new ParameterAbbreviate();
  }
  @Override public Class<SingleVariableDeclaration> tipsOn() {
    return SingleVariableDeclaration.class;
  }
}
