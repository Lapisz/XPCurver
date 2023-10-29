package net.lapisz.xpcurver.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {
    /*
     * Error types:
     * 1 - Extra unprocessed characters at the end of string
     * 2 - Expected a numerical value or expression here
     *         (the list is empty or there is a dangling operator)
     * 3 - Expected a ")" here - unbalanced parentheses
     * 4 - Unexpected character - check your formatting
     */
    public static int err_pos = 0;
    public static int err_type = -1;

    /*
     * Filter out unwanted characters
     * Negative constants are not supported
     */
    public static ArrayList<String> lex(String input) {
        err_pos = 0; // Reset error counters
        err_type = -1;

        ArrayList<String> accepted = new ArrayList<>();

        String current_string = input;
        Pattern accepted_pattern = Pattern.compile("((\\d*\\.\\d+|\\d+\\.|\\d+)|[+\\-*/()^]|level)");
        Matcher matcher = accepted_pattern.matcher(current_string);

        while (matcher.find()) {
            accepted.add(current_string.substring(matcher.start(), matcher.end()));

            // Iterative part
            current_string = current_string.substring(matcher.end());
            matcher = accepted_pattern.matcher(current_string);
        }

        return accepted;
    }

    /*
     * Turn the list into an expression tree
     *
     * Context-free grammar:
     * A -> B + A | B - A | B
     * B -> C D | C * B | C / B | C
     * C -> D ^ C | D
     * D -> (A) | number | "level"
     */
    public static ExpressionTree parse(ArrayList<String> lst) throws Exception {
        // Throughout the recursions, lst serves as a list of the remaining items to be processed
        ExpressionTree tree = parse_add_sub(lst);

        if (lst.size() == 0) {
            return tree;
        } else {
            // Dangling characters at end = invalid mathematical expression
            err_type = 1;
            throw new Exception();
        }
    }

    private static ExpressionTree parse_add_sub(ArrayList<String> lst) throws Exception {
        ExpressionTree left_tree;
        ExpressionTree right_tree;

        left_tree = parse_mult_div(lst);
        if (lst.size() > 0) {
            String current_val = lst.get(0);
            if (current_val.equals("+") || current_val.equals("-")) {
                lst.remove(0);
                err_pos++;
                right_tree = parse_add_sub(lst);

                return new ExpressionTree(1, current_val, left_tree, right_tree);
            } else {
                return left_tree;
            }
        } else {
            return left_tree;
        }
    }

    private static ExpressionTree parse_mult_div(ArrayList<String> lst) throws Exception {
        ExpressionTree left_tree;
        ExpressionTree right_tree;

        left_tree = parse_exp(lst);

        // Process stuff after the first item we were given, if applicable
        if (lst.size() > 0) {
            String current_val = lst.get(0);
            if (current_val.equals("*") || current_val.equals("/")) {
                // Process * or / operator
                lst.remove(0);
                err_pos++;
                right_tree = parse_mult_div(lst);

                return new ExpressionTree(1, current_val, left_tree, right_tree);
            } else if (current_val.equals("(")) {
                // If the right is a paren expression, just process it and add to multiplication tree
                right_tree = parse_num_paren(lst);

                return new ExpressionTree(1, "*", left_tree, right_tree);
            } else {
                // Check if the right is a raw number (or the variable "level"),
                // and if so, add to multiplication tree
                Pattern num = Pattern.compile("(\\d*\\.\\d+|\\d+\\.|\\d+)");
                Matcher m = num.matcher(current_val);

                if (m.find()) {
                    // It is a number
                    lst.remove(0);
                    err_pos++;
                    return new ExpressionTree(1, "*", left_tree, new ExpressionTree(m.group()));
                } else {
                    if (current_val.equals("level")) {
                        lst.remove(0);
                        err_pos++;
                        return new ExpressionTree(1, "*", left_tree, new ExpressionTree(2, "level"));
                    } else {
                        // The value was not a number, the variable "level", (paren expression), *, or /
                        return left_tree;
                    }
                }
            }
        } else {
            return left_tree;
        }
    }

    private static ExpressionTree parse_exp(ArrayList<String> lst) throws Exception {
        ExpressionTree left_tree;
        ExpressionTree right_tree;

        left_tree = parse_num_paren(lst);
        if (lst.size() > 0 && lst.get(0).equals("^")) {
            lst.remove(0);
            err_pos++;
            right_tree = parse_exp(lst);
            return new ExpressionTree(1, "^", left_tree, right_tree);
        } else {
            return left_tree;
        }
    }

    private static ExpressionTree parse_num_paren(ArrayList<String> lst) throws Exception {
        ExpressionTree tree;
        String current_val;

        // Check for empty string
        // (which may happen when original parse is given an empty list,
        // or when there is a dangling/hanging operator, eg "5 + ")
        if (lst.size() > 0) {
            current_val = lst.get(0);
        } else {
            // Empty
            err_type = 2;
            throw new Exception();
        }

        // Check for parentheses
        if (current_val.equals("(")) {
            // Process expression inside
            lst.remove(0);
            err_pos++;
            tree = parse_add_sub(lst);

            if (lst.size() > 0 && lst.get(0).equals(")")) {
                lst.remove(0);
                err_pos++;
                return tree;
            } else {
                // Missing right parentheses / unbalanced
                err_type = 3;
                throw new Exception();
            }
        } else { // Check for number
            Pattern num = Pattern.compile("(\\d*\\.\\d+|\\d+\\.|\\d+)");
            Matcher m = num.matcher(current_val);

            if (m.find()) {
                // It is a number
                lst.remove(0);
                err_pos++;
                return new ExpressionTree(m.group());
            } else { // It was not a number
                // Now check if it is our variable "level"
                if (current_val.equals("level")) {
                    lst.remove(0);
                    err_pos++;
                    return new ExpressionTree(2, "level");
                } else {
                    // It was none of the above, must be an invalid character
                    // (eg the second "+" in "5 + +")
                    err_type = 4;
                    throw new Exception();
                }
            }
        }
    }

    public static double evaluate(ExpressionTree tree, int level) {
        int opType = tree.isOp;
        switch (opType) {
            case 0: // tree with just a number as its node
                return Double.parseDouble(tree.value);
            case 1: // tree with operator, and one or more subtrees
                String operator = tree.value;
                double l_eval = 0, r_eval = 0;
                if (tree.left != null) { l_eval = evaluate(tree.left, level); }
                if (tree.right != null) { r_eval = evaluate(tree.right, level); }
                switch (operator) {
                    case "+":
                        return l_eval + r_eval;
                    case "-":
                        return l_eval - r_eval;
                    case "*":
                        return l_eval * r_eval;
                    case "/":
                        return l_eval / r_eval;
                    case "^":
                        return Math.pow(l_eval, r_eval);
                    default:
                        return 0.0;
                }
            case 2: // tree with the variable "level" as its node
            default:
                return (double) level;
        }
    }

}
