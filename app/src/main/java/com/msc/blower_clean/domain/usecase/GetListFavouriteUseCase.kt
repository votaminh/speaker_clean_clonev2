package com.msc.blower_clean.domain.usecase

import com.msc.blower_clean.data.repositories.FavouriteSoundRepository
import com.msc.blower_clean.domain.layer.DetailsSound
import javax.inject.Inject

class GetListFavouriteUseCase @Inject constructor(private val favouriteSoundRepository: FavouriteSoundRepository)
    : UseCase<GetListFavouriteUseCase.Param, List<DetailsSound>>() {
    class Param : UseCase.Param()

    override suspend fun execute(param: Param): List<DetailsSound> {
        return favouriteSoundRepository.getList()
    }
}