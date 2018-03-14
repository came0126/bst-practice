package impl;

/**
 * TraditionalRedBlackTreeMap
 * 
 * A BST map using the (traditional) red-black approach for
 * maintaining a balanced tree. This inherits most of the code for 
 * manipulating the BST from RecursiveBSTMap. This class's purpose is 
 * to house the code for fixing up an RB tree when the red-black property
 *  is violated. TRBVerify.verify is called on the root this tree and throws
 *  an exception if the tree violated the red black tree conditions.
 * 
 * @author Thomas VanDrunen
 * CSCI 345, Wheaton College
 * July 2, 2015
 * @param <K> The key type
 * @param <V> The value type
 * @param <N> A super type of nodes in whatever child class is refining this class
 */
public class TraditionalRedBlackTreeMap<K extends Comparable<K>, V> extends RedBlackTreeMap<K, V> {

    /**
     * Class for real, "non-null" nodes, containing the code for
     * fixing up the tree when red-black tree properties are violated.
     */
    private class TradRBRealNode extends RBRealNode {

        /**
         * Plain constructor
         */
        protected TradRBRealNode(K key, V val, RBNode<K, V> left, RBNode<K, V> right) {
            super(key, val, left, right);
        }

        /**
         * Fix this subtree to conform to the constraints of RB trees.
         * PRECONDITION: left and right subtrees are red-black trees
         * except that (1) their roots (left and right themselves) may be red,
         * and (2) at most one of them may have a double red that involves
         * their root. Exception (2) would happen only if this node is
         * black.
         * POSTCONDITION: This tree has been modified to contain the 
         * same information as before but also to satisfy the RB 
         * constraints, again with the exception that the root of
         * this subtree may be red. The node on which this method is called,
         * currently the root of this subtree, might no longer be
         * the root; the root of the modified tree is returned.
         * @return The root of the tree like this one but
         * satisfying the constraints.
         */
        public RBNode<K, V> putFixup() {
        	this.recomputeBlackHeight();
        	
        	RBNode<K,V> replace = this;
        	
        	//If this is black then check LR, LL, RL, or RR double red violations
        	if(!replace.isRed()) {
        		
        		//handle RL
        		if(right.isRed() && right.left().isRed()) {
        			this.right = ((TradRBRealNode) right).rotateRight();
        			replace = this;
        		}

        		//handle LR
        		else if(left.isRed() && left.right().isRed()) {
        			this.left = ((TradRBRealNode) left).rotateRight();
        			replace = this;
        		}
        		
        		//handle RR
        		if(right.isRed() && right.right().isRed()) {
        			
        			//Checking if red uncle exists
        			if(left.isRed()) {
        				//Recolor and recalculate
        				replace.redden();
            			right.blacken();
            			left.blacken();
            			left.recomputeBlackHeight();
            			right.recomputeBlackHeight();
        			}
        			
        			//Red uncle doesn't exist so rotate
        			else {
        				//Recolor
        				replace.redden();
        				replace.right().blacken();
        				replace = rotateLeft();
        			}
        			
        			
        		}
        		
        		//handle LL
        		else if(left.isRed() && left.left().isRed()) {
        			
        			//Checking if red uncle exists
        			if(right.isRed()) {
        				//Recolor and recalculate
        				replace.redden();
            			left.blacken();
            			right.blacken();
            			right.recomputeBlackHeight();
            			left.recomputeBlackHeight();
        			}
        			
        			//Red uncle doesn't exist so rotate
        			else {
        				//Recolor
        				replace.redden();
        				replace.left().blacken();
        				replace = rotateRight();
        			}
        		
        		}
        	}
        	
        	return replace;
        	
        }

        /**
         * Rotate this tree to the left.
         * @return The node that is newly the root
         */
        private RBNode<K, V> rotateLeft() {
        	TradRBRealNode replace = (TraditionalRedBlackTreeMap<K, V>.TradRBRealNode) this.right;
        	
        	TradRBRealNode newLeft = (TraditionalRedBlackTreeMap<K, V>.TradRBRealNode) this;
        	//Give newLeft the potental subtree at replace's left
        	newLeft.right = replace.left;
        	
			replace.left = newLeft;
			//The right subtree's black height attributes may have changed
			right.recomputeBlackHeight();
        	return this;
        }

        /**
         * Rotate this tree to the right.
         * @return The node that is newly the root
         */
        private RBNode<K, V> rotateRight() {
        	TradRBRealNode replace = (TraditionalRedBlackTreeMap<K, V>.TradRBRealNode) this.left;
        	
        	TradRBRealNode newRight = (TraditionalRedBlackTreeMap<K, V>.TradRBRealNode) this;
        	//Give newRight the potental subtree at replace's right
        	newRight.left = replace.right;
        	
			replace.right = newRight;
			//The left subtree's black height attributes may have changed
			right.recomputeBlackHeight();
        	
        	return this;
        }

    }

    public TraditionalRedBlackTreeMap(boolean verifying) {
        super(new TRBVerify<K, V, RBNode<K, V>>(), verifying);
    }

    /**
     * Factory method for making new real nodes, used by the
     * code in the parent class which does not have direct access
     * to the class RBRealNode defined here.
     */
    protected RBNode<K, V> realNodeFactory(K key, V val, RBNode<K, V> left, RBNode<K, V> right) {
        return new TradRBRealNode(key, val, left, right);
    }

}
