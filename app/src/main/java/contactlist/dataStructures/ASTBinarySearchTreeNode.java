package contactlist.dataStructures;

import java.text.MessageFormat;

import contactlist.App;
import contactlist.dataStructures.ASTBinarySearchTree.TreeState;

public class ASTBinarySearchTreeNode<T extends Comparable<T>> 
    implements ITreeNode<T>{

    public T data;
    public ASTBinarySearchTreeNode<T> leftNode;
    public ASTBinarySearchTreeNode<T> rightNode;
    public ASTBinarySearchTreeNode<T> parentNode;
    public ASTBinarySearchTree<T> tree;         


    public ASTBinarySearchTreeNode(T value, ASTBinarySearchTreeNode<T> parentNode, ASTBinarySearchTree<T> tree) {
        this.data = value;
        this.parentNode = parentNode;
        this.tree = tree;
    }


    public T getData() {
        return this.data;
    }

    public ITreeNode<T> getRightNode() {
        return this.rightNode;
    }

    public ITreeNode<T> getLeftNode() {
        return this.leftNode;
    }

    protected void setRightNode(ASTBinarySearchTreeNode<T> newRight) {
        this.rightNode = newRight;
        if(this.rightNode != null) {
            this.rightNode.parentNode = this;
            //App.print(this.getClass().getTypeName() + "setRightNode Result (rightNode, parent) ->(" + rightNode.data + ", " + rightNode.parentNode.data +")");  
        } else {
            //App.print("setRightNode is null!");
        }
    }

    protected void setLeftNode(ASTBinarySearchTreeNode<T> newLeft) {
        this.leftNode = newLeft;
        if(this.leftNode != null) {
            this.leftNode.parentNode = this;
            //App.print(this.getClass().getTypeName() + "setLeftNode Result (leftNode, parent) ->(" + leftNode.data + ", " + leftNode.parentNode.data +")");  
        } else {
            //App.print("setLeftNode is null!");
        }
    }

    protected int getLeftHeight() {
        return maxChildHeight(leftNode);
    }

    protected int getRightHeight() {
        return maxChildHeight(rightNode);
    }

    private ASTBinarySearchTree.TreeState getTreeState(){
        int balance = getLeftHeight() - getRightHeight(); 
        if(balance > 1) {
            return TreeState.LeftHeavy;
        }

        if(balance < -1) {
            return TreeState.RightHeavy;
        }

        return TreeState.Balanced;
    }

    private int getBalanceFactor() {
        return getRightHeight() - getLeftHeight();
    }


    protected void balance() {
        TreeState currentState = getTreeState();
        if(currentState == TreeState.RightHeavy) {
            if(null != rightNode && rightNode.getBalanceFactor() < 0) {
                App.print("leftRightRotation()");
                leftRightRotation();
            } else {
                App.print("leftRotation()");
                leftRotation();
            }
        }

        if(currentState == TreeState.LeftHeavy) {
            if(null != leftNode && leftNode.getBalanceFactor() > 0) {
                App.print("rightLeftRotation()");
                rightLeftRotation();
            } else {
                App.print("rightRotation()");
                rightRotation();
            }
        }
    }


    private void leftRotation() {
    //  a
    //   \
    //    b
    //     \
    //      c
    //
    //  becomes
    //
    //      b
    //     / \
    //    a   c
    //
        ASTBinarySearchTreeNode<T> newRoot = this.rightNode;
        
        //replace current root with new root
        replaceRoot(newRoot);

        // this' right node is the left on newRoot (is smaller than this value by definition)
        setRightNode(newRoot.leftNode);
        // this becomes newRoot's left node
        newRoot.setLeftNode(this);
    }


    private void rightRotation() {
    //       c
    //      /
    //     b
    //    /
    //   a
    //
    //  setRoot
    //
    //         b    
    //       / /
    //      c a
    //
    //  setLeftNode
    //      
    //       b
    //     / /
    //    c a
    //
    //  setRightNode
    //    
    //      b   
    //     / \
    //    a   c
    //
    //  becomes
    //
    //      b
    //     / \
    //    a   c
    //
        ASTBinarySearchTreeNode<T> newRoot = this.leftNode;
        
        //replace current root with new root
        replaceRoot(newRoot);

        // this' left node is the right on newRoot (is larger than this value by definition)
        setLeftNode(newRoot.rightNode);
        // this becomes newRoot's right node
        newRoot.setRightNode(this);
    }

    private void leftRightRotation() {
        this.rightNode.rightRotation();
        leftRotation();
    } 

    private void rightLeftRotation() {
        this.leftNode.leftRotation();
        rightRotation();
    }

    private void replaceRoot(ASTBinarySearchTreeNode<T> newRoot) {
        if(this.parentNode != null) {
            //App.print(this.getClass().getTypeName() +  "this.equals(this.parentNode.leftNode)?" + this.equals(this.parentNode.leftNode));
            //App.print(this.getClass().getTypeName() +  "this.equals(this.parentNode.rightNode)?" + this.equals(this.parentNode.rightNode));
            if(this.equals(this.parentNode.leftNode)) {
                this.parentNode.setLeftNode(newRoot);
            } else if(this.equals(this.parentNode.rightNode)) {
                this.parentNode.setRightNode(newRoot);
            }
        } else {
            App.print(this.getClass().getTypeName() +  "setRoot Result (root) ->(" + newRoot.data +")");  
            this.tree.setRoot(newRoot);
        }

        newRoot.parentNode = this.parentNode;
        this.parentNode = newRoot;
    }

    private int maxChildHeight(ITreeNode<T> child) {
        if(null != child) {
            //App.print(MessageFormat.format("maxChildHeight node(hasLeft, hasRight):{0}({1},{2})",child.getData(),
                //null != child.getLeftNode(), null != child.getRightNode()));
        } else {
            //App.print("child is null!!");
        }

        if(null != child) {
          return 1 + Math.max(maxChildHeight(child.getLeftNode()), maxChildHeight(child.getRightNode()));
        }

        return 0;
    }
}

