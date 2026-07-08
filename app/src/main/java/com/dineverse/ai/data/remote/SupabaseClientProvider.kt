package com.dineverse.ai.data.remote

import com.dineverse.ai.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import timber.log.Timber

object SupabaseClientProvider {
    fun createClient(): SupabaseClient {
        return try {
            Timber.d("Initializing Supabase with URL: ${BuildConfig.SUPABASE_URL}")
            val client = createSupabaseClient(
                supabaseUrl = BuildConfig.SUPABASE_URL,
                supabaseKey = BuildConfig.SUPABASE_ANON_KEY
            ) {
                install(Auth)
                install(Postgrest)
                install(Storage)
                install(Realtime)
            }
            Timber.d("Supabase initialized successfully")
            client
        } catch (e: Exception) {
            Timber.e(e, "Supabase initialization failed")
            throw e
        }
    }
}
