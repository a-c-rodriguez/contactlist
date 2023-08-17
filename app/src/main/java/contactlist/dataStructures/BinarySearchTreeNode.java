package contactlist.dataStructures;

public class BinarySearchTreeNode<T extends Comparable<T>>{

    public T data;
    public BinarySearchTreeNode<T> leftNode;
    public BinarySearchTreeNode<T> rightNode;

    public BinarySearchTreeNode(T value) {
        this.data = value;
    }

}

