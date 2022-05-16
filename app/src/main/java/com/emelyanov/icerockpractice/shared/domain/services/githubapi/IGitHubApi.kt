package com.emelyanov.icerockpractice.shared.domain.services.githubapi

import com.emelyanov.icerockpractice.shared.domain.models.responses.ReadmeResponse
import com.emelyanov.icerockpractice.shared.domain.models.responses.RepositoryDetailsResponse
import com.emelyanov.icerockpractice.shared.domain.models.responses.RepositoryShortResponse
import com.emelyanov.icerockpractice.shared.domain.models.responses.UserInfoResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface IGitHubApi {
    @GET("/user")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ) : UserInfoResponse

    @GET("/user/repos?per_page=10&page=1&type=owner&sort=created&direction=desc")
    suspend fun getRepositories(
        @Header("Authorization") token: String
    ) : List<RepositoryShortResponse>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepoDetails(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ) : RepositoryDetailsResponse

    @GET("repos/{owner}/{repo}/readme")
    suspend fun getReadme(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
    ) : ReadmeResponse

    @GET("repos/{owner}/{repo}/readme")
    suspend fun getReadme(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("ref") branch: String
    ) : ReadmeResponse
}