package com.gnahraf.util.bitmo;

import java.io.IOException;
import java.io.OutputStream;

public class Writer {
  private final TraverseState state;
  private final BitOutputStream out;

  Writer(int maxDepth, OutputStream out) {
    this.state = new TraverseState(maxDepth);
    this.out = new BitOutputStream(out);
  }

  public void write(final int value) throws IOException {
    {
      int last = state.getCompletedValue();
      if (value <= last)
        throw new IllegalArgumentException(String.valueOf(value));
    }

    final int rewindDepthValue;
    int i = 31;
    {
      // find the index of the first bit of *stackValue* that doesn't
      // match the corresponding bit in *value*
      // TODO: there's gotta be a faster way involving arithmetic instead of
      // checking individual bits one at a time..
      final int minI = state.getMaxDepth() - state.getDepth() - 1;
      final int stackValue = state.getStackValue();
      for (; i > minI
          && BitUtils.getBit(value, i) == BitUtils.getBit(stackValue, i); --i)
        ;

      rewindDepthValue = state.getMaxDepth() - i - 1;
      if (rewindDepthValue < 0)
        throw new IllegalArgumentException(value + " too large");
    }

    while (rewindDepthValue < state.getDepth()) {
      out.writeBit(false);
      state.pushBit(false);
    }

    for (; i >= 0; --i) {
      final boolean encodedBit;
      {
        boolean bit = BitUtils.getBit(value, i);
        encodedBit = state.isRight() ? bit : !bit;
      }
      out.writeBit(encodedBit);
      state.pushBit(encodedBit);
    }
  }

  public void finish() throws IOException {
    while (!state.isDone()) {
      out.writeBit(false);
      state.pushBit(false);
    }
    out.flushBits();
  }
}
