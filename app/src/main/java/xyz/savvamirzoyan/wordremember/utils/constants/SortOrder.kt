package xyz.savvamirzoyan.wordremember.utils.constants

sealed class SortOrder {
    object Alphabetical : SortOrder()
    object WordTypeOrGender : SortOrder()
}