package backjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class N16637 {
	static char[] arr;
	static int N, max = Integer.MIN_VALUE;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		N = Integer.parseInt(br.readLine());
		arr = new char[N];
		arr = br.readLine().toCharArray();
		
		if(N == 1) { // 길이 하나면 바로 출력하기
			max = Math.max(max, arr[0] - '0');
		}else {
			findVal(arr[0] - '0', 2); // 첫번째 연산 그룹으로 안묶고 넘기기
			long val = cal(arr[0] - '0', arr[2] - '0', arr[1]); 
			findVal(val, 4); // 첫번째 연산 그룹으로 묶고 넘기기
		}
		System.out.println(max);
	}
	
	static void findVal(long sum, int idx) { // sum : 앞의 연산 계산한 값, idx : 숫자를 가르키는 인덱스
		if(idx >= N) { // 현재 인덱스가 연산 끝까지 왔으면 종료
			max = Math.max(max, (int)sum);
			return;
		}
		
		long val = cal(sum, arr[idx] - '0', arr[idx-1]); // 앞에서 계산했던 값과 현재 위치의 수 계산하기
		findVal(val, idx+2); // 다음 숫자로 넘어가기
		
		if(idx != N-1) { // 마지막 숫자가 아니면 다음 숫자랑 그룹으로 묶어서 계산하고 넘기기
			val = cal(arr[idx] - '0', arr[idx+2] - '0', arr[idx+1]);
			val = cal(sum, val, arr[idx-1]);
			findVal(val, idx+4);
		}		
	}
	
	static long cal(long a, long b, char e) { // 연산 계산하기
		if(e == '+')
			return a+b;
		else if(e == '-')
			return a-b;
		else 
			return a*b;
	}
}
