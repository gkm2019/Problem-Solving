package backjoon;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class N17070 {
	static int size;
	static int map[][];
	static int dir[][] = {{0,1}, {1,0}, {1,1}}; // 현재 모양에서 확인해야 할 다음 위치 방향 값 (옆, 아래, 대각선)
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		size = Integer.parseInt(br.readLine());
		map = new int[size][size];
		
		for(int i = 0; i < size; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			for(int j = 0; j < size; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		System.out.println(dfs(0,1,0));
	}
	
	private static int dfs(int r, int c, int idx) {
		if(r == size-1 && c == size-1) return 1; // 현재 위치가 마지막일 때 방법이 하나 추가 되므로 1 리턴
		
		boolean chk = false; // 대각선으로 가고자 할때는 옆, 아래, 대각선을 확인해야 갈 수 있으므로 갈수있는지 체크하는 변수
		int cnt = 0; // 다음 방향으로 이동했을 때 마지막에 도달하는 방법 수
		for(int i = 0; i < 3; i++) {
			int nr = r + dir[i][0], nc = c + dir[i][1];
			if(nr < 0 || nc < 0 || nr >= size || nc >= size || map[nr][nc] == 1) { // 범위가 벗어나거나 갈 수 없으면
				chk = true; // 변수값 변경
			} // 현재와 동일한 모양으로 이동하거나 현재 모양이 대각선인데 갈 곳은 대각선이 아니면 (대각선은 구역체크 후 갈수있음)
			else if((idx == i || idx == 2) && i != 2) {
				cnt += dfs(nr, nc, i);				
			}
		}
		if(!chk) cnt += dfs(r+1,c+1, 2); // 대각선으로 갈 수 있으므로 가기
		return cnt; // 현재 위치에서 목적지까지 도달한 횟수 리턴
	}
}
