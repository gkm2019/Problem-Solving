package BOJ;
import java.util.*;
import java.io.*;

public class Main_캐슬디펜스 {

	static int N,M,D,cnt,ans=Integer.MIN_VALUE;
	static int[][] map;
	static boolean[][] check;//적 죽일 때 중복이면 카운팅 안하기!!
	static int[] select;//궁수 위치 저장
	static ArrayList<Enemy> possible;//죽일 수 있는 적을 저장
	static class Enemy implements Comparable<Enemy>{
		int x, y, d;
		
		Enemy(int x, int y, int d){
			this.x=x;
			this.y=y;
			this.d=d;
		}

		@Override
		public int compareTo(Enemy o) {
			if(this.d>o.d)return 1;
			if(this.d==o.d) {
				if(this.y>o.y)return 1;
				return -1;
			}
			return -1;
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine()," ");
		N=Integer.parseInt(st.nextToken());
		M=Integer.parseInt(st.nextToken());
		D=Integer.parseInt(st.nextToken());
		map = new int[N][M];
		select = new int[3];
		
		for(int i=0;i<N;i++) {
			st = new StringTokenizer(br.readLine()," ");
			for(int j=0;j<M;j++) {
				map[i][j]=Integer.parseInt(st.nextToken());
			}
		}//end for input
		
		dfs(0,0);
		System.out.println(ans);
	}

	static void dfs(int depth, int idx) {
		if(depth==3) {
			solution();
			return;
		}
		for(int i=idx;i<M;i++) {
			select[depth]=i;
			dfs(depth+1, i+1);
		}
	}//end dfs
	
	static void solution() {
		cnt=0;
		check = new boolean[N][M];
		ArrayList<Enemy> killed = new ArrayList<Enemy>();
		for(int sniperX=N ; sniperX>=1; sniperX--) {
			for(int i=0;i<3;i++) {
				int sniperY = select[i];
				Enemy tmp = kill(sniperX, sniperY); //죽일 적 1마리 가져옴
				if(tmp!=null)
					killed.add(tmp);//죽여!
			}
			
			for(Enemy e:killed) {
				int r=e.x;
				int c=e.y;
				if(check[r][c]==false) {
					check[r][c]=true;
					cnt++;
				}
			}
			killed.clear();//비워
		}//end sniper hunting
		ans=Math.max(ans, cnt);
	}//end solution
	
	static int dist(int x1, int y1, int x2, int y2) {
		return Math.abs(x1-x2)+Math.abs(y1-y2);
	}
	
	static Enemy kill(int x, int y) {
		possible = new ArrayList<Enemy>();
		//적 검사해서 담기 (아래에서 위로/왼쪽에서 오른쪽)
		for(int i=x-1;i>=0;i--) {
			for(int j=0;j<M;j++) {
				if(map[i][j]==0||check[i][j])continue;
				int dis=dist(i,j,x,y);
				if(dis>D)continue;
				possible.add(new Enemy(i,j,dis));
			}
		}//end for
		if(possible.size()==0)return null; //죽일 수 있는 적 없음
		Collections.sort(possible);//적 정렬
		return possible.get(0);
	}//end kill
}
