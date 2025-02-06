package com.android.grunfeld_project.network

import com.android.grunfeld_project.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {

    val supabaseClient: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = BuildConfig.BASE_URL,
            supabaseKey = BuildConfig.ANON_KEY
        ) {
            install(Auth)
            install(Postgrest)
        }
    }
}