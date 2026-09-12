package com.jsuka.breweryscheduler.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.jsuka.breweryscheduler.data.local.dao.DepartmentDao;
import com.jsuka.breweryscheduler.data.local.dao.DepartmentDao_Impl;
import com.jsuka.breweryscheduler.data.local.dao.EmployeeDao;
import com.jsuka.breweryscheduler.data.local.dao.EmployeeDao_Impl;
import com.jsuka.breweryscheduler.data.local.dao.QualificationDao;
import com.jsuka.breweryscheduler.data.local.dao.QualificationDao_Impl;
import com.jsuka.breweryscheduler.data.local.dao.ScheduleDao;
import com.jsuka.breweryscheduler.data.local.dao.ScheduleDao_Impl;
import com.jsuka.breweryscheduler.data.local.dao.UserAccountDao;
import com.jsuka.breweryscheduler.data.local.dao.UserAccountDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile DepartmentDao _departmentDao;

  private volatile EmployeeDao _employeeDao;

  private volatile QualificationDao _qualificationDao;

  private volatile ScheduleDao _scheduleDao;

  private volatile UserAccountDao _userAccountDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `departments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `minimumStaffing` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `job_positions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `departmentId` INTEGER NOT NULL, `title` TEXT NOT NULL, `requiredCredentialCode` TEXT NOT NULL, FOREIGN KEY(`departmentId`) REFERENCES `departments`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_job_positions_departmentId` ON `job_positions` (`departmentId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `people` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `firstName` TEXT NOT NULL, `lastName` TEXT NOT NULL, `email` TEXT NOT NULL, `hireDate` INTEGER NOT NULL, `role` TEXT NOT NULL, `homeDepartmentId` INTEGER, FOREIGN KEY(`homeDepartmentId`) REFERENCES `departments`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_people_homeDepartmentId` ON `people` (`homeDepartmentId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `credentials` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `personId` INTEGER NOT NULL, `code` TEXT NOT NULL, `name` TEXT NOT NULL, `issuingAuthority` TEXT NOT NULL, `earnedDate` INTEGER NOT NULL, `isCertification` INTEGER NOT NULL, `expirationDate` INTEGER, `renewalRequired` INTEGER NOT NULL, FOREIGN KEY(`personId`) REFERENCES `people`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_credentials_personId` ON `credentials` (`personId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_credentials_code` ON `credentials` (`code`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `availability` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `personId` INTEGER NOT NULL, `dayOfWeek` INTEGER NOT NULL, `startMinute` INTEGER NOT NULL, `endMinute` INTEGER NOT NULL, FOREIGN KEY(`personId`) REFERENCES `people`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_availability_personId` ON `availability` (`personId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `shifts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `departmentId` INTEGER NOT NULL, `date` INTEGER NOT NULL, `startMinute` INTEGER NOT NULL, `endMinute` INTEGER NOT NULL, `label` TEXT NOT NULL, FOREIGN KEY(`departmentId`) REFERENCES `departments`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_shifts_departmentId` ON `shifts` (`departmentId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_shifts_date` ON `shifts` (`date`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `schedules` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `weekStartDate` INTEGER NOT NULL, `state` TEXT NOT NULL, `version` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `schedule_assignments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `scheduleId` INTEGER NOT NULL, `personId` INTEGER NOT NULL, `jobPositionId` INTEGER NOT NULL, `shiftId` INTEGER NOT NULL, `state` TEXT NOT NULL, `conflictReason` TEXT, `overridden` INTEGER NOT NULL, `version` INTEGER NOT NULL, FOREIGN KEY(`scheduleId`) REFERENCES `schedules`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`personId`) REFERENCES `people`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`jobPositionId`) REFERENCES `job_positions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`shiftId`) REFERENCES `shifts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_schedule_assignments_scheduleId` ON `schedule_assignments` (`scheduleId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_schedule_assignments_personId` ON `schedule_assignments` (`personId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_schedule_assignments_jobPositionId` ON `schedule_assignments` (`jobPositionId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_schedule_assignments_shiftId` ON `schedule_assignments` (`shiftId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_accounts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `personId` INTEGER NOT NULL, `username` TEXT NOT NULL, `passwordSalt` TEXT NOT NULL, `passwordHash` TEXT NOT NULL, `role` TEXT NOT NULL, FOREIGN KEY(`personId`) REFERENCES `people`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_user_accounts_username` ON `user_accounts` (`username`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_user_accounts_personId` ON `user_accounts` (`personId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '339f9c22f53da23686a4f268e132a850')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `departments`");
        db.execSQL("DROP TABLE IF EXISTS `job_positions`");
        db.execSQL("DROP TABLE IF EXISTS `people`");
        db.execSQL("DROP TABLE IF EXISTS `credentials`");
        db.execSQL("DROP TABLE IF EXISTS `availability`");
        db.execSQL("DROP TABLE IF EXISTS `shifts`");
        db.execSQL("DROP TABLE IF EXISTS `schedules`");
        db.execSQL("DROP TABLE IF EXISTS `schedule_assignments`");
        db.execSQL("DROP TABLE IF EXISTS `user_accounts`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsDepartments = new HashMap<String, TableInfo.Column>(3);
        _columnsDepartments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepartments.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDepartments.put("minimumStaffing", new TableInfo.Column("minimumStaffing", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDepartments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDepartments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDepartments = new TableInfo("departments", _columnsDepartments, _foreignKeysDepartments, _indicesDepartments);
        final TableInfo _existingDepartments = TableInfo.read(db, "departments");
        if (!_infoDepartments.equals(_existingDepartments)) {
          return new RoomOpenHelper.ValidationResult(false, "departments(com.jsuka.breweryscheduler.data.local.entity.DepartmentEntity).\n"
                  + " Expected:\n" + _infoDepartments + "\n"
                  + " Found:\n" + _existingDepartments);
        }
        final HashMap<String, TableInfo.Column> _columnsJobPositions = new HashMap<String, TableInfo.Column>(4);
        _columnsJobPositions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJobPositions.put("departmentId", new TableInfo.Column("departmentId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJobPositions.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJobPositions.put("requiredCredentialCode", new TableInfo.Column("requiredCredentialCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysJobPositions = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysJobPositions.add(new TableInfo.ForeignKey("departments", "CASCADE", "NO ACTION", Arrays.asList("departmentId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesJobPositions = new HashSet<TableInfo.Index>(1);
        _indicesJobPositions.add(new TableInfo.Index("index_job_positions_departmentId", false, Arrays.asList("departmentId"), Arrays.asList("ASC")));
        final TableInfo _infoJobPositions = new TableInfo("job_positions", _columnsJobPositions, _foreignKeysJobPositions, _indicesJobPositions);
        final TableInfo _existingJobPositions = TableInfo.read(db, "job_positions");
        if (!_infoJobPositions.equals(_existingJobPositions)) {
          return new RoomOpenHelper.ValidationResult(false, "job_positions(com.jsuka.breweryscheduler.data.local.entity.JobPositionEntity).\n"
                  + " Expected:\n" + _infoJobPositions + "\n"
                  + " Found:\n" + _existingJobPositions);
        }
        final HashMap<String, TableInfo.Column> _columnsPeople = new HashMap<String, TableInfo.Column>(7);
        _columnsPeople.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("firstName", new TableInfo.Column("firstName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("lastName", new TableInfo.Column("lastName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("hireDate", new TableInfo.Column("hireDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("homeDepartmentId", new TableInfo.Column("homeDepartmentId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPeople = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysPeople.add(new TableInfo.ForeignKey("departments", "SET NULL", "NO ACTION", Arrays.asList("homeDepartmentId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesPeople = new HashSet<TableInfo.Index>(1);
        _indicesPeople.add(new TableInfo.Index("index_people_homeDepartmentId", false, Arrays.asList("homeDepartmentId"), Arrays.asList("ASC")));
        final TableInfo _infoPeople = new TableInfo("people", _columnsPeople, _foreignKeysPeople, _indicesPeople);
        final TableInfo _existingPeople = TableInfo.read(db, "people");
        if (!_infoPeople.equals(_existingPeople)) {
          return new RoomOpenHelper.ValidationResult(false, "people(com.jsuka.breweryscheduler.data.local.entity.PersonEntity).\n"
                  + " Expected:\n" + _infoPeople + "\n"
                  + " Found:\n" + _existingPeople);
        }
        final HashMap<String, TableInfo.Column> _columnsCredentials = new HashMap<String, TableInfo.Column>(9);
        _columnsCredentials.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCredentials.put("personId", new TableInfo.Column("personId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCredentials.put("code", new TableInfo.Column("code", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCredentials.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCredentials.put("issuingAuthority", new TableInfo.Column("issuingAuthority", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCredentials.put("earnedDate", new TableInfo.Column("earnedDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCredentials.put("isCertification", new TableInfo.Column("isCertification", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCredentials.put("expirationDate", new TableInfo.Column("expirationDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCredentials.put("renewalRequired", new TableInfo.Column("renewalRequired", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCredentials = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysCredentials.add(new TableInfo.ForeignKey("people", "CASCADE", "NO ACTION", Arrays.asList("personId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesCredentials = new HashSet<TableInfo.Index>(2);
        _indicesCredentials.add(new TableInfo.Index("index_credentials_personId", false, Arrays.asList("personId"), Arrays.asList("ASC")));
        _indicesCredentials.add(new TableInfo.Index("index_credentials_code", false, Arrays.asList("code"), Arrays.asList("ASC")));
        final TableInfo _infoCredentials = new TableInfo("credentials", _columnsCredentials, _foreignKeysCredentials, _indicesCredentials);
        final TableInfo _existingCredentials = TableInfo.read(db, "credentials");
        if (!_infoCredentials.equals(_existingCredentials)) {
          return new RoomOpenHelper.ValidationResult(false, "credentials(com.jsuka.breweryscheduler.data.local.entity.CredentialEntity).\n"
                  + " Expected:\n" + _infoCredentials + "\n"
                  + " Found:\n" + _existingCredentials);
        }
        final HashMap<String, TableInfo.Column> _columnsAvailability = new HashMap<String, TableInfo.Column>(5);
        _columnsAvailability.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAvailability.put("personId", new TableInfo.Column("personId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAvailability.put("dayOfWeek", new TableInfo.Column("dayOfWeek", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAvailability.put("startMinute", new TableInfo.Column("startMinute", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAvailability.put("endMinute", new TableInfo.Column("endMinute", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAvailability = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysAvailability.add(new TableInfo.ForeignKey("people", "CASCADE", "NO ACTION", Arrays.asList("personId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesAvailability = new HashSet<TableInfo.Index>(1);
        _indicesAvailability.add(new TableInfo.Index("index_availability_personId", false, Arrays.asList("personId"), Arrays.asList("ASC")));
        final TableInfo _infoAvailability = new TableInfo("availability", _columnsAvailability, _foreignKeysAvailability, _indicesAvailability);
        final TableInfo _existingAvailability = TableInfo.read(db, "availability");
        if (!_infoAvailability.equals(_existingAvailability)) {
          return new RoomOpenHelper.ValidationResult(false, "availability(com.jsuka.breweryscheduler.data.local.entity.AvailabilityEntity).\n"
                  + " Expected:\n" + _infoAvailability + "\n"
                  + " Found:\n" + _existingAvailability);
        }
        final HashMap<String, TableInfo.Column> _columnsShifts = new HashMap<String, TableInfo.Column>(6);
        _columnsShifts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShifts.put("departmentId", new TableInfo.Column("departmentId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShifts.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShifts.put("startMinute", new TableInfo.Column("startMinute", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShifts.put("endMinute", new TableInfo.Column("endMinute", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShifts.put("label", new TableInfo.Column("label", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysShifts = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysShifts.add(new TableInfo.ForeignKey("departments", "CASCADE", "NO ACTION", Arrays.asList("departmentId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesShifts = new HashSet<TableInfo.Index>(2);
        _indicesShifts.add(new TableInfo.Index("index_shifts_departmentId", false, Arrays.asList("departmentId"), Arrays.asList("ASC")));
        _indicesShifts.add(new TableInfo.Index("index_shifts_date", false, Arrays.asList("date"), Arrays.asList("ASC")));
        final TableInfo _infoShifts = new TableInfo("shifts", _columnsShifts, _foreignKeysShifts, _indicesShifts);
        final TableInfo _existingShifts = TableInfo.read(db, "shifts");
        if (!_infoShifts.equals(_existingShifts)) {
          return new RoomOpenHelper.ValidationResult(false, "shifts(com.jsuka.breweryscheduler.data.local.entity.ShiftEntity).\n"
                  + " Expected:\n" + _infoShifts + "\n"
                  + " Found:\n" + _existingShifts);
        }
        final HashMap<String, TableInfo.Column> _columnsSchedules = new HashMap<String, TableInfo.Column>(4);
        _columnsSchedules.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSchedules.put("weekStartDate", new TableInfo.Column("weekStartDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSchedules.put("state", new TableInfo.Column("state", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSchedules.put("version", new TableInfo.Column("version", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSchedules = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSchedules = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSchedules = new TableInfo("schedules", _columnsSchedules, _foreignKeysSchedules, _indicesSchedules);
        final TableInfo _existingSchedules = TableInfo.read(db, "schedules");
        if (!_infoSchedules.equals(_existingSchedules)) {
          return new RoomOpenHelper.ValidationResult(false, "schedules(com.jsuka.breweryscheduler.data.local.entity.ScheduleEntity).\n"
                  + " Expected:\n" + _infoSchedules + "\n"
                  + " Found:\n" + _existingSchedules);
        }
        final HashMap<String, TableInfo.Column> _columnsScheduleAssignments = new HashMap<String, TableInfo.Column>(9);
        _columnsScheduleAssignments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleAssignments.put("scheduleId", new TableInfo.Column("scheduleId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleAssignments.put("personId", new TableInfo.Column("personId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleAssignments.put("jobPositionId", new TableInfo.Column("jobPositionId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleAssignments.put("shiftId", new TableInfo.Column("shiftId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleAssignments.put("state", new TableInfo.Column("state", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleAssignments.put("conflictReason", new TableInfo.Column("conflictReason", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleAssignments.put("overridden", new TableInfo.Column("overridden", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleAssignments.put("version", new TableInfo.Column("version", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysScheduleAssignments = new HashSet<TableInfo.ForeignKey>(4);
        _foreignKeysScheduleAssignments.add(new TableInfo.ForeignKey("schedules", "CASCADE", "NO ACTION", Arrays.asList("scheduleId"), Arrays.asList("id")));
        _foreignKeysScheduleAssignments.add(new TableInfo.ForeignKey("people", "CASCADE", "NO ACTION", Arrays.asList("personId"), Arrays.asList("id")));
        _foreignKeysScheduleAssignments.add(new TableInfo.ForeignKey("job_positions", "CASCADE", "NO ACTION", Arrays.asList("jobPositionId"), Arrays.asList("id")));
        _foreignKeysScheduleAssignments.add(new TableInfo.ForeignKey("shifts", "CASCADE", "NO ACTION", Arrays.asList("shiftId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesScheduleAssignments = new HashSet<TableInfo.Index>(4);
        _indicesScheduleAssignments.add(new TableInfo.Index("index_schedule_assignments_scheduleId", false, Arrays.asList("scheduleId"), Arrays.asList("ASC")));
        _indicesScheduleAssignments.add(new TableInfo.Index("index_schedule_assignments_personId", false, Arrays.asList("personId"), Arrays.asList("ASC")));
        _indicesScheduleAssignments.add(new TableInfo.Index("index_schedule_assignments_jobPositionId", false, Arrays.asList("jobPositionId"), Arrays.asList("ASC")));
        _indicesScheduleAssignments.add(new TableInfo.Index("index_schedule_assignments_shiftId", false, Arrays.asList("shiftId"), Arrays.asList("ASC")));
        final TableInfo _infoScheduleAssignments = new TableInfo("schedule_assignments", _columnsScheduleAssignments, _foreignKeysScheduleAssignments, _indicesScheduleAssignments);
        final TableInfo _existingScheduleAssignments = TableInfo.read(db, "schedule_assignments");
        if (!_infoScheduleAssignments.equals(_existingScheduleAssignments)) {
          return new RoomOpenHelper.ValidationResult(false, "schedule_assignments(com.jsuka.breweryscheduler.data.local.entity.ScheduleAssignmentEntity).\n"
                  + " Expected:\n" + _infoScheduleAssignments + "\n"
                  + " Found:\n" + _existingScheduleAssignments);
        }
        final HashMap<String, TableInfo.Column> _columnsUserAccounts = new HashMap<String, TableInfo.Column>(6);
        _columnsUserAccounts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserAccounts.put("personId", new TableInfo.Column("personId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserAccounts.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserAccounts.put("passwordSalt", new TableInfo.Column("passwordSalt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserAccounts.put("passwordHash", new TableInfo.Column("passwordHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserAccounts.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserAccounts = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysUserAccounts.add(new TableInfo.ForeignKey("people", "CASCADE", "NO ACTION", Arrays.asList("personId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesUserAccounts = new HashSet<TableInfo.Index>(2);
        _indicesUserAccounts.add(new TableInfo.Index("index_user_accounts_username", true, Arrays.asList("username"), Arrays.asList("ASC")));
        _indicesUserAccounts.add(new TableInfo.Index("index_user_accounts_personId", false, Arrays.asList("personId"), Arrays.asList("ASC")));
        final TableInfo _infoUserAccounts = new TableInfo("user_accounts", _columnsUserAccounts, _foreignKeysUserAccounts, _indicesUserAccounts);
        final TableInfo _existingUserAccounts = TableInfo.read(db, "user_accounts");
        if (!_infoUserAccounts.equals(_existingUserAccounts)) {
          return new RoomOpenHelper.ValidationResult(false, "user_accounts(com.jsuka.breweryscheduler.data.local.entity.UserAccountEntity).\n"
                  + " Expected:\n" + _infoUserAccounts + "\n"
                  + " Found:\n" + _existingUserAccounts);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "339f9c22f53da23686a4f268e132a850", "187772ebacf310620753ae7ce4daadd2");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "departments","job_positions","people","credentials","availability","shifts","schedules","schedule_assignments","user_accounts");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `departments`");
      _db.execSQL("DELETE FROM `job_positions`");
      _db.execSQL("DELETE FROM `people`");
      _db.execSQL("DELETE FROM `credentials`");
      _db.execSQL("DELETE FROM `availability`");
      _db.execSQL("DELETE FROM `shifts`");
      _db.execSQL("DELETE FROM `schedules`");
      _db.execSQL("DELETE FROM `schedule_assignments`");
      _db.execSQL("DELETE FROM `user_accounts`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(DepartmentDao.class, DepartmentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EmployeeDao.class, EmployeeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(QualificationDao.class, QualificationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ScheduleDao.class, ScheduleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserAccountDao.class, UserAccountDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public DepartmentDao departmentDao() {
    if (_departmentDao != null) {
      return _departmentDao;
    } else {
      synchronized(this) {
        if(_departmentDao == null) {
          _departmentDao = new DepartmentDao_Impl(this);
        }
        return _departmentDao;
      }
    }
  }

  @Override
  public EmployeeDao employeeDao() {
    if (_employeeDao != null) {
      return _employeeDao;
    } else {
      synchronized(this) {
        if(_employeeDao == null) {
          _employeeDao = new EmployeeDao_Impl(this);
        }
        return _employeeDao;
      }
    }
  }

  @Override
  public QualificationDao qualificationDao() {
    if (_qualificationDao != null) {
      return _qualificationDao;
    } else {
      synchronized(this) {
        if(_qualificationDao == null) {
          _qualificationDao = new QualificationDao_Impl(this);
        }
        return _qualificationDao;
      }
    }
  }

  @Override
  public ScheduleDao scheduleDao() {
    if (_scheduleDao != null) {
      return _scheduleDao;
    } else {
      synchronized(this) {
        if(_scheduleDao == null) {
          _scheduleDao = new ScheduleDao_Impl(this);
        }
        return _scheduleDao;
      }
    }
  }

  @Override
  public UserAccountDao userAccountDao() {
    if (_userAccountDao != null) {
      return _userAccountDao;
    } else {
      synchronized(this) {
        if(_userAccountDao == null) {
          _userAccountDao = new UserAccountDao_Impl(this);
        }
        return _userAccountDao;
      }
    }
  }
}
