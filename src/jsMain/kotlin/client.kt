import react.dom.render
import kotlinx.browser.document
import kotlinx.browser.window
import nl.hybrit.workshop.mp.api.common.WEB_DOC_ROOT

fun main() {
    window.onload = {
        render(document.getElementById(WEB_DOC_ROOT)) {
            child(Welcome::class) {
                attrs {
                    name = "Kotlin/JS"
                }
            }
        }
    }
}
