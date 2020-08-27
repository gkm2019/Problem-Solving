package BOJ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main_파이프옮기기2 {

	static int n, d;
	static int[] dx = { 0, 1, 1 }, dy = { 1, 1, 0 }; // 가로,대각,아래
	static int[][] map;
	static long[][][] visit;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());

		map = new int[n][n];
		visit = new long[n][n][3];
		//방문하지 않은 경로는 -1 .. visit또하나 만들지않고 하나로 처리해버리기
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++)
				for(int k=0;k<3;k++)
					visit[i][j][k]=-1;
		
		for (int i = 0; i < n; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		} // end input
		// solution
		System.out.println(dfs(0,1,0));
	}
	
	/*
	 * 재귀 -> dp
	 * 리턴값을 두고 dp배열 return 해주면된다~
	 * */

	static long dfs(int x, int y, int d) {
		//base case
		if(visit[x][y][d]!=-1)return visit[x][y][d];
		if(x==n-1 && y==n-1)return 1; //경로 하나 추가~
		
		visit[x][y][d]=0; //방문 표시!
		int nx,ny;
		for(int i=0;i<4;i++) {
			if(i==3) {//장애물 없으니까 이동은 가능함. y==2범위 체크만 하자.
				visit[x][y][d] += dfs(x+dx[1], y+dy[1], 1);//대각선 이동
				break;
			}
			nx=x+dx[i]; ny=y+dy[i];
			if(!Range(nx,ny) || map[nx][ny]==1)
				break;
		}
		
		if (d == 1) { 
			nx = x + dx[0];
			ny = y + dy[0];
			if (Range(nx, ny) && map[nx][ny] == 0)
				visit[x][y][d]+=dfs(nx,ny,0);
			nx = x + dx[2];
			ny = y + dy[2];
			if (Range(nx, ny) && map[nx][ny] == 0)
				visit[x][y][d]+=dfs(nx,ny,2);

		} else {
			nx = x + dx[d];
			ny = y + dy[d];
			if (Range(nx, ny) && map[nx][ny] == 0)
				visit[x][y][d]+=dfs(nx,ny,d);
		}
		
		return visit[x][y][d];
	}//end dfs();
	
	static boolean Range(int x, int y) {
		if(0<=x && 1<=y && x<n && y<n)return true;
		return false;
	}
}
