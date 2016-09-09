package il.org.sparan.classfiles;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.classfiles.*;

@SuppressWarnings("static-method") public class FilenameTest {
  @Test public void Head() {
    assertEquals("", Filename.headPart(""));
    assertEquals("", Filename.headPart("a"));
    assertEquals("", Filename.headPart("$"));
    assertEquals("", Filename.headPart("$a"));
    assertEquals("", Filename.headPart("a$"));
    assertEquals("a.b", Filename.headPart("a.b.c"));
    assertEquals("a.b", Filename.headPart("a.b.c$1"));
    assertEquals("a.b", Filename.headPart("a.b.c$d"));
    assertEquals("a.b", Filename.headPart("a.b.$d"));
    assertEquals("a.b$1.c$d", Filename.headPart("a.b$1.c$d.X"));
    assertEquals("a.b$a", Filename.headPart("a.b$a.c$d"));
    assertEquals("a.b", Filename.headPart("a.b.c"));
    assertEquals("a.b.c.$", Filename.headPart("a.b.c.$.XXX"));
    assertEquals("a.b$c", Filename.headPart("a.b$c.x$d"));
    assertEquals("com.sun.corba.se.impl.io", Filename.headPart("com.sun.corba.se.impl.io.ValueUtility$IdentityKeyValueStack$KeyValuePair"));
    assertEquals("mypackage", Filename.headPart("mypackage.Myclass$1"));
    assertEquals("", Filename.headPart("a$1"));
    assertEquals("b", Filename.headPart("b.a$1"));
    assertEquals("b", Filename.headPart("b.a$c"));
    assertEquals("com.sun.corba.se.impl.encoding", Filename.headPart("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1"));
    assertEquals("com.sun.security.auth.callback", Filename.headPart("com.sun.security.auth.callback.DialogCallbackHandler$2"));
    assertEquals("javax.swing", Filename.headPart("javax.swing.JSlider$1SmartHashtable$LabelUIResource"));
    assertEquals("", Filename.headPart("$1"));
    assertEquals("", Filename.headPart("a"));
    assertEquals("mypackage", Filename.headPart("mypackage.Myclass$A"));
    assertEquals("", Filename.headPart("a$1"));
    assertEquals("", Filename.headPart("a$b"));
    assertEquals("b", Filename.headPart("b.a$1"));
    assertEquals("b", Filename.headPart("b.a$b"));
    assertEquals("b", Filename.headPart("b.a$c"));
    assertEquals("com.sun.corba.se.impl.encoding", Filename.headPart("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1"));
    assertEquals("com.sun.security.auth.callback", Filename.headPart("com.sun.security.auth.callback.DialogCallbackHandler$2"));
    assertEquals("com.sun.corba.se.impl.encoding", Filename.headPart("com.sun.corba.se.impl.encoding.CodeSetConversion$UTF16BTCConverter"));
    assertEquals("com.sun.corba.se.impl.encoding",
        Filename.headPart("com.sun.corba.se.impl.encoding.IDLJavaSerializationInputStream$_ByteArrayInputStream"));
    assertEquals("javax.swing", Filename.headPart("javax.swing.JSlider$1SmartHashtable$LabelUIResource"));
  }

  @Test public void IsAllInner() {
    assert !Filename.isAllInner("$1");
    assert !Filename.isAllInner("a");
    assert Filename.isAllInner("mypackage.Myclass$A");
    assert !Filename.isAllInner("a$1");
    assert Filename.isAllInner("a$b");
    assert !Filename.isAllInner("b.a$1");
    assert Filename.isAllInner("b.a$b");
    assert Filename.isAllInner("b.a$c");
    assert !Filename.isAllInner("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1");
    assert !Filename.isAllInner("com.sun.security.auth.callback.DialogCallbackHandler$2");
    assert Filename.isAllInner("com.sun.corba.se.impl.encoding.CodeSetConversion$UTF16BTCConverter");
    assert Filename.isAllInner("com.sun.corba.se.impl.encoding.IDLJavaSerializationInputStream$_ByteArrayInputStream");
    assert !Filename.isAllInner("javax.swing.JSlider$1SmartHashtable$LabelUIResource");
  }

  @Test public void IsAnonymous() {
    assert Filename.isAnonymous("A$1");
    assert !Filename.isAnonymous("a");
    assert Filename.isAnonymous("mypackage.Myclass$1");
    assert Filename.isAnonymous("a$1");
    assert Filename.isAnonymous("b.a$1");
    assert !Filename.isAnonymous("b.a$c");
    assert Filename.isAnonymous("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1");
    assert Filename.isAnonymous("com.sun.security.auth.callback.DialogCallbackHandler$2");
    assert !Filename.isAnonymous("javax.swing.JSlider$1SmartHashtable$LabelUIResource");
  }

  @Test public void IsInner() {
    assert !Filename.isInner("$1");
    assert !Filename.isInner("a");
    assert Filename.isInner("mypackage.Myclass$A");
    assert !Filename.isInner("a$1");
    assert Filename.isInner("a$b");
    assert !Filename.isInner("b.a$1");
    assert Filename.isInner("b.a$b");
    assert Filename.isInner("b.a$c");
    assert !Filename.isInner("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1");
    assert !Filename.isInner("com.sun.security.auth.callback.DialogCallbackHandler$2");
    assert Filename.isInner("com.sun.corba.se.impl.encoding.CodeSetConversion$UTF16BTCConverter");
    assert Filename.isInner("com.sun.corba.se.impl.encoding.IDLJavaSerializationInputStream$_ByteArrayInputStream");
    assert Filename.isInner("javax.swing.JSlider$1SmartHashtable$LabelUIResource");
  }

  @Test public void IsLocal() {
    assert !Filename.isLocal("$1");
    assert !Filename.isLocal("a");
    assert Filename.isLocal("mypackage.Myclass$2A");
    assert !Filename.isLocal("a$1");
    assert !Filename.isLocal("a$b");
    assert !Filename.isLocal("b.a$1");
    assert !Filename.isLocal("b.a$b");
    assert Filename.isLocal("b.a$2c");
    assert !Filename.isLocal("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1");
    assert !Filename.isLocal("com.sun.security.auth.callback.DialogCallbackHandler$2");
    assert !Filename.isLocal("com.sun.corba.se.impl.activation.RepositoryImpl$DBServerDef");
    assert !Filename.isLocal("javax.swing.JSlider$1SmartHashtable$LabelUIResource");
  }

  @Test public void Name2Canonical() {
    assertEquals("", Filename.name2Canonical(""));
    assertEquals("a", Filename.name2Canonical("a"));
    assertEquals("a.b.c", Filename.name2Canonical("a.b.c"));
    assertEquals("a.b.c.d", Filename.name2Canonical("a.b.c$d"));
    assertEquals("a.b$c.x.d", Filename.name2Canonical("a.b$c.x$d"));
    assertEquals("com.sun.corba.se.impl.io.ValueUtility.IdentityKeyValueStack.KeyValuePair",
        Filename.name2Canonical("com.sun.corba.se.impl.io.ValueUtility$IdentityKeyValueStack$KeyValuePair"));
  }

  @Test public void Path2Class() {
    assertEquals("a.b.c", Filename.path2class("a.b.c"));
    assertEquals("a.b.c", Filename.path2class("a/b/c"));
    assertEquals("a.b.c", Filename.path2class("a/b/c.class"));
    assertEquals("a.b.c$1", Filename.path2class("a/b/c$1.class"));
    assertEquals("a.b.c$d", Filename.path2class("a/b/c$d.class"));
    assertEquals("a.b$1.c$d", Filename.path2class("a/b$1/c$d.class"));
    assertEquals("a.b$a.c$d", Filename.path2class("a/b$a/c$d.class"));
  }

  @Test public void simpleClass2Path() {
    assertEquals("il/ac/technion/cs/ssdl/classfiles/FilenameTest", Filename.class2path(this.getClass()));
  }

  @Test public void simpleObject2Path() {
    assertEquals("il/ac/technion/cs/ssdl/classfiles/FilenameTest", Filename.class2path(this));
  }

  @Test public void simpleString2Path() {
    assertEquals("a/b", Filename.class2path("a.b"));
  }

  @Test public void Tail() {
    assertEquals("", Filename.tailPart(""));
    assertEquals("a", Filename.tailPart("a"));
    assertEquals("$", Filename.tailPart("$"));
    assertEquals("$a", Filename.tailPart("$a"));
    assertEquals("a$", Filename.tailPart("a$"));
    assertEquals("c", Filename.tailPart("a.b.c"));
    assertEquals("c$1", Filename.tailPart("a.b.c$1"));
    assertEquals("c$d", Filename.tailPart("a.b.c$d"));
    assertEquals("$d", Filename.tailPart("a.b.$d"));
    assertEquals("X", Filename.tailPart("a.b$1.c$d.X"));
    assertEquals("c$d", Filename.tailPart("a.b$a.c$d"));
    assertEquals("c", Filename.tailPart("a.b.c"));
    assertEquals("XXX", Filename.tailPart("a.b.c.$.XXX"));
    assertEquals("x$d", Filename.tailPart("a.b$c.x$d"));
    assertEquals("ValueUtility$IdentityKeyValueStack$KeyValuePair",
        Filename.tailPart("com.sun.corba.se.impl.io.ValueUtility$IdentityKeyValueStack$KeyValuePair"));
    assertEquals("Myclass$1", Filename.tailPart("mypackage.Myclass$1"));
    assertEquals("a$1", Filename.tailPart("a$1"));
    assertEquals("a$1", Filename.tailPart("b.a$1"));
    assertEquals("a$c", Filename.tailPart("b.a$c"));
    assertEquals("BufferManagerWriteCollect$1", Filename.tailPart("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1"));
    assertEquals("DialogCallbackHandler$2", Filename.tailPart("com.sun.security.auth.callback.DialogCallbackHandler$2"));
    assertEquals("JSlider$1SmartHashtable$LabelUIResource", Filename.tailPart("javax.swing.JSlider$1SmartHashtable$LabelUIResource"));
    assertEquals("$1", Filename.tailPart("$1"));
    assertEquals("a", Filename.tailPart("a"));
    assertEquals("Myclass$A", Filename.tailPart("mypackage.Myclass$A"));
    assertEquals("a$1", Filename.tailPart("a$1"));
    assertEquals("a$b", Filename.tailPart("a$b"));
    assertEquals("a$1", Filename.tailPart("b.a$1"));
    assertEquals("a$b", Filename.tailPart("b.a$b"));
    assertEquals("a$c", Filename.tailPart("b.a$c"));
    assertEquals("CodeSetConversion$UTF16BTCConverter", Filename.tailPart("com.sun.corba.se.impl.encoding.CodeSetConversion$UTF16BTCConverter"));
    assertEquals("IDLJavaSerializationInputStream$_ByteArrayInputStream",
        Filename.tailPart("com.sun.corba.se.impl.encoding.IDLJavaSerializationInputStream$_ByteArrayInputStream"));
    assertEquals("JSlider$1SmartHashtable$LabelUIResource", Filename.tailPart("javax.swing.JSlider$1SmartHashtable$LabelUIResource"));
  }

  @Test public void Trailer() {
    assertEquals("", Filename.trailerPart(""));
    assertEquals("", Filename.trailerPart("a"));
    assertEquals("", Filename.trailerPart("$"));
    assertEquals("1", Filename.trailerPart("a$1"));
    assertEquals("", Filename.trailerPart("$1"));
    assertEquals("XYZ", Filename.trailerPart("abc$XYZ"));
    assertEquals("XYZ", Filename.trailerPart("ab$XYZ"));
    assertEquals("XYZ", Filename.trailerPart("a$XYZ"));
    assertEquals("1", Filename.trailerPart("abc$XYZ$1"));
    assertEquals("1", Filename.trailerPart("ab$XYZ$1"));
    assertEquals("1", Filename.trailerPart("a$XYZ$1"));
    assertEquals("", Filename.trailerPart("$XYZ"));
    assertEquals("", Filename.trailerPart("$abc"));
    assertEquals("", Filename.trailerPart("a$"));
    assertEquals("", Filename.trailerPart("a.b.c"));
    assertEquals("1", Filename.trailerPart("a.b.c$1"));
    assertEquals("d", Filename.trailerPart("a.b.c$d"));
    assertEquals("", Filename.trailerPart("a.b.$d"));
    assertEquals("", Filename.trailerPart("a.b$1.c$d.X"));
    assertEquals("d", Filename.trailerPart("a.b$a.c$d"));
    assertEquals("", Filename.trailerPart("a.b.c"));
    assertEquals("", Filename.trailerPart("a.b.c.$.XXX"));
    assertEquals("d", Filename.trailerPart("a.b$c.x$d"));
    assertEquals("KeyValuePair", Filename.trailerPart("com.sun.corba.se.impl.io.ValueUtility$IdentityKeyValueStack$KeyValuePair"));
    assertEquals("1", Filename.trailerPart("mypackage.Myclass$1"));
    assertEquals("1", Filename.trailerPart("a$1"));
    assertEquals("1", Filename.trailerPart("b.a$1"));
    assertEquals("c", Filename.trailerPart("b.a$c"));
    assertEquals("1", Filename.trailerPart("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1"));
    assertEquals("2", Filename.trailerPart("com.sun.security.auth.callback.DialogCallbackHandler$2"));
    assertEquals("LabelUIResource", Filename.trailerPart("javax.swing.JSlider$1SmartHashtable$LabelUIResource"));
    assertEquals("", Filename.trailerPart("$1"));
    assertEquals("", Filename.trailerPart("a"));
    assertEquals("A", Filename.trailerPart("mypackage.Myclass$A"));
    assertEquals("1", Filename.trailerPart("a$1"));
    assertEquals("b", Filename.trailerPart("a$b"));
    assertEquals("1", Filename.trailerPart("b.a$1"));
    assertEquals("b", Filename.trailerPart("b.a$b"));
    assertEquals("c", Filename.trailerPart("b.a$c"));
    assertEquals("UTF16BTCConverter", Filename.trailerPart("com.sun.corba.se.impl.encoding.CodeSetConversion$UTF16BTCConverter"));
    assertEquals("_ByteArrayInputStream",
        Filename.trailerPart("com.sun.corba.se.impl.encoding.IDLJavaSerializationInputStream$_ByteArrayInputStream"));
    assertEquals("LabelUIResource", Filename.trailerPart("javax.swing.JSlider$1SmartHashtable$LabelUIResource"));
  }
}
