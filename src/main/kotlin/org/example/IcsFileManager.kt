package org.example

import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Component
import net.fortuna.ical4j.model.Dur
import net.fortuna.ical4j.model.Property
import net.fortuna.ical4j.model.PropertyList

import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.Attendee
import net.fortuna.ical4j.model.property.DtEnd
import net.fortuna.ical4j.model.property.DtStart
import net.fortuna.ical4j.util.MapTimeZoneCache
import java.awt.event.FocusEvent
import java.io.FileInputStream
import java.time.Duration


class IcsFileManager(private val stream: FileInputStream) {

    fun getMeetings(): MutableList<IcsMeeting> {
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache::class.java.name)
        val meetingList = mutableListOf<IcsMeeting>()
        val builder = CalendarBuilder()
        val calendar = builder.build(stream)
        val event = calendar.getComponents<VEvent>(Component.VEVENT).single()
        val description = event.description.value
        val subject = event.summary.value
        val a = event.getProperties<Attendee>(Property.ATTENDEE)
        val attendees = event
                .getProperties<Attendee>(Property.ATTENDEE)
                .filter { it.calAddress.scheme.equals("MAILTO", ignoreCase = true) }
                .map{x->x.calAddress.schemeSpecificPart}
        val organizer = event.organizer.calAddress.schemeSpecificPart

        val dur = calculateDuration(event.startDate, event.endDate, event.duration)
        val duration = Duration.ofSeconds(dur.seconds.toLong()) +
                Duration.ofMinutes(dur.minutes.toLong()) +
                Duration.ofHours(dur.hours.toLong())

        val startDateTime = event.startDate?.date?.toInstant()
        val meeting = IcsMeeting(subject, description, startDateTime, duration, organizer, attendees)
        meetingList.add(meeting)
        return meetingList
    }

    private fun calculateDuration(startDate: DtStart?, endDate: DtEnd?, duration: net.fortuna.ical4j.model.property.Duration?): Dur {
        if (duration != null) {
            return duration.duration
        }
        if (startDate == null){
            return Dur(0,0,0,0)
        }
        if (endDate == null) {
            return Dur(0,0,0,0)
        }
        return Dur(startDate.date, endDate.date)
    }
}