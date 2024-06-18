package org.freeman.service;

import java.util.ArrayList;
import java.util.List;

public class GomokuAI {

    private static int BOARD_SIZE;
    private static final int EMPTY = 0;
    private static final int AI = 1;
    private static final int HUMAN = 2;

    public static int[] findBestMove(int[][] board,int borderSize) {
        BOARD_SIZE = borderSize;
        List<int[]> possibleMoves = getPossibleMoves(board);
        int[] bestMove = null;
        int maxScore = Integer.MIN_VALUE;

        for (int[] move : possibleMoves) {
            board[move[0]][move[1]] = AI;
            int score = evaluateMove(board, move);
            board[move[0]][move[1]] = EMPTY;

            if (score > maxScore) {
                bestMove = move;
                maxScore = score;
            }
        }

        return bestMove;
    }

    // 获取所有可能的落子点
    private static List<int[]> getPossibleMoves(int[][] board) {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    moves.add(new int[]{i, j});
                }
            }
        }
        return moves;
    }

    private static int evaluateMove(int[][] board, int[] move) {
        int score = 0;
        // 检查横向
        score += checkLine(board, move[0], move[1], 0, 1);
        // 检查纵向
        score += checkLine(board, move[0], move[1], 1, 0);
        // 检查斜向
        score += checkLine(board, move[0], move[1], 1, 1);
        score += checkLine(board, move[0], move[1], 1, -1);

        // 增加对全局策略的评估
        int aiLineLength = getLongestLineLength(board, AI);
        int humanLineLength = getLongestLineLength(board, HUMAN);

        // 进攻评分
        if (aiLineLength >= 4) {
            score += 10000;
        } else if (aiLineLength == 3) {
            score += 100;
        } else if (aiLineLength == 2) {
            score += 10;
        }

        // 防守评分 - 增加防守权重
        if (humanLineLength >= 4) {
            score -= 20000; // 将防守权重提高到 20000
        } else if (humanLineLength == 3) {
            score -= 200; // 将防守权重提高到 200
        } else if (humanLineLength == 2) {
            score -= 20; // 将防守权重提高到 20
        }

        // 考虑棋盘中心位置
        if (move[0] >= 3 && move[0] <= 5 && move[1] >= 3 && move[1] <= 5) {
            score += 10; // 给予中心区域的落子点额外的得分
        }

        return score;
    }

    // 检查一条线上的得分
    private static int checkLine(int[][] board, int row, int col, int dx, int dy) {
        int score = 0;
        int aiCount = 0;
        int humanCount = 0;

        // 检查左边
        for (int i = 1; i <= 4; i++) {
            int x = row - i * dx;
            int y = col - i * dy;
            if (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE) {
                if (board[x][y] == AI) {
                    aiCount++;
                } else if (board[x][y] == HUMAN) {
                    humanCount++;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        // 检查右边
        for (int i = 1; i <= 4; i++) {
            int x = row + i * dx;
            int y = col + i * dy;
            if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
                break;
            } else {
                if (board[x][y] == AI) {aiCount++;}
                else if (board[x][y] == HUMAN) {humanCount++;}
                else {break;}
            }
        }

        // 计算得分
        if (aiCount >= 4) {
            score += 10000;
        } else if (aiCount == 3) {
            score += 100;
        } else if (aiCount == 2) {
            score += 10;
        }

        if (humanCount >= 4) {
            score -= 20000; // 将防守权重提高到 20000
        } else if (humanCount == 3) {
            score -= 200; // 将防守权重提高到 200
        } else if (humanCount == 2) {
            score -= 20; // 将防守权重提高到 20
        }

        return score;
    }


    private static int getLongestLineLength(int[][] board, int player) {
        int maxLength = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == player) {
                    int lineLength = checkLineLength(board, i, j, 0, 1, player);
                    maxLength = Math.max(maxLength, lineLength);
                    lineLength = checkLineLength(board, i, j, 1, 0, player);
                    maxLength = Math.max(maxLength, lineLength);
                    lineLength = checkLineLength(board, i, j, 1, 1, player);
                    maxLength = Math.max(maxLength, lineLength);
                    lineLength = checkLineLength(board, i, j, 1, -1, player);
                    maxLength = Math.max(maxLength, lineLength);
                }
            }
        }
        return maxLength;
    }

    private static int checkLineLength(int[][] board, int row, int col, int dx, int dy, int player) {
        int length = 1;
        for (int i = 1; i <= 4; i++) {
            int x = row + i * dx;
            int y = col + i * dy;
            if (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE && board[x][y] == player) {
                length++;
            } else {
                break;
            }
        }
        for (int i = 1; i <= 4; i++) {
            int x = row - i * dx;
            int y = col - i * dy;
            if (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE && board[x][y] == player) {
                length++;
            } else {
                break;
            }
        }
        return length;
    }

//    public static void main(String[] args) {
//        int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
//        // AI计算最佳落子点
//        board[0][4] = AI;
//        board[5][5] = HUMAN;
//        board[0][3] =AI;
//        board[5][6] = HUMAN;
//        board[0][2] = AI;
//        board[0][1] = HUMAN;
//        int[] bestMove = findBestMove(board);
//        System.out.println("AI 最佳落子点: (" + bestMove[0] + ", " + bestMove[1] + ")");
//    }
}