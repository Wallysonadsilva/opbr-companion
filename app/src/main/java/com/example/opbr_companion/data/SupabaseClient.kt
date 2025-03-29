package com.example.opbr_companion.data

import com.example.opbr_companion.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://bwpgdqzcvqroakybcgjd.supabase.co",
        supabaseKey = BuildConfig.SUPABASE_Key
    ){
        install(Postgrest)
    }
}