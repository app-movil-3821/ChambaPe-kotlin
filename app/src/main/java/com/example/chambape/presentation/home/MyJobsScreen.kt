package com.example.chambape.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chambape.di.AppModule
import com.example.chambape.domain.model.Job
import com.example.chambape.domain.repository.JobAction

private val BackgroundGray = Color(0xFFF8FAFC)
private val ChambaPeBlue   = Color(0xFF0B57D0)
private val TextPrimary    = Color(0xFF1E293B)
private val TextSecondary  = Color(0xFF64748B)

/**
 * Pestaña "Jobs" del contratante: muestra las chambas que publicó y sus estados.
 * Aquí vive ahora el botón "Publicar Chamba" (antes estaba en el HomeFeed).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyJobsScreen(
    onNavigateToCreateJob: () -> Unit,
    onNotificationsClick: (() -> Unit)? = null
) {
    val viewModel: MyJobsViewModel = viewModel(
        factory = MyJobsViewModelFactory(
            AppModule.jobRepository,
            AppModule.shiftRepository,
            AppModule.authRepository,
            AppModule.tokenManager
        )
    )
    val jobs by viewModel.jobs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val actioningId by viewModel.actioningId.collectAsState()
    val applicantsMap by viewModel.applicantsMap.collectAsState()
    val expandedJobIds by viewModel.expandedJobIds.collectAsState()
    val actioningEnrollmentId by viewModel.actioningEnrollmentId.collectAsState()

    // Chamba pendiente de confirmar cancelación (null = sin diálogo abierto)
    var pendingCancelId by remember { mutableStateOf<String?>(null) }

    // Recarga la lista al entrar a la pestaña y al volver de publicar una chamba.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.loadMyJobs()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        containerColor = BackgroundGray,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToCreateJob,
                containerColor = ChambaPeBlue,
                contentColor = Color.White,
                icon = { Icon(Icons.Outlined.WorkOutline, contentDescription = null) },
                text = { Text("Publicar Chamba", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Encabezado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 12.dp, top = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Mis Chambas", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("Trabajos que has publicado", fontSize = 14.sp, color = TextSecondary)
                }
                if (onNotificationsClick != null) {
                    IconButton(onClick = onNotificationsClick) {
                        Icon(Icons.Outlined.NotificationsNone, contentDescription = "Notificaciones", tint = TextPrimary)
                    }
                }
            }

            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ChambaPeBlue)
                    }
                }

                errorMessage != null -> {
                    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(errorMessage ?: "", color = TextSecondary)
                            Spacer(Modifier.height(12.dp))
                            Button(
                                onClick = { viewModel.loadMyJobs() },
                                colors = ButtonDefaults.buttonColors(containerColor = ChambaPeBlue)
                            ) { Text("Reintentar") }
                        }
                    }
                }

                jobs.isEmpty() -> {
                    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            "Aún no has publicado ninguna chamba.\nToca \"Publicar Chamba\" para empezar.",
                            color = TextSecondary,
                            fontSize = 15.sp
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(jobs, key = { it.id }) { job ->
                            MyJobCard(
                                job = job,
                                isActioning = actioningId == job.id,
                                isExpanded = job.id in expandedJobIds,
                                applicants = applicantsMap[job.id],
                                actioningEnrollmentId = actioningEnrollmentId,
                                onToggleApplicants = { viewModel.toggleApplicants(job.id) },
                                onEnrollmentAction = { enrollmentId, accept ->
                                    viewModel.onEnrollmentAction(job.id, enrollmentId, accept)
                                },
                                onAction = { action ->
                                    if (action == JobAction.CANCEL) {
                                        pendingCancelId = job.id
                                    } else {
                                        viewModel.onJobAction(job.id, action)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Confirmación antes de cancelar (acción destructiva)
    if (pendingCancelId != null) {
        AlertDialog(
            onDismissRequest = { pendingCancelId = null },
            title = { Text("Cancelar chamba") },
            text = { Text("¿Seguro que quieres cancelar esta chamba? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    pendingCancelId?.let { viewModel.onJobAction(it, JobAction.CANCEL) }
                    pendingCancelId = null
                }) { Text("Sí, cancelar", color = Color(0xFFD93025)) }
            },
            dismissButton = {
                TextButton(onClick = { pendingCancelId = null }) { Text("Volver") }
            }
        )
    }
}

@Composable
private fun MyJobCard(
    job: Job,
    isActioning: Boolean,
    isExpanded: Boolean,
    applicants: List<ApplicantUi>?,
    actioningEnrollmentId: String?,
    onToggleApplicants: () -> Unit,
    onEnrollmentAction: (enrollmentId: String, accept: Boolean) -> Unit,
    onAction: (JobAction) -> Unit
) {
    val (statusLabel, statusColor, statusBg) = jobStatusStyle(job.status)
    val actions = actionsForStatus(job.status)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            // ─── Encabezado: título + badge de estado ───
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = job.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .background(statusBg, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(statusLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = statusColor)
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = job.district.ifBlank { job.address }.ifBlank { "Sin ubicación" },
                fontSize = 13.sp,
                color = TextSecondary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "S/ ${job.paymentAmount}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = ChambaPeBlue
            )

            // ─── Desplegable de postulantes (solo en estado PUBLISHED) ───
            if (job.status == "PUBLISHED") {
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFE2E8F0))
                Spacer(Modifier.height(8.dp))

                // Botón para abrir/cerrar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isExpanded) "Ocultar postulantes" else "Ver postulantes",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ChambaPeBlue
                    )
                    IconButton(
                        onClick = onToggleApplicants,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded)
                                androidx.compose.material.icons.Icons.Default.KeyboardArrowUp
                            else
                                androidx.compose.material.icons.Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = ChambaPeBlue
                        )
                    }
                }

                // Contenido del desplegable
                if (isExpanded) {
                    when {
                        applicants == null -> {
                            // Cargando
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = ChambaPeBlue
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Cargando postulantes...", fontSize = 13.sp, color = TextSecondary)
                            }
                        }
                        applicants.isEmpty() -> {
                            Text(
                                "Aún no hay postulantes para esta chamba.",
                                fontSize = 13.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        else -> {
                            Spacer(Modifier.height(4.dp))
                            applicants.forEach { applicant ->
                                ApplicantRow(
                                    applicant = applicant,
                                    isActioning = actioningEnrollmentId == applicant.enrollmentId,
                                    onAccept = { onEnrollmentAction(applicant.enrollmentId, true) },
                                    onReject = { onEnrollmentAction(applicant.enrollmentId, false) }
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            // ─── Acciones de gestión válidas para el estado actual ───
            if (actions.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                if (isActioning) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = ChambaPeBlue)
                        Spacer(Modifier.width(8.dp))
                        Text("Actualizando...", fontSize = 13.sp, color = TextSecondary)
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        actions.forEach { ui ->
                            if (ui.destructive) {
                                OutlinedButton(
                                    onClick = { onAction(ui.action) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD93025))
                                ) { Text(ui.label) }
                            } else {
                                Button(
                                    onClick = { onAction(ui.action) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ChambaPeBlue)
                                ) { Text(ui.label, color = Color.White) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ApplicantRow(
    applicant: ApplicantUi,
    isActioning: Boolean,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar inicial
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(ChambaPeBlue.copy(alpha = 0.12f), RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = applicant.workerName.firstOrNull()?.uppercase() ?: "?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ChambaPeBlue
            )
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text = applicant.workerName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        if (isActioning) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = ChambaPeBlue)
        } else {
            // Botón Aceptar
            Button(
                onClick = onAccept,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) { Text("Aceptar", fontSize = 12.sp, color = Color.White) }
            Spacer(Modifier.width(6.dp))
            // Botón Rechazar
            OutlinedButton(
                onClick = onReject,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD93025)),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) { Text("Rechazar", fontSize = 12.sp) }
        }
    }
}

/** Acción mostrable en la tarjeta: etiqueta + transición + si es destructiva. */
private data class JobActionUi(val label: String, val action: JobAction, val destructive: Boolean = false)

/** Devuelve solo las acciones válidas según las guardas del backend para cada estado. */
private fun actionsForStatus(status: String): List<JobActionUi> = when (status) {
    "DRAFT"       -> listOf(JobActionUi("Publicar", JobAction.PUBLISH), JobActionUi("Cancelar", JobAction.CANCEL, true))
    "PUBLISHED"   -> listOf(JobActionUi("Cancelar", JobAction.CANCEL, true))
    "MATCHED"     -> listOf(JobActionUi("Iniciar", JobAction.START), JobActionUi("Cancelar", JobAction.CANCEL, true))
    "IN_PROGRESS" -> listOf(JobActionUi("Completar", JobAction.COMPLETE), JobActionUi("Cancelar", JobAction.CANCEL, true))
    "COMPLETED"   -> listOf(JobActionUi("Cerrar", JobAction.CLOSE))
    "CLOSED"      -> listOf(JobActionUi("Reabrir", JobAction.REOPEN))
    "REOPENED"    -> listOf(JobActionUi("Publicar", JobAction.PUBLISH), JobActionUi("Cancelar", JobAction.CANCEL, true))
    "CANCELLED"   -> listOf(JobActionUi("Reabrir", JobAction.REOPEN))
    else          -> emptyList()
}

/** Estilo (etiqueta, color de texto, color de fondo) para cada estado de un Job. */
private fun jobStatusStyle(status: String): Triple<String, Color, Color> = when (status) {
    "DRAFT"       -> Triple("Borrador",    Color(0xFF6B6B6B), Color(0xFFF0F0F0))
    "PUBLISHED"   -> Triple("Publicado",   Color(0xFF1A3FD8), Color(0xFFE8EDFF))
    "MATCHED"     -> Triple("Asignado",    Color(0xFF1A3FD8), Color(0xFFE8EDFF))
    "IN_PROGRESS" -> Triple("En progreso", Color(0xFF7B3F00), Color(0xFFFFEDD5))
    "COMPLETED"   -> Triple("Completado",  Color(0xFF2E7D32), Color(0xFFE8F5E9))
    "CLOSED"      -> Triple("Cerrado",     Color(0xFF6B6B6B), Color(0xFFF0F0F0))
    "REOPENED"    -> Triple("Reabierto",   Color(0xFF1A3FD8), Color(0xFFE8EDFF))
    "CANCELLED"   -> Triple("Cancelado",   Color(0xFFD93025), Color(0xFFFFEBEA))
    else          -> Triple(status,        Color(0xFF6B6B6B), Color(0xFFF0F0F0))
}