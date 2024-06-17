public class Chess {
  private static final int[][] directions = { { -1, -2 }, { -2, -1 }, { -2, 1 }, { -1, 2 }, { 1, -2 }, { 2, -1 },
      { 2, 1 }, { 1, 2 } }; // 马走的8个方向（相对坐标）

  public static int countWays(int sx, int sy, int ex, int ey) {
    int[][] ways = new int[10][9]; // 记录能够到达每个点的方式数
    ways[sx][sy] = 1; // 起始点只有一种方式到达
    // 遍历每个点
    for (int x = sx; x <= ex; x++) {
      for (int y = 0; y <= 8; y++) {
        // 遍历马能够走的每个方向
        for (int[] direction : directions) {
          int dx = direction[0], dy = direction[1];
          int nx = x + dx, ny = y + dy;
          if (nx < sx || nx > ex || ny < 0 || ny > 8) { // 越界的情况
            continue;
          }
          ways[nx][ny] += ways[x][y]; // 统计到达方式数
        }
      }
    }
    return ways[ex][ey]; // 返回能够到达结束点的方式数
  }

  public static void main(String[] args) {
    int ways = countWays(0, 0, 9, 8);
    System.out.printf("从(%d,%d)到(%d,%d)有%d种方式", 0, 0, 9, 8, ways);
  }
}