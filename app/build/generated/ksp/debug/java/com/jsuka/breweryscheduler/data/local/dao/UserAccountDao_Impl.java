package com.jsuka.breweryscheduler.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.jsuka.breweryscheduler.data.local.Converters;
import com.jsuka.breweryscheduler.data.local.entity.UserAccountEntity;
import com.jsuka.breweryscheduler.domain.model.PersonRole;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserAccountDao_Impl implements UserAccountDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserAccountEntity> __insertionAdapterOfUserAccountEntity;

  private final Converters __converters = new Converters();

  public UserAccountDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserAccountEntity = new EntityInsertionAdapter<UserAccountEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `user_accounts` (`id`,`personId`,`username`,`passwordSalt`,`passwordHash`,`role`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserAccountEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPersonId());
        statement.bindString(3, entity.getUsername());
        statement.bindString(4, entity.getPasswordSalt());
        statement.bindString(5, entity.getPasswordHash());
        final String _tmp = __converters.personRoleToName(entity.getRole());
        statement.bindString(6, _tmp);
      }
    };
  }

  @Override
  public Object insert(final UserAccountEntity account,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfUserAccountEntity.insertAndReturnId(account);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object findByUsername(final String username,
      final Continuation<? super UserAccountEntity> $completion) {
    final String _sql = "SELECT * FROM user_accounts WHERE username = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, username);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserAccountEntity>() {
      @Override
      @Nullable
      public UserAccountEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfPasswordSalt = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordSalt");
          final int _cursorIndexOfPasswordHash = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordHash");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final UserAccountEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final String _tmpPasswordSalt;
            _tmpPasswordSalt = _cursor.getString(_cursorIndexOfPasswordSalt);
            final String _tmpPasswordHash;
            _tmpPasswordHash = _cursor.getString(_cursorIndexOfPasswordHash);
            final PersonRole _tmpRole;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfRole);
            _tmpRole = __converters.nameToPersonRole(_tmp);
            _result = new UserAccountEntity(_tmpId,_tmpPersonId,_tmpUsername,_tmpPasswordSalt,_tmpPasswordHash,_tmpRole);
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
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM user_accounts";
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
