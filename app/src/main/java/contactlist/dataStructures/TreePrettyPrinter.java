package contactlist.dataStructures;

public class TreePrettyPrinter<T extends Comparable<T>> {

    private static final String POINTER_LEFT_BOTH = "|=L==";
    private static final String POINTER_LEFT = "|-L--";
    private static final String POINTER_RIGHT = "|_R__";
    private static final String EMPTY_STRING = "";

    public String traversePreOrder(ITreeNode<T> root) {
        if (root == null) {
            return EMPTY_STRING;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(root.getData());

        String pointerRight = POINTER_RIGHT;
        String pointerLeft = (root.getRightNode() != null) ? POINTER_LEFT_BOTH : POINTER_RIGHT;

        traverseNodes(sb, EMPTY_STRING, pointerRight, root.getRightNode(), false);
        traverseNodes(sb, EMPTY_STRING, pointerLeft, root.getLeftNode(), root.getRightNode() != null);

        return sb.toString();
    }

    public void traverseNodes(StringBuilder sb, String padding, String pointer, 
            ITreeNode<T> node, boolean hasRightSibling) {
        if (node != null) {
            sb.append("\n");
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.getData());

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (hasRightSibling) {
                paddingBuilder.append("   ");
            } else {
                paddingBuilder.append("|  ");
            }

            String paddingForBoth = paddingBuilder.toString();
            String pointerRight = POINTER_RIGHT;
            String pointerLeft = (node.getRightNode() != null) ? POINTER_LEFT_BOTH : POINTER_LEFT;

            traverseNodes(sb, paddingForBoth, pointerRight, node.getRightNode(), false);
            traverseNodes(sb, paddingForBoth, pointerLeft, node.getLeftNode(), node.getRightNode() != null);
        }
    }
}
