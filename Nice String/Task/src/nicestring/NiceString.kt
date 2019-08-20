package nicestring

fun String.isNice(): Boolean {
    if (this.isEmpty() && this.isBlank()) return false
    val subs = listOf("bu", "ba", "be")

    val noBadString_old = !this.contains("ba") && !contains("bu") && !contains("be")

    fun checkSubs(): Boolean {
//        subs.map {
//            e -> if (this.contains(e)) return false
//        }
        subs.forEach { e -> if (this.contains(e)) {
            return false
        } }
        return true
    }

    val hasTreeVowels_old = count {
        it == 'a' || it == 'e' || it == 'i' || it == 'o' || it == 'u'
    } >= 3

    fun checkVowels(): Boolean {
        var count = 0
        fun counter(word: String): Int {
            val vowels = listOf('a', 'e', 'i', 'o', 'u')
            val chSeq = word.toCharArray()
            //chSeq.forEach { ch -> if (ch.isVowel() count++) }
            for (ch in chSeq) {
                if (vowels.contains(ch)) count += 1
                if (count == 3) return count
            }
            return count
        }
        return counter(this) >= 3
    }

    fun checkDoubles(): Boolean {
        var chPrev: Char
        chPrev = this[0]
        for (ch in this.subSequence(1, this.length )) {
            if (chPrev == ch) return true
            chPrev = ch
        }
        return false
    }

    /* start block instead of function
    fun checkDoubles()*/
    var hasDoubles = false
    if (length > 1) {
        var prevCh: Char? = null
        for (ch in this) {
            if (ch == prevCh) hasDoubles = true
            prevCh = ch
        }
    }
    // or: val hasDoubles =
    (0 until lastIndex).any { this[it] == this[it + 1] }
    // or: val hasDoubles =
    zipWithNext().any { it.first == it.second }

    /* end of block */

    val noBadString = subs.none { this.contains(it) }
    val hasTreeVowels = count { it in "aeiou" } >= 3
    val hasDouble2 = windowed(2).any { it[0] == it[1] }

    return listOf(noBadString, hasTreeVowels, hasDouble2).count { it == true } >= 2
}

