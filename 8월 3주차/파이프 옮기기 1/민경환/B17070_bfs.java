import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class B17070_bfs {

    static class Point {
        int i, j, before;

        public Point(int i, int j, int before) {
            this.i = i;
            this.j = j;
            this.before = before;
        }

        @Override
        public String toString() {
            return "(" + i + ", " + j + ")(" + before + ")";
        }
    }
    static int N;
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

        bw.write(String.valueOf(bfs()));
        bw.flush();
    }

    // 가로 : 0, 세로 : 1, 대각 2
    private static int bfs() {
        Queue<Point> queue = new LinkedList<>();
        queue.offer(new Point(0, 1, 0));

        int cnt = 0;
        while (!queue.isEmpty()) {
            Point poll = queue.poll();
            int ci = poll.i;
            int cj = poll.j;
            int before = poll.before;

            if (ci == N - 1 && cj == N - 1) {
                cnt++;
            }

            for (int d = 0; d < 3; d++) {
                // 가로(0)인데 세로(1)인 경우, 세로(1)인데 가로(0)인 경우
                if ((d == 0 && before == 1) || (d == 1 && before == 0)) continue;
                int ni = ci + di[d];
                int nj = cj + dj[d];
                if (ni >= N || nj >= N || map[ni][nj] == 1) continue;
                if (d == 2 && (map[ni - 1][nj - 1] == 1 || map[ni - 1][nj] == 1 || map[ni][nj - 1] == 1)) continue;
                queue.offer(new Point(ni, nj, d));
            }
        }

        return cnt;
    }

}
