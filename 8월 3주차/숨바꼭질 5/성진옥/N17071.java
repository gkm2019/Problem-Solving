package backjoon;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class N17071 {
	static int N, K;
	static int dir[][] = {{1,-1}, {1,1}, {2,0}};
	static boolean visit[][] = new boolean[2][500001];
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		System.out.println(bfs());
	}
	
	private static int bfs() {
		if(N == K) return 0;
		
		int cnt = 0;
		Queue<Integer> q = new LinkedList<>();
		visit[0][N] = true;
		q.add(N);
		
		while(!q.isEmpty()) {
			int size = q.size();
			
			K += (++cnt);
			if(K > 500000) break;
			if(visit[cnt%2][K]) return cnt;
			
			for(int s = 0; s < size; s++) {
				int cur = q.poll();
				
				for(int i = 0; i < 3; i++) {
					int next = cur * dir[i][0] + dir[i][1];
					
					if(next < 0 || next > 500000 || visit[cnt%2][next]) continue;
					if(next == K) return cnt;
					
					visit[cnt%2][next] = true;
					q.add(next);
				}				
			}
		}
		return -1;
	}
}
