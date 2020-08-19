package day0819;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class BOJ_17070_파이프옮기기1_김상원_DFS {
	static int N, result; // 집의 크기와 결과값을 담을 변수들
	static int map[][]; // 격자판을 담을 변수
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		N = Integer.parseInt(br.readLine());
		map = new int[N][N]; // 문제에서는 (1,1)에서 (N,N)이지만 편의상 (0,0)에서 (N-1,N-1)로 진행하였음
		result = 0;
		for (int i = 0; i < N; i++) {
			String[] line = br.readLine().split(" ");
			for (int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(line[j]);
			}
		}
		// 입력 끝
		dfs(new Point(0,1,"right")); // DFS 시작
		System.out.println(result); // 값 출력

	}
	
	private static void dfs(Point point) {
		if(point.hang == N-1 && point.rul == N-1) { // 재귀함수의 기저부분 마지막 호출의 좌표가 (N-1,N-1)이라면 count++ 후 종료
			result++;
			return;
		}
		else {
			String dir = point.dir; // 움직이기 전에 존재하는 파이프 모양 [right=일자, cross=대각선, down=아래]
			switch (dir) { // switch문을 통해 파이프 모양에 따라 붙이는 걸 달리한다.
			// right라면 right와 cross를 붙일 수 있음, if문은 좌표가 범위 안에 있고 벽을 만나지 않았을 경우를 만족하기 위한 조건문임
			case "right":
				if(point.rul+1<N && map[point.hang][point.rul+1]!=1) { 
					dfs(new Point(point.hang, point.rul+1, "right"));
				}
				if(point.hang+1<N && point.rul+1<N && map[point.hang][point.rul+1]!=1 && map[point.hang+1][point.rul]!=1 && map[point.hang+1][point.rul+1]!=1) {
					dfs(new Point(point.hang+1, point.rul+1, "cross"));
				}
				break;
			// cross라면 right, cross, down을 붙일 수 있음
			case "cross":
				if(point.rul+1<N && map[point.hang][point.rul+1]!=1) {
					dfs(new Point(point.hang, point.rul+1, "right"));
				}
				if(point.hang+1<N && point.rul+1<N && map[point.hang][point.rul+1]!=1 && map[point.hang+1][point.rul]!=1 && map[point.hang+1][point.rul+1]!=1) {
					dfs(new Point(point.hang+1, point.rul+1, "cross"));
				}
				if(point.hang+1<N && map[point.hang+1][point.rul]!=1) {
					dfs(new Point(point.hang+1, point.rul, "down"));
				}
				break;
			// down이라면 cross, down을 붙일 수 있음
			case "down":
				if(point.hang+1<N && point.rul+1<N && map[point.hang][point.rul+1]!=1 && map[point.hang+1][point.rul]!=1 && map[point.hang+1][point.rul+1]!=1) {
					dfs(new Point(point.hang+1, point.rul+1, "cross"));
				}
				if(point.hang+1<N && map[point.hang+1][point.rul]!=1) {
					dfs(new Point(point.hang+1, point.rul, "down"));
				}
				break;

			default:
				break;
			}
		}
		
	}

	static class Point { // 좌표 그리고 파이프 모양을 저장할 클래스 객체
		int hang, rul;
		String dir;

		public Point(int hang, int rul, String dir) {
			this.hang = hang;
			this.rul = rul;
			this.dir = dir;
		}
	}

}
