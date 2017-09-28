// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.template.mvvm.customtabs

import android.content.ComponentName
import android.support.customtabs.CustomTabsClient
import android.support.customtabs.CustomTabsServiceConnection
import java.lang.ref.WeakReference

/**
 * Implementation for the CustomTabsServiceConnection that avoids leaking the
 * ServiceConnectionCallback
 */
internal class ServiceConnection(connectionCallback: ServiceConnectionCallback) : CustomTabsServiceConnection() {
    private val callback: WeakReference<ServiceConnectionCallback> = WeakReference(connectionCallback)

    override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
        val connectionCallback = callback.get()
        connectionCallback?.onServiceConnected(client)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        val connectionCallback = callback.get()
        connectionCallback?.onServiceDisconnected()
    }
}

/**
 * Callback for events when connecting and disconnecting from Custom Tabs Service.
 */
internal interface ServiceConnectionCallback {
    /**
     * Called when the service is connected.
     *
     * @param client a CustomTabsClient
     */
    fun onServiceConnected(client: CustomTabsClient)

    /**
     * Called when the service is disconnected.
     */
    fun onServiceDisconnected()
}