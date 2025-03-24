package ru.crazerr.core.root

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class BottomNavigationItem(@StringRes val stringRes: Int, @DrawableRes val iconRes: Int) {
    Main(stringRes = R.string.bottom_item_main_title, iconRes = R.drawable.ic_main),
    Transactions(
        stringRes = R.string.bottom_item_transactions_title,
        iconRes = R.drawable.ic_transactions
    ),
    Budget(stringRes = R.string.bottom_item_budget_title, iconRes = R.drawable.ic_budget),
    Analysis(stringRes = R.string.bottom_item_analysis_title, iconRes = R.drawable.ic_analysis),
    Profile(stringRes = R.string.bottom_item_profile_title, iconRes = R.drawable.ic_profile);
}