package com.conekt.suite.data.repository

import com.conekt.suite.data.model.FileRecord
import com.conekt.suite.data.model.PostRecord
import com.conekt.suite.data.model.ProfileRecord
import com.conekt.suite.data.remote.SupabaseProvider
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.decodeList
import io.github.jan.supabase.postgrest.query.decodeSingle

data class UpdateProfileRequest(
    val displayName: String,
    val bio: String,
    val location: String,
    val website: String,
    val isPrivate: Boolean,
    val avatarUrl: String? = null,
    val bannerUrl: String? = null
)

class ProfileRepository(
    private val authRepository: AuthRepository = AuthRepository()
) {
    private val supabase = SupabaseProvider.client

    private fun requireUid(): String {
        return authRepository.currentUserId()
            ?: error("No authenticated user")
    }

    suspend fun getMyProfile(): ProfileRecord {
        val uid = requireUid()

        return supabase.from("profiles")
            .select {
                filter { eq("id", uid) }
            }
            .decodeSingle()
    }

    suspend fun updateMyProfile(request: UpdateProfileRequest): ProfileRecord {
        val uid = requireUid()

        return supabase.from("profiles")
            .update(
                {
                    set("display_name", request.displayName)
                    set("bio", request.bio)
                    set("location", request.location)
                    set("website", request.website)
                    set("is_private", request.isPrivate)
                    set("avatar_url", request.avatarUrl)
                    set("banner_url", request.bannerUrl)
                }
            ) {
                select()
                filter { eq("id", uid) }
            }
            .decodeSingle()
    }

    suspend fun getMyPosts(): List<PostRecord> {
        val uid = requireUid()

        return supabase.from("posts")
            .select {
                filter { eq("author_id", uid) }
            }
            .decodeList()
    }

    suspend fun getMyPostedNotes(): List<PostRecord> {
        val uid = requireUid()

        return supabase.from("posts")
            .select {
                filter {
                    eq("author_id", uid)
                    eq("post_type", "note_share")
                }
            }
            .decodeList()
    }

    suspend fun getMyFiles(): List<FileRecord> {
        val uid = requireUid()

        return supabase.from("files")
            .select {
                filter { eq("owner_id", uid) }
            }
            .decodeList()
    }

    suspend fun getPublicProfile(userId: String): ProfileRecord {
        return supabase.from("profiles")
            .select {
                filter { eq("id", userId) }
            }
            .decodeSingle()
    }

    suspend fun getPublicProfilePosts(userId: String): List<PostRecord> {
        return supabase.from("posts")
            .select {
                filter {
                    eq("author_id", userId)
                    eq("visibility", "public")
                }
            }
            .decodeList()
    }

    suspend fun getPublicProfileFiles(userId: String): List<FileRecord> {
        return supabase.from("files")
            .select {
                filter {
                    eq("owner_id", userId)
                    eq("is_public", true)
                }
            }
            .decodeList()
    }
}