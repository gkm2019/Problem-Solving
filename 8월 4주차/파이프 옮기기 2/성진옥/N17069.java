package backjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class N17069 {
	static int N;
	static int map[][];
	static long memo[][][]; // 메모이제이션 변수
	static int dir[][] = {{0,1}, {1,0}, {1,1}}; // 가로, 세로, 대각선
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		N = Integer.parseInt(st.nextToken());
		map = new int[N][N];
		memo = new long[3][N][N]; // 0 : 가로로 방문 / 1 : 세로로 방문 / 2 : 대각선으로 방문
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			for(int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				for(int k = 0; k < 3; k++) // 방문체크 배열 초기화
					memo[k][i][j] = -1;
			}
		}
		System.out.println(func(0, 1, 0));
	}
	
	static long func(int r, int c, int type) {
		if(r == N-1 && c == N-1) return 1; // 도착했으면 카운트 1 리턴
		if(memo[type][r][c] != -1) return memo[type][r][c]; // 방문한 적이 있는 곳이면 방문체크배열 리턴
		
		long sum = 0; // 다음 방문장소에서 반환되어온 값 저장 변수
		boolean canMove2 = true; // 2번 (대각선)으로 이동가능한지 체크
		for(int i = 0; i <3; i++) { // 0-2번 장소 확인하기
			int nr = r + dir[i][0];
			int nc = c + dir[i][1];
			if(nr >= N || nc >= N || map[nr][nc] == 1) {
				canMove2 = false; // 3곳 중 하나라도 불가능하면 대각선 못감
				continue;
			}
			
			//가로-> 가로 / 세로-> 세로 / 대각선->가로, 세로 방문 조건
			if((i == type && type != 2) || (type == 2 && i != 2))			
				sum += func(nr, nc, i);
		}
		if(canMove2) // 대각선 방문 조건
			sum += func(r + dir[2][0], c + dir[2][1], 2);
		
		memo[type][r][c] = sum; // 반환값 저장 변수 방문체크배열에 저장하기
		return memo[type][r][c]; 
	}
}
