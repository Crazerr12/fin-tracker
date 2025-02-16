package ru.crazerr.core.root

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class BottomNavigationItem(@StringRes val stringRes: Int, @DrawableRes val iconRes: Int) {
    Main(stringRes = R.string.main_title, iconRes = R.drawable.ic_main),
    Transactions(stringRes = R.string.transactions_title, iconRes = R.drawable.ic_transactions),
    Budget(stringRes = R.string.budget_title, iconRes = R.drawable.ic_budget),
    Analysis(stringRes = R.string.analysis_title, iconRes = R.drawable.ic_analysis),
    Profile(stringRes = R.string.profile_title, iconRes = R.drawable.ic_profile);
}