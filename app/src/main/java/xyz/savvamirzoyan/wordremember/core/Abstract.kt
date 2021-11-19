package xyz.savvamirzoyan.wordremember.core

abstract class Abstract {

    // All models should be inherited from this interface
    interface Object<T, M : Mapper> {
        fun map(mapper: M): T
    }

    interface Mapper {
        interface Data<S, R> : Mapper {
            fun map(data: S): R
        }
    }
}