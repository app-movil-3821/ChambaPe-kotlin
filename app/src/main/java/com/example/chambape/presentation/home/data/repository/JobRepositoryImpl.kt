package com.example.chambape.presentation.home.data.repository

import com.example.chambape.presentation.home.data.remote.JobService
import com.example.chambape.presentation.home.domain.model.Job
import com.example.chambape.presentation.home.domain.repository.JobRepository

class JobRepositoryImpl(
    val jobService: JobService
): JobRepository {

    override suspend fun getJobs(): List<Job> {

        // Queremos ver si el app explota, eso nos dará el error real en el Logcat.
        val response = jobService.getJobs()

        if (response.isSuccessful) {
            return response.body()?.map { dto ->
                Job(
                    id = dto.id,
                    title = dto.title,
                    description = dto.description,
                    paymentAmount = dto.paymentAmount,
                    district = dto.location.district
                )
            } ?: emptyList()
        } else {
            // SI LLEGA AQUÍ, IMPRIME EL ERROR EN EL LOGCAT
            println("ERROR DE BACKEND: ${response.code()} - ${response.errorBody()?.string()}")
            return emptyList()
        }
    }

    // Datos estáticos de respaldo idénticos para que la app nunca falle en la entrega
    private fun getMockJobs(): List<Job> {
        return listOf(
            Job("6a244b693f37e57bb4361c73", "Apoyo en cafetería por la tarde", "Se necesita apoyo para atención al cliente, limpieza ligera y orden del local.", 60.0, "Av. Larco 450"),
            Job("2", "Ayudante de Cocina", "Apoyo en el área de producción, lavado de vajilla y organización de insumos médicos alimenticios.", 50.0, "San Miguel"),
            Job("3", "Mozo de Almacén", "Control de inventarios, empaque de productos y carga de cajas ligeras.", 70.0, "Surco")
        )
    }
}