package com.android.grunfeld_project.network

import com.android.grunfeld_project.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient ***REMOVED***

    val supabaseClient: SupabaseClient by lazy ***REMOVED***
        createSupabaseClient(
            supabaseUrl = BuildConfig.BASE_URL,
            supabaseKey = BuildConfig.ANON_KEY
        ) ***REMOVED***
            install(Auth)
            install(Postgrest)
***REMOVED***
***REMOVED***
***REMOVED***