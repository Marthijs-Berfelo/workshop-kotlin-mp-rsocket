package nl.hybrit.workshop.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveService

fun main(args: Array<String>){
    runApplication<ReactiveService>(*args)
}
