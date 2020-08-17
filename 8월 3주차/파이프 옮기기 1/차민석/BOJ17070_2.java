package day0817;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

// DFS 활용
public class BOJ17070_2 {

	static int N, ans;
	static boolean[][] map;
	static int[] di = { 0, 1, 1 }; // 가로, 세로, 대각선
	static int[] dj = { 1, 0, 1 };

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		N = Integer.parseInt(br.readLine());
		map = new boolean[N][N];
		ans = 0;

		for (int i = 0; i < N; i++) {
			String[] s = br.readLine().split(" ");
			for (int j = 0; j < N; j++) {
				map[i][j] = s[j].charAt(0) == '1';
			}
		}

		dfs(0, 1, 0);

		System.out.println(ans);

	}

	static void dfs(int x, int y, int shape) {
		if (x == N - 1 && y == N - 1) {
			ans++;
			return;
		}

		// 0: 가로, 1: 세로, 2: 대각
		boolean isDiagonal = true; // 대각 되는지 여부확인
		for (int d = 0; d < 3; d++) {
			boolean isMove = false;

			int ni = x + di[d];
			int nj = y + dj[d];

			if (ni >= N || nj >= N || map[ni][nj]) {
				isDiagonal = false; // 대각이 되는지 체크
			} else if (d == 0) {
				if (shape != 1) {
					isMove = true;
				}
			} else if (d == 1) {
				if (shape != 0) {
					isMove = true;
				}
			} else if (d == 2 && isDiagonal) {
				isMove = true;
			}

			if (isMove) {
				dfs(ni, nj, d);
			}
		}
	}
}
