package backjoon;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

// 언니가 이동을 +1 -1 하거나 -1 +1 하면 2일 만에 같은 장소 방문하게 됨 이부분을 체크하여 시간 줄이기
// 2일은 나머지가 0이거나 1로 구분할 수 있으므로 visit 배열을 2차원으로 생성
// 동생의 이동장소를 계산하기 위해 업데이트 되는 cnt 변수를 이용하여 확인
// 동생이 이동한 곳이 언니가 방문한 적이 있거나 언니가 이동한 곳에 동생이 있으면 탐색 종료

public class N17071 {
	static int N, K;
	static int dir[][] = {{1,-1}, {1,1}, {2,0}}; // 이동할 수 있는 장소
	static boolean visit[][] = new boolean[2][500001]; // 방문여부 저장
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		System.out.println(bfs());
	}
	
	private static int bfs() {
		if(N == K) return 0; // 출발점과 도착점이 같으면
		
		int cnt = 0; // 동생의 이동량 저장
		Queue<Integer> q = new LinkedList<>();
		visit[0][N] = true; // 언니의 출발점 visit 체크
		q.add(N); // 언니 현재 위치 저장
		
		while(!q.isEmpty()) {
			int size = q.size(); // 같은 날에 도착하는 곳 먼저 체크하기
			
			K += (++cnt); // 동생의 위치 계산
			if(K > 500000) break; // 범위 벗어나면 끝
			if(visit[cnt%2][K]) return cnt; // 동생이 도착하는 곳이 이미 방문했던 곳이면 종료
			
			for(int s = 0; s < size; s++) { // 언니가 갈 수 있는 곳 체크
				int cur = q.poll();
				
				for(int i = 0; i < 3; i++) {
					int next = cur * dir[i][0] + dir[i][1]; // 다음 이동장소
					
					if(next < 0 || next > 500000 || visit[cnt%2][next]) continue; // 범위 밖이거나 이미 방문한적 있으면 패스
					if(next == K) return cnt; // 언니와 동생이 만나면 종료
					
					visit[cnt%2][next] = true; // 현재 위치 방문 체크
					q.add(next);
				}				
			}
		}
		return -1;
	}
}
