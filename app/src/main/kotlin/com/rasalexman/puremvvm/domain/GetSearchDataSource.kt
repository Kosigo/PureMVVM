package com.rasalexman.puremvvm.domain

import androidx.paging.LivePagedListBuilder
import com.rasalexman.core.common.typealiases.PagedLiveData
import com.rasalexman.core.common.typealiases.ResultMutableLiveData
import com.rasalexman.core.domain.IUseCase
import com.rasalexman.data.repository.IMoviesRepository
import com.rasalexman.models.ui.MovieItemUI

class GetSearchDataSource(
    private val movieRepository: IMoviesRepository
) : IUseCase.DoubleInOut<String, ResultMutableLiveData<Boolean>, PagedLiveData<MovieItemUI>> {
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun execute(
        query: String,
        resultLiveData: ResultMutableLiveData<Boolean>
    ): PagedLiveData<MovieItemUI> {
        val searchDataSourceFactory = movieRepository.getRemoteSearchDataSource(query, resultLiveData).map { it.convertTo() }
        val pageLiveData = LivePagedListBuilder(searchDataSourceFactory, 20)
        return pageLiveData.build()
    }
}