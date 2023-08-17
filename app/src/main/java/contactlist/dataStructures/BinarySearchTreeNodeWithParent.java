package contactlist.dataStructures;

public class BinarySearchTreeNodeWithParent<T extends Comparable<T>> {

    public BinarySearchTreeNode<T> parentNode;
    public BinarySearchTreeNode<T> childNode;

    public BinarySearchTreeNodeWithParent(BinarySearchTreeNode<T> parent, BinarySearchTreeNode<T> child) {
        this.parentNode = parent;
        this.childNode = child;
    }

}
