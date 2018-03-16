# bst_practice
Exploring 3 types of balanced binary search trees.

For all three types of balancing strategies, remove is not implemented- only insert. After a node is inserted, a fix up method will be used to repair the tree- possibly rotating, coloring, or recomputing.

### AVL TREE
  Maintains pairs of self balancing sub-trees that can differ in height by at most 1.
 
  * Fix up method is found in ``` impl/AVLBSTMap.java ```
  * Tested empirically (with JUnit cases) using ``` test/AVLBSTMTest.java ```


### RED-BLACK TREE
  Maintains pairs of self balancing sub-trees where each node has an attached boolean that holds red or black. This coloring scheme is used with invariants to measure how unbalanced a tree can become, before a fix up is required.
 
  * Fix up method is found in ``` impl/TraditionalRedBlackTreeMap.java ```
  * Tested empirically (with JUnit cases) using ``` test/TRBBSTMTest.java ```
 
 
  ### LEFT-LEANING RED-BLACK TREE
  Maintains pairs of self balancing sub-trees where each left node has an attached boolean that holds red or black. This coloring scheme is used with invariants to measure how unbalanced a tree can become, before a fix up is required. For LLRBT's right nodes will not remain red after a fix up is processed.
 
  * Fix up method is found in ``` impl/LLRedBlackTreeMap.java ```
  * Tested empirically (with JUnit cases) using ``` test/LLRBTMTest.java ```
 
 
  *Note: Basic project stub, outline, and test cases are written by Dr. Thomas VanDrunen of Wheaton College (IL)*
