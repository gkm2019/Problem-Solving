package backjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class N17135 {
	static int max = 0, top = 0, n, m, d, ans;
	static int[] shooterColSet = new int[3]; // 궁수 3명 위치 열값 저장 변수
	static ArrayList<Pos> killedEnemy = new ArrayList<>(); // 죽는 적 위치 저장 변수
	static int dir[][] = {{0,-1}, {-1,0}, {0,1}}; // 죽일 적 위치 찾는 델타 배열
	static int map[][], testmap[][]; // 진짜 맵과 해당 라운드당 업데이트 될 맵
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		d = Integer.parseInt(st.nextToken());
		
		map = new int[n][m];
		testmap = new int[n][m];
		for(int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			for(int j = 0; j < m; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		selectShooterPos(0, -1);
		System.out.println(max);
	}
	
	// 궁수들 위치 조합으로 찾는 함수
	static void selectShooterPos(int cnt, int idx) {
		if(cnt == 3) { // 궁수 3명 위치 다 정해짐
			ans = 0;
			
			// 테스트 맵 저장하기
			for(int i = 0; i < n; i++)
				for(int j = 0; j < m; j++)
					testmap[i][j] = map[i][j];
			
			// 죽일 적 확인하러 가기
			cntKilledEnemy(n);
			return;
		}
		
		// 다음 열 위치에 궁수 위치시키러 가기
		for(int i = idx+1; i < m; i++) {
			shooterColSet[top++] = i;
			selectShooterPos(cnt + 1, i);
			top--;
		}		
	}
	
	// 이번 라운드에서 죽인 적이 몇명인지 확인하는 함수
	static void cntKilledEnemy(int shooterRow) {
		if(shooterRow == 0) { // 게임이 종료 되었을 때
			if(ans > max) max = ans; // 스코어 최고 경신여부 확인
			return ;
		}
		
		killedEnemy.clear(); // 전에 죽었던 적 목록 초기화
		for(int i = 0; i < 3; i++) {
			findEnemy(shooterRow, shooterColSet[i]); // 궁수별로 공격할 적 누군지 확인하기
		}
		
		for(Pos p : killedEnemy) { // 공격받는 적 확인하며
			if(testmap[p.r][p.c] == 1) ans++; // 다른 사람이 죽이지 않았으면 카운트 세기
			testmap[p.r][p.c] = 0; // 적 죽음 표시
		}
		cntKilledEnemy(shooterRow - 1); // 다음 라운드 진행
	}
	
	// 다음에 쏠 적을 찾는 함수
	static void findEnemy(int shooterRow, int shooterCol) {
		Queue<Pos> q = new LinkedList<>();
		q.add(new Pos(shooterRow, shooterCol)); // 현재 궁수 위치 저장
		int cntD = 0; // 공격 할 수 있는 거리 측정
		boolean find = false; // 적을 찾았는지 여부 저장
		
		while(!q.isEmpty()) {
			cntD++; // 거리 증가
			int size = q.size(); // 해당 거리만큼에 존재하는 적 확인하기
			for(int i = 0; i < size; i++) {
				Pos cur = q.poll();
				
				for(int j= 0; j < 3; j++) { // 왼쪽을 우선으로 적 찾기
					int chkR = cur.r + dir[j][0];
					int chkC = cur.c + dir[j][1];
					
					if(chkR < 0 || chkR >= shooterRow || chkC < 0 || chkC >= m) continue;
				
					q.add(new Pos(chkR, chkC));
					if(testmap[chkR][chkC] == 1) { // 적이 존재하면 목록에 추가하고 종료하기
						killedEnemy.add(new Pos(chkR, chkC));
						find = true;
						break;
					}
				}
				if(find) break; // 적을 찾았으면 종료
			}
			if(find || cntD == d) break; // 적을 찾았거나 최대 거리 범위 확인 다 끝나면 종료
		}		
	}
	
	// 현재 위치 저장 객체 클래스
	static class Pos{
		int r, c;
		Pos(int r, int c){
			this.r = r;
			this.c = c;
		}
	}
}
