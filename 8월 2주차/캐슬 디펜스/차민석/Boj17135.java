import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

/*
 * 1. 궁수가 어느 위치에 있어야 할지 위치 지정해주기 ( 조합이므로 재귀 사용 )
 * 2. 적들이 내려온다는 것보다 궁수가 위로 올라가는것으로 표현하는게 편할 것 같다.
 * 3. 그렇다면 궁수는 한칸 올라가므로 그 행의 적들은 밖으로 나갔다고 생각해서 잡을 수 없게 해야한다.
 * 4. 궁수 공격 알고리즘 : 거리 D 이하 가장 가까운적. 여럿이면 가장 왼쪽의 적 (열이 가장 작은 것 )
 * 5. 죽일 녀석 체크 후 한 번에 같이 죽인다. ( 겹칠 수 있다. )
 */
public class Boj17135 {

	static int N, M, D, maxKill;
	static boolean[][] map;
	static int[] temp;
	static boolean[] visit;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		maxKill = 0;
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		D = Integer.parseInt(st.nextToken());
		map = new boolean[N + 1][M]; // N행은 처음 궁수 위치

		temp = new int[3];
		visit = new boolean[M];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				map[i][j] = st.nextToken().charAt(0) == '1';
			}
		}

		makeAPos(0, 0);
		
		// 위의 함수가 다 끝나면 maxKill이 정해져 있다.
		System.out.println(maxKill);

	}
	
	static class Point { 
		int i, j, d;
		Point(int i, int j) { // 기본 좌표 표시 용도
			this.i = i;
			this.j = j;
		}
		Point (int i, int j, int d) { // 적들 큐에 넣을 때 용도
			this(i, j);
			this.d = d;
		}
	}

	// 1번 궁수 위치 정하기 ( DFS )
	static void makeAPos(int cnt, int cur) {
		if (cnt == 3) {
			attack(temp);
			// 왜인지는 몰라도 재귀함수 밖으로 나가면 데이터가 제대로 저장이 안되니까 그냥 이 안에서 호출해야겠다
			
			return;
		}

		for (int i = cur; i < M; i++) {
			temp[cnt] = i;
			makeAPos(cnt + 1, i + 1);
		}
	}
	
	// 처음 배열은 그대로 놓고 배열을 복사해서 변경해야 다음 궁수 위치 변경시 영향을 주지 않는다.
	static boolean[][] copy(boolean[][] map) {
		boolean[][] temp = new boolean[N + 1][M];
		for (int i = 0; i <= N; i++) {
			for (int j = 0; j < M; j++) {
				temp[i][j] = map[i][j];
			}
		}
		return temp;
	}
	
	// 2번 main 공격 함수 : 궁수가 N번 행에서 1번행까지 올라가면서 적 공격
	static void attack(int[] aPos) {
		boolean[][] tempMap = copy(map);
		Point[] killEnemy = new Point[3]; // 죽일 적 위치 저장하는 배열
		
		int killCnt = 0; // 적 죽인 횟수
		
		// 궁수가 N 행에서 1행까지 올라간다 
		for (int i = N; i >= 1; i--) {
			for (int a = 0; a < 3; a++) {
				Point p = killEnemy(i, aPos[a], tempMap);
				// 좌표 (N, 궁수위치) 를 매개변수로 넘겨서 D에 따라 죽이는 녀석이 있는지 확인
				
				killEnemy[a] = p; 
				// 바로 죽여서 맵에 갱신하지 않고 일단 배열에 넣어둔다. 	
				// 다음 궁수가 죽이는 적의 위치가 달라질 수 있다.
			}
			
			// 위의 for문 ( 궁수들의 적 찾기 ) 끝나면 적을 죽이는 for문 실행
			for (int b = 0; b < 3; b++) {
				Point p = killEnemy[b];
				if (p == null) { // 죽일 적이 없는 경우 다음 궁수로 넘어간다.
					continue;
				}
				killCnt += (tempMap[p.i][p.j]) ? 1 : 0;
				// 적이 이미 죽어있으면 killCnt 올리지 않는다.
				tempMap[p.i][p.j] = false; // 적 죽이기
			}
		}		
		
		// 이중 for문이 끝나면 궁수 위치 1가지 경우에 대한 적을 죽이는 수가 나온다. 전체 max와 비교
		maxKill = Math.max(killCnt, maxKill);
	}
	
	// 3번 BFS 이용
	static Point killEnemy(int pi, int pj, boolean[][] temp) {
		// 적을 죽이는 우선순위 1. D이하에서 가장 가까운 것
		// 2. 가장 왼쪽 적 ( j가 가장 작은 것 )
		// 좌, 상, 우 순서대로 탐색하면 같은 D에서 가장 왼쪽 적을 탐색할 수 있다.
		// D가 1일 경우에는 한칸 앞 한놈만 가능하다.
		Queue<Point> queue = new LinkedList<>(); 
		// 탐색할 좌표를 큐에 저장
				
		int[] di = { 0, -1, 0 }; // 좌 상 우  순서 delta 배열
		int[] dj = { -1, 0, 1 };
		
		// 큐 체크 용 visit 배열
		boolean[][] visit = new boolean[N][M];
		
		queue.add(new Point(pi - 1, pj, 1)); // D = 1
		visit[pi -1][pj] = true; 
		// 궁수 위치에서 상 방향 1칸 위 , D = 1일 때는 한 가지 경우
		
		while (!queue.isEmpty()) {
			Point now = queue.poll();
			int ci = now.i;
			int cj = now.j;
			int cd = now.d;
						
			if (temp[ci][cj]) { 
				// D가 작은 순서대로, 좌, 상, 우 순서대로 poll 된다
				return new Point(ci, cj);
			}
						
			for (int d = 0; d < 3; d++) {
				int ni = ci + di[d];
				int nj = cj + dj[d];
				if (ni >= 0 && nj >= 0 && nj < M && !visit[ni][nj] && cd + 1 <= D) {
					queue.add(new Point(ni, nj, cd + 1));
					visit[ni][nj] = true;
				}
			}
		}		
		return null;
	}
}
