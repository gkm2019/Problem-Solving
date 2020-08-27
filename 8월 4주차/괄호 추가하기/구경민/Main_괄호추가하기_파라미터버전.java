package BOJ;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main_괄호추가하기_파라미터버전 {

	static int n, ans = Integer.MIN_VALUE;
	static char[] opSelect;
	static int[] numSelect;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		opSelect = new char[n / 2];
		numSelect = new int[n / 2 + 1];

		int op=0, num=0;
		String str = br.readLine();
		for (int i = 0; i < n; i++) {
			if(i%2==0)
				numSelect[num++]=str.charAt(i)-'0';
			else
				opSelect[op++]=str.charAt(i);
		} // end input

		dfs(0, numSelect[0]);
		System.out.println(ans);
	}

	static void dfs(int idx, int ret) {
		if(idx>=n/2) { //operate index기준 n/2까지!!
			ans=Math.max(ans, ret);
			return;
		}
		
		//괄호X
		int tmp = calc(ret, numSelect[idx+1], opSelect[idx]);
		dfs(idx+1, tmp);
		
		//괄호O
		if(idx+1<n/2) {
			int pretmp=calc(numSelect[idx+1], numSelect[idx+2], opSelect[idx+1]);
			tmp = calc(ret, pretmp, opSelect[idx]);
			dfs(idx+2, tmp);
		}
	}
	
	static int calc(int x, int y, char op) {
		if (op == '+')return x + y;
		if (op == '-')return x - y;
		return x * y;
	}
}
