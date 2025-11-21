package app.grocery.list.commons.compose.internal.di

import app.grocery.list.commons.compose.ToolbarEmojiProvider
import app.grocery.list.commons.compose.ToolbarEmojiProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface ImplsModule {
    @Binds fun toolbarEmojiProvider(impl: ToolbarEmojiProviderImpl): ToolbarEmojiProvider
}
