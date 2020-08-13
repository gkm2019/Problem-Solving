import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class CastleDef {
	static int N;
	static int M;
	static int len;
	static int[][] ar;
	static ArrayList<int[]> archer_loc = new ArrayList<int[]>();
	static int result;
	static int[] di = { 0, -1, 0 };
	static int[] dj = { -1, 0, 1 };
	static int[] kill_count;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		len = Integer.parseInt(st.nextToken());

		ar = new int[N][M];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				ar[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		result = 0;
		boolean[] visit = new boolean[M];
		int[] result1 = new int[3];
		// 사수 위치 받기
		archer(visit, result1, 0, 0);
		kill_count = new int[archer_loc.size()];
		for (int i = 0; i < archer_loc.size(); i++) {
			int archer1 = archer_loc.get(i)[0];	// 궁수 위치 조합을 통해 정해진 1번 궁수
			int archer2 = archer_loc.get(i)[1]; // 2번 궁수
			int archer3 = archer_loc.get(i)[2]; // 3번 궁수
			// 맨 아래서 부터
			int[][] ar_temp = new int[N][M];
			ar_temp = arrayCopy(ar, ar_temp);
			int count = 0; // 사정거리
			for (int bot = N - 1; bot >= 0; bot--) {
				boolean[][] visited = new boolean[N][M];
				ArrayList<int[]> al = new ArrayList<>();
				find_next_enemy(bot, archer1, count, visited, ar_temp, i, al);	// 1번 궁수 활 
				find_next_enemy(bot, archer2, count, visited, ar_temp, i, al);  // 2번 궁수 활
				find_next_enemy(bot, archer3, count, visited, ar_temp, i, al);  // 3번 궁수 활
				for(int k=0; k<al.size(); k++) {
					if(ar_temp[al.get(k)[0]][al.get(k)[1]]==1) {
						ar_temp[al.get(k)[0]][al.get(k)[1]] = 0;
						kill_count[i]++;
					}
//					System.out.print(al.get(k)[0]+", "+al.get(k)[1]+" / ");
				}
//				System.out.println();
//				for(int k=0; k<N; k++) {
//					for(int l=0; l<M; l++) {
//						System.out.print(ar_temp[k][l]);
//					}
//					System.out.println();
//				}
//				System.out.println();
				count = 0;
			}
			

		}
		int max_value = 0;
		for (int i = 0; i < kill_count.length; i++) {
			if (kill_count[i] > max_value) {
				max_value = kill_count[i];
			}
		}
		System.out.println(max_value);

	}
	// 원본 훼손 방지
	public static int[][] arrayCopy(int[][] array, int[][] copyarray) {
		for (int i = 0; i < array.length; i++) {
			copyarray[i] = array[i].clone();
		}
		return copyarray;
	}
	
	public static void find_next_enemy(int line, int archer, int count, boolean[][] visited, int[][] ar_temp, int i,
			ArrayList<int[]> al) {
		// 현재 위치의 바로 위에 있는지 확인 있으면 지울 대상에 추가
		if (count >= len)
			return;
		// 사정거리 1일때 처리를 생각 못해서 따로 만들어준 부분
		if (len == 1) {
			if (ar_temp[line][archer] == 1) {
				int[] temp = { line, archer };
				al.add(temp);
			} else {
				return;
			}
		}
		// 사정거리 2이상일때,
		else {
			// 궁수 바로 앞에 있는 경우
			if (ar_temp[line][archer] == 1) {
				int[] temp = { line, archer };
				al.add(temp);
				if (al.size() == 3) {
					return;
				}
			}
			// 바로 앞에 없는 경우(바로앞이 0인경우)
			else {
				boolean flag = false;
				count++;
				for (int ii = 0; ii < 3; ii++) {
					int dii = di[ii] + line;
					int djj = dj[ii] + archer;
					// 왼쪽, 위, 오른쪽 확인했을 때, 있으면 바로 break를 건다 (왼쪽이 먼저기때문)
					if (dii >= 0 && djj >= 0 && djj < M) {
						if (ar_temp[dii][djj] == 1) {
							int[] temp = { dii, djj };
							al.add(temp);
							flag = true;
							break;
						}
					}
				}
				// 좌 상 우 중 단 한개도 1이 아닌경우
				if (!flag) {
					Queue<Integer> que = new LinkedList<>();
					count++;
					// 일단 다시 좌 상 우 x,y 좌표를 큐에 넣고
					for (int ii = 0; ii < 3; ii++) {
						int dii = di[ii] + line;
						int djj = dj[ii] + archer;
						if (dii >= 0 && djj >= 0 && djj < M) {
							que.add(dii);
							que.add(djj);
						}
					}
					// bfs 를 사용해서 1이 나올때까지 (어차피 좌상우 기준이기때문에 좌측부터 확인)
					// 근데 모두 또 0이 나올 수 있으므로, 우선 좌측 검색할때부터 que에 쌓아둔다.(그럼 또 좌측부터 검색가능함)
					while (true) {
						if (count >= len) {
							break;
						}
						int size = que.size();
						boolean reflag = false;
						for (int ii = 0; ii < size / 2; ii++) {
							int aa = que.poll();
							int bb = que.poll();
							for (int jj = 0; jj < 3; jj++) {
								int dii = di[jj] + aa;
								int djj = dj[jj] + bb;
								if (dii >= 0 && djj >= 0 && djj < M) {
									if (ar_temp[dii][djj] == 1) {
										int[] temp = { dii, djj };
										al.add(temp);
										flag = true;
										break;
									} else {
										que.add(dii);
										que.add(djj);
									}

								}
							}
							if (flag) {
								break;
							}
						}
						if (!reflag) {
							count++;
						}
					}
				}
			}
		}
	}
// 궁수 위치 조합
	public static int[] archer(boolean[] visit, int[] result, int count, int idx) {
		if (count == 3) {
			int[] temp = { result[0], result[1], result[2] };
			archer_loc.add(temp);
			return result;
		}
		for (int i = idx; i < M; i++) {
			if (!visit[i]) {
				result[count++] = i;
				visit[i] = true;
				archer(visit, result, count, i);
				visit[i] = false;
				count--;
			}
		}
		return result;
	}
	
}
