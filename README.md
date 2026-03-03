# Ultimate Tic-Tac-Toe
A Java implementation of <b>Ultimate Tic-Tac-Toe</b> - a board game composed of nine tic-tac-toe boards arragned in a 3x3 grid. Every move on a small board decides where your opponent must play next on the larger board.

# Rules of Ultimate Tic-Tac-Toe
The rules that make this game strategically deep:
<ul>
  <li>Each small board is a standard game of Tic-Tac-Toe</li>
  <li>Your move on a small board determines which small board your opponent must play in next. For example, if you play in the center-right cell of any small board, your opponent must play their next move in the center-right small board.</li>
  <li>If you make your move, but the small board your opponent should play next is already won, he can choose <b>any</b> open small board.</li>
  <li>To overall win game: win three small boards in a row (either horizontally, vertically, or diagonally)</li>
</ul>

# Features
