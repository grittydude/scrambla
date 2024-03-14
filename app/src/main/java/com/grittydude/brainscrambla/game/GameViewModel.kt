package com.grittydude.brainscrambla.game
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grittydude.brainscrambla.datasource.INCREASE_SCORE
import com.grittydude.brainscrambla.datasource.MAX_NO_WORDS

class GameViewModel : ViewModel() {

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private val _timer = MutableLiveData(0)
    val timer: LiveData<Int> get() = _timer

    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String> get() = _currentScrambledWord

    private val wordsList: MutableList<String>? = mutableListOf()

    private lateinit var currentWord: String

    private var category: List<String>

    fun hint(): String {
        return currentWord
    }

    private fun increaseScore() {
        _score.value = _score.value?.plus(INCREASE_SCORE)
    }

    fun getNextWord(myCategory: List<String>) {
        category = myCategory
        currentWord = myCategory.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()
        while (tempWord.toString().equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList!!.contains(currentWord)) {
            getNextWord(category)
        } else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }
    }

    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_WORDS) {
            getNextWord(category)
            true
        } else false
    }


    fun isWordCorrect(word: String): Boolean {
        if (word.equals(currentWord, true)) {

            increaseScore()
            return true
        }
        return false
    }

    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList?.clear()
        getNextWord(category)
    }


    init {
        category = listOf(String())
    }

}