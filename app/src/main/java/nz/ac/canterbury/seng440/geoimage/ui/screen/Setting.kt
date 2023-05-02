package nz.ac.canterbury.seng440.geoimage.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng440.geoimage.R
import nz.ac.canterbury.seng440.geoimage.data.StorePreference
import nz.ac.canterbury.seng440.geoimage.model.Setting
import nz.ac.canterbury.seng440.geoimage.ui.component.MyNotification
import nz.ac.canterbury.seng440.geoimage.ui.theme.Black
import nz.ac.canterbury.seng440.geoimage.ui.theme.ButtonWithIcon_SpaceBetween
import nz.ac.canterbury.seng440.geoimage.ui.theme.FABText_SizeOfHeader
import nz.ac.canterbury.seng440.geoimage.view.GeoImageViewModel

@Composable
fun getSettings(setting: Setting) : Setting {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StorePreference(context)

    setting.gpsMethod = dataStore.getGPSMethod.collectAsState(initial = "GPS + Other sources").value!!
    setting.mapType = dataStore.getMapType.collectAsState(initial = "Normal").value!!
    setting.language = dataStore.getLanguageSetting.collectAsState(initial = "English").value!!


    return setting

}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingScreen(navController: NavController, navBackStackEntry: NavBackStackEntry, viewModel: GeoImageViewModel) {

    var setting = Setting()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StorePreference(context)



    var defaultGps = dataStore.getGPSMethod.collectAsState(initial = "GPS + Other sources")
    var defaultMap = dataStore.getMapType.collectAsState(initial = "Normal")
    var defaultLang = dataStore.getLanguageSetting.collectAsState(initial = "English")


    if (defaultGps.value == "" && defaultMap.value == "" && defaultLang.value == "") {
        scope.launch{
            dataStore.saveGPSMethod("GPS + Other sources")
            dataStore.saveMapType("Normal")
            dataStore.saveLanguageSetting("English")
        }
    }
    Column() {
        SettingHeader()
        AlertDialogSetting(icon = R.drawable.ic_location,
            mainText = stringResource(R.string.GPS),
            default = defaultGps.value!!,
            radioOptions = listOf(stringResource(id = R.string.GPS_other), stringResource(id = R.string.GPS_only))){
            scope.launch {
                dataStore.saveGPSMethod(it)
            }
        }
        AlertDialogSetting(icon = R.drawable.ic_map,
            mainText = stringResource(R.string.map_type),
            default = defaultMap.value!!,
            radioOptions = listOf(stringResource(R.string.map_normal),
            stringResource(R.string.map_satellite),
            stringResource(R.string.map_terrain),
            stringResource(R.string.map_hybrid)
        )){
            scope.launch {
                dataStore.saveMapType(it)
            }

        }
        AlertDialogSetting(icon = R.drawable.ic_language,
            mainText = stringResource(R.string.language),
            default = defaultLang.value!!,
            radioOptions = listOf(stringResource(R.string.lang_En), stringResource(R.string.lang_ch))){
            scope.launch {
                dataStore.saveLanguageSetting(it)
            }

        }

    }
    getSettings(setting)

}

    @Composable
    fun SettingHeader() {
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    Text(
                        text = stringResource(R.string.app_navi_setting),
                        color = Black,
                        fontSize = FABText_SizeOfHeader,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        }
    }

    @ExperimentalMaterialApi
    @Composable
    fun SettingItem(icon: Int, mainText: String, onClick: () -> Unit) {
        Card(
            onClick = { onClick() },
            modifier = Modifier
                .padding(bottom = ButtonWithIcon_SpaceBetween)
                .fillMaxWidth(),
            elevation = 0.dp,
        ) {
            Row(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)

                    ) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "",
                            modifier = Modifier.padding(ButtonWithIcon_SpaceBetween)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Text(
                        text = mainText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "",
                    modifier = Modifier.size(16.dp)
                )

            }
        }
    }


@Composable
fun RadioButton(radioOptions: List<String>, default:String?, saveAction:  (String) -> Unit, ) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf( default ) }
    Column {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                            saveAction(text)
                        }
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = {
                        onOptionSelected(text)
                        saveAction(text)
                    }
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlertDialogSetting(icon: Int, mainText: String, default: String, radioOptions: List<String>, saveAction: (String) -> Unit) {
    MaterialTheme {
        Column {
            val openDialog = remember { mutableStateOf(false)  }

            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .padding(top = 10.dp)
            ) {
                SettingItem(
                    icon = icon,
                    mainText = mainText,
                    onClick = { openDialog.value = true }
                )
            }

            if (openDialog.value) {
                var selectValue = default;
                AlertDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onCloseRequest.
                        openDialog.value = false
                    },
                    text = {
                        RadioButton(radioOptions, default){
                            selectValue=it
                        }
                    },
                    confirmButton = {
                        TextButton(

                            onClick = {
                                openDialog.value = false
                                saveAction(selectValue)
                            }) {
                            Text("Confirm")
                        }
                    },
                )
            }
        }

    }
}


//Testing Notification function
@Composable
fun TestNotification() {
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedButton(onClick = { val notish = MyNotification(context,  "TESTING", "testing message")
        notish.TestNotification()}) {
            Text(text = "Test Notification", fontSize = 16.sp)
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun SettingPreview() {
    Column() {

    }
}






