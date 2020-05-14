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

package io.netfoundry.ziti.net.nio

import java.nio.channels.CompletionHandler
import java.util.concurrent.CompletableFuture

class FutureHandler<A>: CompletionHandler<A, CompletableFuture<A>> {
    override fun completed(result: A?, f: CompletableFuture<A>) { f.complete(result) }
    override fun failed(exc: Throwable, f: CompletableFuture<A>) { f.completeExceptionally(exc) }
}