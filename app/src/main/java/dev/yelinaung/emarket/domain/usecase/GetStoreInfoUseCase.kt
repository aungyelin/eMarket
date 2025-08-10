package dev.yelinaung.emarket.domain.usecase

import dev.yelinaung.emarket.domain.model.StoreInfo
import dev.yelinaung.emarket.domain.repository.StoreRepository
import javax.inject.Inject

class GetStoreInfoUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(): Result<StoreInfo> {
        return storeRepository.getStoreInfo()
    }
}