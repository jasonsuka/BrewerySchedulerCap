package com.jsuka.breweryscheduler.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.jsuka.breweryscheduler.data.local.Converters;
import com.jsuka.breweryscheduler.data.local.entity.ScheduleAssignmentEntity;
import com.jsuka.breweryscheduler.data.local.entity.ScheduleEntity;
import com.jsuka.breweryscheduler.data.local.entity.ShiftEntity;
import com.jsuka.breweryscheduler.domain.model.AssignmentState;
import com.jsuka.breweryscheduler.domain.model.ScheduleState;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ScheduleDao_Impl implements ScheduleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ScheduleEntity> __insertionAdapterOfScheduleEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<ScheduleAssignmentEntity> __insertionAdapterOfScheduleAssignmentEntity;

  private final EntityInsertionAdapter<ShiftEntity> __insertionAdapterOfShiftEntity;

  private final EntityDeletionOrUpdateAdapter<ScheduleEntity> __deletionAdapterOfScheduleEntity;

  private final EntityDeletionOrUpdateAdapter<ScheduleAssignmentEntity> __deletionAdapterOfScheduleAssignmentEntity;

  private final EntityDeletionOrUpdateAdapter<ScheduleAssignmentEntity> __updateAdapterOfScheduleAssignmentEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStateIfCurrent;

  private final SharedSQLiteStatement __preparedStmtOfUpdateAssignmentIfCurrent;

  public ScheduleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScheduleEntity = new EntityInsertionAdapter<ScheduleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `schedules` (`id`,`weekStartDate`,`state`,`version`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduleEntity entity) {
        statement.bindLong(1, entity.getId());
        final Long _tmp = __converters.localDateToEpochDay(entity.getWeekStartDate());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, _tmp);
        }
        final String _tmp_1 = __converters.scheduleStateToName(entity.getState());
        statement.bindString(3, _tmp_1);
        statement.bindLong(4, entity.getVersion());
      }
    };
    this.__insertionAdapterOfScheduleAssignmentEntity = new EntityInsertionAdapter<ScheduleAssignmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `schedule_assignments` (`id`,`scheduleId`,`personId`,`jobPositionId`,`shiftId`,`state`,`conflictReason`,`overridden`,`version`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduleAssignmentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getScheduleId());
        statement.bindLong(3, entity.getPersonId());
        statement.bindLong(4, entity.getJobPositionId());
        statement.bindLong(5, entity.getShiftId());
        final String _tmp = __converters.assignmentStateToName(entity.getState());
        statement.bindString(6, _tmp);
        if (entity.getConflictReason() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getConflictReason());
        }
        final int _tmp_1 = entity.getOverridden() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        statement.bindLong(9, entity.getVersion());
      }
    };
    this.__insertionAdapterOfShiftEntity = new EntityInsertionAdapter<ShiftEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `shifts` (`id`,`departmentId`,`date`,`startMinute`,`endMinute`,`label`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ShiftEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDepartmentId());
        final Long _tmp = __converters.localDateToEpochDay(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp);
        }
        statement.bindLong(4, entity.getStartMinute());
        statement.bindLong(5, entity.getEndMinute());
        statement.bindString(6, entity.getLabel());
      }
    };
    this.__deletionAdapterOfScheduleEntity = new EntityDeletionOrUpdateAdapter<ScheduleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `schedules` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduleEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfScheduleAssignmentEntity = new EntityDeletionOrUpdateAdapter<ScheduleAssignmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `schedule_assignments` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduleAssignmentEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfScheduleAssignmentEntity = new EntityDeletionOrUpdateAdapter<ScheduleAssignmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `schedule_assignments` SET `id` = ?,`scheduleId` = ?,`personId` = ?,`jobPositionId` = ?,`shiftId` = ?,`state` = ?,`conflictReason` = ?,`overridden` = ?,`version` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduleAssignmentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getScheduleId());
        statement.bindLong(3, entity.getPersonId());
        statement.bindLong(4, entity.getJobPositionId());
        statement.bindLong(5, entity.getShiftId());
        final String _tmp = __converters.assignmentStateToName(entity.getState());
        statement.bindString(6, _tmp);
        if (entity.getConflictReason() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getConflictReason());
        }
        final int _tmp_1 = entity.getOverridden() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        statement.bindLong(9, entity.getVersion());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateStateIfCurrent = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE schedules\n"
                + "        SET state = ?, version = version + 1\n"
                + "        WHERE id = ? AND version = ?\n"
                + "        ";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateAssignmentIfCurrent = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE schedule_assignments\n"
                + "        SET state = ?, conflictReason = ?, version = version + 1\n"
                + "        WHERE id = ? AND version = ?\n"
                + "        ";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ScheduleEntity schedule,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfScheduleEntity.insertAndReturnId(schedule);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAssignment(final ScheduleAssignmentEntity assignment,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfScheduleAssignmentEntity.insertAndReturnId(assignment);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertShift(final ShiftEntity shift, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfShiftEntity.insertAndReturnId(shift);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final ScheduleEntity schedule,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfScheduleEntity.handle(schedule);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAssignment(final ScheduleAssignmentEntity assignment,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfScheduleAssignmentEntity.handle(assignment);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateAssignment(final ScheduleAssignmentEntity assignment,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __updateAdapterOfScheduleAssignmentEntity.handle(assignment);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object commitAssignments(final List<ScheduleAssignmentEntity> assignments,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> ScheduleDao.DefaultImpls.commitAssignments(ScheduleDao_Impl.this, assignments, __cont), $completion);
  }

  @Override
  public Object updateStateIfCurrent(final long id, final ScheduleState newState,
      final int expectedVersion, final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStateIfCurrent.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.scheduleStateToName(newState);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, expectedVersion);
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateStateIfCurrent.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateAssignmentIfCurrent(final long id, final AssignmentState newState,
      final String reason, final int expectedVersion,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateAssignmentIfCurrent.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.assignmentStateToName(newState);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        if (reason == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, reason);
        }
        _argIndex = 3;
        _stmt.bindLong(_argIndex, id);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, expectedVersion);
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateAssignmentIfCurrent.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object findById(final long id, final Continuation<? super ScheduleEntity> $completion) {
    final String _sql = "SELECT * FROM schedules WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ScheduleEntity>() {
      @Override
      @Nullable
      public ScheduleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWeekStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "weekStartDate");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
          final ScheduleEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDate _tmpWeekStartDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfWeekStartDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfWeekStartDate);
            }
            final LocalDate _tmp_1 = __converters.epochDayToLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpWeekStartDate = _tmp_1;
            }
            final ScheduleState _tmpState;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.nameToScheduleState(_tmp_2);
            final int _tmpVersion;
            _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
            _result = new ScheduleEntity(_tmpId,_tmpWeekStartDate,_tmpState,_tmpVersion);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object findAll(final Continuation<? super List<ScheduleEntity>> $completion) {
    final String _sql = "SELECT * FROM schedules ORDER BY weekStartDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ScheduleEntity>>() {
      @Override
      @NonNull
      public List<ScheduleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWeekStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "weekStartDate");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
          final List<ScheduleEntity> _result = new ArrayList<ScheduleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduleEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDate _tmpWeekStartDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfWeekStartDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfWeekStartDate);
            }
            final LocalDate _tmp_1 = __converters.epochDayToLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpWeekStartDate = _tmp_1;
            }
            final ScheduleState _tmpState;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.nameToScheduleState(_tmp_2);
            final int _tmpVersion;
            _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
            _item = new ScheduleEntity(_tmpId,_tmpWeekStartDate,_tmpState,_tmpVersion);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object findByWeek(final LocalDate weekStart,
      final Continuation<? super ScheduleEntity> $completion) {
    final String _sql = "SELECT * FROM schedules WHERE weekStartDate = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final Long _tmp = __converters.localDateToEpochDay(weekStart);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ScheduleEntity>() {
      @Override
      @Nullable
      public ScheduleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWeekStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "weekStartDate");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
          final ScheduleEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDate _tmpWeekStartDate;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfWeekStartDate)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfWeekStartDate);
            }
            final LocalDate _tmp_2 = __converters.epochDayToLocalDate(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpWeekStartDate = _tmp_2;
            }
            final ScheduleState _tmpState;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.nameToScheduleState(_tmp_3);
            final int _tmpVersion;
            _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
            _result = new ScheduleEntity(_tmpId,_tmpWeekStartDate,_tmpState,_tmpVersion);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ScheduleEntity>> observeByState(final ScheduleState state) {
    final String _sql = "SELECT * FROM schedules WHERE state = ? ORDER BY weekStartDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.scheduleStateToName(state);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"schedules"}, new Callable<List<ScheduleEntity>>() {
      @Override
      @NonNull
      public List<ScheduleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWeekStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "weekStartDate");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
          final List<ScheduleEntity> _result = new ArrayList<ScheduleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduleEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDate _tmpWeekStartDate;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfWeekStartDate)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfWeekStartDate);
            }
            final LocalDate _tmp_2 = __converters.epochDayToLocalDate(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpWeekStartDate = _tmp_2;
            }
            final ScheduleState _tmpState;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.nameToScheduleState(_tmp_3);
            final int _tmpVersion;
            _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
            _item = new ScheduleEntity(_tmpId,_tmpWeekStartDate,_tmpState,_tmpVersion);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object findAssignmentById(final long id,
      final Continuation<? super ScheduleAssignmentEntity> $completion) {
    final String _sql = "SELECT * FROM schedule_assignments WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ScheduleAssignmentEntity>() {
      @Override
      @Nullable
      public ScheduleAssignmentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfScheduleId = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleId");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfJobPositionId = CursorUtil.getColumnIndexOrThrow(_cursor, "jobPositionId");
          final int _cursorIndexOfShiftId = CursorUtil.getColumnIndexOrThrow(_cursor, "shiftId");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfConflictReason = CursorUtil.getColumnIndexOrThrow(_cursor, "conflictReason");
          final int _cursorIndexOfOverridden = CursorUtil.getColumnIndexOrThrow(_cursor, "overridden");
          final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
          final ScheduleAssignmentEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpScheduleId;
            _tmpScheduleId = _cursor.getLong(_cursorIndexOfScheduleId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final long _tmpJobPositionId;
            _tmpJobPositionId = _cursor.getLong(_cursorIndexOfJobPositionId);
            final long _tmpShiftId;
            _tmpShiftId = _cursor.getLong(_cursorIndexOfShiftId);
            final AssignmentState _tmpState;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.nameToAssignmentState(_tmp);
            final String _tmpConflictReason;
            if (_cursor.isNull(_cursorIndexOfConflictReason)) {
              _tmpConflictReason = null;
            } else {
              _tmpConflictReason = _cursor.getString(_cursorIndexOfConflictReason);
            }
            final boolean _tmpOverridden;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfOverridden);
            _tmpOverridden = _tmp_1 != 0;
            final int _tmpVersion;
            _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
            _result = new ScheduleAssignmentEntity(_tmpId,_tmpScheduleId,_tmpPersonId,_tmpJobPositionId,_tmpShiftId,_tmpState,_tmpConflictReason,_tmpOverridden,_tmpVersion);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object findAssignmentsFor(final long scheduleId,
      final Continuation<? super List<ScheduleAssignmentEntity>> $completion) {
    final String _sql = "SELECT * FROM schedule_assignments WHERE scheduleId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, scheduleId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ScheduleAssignmentEntity>>() {
      @Override
      @NonNull
      public List<ScheduleAssignmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfScheduleId = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleId");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfJobPositionId = CursorUtil.getColumnIndexOrThrow(_cursor, "jobPositionId");
          final int _cursorIndexOfShiftId = CursorUtil.getColumnIndexOrThrow(_cursor, "shiftId");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfConflictReason = CursorUtil.getColumnIndexOrThrow(_cursor, "conflictReason");
          final int _cursorIndexOfOverridden = CursorUtil.getColumnIndexOrThrow(_cursor, "overridden");
          final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
          final List<ScheduleAssignmentEntity> _result = new ArrayList<ScheduleAssignmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduleAssignmentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpScheduleId;
            _tmpScheduleId = _cursor.getLong(_cursorIndexOfScheduleId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final long _tmpJobPositionId;
            _tmpJobPositionId = _cursor.getLong(_cursorIndexOfJobPositionId);
            final long _tmpShiftId;
            _tmpShiftId = _cursor.getLong(_cursorIndexOfShiftId);
            final AssignmentState _tmpState;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.nameToAssignmentState(_tmp);
            final String _tmpConflictReason;
            if (_cursor.isNull(_cursorIndexOfConflictReason)) {
              _tmpConflictReason = null;
            } else {
              _tmpConflictReason = _cursor.getString(_cursorIndexOfConflictReason);
            }
            final boolean _tmpOverridden;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfOverridden);
            _tmpOverridden = _tmp_1 != 0;
            final int _tmpVersion;
            _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
            _item = new ScheduleAssignmentEntity(_tmpId,_tmpScheduleId,_tmpPersonId,_tmpJobPositionId,_tmpShiftId,_tmpState,_tmpConflictReason,_tmpOverridden,_tmpVersion);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ScheduleAssignmentEntity>> observeAssignmentsFor(final long scheduleId) {
    final String _sql = "SELECT * FROM schedule_assignments WHERE scheduleId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, scheduleId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"schedule_assignments"}, new Callable<List<ScheduleAssignmentEntity>>() {
      @Override
      @NonNull
      public List<ScheduleAssignmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfScheduleId = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleId");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfJobPositionId = CursorUtil.getColumnIndexOrThrow(_cursor, "jobPositionId");
          final int _cursorIndexOfShiftId = CursorUtil.getColumnIndexOrThrow(_cursor, "shiftId");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfConflictReason = CursorUtil.getColumnIndexOrThrow(_cursor, "conflictReason");
          final int _cursorIndexOfOverridden = CursorUtil.getColumnIndexOrThrow(_cursor, "overridden");
          final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
          final List<ScheduleAssignmentEntity> _result = new ArrayList<ScheduleAssignmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduleAssignmentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpScheduleId;
            _tmpScheduleId = _cursor.getLong(_cursorIndexOfScheduleId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final long _tmpJobPositionId;
            _tmpJobPositionId = _cursor.getLong(_cursorIndexOfJobPositionId);
            final long _tmpShiftId;
            _tmpShiftId = _cursor.getLong(_cursorIndexOfShiftId);
            final AssignmentState _tmpState;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.nameToAssignmentState(_tmp);
            final String _tmpConflictReason;
            if (_cursor.isNull(_cursorIndexOfConflictReason)) {
              _tmpConflictReason = null;
            } else {
              _tmpConflictReason = _cursor.getString(_cursorIndexOfConflictReason);
            }
            final boolean _tmpOverridden;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfOverridden);
            _tmpOverridden = _tmp_1 != 0;
            final int _tmpVersion;
            _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
            _item = new ScheduleAssignmentEntity(_tmpId,_tmpScheduleId,_tmpPersonId,_tmpJobPositionId,_tmpShiftId,_tmpState,_tmpConflictReason,_tmpOverridden,_tmpVersion);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object findAssignmentsForPerson(final long personId,
      final Continuation<? super List<ScheduleAssignmentEntity>> $completion) {
    final String _sql = "SELECT * FROM schedule_assignments WHERE personId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, personId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ScheduleAssignmentEntity>>() {
      @Override
      @NonNull
      public List<ScheduleAssignmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfScheduleId = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleId");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfJobPositionId = CursorUtil.getColumnIndexOrThrow(_cursor, "jobPositionId");
          final int _cursorIndexOfShiftId = CursorUtil.getColumnIndexOrThrow(_cursor, "shiftId");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfConflictReason = CursorUtil.getColumnIndexOrThrow(_cursor, "conflictReason");
          final int _cursorIndexOfOverridden = CursorUtil.getColumnIndexOrThrow(_cursor, "overridden");
          final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
          final List<ScheduleAssignmentEntity> _result = new ArrayList<ScheduleAssignmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduleAssignmentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpScheduleId;
            _tmpScheduleId = _cursor.getLong(_cursorIndexOfScheduleId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final long _tmpJobPositionId;
            _tmpJobPositionId = _cursor.getLong(_cursorIndexOfJobPositionId);
            final long _tmpShiftId;
            _tmpShiftId = _cursor.getLong(_cursorIndexOfShiftId);
            final AssignmentState _tmpState;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfState);
            _tmpState = __converters.nameToAssignmentState(_tmp);
            final String _tmpConflictReason;
            if (_cursor.isNull(_cursorIndexOfConflictReason)) {
              _tmpConflictReason = null;
            } else {
              _tmpConflictReason = _cursor.getString(_cursorIndexOfConflictReason);
            }
            final boolean _tmpOverridden;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfOverridden);
            _tmpOverridden = _tmp_1 != 0;
            final int _tmpVersion;
            _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
            _item = new ScheduleAssignmentEntity(_tmpId,_tmpScheduleId,_tmpPersonId,_tmpJobPositionId,_tmpShiftId,_tmpState,_tmpConflictReason,_tmpOverridden,_tmpVersion);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countAssignmentsInState(final long scheduleId, final AssignmentState state,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM schedule_assignments\n"
            + "        WHERE scheduleId = ? AND state = ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, scheduleId);
    _argIndex = 2;
    final String _tmp = __converters.assignmentStateToName(state);
    _statement.bindString(_argIndex, _tmp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(0);
            _result = _tmp_1;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object findShiftById(final long id, final Continuation<? super ShiftEntity> $completion) {
    final String _sql = "SELECT * FROM shifts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ShiftEntity>() {
      @Override
      @Nullable
      public ShiftEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDepartmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "departmentId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStartMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "startMinute");
          final int _cursorIndexOfEndMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "endMinute");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final ShiftEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDepartmentId;
            _tmpDepartmentId = _cursor.getLong(_cursorIndexOfDepartmentId);
            final LocalDate _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            final LocalDate _tmp_1 = __converters.epochDayToLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpDate = _tmp_1;
            }
            final int _tmpStartMinute;
            _tmpStartMinute = _cursor.getInt(_cursorIndexOfStartMinute);
            final int _tmpEndMinute;
            _tmpEndMinute = _cursor.getInt(_cursorIndexOfEndMinute);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            _result = new ShiftEntity(_tmpId,_tmpDepartmentId,_tmpDate,_tmpStartMinute,_tmpEndMinute,_tmpLabel);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object findShiftsFor(final long departmentId,
      final Continuation<? super List<ShiftEntity>> $completion) {
    final String _sql = "SELECT * FROM shifts WHERE departmentId = ? ORDER BY date, startMinute";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, departmentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ShiftEntity>>() {
      @Override
      @NonNull
      public List<ShiftEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDepartmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "departmentId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStartMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "startMinute");
          final int _cursorIndexOfEndMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "endMinute");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final List<ShiftEntity> _result = new ArrayList<ShiftEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ShiftEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDepartmentId;
            _tmpDepartmentId = _cursor.getLong(_cursorIndexOfDepartmentId);
            final LocalDate _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            final LocalDate _tmp_1 = __converters.epochDayToLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpDate = _tmp_1;
            }
            final int _tmpStartMinute;
            _tmpStartMinute = _cursor.getInt(_cursorIndexOfStartMinute);
            final int _tmpEndMinute;
            _tmpEndMinute = _cursor.getInt(_cursorIndexOfEndMinute);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            _item = new ShiftEntity(_tmpId,_tmpDepartmentId,_tmpDate,_tmpStartMinute,_tmpEndMinute,_tmpLabel);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object findShiftsBetween(final LocalDate from, final LocalDate to,
      final Continuation<? super List<ShiftEntity>> $completion) {
    final String _sql = "SELECT * FROM shifts WHERE date BETWEEN ? AND ? ORDER BY date, startMinute";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final Long _tmp = __converters.localDateToEpochDay(from);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 2;
    final Long _tmp_1 = __converters.localDateToEpochDay(to);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp_1);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ShiftEntity>>() {
      @Override
      @NonNull
      public List<ShiftEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDepartmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "departmentId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStartMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "startMinute");
          final int _cursorIndexOfEndMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "endMinute");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final List<ShiftEntity> _result = new ArrayList<ShiftEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ShiftEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDepartmentId;
            _tmpDepartmentId = _cursor.getLong(_cursorIndexOfDepartmentId);
            final LocalDate _tmpDate;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfDate);
            }
            final LocalDate _tmp_3 = __converters.epochDayToLocalDate(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpDate = _tmp_3;
            }
            final int _tmpStartMinute;
            _tmpStartMinute = _cursor.getInt(_cursorIndexOfStartMinute);
            final int _tmpEndMinute;
            _tmpEndMinute = _cursor.getInt(_cursorIndexOfEndMinute);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            _item = new ShiftEntity(_tmpId,_tmpDepartmentId,_tmpDate,_tmpStartMinute,_tmpEndMinute,_tmpLabel);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
