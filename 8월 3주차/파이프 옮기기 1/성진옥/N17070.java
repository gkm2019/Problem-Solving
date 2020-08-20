package backjoon;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class N17070 {
	static int size;
	static int map[][];
	static int dir[][] = {{0,1}, {1,0}, {1,1}};
	
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
		System.out.println(r + " " + c + " " + idx);
		if(r == size-1 && c == size-1) return 1;
		
		boolean chk = false;
		int cnt = 0;
		for(int i = 0; i < 3; i++) {
			int nr = r + dir[i][0], nc = c + dir[i][1];
			if(nr < 0 || nc < 0 || nr >= size || nc >= size || map[nr][nc] == 1) {
				chk = true;
			}else if((idx == i || idx == 2) && i != 2) {
				cnt += dfs(nr, nc, i);				
			}
		}
		if(!chk) cnt += dfs(r+1,c+1, 2);
		return cnt;
	}
}
