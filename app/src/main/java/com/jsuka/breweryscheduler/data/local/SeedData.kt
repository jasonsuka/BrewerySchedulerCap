package com.jsuka.breweryscheduler.data.local

import com.jsuka.breweryscheduler.data.local.entity.AvailabilityEntity
import com.jsuka.breweryscheduler.data.local.entity.CredentialEntity
import com.jsuka.breweryscheduler.data.local.entity.DepartmentEntity
import com.jsuka.breweryscheduler.data.local.entity.JobPositionEntity
import com.jsuka.breweryscheduler.data.local.entity.PersonEntity
import com.jsuka.breweryscheduler.data.local.entity.ShiftEntity
import com.jsuka.breweryscheduler.data.local.entity.UserAccountEntity
import com.jsuka.breweryscheduler.domain.model.CredentialCodes
import com.jsuka.breweryscheduler.domain.model.PersonRole
import com.jsuka.breweryscheduler.security.PasswordHasher
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * Populates the database on first launch with the four brewery departments,
 * their eight positions, twelve employees, one supervisor and a week of
 * shifts.
 *
 * Credential dates are anchored to the current date on purpose. Two fork
 * truck certifications sit on either side of today so the boundary pair in
 * the Module 6 test plan is always live: one expires today and must pass,
 * one expired yesterday and must fail.
 */
object SeedData {

    private const val DAY_START = 6 * 60      // 06:00
    private const val DAY_END = 14 * 60       // 14:00
    private const val SWING_START = 14 * 60   // 14:00
    private const val SWING_END = 22 * 60     // 22:00

    suspend fun populate(db: AppDatabase, today: LocalDate = LocalDate.now()) {
        val departmentDao = db.departmentDao()
        val employeeDao = db.employeeDao()
        val qualificationDao = db.qualificationDao()
        val scheduleDao = db.scheduleDao()
        val accountDao = db.userAccountDao()

        if (departmentDao.count() > 0) return

        // ---- Departments. Minimum staffing of 2 applies to all four. ----
        val warehouseId = departmentDao.insert(DepartmentEntity(name = "Warehouse", minimumStaffing = 2))
        val productionId = departmentDao.insert(DepartmentEntity(name = "Production", minimumStaffing = 2))
        val hotSideId = departmentDao.insert(DepartmentEntity(name = "Hot Side Brewing", minimumStaffing = 2))
        val coldSideId = departmentDao.insert(DepartmentEntity(name = "Cold Side Brewing", minimumStaffing = 2))

        // ---- Positions, two per department. ----
        departmentDao.insertPosition(JobPositionEntity(departmentId = warehouseId, title = "Palletizer Operator", requiredCredentialCode = CredentialCodes.PALLETIZER))
        departmentDao.insertPosition(JobPositionEntity(departmentId = warehouseId, title = "Fork Truck Operator", requiredCredentialCode = CredentialCodes.FORK_TRUCK))
        departmentDao.insertPosition(JobPositionEntity(departmentId = productionId, title = "Filler Operator", requiredCredentialCode = CredentialCodes.FILLER))
        departmentDao.insertPosition(JobPositionEntity(departmentId = productionId, title = "Packer Operator", requiredCredentialCode = CredentialCodes.PACKER))
        departmentDao.insertPosition(JobPositionEntity(departmentId = hotSideId, title = "Brewhouse Operator", requiredCredentialCode = CredentialCodes.BREWHOUSE))
        departmentDao.insertPosition(JobPositionEntity(departmentId = hotSideId, title = "Fermenting Operator", requiredCredentialCode = CredentialCodes.FERMENTING))
        departmentDao.insertPosition(JobPositionEntity(departmentId = coldSideId, title = "Filter Operator", requiredCredentialCode = CredentialCodes.FILTER))
        departmentDao.insertPosition(JobPositionEntity(departmentId = coldSideId, title = "Bright Beer Operator", requiredCredentialCode = CredentialCodes.BRIGHT_BEER))

        val hireDate = today.minusYears(2)

        // ---- Supervisor ----
        val supervisorId = employeeDao.insert(
            PersonEntity(
                firstName = "Dana",
                lastName = "Whitfield",
                email = "dwhitfield@example-brewery.com",
                hireDate = today.minusYears(6),
                role = PersonRole.SUPERVISOR,
                homeDepartmentId = null
            )
        )

        // ---- Twelve employees, three per department ----
        data class Seed(
            val first: String,
            val last: String,
            val department: Long,
            val codes: List<String>
        )

        val seeds = listOf(
            // Warehouse. Two hold a fork truck certification on either side
            // of today's date, which drives the Module 6 boundary pair.
            Seed("Dylan", "Reyes", warehouseId, listOf(CredentialCodes.PALLETIZER, CredentialCodes.FORK_TRUCK)),
            Seed("Marcus", "Webb", warehouseId, listOf(CredentialCodes.PALLETIZER, CredentialCodes.FORK_TRUCK)),
            Seed("Tasha", "Nolan", warehouseId, listOf(CredentialCodes.PALLETIZER)),

            Seed("Joel", "Brandt", productionId, listOf(CredentialCodes.FILLER, CredentialCodes.PACKER)),
            Seed("Priya", "Raman", productionId, listOf(CredentialCodes.FILLER)),
            Seed("Curtis", "Hale", productionId, listOf(CredentialCodes.PACKER)),

            Seed("Andre", "Cole", hotSideId, listOf(CredentialCodes.BREWHOUSE, CredentialCodes.FERMENTING)),
            Seed("Sofia", "Mirek", hotSideId, listOf(CredentialCodes.BREWHOUSE)),
            Seed("Reed", "Latham", hotSideId, listOf(CredentialCodes.FERMENTING)),

            Seed("Nina", "Alvarez", coldSideId, listOf(CredentialCodes.FILTER, CredentialCodes.BRIGHT_BEER)),
            Seed("Grant", "Whitley", coldSideId, listOf(CredentialCodes.FILTER)),
            Seed("Lena", "Marsh", coldSideId, listOf(CredentialCodes.BRIGHT_BEER))
        )

        seeds.forEachIndexed { index, seed ->
            val personId = employeeDao.insert(
                PersonEntity(
                    firstName = seed.first,
                    lastName = seed.last,
                    email = "${seed.first.first().lowercase()}${seed.last.lowercase()}@example-brewery.com",
                    hireDate = hireDate.plusDays(index * 30L),
                    role = PersonRole.EMPLOYEE,
                    homeDepartmentId = seed.department
                )
            )

            seed.codes.forEach { code ->
                val isCert = code == CredentialCodes.FORK_TRUCK
                val expiration = when {
                    !isCert -> null
                    // Dylan Reyes: expires today, must pass at the boundary.
                    seed.last == "Reyes" -> today
                    // Marcus Webb: expired yesterday, must fail.
                    seed.last == "Webb" -> today.minusDays(1)
                    else -> today.plusMonths(6)
                }
                qualificationDao.insert(
                    CredentialEntity(
                        personId = personId,
                        code = code,
                        name = displayNameFor(code),
                        issuingAuthority = if (isCert) "State Occupational Safety Board" else "Internal Training",
                        earnedDate = hireDate.plusDays(index * 30L + 14),
                        isCertification = isCert,
                        expirationDate = expiration,
                        renewalRequired = isCert
                    )
                )
            }

            // Availability. Everyone works weekday days except Priya Raman,
            // who is on swing only, so AvailabilityRule has a real failure
            // case to catch on a day shift.
            val onSwing = seed.last == "Raman"
            DayOfWeek.values().take(5).forEach { day ->
                employeeDao.insertAvailability(
                    AvailabilityEntity(
                        personId = personId,
                        dayOfWeek = day.value,
                        startMinute = if (onSwing) SWING_START else DAY_START,
                        endMinute = if (onSwing) SWING_END else DAY_END
                    )
                )
            }
        }

        // ---- A week of day shifts for each department ----
        val monday = today.with(DayOfWeek.MONDAY)
        listOf(warehouseId, productionId, hotSideId, coldSideId).forEach { deptId ->
            (0..4).forEach { offset ->
                scheduleDao.insertShift(
                    ShiftEntity(
                        departmentId = deptId,
                        date = monday.plusDays(offset.toLong()),
                        startMinute = DAY_START,
                        endMinute = DAY_END,
                        label = "Day"
                    )
                )
            }
        }

        // ---- Accounts. Demo passwords only; hashed, never stored plain. ----
        insertAccount(accountDao, supervisorId, "dwhitfield", "Supervisor#1", PersonRole.SUPERVISOR)
        val firstEmployee = employeeDao.findAll().first { it.role == PersonRole.EMPLOYEE }
        insertAccount(accountDao, firstEmployee.id, "employee", "Employee#1", PersonRole.EMPLOYEE)
    }

    private suspend fun insertAccount(
        dao: com.jsuka.breweryscheduler.data.local.dao.UserAccountDao,
        personId: Long,
        username: String,
        password: String,
        role: PersonRole
    ) {
        val salt = PasswordHasher.newSalt()
        dao.insert(
            UserAccountEntity(
                personId = personId,
                username = username,
                passwordSalt = salt,
                passwordHash = PasswordHasher.hash(password, salt),
                role = role
            )
        )
    }

    private fun displayNameFor(code: String): String = when (code) {
        CredentialCodes.PALLETIZER -> "Palletizer Qualification"
        CredentialCodes.FORK_TRUCK -> "Fork Truck Certification"
        CredentialCodes.FILLER -> "Filler Qualification"
        CredentialCodes.PACKER -> "Packer Qualification"
        CredentialCodes.BREWHOUSE -> "Brewhouse Qualification"
        CredentialCodes.FERMENTING -> "Fermenting Qualification"
        CredentialCodes.FILTER -> "Filter Qualification"
        CredentialCodes.BRIGHT_BEER -> "Bright Beer Qualification"
        else -> code
    }
}
