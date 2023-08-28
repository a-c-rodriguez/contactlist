package contactlist.dataStructures;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import contactlist.App;
/**
 * ArrayASTBinarySearchTree
 */
public class ArrayASTBinarySearchTree<T extends Comparable<T>> implements Collection<T> {

    public enum TreeState {
        RightHeavy,
        LeftHeavy,
        Balanced
    }

    private int count = 0;
    private List<T> nodes = new ArrayList<>(20);

    public T getNode(int index) {
        T node = null;
        if(index < this.nodes.size()) {
            node = this.nodes.get(index);
        } 
        return node;
    }


    public void setNode(int index, T value){
        if(index > this.nodes.size()-1) {
            int newSize = (int)Math.abs((this.nodes.size()*2)+1);
            App.print(MessageFormat.format("Old size {0} -> New Size {1}", this.nodes.size(), newSize));
            for(int i=0; i < newSize; i++) {
                this.nodes.add(null);
            }
        }

        if(index < this.nodes.size()) {
            T oldVal = this.nodes.set(index, value);
            App.print(MessageFormat.format("Replaced old val {0} at index {1} with new val {2}", oldVal, index, value));
        } else {
            App.print(MessageFormat.format("this.nodes size:{0} > index:{1}", this.nodes.size(), index));
        }

    }

    public void setRoot(T newRoot) {
        setNode(0, newRoot);
    }

    public void addNode(T value) {
        if (this.size() == 0) {
            setRoot(value);
        } else {
            insertNode(0, value);
        }
        this.count++;
    }

    private void insertNode(int nodeIndex, T value) {
        if(null == value) return;

        T node = this.getNode(nodeIndex);
        if (value.compareTo(node) < 0) {
            int leftNodeIndex = (2*nodeIndex)+1;
            if (null == this.getNode(leftNodeIndex)) {
                setNode(leftNodeIndex, value);
            } else {
                insertNode(leftNodeIndex, value);
            }
        } else {
            int rightNodeIndex = (2*nodeIndex)+2;
            if (null == this.getNode(rightNodeIndex)) {
                setNode(rightNodeIndex, value);
            } else {
                insertNode(rightNodeIndex, value);
            }
        }

        //node.balance();
    }

    public void deleteNode(Object o) {
        T val = null;
        if (o instanceof Comparable) {
            val = (T) o;
        }
        if (null != val)
            deleteNode(val);
    }

    public void deleteNode(T value) {
        Result<T> foundNode = search(value);
        if (foundNode == null) {
            contactlist.App.print("No match foundNode for " + value.toString());
            return;
        } else {

            int parentNodeIndex = (foundNode.index == 0) ? -1 : (foundNode.index-1)/2;
            T parentNode = this.getNode(parentNodeIndex);
            int indexToBalance = parentNodeIndex;

            int leftNodeIndex = (2*foundNode.index)+1;
            int rightNodeIndex = (2*foundNode.index)+2;

            // zero child - delete
            if (null == this.getNode(leftNodeIndex) && null == this.getNode(rightNodeIndex)) {
                if (null != parentNode) {
                    int parentLeftNodeIndex = (2*parentNodeIndex)+1;
                    int parentRightNodeIndex = (2*parentNodeIndex)+2;
                    if (foundNode.value.compareTo(this.getNode(parentLeftNodeIndex)) == 0) {
                        setNode(parentLeftNodeIndex, null);
                    } else if (foundNode.value.compareTo(this.getNode(parentRightNodeIndex)) == 0) {
                        setNode(parentRightNodeIndex, null);
                    }
                } else {
                    this.setRoot(null);
                }
                foundNode = null;
            // two children - goto right child and promote left most child
            } else if (null != this.getNode(leftNodeIndex) && null != this.getNode(rightNodeIndex))  {
                T foundNodeLeft = this.getNode(leftNodeIndex);
                T foundNodeRight = this.getNode(rightNodeIndex);
                int leftMostChildIndex = (2*rightNodeIndex)+1;
				Result<T> parentLeftMostChild = new Result<T>(rightNodeIndex, foundNodeRight);
                Result<T> leftMostChild = new Result<T>(rightNodeIndex, foundNodeRight);
                while (leftMostChildIndex < this.nodes.size()-1 && null != this.getNode(leftMostChildIndex)) {
        			parentLeftMostChild = leftMostChild;
					leftMostChild = new Result<T>(leftMostChildIndex, this.getNode(leftMostChildIndex));
                    leftMostChildIndex = (2*leftMostChildIndex)+1;
                } 

                T temp;
               if(null != leftMostChild){
                    temp = leftMostChild.value;
                    //parentLeftMostChild.setLeftNode(temp.rightNode);
                } else { 
                    temp = foundNodeRight;
                }
                if (parentNode != null) {
                    int parentLeftNodeIndex = (2*parentNodeIndex)+1;
                    int parentRightNodeIndex = (2*parentNodeIndex)+2;
                    if (foundNode.value.compareTo(this.getNode(parentLeftNodeIndex)) == 0) {
                        setNode(parentLeftNodeIndex, temp);
                    } else if (foundNode.value.compareTo(this.getNode(parentRightNodeIndex)) == 0) {
                        setNode(parentRightNodeIndex, temp);
                    }
                } else {
                    //temp.parentNode = null;
                    this.setRoot(temp);
                }
                
                //not necessary since previous code set this value
                //setNode(foundNode.index, temp);
                setNode(leftNodeIndex, foundNodeLeft);
                //foundNode.setLeftNode(foundNodeLeft);
                //foundNode.setRightNode(foundNodeRight);
                if(null != leftMostChild) {
                    //foundNode.setRightNode(foundNodeRight);
                }
            } else {
                // one child - promote child
                T child = (null == this.getNode(rightNodeIndex)) ? this.getNode(leftNodeIndex) : this.getNode(rightNodeIndex);
                if (parentNode != null) {
                    int parentLeftNodeIndex = (2*parentNodeIndex)+1;
                    int parentRightNodeIndex = (2*parentNodeIndex)+2;
                    if (foundNode.value.compareTo(this.getNode(parentLeftNodeIndex)) == 0) {
                        setNode(parentLeftNodeIndex, child);
                    } else if (foundNode.value.compareTo(this.getNode(parentRightNodeIndex)) == 0) {
                        setNode(parentRightNodeIndex, child);
                    }
                } else {
                    //child.parentNode = null;
                    this.setRoot(child);
                }
            }
            this.count--;
/*
            if(null != treeToBalance) {
                App.print("Tree to balance -> " + treeToBalance.getData());
                treeToBalance.balance();
            } else {
                if(null != this.rootNode) {
                    App.print("Root to balance -> " + this.rootNode.getData());
                    this.rootNode.balance();
                }
            }
*/
        }
    }

    public Result<T> search(Object o) {
        T val = null;
        if (o instanceof Comparable) {
            val = (T) o;
        }
        if (null != val) {
            return search(val);
        } else {
            return null;
        }
    }

    public Result<T> search(T value) {
        return findNode(0, value);
    }

    private Result<T> findNode(int nodeIndex, T valueToFind) {
        T node = this.getNode(nodeIndex);
        App.print(MessageFormat.format("Node ({0}, {1}) -> ValueToFind ({2})", nodeIndex, node, valueToFind));
        if (node == null) {
            return null;
        } else {
            if (valueToFind.compareTo(node) == 0) {
                return new Result<T>(nodeIndex, node);
            } else if (valueToFind.compareTo(node) < 0) {
                return findNode((2*nodeIndex)+1, valueToFind);
            } else {
                return findNode((2*nodeIndex)+2, valueToFind);
            }
        }
    }

    private void InPlaceTraversal(List<T> list, int nodeIndex) {
        if (nodeIndex > -1 && nodeIndex < this.nodes.size()-1) {
            // contactlist.App.print(MessageFormat.format("Traversing {0}",
            // node.data.toString()));
            InPlaceTraversal(list, (2*nodeIndex)+1);
            T node = this.getNode(nodeIndex);
            if(null != node) list.add(node);
            // contactlist.App.print(MessageFormat.format("Added {0} to list",
            // node.data.toString()));
            InPlaceTraversal(list, (2*nodeIndex)+2);
        }
    }

    /**
     * Returns the number of elements in this collection. If this collection
     * contains more than {@code Integer.MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.
     *
     * @return the number of elements in this collection
     */
    public int size() {
        return this.count;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    public boolean isEmpty() {
        return this.count == 0;
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * More formally, returns {@code true} if and only if this collection
     * contains at least one element {@code e} such that
     * {@code Objects.equals(o, e)}.
     *
     * @param o element whose prese cnce in this collection is to be tested
     * @return {@code true} if this collection contains the specified
     *         element
     * @throws ClassCastException   if the type of the specified element
     *                              is incompatible with this collection
     *                              (<a href=
     *                              "{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              collection does not permit null elements
     *                              (<a href=
     *                              "{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    public boolean contains(Object o) {
        Result<T> node = search(o);
        return null != node && null != node.value;
    }

    /**
     * Returns an iterator over the elements in this collection. There are no
     * guarantees concerning the order in which the elements are returned
     * (unless this collection is an instance of some class that provides a
     * guarantee).
     *
     * @return an {@code Iterator} over the elements in this collection
     */
    public Iterator<T> iterator() {
        List<T> sortedList = new ArrayList<>();
        InPlaceTraversal(sortedList, 0);
        // contactlist.App.print(sortedList.size() + " items in list");
        return sortedList.iterator();
    }

    /**
     * Returns an array containing all of the elements in this collection.
     * If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order. The returned array's {@linkplain Class#getComponentType
     * runtime component type} is {@code Object}.
     *
     * <p>
     * The returned array will be "safe" in that no references to it are
     * maintained by this collection. (In other words, this method must
     * allocate a new array even if this collection is backed by an array).
     * The caller is thus free to modify the returned array.
     *
     * @apiNote
     *          This method acts as a bridge between array-based and
     *          collection-based APIs.
     *          It returns an array whose runtime type is {@code Object[]}.
     *          Use {@link #toArray(Object[]) toArray(T[])} to reuse an existing
     *          array, or use {@link #toArray(IntFunction)} to control the runtime
     *          type
     *          of the array.
     *
     * @return an array, whose {@linkplain Class#getComponentType runtime component
     *         type} is {@code Object}, containing all of the elements in this
     *         collection
     */
    public Object[] toArray() {
        List<T> list = new ArrayList<>();
        InPlaceTraversal(list, 0);
        return list.toArray();
    }

    /**
     * Returns an array containing all of the elements in this collection;
     * the runtime type of the returned array is that of the specified array.
     * If the collection fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this collection.
     *
     * <p>
     * If this collection fits in the specified array with room to spare
     * (i.e., the array has more elements than this collection), the element
     * in the array immediately following the end of the collection is set to
     * {@code null}. (This is useful in determining the length of this
     * collection <i>only</i> if the caller knows that this collection does
     * not contain any {@code null} elements.)
     *
     * <p>
     * If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     *
     * @apiNote
     *          This method acts as a bridge between array-based and
     *          collection-based APIs.
     *          It allows an existing array to be reused under certain
     *          circumstances.
     *          Use {@link #toArray()} to create an array whose runtime type is
     *          {@code Object[]},
     *          or use {@link #toArray(IntFunction)} to control the runtime type of
     *          the array.
     *
     *          <p>
     *          Suppose {@code x} is a collection known to contain only strings.
     *          The following code can be used to dump the collection into a
     *          previously
     *          allocated {@code String} array:
     *
     *          <pre>
     *     String[] y = new String[SIZE];
     *     ...
     *     y = x.toArray(y);
     *          </pre>
     *
     *          <p>
     *          The return value is reassigned to the variable {@code y}, because a
     *          new array will be allocated and returned if the collection {@code x}
     *          has
     *          too many elements to fit into the existing array {@code y}.
     *
     *          <p>
     *          Note that {@code toArray(new Object[0])} is identical in function to
     *          {@code toArray()}.
     *
     * @param <E> the component type of the array to contain the collection
     * @param a   the array into which the elements of this collection are to be
     *            stored, if it is big enough; otherwise, a new array of the same
     *            runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this collection
     * @throws ArrayStoreException  if the runtime type of any element in this
     *                              collection is not assignable to the
     *                              {@linkplain Class#getComponentType
     *                              runtime component type} of the specified array
     * @throws NullPointerException if the specified array is null
     */
    public <E> E[] toArray(E[] a) {
        if (a.length < count)
            a = (E[]) java.lang.reflect.Array
                    .newInstance(a.getClass().getComponentType(), count);
        List<T> list = new ArrayList<>();
        InPlaceTraversal(list, 0);
        return list.toArray(a);
    }

    // Modification Operations

    /**
     * Ensures that this collection contains the specified element (optional
     * operation). Returns {@code true} if this collection changed as a
     * result of the call. (Returns {@code false} if this collection does
     * not permit duplicates and already contains the specified element.)
     * <p>
     *
     * Collections that support this operation may place limitations on what
     * elements may be added to this collection. In particular, some
     * collections will refuse to add {@code null} elements, and others will
     * impose restrictions on the type of elements that may be added.
     * Collection classes should clearly specify in their documentation any
     * restrictions on what elements may be added.
     * <p>
     *
     * If a collection refuses to add a particular element for any reason
     * other than that it already contains the element, it <i>must</i> throw
     * an exception (rather than returning {@code false}). This preserves
     * the invariant that a collection always contains the specified element
     * after this call returns.
     *
     * @param e element whose presence in this collection is to be ensured
     * @return {@code true} if this collection changed as a result of the
     *         call
     * @throws UnsupportedOperationException if the {@code add} operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the class of the specified element
     *                                       prevents it from being added to this
     *                                       collection
     * @throws NullPointerException          if the specified element is null and
     *                                       this
     *                                       collection does not permit null
     *                                       elements
     * @throws IllegalArgumentException      if some property of the element
     *                                       prevents it from being added to this
     *                                       collection
     * @throws IllegalStateException         if the element cannot be added at this
     *                                       time due to insertion restrictions
     */
    public boolean add(T e) {
        addNode(e);
        return true;
    }

    /**
     * Removes a single instance of the specified element from this
     * collection, if it is present (optional operation). More formally,
     * removes an element {@code e} such that
     * {@code Objects.equals(o, e)}, if
     * this collection contains one or more such elements. Returns
     * {@code true} if this collection contained the specified element (or
     * equivalently, if this collection changed as a result of the call).
     * o
     * 
     * @param o element to be removed from this collection, if present
     * @return {@code true} if an element was removed as a result of this call
     * @throws ClassCastException            if the type of the specified element
     *                                       is incompatible with this collection
     *                                       (<a href=
     *                                       "{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified element is null and
     *                                       this
     *                                       collection does not permit null
     *                                       elements
     *                                       (<a href=
     *                                       "{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws UnsupportedOperationException if the {@code remove} operation
     *                                       is not supported by this collection
     */
    public boolean remove(Object o) {
        deleteNode(o);
        return !contains(o);
    }

    // Bulk Operations

    /**
     * Returns {@code true} if this collection contains all of the elements
     * in the specified collection.
     *
     * @param c collection to be checked for containment in this collection
     * @return {@code true} if this collection contains all of the elements
     *         in the specified collection
     * @throws ClassCastException   if the types of one or more elements
     *                              in the specified collection are incompatible
     *                              with this
     *                              collection
     *                              (<a href=
     *                              "{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified collection contains one
     *                              or more null elements and this collection does
     *                              not permit null
     *                              elements
     *                              (<a href=
     *                              "{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>),
     *                              or if the specified collection is null.
     * @see #contains(Object)
     */
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    /**
     * Adds all of the elements in the specified collection to this collection
     * (optional operation). The behavior of this operation is undefined if
     * the specified collection is modified while the operation is in progress.
     * (This implies that the behavior of this call is undefined if the
     * specified collection is this collection, and this collection is
     * nonempty.)
     *
     * @param c collection containing elements to be added to this collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code addAll} operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the class of an element of the
     *                                       specified
     *                                       collection prevents it from being added
     *                                       to this collection
     * @throws NullPointerException          if the specified collection contains a
     *                                       null element and this collection does
     *                                       not permit null elements,
     *                                       or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     *                                       specified collection prevents it from
     *                                       being added to this
     *                                       collection
     * @throws IllegalStateException         if not all the elements can be added at
     *                                       this time due to insertion restrictions
     * @see #add(Object)
     */
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    /**
     * Removes all of this collection's elements that are also contained in the
     * specified collection (optional operation). After this call returns,
     * this collection will contain no elements in common with the specified
     * collection.
     *
     * @param c collection containing elements to be removed from this collection
     * @return {@code true} if this collection changed as a result of the
     *         call
     * @throws UnsupportedOperationException if the {@code removeAll} method
     *                                       is not supported by this collection
     * @throws ClassCastException            if the types of one or more elements
     *                                       in this collection are incompatible
     *                                       with the specified
     *                                       collection
     *                                       (<a href=
     *                                       "{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if this collection contains one or more
     *                                       null elements and the specified
     *                                       collection does not support
     *                                       null elements
     *                                       (<a href=
     *                                       "{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>),
     *                                       or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    /**
     * Retains only the elements in this collection that are contained in the
     * specified collection (optional operation). In other words, removes from
     * this collection all of its elements that are not contained in the
     * specified collection.
     *
     * @param c collection containing elements to be retained in this collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the types of one or more elements
     *                                       in this collection are incompatible
     *                                       with the specified
     *                                       collection
     *                                       (<a href=
     *                                       "{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if this collection contains one or more
     *                                       null elements and the specified
     *                                       collection does not permit null
     *                                       elements
     *                                       (<a href=
     *                                       "{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>),
     *                                       or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    /**
     * Removes all of the elements from this collection (optional operation).
     * The collection will be empty after this method returns.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *                                       is not supported by this collection
     */
    public void clear() {
    }

    // Comparison and hashing

    /**
     * Compares the specified object with this collection for equality.
     * <p>
     *
     * While the {@code Collection} interface adds no stipulations to the
     * general contract for the {@code Object.equals}, programmers who
     * implement the {@code Collection} interface "directly" (in other words,
     * create a class that is a {@code Collection} but is not a {@code Set}
     * or a {@code List}) must exercise care if they choose to override the
     * {@code Object.equals}. It is not necessary to do so, and the simplest
     * course of action is to rely on {@code Object}'s implementation, but
     * the implementor may wish to implement a "value comparison" in place of
     * the default "reference comparison." (The {@code List} and
     * {@code Set} interfaces mandate such value comparisons.)
     * <p>
     *
     * The general contract for the {@code Object.equals} method states that
     * equals must be symmetric (in other words, {@code a.equals(b)} if and
     * only if {@code b.equals(a)}). The contracts for {@code List.equals}
     * and {@code Set.equals} state that lists are only equal to other lists,
     * and sets to other sets. Thus, a custom {@code equals} method for a
     * collection class that implements neither the {@code List} nor
     * {@code Set} interface must return {@code false} when this collection
     * is compared to any list or set. (By the same logic, it is not possible
     * to write a class that correctly implements both the {@code Set} and
     * {@code List} interfaces.)
     *
     * @param o object to be compared for equality with this collection
     * @return {@code true} if the specified object is equal to this
     *         collection
     *
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     * @see List#equals(Object)
     */
    public boolean equals(Object o) {
        return false;
    }

    /**
     * Returns the hash code value for this collection. While the
     * {@code Collection} interface adds no stipulations to the general
     * contract for the {@code Object.hashCode} method, programmers should
     * take note that any class that overrides the {@code Object.equals}
     * method must also override the {@code Object.hashCode} method in order
     * to satisfy the general contract for the {@code Object.hashCode} method.
     * In particular, {@code c1.equals(c2)} implies that
     * {@code c1.hashCode()==c2.hashCode()}.
     *
     * @return the hash code value for this collection
     *
     * @see Object#hashCode()
     * @see Object#equals(Object)
     */
    public int hashCode() {
        return Integer.MIN_VALUE;
    }

    public String print() {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i < this.nodes.size(); i++) {
            T node = this.getNode(i);
            if(null != node) {
                sb.append(MessageFormat.format("({0},{1}), ", i, node));
            }
        }
        sb.append(MessageFormat.format("Array size:{0}, Count:{1}", this.nodes.size(), this.size()));
        return sb.toString();
    }
}

class Result<T extends Comparable<T>> {
    public int index = -1;
    public T value;

    public Result(int index, T val){
        this.index = index;
        this.value = val;
    }
}
