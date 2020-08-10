package com.smallaswater.levelawakensystem.data.math;

import java.util.Collections;
import java.util.Stack;

public class Calculator {

    /**后缀式栈*/
    private Stack<String> postfixStack = new Stack<>();
    /** 运算符栈*/
    private Stack<Character> opStack = new Stack<>();
    /** 运用运算符ASCII码-40做索引的运算符优先级*/
    private int[] operatPriority = new int[] { 0, 3, 2, 1, -1, 1, 0, 2 };

    public static double conversion(String expression) {
        double result = 0;
        Calculator cal = new Calculator();
        try {
            expression = transform(expression);
            result = cal.calculate(expression);
        } catch (Exception e) {
            e.printStackTrace();
            // 运算错误返回NaN
            return Double.NaN;
        }

        return result;
    }

    /**
     * 计算百分数或者小数
     * */
    public static double mathRound(double number,String math){
        double n;
        if(math.matches("^([0-9.]+)[ ]*%$")){
            double a1 = Double.parseDouble(math.split("%")[0]);
            a1 /= 100;
            if(a1 > 0){
                n = number + number * a1;
            }else{
                n = number;
            }
        }else{
            n = Double.parseDouble(math) + number;
        }
        return n;
    }



    /**
     * 将表达式中负数的符号更改
     *
     * @param expression
     *            例如-2+-1*(-3E-2)-(-1) 被转为 ~2+~1*(~3E~2)-(~1)
     * @return 修复
     */
    private static String transform(String expression) {
        char[] arr = expression.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '-') {
                if (i == 0) {
                    arr[i] = '~';
                } else {
                    char c = arr[i - 1];
                    if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == 'E' || c == 'e') {
                        arr[i] = '~';
                    }
                }
            }
        }
        if(arr[0]=='~'||arr[1]=='('){
            arr[0]='-';
            return "0"+new String(arr);
        }else{
            return new String(arr);
        }
    }

    /**
     * 按照给定的表达式计算
     *
     * @param expression
     *            要计算的表达式例如:5+12*(3+5)/7
     * @return 数值
     */
    private double calculate(String expression) {
        Stack<String> resultStack = new Stack<String>();
        prepare(expression);
        // 将后缀式栈反转
        Collections.reverse(postfixStack);
        // 参与计算的第一个值，第二个值和算术运算符
        String firstValue, secondValue, currentValue;
        while (!postfixStack.isEmpty()) {
            currentValue = postfixStack.pop();
            // 如果不是运算符则存入操作数栈中
            if (!isOperator(currentValue.charAt(0))) {
                currentValue = currentValue.replace("~", "-");
                resultStack.push(currentValue);
            } else {
                // 如果是运算符则从操作数栈中取两个值和该数值一起参与运算
                secondValue = resultStack.pop();
                firstValue = resultStack.pop();

                // 将负数标记符改为负号
                firstValue = firstValue.replace("~", "-");
                secondValue = secondValue.replace("~", "-");

                String tempResult = calculate(firstValue, secondValue, currentValue.charAt(0));
                resultStack.push(tempResult);
            }
        }
        return Double.valueOf(resultStack.pop());
    }

    /**
     * 数据准备阶段将表达式转换成为后缀式栈
     *
     * @param expression 运算符
     */
    private void prepare(String expression) {
        // 运算符放入栈底元素逗号，此符号优先级最低
        opStack.push(',');
        char[] arr = expression.toCharArray();
        // 当前字符的位置
        int currentIndex = 0;
        // 上次算术运算符到本次算术运算符的字符的长度便于或者之间的数值
        int count = 0;
        // 当前操作符和栈顶操作符
        char currentOp, peekOp;
        for (int i = 0; i < arr.length; i++) {
            currentOp = arr[i];
            if (isOperator(currentOp)) {
                // 如果当前字符是运算符
                if (count > 0) {
                    // 取两个运算符之间的数字
                    postfixStack.push(new String(arr, currentIndex, count));
                }
                peekOp = opStack.peek();
                if (currentOp == ')') {
                    // 遇到反括号则将运算符栈中的元素移除到后缀式栈中直到遇到左括号
                    while (opStack.peek() != '(') {
                        postfixStack.push(String.valueOf(opStack.pop()));
                    }
                    opStack.pop();
                } else {
                    while (currentOp != '(' && peekOp != ',' && compare(currentOp, peekOp)) {
                        postfixStack.push(String.valueOf(opStack.pop()));
                        peekOp = opStack.peek();
                    }
                    opStack.push(currentOp);
                }
                count = 0;
                currentIndex = i + 1;
            } else {
                count++;
            }
        }
        if (count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {
            // 最后一个字符不是括号或者其他运算符的则加入后缀式栈中
            postfixStack.push(new String(arr, currentIndex, count));
        }

        while (opStack.peek() != ',') {
            // 将操作符栈中的剩余的元素添加到后缀式栈中
            postfixStack.push(String.valueOf(opStack.pop()));

        }
    }

    /**
     * 判断是否为算术符号
     *
     * @param c 字符
     * @return 是否为算数符号
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
    }

    /**
     * 利用ASCII码-40做下标去算术符号优先级
     *
     * @param cur 优先级
     * @param peek peek
     * @return 优先级判断
     */
    private boolean compare(char cur, char peek) {// 如果是peek优先级高于cur，返回true，默认都是peek优先级要低
        boolean result = false;
        if (operatPriority[(peek) - 40] >= operatPriority[(cur) - 40]) {
            result = true;
        }
        return result;
    }

    /**
     * 按照给定的算术运算符做计算
     *
     * @param firstValue 前面的数值
     * @param secondValue 后面的数值
     * @param currentOp 符号
     * @return 计算结果
     */
    private String calculate(String firstValue, String secondValue, char currentOp) {
        String result = "";
        switch (currentOp) {
            case '+':
                result = String.valueOf(ArithHelper.add(firstValue, secondValue));
                break;
            case '-':
                result = String.valueOf(ArithHelper.sub(firstValue, secondValue));
                break;
            case '*':
                result = String.valueOf(ArithHelper.mul(firstValue, secondValue));
                break;
            case '/':
                result = String.valueOf(ArithHelper.div(firstValue, secondValue));
                break;
            default:break;
        }
        return result;
    }
}
