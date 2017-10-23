/*
 * Copyright 2017 vrabel.zdenko@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package infrastructure

import domain.NewThemeCreated
import domain.ThemeEvent

/**
 * This is basically what you know under DAO term. Abstraction for persisting
 * where are all events pushed and can be retrieved as well.
 */
interface ThemeEventsRepository {
	fun save(name: String, event: ThemeEvent)
	fun retrieve(name: String): List<ThemeEvent>
}

/**
 * In Memory impl. of [ThemeEventsRepository]
 */
class InMemoryRepository: ThemeEventsRepository {

	private val storage: MutableMap<String, MutableList<ThemeEvent>> = mutableMapOf()

	override fun save(name: String, event: ThemeEvent) {
		val themeEvents = storage.getOrDefault(name, mutableListOf())
		themeEvents.add(event)
		storage.put(name, themeEvents)
	}

	override fun retrieve(name: String): List<ThemeEvent> =
		storage.get(name)!!

}