package org.example

import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.Component
import net.fortuna.ical4j.model.ComponentList
import net.fortuna.ical4j.model.component.CalendarComponent
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.util.MapTimeZoneCache
import org.example.IcsFileManager
import java.io.FileInputStream

fun main(args: Array<String>) {
    System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache::class.java.name)

    val fin = FileInputStream("no_name.ics")
    val icsFileManager = IcsFileManager(fin)
    val meetings = icsFileManager.getMeetings()
    val meeting = meetings.single()

    println(
            meeting.subject
                    +" \n     ***** \n"
                    + meeting.description
                    +" \n     ***** \n"
                    + meeting.startTime
                    +" \n     ***** \n"
                    + meeting.duration
                    +" \n     ***** \n"
                    + meeting.attendees)
}

