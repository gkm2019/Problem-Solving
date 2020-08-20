package day0820;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class BOJ_17071_숨바꼭질5_김상원 {
	static int N, K; // 수빈, 동생 초기위치
	static boolean[][] visited = new boolean[2][500001]; // 홀수초일 때와 짝수초일 때의 방문처리를 위한 boolean 배열
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] line = br.readLine().split(" ");
		N = Integer.parseInt(line[0]);
		K = Integer.parseInt(line[1]);
		// 입력 끝!
		
		bfs(N, K); // bfs시작!!
	}
	private static void bfs(int n, int k) {
		Queue<Integer> q = new LinkedList<Integer>();
		q.offer(n);
		int cnt=0; // 시간을 담을 변수
		int flag=0; // 홀수초, 짝수초 구분 변수
		while(!q.isEmpty()) {
			int size = q.size(); // 너비마다 for문을 돌려 초를 측정하기 위한 size
			flag = (cnt%2==0) ? 0:1; // 짝수 초면 0, 홀수 초면 1
			for(int i=0; i<size; i++) {
				int p = q.poll();
				if(p==k || visited[flag][k]) { // 종료조건 : poll한 값(현재 수빈이 위치)과 k(현재 동생 위치)가 같거나 || visit배열의 [현재 시간(홀수인지 짝수인지)] [k값] 이 true라면 
					System.out.println(cnt); // 결과값 출력 후 종료
					return;
				}
				if(visited[flag][p]) continue; // 만약 현재초(홀수, 짝수)의 visit배열이 방문한 적이 있다면 pass
				visited[flag][p] = true; // 방문한적 없다면 true로 값 세팅
				
				// +1, *2, -1을 해준 값이 범위 안에 있다면 q에 넣어준다.
				if(p+1<=500000) {
					q.offer(p+1);
				}
				if(p>0 && p*2<=500000) {
					q.offer(2*p);
				}
				if(p-1>=0) {
					q.offer(p-1);
				}
				
			}
			cnt++; // 처음 지정한 큐를 다돌면 시간++
			k = k+cnt; // 동생의 위치도 바꿔주고
			if(k>500000) { // 만약 동생의 위치가 500000이 넘었따? 더 진행할 필요 없으니 -1출력하고 종료
				System.out.println(-1);
				return;
			}
		}
		
	}

}
