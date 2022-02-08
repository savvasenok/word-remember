package xyz.savvamirzoyan.wordremember.core

interface Mapper<T, R> {
    fun map(data: T): R
}