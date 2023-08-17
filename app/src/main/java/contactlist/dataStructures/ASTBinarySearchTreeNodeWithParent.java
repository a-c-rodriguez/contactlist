package contactlist.dataStructures;

public class ASTBinarySearchTreeNodeWithParent<T extends Comparable<T>> {

    public ASTBinarySearchTreeNode<T> parentNode;
    public ASTBinarySearchTreeNode<T> childNode;

    public ASTBinarySearchTreeNodeWithParent(ASTBinarySearchTreeNode<T> parent, ASTBinarySearchTreeNode<T> child) {
        this.parentNode = parent;
        this.childNode = child;
    }

}
