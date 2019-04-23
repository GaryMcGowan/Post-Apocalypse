package com.garymcgowan.postapocalypse.utils

import com.garymcgowan.postapocalypse.core.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

open class TestSchedulerProvider(private val scheduler: Scheduler = Schedulers.trampoline()) :
    SchedulerProvider {
    override fun ui(): Scheduler = scheduler

    override fun computation(): Scheduler = scheduler

    override fun trampoline(): Scheduler = scheduler

    override fun newThread(): Scheduler = scheduler

    override fun io(): Scheduler = scheduler
}