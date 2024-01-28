package com.ouday.memo.di

import android.app.Application
import androidx.room.Room
import com.ouday.memo.memo.data.data_source.MemoDatabase
import com.ouday.memo.memo.data.repository.MemoRepositoryImpl
import com.ouday.memo.memo.domain.repository.MemoRepository
import com.ouday.memo.memo.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMemoDatabase(app: Application): MemoDatabase {
        return Room.databaseBuilder(
            app,
            MemoDatabase::class.java,
            MemoDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideMemoRepository(db: MemoDatabase): MemoRepository {
        return MemoRepositoryImpl(db.memoDao)
    }

    @Provides
    @Singleton
    fun provideMemoUseCases(repository: MemoRepository): MemoUseCases {
        return MemoUseCases(
            getMemos = GetMemos(repository),
            deleteMemo = DeleteMemo(repository),
            addMemo = AddMemo(repository),
            getMemo = GetMemo(repository)
        )
    }
}