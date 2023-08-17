package contactlist.dataStructures;

public interface ITreeNode<T extends Comparable <T>> {

    public T getData();
    public ITreeNode<T> getRightNode();
    public ITreeNode<T> getLeftNode();

}
