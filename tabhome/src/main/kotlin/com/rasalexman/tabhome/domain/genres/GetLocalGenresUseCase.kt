package com.rasalexman.tabhome.domain.genres

import com.rasalexman.core.common.extensions.mapListTo
import com.rasalexman.core.data.dto.SResult
import com.rasalexman.core.domain.IUseCase
import com.rasalexman.data.repository.IGenresRepository
import com.rasalexman.models.ui.GenreItemUI

class GetLocalGenresUseCase(
    private val repository: IGenresRepository
) : IUseCase.Out<SResult<List<GenreItemUI>>> {
    override suspend fun execute(): SResult<List<GenreItemUI>> =
        repository.getLocalGenresList().mapListTo()
}