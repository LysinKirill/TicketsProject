package ru.hse.ticketsservice.exceptions

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException

class StationNotFoundException : NotFoundException()