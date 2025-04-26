package com.guide.opbr_companion.data

import android.util.Log
import com.guide.opbr_companion.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

/**
 * Singleton object that provides access to the Supabase client.
 * Includes error handling and initialization validation.
 */
object SupabaseClient {
    private const val TAG = "SupabaseClient"
    private const val FALLBACK_URL = "https://bwpgdqzcvqroakybcgjd.supabase.co"

    val client by lazy {
        try {
            // Get the API key from BuildConfig, with fallback handling
            val apiKey = BuildConfig.SUPABASE_KEY.takeIf { it.isNotBlank() }
                ?: throw IllegalStateException("Supabase API key is missing or invalid")

            // Create the Supabase client with error handling
            createSupabaseClient(
                supabaseUrl = FALLBACK_URL,
                supabaseKey = apiKey
            ) {
                install(Postgrest)
            }.also {
                Log.d(TAG, "Supabase client initialized successfully")
            }
        } catch (e: Exception) {
            // Log the error but don't crash the app
            Log.e(TAG, "Error initializing Supabase client: ${e.message}", e)

            // Create a minimal client as fallback to prevent immediate crashes
            // This won't work for actual API calls, but prevents initialization crashes
            createSupabaseClient(
                supabaseUrl = FALLBACK_URL,
                supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" // Dummy placeholder
            ) {
                install(Postgrest)
            }
        }
    }
}