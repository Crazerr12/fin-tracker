package ru.crazerr.core.database.repeatTransactions.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import ru.crazerr.core.database.converters.IntListConverter;
import ru.crazerr.core.database.converters.LocalDateConverter;
import ru.crazerr.core.database.repeatTransactions.model.RepeatTransactionEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RepeatTransactionsDao_Impl implements RepeatTransactionsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RepeatTransactionEntity> __insertionAdapterOfRepeatTransactionEntity;

  private final LocalDateConverter __localDateConverter = new LocalDateConverter();

  private final IntListConverter __intListConverter = new IntListConverter();

  private final EntityDeletionOrUpdateAdapter<RepeatTransactionEntity> __deletionAdapterOfRepeatTransactionEntity;

  private final EntityDeletionOrUpdateAdapter<RepeatTransactionEntity> __updateAdapterOfRepeatTransactionEntity;

  public RepeatTransactionsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRepeatTransactionEntity = new EntityInsertionAdapter<RepeatTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `repeat_transactions` (`id`,`category_id`,`amount`,`type`,`completion_date`,`account_id`,`repeat_type`,`repeat_interval`,`repeat_units`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RepeatTransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCategoryId());
        statement.bindLong(3, entity.getAmount());
        final int _tmp = entity.getType() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final String _tmp_1 = __localDateConverter.fromLocalDate(entity.getCompletionDate());
        statement.bindString(5, _tmp_1);
        statement.bindLong(6, entity.getAccountId());
        statement.bindLong(7, entity.getRepeatType());
        statement.bindLong(8, entity.getRepeatInterval());
        final String _tmp_2 = __intListConverter.fromList(entity.getRepeatUnits());
        statement.bindString(9, _tmp_2);
      }
    };
    this.__deletionAdapterOfRepeatTransactionEntity = new EntityDeletionOrUpdateAdapter<RepeatTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `repeat_transactions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RepeatTransactionEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRepeatTransactionEntity = new EntityDeletionOrUpdateAdapter<RepeatTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `repeat_transactions` SET `id` = ?,`category_id` = ?,`amount` = ?,`type` = ?,`completion_date` = ?,`account_id` = ?,`repeat_type` = ?,`repeat_interval` = ?,`repeat_units` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RepeatTransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCategoryId());
        statement.bindLong(3, entity.getAmount());
        final int _tmp = entity.getType() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final String _tmp_1 = __localDateConverter.fromLocalDate(entity.getCompletionDate());
        statement.bindString(5, _tmp_1);
        statement.bindLong(6, entity.getAccountId());
        statement.bindLong(7, entity.getRepeatType());
        statement.bindLong(8, entity.getRepeatInterval());
        final String _tmp_2 = __intListConverter.fromList(entity.getRepeatUnits());
        statement.bindString(9, _tmp_2);
        statement.bindLong(10, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final RepeatTransactionEntity[] obj,
      final Continuation<? super List<Long>> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        __db.beginTransaction();
        try {
          final List<Long> _result = __insertionAdapterOfRepeatTransactionEntity.insertAndReturnIdsList(obj);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final RepeatTransactionEntity[] obj,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRepeatTransactionEntity.handleMultiple(obj);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final RepeatTransactionEntity[] obj,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRepeatTransactionEntity.handleMultiple(obj);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<RepeatTransactionEntity> getAllRepeatTransactions() {
    final String _sql = "SELECT * FROM repeat_transactions";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"repeat_transactions"}, new Callable<RepeatTransactionEntity>() {
      @Override
      @NonNull
      public RepeatTransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "category_id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCompletionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "completion_date");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "account_id");
          final int _cursorIndexOfRepeatType = CursorUtil.getColumnIndexOrThrow(_cursor, "repeat_type");
          final int _cursorIndexOfRepeatInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "repeat_interval");
          final int _cursorIndexOfRepeatUnits = CursorUtil.getColumnIndexOrThrow(_cursor, "repeat_units");
          final RepeatTransactionEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCategoryId;
            _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            final long _tmpAmount;
            _tmpAmount = _cursor.getLong(_cursorIndexOfAmount);
            final boolean _tmpType;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfType);
            _tmpType = _tmp != 0;
            final LocalDate _tmpCompletionDate;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfCompletionDate);
            _tmpCompletionDate = __localDateConverter.toLocalDate(_tmp_1);
            final int _tmpAccountId;
            _tmpAccountId = _cursor.getInt(_cursorIndexOfAccountId);
            final int _tmpRepeatType;
            _tmpRepeatType = _cursor.getInt(_cursorIndexOfRepeatType);
            final int _tmpRepeatInterval;
            _tmpRepeatInterval = _cursor.getInt(_cursorIndexOfRepeatInterval);
            final List<Integer> _tmpRepeatUnits;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfRepeatUnits);
            _tmpRepeatUnits = __intListConverter.toList(_tmp_2);
            _result = new RepeatTransactionEntity(_tmpId,_tmpCategoryId,_tmpAmount,_tmpType,_tmpCompletionDate,_tmpAccountId,_tmpRepeatType,_tmpRepeatInterval,_tmpRepeatUnits);
          } else {
            _result = null;
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
