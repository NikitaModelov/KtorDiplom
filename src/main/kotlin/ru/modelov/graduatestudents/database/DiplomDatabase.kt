package ru.modelov.graduatestudents.database

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object DiplomDatabase {

    const val DATABASE_NAME = "diplom"

    val database: CoroutineDatabase =
        KMongo.createClient("mongodb+srv://admin:admin@clusterdiplom.xz7cl.mongodb.net/myFirstDatabase?retryWrites=true&w=majority").coroutine.getDatabase(
            DATABASE_NAME
        )
}
