import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/*
 * 완전탐색으로 풀어보자
 * 맨 앞에서 뒤로 가면서 이녀석이 5x5 ~ 1x1 중에 무었이 되는지 보고
 * 일단 붙이고 맵 갱신 후 쭉 돌아보다가 -1되면 처음으로 돌아가서
 * 처음에 붙인 숫자가 4x4면 3x3으로 붙여보고 다시 진행 ... 너무 많은 경우가 생길거같다..
 * 
 * 맵 전체를 돌면서 
 * 0 0 0
 * 0 1 0
 * 0 0 0 이런 식으로 주위가 전부 0으로 둘러싸인 NxN 모양을 찾고 먼저 색종이를 붙여보고 나머지는 완전탐색으로 진행하기
 * 
 */

public class Boj17136 {
	
	static int ans;
	static int[] cnts;
	static boolean[][] map;
	static boolean[][] visit;
	

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
		map = new boolean[10][10];
		cnts = new int[6]; // 1x1 ~ 5x5 색종이 5개 제한 확인용, 0번 인덱스 사용 x
		ans = Integer.MAX_VALUE;
		
		for (int i = 0; i < 10; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			for (int j = 0; j < 10; j++) {
				map[i][j] = st.nextToken().charAt(0) == '1';
			}
		}
		
		// 1. 맵 전체를 돌면서 rectCheck 후 0으로 둘러싸인 곳 먼저 처리하기
		alonePart();
		
		for (boolean[] m : map) {
			for (boolean b : m) {
				System.out.print(b? "1 " : "0 ");
			}
			System.out.println();
		}
		
		// 2. 남은 맵에서는 완전 탐색 해보기 ? DFS
		boolean[][] temp = copy(map);
		int[] paper = new int[6];
		for (int i = 0; i < 6; i++) {
			paper[i] = cnts[i];
		}
		dfs(0, 0, temp, paper);	
		
		System.out.println(ans);
		
	}
	
	static boolean[][] copy(boolean[][] map) {
		boolean[][] temp = new boolean[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				temp[i][j] = map[i][j];
			}
		}
		return temp;
	}
	
	static void dfs(int ci, int cj, boolean[][] temp, int[] paper) {
		if (ci == 10 && cj == 10) {
			int sum = 0;
			for (int p : paper) {
				sum += p;
			}
			System.out.println(sum);
			ans = Math.min(sum, ans);
			return;
		}
		
		for (int i = ci; i < 10; i++) {
			for (int j = cj; j < 10; j++) {
				if (temp[i][j] && !visit[i][j]) {
					visit[i][j] = true;
					int maxN = rectCheck(i, j);
					for (int k = 1; k <= maxN; k++) {
						makeZero(i, j, k, temp);
						
						if (++paper[k] > 5) {
							System.out.println(-1);
							return;
						}
						
						dfs(i, j + 1, temp, paper);
					}
					visit[i][j] = false;
				}
			}
		}
		
	}
	
	static void alonePart() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (map[i][j]) {
					int c = rectCheck(i, j); // 1인 점에서의 체크
					if (isAlone(i, j, c)) {
						makeZero(i, j, c, map);
						
						if (++cnts[c] > 5) {
							// 색종이 배열에 추가 후 색종이 갯수가 5개를 넘어가면 -1을 출력하고 return
							System.out.println(-1);
							return;
						}						
					}
				}
			}
		}
	}
	
	static int rectCheck(int ci, int cj) {	
		int ans = 5; // 다 통과하면 max값인 5x5 색종이
		
		for (int cnt = 1; cnt <= 5; cnt++) {
			for (int a = 0; a <= cnt; a++) {
				if (ci + a >= 10 || ci + cnt >= 10 || cj + cnt >= 10 || cj + a >= 10 
						|| !map[ci + a][cj + cnt] || !map[ci + cnt][cj + a]) {
					return cnt;
				} 
			}
		}
		
		return ans;
	}
	
	static boolean isAlone(int ci, int cj, int cnt) {
		// 이 메소드는 NxN이면서 상,하,좌,우가 1과 전혀 안 만나는 경우입니다.
		boolean flag = true;
		for (int a = 0; a < cnt; a++) {
			// 상
			if (cj - 1 >= 0 && map[ci + a][cj - 1]) {
				flag = false;
				break;
			}
			// 하
			if (cj + 1 < 10 && map[ci + a][cj + 1]) {
				flag = false;
				break;
			}
			// 좌
			if (ci - 1 >= 0 && map[ci - 1][cj + a]) {
				flag = false;
				break;
			}
			// 우
			if (ci + 1 < 10 && map[ci + 1][cj + a]) {
				flag = false;
				break;
			}
		}	
		return flag;
	}
	
	static void makeZero(int ci, int cj, int cnt, boolean[][] map) {
		for (int i = 0; i < cnt; i++) {
			for (int j = 0; j < cnt; j++) {
				int ni = ci + i;
				int nj = cj + j;
				map[ni][nj] = false;
			}
		}		
	}
}
