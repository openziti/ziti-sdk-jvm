/*
 * Copyright (c) 2018-2021 NetFoundry, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openziti.android

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.openziti.Ziti.IdentityEventType
import org.openziti.ZitiContext
import java.util.concurrent.ConcurrentHashMap

/**
 * Class to allow application components to consume Ziti data.
 */
class ZitiViewModel(app: Application): AndroidViewModel(app) {
    private val contexts = MutableLiveData<List<ZitiContext>>()
    private val ztxMap = ConcurrentHashMap<String, ZitiContext>()

    init {
        viewModelScope.launch {
            Ziti.identities().collect {
                when (it.type) {
                    IdentityEventType.Loaded -> ztxMap[it.ztx.name()] = it.ztx
                    IdentityEventType.Removed -> ztxMap.remove(it.ztx.name())
                }

                contexts.postValue(ztxMap.values.toList())
            }
        }
    }

    /**
     * Live data of ZitiContextEvents.
     */
    fun getIdentities() = Ziti.identities().asLiveData()

    /**
     * All current ZitiContext as LiveData
     */
    fun allIdentities(): LiveData<List<ZitiContext>> = contexts

    override fun onCleared() {
        ztxMap.clear()
        super.onCleared()
    }
}