package day0820;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;
/*
 * 규칙성
ex) 5 17
+1 6, 16 18 34 |
+3 8, 15 17 32 | 17 19 36 | 33 35 68 |
+6 11, 14 16 30 | 16 18 34 | 31 33 34 | 16 18 34 | 18 20 38 | 35 37 72 | 32 34 66 | 34 36 70 | 67 69 136 |
+10 15, 13 15 28 | 15 17 32 | 29 31 60 | 15 17 32 | 17 19 36 | 33 35 68 | 30 32 62 | 32 34 66 | 63 65 128 |15 17 32 | 17 19 36 | 33 35 68 | 17 19 36 | 19 21 40 | 37 39 76 | 34 36 70 | 36 38 74 | 71 73 144 | 31 33 64 | 33 
 *
 * 15를 기준으로, 짝수번째에 있으면 그 다음 짝수번째에는 15가 반드시 나옴, 홀수도 마찬가지(+1, -1 되기 때문에) 
 * - 예를들어 동생 수빈이 추월할 경우를 생각했을 때, visit에 true된걸 거르는건 위험할 수있음
 * - visit을 없애자니 너무 많은 queue값이 생성됨
 * - visit에 true가 된 값을 하자니, 위 예시와 같이 짝수, 홀수가 다른것을 볼 수 있음(+1, -1 되기 때문에)
 * -> 짝, 홀수를 나눠서 생각해주는 visit 2차원 배열 생성
 */

public class HideSeek5 {
	static Queue<Integer> que = new LinkedList<>();
	static boolean[][] visit = new boolean[2][500001];		// 각 BFS의 짝수 경우와 홀수 경우로 나눔
	static int a;	// 수빈
	static int b;	// 동생

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		a = Integer.parseInt(st.nextToken()); // 수빈이 : +1, -1, *2
		b = Integer.parseInt(st.nextToken()); // 동생 : 1초당 1씩 가속
		int count = 0; // 동생이 초마다 움직이는 변수, 움직이기 전
		
		check(a, count); // 첫번째 큐에 넣어주기
		boolean find_flag = false;
		// 이미 같은 경우
		if (a == b) {
			System.out.println(0);
			find_flag = true;
		}
		
		while (true) {
			count++; 	// 시간 1초씩 추가
			b += count; // 동생 가속
			
			int size = que.size();
			if (find_flag || size == 0) {	// 동생을 만났거나, queue size가 0일 때ㅡ
				break;
			}if (b > 500000) {				// 동생이 500000을 넘어갔을때,
				System.out.println(-1);
				break;
			}
			if(visit[(count+1)%2][b]) {		// 현재 있는 것과 홀, 짝수를 맞추기 위해서 +1의 나머지를 구해줌
				System.out.println(count);	// 이미 들렀던 곳이면, 현재 큐에서도 들를 수 있다는 뜻이므로 출력
				break;
			}
			
			for (int i = 0; i < size; i++) {	// 흔한 bfs
				int aa = que.poll();
				if (aa == b) {
					find_flag = true;			// 찾으면 flag를 true로 하고 main함수를 끝냄
					System.out.println(count);  // 해당 count 출력
					break;
				} else {
					check(aa, count);			// visit에 true할 것들과, que에 넣을 것들을 찾아주는 메소드
				}
			}
			
		}

	}

	public static void check(int aa, int count) {
		int holjjak = count%2;	// 현재 카운트가 홀수위치인지 짝수 위치인지 알려주는 함수
		if (aa - 1 >= 0 && !visit[holjjak][aa - 1]) {
			que.add(aa - 1);
			visit[holjjak][aa - 1] = true;
		}
		if (aa + 1 <= 500000 && !visit[holjjak][aa + 1]) {
			que.add(aa + 1);
			visit[holjjak][aa + 1] = true;
		}
		if (aa * 2 <= 500000 && !visit[holjjak][aa * 2]) {
			que.add(aa * 2);
			visit[holjjak][aa * 2] = true;
		}

	}
}
