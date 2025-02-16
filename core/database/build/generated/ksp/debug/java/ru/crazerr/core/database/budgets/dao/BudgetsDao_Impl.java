package ru.crazerr.core.database.budgets.dao;

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
import ru.crazerr.core.database.budgets.model.BudgetEntity;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BudgetsDao_Impl implements BudgetsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BudgetEntity> __insertionAdapterOfBudgetEntity;

  private final EntityDeletionOrUpdateAdapter<BudgetEntity> __deletionAdapterOfBudgetEntity;

  private final EntityDeletionOrUpdateAdapter<BudgetEntity> __updateAdapterOfBudgetEntity;

  public BudgetsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBudgetEntity = new EntityInsertionAdapter<BudgetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `budgets` (`id`,`category_id`,`max_amount`,`current_amount`,`is_regular`,`date`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BudgetEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCategoryId());
        statement.bindLong(3, entity.getMaxAmount());
        statement.bindLong(4, entity.getCurrentAmount());
        final int _tmp = entity.isRegular() ? 1 : 0;
        statement.bindLong(5, _tmp);
      }
    };
    this.__deletionAdapterOfBudgetEntity = new EntityDeletionOrUpdateAdapter<BudgetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `budgets` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BudgetEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfBudgetEntity = new EntityDeletionOrUpdateAdapter<BudgetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `budgets` SET `id` = ?,`category_id` = ?,`max_amount` = ?,`current_amount` = ?,`is_regular` = ?,`date` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BudgetEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCategoryId());
        statement.bindLong(3, entity.getMaxAmount());
        statement.bindLong(4, entity.getCurrentAmount());
        final int _tmp = entity.isRegular() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(7, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final BudgetEntity[] obj, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBudgetEntity.insert(obj);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final BudgetEntity[] obj, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBudgetEntity.handleMultiple(obj);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final BudgetEntity[] obj, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBudgetEntity.handleMultiple(obj);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<BudgetEntity> getAllBudgets() {
    final String _sql = "SELECT * FROM budgets";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"budgets"}, new Callable<BudgetEntity>() {
      @Override
      @NonNull
      public BudgetEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "category_id");
          final int _cursorIndexOfMaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "max_amount");
          final int _cursorIndexOfCurrentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "current_amount");
          final int _cursorIndexOfIsRegular = CursorUtil.getColumnIndexOrThrow(_cursor, "is_regular");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final BudgetEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCategoryId;
            _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            final long _tmpMaxAmount;
            _tmpMaxAmount = _cursor.getLong(_cursorIndexOfMaxAmount);
            final long _tmpCurrentAmount;
            _tmpCurrentAmount = _cursor.getLong(_cursorIndexOfCurrentAmount);
            final boolean _tmpIsRegular;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRegular);
            _tmpIsRegular = _tmp != 0;
            final LocalDate _tmpDate;
            _result = new BudgetEntity(_tmpId,_tmpCategoryId,_tmpMaxAmount,_tmpCurrentAmount,_tmpIsRegular,_tmpDate);
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
