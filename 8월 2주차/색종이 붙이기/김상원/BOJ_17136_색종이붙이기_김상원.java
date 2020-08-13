
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class BOJ_17136_색종이붙이기_김상원 {
	static int size, cnt, result;
	static int[][] paper;
	static boolean[][] visited;
	static boolean[][] copy_visited;
	static Queue<Point> check = new LinkedList<Point>();
	static int[] color_paper;
	static int[] di = {-1, 1, 0, 0}; // 상, 하, 좌, 우
	static int[] dj = {0, 0, -1, 1};
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		paper = new int[10][10];
		visited = new boolean[10][10];
		copy_visited = new boolean[10][10];
		color_paper = new int[6];
		result = 0;
		for(int i=0; i<10; i++) {
			String[] line = br.readLine().split(" ");
			for(int j=0; j<10; j++) {
				paper[i][j] = Integer.parseInt(line[j]);
			}
		}
		///////////////////////////////////////////////////////여기까지 색종이 배열 입력받기
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				if(paper[i][j]==1 && !visited[i][j]) { // 1. 2중 for문을 통해 1인 곳을 찾는다.
					check.clear();
					size = 0;
					size = bfs(new Point(i, j), 0); // 2. bfs를 통해 1이 몇개 있는지 받아온다.
					int size_copy = size;
					System.out.println(size);
					int min = Integer.MAX_VALUE;
					for(int k=5; k>0; k--) { // 3. 종이를 몇 개 쓸 수 있는지 확인하는 곳(현재 문제가 있는 곳이다.)
											 // 논리 : 5x5부터 차례대로 넣을 수 있는지 확인하고 넣어본다. 그러나 5x5를 넣을 수 있어도 4x4를 넣었을 때 최소가 될 수도 있으므로 for문을 통해 5x5도 넣고 4x4도 넣었다.
											 // 하지만 히든테케에서 5x5를 어디에 넣는지에 따라 최소 수가 달라지는 것을 알게 됨. 그래서 이를 완탐으로 바꾸려고 시도 할 예정
						Arrays.fill(color_paper, 5);
						copy_visited();
						cnt=0;
						size = size_copy;
						int n=k;
						while(!(size==0)) {
							if(cnt==-1) {
								break;
							}
							int l_size = size;
							posible(n);
							if((size == l_size) && n>0) n--;
						}
						if(cnt==-1) continue;
						min = Math.min(cnt, min);
					}
					if(min!=Integer.MAX_VALUE) result+=min;
				}
			}
		}
		System.out.println(result);
		System.out.println(Arrays.toString(color_paper));
	}
	// 방문한 곳을 알려주는 visited를 copy하는 함수이다.
	private static void copy_visited() {
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				copy_visited[i][j] = visited[i][j];
			}
		}
	}
	
	// 종이를 넣어보는 곳 parameter는 넣는 종이(5x5, 4x4, 3x3, 2x2 ...)의 크기이다.
	private static void posible(int s) {
		if(size<s*s) {
			return;
		}
		Iterator<Point> iter = check.iterator();
		while(iter.hasNext()) {
			Point p = iter.next();
			boolean flag = false;
			loop:
			for(int i=p.x; i<p.x+s; i++) {
				for(int j=p.y; j<p.y+s; j++) {
					if(!copy_visited[i][j]) {
						flag = true;
						break loop;
					}
				}
			}
			if(!flag){
				for(int i=p.x; i<p.x+s; i++) {
					for(int j=p.y; j<p.y+s; j++) {
						copy_visited[i][j] = false;
					}
				}
				if(color_paper[s]>0) {
					color_paper[s]--;
				}
				else {
					cnt = -1;
					return;
				}
				size = size - s*s;
				cnt++;
				break;
			}
		}
		
	}
	
	// 1이 몇개 있는지 확인해주는 메소드이다. return 값으로 1의 갯수 반환
	private static int bfs(Point point, int cnt) {
		Queue<Point> q = new LinkedList<>();
		q.offer(point);
		check.offer(point);
		visited[point.x][point.y] = true;
		cnt++;
		while(!q.isEmpty()) {
			Point p = q.poll();
			for(int i=0; i<4; i++) {
				int ni = p.x + di[i];
				int nj = p.y + dj[i];
				if(ni>=0 && ni<10 && nj>=0 && nj<10 && paper[ni][nj]==1 && !visited[ni][nj]) {
					Point n_point = new Point(ni, nj);
					q.offer(n_point);
					check.offer(n_point);
					visited[ni][nj] = true;
					cnt++;
				}
			}
		}
		return cnt;
	}
	
	// 좌표 값을 받아주는 point class
	static class Point{
		int x, y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}

