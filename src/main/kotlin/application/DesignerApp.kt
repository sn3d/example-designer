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
package application

import domain.*
import infrastructure.InMemoryRepository


object DesignerApp {

	/// ref. to repository where are data persist (currently InMemory impl.)
	val repository = InMemoryRepository()

	/**
	 * create new theme and persist with repository
	 */
	fun createNewTheme(name: String) {
		val theme = Theme(name)
		theme.commit { e ->
			repository.save(name, e)
		}
	}

	/**
	 * create theme projection based on events
	 */
	fun getTheme(name: String): Theme {
		val events = repository.retrieve(name)
		val theme = Theme(events)
		return theme
	}

	/**
	 * save event directly to Repository
	 */
	fun applyChange(name: String, event: ThemeEvent) {
		repository.save(name, event)
	}

	/**
	 * activate existing theme
	 */
	fun activate(name: String) {
		val theme = getTheme(name)
		if (theme.activate()) {
			theme.commit { e ->
				repository.save(name, e)
			}
		} else {
			throw IllegalStateException("Wrong state of theme ${name}. Must be INACTIVE if you wish to activate it")
		}
	}
}