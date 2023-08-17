package contactlist.dataStructures;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * ASTBinarySearchTree
 */
public class ASTBinarySearchTree<T extends Comparable<T>> implements Collection<T> {

    public enum TreeState {
        RightHeavy,
        LeftHeavy,
        Balanced
    }

    private int count = 0;
    private ASTBinarySearchTreeNode<T> rootNode;

    public void setRoot(ASTBinarySearchTreeNode<T> newRoot) {
        if (null != newRoot) {
            this.rootNode = newRoot;
        }
    }

    public void addNode(T value) {
        if (null == this.rootNode) {
            this.rootNode = new ASTBinarySearchTreeNode<T>(value, null, this);
        } else {
            insertNode(this.rootNode, value);
        }
        this.count++;
    }

    private void insertNode(ASTBinarySearchTreeNode<T> node, T value) {
        if (node.data.compareTo(value) < 0) {
            if (null == node.leftNode) {
                node.setLeftNode(new ASTBinarySearchTreeNode<T>(value, node, this));
            } else {
                insertNode(node.leftNode, value);
            }
        } else {
            if (null == node.rightNode) {
                node.setRightNode(new ASTBinarySearchTreeNode<T>(value, node, this));
            } else {
                insertNode(node.rightNode, value);
            }
        }

        node.balance();
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
        removeNode(new ASTBinarySearchTreeNode<>(value, null, this));
    }

    private void removeNode(ASTBinarySearchTreeNode<T> node) {
        ASTBinarySearchTreeNode<T> foundNode = search(node);
        if (foundNode == null) {
            contactlist.App.print("No match foundNode for " + node.data.toString());
            return;
        } else {
            ASTBinarySearchTreeNode<T> parentNode = foundNode.parentNode;
            ASTBinarySearchTreeNode<T> treeToBalance = parentNode;
            // zero child - delete
            if (null == foundNode.leftNode && null == foundNode.rightNode) {
                if (parentNode != null) {
                    if (foundNode.data.compareTo(parentNode.leftNode.data) == 0) {
                        parentNode.leftNode = null;
                    } else if (foundNode.data.compareTo(parentNode.rightNode.data) == 0) {
                        parentNode.rightNode = null;
                    }
                    foundNode = null;
                }
                // two children - goto right child and promote left most child
            } else if (null != foundNode.leftNode && null != foundNode.rightNode) {
                ASTBinarySearchTreeNode<T> foundNodeLeft = foundNode.leftNode;
                ASTBinarySearchTreeNode<T> foundNodeRight = foundNode.rightNode;
                ASTBinarySearchTreeNode<T> leftMostChild = foundNodeRight.leftNode;
                while (null != leftMostChild.leftNode) {
                    leftMostChild = leftMostChild.leftNode;
                }
                ASTBinarySearchTreeNode<T> temp = leftMostChild;
                leftMostChild = leftMostChild.rightNode;
                if (parentNode != null) {
                    if (foundNode.data.compareTo(parentNode.leftNode.data) == 0) {
                        parentNode.setLeftNode(temp);
                    } else if (foundNode.data.compareTo(parentNode.rightNode.data) == 0) {
                        parentNode.setRightNode(temp);
                    }
                }
                foundNode = temp;
                foundNode.setLeftNode(foundNodeLeft);
                foundNode.setRightNode(foundNodeRight);
            } else {
                // one child - promote child
                ASTBinarySearchTreeNode<T> child = (null == foundNode.rightNode) ? foundNode.leftNode : foundNode.rightNode;
                if (parentNode != null) {
                    if (foundNode.data.compareTo(parentNode.leftNode.data) == 0) {
                        parentNode.setLeftNode(child);
                    } else if (foundNode.data.compareTo(parentNode.rightNode.data) == 0) {
                        parentNode.setRightNode(child);
                    }
                }
            }
            this.count--;

            if(null != treeToBalance) {
                treeToBalance.balance();
            } else {
                if(null != this.rootNode) {
                    this.rootNode.balance();
                }
            }
        }
    }

    public ASTBinarySearchTreeNode<T> search(Object o) {
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

    public ASTBinarySearchTreeNode<T> search(T value) {
        return findNode(this.rootNode, new ASTBinarySearchTreeNode<T>(value, null, this));
    }

    private ASTBinarySearchTreeNode<T> findNode(ASTBinarySearchTreeNode<T> parentNode,
            ASTBinarySearchTreeNode<T> nodeToFind) {
        if (parentNode == null) {
            return null;
        } else {
            if (nodeToFind.data.compareTo(parentNode.data) == 0) {
                return parentNode;
            } else if (nodeToFind.data.compareTo(parentNode.data) < 0) {
                return findNode(parentNode.leftNode, nodeToFind);
            } else {
                return findNode(parentNode.rightNode, nodeToFind);
            }
        }
    }

    private void InPlaceTraversal(List<T> list, ASTBinarySearchTreeNode<T> node) {
        if (null != node) {
            // contactlist.App.print(MessageFormat.format("Traversing {0}",
            // node.data.toString()));
            InPlaceTraversal(list, node.leftNode);
            list.add(node.data);
            // contactlist.App.print(MessageFormat.format("Added {0} to list",
            // node.data.toString()));
            InPlaceTraversal(list, node.rightNode);
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
        ASTBinarySearchTreeNode<T> node = search(o);
        return null != node;
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
        InPlaceTraversal(sortedList, this.rootNode);
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
        InPlaceTraversal(list, this.rootNode);
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
        InPlaceTraversal(list, this.rootNode);
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
        sb.append(new TreePrettyPrinter<T>().traversePreOrder(this.rootNode));
        sb.append("\n\nLeft Height: "+this.rootNode.getLeftHeight());
        sb.append("\tRight Height: "+this.rootNode.getRightHeight());
        return sb.toString();
    }
}
