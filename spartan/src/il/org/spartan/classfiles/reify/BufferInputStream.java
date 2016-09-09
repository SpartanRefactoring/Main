/* Copyright (c) 1994, 2005, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. This code is
 * free software; you can redistribute it and/or modify it under the terms of
 * the GNU General Public License version 2 only, as published by the Free
 * Software Foundation. Oracle designates this particular file as subject to the
 * "Classpath" exception as provided by Oracle in the LICENSE file that
 * accompanied this code. This code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License version 2 for more details (a copy is included in the LICENSE
 * file that accompanied this code). You should have received a copy of the GNU
 * General Public License version 2 along with this work; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA. Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA
 * 94065 USA or visit www.oracle.com if you need additional information or have
 * any questions. */
package il.org.spartan.classfiles.reify;

import java.io.*;

import il.org.spartan.utils.*;

/** A sane re-implementation of {@link ByteArrayInputStream}.
 * @author Yossi Gil */
public class BufferInputStream extends InputStream {
  protected byte bytes[];
  protected int position = 0;
  protected int mark = 0;
  protected int length;

  public BufferInputStream(final byte bytes[]) {
    this.bytes = bytes;
    length = bytes.length;
  }

  @Override public synchronized int available() {
    return length - position;
  }

  @Override public void close() {
    ____.nothing();
  }

  public boolean eof() {
    return !more();
  }

  @Override public synchronized void mark(final int limit) {
    mark = position;
  }

  @Override public boolean markSupported() {
    return true;
  }

  public boolean more() {
    return position < length;
  }

  public final int position() {
    return position;
  }

  @Override public synchronized int read() {
    return !more() ? -1 : bytes[position++] & 0xff;
  }

  @Override public synchronized int read(final byte b[], final int offset, final int len) {
    if (b == null)
      throw new NullPointerException();
    if (offset < 0 || len < 0 || len > b.length - offset)
      throw new IndexOutOfBoundsException();
    if (eof())
      return -1;
    if (len <= 0)
      return 0;
    final int toRead = position + len > length ? length - position : len;
    System.arraycopy(bytes, position, b, offset, toRead);
    position += toRead;
    return toRead;
  }

  @Override public synchronized void reset() {
    position = mark;
  }

  @Override public synchronized long skip(long n) {
    if (position + n > length)
      n = length - position;
    if (n < 0)
      return 0;
    position += n;
    return n;
  }
}