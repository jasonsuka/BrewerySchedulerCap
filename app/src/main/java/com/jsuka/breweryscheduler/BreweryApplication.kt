package com.jsuka.breweryscheduler

import android.app.Application
import com.jsuka.breweryscheduler.data.local.AppDatabase
import com.jsuka.breweryscheduler.data.local.SeedData
import com.jsuka.breweryscheduler.data.repository.DepartmentRepository
import com.jsuka.breweryscheduler.data.repository.EmployeeRepository
import com.jsuka.breweryscheduler.data.repository.QualificationRepository
import com.jsuka.breweryscheduler.data.repository.ScheduleRepository
import com.jsuka.breweryscheduler.domain.service.AssignmentContextBuilder
import com.jsuka.breweryscheduler.domain.service.AuthService
import com.jsuka.breweryscheduler.domain.service.ConflictValidator
import com.jsuka.breweryscheduler.domain.service.RecommendationService
import com.jsuka.breweryscheduler.domain.service.ReportGenerator
import com.jsuka.breweryscheduler.domain.service.SchedulingEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Holds the database, repositories and services for the process lifetime.
 * A single container keeps construction in one place without pulling in a
 * dependency-injection framework the project does not otherwise need.
 */
class BreweryApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(AppDatabase.get(this))

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            SeedData.populate(container.database)
        }
    }
}

class AppContainer(val database: AppDatabase) {

    // Data layer
    val employees by lazy { EmployeeRepository(database.employeeDao()) }
    val departments by lazy { DepartmentRepository(database.departmentDao()) }
    val qualifications by lazy { QualificationRepository(database.qualificationDao()) }
    val schedules by lazy { ScheduleRepository(database.scheduleDao()) }

    // Domain layer
    val validator by lazy { ConflictValidator() }

    val contextBuilder by lazy {
        AssignmentContextBuilder(employees, departments, qualifications, schedules)
    }

    val schedulingEngine by lazy {
        SchedulingEngine(schedules, contextBuilder, validator)
    }

    val recommendations by lazy {
        RecommendationService(employees, departments, contextBuilder, validator)
    }

    val reports by lazy {
        ReportGenerator(employees, departments, qualifications)
    }

    val auth by lazy {
        AuthService(database.userAccountDao(), database.employeeDao())
    }
}
