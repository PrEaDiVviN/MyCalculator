package computerUtilities;

import java.util.ArrayList;
import java.util.Stack;

public class computer {
    private double precedenceOperator(char op) {
        if(op == '+' || op == '-')
            return 1;
        else if(op == ':' || op == '*')
            return 2;
        return 0;
    }

    private double applyOperator(double number1, double number2, char operator) {
        switch (operator) {
            case '+' : return number1 + number2;
            case '-' : return number1 - number2;
            case ':' : return number1 / number2;
            case '*' : return number1 * number2;
            case '^' : return Math.pow(number1,number2);
            case '%' : return number1 * number2 / 100d;
            default : return 0; ///modifica astfel incat sa arunce eroare
        }
    }

    private double clearRemainingOperations(Stack<Double> values, Stack<Character> operators) {
        //modifica astfel incat sa arunce erori
        while(!operators.empty()) {
            double value2 = values.pop();
            double value1 = values.pop();
            char operator = operators.pop();
            double result = applyOperator(value1, value2, operator);
            values.push(result);
        }
        return values.pop();
    }

    private boolean isDigit(char ch) {
        if(ch >= '0' && ch <= '9')
            return true;
        return false;
    }

    private int parseNumber(String expression, int i, Stack<Double> values, Stack<Character> operators, boolean positive) {
        //construct number
        double number = 0.0;
        boolean pointNumber = false;
        while(i < expression.length() && (isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
            if (isDigit(expression.charAt(i))) {
                if (!pointNumber)
                    number = number * 10.0d + Double.parseDouble(expression.substring(i, i + 1));
                else {
                    int space = 0;
                    double rest = 0.0;
                    while (isDigit(expression.charAt(i))) {
                        rest = rest + Double.parseDouble((expression.substring(i,i+1)));
                        space ++ ;
                        i++;
                    }
                    System.out.println(rest);
                    rest = rest / Math.pow(10,space);
                    number = number + rest;

                    i--;
                }
            } else if (expression.charAt(i) == '.')
                pointNumber = true;
            i++;
        }
        i--;
        if(!positive){
            number = number * (-1.0);
            i++;
        }
        values.push(number);
        return i;
    }

    public double evaluateExpression(StringBuilder expression) {
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();


        for (int i = 0; i < expression.length(); i++) {

            if(expression.charAt(i) == '(') {
                if(expression.charAt(i+1) != '-')
                    operators.push('(');
                else
                    i = parseNumber(expression.toString(), i + 2, values, operators, false);
            }
            else if(isDigit(expression.charAt(i))) {
                i = parseNumber(expression.toString(), i, values, operators, true);
            }
            else if(expression.charAt(i) == ')') {
                while (!operators.empty() && operators.lastElement() != '(') {
                    double value2 = values.pop();
                    double value1 = values.pop();
                    char operator = operators.pop();
                    values.push(applyOperator(value1, value2, operator));
                }
                operators.pop();
            }
            else {
                while(!operators.empty() && precedenceOperator(operators.lastElement()) >= precedenceOperator(expression.charAt(i))) {
                    double value2 = values.pop();
                    double value1 = values.pop();
                    char operator = operators.pop();
                    values.push(applyOperator(value1, value2, operator));
                }
                operators.push(expression.charAt(i));
            }
        }
        return clearRemainingOperations(values,operators);
    }
}
