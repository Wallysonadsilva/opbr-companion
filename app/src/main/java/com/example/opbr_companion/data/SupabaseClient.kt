package com.example.opbr_companion.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://bwpgdqzcvqroakybcgjd.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJ3cGdkcXpjdnFyb2FreWJjZ2pkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDEzODY4MTgsImV4cCI6MjA1Njk2MjgxOH0.sqsWDAJ9CduUF9RHUHeEy9yv4ByJceAzhG54y-DRb8I",
    ){
        install(Postgrest)
    }
}