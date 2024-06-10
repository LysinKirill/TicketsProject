package ru.hse.ticketsservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.hse.ticketsservice.entity.Station

@Repository
interface StationRepository : JpaRepository<Station, Long>