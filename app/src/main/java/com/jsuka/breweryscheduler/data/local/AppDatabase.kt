package com.jsuka.breweryscheduler.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jsuka.breweryscheduler.data.local.dao.DepartmentDao
import com.jsuka.breweryscheduler.data.local.dao.EmployeeDao
import com.jsuka.breweryscheduler.data.local.dao.QualificationDao
import com.jsuka.breweryscheduler.data.local.dao.ScheduleDao
import com.jsuka.breweryscheduler.data.local.dao.UserAccountDao
import com.jsuka.breweryscheduler.data.local.entity.AvailabilityEntity
import com.jsuka.breweryscheduler.data.local.entity.CredentialEntity
import com.jsuka.breweryscheduler.data.local.entity.DepartmentEntity
import com.jsuka.breweryscheduler.data.local.entity.JobPositionEntity
import com.jsuka.breweryscheduler.data.local.entity.PersonEntity
import com.jsuka.breweryscheduler.data.local.entity.ScheduleAssignmentEntity
import com.jsuka.breweryscheduler.data.local.entity.ScheduleEntity
import com.jsuka.breweryscheduler.data.local.entity.ShiftEntity
import com.jsuka.breweryscheduler.data.local.entity.UserAccountEntity

@Database(
    entities = [
        DepartmentEntity::class,
        JobPositionEntity::class,
        PersonEntity::class,
        CredentialEntity::class,
        AvailabilityEntity::class,
        ShiftEntity::class,
        ScheduleEntity::class,
        ScheduleAssignmentEntity::class,
        UserAccountEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun departmentDao(): DepartmentDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun qualificationDao(): QualificationDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun userAccountDao(): UserAccountDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: build(context).also { instance = it }
            }

        private fun build(context: Context): AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "brewery_scheduler.db"
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        // Room does not enforce foreign keys by default.
                        db.execSQL("PRAGMA foreign_keys = ON")
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
    }
}
