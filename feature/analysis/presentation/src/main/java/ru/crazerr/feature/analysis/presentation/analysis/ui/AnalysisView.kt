package ru.crazerr.feature.analysis.presentation.analysis.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.core.utils.components.CategoryIcon
import ru.crazerr.core.utils.components.ErrorView
import ru.crazerr.core.utils.components.LoadingView
import ru.crazerr.core.utils.date.toMonthYearFormat
import ru.crazerr.core.utils.date.toYearFormat
import ru.crazerr.core.utils.presentation.capitalizeFirst
import ru.crazerr.core.utils.presentation.toAmountFormat
import ru.crazerr.feature.analysis.domain.model.AnalysisCategory
import ru.crazerr.feature.analysis.domain.model.AnalysisPeriod
import ru.crazerr.feature.analysis.presentation.R
import ru.crazerr.feature.analysis.presentation.analysis.AnalysisComponent
import ru.crazerr.feature.analysis.presentation.analysis.AnalysisState
import ru.crazerr.feature.analysis.presentation.analysis.AnalysisViewAction
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AnalysisView(modifier: Modifier = Modifier, component: AnalysisComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) { paddingValues ->
        AnalysisViewContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = state,
            handleViewAction = component::handleViewAction
        )
    }
}

@Composable
private fun AnalysisViewContent(
    modifier: Modifier = Modifier,
    state: AnalysisState,
    handleViewAction: (AnalysisViewAction) -> Unit,
) {
    AnalysisViewContentBody(
        modifier = modifier,
        state = state,
        handleViewAction = handleViewAction,
    )
}

@Composable
private fun AnalysisViewContentBody(
    modifier: Modifier = Modifier,
    state: AnalysisState,
    handleViewAction: (AnalysisViewAction) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        item { TransactionsTypeRow(state = state, handleViewAction = handleViewAction) }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item { PeriodCard(state = state, handleViewAction = handleViewAction) }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        when {
            state.isLoading -> item { LoadingView(modifier = modifier) }
            state.error.isNotEmpty() -> item {
                ErrorView(
                    modifier = modifier,
                    message = state.error,
                    onRetry = { handleViewAction(AnalysisViewAction.RetryButtonClick) },
                )
            }

            state.analysisCategories.isEmpty() -> item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                ) {
                    Text(
                        text = stringResource(R.string.analysis_empty_categories),
                        style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                    )
                }
            }

            else -> {

                item { PieCard(state = state) }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 8.dp,
                            ),
                            text = stringResource(if (state.selectedTransactionType == TransactionType.Income) R.string.analysis_category_incomes else R.string.analysis_category_expenses),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }

                itemsIndexed(state.analysisCategories) { index, analysisCategory ->
                    CategoryItem(
                        analysisCategory = analysisCategory,
                        index = index,
                        state = state,
                        onClick = { handleViewAction(AnalysisViewAction.CategoryClick(categoryId = analysisCategory.category.id)) },
                    )
                }

                if (state.analysisCategories.isNotEmpty()) {
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
private fun TransactionsTypeRow(
    modifier: Modifier = Modifier,
    state: AnalysisState,
    handleViewAction: (AnalysisViewAction) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        TransactionTypeButton(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.analysis_expenses),
            icon = painterResource(R.drawable.ic_expense),
            isSelected = TransactionType.Expense == state.selectedTransactionType,
            onClick = { handleViewAction(AnalysisViewAction.SelectTransactionType(TransactionType.Expense)) }
        )

        Spacer(modifier = Modifier.width(12.dp))

        TransactionTypeButton(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.analysis_incomes),
            icon = painterResource(R.drawable.ic_income),
            isSelected = TransactionType.Income == state.selectedTransactionType,
            onClick = { handleViewAction(AnalysisViewAction.SelectTransactionType(TransactionType.Income)) }
        )
    }
}

@Composable
private fun TransactionTypeButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(12.dp),
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onBackground
            ),
        )
    }
}

@Composable
private fun PeriodCard(
    modifier: Modifier = Modifier,
    state: AnalysisState,
    handleViewAction: (AnalysisViewAction) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            PeriodSelector(state = state, handleViewAction = handleViewAction)

            Spacer(modifier = Modifier.height(8.dp))

            PeriodButtonsRow(state = state, handleViewAction = handleViewAction)
        }
    }
}

@Composable
private fun PeriodSelector(
    modifier: Modifier = Modifier,
    state: AnalysisState,
    handleViewAction: (AnalysisViewAction) -> Unit,
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    onClick = { handleViewAction(AnalysisViewAction.PreviousDateClick) },
                    indication = null,
                    interactionSource = null,
                )
                .padding(top = 16.dp, start = 16.dp, bottom = 8.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.analysis_previous_period_content_description),
            )
        }

        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            text = getStringDateForAnalysisPeriod(
                analysisPeriod = state.selectedAnalysisPeriod,
                date = state.date,
            ),
            style = MaterialTheme.typography.titleMedium,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    onClick = { handleViewAction(AnalysisViewAction.NextDateClick) },
                    indication = null,
                    interactionSource = null,
                )
                .padding(end = 16.dp, top = 16.dp, bottom = 8.dp),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.analysis_next_period_content_description),
            )
        }
    }
}

@Composable
private fun PeriodButtonsRow(
    modifier: Modifier = Modifier,
    state: AnalysisState,
    handleViewAction: (AnalysisViewAction) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        PeriodButton(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.analysis_period_week),
            isSelected = state.selectedAnalysisPeriod == AnalysisPeriod.Week,
        ) { handleViewAction(AnalysisViewAction.SelectAnalysisPeriod(AnalysisPeriod.Week)) }

        Spacer(modifier = Modifier.width(8.dp))

        PeriodButton(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.analysis_period_month),
            isSelected = state.selectedAnalysisPeriod == AnalysisPeriod.Month
        ) { handleViewAction(AnalysisViewAction.SelectAnalysisPeriod(AnalysisPeriod.Month)) }

        Spacer(modifier = Modifier.width(8.dp))

        PeriodButton(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.analysis_period_year),
            isSelected = state.selectedAnalysisPeriod == AnalysisPeriod.Year,
        ) { handleViewAction(AnalysisViewAction.SelectAnalysisPeriod(AnalysisPeriod.Year)) }
    }
}

@Composable
private fun PeriodButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
            text = text,
            style = MaterialTheme.typography.titleSmall.copy(
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurface
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    analysisCategory: AnalysisCategory,
    index: Int,
    state: AnalysisState,
    onClick: () -> Unit,
) {
    val percent = analysisCategory.moneySpent / state.totalAmount * 100

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = if (index == state.analysisCategories.size - 1) RoundedCornerShape(
            bottomStart = 12.dp,
            bottomEnd = 12.dp,
        ) else RectangleShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    paddingValues = if (index == state.analysisCategories.size - 1) PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp,
                        top = 8.dp,
                    ) else PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CategoryIcon(
                color = analysisCategory.category.color,
                icon = analysisCategory.category.iconModel.icon,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = analysisCategory.category.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = stringResource(
                        if (state.selectedTransactionType == TransactionType.Income) R.string.analysis_percent_from_incomes else R.string.analysis_percent_from_expenses,
                        if (percent % 1 == 0.0) percent.toInt().toString()
                        else String.format(Locale.getDefault(), "%.1f", percent),
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = analysisCategory.moneySpent.toAmountFormat('₽'),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun PieCard(modifier: Modifier = Modifier, state: AnalysisState) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            PieChart(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                total = state.totalAmount,
                data = state.analysisCategories.map {
                    Pair(
                        Color(it.category.color),
                        it.moneySpent
                    )
                })
        }
    }
}

@Composable
private fun PieChart(
    modifier: Modifier = Modifier,
    total: Double,
    data: List<Pair<Color, Double>>,
) {
    Canvas(
        modifier = modifier
            .width(LocalConfiguration.current.screenWidthDp.dp / 2)
            .aspectRatio(1f)
    ) {
        var startAngle = 0f
        val strokeWidth = size.minDimension / 8
        val radius = (size.minDimension - strokeWidth) / 2
        val rectSize = Size(radius * 2, radius * 2)
        val center = Offset(size.width / 2, size.height / 2)
        val topLeft = Offset(center.x - radius, center.y - radius)

        data.forEach { pair ->
            val sweepAngle = ((pair.second / total) * 360).toFloat()

            drawArc(
                color = pair.first,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = rectSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            startAngle += sweepAngle
        }
    }
}

private fun getStringDateForAnalysisPeriod(
    analysisPeriod: AnalysisPeriod,
    date: LocalDate,
): String = when (analysisPeriod) {
    AnalysisPeriod.Month -> date.toMonthYearFormat().capitalizeFirst()
    AnalysisPeriod.Week -> {
        val startDate = analysisPeriod.getStartDate(date)
        val endDate = analysisPeriod.getEndDate(date)

        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
        if (startDate.year == endDate.year) {
            "${startDate.format(DateTimeFormatter.ofPattern("d MMMM"))} – ${
                endDate.format(
                    formatter
                )
            }"
        } else {
            "${startDate.format(formatter)} – ${endDate.format(formatter)}"
        }
    }

    AnalysisPeriod.Year -> date.toYearFormat()
}
