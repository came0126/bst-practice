package impl;

import impl.RedBlackTreeMap.RBNode;

/**
 * LLRedBlackTreeMap
 * 
 * A BST map using the left-leaning red-black approach for
 * maintaining a balanced tree. This inherits most of the code for
 * manipulating the BST from RecursiveBSTMap. The method LLRBVerify.verify
 * is run on the root of the tree and throws and exception if the the 
 * tree violates any of the left leaning red black tree conditions.
 * 
 * @author Thomas VanDrunen
 * CSCI 345, Wheaton College
 * July 2, 2015
 * @param <K> The key type
 * @param <V> The value type
 * @param <N> A super type of nodes in whatever child class is refining this class
 */
public class LLRedBlackTreeMap<K extends Comparable<K>, V> extends RedBlackTreeMap<K, V> {


    /**
     * Exception to indicate that a red node is a right child.
     */
    public static class RedRightException extends RuntimeException {

        private static final long serialVersionUID = 3766304873228252316L;

        public RedRightException(String msg) { super(msg); }
    }
    
    /**
     * Class for real, "non-null" nodes, containing the code for
     * fixing up the tree when left-leaning red-black tree properties are violated.
     */
   private class LLRBRealNode extends impl.RedBlackTreeMap<K,V>.RBRealNode{

       /**
        * Plain constructor
        */
        public LLRBRealNode(K key, V val,
                RBNode<K,V> left,
                RBNode<K, V> right) {
            super(key, val, left, right);
        }

        /**
         * Fix this subtree to conform to the constraints of RB trees.
         * PRECONDITION: left and right subtrees are left-leaning red-black trees
         * except that (1) their roots (left and right themselves) may be red,
         * and (2) at most one of them may have a double red that involves
         * their root. Exception (2) would happen only if this node is
         * black.
         * POSTCONDITION: This tree has been modified to contain the 
         * same information as before but also to satisfy the LL RB 
         * constraints, with the exception that the root of
         * this subtree may be red and, if so, it may have a red child (thus
         * it is possible that on exit this subtree may have a double red violation,
         * but only in the case of a red root with a red left child). 
         * The node on which this method is called,
         * currently the root of this subtree, might no longer be
         * the root; the root of the modified tree is returned.
         * @return The root of the tree like this one but
         * satisfying the constraints.
         */
        public RBNode<K, V> putFixup() {

			RBNode<K, V> replace = this;

			// Left node and right node are both red -> right-red violation
			if(left.isRed() && right.isRed()) {
				replace.left().blacken();
				replace.right().blacken();
				replace.redden();
				//Recompute black heights
				replace.left().recomputeBlackHeight();
				replace.right().recomputeBlackHeight();
				replace.recomputeBlackHeight();
			}

			// Right node is only red -> right-red violation
			else if(right.isRed()) {
				replace = rotateLeft();
				
				//Temp to hold color of root
				boolean leftIsRed = replace.left().isRed();
				//Redden root
				replace.left().redden();
				//Swap colors
				if(leftIsRed)
					replace.redden();
				else
					replace.blacken();
				//Rotate left on root
				replace.left().recomputeBlackHeight();
				
				replace.recomputeBlackHeight();
			}

			// Left node and left.left node is also red -> double-red violation
			else if(left.isRed() && left.left().isRed()) {
				//Rotate right on root
				replace = rotateRight();
				replace.left().blacken();
				replace.left().recomputeBlackHeight();
				
			}

			// What if there are no violations?
			return replace;
        }        

        /**
         * Rotate this tree to the left.
         * @return The node that is the new root
         */
        private RBNode<K, V> rotateLeft() {
        	LLRBRealNode replace = (LLRedBlackTreeMap<K, V>.LLRBRealNode) this.right;

        	LLRBRealNode newLeft = this;
			// Give newLeft the potental subtree at replace's left
			newLeft.right = replace.left;

			replace.left = newLeft;
			// The right subtree's black height attributes may have changed
			right.recomputeBlackHeight();
			return replace;
        }
        
        /**
         * Rotate this tree to the right.
         * @return The node that is the new root
         */
       private RBNode<K, V> rotateRight() {
			LLRBRealNode replace = (LLRedBlackTreeMap<K, V>.LLRBRealNode) this.left;

			LLRBRealNode newRight = this;
			// Give newRight the potental subtree at replace's right
			newRight.left = replace.right;

			replace.right = newRight;
			// The left subtree's black height attributes may have changed
			right.recomputeBlackHeight();

			return replace;
        }
   }
   
   public LLRedBlackTreeMap(boolean verifying) {
        super(new LLRBVerify<K,V,RBNode<K,V>>(),verifying);
    }
    
   /**
    * Factory method for making new real nodes, used by the
    * code in the parent class which does not have direct access
    * to the class LLRBRealNode defined here.
    */
    protected RBNode<K, V> realNodeFactory(K key,
            V val, RBNode<K, V> left,
            RBNode<K, V> right) {
        return new LLRBRealNode(key, val, left, right);
    }

    
    
}
