package contactlist.dataStructures;

public class TreePrettyPrinter<T extends Comparable<T>> {

    private static final String POINTER_RIGHT = "|__";
    private static final String POINTER_LEFT_BOTH = "|==";

    public String traversePreOrder(ASTBinarySearchTreeNode<T> root) {

        if (root == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(root.data);

        String pointerRight = POINTER_RIGHT;
        String pointerLeft = (root.rightNode != null) ? POINTER_LEFT_BOTH : POINTER_RIGHT;

        traverseNodes(sb, "", pointerLeft, root.leftNode, root.rightNode != null);
        traverseNodes(sb, "", pointerRight, root.rightNode, false);

        return sb.toString();
    }

    public void traverseNodes(StringBuilder sb, String padding, String pointer, ASTBinarySearchTreeNode<T> node,
            boolean hasRightSibling) {
        if (node != null) {
            sb.append("\n");
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.data);

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (hasRightSibling) {
                paddingBuilder.append("|  ");
            } else {
                paddingBuilder.append("   ");
            }

            String paddingForBoth = paddingBuilder.toString();
            String pointerRight = POINTER_RIGHT;
            String pointerLeft = (node.rightNode != null) ? POINTER_LEFT_BOTH : POINTER_RIGHT;

            traverseNodes(sb, paddingForBoth, pointerLeft, node.leftNode, node.rightNode != null);
            traverseNodes(sb, paddingForBoth, pointerRight, node.rightNode, false);
        }
    }
}
