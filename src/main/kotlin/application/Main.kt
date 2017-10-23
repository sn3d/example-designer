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

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import domain.HeaderColorChanged
import domain.LogoPositionChanged
import spark.ModelAndView
import spark.Spark
import spark.Spark.port
import spark.template.mustache.MustacheTemplateEngine


fun main(args: Array<String>) {

	port(8080)

	/**
	 * endpoint for theme creation.
	 *
	 * example:
	 * ```
	 * curl -X POST http://localhost:4567/theme?name=mytheme
	 * ```
	 */
	Spark.post("/theme") { req, resp ->
		val name = req.queryParams("name")
		DesignerApp.createNewTheme(name)
		resp.redirect("/theme/${name}")
	}

	/**
	 * endpoint for getting the theme projection
	 *
	 * example:
	 * ```
	 * 	curl -X GET http://localhost:4567/theme/mytheme
	 * ```
	 */
	Spark.get("/theme/:name") { req, resp ->
		val name = req.params("name")
		val theme = DesignerApp.getTheme(name)
		resp.type("application/json")
		jacksonObjectMapper().writeValueAsString(theme)
	}

	/**
	 * this endpoint render template as CSS
	 */
	Spark.get("/theme/:name/css") { req, resp ->
		val name = req.params("name")
		val theme = DesignerApp.getTheme(name)
		resp.type("text/css")
		MustacheTemplateEngine().render(ModelAndView(theme, "css.mustache"))
	}

	/**
	 * endpoint for processing events & commands
	 *
	 * example of events:
	 * ```
	 * 	curl -X POST -H "Content-Type: application/json;event=LogoPositionChanged" -d '{"left":10.0,"top":10.0}' http://localhost:4567/theme/mytheme
	 * 	curl -X POST -H "Content-Type: application/json;event=HeaderColorChanged" -d '{"color":{"red":1.0,"green":0.0,"blue":0.0,"apha":0.0}}' http://localhost:4567/theme/mytheme
	 * ```
	 *
	 * example of command:
	 * ```
	 * 	curl -X POST -H "Content-Type: application/json;command=Activate" http://localhost:4567/theme/mytheme
	 * ```
	 */
	Spark.post("/theme/:name") { req, _ ->
		val name = req.params("name")
		when (req.contentType()) {
			"application/json;event=HeaderColorChanged" ->
				DesignerApp.applyChange(name, jacksonObjectMapper().readValue<HeaderColorChanged>(req.body()))
			"application/json;event=LogoPositionChanged" ->
				DesignerApp.applyChange(name, jacksonObjectMapper().readValue<LogoPositionChanged>(req.body()))
			"application/json;command=Activate" ->
				DesignerApp.activate(name)
			else ->
				throw IllegalArgumentException("unsupported content type")
		}
		"OK"
	}

	/**
	 * this endpoint show you all event applied on theme (non-json)
	 */
	Spark.get("/theme/:name/events") { req, resp ->
		val theme = DesignerApp.getTheme(req.params("name"))
		val body = StringBuilder()
		theme.allEvents().forEach { event ->
			body.append("${event}\n")
		}
		resp.type("application/text")
		body.toString()
	}
}
