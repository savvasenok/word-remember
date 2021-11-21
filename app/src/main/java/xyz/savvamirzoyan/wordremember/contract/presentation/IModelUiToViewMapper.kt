package xyz.savvamirzoyan.wordremember.contract.presentation

import android.content.Context

interface IModelUiToViewMapper<T> {
    fun map(view: T, context: Context?)
}