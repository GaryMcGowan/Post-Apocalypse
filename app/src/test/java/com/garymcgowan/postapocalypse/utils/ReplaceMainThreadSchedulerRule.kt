package com.garymcgowan.postapocalypse.utils

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ReplaceMainThreadSchedulerRule(val replaceScheduler: Scheduler = Schedulers.trampoline()) : TestRule {
    override fun apply(base: Statement, description: Description): Statement = object : Statement() {
        override fun evaluate() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { replaceScheduler }
            try {
                base.evaluate()
            } finally {
                RxJavaPlugins.reset()
            }
        }
    }
}