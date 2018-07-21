/*
 * Copyright 2018 Babak Farhang 
 */
package com.gnahraf.util.bitmo;

class TraverseState {
  private final int maxDepth;

  private int depth;
  private int stackValue;
  private boolean leftRight;

  private final static boolean LEFT = false;
  private final static boolean RIGHT = !LEFT;

  private int completedValue = -1;
  private boolean done;

  public TraverseState(int maxDepth) {
    this.maxDepth = maxDepth;
    leftRight = LEFT;
  }

  public int getDepth() {
    return depth;
  }

  public boolean isLeft() {
    return leftRight == LEFT;
  }

  public boolean isRight() {
    return leftRight == RIGHT;
  }

  public int getStackValue() {
    int numRightmostBitsToZeroOut = maxDepth - depth;
    int mask = 0x80000000 >> (31 - numRightmostBitsToZeroOut);
    return stackValue & mask;
  }

  public boolean isDone() {
    return done;
  }

  public boolean pushBit(boolean bit) {
    boolean completed;
    if (bit) // the bit is 1; there's a sub-branch under this node..
    {
      // if we're at the left node of our parent, this signifies a '0' bit;
      // if we're at the right node, then we have a '1' bit
      setStackValue(depth, leftRight == RIGHT);
      ++depth;

      completed = depth == maxDepth;
      if (completed) {
        completedValue = stackValue;
        popDepth();
      } else
        leftRight = LEFT;
    } else // the bit is 0; there's *no* sub-branch under this node..
    {
      if (leftRight == LEFT) {
        // there's no left sub-branch; therefore there *must* be a right
        // sub-branch. So our encoding avoids the redundant 1 bit that would
        // represent the already known existence of the right branch. We
        // add the 1 bit to the stack value, increase the depth by one, and
        // position ourselves again on the left child node...
        setStackValue(depth, true);
        completed = pushDepth();
      } else {
        popDepth();
        completed = done;
      }
    }
    return completed;
  }

  public int getCompletedValue() {
    return completedValue;
  }

  private boolean pushDepth() {
    ++depth;

    boolean completed = depth == maxDepth;
    if (completed) {
      completedValue = stackValue;
      popDepth();
    }
    return completed;
  }

  private void popDepth() {
    --depth;
    if (depth < 0)
      done = true;
    else if (getLeftRight(depth) == LEFT)
      leftRight = RIGHT;
    else
      popDepth();
  }

  private boolean getLeftRight(int level) {

    return BitUtils.getBit(stackValue, maxDepth - level - 1) ? RIGHT : LEFT;
  }

  private void setStackValue(int level, boolean value) {
    stackValue = BitUtils.setBit(stackValue, maxDepth - level - 1, value);
  }

  public int getMaxDepth() {
    return maxDepth;
  }

}
