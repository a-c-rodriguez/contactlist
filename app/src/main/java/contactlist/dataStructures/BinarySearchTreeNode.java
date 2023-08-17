package contactlist.dataStructures;

public class BinarySearchTreeNode<T extends Comparable<T>>
implements ITreeNode<T> {

    public T data;
    public BinarySearchTreeNode<T> leftNode;
    public BinarySearchTreeNode<T> rightNode;

    public BinarySearchTreeNode(T value) {
        this.data = value;
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
    
    protected int getLeftHeight() {
        return maxChildHeight(leftNode);
    }

    protected int getRightHeight() {
        return maxChildHeight(rightNode);
    }

    private int maxChildHeight(ITreeNode<T> child) {
        if(null != child) {
          return 1 + Math.max(maxChildHeight(child.getLeftNode()), maxChildHeight(child.getRightNode()));
        }

        return 0;
    }
}

