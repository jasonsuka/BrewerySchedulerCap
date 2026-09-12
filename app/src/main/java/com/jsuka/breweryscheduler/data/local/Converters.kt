package com.jsuka.breweryscheduler.data.local

import androidx.room.TypeConverter
import com.jsuka.breweryscheduler.domain.model.AssignmentState
import com.jsuka.breweryscheduler.domain.model.PersonRole
import com.jsuka.breweryscheduler.domain.model.ScheduleState
import java.time.LocalDate

/**
 * Dates are stored as epoch day so the schema stays simple and sortable.
 * Times are stored elsewhere as minutes past midnight for the same reason.
 */
class Converters {

    @TypeConverter
    fun localDateToEpochDay(value: LocalDate?): Long? = value?.toEpochDay()

    @TypeConverter
    fun epochDayToLocalDate(value: Long?): LocalDate? = value?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun assignmentStateToName(value: AssignmentState): String = value.name

    @TypeConverter
    fun nameToAssignmentState(value: String): AssignmentState = AssignmentState.valueOf(value)

    @TypeConverter
    fun scheduleStateToName(value: ScheduleState): String = value.name

    @TypeConverter
    fun nameToScheduleState(value: String): ScheduleState = ScheduleState.valueOf(value)

    @TypeConverter
    fun personRoleToName(value: PersonRole): String = value.name

    @TypeConverter
    fun nameToPersonRole(value: String): PersonRole = PersonRole.valueOf(value)
}
