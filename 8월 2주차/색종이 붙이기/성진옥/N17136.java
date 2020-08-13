package backjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class N17136 {
	static int map[][] = new int[10][10];
	static int paperSizeCnt[] = {0,5,5,5,5,5}; // 사이즈별 색종이 남아있는 갯수 저장
	// 최소값 가지고 있을 변수, 붙인 종이 갯수, 종이 붙여야 하는 면적
	static int ans = Integer.MAX_VALUE, paperCnt = 0, paperNeedCnt = 0; 
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for(int i = 0; i < 10; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			for(int j = 0; j < 10; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				paperNeedCnt += map[i][j]; // 1이면 종이 붙여야 하므로 변수에 더하기
			}
		}
		
		if(paperNeedCnt == 0) ans = 0; // 종이를 붙여야 할 면적이 없으므로 최소값 0으로 변경
		else {			
			for(int r = 0; r < 10; r++) {
				for(int c = 0; c < 10; c++) {
					if(map[r][c] == 1) { // 종이 붙여야 하는 곳 나오면 갯수 확인하기
						chkPaper(r, c); 
						break; // 다른곳도 이미 확인한 이후이므로 더 찾아보지 않아도 됨
					}
				}
			}
		}
		if(ans == Integer.MAX_VALUE) ans = -1; // 최솟값 변수가 초기값이면 불가능하다는 것이므로 -1로 변경
		System.out.println(ans);
	}
	
	public static void chkPaper(int r, int c) {
		int curMaxSize = chkSize(r, c); // 현재 시점에서 붙일 수 있는 가장 큰 사이즈 계산
		
		for(int size = curMaxSize; size > 0; size--) { // 최대 사이즈부터 1까지 붙여보기
			if(paperSizeCnt[size] == 0) continue; // 붙이려는 종이 사이즈의 재고가 없으면 다음 사이즈 확인
			
			paperCnt++; // 붙인 색종이 갯수 세기
			paperNeedCnt -= size*size; // 색종이 붙인 면적 계산하기
			paperSizeCnt[size]--; // 붙인 색종이 사이즈 재고 업데이트 하기
			changeMap(r, c, size, 0); // 색종이 붙이기
			
			// 모든 곳에 색종이 다 붙었는지 확인
			if(paperNeedCnt == 0) {
				if(ans > paperCnt) {
					ans = paperCnt;
				}
				paperCnt--; 
				paperNeedCnt += size*size;
				paperSizeCnt[size]++;
				changeMap(r, c, size, 1);
				return; // 더이상 알아볼 가치가 없어서 방금 붙인 색종이 떼고 반환하기
			}
			
			// 다음 위치 가보고
			int curR = r, curC = c;
			while(true) { // 현재 색종이 붙인거 다음으로 붙인 장소 한군데만 가면 됨
				// 현재 열이 끝까지 왓으면 다음 행으로 넘어가기 위해 하는 절차
				int nc = (curC == 9)? 0 : curC + 1; 
				int nr = (nc == 0)? curR+1 : curR;
				curR = nr; curC = nc;
				
				if(nr == 10 && nc == 0) break; // 맵의 끝까지 왔으면 종료
				if(map[nr][nc] == 0) continue; // 가려는 위치에 색종이를 못붙이면 다음 위치 찾기
				else {
					chkPaper(nr, nc); // 다음위치에서 색종이 붙이러 가기
					break;
				}
			}
			
			// 현재 사이즈의 색종이 붙인거 떼고 다음 사이즈 색종이 붙일 것이므로 계산 되돌리기
			paperCnt--; // 색종이 떼므로 사용한 색종이 갯수 -1하기
			paperNeedCnt += size*size; // 색종이 떼는 면적 계산하기
			paperSizeCnt[size]++; // 떼는 색종이 사이즈 재고 업데이트 하기
			changeMap(r, c, size, 1);// 색종이 떼기
		}
	}
	
	// 그 지점에서 붙일 수 있는 가장 큰 색종이 사이즈 측정
	public static int chkSize(int r, int c) {
		for(int size = 5; size > 0; size--) {
			boolean finish = false; // 현재 사이즈가 확보 가능한지 여부
			for(int i = r; i < r+size; i++) {
				if(i >= 10) { // 현재 사이즈가 불가능하면 종료
					finish = true;
					break;
				}
				for(int j = c; j < c+size; j++) {
					if(j >= 10 || map[i][j] == 0) { // 현재 사이즈가 불가능하거나 종이가 필요없으면 종료
						finish = true;
						break;
					}
				}
				if(finish) break; // 불가능한 조건이 있다는 것이므로 종료
			}
			if(!finish) return size; // 사이즈가 가능하고 종이가 필요하면 현재 사이즈 반환
		}
		return 1;
	}
	
	// 현재 위치에서 사이즈 만큼 색종이 붙여다 뗄 함수(value가 0이면 색종이 붙이기, 1이면 색종이 떼기)
	private static void changeMap(int r, int c, int size, int value) {
		for(int i = r; i < r+size; i++) {
			for(int j = c; j < c+size; j++) {
				map[i][j] = value;
			}
		}
	}
}