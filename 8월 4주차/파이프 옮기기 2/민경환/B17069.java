import java.io.*;
import java.util.StringTokenizer;

public class B17069 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int N = Integer.parseInt(br.readLine().trim());
        int[][] map = new int[N + 1][N + 1];
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine().trim());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        long[][][] dp = new long[N + 1][N + 1][3];
        /*
        map과 dp의 크기를 N개 만큼 설정하기 위하여 N으로 초기화하였더니, IndexOutOfRangeException 처리를 계속 해줘야 해서
        좌표의 크기가 +1 만큼만 증가하기 때문에 그냥 배열의 크기를 한 칸 더 늘려서 처리하였다.
        0 -> 가로, 1 -> 세로, 2 -> 대각
        dp[x][y][z] : x, y 좌표에 z 방향일 때 올 수 있는 경우의 수
        ex) dp[1][3][0] => (1, 3) 좌표에 가로방향으로 놓을 수 있는 경우의 수는 (1, 2) 좌표일 때 가로(0)방향 + (1, 2) 좌표일 때 대각(2)방향
        dp[x][y + 1][0]     : dp[x][y][0] + dp[x][y][2];
        dp[x + 1][y][1]     : dp[x][y][1] + dp[x][y][2];
        dp[x + 1][y + 1][2] : dp[x][y][0] + dp[x][y][1] + dp[x][y][2];
         */
        dp[0][1][0] = 1;
        for (int i = 0; i < N; i++) {
            for (int j = 1; j < N; j++) {
                if (map[i][j + 1] == 0) dp[i][j + 1][0] = dp[i][j][0] + dp[i][j][2];
                if (map[i + 1][j] == 0) dp[i + 1][j][1] = dp[i][j][1] + dp[i][j][2];
                if (map[i + 1][j] == 0 && map[i][j + 1] == 0 && map[i + 1][j + 1] == 0) {
                    dp[i + 1][j + 1][2] = dp[i][j][0] + dp[i][j][1] + dp[i][j][2];
                }
            }
        }

        long ret = dp[N - 1][N - 1][0] + dp[N - 1][N - 1][1] + dp[N - 1][N - 1][2];
        bw.write(String.valueOf(ret));
        bw.flush();
    }

}
