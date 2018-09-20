package us.nineworlds.serenity.emby.server.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path
import us.nineworlds.serenity.emby.server.model.AuthenticateUserByName
import us.nineworlds.serenity.emby.server.model.AuthenticationResult
import us.nineworlds.serenity.emby.server.model.PublicUserInfo
import us.nineworlds.serenity.emby.server.model.QueryResult

interface UsersService {

  @GET("/emby/Users/Public")
  fun allPublicUsers(): Call<List<PublicUserInfo>>

  @POST("/emby/Users/AuthenticateByName")
  fun authenticate(@Body authenticateUserByName: AuthenticateUserByName, @HeaderMap headerMap: Map<String, String>): Call<AuthenticationResult>

  /**
   * use this to provide menus.  You can ignore the Folders
   * Folders will be identified by CollectionType: folders.  Movies are CollectionType: movies, TV Shows are CollectionType: tvshows
   * 1. User must be logged in
   * 2. Token must be valid
   *
   * The plex corresponding call is retrieveRootData()
   */
  @GET("/emby/Users/{userId}/Views")
  fun usersViews(@HeaderMap headerMap: Map<String, String>, @Path("userId") userId: String): Call<QueryResult>

}