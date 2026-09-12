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
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.jsuka.breweryscheduler.data.local.entity.DepartmentEntity;
import com.jsuka.breweryscheduler.data.local.entity.JobPositionEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DepartmentDao_Impl implements DepartmentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DepartmentEntity> __insertionAdapterOfDepartmentEntity;

  private final EntityInsertionAdapter<JobPositionEntity> __insertionAdapterOfJobPositionEntity;

  private final EntityDeletionOrUpdateAdapter<DepartmentEntity> __deletionAdapterOfDepartmentEntity;

  private final EntityDeletionOrUpdateAdapter<DepartmentEntity> __updateAdapterOfDepartmentEntity;

  public DepartmentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDepartmentEntity = new EntityInsertionAdapter<DepartmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `departments` (`id`,`name`,`minimumStaffing`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DepartmentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getMinimumStaffing());
      }
    };
    this.__insertionAdapterOfJobPositionEntity = new EntityInsertionAdapter<JobPositionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `job_positions` (`id`,`departmentId`,`title`,`requiredCredentialCode`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final JobPositionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDepartmentId());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getRequiredCredentialCode());
      }
    };
    this.__deletionAdapterOfDepartmentEntity = new EntityDeletionOrUpdateAdapter<DepartmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `departments` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DepartmentEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfDepartmentEntity = new EntityDeletionOrUpdateAdapter<DepartmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `departments` SET `id` = ?,`name` = ?,`minimumStaffing` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DepartmentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getMinimumStaffing());
        statement.bindLong(4, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final DepartmentEntity department,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfDepartmentEntity.insertAndReturnId(department);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertPosition(final JobPositionEntity position,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfJobPositionEntity.insertAndReturnId(position);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final DepartmentEntity department,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfDepartmentEntity.handle(department);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final DepartmentEntity department,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __updateAdapterOfDepartmentEntity.handle(department);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object findById(final long id, final Continuation<? super DepartmentEntity> $completion) {
    final String _sql = "SELECT * FROM departments WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DepartmentEntity>() {
      @Override
      @Nullable
      public DepartmentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfMinimumStaffing = CursorUtil.getColumnIndexOrThrow(_cursor, "minimumStaffing");
          final DepartmentEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpMinimumStaffing;
            _tmpMinimumStaffing = _cursor.getInt(_cursorIndexOfMinimumStaffing);
            _result = new DepartmentEntity(_tmpId,_tmpName,_tmpMinimumStaffing);
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
  public Object findAll(final Continuation<? super List<DepartmentEntity>> $completion) {
    final String _sql = "SELECT * FROM departments ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DepartmentEntity>>() {
      @Override
      @NonNull
      public List<DepartmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfMinimumStaffing = CursorUtil.getColumnIndexOrThrow(_cursor, "minimumStaffing");
          final List<DepartmentEntity> _result = new ArrayList<DepartmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DepartmentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpMinimumStaffing;
            _tmpMinimumStaffing = _cursor.getInt(_cursorIndexOfMinimumStaffing);
            _item = new DepartmentEntity(_tmpId,_tmpName,_tmpMinimumStaffing);
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
  public Flow<List<DepartmentEntity>> observeAll() {
    final String _sql = "SELECT * FROM departments ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"departments"}, new Callable<List<DepartmentEntity>>() {
      @Override
      @NonNull
      public List<DepartmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfMinimumStaffing = CursorUtil.getColumnIndexOrThrow(_cursor, "minimumStaffing");
          final List<DepartmentEntity> _result = new ArrayList<DepartmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DepartmentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpMinimumStaffing;
            _tmpMinimumStaffing = _cursor.getInt(_cursorIndexOfMinimumStaffing);
            _item = new DepartmentEntity(_tmpId,_tmpName,_tmpMinimumStaffing);
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
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM departments";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
  public Object findPositionsFor(final long departmentId,
      final Continuation<? super List<JobPositionEntity>> $completion) {
    final String _sql = "SELECT * FROM job_positions WHERE departmentId = ? ORDER BY title";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, departmentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<JobPositionEntity>>() {
      @Override
      @NonNull
      public List<JobPositionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDepartmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "departmentId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfRequiredCredentialCode = CursorUtil.getColumnIndexOrThrow(_cursor, "requiredCredentialCode");
          final List<JobPositionEntity> _result = new ArrayList<JobPositionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JobPositionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDepartmentId;
            _tmpDepartmentId = _cursor.getLong(_cursorIndexOfDepartmentId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpRequiredCredentialCode;
            _tmpRequiredCredentialCode = _cursor.getString(_cursorIndexOfRequiredCredentialCode);
            _item = new JobPositionEntity(_tmpId,_tmpDepartmentId,_tmpTitle,_tmpRequiredCredentialCode);
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
  public Flow<List<JobPositionEntity>> observeAllPositions() {
    final String _sql = "SELECT * FROM job_positions ORDER BY title";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"job_positions"}, new Callable<List<JobPositionEntity>>() {
      @Override
      @NonNull
      public List<JobPositionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDepartmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "departmentId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfRequiredCredentialCode = CursorUtil.getColumnIndexOrThrow(_cursor, "requiredCredentialCode");
          final List<JobPositionEntity> _result = new ArrayList<JobPositionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JobPositionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDepartmentId;
            _tmpDepartmentId = _cursor.getLong(_cursorIndexOfDepartmentId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpRequiredCredentialCode;
            _tmpRequiredCredentialCode = _cursor.getString(_cursorIndexOfRequiredCredentialCode);
            _item = new JobPositionEntity(_tmpId,_tmpDepartmentId,_tmpTitle,_tmpRequiredCredentialCode);
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
  public Object findPositionById(final long id,
      final Continuation<? super JobPositionEntity> $completion) {
    final String _sql = "SELECT * FROM job_positions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<JobPositionEntity>() {
      @Override
      @Nullable
      public JobPositionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDepartmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "departmentId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfRequiredCredentialCode = CursorUtil.getColumnIndexOrThrow(_cursor, "requiredCredentialCode");
          final JobPositionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDepartmentId;
            _tmpDepartmentId = _cursor.getLong(_cursorIndexOfDepartmentId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpRequiredCredentialCode;
            _tmpRequiredCredentialCode = _cursor.getString(_cursorIndexOfRequiredCredentialCode);
            _result = new JobPositionEntity(_tmpId,_tmpDepartmentId,_tmpTitle,_tmpRequiredCredentialCode);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
