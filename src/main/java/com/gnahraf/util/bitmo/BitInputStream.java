/*
 * Copyright 2018 Babak Farhang 
 */
package com.gnahraf.util.bitmo;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitInputStream extends FilterInputStream {
  private byte b;
  private int bi = 8;

  public BitInputStream(InputStream in) {
    super(in);
  }

  public boolean readBit() throws IOException {
    if (bi == 8) {
      final int next = read();
      if (next == -1)
        throw new EOFException();
      b = (byte) next;
    }
    final boolean value = BitUtils.getBit(b, --bi);
    if (bi == 0)
      bi = 8;
    return value;
  }

}
