
package com.example.waterme.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.waterme.FIVE_SECONDS
import com.example.waterme.ONE_DAY
import com.example.waterme.R
import com.example.waterme.SEVEN_DAYS
import com.example.waterme.THIRTY_DAYS
import com.example.waterme.data.DataSource
import com.example.waterme.data.Reminder
import com.example.waterme.model.Plant
import com.example.waterme.ui.theme.WaterMeTheme
import java.util.concurrent.TimeUnit

// Chuyển nội dung sang tiếng Việt
@Composable
fun tiengViet(id: Int): String {
    return when (id) {
        // Tên cây
        R.string.lithop -> "Cây đá sống"
        R.string.carrot -> "Cà rốt"
        R.string.peony -> "Hoa mẫu đơn"
        R.string.pothos -> "Cây trầu bà"
        R.string.fiddle_leaf_fig -> "Cây bàng Singapore"
        R.string.strawberry -> "Dâu tây"

        // Lịch tưới
        R.string.daily -> "Hằng ngày"
        R.string.weekly -> "Hằng tuần"
        R.string.monthly -> "Hằng tháng"

        // Loại cây
        R.string.succulent -> "Cây mọng nước"
        R.string.root -> "Cây lấy củ"
        R.string.flower -> "Cây hoa"
        R.string.houseplant -> "Cây trồng trong nhà"
        R.string.broadleaf_evergreen -> "Cây thường xanh lá rộng"
        R.string.fruit -> "Cây ăn quả"

        // Mô tả cây
        R.string.stone_mimicking_succulent ->
            "Cây mọng nước có hình dáng giống những viên đá nhỏ."

        R.string.hardy_root_vegetable ->
            "Cà rốt là loại rau củ dễ trồng và thích nghi tốt với môi trường."

        R.string.spring_blooming_flower ->
            "Hoa mẫu đơn có màu sắc rực rỡ, thường nở vào mùa xuân."

        R.string.indoor_vine ->
            "Cây trầu bà dễ chăm sóc, thích hợp trang trí trong nhà."

        R.string.ornamental_fig ->
            "Cây bàng Singapore có lá lớn, thích hợp trang trí nhà cửa."

        R.string.delicious_multiple_fruit ->
            "Dâu tây có quả thơm ngon, mọng nước và giàu vitamin."

        // Thời gian nhắc nhở
        R.string.five_seconds -> "Sau 5 giây"
        R.string.one_day -> "Sau 1 ngày"
        R.string.one_week -> "Sau 1 tuần"
        R.string.one_month -> "Sau 1 tháng"

        else -> stringResource(id)
    }
}

@Composable
fun WaterMeApp(
    waterViewModel: WaterViewModel = viewModel(
        factory = WaterViewModel.Factory
    )
) {
    val layoutDirection = LocalLayoutDirection.current

    WaterMeTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(
                    start = WindowInsets.safeDrawing
                        .asPaddingValues()
                        .calculateStartPadding(layoutDirection),
                    end = WindowInsets.safeDrawing
                        .asPaddingValues()
                        .calculateEndPadding(layoutDirection)
                )
        ) {
            PlantListContent(
                plants = waterViewModel.plants,
                onScheduleReminder = {
                    waterViewModel.scheduleReminder(it)
                }
            )
        }
    }
}

@Composable
fun PlantListContent(
    plants: List<Plant>,
    onScheduleReminder: (Reminder) -> Unit,
    modifier: Modifier = Modifier
) {
    if (plants.isEmpty()) {
        Text(
            text = "Chưa có cây trồng nào",
            modifier = modifier.padding(16.dp)
        )
        return
    }

    var selectedPlant by rememberSaveable {
        mutableStateOf(plants[0])
    }

    var showReminderDialog by rememberSaveable {
        mutableStateOf(false)
    }

    LazyColumn(
        contentPadding = PaddingValues(
            dimensionResource(id = R.dimen.padding_medium)
        ),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_medium)
        ),
        modifier = modifier
    ) {
        items(items = plants) { plant ->
            PlantListItem(
                plant = plant,
                onItemSelect = {
                    selectedPlant = it
                    showReminderDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (showReminderDialog) {
        ReminderDialogContent(
            onDialogDismiss = {
                showReminderDialog = false
            },
            plantName = tiengViet(selectedPlant.name),
            onScheduleReminder = onScheduleReminder
        )
    }
}

@Composable
fun PlantListItem(
    plant: Plant,
    onItemSelect: (Plant) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable {
            onItemSelect(plant)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(
                    dimensionResource(id = R.dimen.padding_medium)
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = tiengViet(plant.name),
                modifier = Modifier.fillMaxWidth(),
                style = typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Loại cây: ${tiengViet(plant.type)}",
                style = typography.titleMedium
            )

            Text(
                text = "Mô tả: ${tiengViet(plant.description)}",
                style = typography.titleMedium
            )

            Text(
                text = "Lịch tưới: ${tiengViet(plant.schedule)}",
                style = typography.titleMedium
            )
        }
    }
}

@Composable
fun ReminderDialogContent(
    onDialogDismiss: () -> Unit,
    plantName: String,
    onScheduleReminder: (Reminder) -> Unit,
    modifier: Modifier = Modifier
) {
    val reminders = listOf(
        Reminder(
            R.string.five_seconds,
            FIVE_SECONDS,
            TimeUnit.SECONDS,
            plantName
        ),
        Reminder(
            R.string.one_day,
            ONE_DAY,
            TimeUnit.DAYS,
            plantName
        ),
        Reminder(
            R.string.one_week,
            SEVEN_DAYS,
            TimeUnit.DAYS,
            plantName
        ),
        Reminder(
            R.string.one_month,
            THIRTY_DAYS,
            TimeUnit.DAYS,
            plantName
        )
    )

    AlertDialog(
        onDismissRequest = {
            onDialogDismiss()
        },
        confirmButton = {},
        title = {
            Text("Đặt lịch nhắc tưới: $plantName")
        },
        text = {
            Column {
                reminders.forEach { reminder ->
                    Text(
                        text = tiengViet(reminder.durationRes),
                        modifier = Modifier
                            .clickable {
                                onScheduleReminder(reminder)
                                onDialogDismiss()
                            }
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PlantListItemPreview() {
    WaterMeTheme {
        PlantListItem(
            plant = DataSource.plants[0],
            onItemSelect = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlantListContentPreview() {
    PlantListContent(
        plants = DataSource.plants,
        onScheduleReminder = {}
    )
}
