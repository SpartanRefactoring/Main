package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

@Ignore
@SuppressWarnings("static-method")
public class Issue903 {
  @Test public void A$120() {
    trimmingOf("boolean b=false;for(int i=4;i<s.length();++i){if(i==5){tipper+=9;return x;}else return tr;y+=15;return x;}return x;")
        .gives("for(int i=4;i<s.length();++i){if(i==5){tipper+=9;return x;}else return tr;y+=15;return x;}return x;")
        .gives("for(int i=4;i<s.length();++i){if(i==5){tipper+=9;return x;}else return tr;y+=15;break;}return x;")
        .gives("for(int i=4;i<s.length();++i){if(i==5){tipper+=9;break;}else return tr;y+=15;break;}return x;");
  }
}
