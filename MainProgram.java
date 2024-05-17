import java.util.*;
public class MainProgram {
    class ExpressionTree {

        // Tree Structure
        static class Node {
            String data;
            Node left, right;
    
            Node(String d) {
                data = d;
                left = right = null;
            }
        }
    
        // Function to create a new node
        static Node newNode(String data) {
            return new Node(data);
        }
    
        // Function to build Expression Tree
        static Node build(String s) {
            Stack<Node> stN = new Stack<>(); // Stack to hold nodes
            Stack<Character> stC = new Stack<>(); // Stack to hold chars
            Node t, t1, t2;
    
            // Prioritizing the operators
            int[] p = new int[123];
            p['+'] = p['-'] = 1;
            p['/'] = p['*'] = 2;
            p['^'] = 3;
            p['('] = 0;
    
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                if (ch == '(') {
                    stC.push(ch); // Push '(' in char stack
                } else if (Character.isDigit(ch)) {
                    StringBuilder num = new StringBuilder();
                    while (i < s.length() && Character.isDigit(s.charAt(i))) {
                        num.append(s.charAt(i++));
                    }
                    i--;
                    t = newNode(num.toString()); // Push the operands in node stack
                    stN.push(t);
                } else if (p[ch] > 0) {
                    while (!stC.isEmpty() && stC.peek() != '(' &&
                            ((ch != '^' && p[stC.peek()] >= p[ch]) ||
                                    (ch == '^' && p[stC.peek()] > p[ch]))) {
                        t = newNode(String.valueOf(stC.pop()));
                        t1 = stN.pop();
                        t2 = stN.pop();
                        t.left = t2;
                        t.right = t1;
                        stN.push(t);
                    }
                    stC.push(ch); // Push current operator to char stack
                } else if (ch == ')') {
                    while (!stC.isEmpty() && stC.peek() != '(') {
                        t = newNode(String.valueOf(stC.pop()));
                        t1 = stN.pop();
                        t2 = stN.pop();
                        t.left = t2;
                        t.right = t1;
                        stN.push(t);
                    }
                    stC.pop(); // Pop '('
                }
            }
            return stN.peek();
        }
    
        // Function to print postfix (postorder) traversal of the tree
        static void printPostfix(Node root) {
            if (root != null) {
                printPostfix(root.left);
                printPostfix(root.right);
                System.out.print(root.data + " ");
            }
        }
    
        // Function to print prefix (preorder) traversal of the tree
        static void printPrefix(Node root) {
            if (root != null) {
                System.out.print(root.data + " ");
                printPrefix(root.left);
                printPrefix(root.right);
            }
        }
    
        // Function to evaluate the expression tree
        static int evalTree(Node root) {
            if (root == null) return 0;
    
            // Leaf node (operand)
            if (root.left == null && root.right == null) return Integer.parseInt(root.data);
    
            // Evaluate left subtree
            int leftEval = evalTree(root.left);
    
            // Evaluate right subtree
            int rightEval = evalTree(root.right);
    
            // Apply the operator
            switch (root.data) {
                case "+": return leftEval + rightEval;
                case "-": return leftEval - rightEval;
                case "*": return leftEval * rightEval;
                case "/": return leftEval / rightEval;
                case "^": return (int) Math.pow(leftEval, rightEval);
            }
            return 0;
        }
    
        // Function to print the tree in a hierarchical tree form
        static void printTree(Node root, String indent, boolean last, boolean isRoot) {
            if (root != null) {
                System.out.print(indent);
                if (!isRoot) {
                    if (last) {
                        System.out.print("R---- ");
                        indent += "   ";
                    } else {
                        System.out.print("L---- ");
                        indent += "|  ";
                    }
                }
                System.out.println(root.data);
                printTree(root.left, indent, false, false);
                printTree(root.right, indent, true, false);
            }
        }
    
        // Driver code
        public static void main(String[] args) {
            String s = "(5*(4+3)-(2/1))";
            s = "(" + s;
            s += ")";
            Node root = build(s);
    
            // Function calls
            System.out.print("Postfix: ");
            printPostfix(root);
            System.out.println();
    
            System.out.print("Prefix: ");
            printPrefix(root);
            System.out.println();
    
            System.out.print("Evaluation: ");
            System.out.println(evalTree(root));
    
            System.out.println("Expression Tree:");
            printTree(root, "", true, true);
        }
    }
    
}

