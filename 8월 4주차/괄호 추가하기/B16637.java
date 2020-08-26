import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/*
================================================
(1) 수식 : 3
연산자 인덱스 : 0
최다 괄호 개수 : 3 -> 0개
================================================
(3) 수식 : 3+8
연산자 인덱스 : 1
최다 괄호 개수 : (3+8) -> 1개
================================================
(5) 수식 : 3+8*7
연산자 인덱스 : 1, 3
최다 괄호 개수 : (3+8)*7 -> 1개
================================================
(7) 수식 : 3+8*7-9
연산자 인덱스 : 1, 3, 5
최다 괄호 개수 : (3+8)*(7-9) -> 2개
================================================
(9) 수식 : 3+8*7-9*2
연산자 인덱스 : 1, 3, 5, 7
최다 괄호 개수 : (3+8)*(7-9)*2 -> 2개
================================================
(11) 수식 : 3+8*7-9*2+2
연산자 인덱스 : 1, 3, 5, 7, 9
최다 괄호 개수 : (3+8)*(7-9)*(2+2) -> 3개
================================================
(13) 수식 : 3+8*7-9*2+2*1
연산자 인덱스 : 1, 3, 5, 7, 9, 11
최다 괄호 개수 : (3+8)*(7-9)*(2+2)*1 -> 3개
================================================
(15) 수식 : 3+8*7-9*2+2*1-5
연산자 인덱스 : 1, 3, 5, 7, 9, 11, 13
최다 괄호 개수 : (3+8)*(7-9)*(2+2)*(1-5) -> 4개
================================================
(17) 수식 : 3+8*7-9*2+2*1-5+6
연산자 인덱스 : 1, 3, 5, 7, 9, 11, 13, 15
최다 괄호 개수 : (3+8)*(7-9)*(2+2)*(1-5)+6 -> 4개
================================================
(19) 수식 : 3+8*7-9*2+2*1-5+6*8
연산자 인덱스 : 1, 3, 5, 7, 9, 11, 13, 15, 17
최다 괄호 개수 : (3+8)*(7-9)*(2+2)*(1-5)+(6*8) -> 5개
================================================
 */

public class B16637 {

    static String exp;
    static int N, R, MAX;
    static int[] num;
    static int[] result;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        N = Integer.parseInt(br.readLine().trim());
        exp = br.readLine().trim();

        num = new int[N / 2];
        int index = 0;
        for (int i = 0; i < N; i++) {
            char e = exp.charAt(i);
            if (e == '+' || e == '*' || e == '-') {
                num[index++] = i;
            }
        }

        // 최대로 생성할 수 있는 괄호의 개수 => 연산자의 개수(index)가 짝수 -> index / 2, 홀수 -> (index + 1) / 2
        index = index % 2 == 0 ? index / 2 : (index + 1) / 2;

        MAX = calc(exp);
        // 생성할 수 있는 괄호의 개수가 1개 ~ 최대까지 모든 경우의 수를 생성
        for (int i = 1; i <= index; i++) {
            R = i;
            result = new int[R];
            backtrack(0, 0);
        }

        bw.write(String.valueOf(MAX));
        bw.flush();
    }

    // nCr 조합 생성
    private static void backtrack(int idx, int cnt) {
        if (cnt == R) {

            // 중복된 괄호가 생성 될 경우 return
            for (int i = 0; i < result.length - 1; i++) {
                if (result[i] + 2 == result[i + 1]) return;
            }

            // 괄호가 있는 수식 생성
            List<Character> lst = new ArrayList<>();
            for (char ch : exp.toCharArray()) lst.add(ch);
            createExpression(lst);

            // 수식 계산
            StringBuilder sb = new StringBuilder();
            for (char ch : lst) sb.append(ch);
            String str = sb.toString().trim();
            MAX = Math.max(MAX, calc(str));

            return;
        }

        for (int i = idx; i < num.length; i++) {
            result[cnt] = num[i];
            backtrack(i + 1, cnt + 1);
        }
    }

    // 괄호가 있는 수식 생성
    private static void createExpression(List<Character> lst) {
        int add = 0;
        int index;
        /*
        1+2-3+5 이라는 수식이 있다면 1, 3, 5 번 째에 연산자가 존재한다.
        첫 번째 연산자 (1)를 기준으로 (1+2)-3+5 라는 수식을 생성하면
        5번째 연산자를 기준으로도 괄호를 생성할 수 있는데, 인덱스가 5번이 아니라 7이 되므로
        2개 이상의 괄호를 생성하려면 +2 씩 늘려주면서 괄호를 추가한다.
         */
        for (int num : result) {
            index = Math.max(num - 1, 0);
            if (result.length != 1) index += add;
            lst.add(index, '(');
            index = num + 3;
            if (result.length != 1) index += add;
            lst.add(index, ')');
            add += 2;
        }
    }

    // 스택 계산기, 중위 표현식 -> 후위 표현식 변경 후 계산, 연산자 별 우선순위 없게 동작하도록 처리
    private static int calc(String str) {
        // System.out.print(str + " => ");
        StringBuilder temp = new StringBuilder();
        Stack<Character> op = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '+') {
                while (!op.isEmpty()) {
                    int c = op.peek();
                    if (c == '(') break;
                    temp.append(op.pop());
                }
                op.push(ch);
            } else if (ch == '-') {
                while (!op.isEmpty()) {
                    int c = op.peek();
                    if (c == '(') break;
                    temp.append(op.pop());
                }
                op.push(ch);
            } else if (ch == '*') {
                while (!op.isEmpty()) {
                    int c = op.peek();
                    if (c == '(') break;
                    temp.append(op.pop());
                }
                op.push(ch);
            } else if (ch == '(') {
                op.push(ch);
            } else if (ch == ')') {
                while (!op.isEmpty()) {
                    char c = op.pop();
                    if (c != '(') {
                        temp.append(c);
                    }
                    if (c == '(') break;
                }
            } else {
                temp.append(ch);
            }
        }

        while (!op.isEmpty()) {
            temp.append(op.pop());
        }

        str = temp.toString().trim();
        // System.out.println(str);
        Stack<Integer> num = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '*' || ch == '+' || ch == '-') {
                int n1 = num.pop();
                int n2 = num.pop();
                if (ch == '*') {
                    num.push(n1 * n2);
                } else if (ch == '-') {
                    num.push(n2 - n1);
                } else {
                    num.push(n1 + n2);
                }
            } else {
                num.push(ch - '0');
            }
        }

        int ret = num.pop();
        // System.out.println(ret);
        return ret;
    }

}
