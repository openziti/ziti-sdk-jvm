/*
 * Copyright (c) 2018-2020 NetFoundry, Inc.
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

package io.netfoundry.ziti.netty

import io.netfoundry.ziti.ZitiContext
import io.netty.channel.ChannelFactory
import io.netty.channel.ServerChannel

class ZitiServerChannelFactory(val ziti: ZitiContext): ChannelFactory<ServerChannel> {
    override fun newChannel(): ServerChannel = ZitiServerChannel(ziti.openServer())
}