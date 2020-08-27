package BOJ;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Main_파이프옮기기1_dfs {
	static int n, d, ans;
	static int[] dx = { 0, 1, 1 }, dy = { 1, 1, 0 }; // 가로,대각,아래
	static int[][] map;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());

		map = new int[n][n];
		for (int i = 0; i < n; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		} // end input

		// solution
		dfs(0, 1, 0);
		System.out.println(ans);
	}

	static void dfs(int x, int y, int d) {
		if(x==n-1 && y==n-1) {
			ans++;
			return;
		}
		int nx, ny;
		for (int i = 0; i < 4; i++) {
			if (i == 3) { // 대각 선 이동 가능함!
				dfs(x + dx[1], y + dy[1], 1);
				break;
			}
			nx = x + dx[i];
			ny = y + dy[i];
			if (!Range(nx, ny) || map[nx][ny] != 0)
				break;
		}

		if (d == 1) { // 대각선은 3가지 가능해~ 2개 더 검사

			// 가로 가능?(0)
			nx = x + dx[0];
			ny = y + dy[0];
			if (Range(nx, ny) && map[nx][ny] == 0)
				dfs(nx, ny, 0);
			// 아래 가능?(2)
			nx = x + dx[2];
			ny = y + dy[2];
			if (Range(nx, ny) && map[nx][ny] == 0)
				dfs(nx, ny, 2);

		} else {// 나머지는 그냥 같은 방향 검사
			nx = x + dx[d];
			ny = y + dy[d];
			if (Range(nx, ny) && map[nx][ny] == 0)
				dfs(nx, ny, d);
		}
	}

	static boolean Range(int x, int y) {
		if (0 <= x && 0 <= y && x < n && y < n)
			return true;
		return false;
	}
}