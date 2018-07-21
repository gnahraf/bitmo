#Bitmo

A compression scheme for moderately populated bit vectors

##Synopsis

This uses a simple, bit-level prefix compression. Although I've seen variations on this scheme
in closed source software, I've never seen it properly documented. This is rescued code I originally
I banged out to share in an email conversation with a colleague some years ago. It's probably mostly
of pedagogical interest (seldom beats P4Delta, for eg). Probably.

##Binary Data Model

A Bitmo vector represents a set numbers in the range [0, 2^depth) using a tree abstraction. In this
model, the leaves of the tree represent membership in the set of numbers. The ASCII diagram below
depicts a tree of depth 3.

                               ROOT
                          /             \
                         1               1
                      /     \         /     \
                     1       1       0       1
                    / \     / \     / \     / \
                   1   0   0   1   0   0   0   1
                   
The above tree represents 3 of 8 possible numbers in the range 0 thru 7 (see the bottom leaf row).
The numbers represented above are 0, 3, and 7.
Its binary format encodes a pre-order traversal of the tree. For the above tree, this looks like

        11101011000101

Note however that the zeroes below a zero parent node are redundant. We may therefore prune the
tree as follows.

                               ROOT
                          /             \
                         1               1
                      /     \         /     \
                     1       1       0       1
                    / \     / \             / \
                   1   0   0   1           0   1

This becomes

        111010110101


So a zero in the middle of the tree tells us not to follow that branch. Other optimizations are possible.
For example (not yet implemented here), we can represent 0, 3, 4, 5, 6, and 7
using

                               ROOT
                          /             \
                         1               1
                      /     \         /     \
                     1       1       0       0
                    / \     / \ 
                   1   0   0   1



Here, we exploit the fact if we are are not following zero branches, then we should never encounter 2
consecutive zeroes. And since we don't, we can reserve two consecutive zeroes to mean the opposite: i.e.
everything under that branch is populated.

## Performance

Early benchmarks indicate about 1.4 bytes overhead per number when the set contains about 1% of the numbers
in a given range. The overhead drops the bigger the percentage of membership, of course.

## Next Steps

More testing in order to better understand if this algo has sweet spots for applicability.
