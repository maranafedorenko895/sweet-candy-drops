package com.Qesnisu9.Madqis.domain.usecase

import com.Qesnisu9.Madqis.data.sample.SampleData
import com.Qesnisu9.Madqis.domain.model.DailyRoute
import com.Qesnisu9.Madqis.domain.model.PlayerProgress
import java.time.LocalDate

class EvaluateDailyRouteUseCase {

    operator fun invoke(progress: PlayerProgress, today: LocalDate = LocalDate.now()): DailyRoute {
        val dayOfYear = today.dayOfYear
        val index = dayOfYear % SampleData.dailyRouteTitles.size
        return DailyRoute(
            title = SampleData.dailyRouteTitles[index],
            target = BASE_TARGET + index * TARGET_STEP,
            seed = dayOfYear * SEED_FACTOR + index,
            dayOfYear = dayOfYear,
            dayOfWeek = today.dayOfWeek.value % WEEK_DAYS,
            claimed = progress.dailyClaimedDayOfYear == dayOfYear
        )
    }

    private companion object {
        const val BASE_TARGET = 420
        const val TARGET_STEP = 60
        const val SEED_FACTOR = 977
        const val WEEK_DAYS = 7
    }
}
