package xyz.savvamirzoyan.wordremember.domain

import android.app.Application
import timber.log.Timber
import xyz.savvamirzoyan.wordremember.contract.data.*
import xyz.savvamirzoyan.wordremember.data.database.AppDatabase
import xyz.savvamirzoyan.wordremember.data.repository.WordsListRepository
import xyz.savvamirzoyan.wordremember.domain.interactors.WordsListInteractor
import xyz.savvamirzoyan.wordremember.presentation.viewmodel.WordsListViewModel

class WordRememberApplication : Application() {

    lateinit var wordsListViewModel: WordsListViewModel

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        AppDatabase.getInstance(this)

        val verbWordWithVerbFormsDataToDomainMapper = VerbWordWithVerbFormsDataToDomainMapper()
        val nounWordDataToDomainMapper = NounWordDataToDomainMapper()
        val adjectiveWordDataToDomainMapper = AdjectiveWordDataToDomainMapper()


        val wordsListRepository = WordsListRepository.Base(
            verbWordWithVerbFormsDataToDomainMapper,
            nounWordDataToDomainMapper,
            adjectiveWordDataToDomainMapper
        )
        val nounWordDomainToUiMapper = NounWordDomainToUiMapper(applicationContext)
        val adjectiveWordDomainToUiMapper = AdjectiveWordDomainToUiMapper()
        val verbWordWithVerbFormsDomainToNoFormsUiMapper = VerbWordWithVerbFormsDomainToNoFormsUiMapper()

        val wordsListInteractor: WordsListInteractor = WordsListInteractor.Base(
            wordsListRepository,
            nounWordDomainToUiMapper,
            adjectiveWordDomainToUiMapper,
            verbWordWithVerbFormsDomainToNoFormsUiMapper
        )

        // ViewModels
        wordsListViewModel = WordsListViewModel(wordsListInteractor)
    }
}