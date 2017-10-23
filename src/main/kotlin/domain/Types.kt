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

data class RgbaColor(val red: Double, val green: Double, val blue: Double, val alpha: Double) {
	companion object {
		val WHITE = RgbaColor(1.0,1.0,1.0,0.0)
		val RED   = RgbaColor(1.0,0.0,0.0,0.0)
		val BLACK = RgbaColor(0.0,0.0,0.0,0.0)
	}
}

enum class Align{CENTER, LEFT, RIGHT}
