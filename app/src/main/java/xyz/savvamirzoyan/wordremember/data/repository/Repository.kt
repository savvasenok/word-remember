package xyz.savvamirzoyan.wordremember.data.repository

import xyz.savvamirzoyan.wordremember.data.database.AppDatabase

abstract class Repository {

    val db = AppDatabase.getInstance()
}