import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class B17136 {

    static int N;
    static int[][] map;
    static int[][] copyMap;
    static int[] cnt = {0, 5, 5, 5, 5, 5};
    static int result = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        N = 10;
        map = new int[N][N];
        copyMap = new int[N][N];
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine().trim());
            for (int j = 0; j < N; j++) {
                int value = Integer.parseInt(st.nextToken());
                map[i][j] = value;
                copyMap[i][j] = value;
            }
        }

        List<Integer> minList = new ArrayList<>();
        for (int i = 5; i >= 1; i--) {
            for (int j = i; j >= 1; j--) {
                check(j);
            }
            minList.add(result);
            init();
        }

        System.out.println(minList);

        result = Integer.MAX_VALUE;
        if (minList.get(0) == -1 && minList.get(1) == -1 && minList.get(2) == -1 && minList.get(3) == -1 && minList.get(4) == -1) {
            result = -1;
        } else {
            for (int min : minList) {
                if (min == -1) continue;
                result = Math.min(min, result);
            }
        }

        bw.write(String.valueOf(result));
        bw.flush();
    }

    // 결과값, 색종이 개수, 맵 초기화
    private static void init() {
        result = 0;
        cnt = new int[]{0, 5, 5, 5, 5, 5};
        for (int i = 0; i < N; i++) {
            System.arraycopy(copyMap[i], 0, map[i], 0, N);
        }
    }

    private static void check(int M) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                // 색종이를 붙일 수 있는 구간이 발견
                if (map[i][j] == 1) {
                    int ni = i + (M - 1);
                    int nj = j + (M - 1);
                    // 범위 검사
                    if (ni >= N || nj >= N || map[ni][nj] == 0 || !valid(i, j, ni, nj)) continue;
                    // 색종이를 붙인다.
                    boolean overCnt = attach(i, j, ni, nj, M);
                    if (overCnt) {
                        result = -1;
                        return;
                    }
                    j = nj;
                }
                // 여기서 다시 맵을 초기화 시키고
            }
        }
    }

    private static boolean attach(int ci, int cj, int ni, int nj, int M) {
        for (int i = ci; i <= ni; i++) {
            for (int j = cj; j <= nj; j++) {
                map[i][j] = 0;
            }
        }
        cnt[M]--;
        result += 1;
        return cnt[M] < 0;
    }

    // 색종이가 전부 덮였는지 검사
    private static boolean valid(int ci, int cj, int ni, int nj) {
        for (int i = ci; i <= ni; i++) {
            for (int j = cj; j <= nj; j++) {
                if (map[i][j] == 0) return false;
            }
        }
        return true;
    }

}
