package day0817;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

// BFS 이용, 백준 시간초과로 포기
public class BOJ17070 {

	static int N, ans;
	static boolean[][] map;
	static int[] di = { 0, 1, 1 }; // 가로, 세로, 대각선
	static int[] dj = { 1, 0, 1 };
	static Queue<Pipe> queue;

	static class Pipe {
		int i, j, shape; // 좌표, 모양
		// shape 가로 : 0, 세로 : 1, 대각선 : 2

		Pipe(int i, int j, int shape) {
			this.i = i;
			this.j = j;
			this.shape = shape;
		}
	}

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

		queue = new LinkedList<>();

		// 우, 하 만 가능하므로 갔던 길을 되돌아올 일은 없다.
		queue.add(new Pipe(0, 1, 0));

		while (!queue.isEmpty()) {
			Pipe now = queue.poll();

			if (now.i == N - 1 && now.j == N - 1) {
				ans++; // (N-1, N-1)에 도착할 때마다 경우의 수 +1
			}

			if (now.shape == 0 && now.j < N - 1) { // 가로의 경우 가로, 대각선
				movePipe(now, 0);
				movePipe(now, 2);
			} else if (now.shape == 1 && now.j < N - 1) { // 세로의 경우 세로, 대각선
				movePipe(now, 1);
				movePipe(now, 2);
			} else if (now.shape == 2 && now.i < N - 1 && now.j < N - 1) { // 대각선의 경우 가로, 세로, 대각선
				movePipe(now, 0);
				movePipe(now, 1);
				movePipe(now, 2);
			}
		}

		System.out.println(ans);

	}

	// 가로 : d=0, 세로 : d=1, 대각 d=2
	static void movePipe(Pipe p, int d) {
		if (d != 2) { // 가로, 세로의 경우
			int ni = p.i + di[d];
			int nj = p.j + dj[d];
			if (ni < N && nj < N && !map[ni][nj]) {
				queue.add(new Pipe(ni, nj, d));
			}
		} else { // 대각의 경우
			boolean isPossible = true;
			for (int a = 0; a < 3; a++) {
				int ni = p.i + di[a];
				int nj = p.j + dj[a];
				if (ni >= N || nj >= N || map[ni][nj]) {
					isPossible = false;
					break;
				}
			}
			if (isPossible) {
				queue.add(new Pipe(p.i + 1, p.j + 1, d));
			}
		}
	}
}
