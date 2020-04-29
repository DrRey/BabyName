package ru.drrey.babyname.names.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.names.domain.entity.Name

/**
 * Name repository
 */
interface NamesRepository {
    fun getNames(): Flow<List<Name>>
    fun getStars(userId: String): Flow<List<NameStars>>
    fun setStars(userId: String, name: Name, stars: Int): Flow<Nothing>
    fun getSexFilter(userId: String): Flow<Sex?>
    fun setSexFilter(userId: String, sex: Sex?): Flow<Nothing>
}