package di

import data.di.repoModule
import org.koin.core.context.startKoin

fun initKoin(){
    startKoin{
        modules(
            repoModule
        )
    }
}