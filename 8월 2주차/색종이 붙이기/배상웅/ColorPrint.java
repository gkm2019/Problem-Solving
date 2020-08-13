import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class ColorPrint {
	static int[][] ar;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		boolean flag_flag = false;
		ar = new int[10][10];
		// 값을 전부 받고 혹시 전부 0 인경우 빠르게 zero_flag 넣어줌.. (확인용)
		for (int tc = 0; tc < 10; tc++) {
			st = new StringTokenizer(br.readLine());
			for (int tt = 0; tt < 10; tt++) {
				ar[tc][tt] = Integer.parseInt(st.nextToken());
				if(ar[tc][tt]!=0) {
					flag_flag = true;
				}
			}
		}
		boolean FoF = false;
		int min_paper = 26; 	// 전체 종이의 수가 26장일 수 없으므로 가장 많은 max_value+1값을 넣어줌
		for (int tc = 0; tc < 10; tc++) {
			for (int tt = 0; tt < 10; tt++) {
				// 1을 찾은경우!
				if (ar[tc][tt] == 1) {
					int max_size = 0;
					int[][] temp_ar = new int[10][10];
					// 원본 보존을 위해 temp 배열에 복사한다
					arrayCopy(ar, temp_ar);
					// 사이즈가 큰것부터 하나씩 줄여줌
					for (int size = 5; size >= 1; size--) {
						// 사이즈가 해당 위치에 맞는지 확인
						max_size = match_size(tc, tt, size, temp_ar);
						// 해당 사이즈가 맞지 않으면 0이므로 바로 다음 size를 넣어줘 보게 한다.
						if(max_size==0) continue;
						// 일단 해당 범위 내에 붙임
						attach_paper(tc, tt, max_size, temp_ar);
						// 붙인 걸 가지고 다시 위 과정을 반복할 수 있는 dfs를 돌림
						dfs(tc, tt, max_size, temp_ar);
						
						// 여기서 부터는 색종이가 각자 얼마나 쓰였는지 확인하는 부분
						int[] color_cnt = new int[6];
						boolean flag= false;
						for (int i = 0; i < 10; i++) {
							for (int j = 0; j < 10; j++) {
								color_cnt[temp_ar[i][j]]++;
								flag = true;
								System.out.print(temp_ar[i][j] + " ");
							}
							System.out.println();
						}
						System.out.println();
						int count = -1;
						// 한개라도 5장 넘어가면 flag를 false로 변경해서 -1을 뽑도록함
						for (int i = 1; i < 6; i++) {
							if((color_cnt[i]/(i*i))>5) {
								flag = false;
								break;
							}
							else {
								count+=(color_cnt[i]/(i*i));
							}
						}
						// 한개라도 flag가 true면 해당 사용된 종이수를 min_paper에 넣어둠
						if(flag) {
							FoF=true;
						}
						if(flag) {
							if(min_paper > count && count != -1) {
								if(count > -1) {
									count++;
								}
								min_paper = count;
							}
						}
						// 만약 모든 경우의 수가 색종이수를 초과하게하면 -1을 뽑도록 함
//						else if(!flag) {
//							if(FoF) {
//							}
//							else {
//								min_paper= -1;
//							}
//						}
						// 맨첨에 붙였던 종이를 떼어넴
						detach_paper(tc, tt, max_size, temp_ar);
						
						
					}				
				}
			}
		}
		// 만약 붙일 종이가 1장도 없을 경우 0 출력
		
		if(!FoF) {
			min_paper = -1;
		}
		if(!flag_flag) {
			min_paper = 0;
		}
		System.out.println(min_paper);
		
	}
	
	// 배열 복사
	public static int[][] arrayCopy(int[][] array, int[][] copyarray) {
		for (int i = 0; i < array.length; i++) {
			copyarray[i] = array[i].clone();
		}
		return copyarray;
	}
	
	public static void dfs(int a, int b, int size, int[][] ar) {
		for (int tc = 0; tc < 10; tc++) {
			for (int tt = 0; tt < 10; tt++) {
				if (ar[tc][tt] == 1) {
					int match_size = 0;
					int max_size = 0;
					for (int psize = 5; psize >= 2; psize--) {
						match_size = match_size(tc, tt, psize, ar);
						if(match_size ==0) continue;
						
						if (max_size < match_size) {
							max_size = match_size;
						}
						attach_paper(tc, tt, max_size, ar);
						
						dfs(tc, tt, max_size, ar);
					}
				}
			}
		}
		return;
	}
	// 붙였던 부분 다시 떼주기
	public static void detach_paper(int a, int b, int size, int[][] ar) {
		for (int i = a; i < a + size; i++) {
			for (int j = b; j < b + size; j++) {
				ar[i][j] = 1;
			}
		}
	}
	
	// size를 입력받아 해당 범위 내에 모두 붙여주기
	public static void attach_paper(int a, int b, int size, int[][] ar) {
		if(a+size>10 || b+size > 10) {return;}
		for (int i = a; i < a + size; i++) {
			for (int j = b; j < b + size; j++) {
				
				ar[i][j] = size;
			}
		}
	}

	// 해당 구역에 붙일 수 있는 가장 큰 사이즈의 색종이를 구하기
	public static int match_size(int a, int b, int size, int[][] ar) {
		if(a+size>10 || b+size > 10) {return 0;}
		for (int tc = a; tc < a + size; tc++) {
			for (int tt = b; tt < b + size; tt++) {
				if (tc >= 10 || tt >= 10) {
					break;
				} else {
					// 만약 1이 아니라 다른 숫자가 이미 있는 경우는 못건드리게 넘김
					if (ar[tc][tt] != 1) {
						return 0;
					}
				}
			}

		}
		return size;
	}
}
