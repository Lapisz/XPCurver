package net.lapisz.xpcurver.util;

public class ExpressionTree {
    public int isOp; // 0 = a number; 1 = operator; 2 = the variable "level"
    public String value;
    public ExpressionTree left;
    public ExpressionTree right;

    public ExpressionTree(String value) {
        this(0, value, null, null);
    }

    public ExpressionTree(int isOp, String value) {
        this(isOp, value, null, null);
    }

    public ExpressionTree(int isOp, String value, ExpressionTree left, ExpressionTree right) {
        this.isOp = isOp;
        this.value = value;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return this.isOp == 1
                ? "{" + this.value + ", [" + this.left.toString() + ", " + this.right.toString() + "]}"
                : this.value;
    }

    public void print() {
        print_recurse(0);
    }

    // For tab spacing
    private void print_recurse(int num_recursions) {
        if (this.isOp == 1) {
            for (int i = 0; i < num_recursions; i++) {
                System.out.print("\t");
            }
            System.out.println("{ " + this.value + ",");

            this.left.print_recurse(num_recursions + 1);
            this.right.print_recurse(num_recursions + 1);

            for (int i = 0; i < num_recursions; i++) {
                System.out.print("\t");
            }
            System.out.println("}");
        } else {
            for (int i = 0; i < num_recursions; i++) {
                System.out.print("\t");
            }
            System.out.println("{" + this.value + "}");
        }
    }

}
