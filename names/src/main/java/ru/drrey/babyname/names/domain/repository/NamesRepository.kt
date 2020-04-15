package ru.drrey.babyname.names.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.names.domain.entity.Name

/**
 * Name repository
 */
interface NamesRepository {
    fun getNames(): Single<List<Name>>
    fun getStars(userId: String): Single<List<NameStars>>
    fun setStars(userId: String, name: Name, stars: Int): Completable
    fun getSexFilter(userId: String): Single<String>
    fun setSexFilter(userId: String, sex: String): Completable
}