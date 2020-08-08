package BOJ;

import java.util.*;
import java.io.*;

public class Main_17136_색종이붙이기 {
	static int[][] map;
	static int[] PaperCnt;
	static int SIZE = 10, ans;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		map = new int[SIZE][SIZE];
		PaperCnt = new int[6];
		for (int i = 0; i < SIZE; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			for (int j = 0; j < SIZE; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		} // end input

		// initialize PaperCnt
		for (int i = 1; i <= 5; i++)
			PaperCnt[i] = 5;

		ans = Integer.MAX_VALUE;
		// logic
		BackTracking(0, 0, 0);
		if (ans == Integer.MAX_VALUE)
			ans=-1;
			
		System.out.println(ans);
	}

	static void BackTracking(int x, int y, int sum) {
		if (ans <= sum)
			return;
		boolean flag = true;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (map[i][j] == 0)
					continue;
				flag = false;// 1이면 안덮힌거임 못나가!!
				x = i;
				y = j;
				break;
			}
			if (!flag)
				break;
		}
		if (flag) { // Base Case : 다 덮혀있을 떄
			ans = Math.min(ans, sum);
			return;
		}
		// 1~5칸 짜리 다 덮어본다.
		for (int paperSize = 5; paperSize >= 1; paperSize--) {
			if (PaperCnt[paperSize] <= 0)
				continue; // 종이 다 써서 없음
			if (!isCovered(x, y, paperSize))
				continue; // 종이가 범위 밖이거나, 사이즈 안맞음
			
			PaperCnt[paperSize]--;// 종이 사용
			Covered(x, y, paperSize, 0);// cover
			BackTracking(x, y, sum + 1);// **recursive Point!!
			Covered(x, y, paperSize, 1);// discover
			PaperCnt[paperSize]++;// 다시 종이 가져다 놔

		} // end for paperSize
	}// end BackTracking
	
	static boolean isCovered(int x, int y, int size) {
		if (0 > x || 0 > y || x + size > SIZE || y + size > SIZE)
			return false;
		for (int i = x; i < x + size; i++) {
			for (int j = y; j < y + size; j++) {
				if (map[i][j] == 0)
					return false;
			}
		}
		return true;
	}// end isCovered

	static void Covered(int x, int y, int size, int val) {// 0:coverd , 1:discovered
		for (int i = x; i < x + size; i++) {
			for (int j = y; j < y + size; j++) {
				map[i][j] = val;
			}
		}
	}// end Covered
}
