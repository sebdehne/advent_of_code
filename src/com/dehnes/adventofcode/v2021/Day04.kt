package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day04 {

    val bingoInputNumbers = getLines().first().split(",").map { it.toInt() }

    val boards: List<List<List<Int>>>

    init {
        val boards: MutableList<List<List<Int>>> = mutableListOf()
        val currentBoard: MutableList<List<Int>> = mutableListOf()
        getLines().drop(1).forEach { boardLine ->
            if (boardLine.isBlank()) {
                if (currentBoard.isNotEmpty()) {
                    boards.add(currentBoard.toMutableList().toList())
                    currentBoard.clear()
                }
                return@forEach
            }
            currentBoard.add(boardLine.split(" ").filterNot { it.isBlank() }.map { it.toInt() })
        }
        if (currentBoard.isNotEmpty()) {
            boards.add(currentBoard.toMutableList().toList())
            currentBoard.clear()
        }
        this.boards = boards
    }

    @Test
    fun run() {
        check(getScoreAtWin(getAllWins().first()) == 21607)
        check(getScoreAtWin(getAllWins().last()) == 19012)
    }

    fun getScoreAtWin(run: Int): Int {
        var score = -1
        playBingo { r, _, s ->
            if (r == run) {
                score = s
                return@playBingo
            }
        }
        return score
    }

    fun getAllWins(): List<Int> {
        val wins = mutableListOf<Int>()
        playBingo { run, _, _ ->
            wins.add(run)
        }
        return wins.toList()
    }

    fun playBingo(onNewWin: (run: Int, board: Int, score: Int) -> Unit) {
        val markingsRows = Array(boards.size) { Array(5) { IntArray(5) } }
        val markingsColumns = Array(boards.size) { Array(5) { IntArray(5) } }

        val sumUnmarked = { winningBoardIndex: Int ->
            var score = 0
            for (row in 0 until 5) {
                for (column in 0 until 5) {
                    if (markingsRows[winningBoardIndex][row][column] == 0) {
                        score += boards[winningBoardIndex][row][column]
                    }
                }
            }
            score
        }

        val boardsAlreadyWon = mutableSetOf<Int>()
        bingoInputNumbers.forEachIndexed { run, bingoNumber ->

            // mark
            boards.forEachIndexed { bIndex, board ->
                board.forEachIndexed { rowIndex, line ->
                    line.forEachIndexed { columnIndex, number ->
                        if (number == bingoNumber) {
                            markingsRows[bIndex][rowIndex][columnIndex] = 1
                            markingsColumns[bIndex][columnIndex][rowIndex] = 1
                        }
                    }
                }
            }

            // check for winning board
            for (board in boards.indices) {
                if (board !in boardsAlreadyWon && (markingsRows[board].any { it.sum() == 5 } || markingsColumns[board].any { it.sum() == 5 })) {
                    boardsAlreadyWon.add(board)
                    onNewWin(run, board, bingoNumber * sumUnmarked(board))
                }
            }
        }
    }

}