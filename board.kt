class Board {
  private val a = Array<Array<Int>>(4) { arrayOf(0, 0, 0, 0) } 

  private fun displayRow(s: StringBuilder, row: Int, 
			 form: String?, term: String) {
    for (col in 0 until 4) {
      if (form == null) {
	val m = a[row][col]
	if (m != 0) {
	  var ms = "   " + m.toString()
	  ms = ms.substring(ms.length - 3)
	  s.append(if (m < 1000) "|$ms " else "|$m")
	} else
	  s.append("|    ")
      } else
	s.append(form)
    }
    s.append(term)
    s.append('\n')
  }

  override fun toString(): String {
    val s = StringBuilder()
    for (row in 0 .. 3) {
      displayRow(s, row, "o----", "o")
      displayRow(s, row, "|    ", "|")
      displayRow(s, row, null, "|")
      displayRow(s, row, "|    ", "|")
    }
    displayRow(s, 3, "o----", "o")
    return s.toString()
  }

  // for debugging and testing
  constructor(vararg contents: Int) {
    if (contents.size != 0) {
      assert(contents.size == 15)
      for (row in 0 until 4)
        for (col in 0 until 4)
          a[row][col] = contents[4 * row + col]
    }
  }  

  // for debugging and testing
  fun toList(): List<Int> = a.flatMap { it.toList() }

  fun cell(row: Int, col: Int): Int = a[row][col]

  // is the board completely filled?
  fun isFull(): Boolean = a.any{0 !in it}
  
  fun insert() {
	val random = java.util.Random()
	val list = mutableListOf<Pair<Int, Int>>()
	for (i in 0..3)
	  for (j in 0..3)
	    if (cell(i, j) == 0) list.add(Pair(i, j))
    val index = random.nextInt(list.size)
	val probab = listOf(2,2,2,2,2,2,2,2,2,4)
	a[list[index].first][list[index].second] = probab[random.nextInt(probab.size)]
  }
	
  fun pushLeft(): Int {
    var sum = 0
    for (i in 0..3) {
      for (j in 0..3) {
	    if (cell(i,j) == 0) {
	      var k = 0
	      while (cell(i, j+k) == 0 && j+k < 3)
		    k+=1
 	      a[i][j] += a[i][j+k]
		  a[i][j+k] = 0
	    }
      }
	  for (j in 0..2)
	    if (cell(i,j) == cell(i,j+1) && cell(i,j) != 0) {
	      var n = 2
	      a[i][j] += a[i][j+1]
		  sum += a[i][j]
	      while (j+n < 4) {
	        a[i][j+n-1] = a[i][j+n]
		    n+=1
		  }
          a[i][3] = 0
	    }
    }
    return sum	  
  }

  fun mirror() {
    for (i in 0..3)
      for (j in 0..1) {
	    var n = 0
	    n+= a[i][j]
	    a[i][j] = a[i][3-j]
	    a[i][3-j] = n
	  }
  }
  fun pushRight(): Int {
    mirror()
    val sum = pushLeft()
    mirror()
	return sum
  }

  fun transpose() {
    for (i in 0..3)
      for (j in i..3) 
	    if (i != j) {
		  var n = 0
	      n += a[i][j]
	      a[i][j] = a[j][i]
	      a[j][i] = n
	    }
  }

  fun pushUp(): Int {
    transpose()
    val sum = pushLeft()
    transpose()
    return sum
  }

  fun pushDown(): Int {
    transpose()
    val sum = pushRight()
    transpose()
    return sum
  }

  // pushes in direction ch (in 'lrud')
  // returns number of points
  fun push(ch: Char): Int = when(ch) {
    'l' -> pushLeft()
    'r' -> pushRight()
    'u' -> pushUp()
    'd' -> pushDown()
    else -> 0
  }
}