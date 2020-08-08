import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class B17135 {

    private static class Point {
        int i;
        int j;
        int d;

        public Point(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public Point(int i, int j, int d) {
            this.i = i;
            this.j = j;
            this.d = d;
        }

        @Override
        public String toString() {
            return "(" + i + ", " + j + ", " + d + ")";
        }
    }

    private static class Location {
        int j1;
        int j2;
        int j3;

        public Location(int j1, int j2, int j3) {
            this.j1 = j1;
            this.j2 = j2;
            this.j3 = j3;
        }

        @Override
        public String toString() {
            return "(" + j1 + ", " + j2 + ", " + j3 + ")";
        }
    }

    private static int N, M, D;
    private static int[] input;
    private static int[] numbers;
    private static int cnt;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine().trim());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        D = Integer.parseInt(st.nextToken());

        int[][] mapOrigin = new int[N + 1][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine().trim());
            for (int j = 0; j < M; j++) {
                mapOrigin[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        input = new int[M];
        IntStream.range(0, M).forEach(i -> input[i] = i);
        numbers = new int[3];

        List<Location> locations = new ArrayList<>();
        // 궁수의 모든 좌표 생성 (mC3)
        combination(0, 0, locations);
        // System.out.println(locations);

        int[][] map = copyMap(mapOrigin);
        int max = 0;
        for (Location location : locations) {
            // 궁수 좌표 설정
            map[N][location.j1] = 9;
            map[N][location.j2] = 9;
            map[N][location.j3] = 9;

            // System.out.println("=== 처음 ===");
            // printMap(map);
            // System.out.println("===================");
            cnt = 0;
            // 적의 좌표가 없을 때 까지 반복
            do {
                // 거리가 D이하의 적을 찾아보자 사살한다.
                Point p1 = distanceCheck(map, location.j1);
                Point p2 = distanceCheck(map, location.j2);
                Point p3 = distanceCheck(map, location.j3);
                killEnemy(map, p1, p2, p3);
                // printMap(map);
                // System.out.println("===================");

                // 턴이 끝나면 적은 내려간다.
                downMap(map);
            } while (!isEnemy(map));

            max = Math.max(max, cnt);

            // 궁수 좌표 초기화, map 초기화
            for (int i = 0; i < M; i++) {
                map[N][i] = 0;
            }
            map = copyMap(mapOrigin);
        }

        bw.write(String.valueOf(max));
        bw.flush();
    }

    private static void killEnemy(int[][] map, Point p1, Point p2, Point p3) {
        if (p1.i != N + 1 && p1.j != M && map[p1.i][p1.j] == 1) {
            map[p1.i][p1.j] = 0;
            cnt++;
        }
        if (p2.i != N + 1 && p2.j != M && map[p2.i][p2.j] == 1) {
            map[p2.i][p2.j] = 0;
            cnt++;
        }
        if (p3.i != N + 1 && p3.j != M && map[p3.i][p3.j] == 1) {
            map[p3.i][p3.j] = 0;
            cnt++;
        }
    }

    private static Point distanceCheck(int[][] map, int y) {
        List<Point> pointList = new ArrayList<>();
        int minD = 11;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (map[i][j] == 1) {
                    int d = Math.abs(N - i) + Math.abs(y - j);
                    if (D >= d) {
                        pointList.add(new Point(i, j, d));
                        minD = Math.min(d, minD);
                    }
                }
            }
        }

        Point point = new Point(N + 1, M);
        for (Point p : pointList) {
            if (p.d == minD) {
                if (Math.abs(point.j) > Math.abs(p.j)) {
                    point = new Point(p.i, p.j);
                }
            }
        }

        // System.out.println("PointList => " + pointList + " => " + point);

        return point;
    }

    private static void downMap(int[][] map) {
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                queue.offer(map[i][j]);
            }
        }
        for (int i = 1; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (queue.isEmpty()) break;
                map[i][j] = queue.poll();
            }
        }
        for (int i = 0; i < M; i++) {
            map[0][i] = 0;
        }
    }

    private static int[][] copyMap(int[][] mapOrigin) {
        int[][] map = new int[N + 1][M];
        for (int i = 0; i < N; i++) {
            System.arraycopy(mapOrigin[i], 0, map[i], 0, M);
        }
        return map;
    }

    private static void combination(int cnt, int start, List<Location> list) {
        if (cnt == 3) {
            list.add(new Location(numbers[0], numbers[1], numbers[2]));
            return;
        }

        for (int i = start; i < M; i++) {
            numbers[cnt] = input[i];
            combination(cnt + 1, i + 1, list);
        }
    }

    private static void printMap(int[][] map) {
        for (int i = 0; i < N + 1; i++) {
            for (int j = 0; j < M; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }

    private static boolean isEnemy(int[][] map) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (map[i][j] == 1) {
                    return false;
                }
            }
        }
        return true;
    }

}
