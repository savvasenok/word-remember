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
class ViewModelTests {

    private val testAddWordRepository: IAddWordRepository = TestAddWordRepository()

    @get:Rule
    val coroutineRule = TestCoroutineRules()


    @Test
    fun `gender DER`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onGenderChange("der")
        assertEquals(
            AddWordStatus.Repeatable.Gender(
                DataInputState(
                    true,
                    null,
                    R.string.add_word_required
                )
            ).value,
            (testResults[1] as AddWordStatus.Repeatable.Gender).value
        )
        assertEquals(
            AddWordStatus.Repeatable.SaveButtonIsEnabled(false).value,
            (testResults[2] as AddWordStatus.Repeatable.SaveButtonIsEnabled).value
        )

        job.cancel()
    }

    @Test
    fun `gender DIE`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onGenderChange("die")
        assertEquals(
            AddWordStatus.Repeatable.Gender(
                DataInputState(
                    true,
                    null,
                    R.string.add_word_required
                )
            ).value,
            (testResults[1] as AddWordStatus.Repeatable.Gender).value
        )
        assertEquals(
            AddWordStatus.Repeatable.SaveButtonIsEnabled(false).value,
            (testResults[2] as AddWordStatus.Repeatable.SaveButtonIsEnabled).value
        )

        job.cancel()
    }

    @Test
    fun `gender DAS`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onGenderChange("das")
        assertEquals(
            AddWordStatus.Repeatable.Gender(
                DataInputState(
                    true,
                    null,
                    R.string.add_word_required
                )
            ).value,
            (testResults[1] as AddWordStatus.Repeatable.Gender).value
        )
        assertEquals(
            AddWordStatus.Repeatable.SaveButtonIsEnabled(false).value,
            (testResults[2] as AddWordStatus.Repeatable.SaveButtonIsEnabled).value
        )

        job.cancel()
    }

    @Test
    fun `gender incorrect`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onGenderChange("Hello World!")
        assertEquals(
            AddWordStatus.Repeatable.Gender(
                DataInputState(
                    true,
                    R.string.input_error_no_gender,
                    R.string.add_word_not_required
                )
            ).value,
            (testResults[1] as AddWordStatus.Repeatable.Gender).value
        )
        assertEquals(
            AddWordStatus.Repeatable.SaveButtonIsEnabled(false).value,
            (testResults[2] as AddWordStatus.Repeatable.SaveButtonIsEnabled).value
        )

        job.cancel()
    }

    @Test
    fun `word has symbols`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordChange("Hello World!")
        assertEquals(
            DataInputState(true, null, R.string.add_word_required),
            (testResults[1] as AddWordStatus.Repeatable.Word).value
        )

        job.cancel()
    }

    @Test
    fun `word is empty`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordChange("")
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_not_be_empty,
                R.string.add_word_not_required
            ),
            (testResults[1] as AddWordStatus.Repeatable.Word).value
        )

        job.cancel()
    }

    @Test
    fun `word is blank`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordChange("    ")
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_not_be_empty,
                R.string.add_word_not_required
            ),
            (testResults[1] as AddWordStatus.Repeatable.Word).value
        )

        job.cancel()
    }

    @Test
    fun `word type is NOUN && gender is null`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onGenderChange("Hello World!")
        viewModel.onWordTypeNounChange(true)
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_no_gender,
                R.string.add_word_not_required
            ),
            (testResults[1] as AddWordStatus.Repeatable.Gender).value
        )

        job.cancel()
    }

    @Test
    fun `word type is NOUN && gender is not null`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onGenderChange("DAS")
        viewModel.onWordTypeNounChange(true)
        assertEquals(
            DataInputState(
                true,
                null,
                R.string.add_word_required
            ),
            (testResults[1] as AddWordStatus.Repeatable.Gender).value
        )

        job.cancel()
    }

    @Test
    fun `word type is NOUN && word is empty`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordChange("")
        viewModel.onWordTypeNounChange(true)
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_not_be_empty,
                R.string.add_word_not_required
            ),
            (testResults[1] as AddWordStatus.Repeatable.Word).value
        )
        assertEquals(
            false,
            (testResults[2] as AddWordStatus.Repeatable.SaveButtonIsEnabled).value
        )
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_no_gender,
                R.string.add_word_not_required
            ),
            (testResults[3] as AddWordStatus.Repeatable.Gender).value
        )

        // 4: Word
        // 5: VerbFormsVisibility

        job.cancel()
    }

    @Test
    fun `word type is NOUN && word is not empty`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordChange("Hello World!")
        viewModel.onWordTypeNounChange(true)
        assertEquals(
            DataInputState(
                true,
                null,
                R.string.add_word_required
            ),
            (testResults[1] as AddWordStatus.Repeatable.Word).value
        )

        job.cancel()
    }

    @Test
    fun `word type is NOUN && word is only plural && plural is empty`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeNounChange(true)
        viewModel.onWordPluralFormChange("")
        viewModel.onOnlyPluralChange(true)
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_have_plural,
                R.string.add_word_not_required
            ),
            (testResults[11] as AddWordStatus.Repeatable.Plural).value
        )

        job.cancel()
    }

    @Test
    fun `word type is NOUN && word is only plural && plural is not empty`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeNounChange(true)
        viewModel.onWordPluralFormChange("Hello World!")
        viewModel.onOnlyPluralChange(true)
        assertEquals(
            DataInputState(
                true,
                null,
                R.string.add_word_required
            ),
            (testResults[11] as AddWordStatus.Repeatable.Plural).value
        )

        job.cancel()
    }

    @Test
    fun `translation empty`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onTranslationChange("")
        assertEquals(
            DataInputState(
                true,
                R.string.input_error_word_must_have_translation,
                R.string.add_word_not_required
            ),

            (testResults[1] as AddWordStatus.Repeatable.Translation).value
        )

        job.cancel()
    }

    @Test
    fun `word type is VERB && hide gender`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeVerbChange(true)
        assertEquals(
            DataInputState(
                false, null, R.string.add_word_not_required, View.GONE

            ),
            (testResults[2] as AddWordStatus.Repeatable.Gender).value
        )

        job.cancel()
    }

    @Test
    fun `word type is VERB && hide plural`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeVerbChange(true)
        assertEquals(
            DataInputState(
                false,
                null,
                R.string.add_word_not_required,
                View.GONE
            ),
            (testResults[3] as AddWordStatus.Repeatable.Plural).value
        )
        assertEquals(
            View.GONE,
            (testResults[6] as AddWordStatus.Repeatable.OnlyPluralSwitchVisibility).value
        )

        job.cancel()
    }

    @Test
    fun `word type is VERB && show verb forms`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeVerbChange(true)
        viewModel.onWordChange("Hello World!")
        assertEquals(
            View.VISIBLE,
            (testResults[4] as AddWordStatus.Repeatable.VerbFormsVisibility).value
        )
        assertEquals(
            DataInputState(
                true, null, R.string.add_word_required
            ),
            (testResults[8] as AddWordStatus.Repeatable.Word).value
        )
        viewModel.onWordChange("")
        assertEquals(
            DataInputState(
                true, R.string.input_error_word_must_not_be_empty, R.string.add_word_not_required
            ),
            (testResults[11] as AddWordStatus.Repeatable.Word).value
        )

        job.cancel()
    }

    @Test
    fun `word type is ADJECTIVE`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
        val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeAdjectiveChange(true)
        assertEquals(
            DataInputState(
                false,
                null,
                R.string.add_word_not_required,
                View.GONE
            ),
            (testResults[1] as AddWordStatus.Repeatable.Gender).value
        )

        job.cancel()
    }

    /* @Test
    fun `verb generate GEHEN`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeVerbChange(true)
        viewModel.onWordChange("gehen")
        assertEquals(
            AddWordViewModel.VerbFormStatus(
                "gehe",
                "gehst",
                "geht",
                "gehen",
                "geht",
                "gehen"
            ),
            viewModel.addWordStatusFlow.value
        )
    }

    @Test
    fun `verb generate ARBEITEN`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeVerbChange(true)
        viewModel.onWordChange("arbeiten")
        assertEquals(
            AddWordViewModel.VerbFormStatus(
                "arbeite",
                "arbeitest",
                "arbeitet",
                "arbeiten",
                "arbeitet",
                "arbeiten"
            ),
            viewModel.addWordStatusFlow.value
        )
    }

    @Test
    fun `verb generate BILDEN`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeVerbChange(true)
        viewModel.onWordChange("bilden")
        assertEquals(
            AddWordViewModel.VerbFormStatus(
                "bilde",
                "bildest",
                "bildet",
                "bilden",
                "bildet",
                "bilden"
            ),
            viewModel.addWordStatusFlow.value
        )
    }

    @Test
    fun `verb generate ATMEN`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeVerbChange(true)
        viewModel.onWordChange("atmen")
        assertEquals(
            AddWordViewModel.VerbFormStatus(
                "atme",
                "atmest",
                "atmet",
                "atmen",
                "atmet",
                "atmen"
            ),
            viewModel.addWordStatusFlow.value
        )
    }

    @Test
    fun `verb generate RECHNEN`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeVerbChange(true)
        viewModel.onWordChange("rechnen")
        assertEquals(
            AddWordViewModel.VerbFormStatus(
                "rechne",
                "rechnest",
                "rechnet",
                "rechnen",
                "rechnet",
                "rechnen"
            ),
            viewModel.addWordStatusFlow.value
        )
    }

    @Test
    fun `verb generate HEISSEN`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeVerbChange(true)
        viewModel.onWordChange("heissen")
        assertEquals(
            AddWordViewModel.VerbFormStatus(
                "heisse",
                "heisst",
                "heisst",
                "heissen",
                "heisst",
                "heissen"
            ),
            viewModel.addWordStatusFlow.value
        )
    }

    @Test
    fun `verb generate HEIZEN`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeVerbChange(true)
        viewModel.onWordChange("heizen")
        assertEquals(
            AddWordViewModel.VerbFormStatus(
                "heize",
                "heizt",
                "heizt",
                "heizen",
                "heizt",
                "heizen"
            ),
            viewModel.addWordStatusFlow.value
        )
    }

    @Test
    fun `verb generate REISEN`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeVerbChange(true)
        viewModel.onWordChange("reisen")
        assertEquals(
            AddWordViewModel.VerbFormStatus(
                "reise",
                "reist",
                "reist",
                "reisen",
                "reist",
                "reisen"
            ),
            viewModel.addWordStatusFlow.value
        )
    }

    @Test
    fun `verb generate DAUERN`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeVerbChange(true)
        viewModel.onWordChange("dauern")
        assertEquals(
            AddWordViewModel.VerbFormStatus(
                "dauere",
                "dauerst",
                "dauert",
                "dauern",
                "dauert",
                "dauern"
            ),
            viewModel.addWordStatusFlow.value as AddWordStatus.Repeatable.VerbForms
        )
    }

    @Test
    fun `verb generate KLINGELN`() = runBlockingTest {
        val viewModel = AddWordViewModel(testAddWordRepository)
val testResults = mutableListOf<AddWordStatus?>()

        val job = launch {
            viewModel.addWordStatusFlow.toList(testResults)
        }

        viewModel.onWordTypeVerbChange(true)
        viewModel.onWordChange("klingeln")
        assertEquals(
            AddWordViewModel.VerbFormStatus(
                "klingele",
                "klingelst",
                "klingelt",
                "klingeln",
                "klingelt",
                "klingeln"
            ),
            (viewModel.addWordStatusFlow.value as AddWordStatus.Repeatable.Word).value
        )
    }*/
}