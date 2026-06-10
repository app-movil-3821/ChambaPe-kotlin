package com.example.chambape.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chambape.di.AppModule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJobScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val viewModel: CreateJobViewModel = viewModel(
        factory = CreateJobViewModelFactory(AppModule.jobRepository, AppModule.tokenManager)
    )
    val uiState by viewModel.uiState.collectAsState()

    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var payment by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val isFormValid = title.isNotBlank() && category.isNotBlank() &&
            payment.isNotBlank() && description.isNotBlank()

    LaunchedEffect(uiState) {
        if (uiState is CreateJobUiState.Success) {
            onSuccess()
            viewModel.resetState()
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Publicar Turno", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Detalles de la Chamba",
                fontSize = 16.sp,
                color = Color(0xFF64748B),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Campos del formulario
            OutlinedTextField(
                value = title, onValueChange = { title = it },
                label = { Text("Título (ej: Apoyo en almacén)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = category, onValueChange = { category = it },
                label = { Text("Categoría (ej: Limpieza, Carga)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = district, onValueChange = { district = it },
                    label = { Text("Distrito") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = payment, onValueChange = { payment = it },
                    label = { Text("Pago S/") },
                    modifier = Modifier.weight(0.7f),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = description, onValueChange = { description = it },
                label = { Text("Descripción del puesto") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 4
            )

            if (uiState is CreateJobUiState.Error) {
                Text(
                    text = (uiState as CreateJobUiState.Error).message,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { viewModel.publishJob(title, description, category, payment, district) },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B57D0)),
                enabled = isFormValid && uiState !is CreateJobUiState.Loading
            ) {
                if (uiState is CreateJobUiState.Loading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Publicar Ahora", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}