package com.template.mvvm.repository.source.remote.interceptors

class MissingNetworkConnectionException : RuntimeException("There is no network connection to process you request.")