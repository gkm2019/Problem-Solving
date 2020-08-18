import java.io.*;
import java.util.StringTokenizer;

public class B17070_dfs {

    static int N, cnt = 0;
    static int[][] map;
    static int[] di = {0, 1, 1};
    static int[] dj = {1, 0, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        N = Integer.parseInt(br.readLine().trim());
        map = new int[N][N];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine().trim());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        dfs(0, 1, 0);
        bw.write(String.valueOf(cnt));
        bw.flush();
    }

    private static void dfs(int i, int j, int before) {
        if (i == N - 1 && j == N - 1) {
            cnt++;
            return;
        }

        for (int d = 0; d < 3; d++) {
            if ((d == 0 && before == 1) || (d == 1 && before == 0)) continue;
            int ni = i + di[d];
            int nj = j + dj[d];
            if (ni >= N || nj >= N || map[ni][nj] == 1) continue;
            if (d == 2 && (map[ni - 1][nj - 1] == 1 || map[ni - 1][nj] == 1 || map[ni][nj - 1] == 1)) continue;
            dfs(ni, nj, d);
        }
    }

}
