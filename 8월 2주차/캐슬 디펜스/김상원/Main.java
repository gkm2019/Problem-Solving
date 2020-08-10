package day0810;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	static int N,M,D,kill;
	static int[][] field, copy_field;
	static int[] archer;
	static int[] di = {-1, 0, 0}; // 상, 좌, 우
	static int[] dj = {0, -1, 1};
	static boolean[][] isKilled;
	static boolean[] isSelected;
	static ArrayList<Integer> kill_arr;
	static ArrayList<Archer> enemy;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
		StringTokenizer st = new StringTokenizer(br.readLine()," ");
		N = Integer.parseInt(st.nextToken()); // 입력받기
		M = Integer.parseInt(st.nextToken());
		D = Integer.parseInt(st.nextToken());
		isKilled = new boolean[N][M]; // 초기화
		copy_field = new int[N][M];
		field = new int[N][M];
		archer = new int[3];
		isSelected = new boolean[M];
		kill_arr = new ArrayList<Integer>();
		enemy = new ArrayList<Archer>();
		for(int i=0; i<N; i++) { // 입력받기
			st = new StringTokenizer(br.readLine()," ");
			for(int j=0; j<M; j++) {
				field[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		combination_archor(0, 0); // 궁수 배치 조합
		Collections.sort(kill_arr);
		System.out.println(kill_arr.get(kill_arr.size()-1));
	}
	private static void combination_archor(int cnt, int start) { // 궁수 배치를 조합 후(MC3) 게임 시작
		if(cnt==3) {
			kill=0;
			copy();
			int kill_cnt = killed_enemy(); // killed_enemy()가 게임시작하는 메소드
			kill_arr.add(kill_cnt);
			return;
		}
		else {
			for(int i=start; i<M; i++) {
				if(!isSelected[i]) {
					isSelected[i] = true;
					archer[cnt] = i;
					combination_archor(cnt+1, i);
					isSelected[i] = false;
				}
			}
		}
		
	}
	private static void copy() { // 맵 복사하는 메소드
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				copy_field[i][j] = field[i][j];
			}
		}
	}
	private static int killed_enemy() { // 여기가 메인 게임
		while(!isFinish()) { // 맵의 값이 다 0이면 게임 종료
			Queue<Archer> q = new LinkedList<Archer>(); // 큐 생성
			for(int i=0; i<3; i++) { // 3명의 궁수 조합을 받았으므로 궁수 3명이 화살을 쏠꺼임
				q.clear(); // 큐를 비워주고
				q.add(new Archer(N, archer[i])); // 큐에 넣고 bfs 시작
				int move_cnt=0;
				loop: // 빠져나오기 위한 loop 설정
				while(!q.isEmpty()) { 
					boolean flag = false; // 궁수가 적을 쐈는지 안쐈는지 확인하는 flag
					int size = q.size();
					for(int j=0; j<size; j++) { // 동일 범위의 D만 확인하기 위해
						Archer archer = q.poll();
						for(int k=0; k<3; k++) { // 3방향 탐색(상,좌,우)
							int ni = archer.hang + di[k];
							int nj = archer.rul + dj[k];
							if(ni>=0 && ni<N && nj>=0 && nj<M) { // 맵 범위 확인 후
								if(copy_field[ni][nj]==1 || copy_field[ni][nj]==2) { // 1 or 2값이 맵에 있다면
									flag = true; // 궁수가 쏠 수 있어!!
									enemy.add(new Archer(ni, nj)); // 좌표를 enemy배열에 넣음
								}
								else {
									q.offer(new Archer(ni, nj)); // 0이라면 q에 넣고
								}
							}
						}
					}
					if(flag) { // 여기가 중요!! D 이하의 범위 동안 궁수가 쏠 수 있는 적을 발견하면 왼쪽의 적을 고르므로
						Collections.sort(enemy);  // comparable을 활용해 왼쪽좌표 순으로 오름차순 정렬
						copy_field[enemy.get(0).hang][enemy.get(0).rul] = 2; // 중복으로 쏠 수 있으니 2라는 값을 줘서 다음 궁수와 구별
						enemy.clear(); // 배열 비워줌
						break loop; // 다음 궁수 쏘자!
					}
					move_cnt++; // 쏠 대상을 못 찾았다 cnt를 늘려 D까지!
					if(move_cnt==D) break; // D인데도 못 찾았으면 못 쏨 다음 궁수로 pass
				}
			}
			next_wave(); // 맵을 아래로 한 칸 씩 밀어주는 메소드
		}
		return kill;
	}
	private static boolean isFinish() { // 맵의 값이 0인지 확인해줌
		boolean flag=true;
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				if(copy_field[i][j]==1) {
					flag = false;
				}
			}
		}
		return flag;
	}
	private static void next_wave() {
		for(int i=0; i<M; i++) {
			if(copy_field[N-1][i]==2) kill++;
		}
		for(int i=N-1; i>0; i--) {
			for(int j=M-1; j>=0; j--) {
				if(copy_field[i-1][j]==2) {
					kill++;
					copy_field[i][j] = 0;
				}
				else {
					copy_field[i][j] = copy_field[i-1][j];
				}
			}
		}
		for(int i=0; i<M; i++) {
			copy_field[0][i] = 0;
		}
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				isKilled[i][j] = false;
			}
		}		
	}
}
class Archer implements Comparable<Archer>{
	int hang, rul;
	public Archer(int hang, int rul) {
		this.hang = hang;
		this.rul = rul;
	}
	@Override
	public int compareTo(Archer o) {
		return Integer.compare(this.rul, o.rul);
	}
}

