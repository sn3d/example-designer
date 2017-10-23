
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
package domain


interface ThemeEvent

/**
 * all events that might happened on [Theme]
 */
data class NewThemeCreated(val name: String): ThemeEvent
data class LogoPositionChanged(val left: Double, val top: Double) : ThemeEvent
data class HeaderColorChanged(val color: RgbaColor) : ThemeEvent
data class ThemeStateChanged(val state: Theme.State): ThemeEvent

class Theme {

	enum class State {
		ACTIVE, INACTIVE, DEFAULT
	}

	var name: String = ""
	var state: Theme.State = Theme.State.INACTIVE
	var header: HeaderRule = HeaderRule()
	var logo: LogoRule = LogoRule()

	private val events: MutableList<ThemeEvent> = mutableListOf()
	private val uncommitedEvents: MutableList<ThemeEvent> = mutableListOf()

	/**
	 * create new [Theme] with given name
	 */
	constructor(name: String) {
		applyChange(NewThemeCreated(name))
	}

	/**
	 * construct projection of events
	 */
	constructor(events: List<ThemeEvent>) {
		events.forEach(this::applyChange)
		commit()
	}

	/**
	 * example of 'activate' command. The command can be refused
	 * because state etc. or can produce event
	 */
	fun activate(): Boolean {
		if (this.state == State.INACTIVE) {
			applyChange(ThemeStateChanged(State.ACTIVE))
			return true
		}
		return false
	}

	/**
	 * undo is very easy to achieve. Also any versioning.
	 */
	fun undo(): Theme =
		Theme(events.subList(0, events.size - 1))

	/**
	 * apply the event to this instance of theme. It's router
	 * where is event routed into correct 'apply' method (similar to visitor pattern)
	 */
	fun applyChange(event: ThemeEvent) {
		when(event) {
			is NewThemeCreated -> apply(event)
			is LogoPositionChanged -> apply(event)
			is HeaderColorChanged -> apply(event)
			is ThemeStateChanged -> apply(event)
			else -> UnsupportedOperationException("")
		}
		uncommitedEvents += event;
	}

	/**
	 * this method commit all uncommited changes that means they're pushed into publisher,
	 * moved to [events] list and [uncommitedEvents] list is cleaned.
	 */
	fun commit(publisher: (ThemeEvent) -> Unit = {}) {
		uncommitedEvents
			.toList()
			.asSequence()
			.onEach(publisher)
			.onEach{event -> events.add(event)}
			.toList()

		uncommitedEvents.clear()
	}

	fun uncommitedEvents(): List<ThemeEvent>
		= uncommitedEvents.toList()

	fun allEvents(): List<ThemeEvent>
		= events + uncommitedEvents


	//-------------------------------------------------------------------------------------------------
	// event processing routines
	//-------------------------------------------------------------------------------------------------

	private fun apply(event: NewThemeCreated) {
		name = event.name;
	}

	private fun apply(event: LogoPositionChanged) {
		logo.left = event.left;
		logo.top = event.top;
	}

	private fun apply(event: HeaderColorChanged) {
		header.color = event.color
	}

	private fun apply(event: ThemeStateChanged) {
		state = event.state
	}



}

