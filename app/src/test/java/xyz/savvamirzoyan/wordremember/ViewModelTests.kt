package xyz.savvamirzoyan.wordremember

import android.view.View
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.savvamirzoyan.wordremember.contract.repository.IAddWordRepository
import xyz.savvamirzoyan.wordremember.data.state.DataInputState
import xyz.savvamirzoyan.wordremember.testimpl.TestAddWordRepository
import xyz.savvamirzoyan.wordremember.viewmodel.AddWordViewModel

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ViewModelTests {

    private val testAddWordRepository: IAddWordRepository = TestAddWordRepository()

    @Test
    fun `gender DER`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onGenderChange("DER")
        assertEquals(
            DataInputState(true, null, R.string.add_word_required),
            viewModel.genderStatusFlow.value
        )
    }

    @Test
    fun `gender DIE`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onGenderChange("der")
        assertEquals(
            DataInputState(true, null, R.string.add_word_required),
            viewModel.genderStatusFlow.value
        )
    }

    @Test
    fun `gender DAS`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onGenderChange("der")
        assertEquals(
            DataInputState(true, null, R.string.add_word_required),
            viewModel.genderStatusFlow.value
        )
    }

    @Test
    fun `gender incorrect`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onGenderChange("Hello World!")
        assertEquals(
            DataInputState(true, R.string.input_error_no_gender, R.string.add_word_not_required),
            viewModel.genderStatusFlow.value
        )
    }

    @Test
    fun `word has symbols`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onWordChange("Hello World!")
        assertEquals(
            DataInputState(true, null, R.string.add_word_required),
            viewModel.wordStatusFlow.value
        )
    }

    @Test
    fun `word is empty`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onWordChange("")
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_not_be_empty,
                R.string.add_word_not_required
            ),
            viewModel.wordStatusFlow.value
        )
    }

    @Test
    fun `word is blank`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onWordChange("    ")
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_not_be_empty,
                R.string.add_word_not_required
            ),
            viewModel.wordStatusFlow.value
        )
    }

    @Test
    fun `word type is NOUN && gender is null`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onGenderChange("Hello World!")
        viewModel.onWordTypeNounChange(true)
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_no_gender,
                R.string.add_word_not_required
            ),
            viewModel.genderStatusFlow.value
        )
    }

    @Test
    fun `word type is NOUN && gender is not null`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onGenderChange("DAS")
        viewModel.onWordTypeNounChange(true)
        assertEquals(
            DataInputState(
                true,
                null,
                R.string.add_word_required
            ),
            viewModel.genderStatusFlow.value
        )
    }

    @Test
    fun `word type is NOUN && word is empty`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onWordChange("")
        viewModel.onWordTypeNounChange(true)
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_not_be_empty,
                R.string.add_word_not_required
            ),
            viewModel.wordStatusFlow.value
        )
    }

    @Test
    fun `word type is NOUN && word is not empty`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onWordChange("Hello World!")
        viewModel.onWordTypeNounChange(true)
        assertEquals(
            DataInputState(
                true,
                null,
                R.string.add_word_required
            ),
            viewModel.wordStatusFlow.value
        )
    }

    @Test
    fun `word type is NOUN && word is only plural && plural is empty`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onWordTypeNounChange(true)
        viewModel.onWordPluralFormChange("")
        viewModel.onOnlyPluralChange(true)
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_have_plural,
                R.string.add_word_not_required
            ),
            viewModel.wordPluralFormStatusFlow.value
        )
    }

    @Test
    fun `word type is NOUN && word is only plural && plural is not empty`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onWordTypeNounChange(true)
        viewModel.onWordPluralFormChange("Hello World!")
        viewModel.onOnlyPluralChange(true)
        assertEquals(
            DataInputState(
                true,
                null,
                R.string.add_word_required
            ),
            viewModel.wordPluralFormStatusFlow.value
        )
    }

    @Test
    fun `translation empty`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onTranslationChange("")
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_have_translation,
                R.string.add_word_not_required
            ),
            viewModel.translationStatusFlow.value
        )
    }

    @Test
    fun `word type is VERB && hide gender`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onWordTypeVerbChange(true)
        assertEquals(
            DataInputState(
                false, null, R.string.add_word_not_required, View.GONE

            ),
            viewModel.genderStatusFlow.value
        )
    }

    @Test
    fun `word type is VERB && hide plural`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onWordTypeVerbChange(true)
        assertEquals(
            DataInputState(
                false, null, R.string.add_word_not_required, View.GONE

            ),
            viewModel.wordPluralFormStatusFlow.value
        )
        assertEquals(
            View.GONE,
            viewModel.onlyPluralSwitchVisibilityStatusFlow.value
        )
    }

    @Test
    fun `word type is VERB && show verb forms`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onWordTypeVerbChange(true)
        viewModel.onWordChange("Hello World!")
        assertEquals(
            DataInputState(
                true, null, R.string.add_word_required
            ),
            viewModel.wordStatusFlow.value
        )
        viewModel.onWordChange("")
        assertEquals(
            DataInputState(
                true, R.string.input_error_word_must_not_be_empty, R.string.add_word_not_required
            ),
            viewModel.wordStatusFlow.value
        )
        assertEquals(
            View.VISIBLE,
            viewModel.verbFormsVisibilityStatusFlow.value
        )
    }

    @Test
    fun `word type is ADJECTIVE`() = runBlocking {
        val viewModel = AddWordViewModel(testAddWordRepository)

        viewModel.onWordTypeAdjectiveChange(true)
        assertEquals(
            DataInputState(false, null, R.string.add_word_not_required, View.GONE),
            viewModel.genderStatusFlow.value
        )

        viewModel.onWordChange("")
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_not_be_empty,
                R.string.add_word_not_required
            ),
            viewModel.wordStatusFlow.value
        )

        viewModel.onWordChange("Hello World!")
        assertEquals(
            DataInputState(
                true,
                null,
                R.string.add_word_required
            ),
            viewModel.wordStatusFlow.value
        )
    }
}