package day0820;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// DFS 버전..
public class Pipeline {
	static boolean[][] wall;	//벽
	static int result = 0;		//결과
	static int N;				//벽 크기
	public static void main(String[] args) throws NumberFormatException, IOException {
		// 입력 받는부분
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		N = Integer.parseInt(br.readLine());
		wall = new boolean[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				int block = Integer.parseInt(st.nextToken());
				if (block == 1) {
					wall[i][j] = true;
				}
			}
		}
		////////////////////////
		// 시작 부분 { (0,1)의 가로 방향(1) }
		Point head = new Point(0, 1, 1);
		// dfs 돌려버려
		dfs(head);
		System.out.println(result);
	}
	
	// 파이파가 놓인 방향에 따라, 다음 파이프를 굴릴 곳 유효 범위에 벽이 있는지 확인 
	static boolean check_the_wall(Point p, int val) {
		if(val == 1) {
			if(!wall[p.x][p.y+1]) {
				return true;
			}
		}
		if(val == 2) {
			if(!wall[p.x+1][p.y]) {
				return true;
			}
		}
		if(val == 3) {
			if(!wall[p.x][p.y+1] && !wall[p.x+1][p.y+1] && !wall[p.x+1][p.y]) {
				return true;
			}
		}
		return false;
	}
	
	
	static void dfs(Point head) {
		if(head.x == N-1 && head.y== N-1) {
			result++;
			return;
		}
		
		// 스스로 아쉬웠던 점 : 이게 가로일때나 세로일때나 한방향으로 쭉 갈때, N-2 까지만 유효한데 N-1까지 DFS를 긁어버려야 하는 불필요한 동작이 있었음
		// N-2까지로 하면 정점에 도달하기 어려움. 결론... BFS, N-2로 하다가 큰코 다쳤다.
		switch(head.val) {
		case 1:		// 가로 일때
			
			if(head.y+1<=N-1 && check_the_wall(head, 1)) {		// 가로로 한번 더 보내보자
				Point temp1 = new Point(head.x, head.y+1, 1);
				dfs(temp1);
			}
			if(head.x+1 <=N-1 && head.y+1 <=N-1 && check_the_wall(head, 3)) {	// 대각선으로 보내보자
				Point temp1 = new Point(head.x+1, head.y+1, 3);
				dfs(temp1);
			}
			break;
		case 2:		// 세로 일때
			if(head.x+1<=N-1 && check_the_wall(head, 2)) {		// 세로로 한번 더 보내보자
				Point temp1 = new Point(head.x+1, head.y, 2);
				dfs(temp1);
			}
			if(head.x+1<=N-1 && head.y+1<=N-1 && check_the_wall(head,3)) {		// 대각선으로 보내보자
				Point temp1 = new Point(head.x+1, head.y+1, 3);
				dfs(temp1);
			}
			break;
		case 3:		// 대각선 일때
			if(head.y+1<=N-1 && check_the_wall(head,1)) {		// 가로로 보내보자
				Point temp1 = new Point(head.x, head.y+1, 1);
				dfs(temp1);
			}
			if(head.x+1<=N-1 && check_the_wall(head,2)) {		// 세로로 보내보자
				Point temp1 = new Point(head.x+1, head.y, 2);
				dfs(temp1);
			}
			if(head.x+1<=N-1 && head.y+1<=N-1 && check_the_wall(head,3)) {		// 대각선으로 한번 더 보내보자
				Point temp1 = new Point(head.x+1, head.y+1, 3);
				dfs(temp1);
			}
			break;
		}
	}
	// 파이프에 뒤에 따라오는놈은 알 필요가 없음 앞 대가리의 좌표
	static class Point {
		int x;
		int y;
		int val;	// 파이프가 놓인 방향? 가로:1  세로:2  대각선:3
		Point(int x, int y, int val) {
			this.x = x;
			this.y = y;
			this.val = val;
		}
	}
}
