package com.jsuka.breweryscheduler.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.jsuka.breweryscheduler.data.local.Converters;
import com.jsuka.breweryscheduler.data.local.entity.AvailabilityEntity;
import com.jsuka.breweryscheduler.data.local.entity.PersonEntity;
import com.jsuka.breweryscheduler.domain.model.PersonRole;
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
public final class EmployeeDao_Impl implements EmployeeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PersonEntity> __insertionAdapterOfPersonEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<AvailabilityEntity> __insertionAdapterOfAvailabilityEntity;

  private final EntityDeletionOrUpdateAdapter<PersonEntity> __deletionAdapterOfPersonEntity;

  private final EntityDeletionOrUpdateAdapter<PersonEntity> __updateAdapterOfPersonEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearAvailabilityFor;

  public EmployeeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPersonEntity = new EntityInsertionAdapter<PersonEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `people` (`id`,`firstName`,`lastName`,`email`,`hireDate`,`role`,`homeDepartmentId`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PersonEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getFirstName());
        statement.bindString(3, entity.getLastName());
        statement.bindString(4, entity.getEmail());
        final Long _tmp = __converters.localDateToEpochDay(entity.getHireDate());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        final String _tmp_1 = __converters.personRoleToName(entity.getRole());
        statement.bindString(6, _tmp_1);
        if (entity.getHomeDepartmentId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getHomeDepartmentId());
        }
      }
    };
    this.__insertionAdapterOfAvailabilityEntity = new EntityInsertionAdapter<AvailabilityEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `availability` (`id`,`personId`,`dayOfWeek`,`startMinute`,`endMinute`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AvailabilityEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPersonId());
        statement.bindLong(3, entity.getDayOfWeek());
        statement.bindLong(4, entity.getStartMinute());
        statement.bindLong(5, entity.getEndMinute());
      }
    };
    this.__deletionAdapterOfPersonEntity = new EntityDeletionOrUpdateAdapter<PersonEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `people` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PersonEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPersonEntity = new EntityDeletionOrUpdateAdapter<PersonEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `people` SET `id` = ?,`firstName` = ?,`lastName` = ?,`email` = ?,`hireDate` = ?,`role` = ?,`homeDepartmentId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PersonEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getFirstName());
        statement.bindString(3, entity.getLastName());
        statement.bindString(4, entity.getEmail());
        final Long _tmp = __converters.localDateToEpochDay(entity.getHireDate());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        final String _tmp_1 = __converters.personRoleToName(entity.getRole());
        statement.bindString(6, _tmp_1);
        if (entity.getHomeDepartmentId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getHomeDepartmentId());
        }
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfClearAvailabilityFor = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM availability WHERE personId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final PersonEntity person, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPersonEntity.insertAndReturnId(person);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAvailability(final AvailabilityEntity availability,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAvailabilityEntity.insertAndReturnId(availability);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final PersonEntity person, final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfPersonEntity.handle(person);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final PersonEntity person, final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __updateAdapterOfPersonEntity.handle(person);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAvailabilityFor(final long personId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAvailabilityFor.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, personId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAvailabilityFor.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object findById(final long id, final Continuation<? super PersonEntity> $completion) {
    final String _sql = "SELECT * FROM people WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PersonEntity>() {
      @Override
      @Nullable
      public PersonEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFirstName = CursorUtil.getColumnIndexOrThrow(_cursor, "firstName");
          final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastName");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfHireDate = CursorUtil.getColumnIndexOrThrow(_cursor, "hireDate");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfHomeDepartmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeDepartmentId");
          final PersonEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFirstName;
            _tmpFirstName = _cursor.getString(_cursorIndexOfFirstName);
            final String _tmpLastName;
            _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final LocalDate _tmpHireDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfHireDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfHireDate);
            }
            final LocalDate _tmp_1 = __converters.epochDayToLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpHireDate = _tmp_1;
            }
            final PersonRole _tmpRole;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfRole);
            _tmpRole = __converters.nameToPersonRole(_tmp_2);
            final Long _tmpHomeDepartmentId;
            if (_cursor.isNull(_cursorIndexOfHomeDepartmentId)) {
              _tmpHomeDepartmentId = null;
            } else {
              _tmpHomeDepartmentId = _cursor.getLong(_cursorIndexOfHomeDepartmentId);
            }
            _result = new PersonEntity(_tmpId,_tmpFirstName,_tmpLastName,_tmpEmail,_tmpHireDate,_tmpRole,_tmpHomeDepartmentId);
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
  public Object findAll(final Continuation<? super List<PersonEntity>> $completion) {
    final String _sql = "SELECT * FROM people ORDER BY lastName, firstName";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PersonEntity>>() {
      @Override
      @NonNull
      public List<PersonEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFirstName = CursorUtil.getColumnIndexOrThrow(_cursor, "firstName");
          final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastName");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfHireDate = CursorUtil.getColumnIndexOrThrow(_cursor, "hireDate");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfHomeDepartmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeDepartmentId");
          final List<PersonEntity> _result = new ArrayList<PersonEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PersonEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFirstName;
            _tmpFirstName = _cursor.getString(_cursorIndexOfFirstName);
            final String _tmpLastName;
            _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final LocalDate _tmpHireDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfHireDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfHireDate);
            }
            final LocalDate _tmp_1 = __converters.epochDayToLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpHireDate = _tmp_1;
            }
            final PersonRole _tmpRole;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfRole);
            _tmpRole = __converters.nameToPersonRole(_tmp_2);
            final Long _tmpHomeDepartmentId;
            if (_cursor.isNull(_cursorIndexOfHomeDepartmentId)) {
              _tmpHomeDepartmentId = null;
            } else {
              _tmpHomeDepartmentId = _cursor.getLong(_cursorIndexOfHomeDepartmentId);
            }
            _item = new PersonEntity(_tmpId,_tmpFirstName,_tmpLastName,_tmpEmail,_tmpHireDate,_tmpRole,_tmpHomeDepartmentId);
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
  public Flow<List<PersonEntity>> observeByRole(final PersonRole role) {
    final String _sql = "SELECT * FROM people WHERE role = ? ORDER BY lastName, firstName";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.personRoleToName(role);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"people"}, new Callable<List<PersonEntity>>() {
      @Override
      @NonNull
      public List<PersonEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFirstName = CursorUtil.getColumnIndexOrThrow(_cursor, "firstName");
          final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastName");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfHireDate = CursorUtil.getColumnIndexOrThrow(_cursor, "hireDate");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfHomeDepartmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeDepartmentId");
          final List<PersonEntity> _result = new ArrayList<PersonEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PersonEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFirstName;
            _tmpFirstName = _cursor.getString(_cursorIndexOfFirstName);
            final String _tmpLastName;
            _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final LocalDate _tmpHireDate;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfHireDate)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfHireDate);
            }
            final LocalDate _tmp_2 = __converters.epochDayToLocalDate(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpHireDate = _tmp_2;
            }
            final PersonRole _tmpRole;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfRole);
            _tmpRole = __converters.nameToPersonRole(_tmp_3);
            final Long _tmpHomeDepartmentId;
            if (_cursor.isNull(_cursorIndexOfHomeDepartmentId)) {
              _tmpHomeDepartmentId = null;
            } else {
              _tmpHomeDepartmentId = _cursor.getLong(_cursorIndexOfHomeDepartmentId);
            }
            _item = new PersonEntity(_tmpId,_tmpFirstName,_tmpLastName,_tmpEmail,_tmpHireDate,_tmpRole,_tmpHomeDepartmentId);
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
  public Object findByDepartment(final long departmentId,
      final Continuation<? super List<PersonEntity>> $completion) {
    final String _sql = "SELECT * FROM people WHERE homeDepartmentId = ? ORDER BY lastName";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, departmentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PersonEntity>>() {
      @Override
      @NonNull
      public List<PersonEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFirstName = CursorUtil.getColumnIndexOrThrow(_cursor, "firstName");
          final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastName");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfHireDate = CursorUtil.getColumnIndexOrThrow(_cursor, "hireDate");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfHomeDepartmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeDepartmentId");
          final List<PersonEntity> _result = new ArrayList<PersonEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PersonEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFirstName;
            _tmpFirstName = _cursor.getString(_cursorIndexOfFirstName);
            final String _tmpLastName;
            _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final LocalDate _tmpHireDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfHireDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfHireDate);
            }
            final LocalDate _tmp_1 = __converters.epochDayToLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpHireDate = _tmp_1;
            }
            final PersonRole _tmpRole;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfRole);
            _tmpRole = __converters.nameToPersonRole(_tmp_2);
            final Long _tmpHomeDepartmentId;
            if (_cursor.isNull(_cursorIndexOfHomeDepartmentId)) {
              _tmpHomeDepartmentId = null;
            } else {
              _tmpHomeDepartmentId = _cursor.getLong(_cursorIndexOfHomeDepartmentId);
            }
            _item = new PersonEntity(_tmpId,_tmpFirstName,_tmpLastName,_tmpEmail,_tmpHireDate,_tmpRole,_tmpHomeDepartmentId);
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
  public Object countInDepartment(final long departmentId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM people WHERE homeDepartmentId = ? AND role = 'EMPLOYEE'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, departmentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
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
  public Object findAvailabilityFor(final long personId,
      final Continuation<? super List<AvailabilityEntity>> $completion) {
    final String _sql = "SELECT * FROM availability WHERE personId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, personId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AvailabilityEntity>>() {
      @Override
      @NonNull
      public List<AvailabilityEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfDayOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfWeek");
          final int _cursorIndexOfStartMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "startMinute");
          final int _cursorIndexOfEndMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "endMinute");
          final List<AvailabilityEntity> _result = new ArrayList<AvailabilityEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AvailabilityEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final int _tmpDayOfWeek;
            _tmpDayOfWeek = _cursor.getInt(_cursorIndexOfDayOfWeek);
            final int _tmpStartMinute;
            _tmpStartMinute = _cursor.getInt(_cursorIndexOfStartMinute);
            final int _tmpEndMinute;
            _tmpEndMinute = _cursor.getInt(_cursorIndexOfEndMinute);
            _item = new AvailabilityEntity(_tmpId,_tmpPersonId,_tmpDayOfWeek,_tmpStartMinute,_tmpEndMinute);
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
