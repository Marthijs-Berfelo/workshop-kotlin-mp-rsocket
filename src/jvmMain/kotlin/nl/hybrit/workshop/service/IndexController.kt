package nl.hybrit.workshop.service

import io.swagger.v3.oas.annotations.Operation
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import nl.hybrit.workshop.extensions.debug
import nl.hybrit.workshop.mp.api.common.WEB_DOC_ROOT
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class IndexController {
    val log: Logger by lazy { LoggerFactory.getLogger(this::class.java) }

    @GetMapping
    @ResponseBody
    @Operation(
        tags = ["Web"],
        summary = "Web user interface",
        description = "Exposes the client interface for use in browsers."
    )
    fun index() = createHTML()
        .html {
            head {
                title("HybrIT Workshop Kotlin")
            }
            body {
                div {
                    id = WEB_DOC_ROOT
                }
                script(src = "/main.js") {}
            }
        }
        .also { log.debug { "HTML: $it" } }
}
