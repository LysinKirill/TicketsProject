package ru.hse.ticketsservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TicketsServiceApplication

fun main(args: Array<String>) {
    runApplication<TicketsServiceApplication>(*args)
}
