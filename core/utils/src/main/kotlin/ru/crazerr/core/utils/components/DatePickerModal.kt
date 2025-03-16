package ru.crazerr.core.utils.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.crazerr.core.utils.R
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    initialDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (initialDate != null) {
            ZonedDateTime.of(
                initialDate, LocalTime.now(), ZoneId.systemDefault()
            ).toInstant().toEpochMilli()
        } else null
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(
                        Instant.ofEpochMilli(datePickerState.selectedDateMillis ?: 0L).atZone(
                            ZoneId.systemDefault()
                        ).toLocalDate()
                    )
                    onDismiss()
                },
            ) {
                Text(
                    text = stringResource(R.string.confirm_button),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cancel_button),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
    ) {
        DatePicker(
            state = datePickerState,
        )
    }
}