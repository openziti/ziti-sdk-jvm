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

import androidx.lifecycle.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.openziti.ZitiContext
import org.openziti.api.Service
import org.openziti.util.Logged
import org.openziti.util.ZitiLog

/**
 * Class to allow application to consume ZitiContext data exposed as LiveData
 */
class ZitiContextViewModel(val ztx: ZitiContext): ViewModel(), Logged by ZitiLog() {
    private val nameData = MutableLiveData(ztx.name())
    private val servicesMap = mutableMapOf<String, Service>()
    private val servicesData = MutableLiveData<List<Service>>()

    init {
        viewModelScope.launch {
            ztx.getIdentity().collect {
                nameData.postValue(it.name)
            }
        }

        viewModelScope.launch {
            ztx.serviceUpdates().collect {
                when (it.type) {
                    ZitiContext.ServiceUpdate.Available -> servicesMap.put(it.service.name, it.service)
                    ZitiContext.ServiceUpdate.Unavailable -> servicesMap.remove(it.service.name)
                    ZitiContext.ServiceUpdate.ConfigurationChange -> {
                    }
                }

                servicesData.postValue(servicesMap.values.toList())
            }
        }
    }

    /**
     * Factory class to create an instance of [ZitiContextViewModel].
     *
     * usage:
     * ```
     *   val ztxModel: ZitiContextViewModel by viewModels { ZitiContextViewModel.Factory(ztx) }
     * ```
     */
    @Suppress("UNCHECKED_CAST")
    class Factory(val ztx: ZitiContext): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ZitiContextViewModel(ztx) as T
    }

    /**
     * Ziti Context Status as LiveData
     */
    val status: LiveData<ZitiContext.Status>
        get() = ztx.statusUpdates().asLiveData()


    /**
     * Ziti Context name
     */
    val name: LiveData<String>
        get() = nameData

    /**
     * Services provided by ZitiContext
     */
    val services: LiveData<List<Service>>
        get() = servicesData


    fun delete() {
        Ziti.deleteIdentity(ztx)
    }
}