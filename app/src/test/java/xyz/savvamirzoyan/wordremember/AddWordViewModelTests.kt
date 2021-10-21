package xyz.savvamirzoyan.wordremember

import android.view.View
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import xyz.savvamirzoyan.wordremember.contract.repository.IAddWordRepository
import xyz.savvamirzoyan.wordremember.data.state.DataInputState
import xyz.savvamirzoyan.wordremember.data.status.AddWordStatus
import xyz.savvamirzoyan.wordremember.testimpl.TestAddWordRepository
import xyz.savvamirzoyan.wordremember.viewmodel.AddWordViewModel

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalCoroutinesApi
class AddWordViewModelTests {

    private val testAddWordRepository: IAddWordRepository = TestAddWordRepository()

    @get:Rule
    val coroutineRule = TestCoroutineRules()

    @Test
    fun `gender DER`() = runFlowTest { viewModel, results ->
        viewModel.onGenderChange("der")
        assertEquals(
            AddWordStatus.Repeatable.Gender(
                DataInputState(
                    true,
                    null,
                    R.string.add_word_required
                )
            ).value,
            (results(0) as AddWordStatus.Repeatable.Gender).value
        )
        assertEquals(
            AddWordStatus.Repeatable.SaveButtonIsEnabled(false).value,
            (results(1) as AddWordStatus.Repeatable.SaveButtonIsEnabled).value
        )
    }

    @Test
    fun `gender DIE`() = runFlowTest { viewModel, results ->
        viewModel.onGenderChange("die")
        assertEquals(
            AddWordStatus.Repeatable.Gender(
                DataInputState(
                    true,
                    null,
                    R.string.add_word_required
                )
            ).value,
            (results(0) as AddWordStatus.Repeatable.Gender).value
        )
        assertEquals(
            AddWordStatus.Repeatable.SaveButtonIsEnabled(false).value,
            (results(1) as AddWordStatus.Repeatable.SaveButtonIsEnabled).value
        )
    }

    @Test
    fun `gender DAS`() = runFlowTest { viewModel, results ->
        viewModel.onGenderChange("das")
        assertEquals(
            AddWordStatus.Repeatable.Gender(
                DataInputState(
                    true,
                    null,
                    R.string.add_word_required
                )
            ).value,
            (results(0) as AddWordStatus.Repeatable.Gender).value
        )
        assertEquals(
            AddWordStatus.Repeatable.SaveButtonIsEnabled(false).value,
            (results(1) as AddWordStatus.Repeatable.SaveButtonIsEnabled).value
        )
    }

    @Test
    fun `gender incorrect`() = runFlowTest { viewModel, results ->
        viewModel.onGenderChange("Hello World!")
        assertEquals(
            AddWordStatus.Repeatable.Gender(
                DataInputState(
                    true,
                    R.string.input_error_no_gender,
                    R.string.add_word_not_required
                )
            ).value,
            (results(0) as AddWordStatus.Repeatable.Gender).value
        )
        assertEquals(
            AddWordStatus.Repeatable.SaveButtonIsEnabled(false).value,
            (results(1) as AddWordStatus.Repeatable.SaveButtonIsEnabled).value
        )
    }

    @Test
    fun `word has symbols`() = runFlowTest { viewModel, results ->
        viewModel.onWordChange("Hello World!")
        assertEquals(
            DataInputState(true, null, R.string.add_word_required),
            (results(0) as AddWordStatus.Repeatable.Word).value
        )
    }

    @Test
    fun `word is empty`() = runFlowTest { viewModel, results ->
        viewModel.onWordChange("")
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_not_be_empty,
                R.string.add_word_not_required
            ),
            (results(0) as AddWordStatus.Repeatable.Word).value
        )
    }

    @Test
    fun `word is blank`() = runFlowTest { viewModel, results ->
        viewModel.onWordChange("    ")
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_not_be_empty,
                R.string.add_word_not_required
            ),
            (results(0) as AddWordStatus.Repeatable.Word).value
        )
    }

    @Test
    fun `word type is NOUN && gender is null`() = runFlowTest { viewModel, results ->
        viewModel.onGenderChange("Hello World!")
        viewModel.onWordTypeNounChange(true)
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_no_gender,
                R.string.add_word_not_required
            ),
            (results(0) as AddWordStatus.Repeatable.Gender).value
        )
    }

    @Test
    fun `word type is NOUN && gender is not null`() = runFlowTest { viewModel, results ->
        viewModel.onGenderChange("DAS")
        viewModel.onWordTypeNounChange(true)
        assertEquals(
            DataInputState(
                true,
                null,
                R.string.add_word_required
            ),
            (results(0) as AddWordStatus.Repeatable.Gender).value
        )
    }

    @Test
    fun `word type is NOUN && word is empty`() = runFlowTest { viewModel, results ->
        viewModel.onWordChange("")
        viewModel.onWordTypeNounChange(true)
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_not_be_empty,
                R.string.add_word_not_required
            ),
            (results(0) as AddWordStatus.Repeatable.Word).value
        )
        assertEquals(
            false,
            (results(1) as AddWordStatus.Repeatable.SaveButtonIsEnabled).value
        )
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_no_gender,
                R.string.add_word_not_required
            ),
            (results(2) as AddWordStatus.Repeatable.Gender).value
        )
    }

    @Test
    fun `word type is NOUN && word is not empty`() = runFlowTest { viewModel, results ->
        viewModel.onWordChange("Hello World!")
        viewModel.onWordTypeNounChange(true)
        assertEquals(
            DataInputState(
                true,
                null,
                R.string.add_word_required
            ),
            (results(0) as AddWordStatus.Repeatable.Word).value
        )
    }

    @Test
    fun `word type is NOUN && word is only plural && plural is empty`() =
        runFlowTest { viewModel, results ->
            viewModel.onWordTypeNounChange(true)
            viewModel.onWordPluralFormChange("")
            viewModel.onOnlyPluralChange(true)
            assertEquals(
                DataInputState(
                    true,
                    R.string.input_error_word_must_have_plural,
                    R.string.add_word_not_required
                ),
                (results(10) as AddWordStatus.Repeatable.Plural).value
            )
        }

    @Test
    fun `word type is NOUN && word is only plural && plural is not empty`() =
        runFlowTest { viewModel, results ->
            viewModel.onWordTypeNounChange(true)
            viewModel.onWordPluralFormChange("Hello World!")
            viewModel.onOnlyPluralChange(true)
            assertEquals(
                DataInputState(
                    true,
                    null,
                    R.string.add_word_required
                ),
                (results(10) as AddWordStatus.Repeatable.Plural).value
            )
        }

    @Test
    fun `translation empty`() = runFlowTest { viewModel, results ->
        viewModel.onTranslationChange("")
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_have_translation,
                R.string.add_word_not_required
            ),
            (results(0) as AddWordStatus.Repeatable.Translation).value
        )
    }

    @Test
    fun `word type is VERB && hide gender`() = runFlowTest { viewModel, results ->
        viewModel.onWordTypeVerbChange(true)
        assertEquals(
            DataInputState(
                false, null, R.string.add_word_not_required, View.GONE

            ),
            (results(1) as AddWordStatus.Repeatable.Gender).value
        )
    }

    @Test
    fun `word type is VERB && hide plural`() = runFlowTest { viewModel, results ->
        viewModel.onWordTypeVerbChange(true)
        assertEquals(
            DataInputState(
                false,
                null,
                R.string.add_word_not_required,
                View.GONE
            ),
            (results(2) as AddWordStatus.Repeatable.Plural).value
        )
        assertEquals(
            View.GONE,
            (results(5) as AddWordStatus.Repeatable.OnlyPluralSwitchVisibility).value
        )
    }

    @Test
    fun `word type is VERB && show verb forms`() = runFlowTest { viewModel, results ->
        viewModel.onWordTypeVerbChange(true)
        viewModel.onWordChange("Hello World!")
        assertEquals(
            View.VISIBLE,
            (results(3) as AddWordStatus.Repeatable.VerbFormsVisibility).value
        )
        assertEquals(
            DataInputState(
                true, null, R.string.add_word_required
            ),
            (results(7) as AddWordStatus.Repeatable.Word).value
        )
        viewModel.onWordChange("")
        assertEquals(
            DataInputState(
                true, R.string.input_error_word_must_not_be_empty, R.string.add_word_not_required
            ),
            (results(10) as AddWordStatus.Repeatable.Word).value
        )
    }

    @Test
    fun `word type is ADJECTIVE`() = runFlowTest { viewModel, results ->
        viewModel.onWordTypeAdjectiveChange(true)
        assertEquals(
            DataInputState(
                false,
                null,
                R.string.add_word_not_required,
                View.GONE
            ),
            (results(0) as AddWordStatus.Repeatable.Gender).value
        )
    }

    private fun runFlowTest(
        function: (AddWordViewModel, (Int) -> AddWordStatus) -> Unit
    ) = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()
        val resultsGetter = { index: Int -> testResults.toList().filterNotNull()[index] }
        val job = launch { viewModel.addWordStatusFlow.toList(testResults) }

        function(viewModel, resultsGetter)
        job.cancel()
    }
}