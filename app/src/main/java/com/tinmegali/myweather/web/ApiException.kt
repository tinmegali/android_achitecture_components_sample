package com.tinmegali.myweather.web

/**
 * com.tinmegali.myweather.web | MyWeatherApp
 * __________________________________
 * Created by tinmegali
 * 26/07/17
 * @see <a href="http://www.tinmegali.com">tinmegali.com</a>
 * @see <a href="http://github.com/tinmegali">github</a>
 * ___________________________________
 */
class ApiException(
        var error: ApiError?
) : Exception("Api Error") {

    override fun toString(): String {
        if ( error != null ) {
            var msg = "Api Exception:\nApi Error. \n--- CODE[${error!!.statusCode}]\n--- msg: ${error!!.message}"
            return msg
        } else return super.toString()
    }
}