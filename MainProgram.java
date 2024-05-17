import java.util.*;

public class MainProgram {
    static class ExpressionTree {
        // membuat tree
        static class Node {
            String data; 
            Node left, right;
            
            //constructor
            Node(String data) {
                this.data = data;
                left = right = null;
            }
        }

        // membuat node baru
        static Node NodeBaru(String data) {
            return new Node(data);
        }
        
        // membuat expression tree 
        static Node createTree(String infix) {
            Stack<Character> CharForTree = new Stack<>(); 
            Stack<Node> NodeForTree = new Stack<>(); 
            
             //untuk menampung node sementara saat operasi
            Node temp, tempLeft, tempRight;
            
            //urutan prioritas (0 paling rendah dan 3 paling tinggi)
            int[] priority = new int[123];
            priority['+'] = 1;
            priority['-'] = 1;
            priority['/'] = 2;
            priority['*'] = 2;
            priority['^'] = 3;
            priority['('] = 0;
    
            for (int i = 0; i < infix.length(); i++) {
                char character = infix.charAt(i);

                // '(' dimasukan langsung ke stack 
                if (character == '(') {
                    CharForTree.push(character);

                    //memasukkan operator ke stack
                } else if (Character.isDigit(character)) {
                    StringBuilder num = new StringBuilder();
                    while (i < infix.length() && Character.isDigit(infix.charAt(i))) {
                        num.append(infix.charAt(i++));
                    }
                    i--;
                    temp = NodeBaru(num.toString());
                    NodeForTree.push(temp);

                    // tidak kosong && paling atas != '(' 
                    // ch bukan ^ apakah paling atas lbh tinggi prioritynya
                    // ch adalah ^ cek paling atas lbh tinggi atau tidak
                } else if (priority[character] > 0) {
                    while (!CharForTree.isEmpty() && CharForTree.peek() != '(' &&
                            ((character != '^' && priority[CharForTree.peek()] >= priority[character]) ||
                                    (character == '^' && priority[CharForTree.peek()] > priority[character]))) {
                        temp = NodeBaru(String.valueOf(CharForTree.pop()));
                        tempRight = NodeForTree.pop();
                        tempLeft = NodeForTree.pop();
                        temp.left = tempLeft;
                        temp.right = tempRight;
                        NodeForTree.push(temp);
                    }
                    CharForTree.push(character);
                } else if (character == ')') {
                    while (!CharForTree.isEmpty() && CharForTree.peek() != '(') {
                        temp = NodeBaru(String.valueOf(CharForTree.pop()));
                        tempRight = NodeForTree.pop();
                        tempLeft = NodeForTree.pop();
                        temp.left = tempLeft;
                        temp.right = tempRight;
                        NodeForTree.push(temp);
                    }
                    CharForTree.pop();
                }
            }
            return NodeForTree.peek();
        }
        
        // ubah infix menjadi postfix (postorder)
        static void convertPostfix(Node root) {
            if (root != null) {
                convertPostfix(root.left);
                convertPostfix(root.right);
                System.out.print(root.data + " ");
            }
        }
        
        // ubah infix menjadi prefix (preorder)
        static void convertPrefix(Node root) {
            if (root != null) {
                System.out.print(root.data + " ");
                convertPrefix(root.left);
                convertPrefix(root.right);
            }
        }
        
        // evaluasi expression tree
        static int hasilEval(Node root) {
            if (root == null) {
                return 0;
            }
    
            if (root.left == null && root.right == null) {
                return Integer.parseInt(root.data);
            } 
    
            int sisiKiri = hasilEval(root.left);
            int sisiKanan = hasilEval(root.right);
    
            if (root.data.equals("+")) {
                return sisiKiri + sisiKanan;
            } else if (root.data.equals("-")) {
                return sisiKiri - sisiKanan;
            } else if (root.data.equals("*")) {
                return sisiKiri * sisiKanan;
            } else if (root.data.equals("/")) {
                return sisiKiri / sisiKanan;
            } else if (root.data.equals("^")) {
                return (int) Math.pow(sisiKiri, sisiKanan);
            }
            return 0;
        }
        
        //untuk membuat output tree
        static void printTree(Node root, String spasi, boolean last, boolean isRoot) {
            if (root != null) {
                System.out.print(spasi);
                if (!isRoot) {
                    if (last) {
                        System.out.print("R---- ");
                        spasi += "   ";
                    } else {
                        System.out.print("L---- ");
                        spasi += "|  ";
                    }
                }
                System.out.println(root.data);
                printTree(root.left, spasi, false, false);
                printTree(root.right, spasi, true, false);
            }
        }
    
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in); 
            System.out.println("Masukkan operasi Infix :");  
            String infix = scanner.next(); 
            scanner.close();

            Node root = createTree(infix);

            System.out.println("Hasil Expression Tree:");
            printTree(root, "", true, true); 
            
            System.out.print("Hasil Postfix: ");
            convertPostfix(root);
            System.out.println();
    
            System.out.print("Hasil Prefix: ");
            convertPrefix(root);
            System.out.println();
    
            System.out.print("Hasil Evaluation: ");
            System.out.println(hasilEval(root));
    
        }
    }
}

