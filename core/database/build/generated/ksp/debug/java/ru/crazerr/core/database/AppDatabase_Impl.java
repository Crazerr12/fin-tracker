package ru.crazerr.core.database;

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
import ru.crazerr.core.database.accounts.dao.AccountsDao;
import ru.crazerr.core.database.accounts.dao.AccountsDao_Impl;
import ru.crazerr.core.database.budgets.dao.BudgetsDao;
import ru.crazerr.core.database.budgets.dao.BudgetsDao_Impl;
import ru.crazerr.core.database.categories.dao.CategoriesDao;
import ru.crazerr.core.database.categories.dao.CategoriesDao_Impl;
import ru.crazerr.core.database.currencies.dao.CurrenciesDao;
import ru.crazerr.core.database.currencies.dao.CurrenciesDao_Impl;
import ru.crazerr.core.database.repeatTransactions.dao.RepeatTransactionsDao;
import ru.crazerr.core.database.repeatTransactions.dao.RepeatTransactionsDao_Impl;
import ru.crazerr.core.database.transactions.dao.TransactionsDao;
import ru.crazerr.core.database.transactions.dao.TransactionsDao_Impl;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TransactionsDao _transactionsDao;

  private volatile RepeatTransactionsDao _repeatTransactionsDao;

  private volatile CurrenciesDao _currenciesDao;

  private volatile AccountsDao _accountsDao;

  private volatile BudgetsDao _budgetsDao;

  private volatile CategoriesDao _categoriesDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `category_id` INTEGER NOT NULL, `amount` INTEGER NOT NULL, `type` INTEGER NOT NULL, `date` TEXT NOT NULL, `account_id` INTEGER NOT NULL, FOREIGN KEY(`category_id`) REFERENCES `categories`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , FOREIGN KEY(`account_id`) REFERENCES `accounts`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_category_id` ON `transactions` (`category_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_account_id` ON `transactions` (`account_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `repeat_transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `category_id` INTEGER NOT NULL, `amount` INTEGER NOT NULL, `type` INTEGER NOT NULL, `completion_date` TEXT NOT NULL, `account_id` INTEGER NOT NULL, `repeat_type` INTEGER NOT NULL, `repeat_interval` INTEGER NOT NULL, `repeat_units` TEXT NOT NULL, FOREIGN KEY(`category_id`) REFERENCES `categories`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , FOREIGN KEY(`account_id`) REFERENCES `accounts`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_repeat_transactions_category_id_account_id` ON `repeat_transactions` (`category_id`, `account_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `currencies` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `symbol` TEXT NOT NULL, `code` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `accounts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `amount` INTEGER NOT NULL, `icon_id` TEXT NOT NULL, `currency_id` INTEGER NOT NULL, FOREIGN KEY(`currency_id`) REFERENCES `currencies`(`id`) ON UPDATE RESTRICT ON DELETE RESTRICT )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_accounts_currency_id` ON `accounts` (`currency_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `budgets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `category_id` INTEGER NOT NULL, `max_amount` INTEGER NOT NULL, `current_amount` INTEGER NOT NULL, `is_regular` INTEGER NOT NULL, `date` TEXT NOT NULL, FOREIGN KEY(`category_id`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_budgets_category_id` ON `budgets` (`category_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `color` INTEGER NOT NULL, `icon_id` TEXT NOT NULL, `is_template` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '75ec1793d80f31c175b5fa78d33484d1')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `transactions`");
        db.execSQL("DROP TABLE IF EXISTS `repeat_transactions`");
        db.execSQL("DROP TABLE IF EXISTS `currencies`");
        db.execSQL("DROP TABLE IF EXISTS `accounts`");
        db.execSQL("DROP TABLE IF EXISTS `budgets`");
        db.execSQL("DROP TABLE IF EXISTS `categories`");
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
        final HashMap<String, TableInfo.Column> _columnsTransactions = new HashMap<String, TableInfo.Column>(6);
        _columnsTransactions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("category_id", new TableInfo.Column("category_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("type", new TableInfo.Column("type", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("account_id", new TableInfo.Column("account_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransactions = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysTransactions.add(new TableInfo.ForeignKey("categories", "CASCADE", "CASCADE", Arrays.asList("category_id"), Arrays.asList("id")));
        _foreignKeysTransactions.add(new TableInfo.ForeignKey("accounts", "CASCADE", "CASCADE", Arrays.asList("account_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTransactions = new HashSet<TableInfo.Index>(2);
        _indicesTransactions.add(new TableInfo.Index("index_transactions_category_id", false, Arrays.asList("category_id"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_account_id", false, Arrays.asList("account_id"), Arrays.asList("ASC")));
        final TableInfo _infoTransactions = new TableInfo("transactions", _columnsTransactions, _foreignKeysTransactions, _indicesTransactions);
        final TableInfo _existingTransactions = TableInfo.read(db, "transactions");
        if (!_infoTransactions.equals(_existingTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "transactions(ru.crazerr.core.database.transactions.model.TransactionEntity).\n"
                  + " Expected:\n" + _infoTransactions + "\n"
                  + " Found:\n" + _existingTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsRepeatTransactions = new HashMap<String, TableInfo.Column>(9);
        _columnsRepeatTransactions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatTransactions.put("category_id", new TableInfo.Column("category_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatTransactions.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatTransactions.put("type", new TableInfo.Column("type", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatTransactions.put("completion_date", new TableInfo.Column("completion_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatTransactions.put("account_id", new TableInfo.Column("account_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatTransactions.put("repeat_type", new TableInfo.Column("repeat_type", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatTransactions.put("repeat_interval", new TableInfo.Column("repeat_interval", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepeatTransactions.put("repeat_units", new TableInfo.Column("repeat_units", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRepeatTransactions = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysRepeatTransactions.add(new TableInfo.ForeignKey("categories", "CASCADE", "CASCADE", Arrays.asList("category_id"), Arrays.asList("id")));
        _foreignKeysRepeatTransactions.add(new TableInfo.ForeignKey("accounts", "CASCADE", "CASCADE", Arrays.asList("account_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRepeatTransactions = new HashSet<TableInfo.Index>(1);
        _indicesRepeatTransactions.add(new TableInfo.Index("index_repeat_transactions_category_id_account_id", false, Arrays.asList("category_id", "account_id"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoRepeatTransactions = new TableInfo("repeat_transactions", _columnsRepeatTransactions, _foreignKeysRepeatTransactions, _indicesRepeatTransactions);
        final TableInfo _existingRepeatTransactions = TableInfo.read(db, "repeat_transactions");
        if (!_infoRepeatTransactions.equals(_existingRepeatTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "repeat_transactions(ru.crazerr.core.database.repeatTransactions.model.RepeatTransactionEntity).\n"
                  + " Expected:\n" + _infoRepeatTransactions + "\n"
                  + " Found:\n" + _existingRepeatTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsCurrencies = new HashMap<String, TableInfo.Column>(4);
        _columnsCurrencies.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencies.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencies.put("symbol", new TableInfo.Column("symbol", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrencies.put("code", new TableInfo.Column("code", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCurrencies = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCurrencies = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCurrencies = new TableInfo("currencies", _columnsCurrencies, _foreignKeysCurrencies, _indicesCurrencies);
        final TableInfo _existingCurrencies = TableInfo.read(db, "currencies");
        if (!_infoCurrencies.equals(_existingCurrencies)) {
          return new RoomOpenHelper.ValidationResult(false, "currencies(ru.crazerr.core.database.currencies.model.CurrencyEntity).\n"
                  + " Expected:\n" + _infoCurrencies + "\n"
                  + " Found:\n" + _existingCurrencies);
        }
        final HashMap<String, TableInfo.Column> _columnsAccounts = new HashMap<String, TableInfo.Column>(5);
        _columnsAccounts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("icon_id", new TableInfo.Column("icon_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("currency_id", new TableInfo.Column("currency_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAccounts = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysAccounts.add(new TableInfo.ForeignKey("currencies", "RESTRICT", "RESTRICT", Arrays.asList("currency_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesAccounts = new HashSet<TableInfo.Index>(1);
        _indicesAccounts.add(new TableInfo.Index("index_accounts_currency_id", false, Arrays.asList("currency_id"), Arrays.asList("ASC")));
        final TableInfo _infoAccounts = new TableInfo("accounts", _columnsAccounts, _foreignKeysAccounts, _indicesAccounts);
        final TableInfo _existingAccounts = TableInfo.read(db, "accounts");
        if (!_infoAccounts.equals(_existingAccounts)) {
          return new RoomOpenHelper.ValidationResult(false, "accounts(ru.crazerr.core.database.accounts.model.AccountEntity).\n"
                  + " Expected:\n" + _infoAccounts + "\n"
                  + " Found:\n" + _existingAccounts);
        }
        final HashMap<String, TableInfo.Column> _columnsBudgets = new HashMap<String, TableInfo.Column>(6);
        _columnsBudgets.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("category_id", new TableInfo.Column("category_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("max_amount", new TableInfo.Column("max_amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("current_amount", new TableInfo.Column("current_amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("is_regular", new TableInfo.Column("is_regular", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBudgets = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBudgets.add(new TableInfo.ForeignKey("categories", "NO ACTION", "NO ACTION", Arrays.asList("category_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBudgets = new HashSet<TableInfo.Index>(1);
        _indicesBudgets.add(new TableInfo.Index("index_budgets_category_id", false, Arrays.asList("category_id"), Arrays.asList("ASC")));
        final TableInfo _infoBudgets = new TableInfo("budgets", _columnsBudgets, _foreignKeysBudgets, _indicesBudgets);
        final TableInfo _existingBudgets = TableInfo.read(db, "budgets");
        if (!_infoBudgets.equals(_existingBudgets)) {
          return new RoomOpenHelper.ValidationResult(false, "budgets(ru.crazerr.core.database.budgets.model.BudgetEntity).\n"
                  + " Expected:\n" + _infoBudgets + "\n"
                  + " Found:\n" + _existingBudgets);
        }
        final HashMap<String, TableInfo.Column> _columnsCategories = new HashMap<String, TableInfo.Column>(5);
        _columnsCategories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("color", new TableInfo.Column("color", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("icon_id", new TableInfo.Column("icon_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("is_template", new TableInfo.Column("is_template", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCategories = new TableInfo("categories", _columnsCategories, _foreignKeysCategories, _indicesCategories);
        final TableInfo _existingCategories = TableInfo.read(db, "categories");
        if (!_infoCategories.equals(_existingCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "categories(ru.crazerr.core.database.categories.model.CategoryEntity).\n"
                  + " Expected:\n" + _infoCategories + "\n"
                  + " Found:\n" + _existingCategories);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "75ec1793d80f31c175b5fa78d33484d1", "84555f22ee701c6fec801d3c6dcb50fa");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "transactions","repeat_transactions","currencies","accounts","budgets","categories");
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
      _db.execSQL("DELETE FROM `transactions`");
      _db.execSQL("DELETE FROM `repeat_transactions`");
      _db.execSQL("DELETE FROM `accounts`");
      _db.execSQL("DELETE FROM `currencies`");
      _db.execSQL("DELETE FROM `budgets`");
      _db.execSQL("DELETE FROM `categories`");
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
    _typeConvertersMap.put(TransactionsDao.class, TransactionsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RepeatTransactionsDao.class, RepeatTransactionsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CurrenciesDao.class, CurrenciesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AccountsDao.class, AccountsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BudgetsDao.class, BudgetsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoriesDao.class, CategoriesDao_Impl.getRequiredConverters());
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
  public TransactionsDao transactionsDao() {
    if (_transactionsDao != null) {
      return _transactionsDao;
    } else {
      synchronized(this) {
        if(_transactionsDao == null) {
          _transactionsDao = new TransactionsDao_Impl(this);
        }
        return _transactionsDao;
      }
    }
  }

  @Override
  public RepeatTransactionsDao repeatTransactionsDao() {
    if (_repeatTransactionsDao != null) {
      return _repeatTransactionsDao;
    } else {
      synchronized(this) {
        if(_repeatTransactionsDao == null) {
          _repeatTransactionsDao = new RepeatTransactionsDao_Impl(this);
        }
        return _repeatTransactionsDao;
      }
    }
  }

  @Override
  public CurrenciesDao currenciesDao() {
    if (_currenciesDao != null) {
      return _currenciesDao;
    } else {
      synchronized(this) {
        if(_currenciesDao == null) {
          _currenciesDao = new CurrenciesDao_Impl(this);
        }
        return _currenciesDao;
      }
    }
  }

  @Override
  public AccountsDao accountsDao() {
    if (_accountsDao != null) {
      return _accountsDao;
    } else {
      synchronized(this) {
        if(_accountsDao == null) {
          _accountsDao = new AccountsDao_Impl(this);
        }
        return _accountsDao;
      }
    }
  }

  @Override
  public BudgetsDao budgetsDao() {
    if (_budgetsDao != null) {
      return _budgetsDao;
    } else {
      synchronized(this) {
        if(_budgetsDao == null) {
          _budgetsDao = new BudgetsDao_Impl(this);
        }
        return _budgetsDao;
      }
    }
  }

  @Override
  public CategoriesDao categoriesDao() {
    if (_categoriesDao != null) {
      return _categoriesDao;
    } else {
      synchronized(this) {
        if(_categoriesDao == null) {
          _categoriesDao = new CategoriesDao_Impl(this);
        }
        return _categoriesDao;
      }
    }
  }
}
