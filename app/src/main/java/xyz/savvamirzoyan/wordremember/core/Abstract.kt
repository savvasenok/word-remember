package xyz.savvamirzoyan.wordremember.core

abstract class Abstract {

    // All models should be inherited from this interface
    interface Object<T, M : Mapper> {
        fun map(mapper: M): T
    }

    // Object from above uses this Mapper interface.
    // Different realisations of this interface could transform
    // Abstract.Object to any other type
    interface Mapper {

//        interface Base<R, T> : Mapper {
//            fun map(data: R): T
//            fun map(e: Exception): T
//        }

        interface Empty : Mapper
    }
}