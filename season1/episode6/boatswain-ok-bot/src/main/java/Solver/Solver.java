package Solver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * Класс парсит и считает выражение
 */
public class Solver {
    private static final char LEFT_PAREN = '(';
    private static final char RIGHT_PAREN = ')';
    private static final char PLUS = '+';
    private static final char MINUS = '-';
    private static final char TIMES = '*';
    private static final char DIVISION = '/';

    /**
     * Проверяет, является ли с оператором
     * @param c
     * @return true если c оператор, false в противном случае
     */
    private boolean isOperat(String c) {
        return !checkString(c) && (c.charAt(0) == PLUS || c.charAt(0) == MINUS || c.charAt(0) == DIVISION || c.charAt(0) == TIMES);
    }

    /**
     * Проверяет, является ли c оператором или скобками
     * @param c
     * @return true если c оператор или скобка, false в противном случае
     */
    private boolean isOperatOrParen(char c) {
        return (c == PLUS || c == MINUS || c == DIVISION || c == TIMES || c == LEFT_PAREN || c == RIGHT_PAREN);
    }

    /**
     * Проверяет, является ли ch цифрой
     * @param ch
     * @return true если ch цифра, false в противном случае
     */
    private boolean isNumber(char ch) {
        return ch >= '0' && ch <= '9';
    }

    /**
     * Проверяет, можно ли  преобразовать str в число типа double
     * @param str
     * @return true если можно преобразовать, false иначе
     */
    private boolean checkString(String str) {
        try {
            Double.parseDouble(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Метод вычисляет значение переданного арифметического выражения, испольуя алгорим Дейкстры (Сортировочная станция)
     * @param s Выражение, которое нужно вычислить. Каждый элемент s содержит отдельный токен выражения (число, знак арифметической операции или скобку)
     * @return
     * @throws ParseExceprion
     * @throws ArithmeticException
     */
    private double build(ArrayList<String> s) throws ParseExceprion, ArithmeticException {
        ArrayList<String> queue = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        for (String value : s) {
            if (checkString(value)) {
                queue.add(value);
            } else if (isOperat(value)) {
                while (!stack.isEmpty() && isOperat(stack.peek()) && (prior(stack.peek()) >= prior(value))) {
                    queue.add(stack.pop());
                }
                stack.push(value);
            } else if (value.charAt(0) == LEFT_PAREN) {
                stack.push("(");
            } else if (value.charAt(0) == RIGHT_PAREN) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    queue.add(stack.pop());
                }
                if (stack.isEmpty()) {
                    throw new ParseExceprion("parse error");
                }
                stack.pop();
            }

        }
        while (!stack.isEmpty()) {
            queue.add(stack.pop());
        }
        stack.clear();
        for (String aQueue : queue) {
            if (checkString(aQueue)) {
                stack.push(aQueue);
            } else {
                try {
                    Double b = Double.valueOf(stack.pop());
                    Double a = Double.valueOf(stack.pop());
                    stack.push(calcOper(aQueue, a, b).toString());
                } catch (NoSuchElementException e) {
                    throw new ParseExceprion("parse error");
                }

            }
        }
        return Double.valueOf(stack.pop());
    }

    /**
     * Возвращает приоритет переданной операции
     * @param s
     * @return
     */
    private int prior(String s) {
        switch (s.charAt(0)) {
        case PLUS:
        case MINUS:
            return 1;
        case TIMES:
        case DIVISION:
            return 2;
        default:
            return 0;
        }
    }

    /**
     * возвращает результат операции c значений l и r
     * @param c операция
     * @param l левый операнд
     * @param r правый операнд
     * @return результат операции
     * @throws ArithmeticException
     */
    private Double calcOper(String c, Double l, Double r) throws ArithmeticException {
        switch (c.charAt(0)) {
        case PLUS:
            return l + r;
        case MINUS:
            return l - r;
        case TIMES:
            return l * r;
        case DIVISION:
            return l / r;
        default:
            return 0.0;
        }
    }

    /**
     * Вычисляет значение арифметического выражения
     * @param str арифметическое выражение
     * @return результат арифметичческого выражения
     * @throws ParseExceprion
     * @throws ArithmeticException
     */
    public double evaluate(String str) throws ParseExceprion, ArithmeticException {
        ArrayList<String> values = new ArrayList<>();
        boolean inNumber = false;
        StringBuilder curr = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == ' ') {
                continue;
            }
            if (isOperatOrParen(c)) {
                if (inNumber) {
                    if (!checkString(curr.toString())) {
                        throw new ParseExceprion("parse error");
                    }
                    values.add(curr.toString());
                    inNumber = false;
                    curr.setLength(0);
                }
                values.add("" + c);
            } else if (isNumber(c) || c == '.') {
                inNumber = true;
                curr.append(c);
            } else {
                throw new ParseExceprion("parse error");
            }

        }
        if (inNumber) {
            values.add(curr.toString());
        }
        return build(values);
    }


}
