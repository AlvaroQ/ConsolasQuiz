package com.quiz.videoconsolas.datasource

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.quiz.data.datasource.DataBaseSource
import com.quiz.domain.App
import com.quiz.domain.Console
import com.quiz.videoconsolas.BuildConfig
import com.quiz.videoconsolas.utils.Constants.PATH_REFERENCE_APPS
import com.quiz.videoconsolas.utils.Constants.PATH_REFERENCE_CONSOLE
import com.quiz.videoconsolas.utils.Constants.TOTAL_ITEM_EACH_LOAD
import com.quiz.videoconsolas.utils.log
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.Exception

class DataBaseSourceImpl : DataBaseSource {

    override suspend fun getConsolaById(id: Int): Console {
        return suspendCancellableCoroutine { continuation ->
            FirebaseDatabase.getInstance().getReference(PATH_REFERENCE_CONSOLE + id)
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        try {
                            continuation.resume(dataSnapshot.getValue(Console::class.java) as Console){}
                        } catch (e: Exception) {
                            log("getConsolaById FAILED", "Failed to read value.", e)
                            continuation.resume(Console()){}
                            FirebaseCrashlytics.getInstance().recordException(Throwable(e))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        log("getConsolaById FAILED", "Failed to read value.", error.toException())
                        continuation.resume(Console()){}
                        FirebaseCrashlytics.getInstance().recordException(Throwable(error.toException()))
                    }
                })
        }
    }

    override suspend fun getConsolaList(currentPage: Int): MutableList<Console> {
        return suspendCancellableCoroutine { continuation ->
            FirebaseDatabase.getInstance().getReference(PATH_REFERENCE_CONSOLE)
                .orderByKey()
                .startAt((currentPage * TOTAL_ITEM_EACH_LOAD).toString())
                .limitToFirst(TOTAL_ITEM_EACH_LOAD)
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val consoleList = mutableListOf<Console>()
                        if(dataSnapshot.hasChildren()) {
                            for(snapshot in dataSnapshot.children) {
                                consoleList.add(snapshot.getValue(Console::class.java)!!)
                            }
                        }
                        continuation.resume(consoleList) {}
                    }

                    override fun onCancelled(error: DatabaseError) {
                        log("DataBaseBaseSourceImpl", "Failed to read value.", error.toException())
                        continuation.resume(mutableListOf()){}
                        FirebaseCrashlytics.getInstance().recordException(Throwable(error.toException()))
                    }
                })
        }
    }

    override suspend fun getAppsRecommended(): MutableList<App> {
        return suspendCancellableCoroutine { continuation ->
            FirebaseDatabase.getInstance().getReference(PATH_REFERENCE_APPS)
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var value = dataSnapshot.getValue<MutableList<App>>()
                        if(value == null) value = mutableListOf()
                        continuation.resume(value
                            .sortedBy { it.priority }
                            .filter { it.url != BuildConfig.APPLICATION_ID }
                            .toMutableList()){}
                    }

                    override fun onCancelled(error: DatabaseError) {
                        log("DataBaseBaseSourceImpl", "Failed to read value.", error.toException())
                        continuation.resume(mutableListOf()){}
                        FirebaseCrashlytics.getInstance().recordException(Throwable(error.toException()))
                    }
                })
        }
    }
}