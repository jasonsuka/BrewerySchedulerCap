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
import com.jsuka.breweryscheduler.data.local.Converters;
import com.jsuka.breweryscheduler.data.local.entity.CredentialEntity;
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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class QualificationDao_Impl implements QualificationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CredentialEntity> __insertionAdapterOfCredentialEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<CredentialEntity> __deletionAdapterOfCredentialEntity;

  private final EntityDeletionOrUpdateAdapter<CredentialEntity> __updateAdapterOfCredentialEntity;

  public QualificationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCredentialEntity = new EntityInsertionAdapter<CredentialEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `credentials` (`id`,`personId`,`code`,`name`,`issuingAuthority`,`earnedDate`,`isCertification`,`expirationDate`,`renewalRequired`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CredentialEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPersonId());
        statement.bindString(3, entity.getCode());
        statement.bindString(4, entity.getName());
        statement.bindString(5, entity.getIssuingAuthority());
        final Long _tmp = __converters.localDateToEpochDay(entity.getEarnedDate());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, _tmp);
        }
        final int _tmp_1 = entity.isCertification() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        final Long _tmp_2 = __converters.localDateToEpochDay(entity.getExpirationDate());
        if (_tmp_2 == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, _tmp_2);
        }
        final int _tmp_3 = entity.getRenewalRequired() ? 1 : 0;
        statement.bindLong(9, _tmp_3);
      }
    };
    this.__deletionAdapterOfCredentialEntity = new EntityDeletionOrUpdateAdapter<CredentialEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `credentials` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CredentialEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCredentialEntity = new EntityDeletionOrUpdateAdapter<CredentialEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `credentials` SET `id` = ?,`personId` = ?,`code` = ?,`name` = ?,`issuingAuthority` = ?,`earnedDate` = ?,`isCertification` = ?,`expirationDate` = ?,`renewalRequired` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CredentialEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPersonId());
        statement.bindString(3, entity.getCode());
        statement.bindString(4, entity.getName());
        statement.bindString(5, entity.getIssuingAuthority());
        final Long _tmp = __converters.localDateToEpochDay(entity.getEarnedDate());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, _tmp);
        }
        final int _tmp_1 = entity.isCertification() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        final Long _tmp_2 = __converters.localDateToEpochDay(entity.getExpirationDate());
        if (_tmp_2 == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, _tmp_2);
        }
        final int _tmp_3 = entity.getRenewalRequired() ? 1 : 0;
        statement.bindLong(9, _tmp_3);
        statement.bindLong(10, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final CredentialEntity credential,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCredentialEntity.insertAndReturnId(credential);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final CredentialEntity credential,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfCredentialEntity.handle(credential);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final CredentialEntity credential,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __updateAdapterOfCredentialEntity.handle(credential);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object findById(final long id, final Continuation<? super CredentialEntity> $completion) {
    final String _sql = "SELECT * FROM credentials WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CredentialEntity>() {
      @Override
      @Nullable
      public CredentialEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIssuingAuthority = CursorUtil.getColumnIndexOrThrow(_cursor, "issuingAuthority");
          final int _cursorIndexOfEarnedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "earnedDate");
          final int _cursorIndexOfIsCertification = CursorUtil.getColumnIndexOrThrow(_cursor, "isCertification");
          final int _cursorIndexOfExpirationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expirationDate");
          final int _cursorIndexOfRenewalRequired = CursorUtil.getColumnIndexOrThrow(_cursor, "renewalRequired");
          final CredentialEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final String _tmpCode;
            _tmpCode = _cursor.getString(_cursorIndexOfCode);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpIssuingAuthority;
            _tmpIssuingAuthority = _cursor.getString(_cursorIndexOfIssuingAuthority);
            final LocalDate _tmpEarnedDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfEarnedDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfEarnedDate);
            }
            final LocalDate _tmp_1 = __converters.epochDayToLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpEarnedDate = _tmp_1;
            }
            final boolean _tmpIsCertification;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCertification);
            _tmpIsCertification = _tmp_2 != 0;
            final LocalDate _tmpExpirationDate;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfExpirationDate)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfExpirationDate);
            }
            _tmpExpirationDate = __converters.epochDayToLocalDate(_tmp_3);
            final boolean _tmpRenewalRequired;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfRenewalRequired);
            _tmpRenewalRequired = _tmp_4 != 0;
            _result = new CredentialEntity(_tmpId,_tmpPersonId,_tmpCode,_tmpName,_tmpIssuingAuthority,_tmpEarnedDate,_tmpIsCertification,_tmpExpirationDate,_tmpRenewalRequired);
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
  public Object findAll(final Continuation<? super List<CredentialEntity>> $completion) {
    final String _sql = "SELECT * FROM credentials";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CredentialEntity>>() {
      @Override
      @NonNull
      public List<CredentialEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIssuingAuthority = CursorUtil.getColumnIndexOrThrow(_cursor, "issuingAuthority");
          final int _cursorIndexOfEarnedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "earnedDate");
          final int _cursorIndexOfIsCertification = CursorUtil.getColumnIndexOrThrow(_cursor, "isCertification");
          final int _cursorIndexOfExpirationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expirationDate");
          final int _cursorIndexOfRenewalRequired = CursorUtil.getColumnIndexOrThrow(_cursor, "renewalRequired");
          final List<CredentialEntity> _result = new ArrayList<CredentialEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CredentialEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final String _tmpCode;
            _tmpCode = _cursor.getString(_cursorIndexOfCode);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpIssuingAuthority;
            _tmpIssuingAuthority = _cursor.getString(_cursorIndexOfIssuingAuthority);
            final LocalDate _tmpEarnedDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfEarnedDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfEarnedDate);
            }
            final LocalDate _tmp_1 = __converters.epochDayToLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpEarnedDate = _tmp_1;
            }
            final boolean _tmpIsCertification;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCertification);
            _tmpIsCertification = _tmp_2 != 0;
            final LocalDate _tmpExpirationDate;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfExpirationDate)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfExpirationDate);
            }
            _tmpExpirationDate = __converters.epochDayToLocalDate(_tmp_3);
            final boolean _tmpRenewalRequired;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfRenewalRequired);
            _tmpRenewalRequired = _tmp_4 != 0;
            _item = new CredentialEntity(_tmpId,_tmpPersonId,_tmpCode,_tmpName,_tmpIssuingAuthority,_tmpEarnedDate,_tmpIsCertification,_tmpExpirationDate,_tmpRenewalRequired);
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
  public Object findForPerson(final long personId,
      final Continuation<? super List<CredentialEntity>> $completion) {
    final String _sql = "SELECT * FROM credentials WHERE personId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, personId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CredentialEntity>>() {
      @Override
      @NonNull
      public List<CredentialEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIssuingAuthority = CursorUtil.getColumnIndexOrThrow(_cursor, "issuingAuthority");
          final int _cursorIndexOfEarnedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "earnedDate");
          final int _cursorIndexOfIsCertification = CursorUtil.getColumnIndexOrThrow(_cursor, "isCertification");
          final int _cursorIndexOfExpirationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expirationDate");
          final int _cursorIndexOfRenewalRequired = CursorUtil.getColumnIndexOrThrow(_cursor, "renewalRequired");
          final List<CredentialEntity> _result = new ArrayList<CredentialEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CredentialEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final String _tmpCode;
            _tmpCode = _cursor.getString(_cursorIndexOfCode);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpIssuingAuthority;
            _tmpIssuingAuthority = _cursor.getString(_cursorIndexOfIssuingAuthority);
            final LocalDate _tmpEarnedDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfEarnedDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfEarnedDate);
            }
            final LocalDate _tmp_1 = __converters.epochDayToLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpEarnedDate = _tmp_1;
            }
            final boolean _tmpIsCertification;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCertification);
            _tmpIsCertification = _tmp_2 != 0;
            final LocalDate _tmpExpirationDate;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfExpirationDate)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfExpirationDate);
            }
            _tmpExpirationDate = __converters.epochDayToLocalDate(_tmp_3);
            final boolean _tmpRenewalRequired;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfRenewalRequired);
            _tmpRenewalRequired = _tmp_4 != 0;
            _item = new CredentialEntity(_tmpId,_tmpPersonId,_tmpCode,_tmpName,_tmpIssuingAuthority,_tmpEarnedDate,_tmpIsCertification,_tmpExpirationDate,_tmpRenewalRequired);
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
  public Object findExpiringThrough(final LocalDate throughDate,
      final Continuation<? super List<CredentialEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM credentials\n"
            + "        WHERE isCertification = 1\n"
            + "          AND expirationDate IS NOT NULL\n"
            + "          AND expirationDate <= ?\n"
            + "        ORDER BY expirationDate\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final Long _tmp = __converters.localDateToEpochDay(throughDate);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CredentialEntity>>() {
      @Override
      @NonNull
      public List<CredentialEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIssuingAuthority = CursorUtil.getColumnIndexOrThrow(_cursor, "issuingAuthority");
          final int _cursorIndexOfEarnedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "earnedDate");
          final int _cursorIndexOfIsCertification = CursorUtil.getColumnIndexOrThrow(_cursor, "isCertification");
          final int _cursorIndexOfExpirationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expirationDate");
          final int _cursorIndexOfRenewalRequired = CursorUtil.getColumnIndexOrThrow(_cursor, "renewalRequired");
          final List<CredentialEntity> _result = new ArrayList<CredentialEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CredentialEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final String _tmpCode;
            _tmpCode = _cursor.getString(_cursorIndexOfCode);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpIssuingAuthority;
            _tmpIssuingAuthority = _cursor.getString(_cursorIndexOfIssuingAuthority);
            final LocalDate _tmpEarnedDate;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfEarnedDate)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfEarnedDate);
            }
            final LocalDate _tmp_2 = __converters.epochDayToLocalDate(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpEarnedDate = _tmp_2;
            }
            final boolean _tmpIsCertification;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsCertification);
            _tmpIsCertification = _tmp_3 != 0;
            final LocalDate _tmpExpirationDate;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfExpirationDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfExpirationDate);
            }
            _tmpExpirationDate = __converters.epochDayToLocalDate(_tmp_4);
            final boolean _tmpRenewalRequired;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfRenewalRequired);
            _tmpRenewalRequired = _tmp_5 != 0;
            _item = new CredentialEntity(_tmpId,_tmpPersonId,_tmpCode,_tmpName,_tmpIssuingAuthority,_tmpEarnedDate,_tmpIsCertification,_tmpExpirationDate,_tmpRenewalRequired);
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
  public Flow<List<CredentialEntity>> observeByCode(final String code) {
    final String _sql = "SELECT * FROM credentials WHERE code = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, code);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"credentials"}, new Callable<List<CredentialEntity>>() {
      @Override
      @NonNull
      public List<CredentialEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIssuingAuthority = CursorUtil.getColumnIndexOrThrow(_cursor, "issuingAuthority");
          final int _cursorIndexOfEarnedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "earnedDate");
          final int _cursorIndexOfIsCertification = CursorUtil.getColumnIndexOrThrow(_cursor, "isCertification");
          final int _cursorIndexOfExpirationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expirationDate");
          final int _cursorIndexOfRenewalRequired = CursorUtil.getColumnIndexOrThrow(_cursor, "renewalRequired");
          final List<CredentialEntity> _result = new ArrayList<CredentialEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CredentialEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final String _tmpCode;
            _tmpCode = _cursor.getString(_cursorIndexOfCode);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpIssuingAuthority;
            _tmpIssuingAuthority = _cursor.getString(_cursorIndexOfIssuingAuthority);
            final LocalDate _tmpEarnedDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfEarnedDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfEarnedDate);
            }
            final LocalDate _tmp_1 = __converters.epochDayToLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpEarnedDate = _tmp_1;
            }
            final boolean _tmpIsCertification;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCertification);
            _tmpIsCertification = _tmp_2 != 0;
            final LocalDate _tmpExpirationDate;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfExpirationDate)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfExpirationDate);
            }
            _tmpExpirationDate = __converters.epochDayToLocalDate(_tmp_3);
            final boolean _tmpRenewalRequired;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfRenewalRequired);
            _tmpRenewalRequired = _tmp_4 != 0;
            _item = new CredentialEntity(_tmpId,_tmpPersonId,_tmpCode,_tmpName,_tmpIssuingAuthority,_tmpEarnedDate,_tmpIsCertification,_tmpExpirationDate,_tmpRenewalRequired);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
