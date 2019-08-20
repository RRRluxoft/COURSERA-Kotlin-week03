package nicestring

fun String.isNice(): Boolean {
    if (this.isEmpty() && this.isBlank()) return false

    var checksSuccess = 0
    val subs = listOf("bu", "ba", "be")

    val noBadString = !contains("ba") && !contains("bu") && !contains("be")

    fun checkSubs(): Boolean {
//        subs.map {
//            e -> if (this.contains(e)) return false
//        }
        subs.forEach { e -> if (this.contains(e)) {
            return false
        } }
        return true
    }

    val hasTreeVowels = count {
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
        var currentCh: Char
        val word = this
        chPrev = word[0]
        for (ch in word.subSequence(1, word.length )) {
            currentCh = ch
            if (chPrev == currentCh) return true
            chPrev = currentCh
        }
        return false
    }

//    if (checkSubs()) { checksSuccess += 1 }
    if (noBadString) { checksSuccess += 1 }
//    if (checkVowels())  { checksSuccess += 1 }
    if (hasTreeVowels)  { checksSuccess += 1 }
    if (checkDoubles())  { checksSuccess += 1 }

    return checksSuccess >= 2
}

