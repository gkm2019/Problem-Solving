import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class BOJ17071_2 {

	static int MAX = 500000;
	static int N, K;
	static int[][] time;
	static Queue<Loc> queue;

	static class Loc {
		int n, s;

		Loc(int n, int s) {
			this.n = n;
			this.s = s;
		}
	}

	/*
	 * 매 초당 K의 위치는 정해져있다. 
	 * 1초 : K+1, 2초 : K+1+2, ... n초 : K+1+2+...+(n-1)+n
	 * 결국 시간 s에 따른 K의 위치는 K + s*(s+1)/2 이다.
	 * N을 move하면서 그때그때의 s에 따라 K위치에 도착하는지만 파악하면 된다. 
	 * 
	 * 500000 1을 할경우 맛이 간다. 같은 시간에 자꾸 같은 위치를 반복한다. +1, -1 이런식으로 ...
	 * 그렇다고 한번 간 위치를 다시 못가게 하면 문제가 발생한다. 그 위치가 지금 시간에는 만나는 위치가 아니지만 몇 초 후 만나는 시간이 될 수도 있다.
	 * 일례로 시작위치인 N은 1초후 N+1 2초 후 N으로 매 짝수시간마다 N으로 도착할 수 있다.
	 * N+1도 마찬가지이다 1초후 N+1, 2초후 N 3초 후 N+1 이 경우 매 홀수 시간마다 N+1로 도착한다.
	 * N이 5일 때 4 5, 4 3 2 1 2 3 4 5, 4 3 6 5, 4 3 2 4 5 이렇게 매 2초마다 가능하고 5초 후도 가능하다. 단 절대로 3초는 안됨.
	 * 일단 조금이라도 필터링을 하기 위해 해당 위치를 짝수 시간에 접근한 것과 홀수 시간에 접근한 것으로 visit배열을 만들어보자
	 * 
	 * 단순하게 홀수 짝수 배열만 나눈 경우 문제가 생긴다. 2초후 는 아니지만 4초후에 만나야 하는경우 2초때 이미 방문했으므로 재방문안한다. 
	 * 방문 여부를 판단하는 visit배열로는 판단불가능
	 * 방문한 시간을 저장해주는 time배열을 만들고 num초 때 홀수시간에 방문시 time[1][N의 위치] = num 이런식으로 저장한다.
	 * 그러면 큐에 넣을 때 
	 * 1. 홀수인지 짝수인지 체크
	 * 2. 현재 N의 위치가 예전에 들른 곳이어도 들어가야하므로 같은 시간에 들르는것만 패스해주기 
	 * 2. time[0 or 1][nowN] == nowS 연산이 true면 pass false면 go
	 * 3. false인 경우 나중에 들른 것이므로 해당 좌표값 갱신
	 * 500000 1은 통과, 백준 틀림
	 * 
	 * 상웅이, 진옥이 코드 보고 추가한 부분
	 * N이 짝수, 홀수에 따라 동생이 그때 그때 위치를 배열에 체크해서 누가 방문한 흔적이 있으면 수빈이가 올수 있다고 판단 후 자신의 초 값을 리턴
	 * 백준 런타임 에러로 인해 포기
	 */

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());

		time = new int[2][MAX + 1]; // 0행은 짝수 초 일때 방문시간, 1행은 홀수 초 일때 방문시간
		
		int ans = bfs();		

		System.out.println(ans);

	}
	
	static int bfs() {
		int ans = -1;
		queue = new LinkedList<>();
		queue.add(new Loc(N, 0));
		time[N % 2][N] = 0;

		while (!queue.isEmpty()) {
			Loc now = queue.poll();
			int nowK = K + now.s * (now.s + 1) / 2;

			if (nowK > MAX || now.n > MAX) { // N이나 K가 500000을 나간 경우 찾을 수 없다
				break;
			}

			if (now.n == nowK) {
				ans = now.s;
				return ans;
			}
			
			

			for (int i = 0; i < 3; i++) {
				int nextN = moveN(now.n, i);
				int nextS = now.s + 1;
				int nextK = nowK + nextS;
				int index = (now.s + 1) % 2;
				if (nextN >= 0 && nextN <= MAX && time[index][nextN] != nextS) {
					queue.add(new Loc(nextN, nextS));
					time[index][nextN] = nextS;
					
					// 수빈이가 이미 방문했던 위치를 동생이 방문한다면
					if (time[index][nextK] != 0) {
						ans = nextS;
						return ans;
					}
				}
			}
		}
		
		return ans;
	}

	static int moveN(int n, int i) {
		int result = 0;
		switch (i) {
		case 0:
			result = n - 1;
			break;
		case 1:
			result = n + 1;
			break;
		case 2:
			result = n * 2;
			break;
		}
		return result;
	}
}
