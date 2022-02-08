package xyz.savvamirzoyan.wordremember.domain.status

import xyz.savvamirzoyan.wordremember.presentation.state.DataInputState
import xyz.savvamirzoyan.wordremember.presentation.viewmodel.AddWordViewModel

sealed class AddWordStatus {

    sealed class Repeatable : AddWordStatus() {
        class Gender(val value: DataInputState) : AddWordStatus()
        class Word(val value: DataInputState) : AddWordStatus()
        class Translation(val value: DataInputState) : AddWordStatus()
        class Plural(val value: DataInputState) : AddWordStatus()
        class VerbFormsVisibility(val value: Int) : AddWordStatus()
        class OnlyPluralSwitchVisibility(val value: Int) : AddWordStatus()
        class SaveButtonIsEnabled(val value: Boolean) : AddWordStatus()
        class AdjectiveFormsVisibility(val value: Int) : AddWordStatus()
        class VerbForms(val value: AddWordViewModel.VerbFormStatus) : AddWordStatus()
    }

    sealed class Unrepeatable : AddWordStatus() {
        object ClearAllInput : AddWordStatus()
    }
}
