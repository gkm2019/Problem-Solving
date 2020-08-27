package BOJ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main_숨바꼭질5 {

	static int n, k, size = 500050;
	static boolean[][] visit;

	static class pair {
		int x, time;

		pair(int x, int time) {
			this.x = x;
			this.time = time;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		n = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		visit = new boolean[size][2];

		Queue<pair> q = new LinkedList<>();
		visit[n][k%2] = true;
		q.add(new pair(n, 0));

		while (!q.isEmpty()) {
			pair front = q.poll();
			int tmpk=k+((1+front.time)*front.time)/2;
			if (tmpk > 500000) break;
			if (visit[tmpk][front.time%2]) {
				System.out.println(front.time);
				return;
			}

			for (int i = 0; i < 3; i++) {
				int nextPos = distN(front.x, i);
				if (nextPos > 500000 || nextPos < 0)continue;
				if (visit[nextPos][(front.time+1)%2])continue;
				visit[nextPos][(front.time+1)%2] = true;
				q.add(new pair(nextPos, front.time + 1));
			}
		} // end while

		System.out.println(-1);
	}

	static int distN(int x, int d) {
		if (d == 0)
			return x - 1;
		if (d == 1)
			return x + 1;
		return x * 2;
	}
}