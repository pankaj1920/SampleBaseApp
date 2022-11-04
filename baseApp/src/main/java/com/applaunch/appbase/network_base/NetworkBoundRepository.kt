package com.applaunch.b2b.app_base.network


import com.google.gson.Gson
import com.applaunch.appbase.listner_base.BaseRepoListener
import com.applaunch.appbase.model_base.BaseResponse
import com.applaunch.appbase.model_base.State
import com.applaunch.appbase.network_base.CheckResponseUtils
import com.applaunch.appbase.utils_base.BaseConstants
import com.applaunch.appbase.utils_base.Print
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

/**
 * A repository which provides resource from local database as well as remote end point.
 *
 * [RESULT] represents the type for database.
 * [REQUEST] represents the type for network.
 */

abstract class NetworkBoundRepository<RESULT>(
    var iRepositoryListener: BaseRepoListener?,
    private val isShowProgress: Boolean = true,
) {
    fun asFlow() = flow<State<RESULT>> {

        // Emit Loading State

        if (iRepositoryListener?.isNetworkConnected()!!) {

            if (isShowProgress) {
                iRepositoryListener?.showLoader()
            }

            // Emit Database content first
            // Fetch latest posts from remote
            val apiResponse: Response<RESULT>

            // Parse body
            val remoteData: RESULT?
            val baseResponse: BaseResponse?

            withContext(Dispatchers.IO) {

                apiResponse = fetchData()
                remoteData = apiResponse.body()


            }
            iRepositoryListener?.hideLoader()

            // Check for response validation
            if (apiResponse.isSuccessful && remoteData != null) {
//                baseResponse = apiResponse.body() as BaseResponse
                // Save posts into the persistence storage
                if (apiResponse.code() == BaseConstants.SuccessCode.SUCCESS || apiResponse.code() == BaseConstants.SuccessCode.ACCEPTED) {
                    // Checking  if status and status code is success or Failed in api response
                    emit(State.success(remoteData))

                } else {

                    emit(State.error(apiResponse.message()))
                    iRepositoryListener?.showErrorMessage(apiResponse.message())
                    Print.log("Error => (NetworkBoundRepository) : ${apiResponse.message()}")
                }
            } else {
                // Something went wrong! Emit Error state.
                /*  Print.log("Response Code : " + apiResponse.code())
                  Print.log("Response message : " + apiResponse.message())*/
                iRepositoryListener?.showErrorMessage(CheckResponseUtils.isErrorMessage(apiResponse.code()))
                emit(State.error(CheckResponseUtils.isErrorMessage(apiResponse.code())))

            }
        }
    }.catch { e ->
        // Exception occurred! Emit error
        withContext(Dispatchers.Main) {
            iRepositoryListener?.hideLoader()
            e.message?.let { iRepositoryListener?.showErrorMessage(it) }
            emit(State.error(e.message!!))
            Print.log("Exception => (NetworkBoundRepository) : ${e.message!!}")
            e.printStackTrace()
        }
    }

    protected abstract suspend fun fetchData(): Response<RESULT>
}


private fun <T> getObjectFromJsonString(jsonString: String, classType: Class<T>): T? {
    try {
        val gson = Gson()
        return gson.fromJson(jsonString, classType)
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: NullPointerException) {
        e.printStackTrace()
    }

    return null
}
