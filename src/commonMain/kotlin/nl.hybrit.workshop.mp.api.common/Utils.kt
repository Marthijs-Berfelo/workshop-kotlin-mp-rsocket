package nl.hybrit.workshop.mp.api.common

import kotlinx.coroutines.delay
import kotlin.random.Random

suspend fun randomDelay() = delay(Random.nextLong(10, 1000))
