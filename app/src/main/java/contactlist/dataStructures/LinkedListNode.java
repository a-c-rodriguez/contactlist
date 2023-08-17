
package contactlist.dataStructures;

public class LinkedListNode<T extends Comparable<T>> {

    private T value;
    private LinkedListNode<T> next;

    public LinkedListNode(T value){
        this.value = value;
    }

    public T getValue() {
        return value;
    }
    
    public LinkedListNode<T> getNext(){
        return next;
    }


    public void setValue(T value){
        this.value = value;
    }

    public void setNext(LinkedListNode<T> next) {
        this.next = next;
    }



}
